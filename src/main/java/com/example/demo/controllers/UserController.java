package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.User;
import com.example.demo.services.UserServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "User Controller", description = "Endpoints for managing user accounts")
public class UserController {

	private final UserServices services;

	public UserController(UserServices services) {
		this.services = services;
	}

	@PostMapping("/addingUser")
	public String addUser(@ModelAttribute("user") User user, Model model) {
		if (this.services.emailExists(user.getUemail())) {
			model.addAttribute("error", "That email is already registered.");
			return "Add_User";
		}
		this.services.addUser(user);
		return "redirect:/admin/services";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("userRegistration") User user, Model model) {
		if (this.services.emailExists(user.getUemail())) {
			model.addAttribute("error", "That email is already registered. Try signing in instead.");
			return "register";
		}
		this.services.addUser(user);
		return "redirect:/login?registered";
	}

	@PostMapping("/updatingUser/{id}")
	public String updateUser(@ModelAttribute User user, @PathVariable("id") int id) {
		this.services.updateUser(user, id);
		return "redirect:/admin/services";
	}

	@PostMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") int id) {
		this.services.deleteUser(id);
		return "redirect:/admin/services";
	}
}
