package com.example.productmanagement.controller;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // List all products
    // @GetMapping
    // public String listProducts(Model model) {
    //     List<Product> products = productService.getAllProducts();
    //     model.addAttribute("products", products);
    //     return "product-list";  // Returns product-list.html
    // }

    @GetMapping
public String listProducts(
    @RequestParam(required = false) String sortBy,
    @RequestParam(defaultValue = "asc") String sortDir,
    @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String category,
    Model model) {
    
    List<Product> products;
    
    if (sortBy != null) {
        Sort sort = sortDir.equals("asc") ? 
            Sort.by(sortBy).ascending() : 
            Sort.by(sortBy).descending();
        products = productService.getAllProducts(sort);
    } else {
        products = productService.getAllProducts();
    }
    List<String> categories = productService.getAllProducts().stream()
            .map(Product::getCategory)
            .filter(Objects::nonNull)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
   model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    model.addAttribute("selectedCategory", category == null ? "" : category);
    model.addAttribute("keyword", keyword == null ? "" : keyword);
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("sortDir", sortDir);

    
    return "product-list";
}
    
    // Show form for new product
    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }
    
    // Show form for editing product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }
    
    // Save product (create or update)
    // @PostMapping("/save")
    // public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
    //     try {
    //         productService.saveProduct(product);
    //         redirectAttributes.addFlashAttribute("message", 
    //                 product.getId() == null ? "Product added successfully!" : "Product updated successfully!");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", "Error saving product: " + e.getMessage());
    //     }
    //     return "redirect:/products";
    // }
    
@PostMapping("/save")
public String saveProduct(
    @Valid @ModelAttribute("product") Product product,
    BindingResult result,
    Model model,
    RedirectAttributes redirectAttributes) {
    
    if (result.hasErrors()) {
        return "product-form";
    }
    
    try {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
    }
    
    return "redirect:/products";
}

    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }
    
    // Search products
    // @GetMapping("/search")
    // public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
    //     List<Product> products = productService.searchProducts(keyword);
    //     model.addAttribute("products", products);
    //     model.addAttribute("keyword", keyword);
    //     return "product-list";
    // }

    @GetMapping("/search")
public String searchProducts(
    @RequestParam("keyword") String keyword,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    Model model) {
    
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> productPage = productService.searchProducts(keyword, pageable);
    
    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("categories", productService.findAllCategories());
    model.addAttribute("selectedCategory", ""); 
    return "product-list";
}
    @GetMapping("/advanced-search")
public String advancedSearch(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        Model model) {
    List<Product> products = productService.advancedSearchProducts(name, category, minPrice, maxPrice);

    List<String> categories = productService.findAllCategories();

    model.addAttribute("products", products);
    model.addAttribute("name", name);
    model.addAttribute("category", category);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
    model.addAttribute("categories", categories);
    model.addAttribute("selectedCategory", category);

    return "product-list";
}



}
