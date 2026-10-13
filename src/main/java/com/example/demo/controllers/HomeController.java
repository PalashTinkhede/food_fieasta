package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.User;
import com.example.demo.services.ProductServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Home Controller", description = "Public pages and navigation")
public class HomeController {

	private final ProductServices productServices;

	public HomeController(ProductServices productServices) {
		this.productServices = productServices;
	}

	@GetMapping(value = { "/home", "/" })
	public String home() {
		return "Home";
	}

	@GetMapping("/products")
	public String products(Model model) {
		model.addAttribute("products", this.productServices.getAllProducts());
		return "Products";
	}

	@GetMapping("/location")
	public String location() {
		return "Locate_us";
	}

	@GetMapping("/about")
	public String about() {
		return "About";
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "registered", required = false) String registered,
			Model model) {
		if (error != null) {
			model.addAttribute("error", "Invalid email or password");
		}
		if (registered != null) {
			model.addAttribute("registered", "Account created. Please sign in.");
		}
		return "Login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("userRegistration", new User());
		return "register";
	}
}
