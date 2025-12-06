package com.example.productmanagement.controller;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ProductService productService;

    @Autowired
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> all = productService.getAllProducts();

        // Total products count
        long totalProducts = all.size();

        // Products by category (Map<String, Long>)
        Map<String, Long> productsByCategory = all.stream()
                .collect(Collectors.groupingBy(
                        p -> Optional.ofNullable(p.getCategory()).orElse("Uncategorized"),
                        Collectors.counting()
                ));

        // Total inventory value = sum(price * quantity)
        BigDecimal totalInventoryValue = all.stream()
                .filter(p -> p.getPrice() != null && p.getQuantity() != null)
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Average price (ignore null prices). If none exist, default 0.
        List<BigDecimal> prices = all.stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal averagePrice = BigDecimal.ZERO;
        if (!prices.isEmpty()) {
            BigDecimal sumPrices = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            averagePrice = sumPrices.divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);
        }

        // Low stock alerts (quantity < 10)
        List<Product> lowStockProducts = all.stream()
                .filter(p -> p.getQuantity() != null && p.getQuantity() < 10)
                .collect(Collectors.toList());

        // Recent products — sort by createdAt desc and take top 5
        List<Product> recentProducts = all.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Product::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Add to model (names match your dashboard.html)
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("productsByCategory", productsByCategory);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("recentProducts", recentProducts);

        return "dashboard";
    }
}
