package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Product;
import com.example.demo.services.ProductServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Product Controller", description = "Endpoints for managing dining products")
public class ProductController {

	private final ProductServices productServices;

	public ProductController(ProductServices productServices) {
		this.productServices = productServices;
	}

	@PostMapping("/addingProduct")
	public String addProduct(@ModelAttribute Product product) {
		this.productServices.addProduct(product);
		return "redirect:/admin/services";
	}

	@PostMapping("/updatingProduct/{productId}")
	public String updateProduct(@ModelAttribute Product product, @PathVariable("productId") int id) {
		this.productServices.updateproduct(product, id);
		return "redirect:/admin/services";
	}

	@PostMapping("/deleteProduct/{productId}")
	public String delete(@PathVariable("productId") int id) {
		this.productServices.deleteProduct(id);
		return "redirect:/admin/services";
	}
}
