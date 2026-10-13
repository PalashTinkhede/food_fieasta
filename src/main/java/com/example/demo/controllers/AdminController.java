package com.example.demo.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.count.Logic;
import com.example.demo.entities.Admin;
import com.example.demo.entities.Orders;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.AdminServices;
import com.example.demo.services.OrderServices;
import com.example.demo.services.ProductServices;
import com.example.demo.services.UserServices;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@Tag(name = "Admin Controller", description = "Back-office and ordering endpoints")
public class AdminController {

	private final UserServices services;
	private final AdminServices adminServices;
	private final ProductServices productServices;
	private final OrderServices orderServices;

	public AdminController(UserServices services, AdminServices adminServices,
			ProductServices productServices, OrderServices orderServices) {
		this.services = services;
		this.adminServices = adminServices;
		this.productServices = productServices;
		this.orderServices = orderServices;
	}

	/**
	 * Resolves the customer for the current request from the authenticated
	 * principal. Previously this came from a controller instance field, which is
	 * shared across every concurrent request on a singleton bean.
	 */
	private User currentUser(Principal principal) {
		if (principal == null) {
			throw new ResourceNotFoundException("Signed-in user", "anonymous");
		}
		User user = this.services.getUserByEmail(principal.getName());
		if (user == null) {
			throw new ResourceNotFoundException("Signed-in user", principal.getName());
		}
		return user;
	}

	@PostMapping("/product/search")
	public String seachHandler(@RequestParam("productName") String name, Principal principal, Model model) {
		User user = currentUser(principal);
		Product product = this.productServices.getProductByName(name);
		if (product == null) {
			model.addAttribute("message", "SORRY...!  Product Unavailable");
		}
		model.addAttribute("product", product);
		model.addAttribute("name", user.getUname());
		model.addAttribute("orders", this.orderServices.getOrdersForUser(user));
		return "BuyProduct";
	}

	@GetMapping("/admin/services")
	public String returnBack(Model model) {
		model.addAttribute("users", this.services.getAllUser());
		model.addAttribute("admins", this.adminServices.getAll());
		model.addAttribute("products", this.productServices.getAllProducts());
		model.addAttribute("orders", this.orderServices.getOrders());
		return "Admin_Page";
	}

	@GetMapping("/addAdmin")
	public String addAdminPage(Model model) {
		model.addAttribute("admin", new Admin());
		return "Add_Admin";
	}

	@PostMapping("/addingAdmin")
	public String addAdmin(@Valid @ModelAttribute("admin") Admin admin, Model model) {
		if (this.adminServices.emailExists(admin.getAdminEmail())) {
			model.addAttribute("error", "That email is already registered to an admin.");
			return "Add_Admin";
		}
		this.adminServices.addAdmin(admin);
		return "redirect:/admin/services";
	}

	@GetMapping("/updateAdmin/{adminId}")
	public String update(@PathVariable("adminId") int id, Model model) {
		model.addAttribute("admin", this.adminServices.getAdmin(id));
		return "Update_Admin";
	}

	@PostMapping("/updatingAdmin/{id}")
	public String updateAdmin(@ModelAttribute Admin admin, @PathVariable("id") int id) {
		this.adminServices.update(admin, id);
		return "redirect:/admin/services";
	}

	@PostMapping("/deleteAdmin/{id}")
	public String deleteAdmin(@PathVariable("id") int id) {
		this.adminServices.delete(id);
		return "redirect:/admin/services";
	}

	@GetMapping("/addProduct")
	public String addProduct(Model model) {
		model.addAttribute("product", new Product());
		return "Add_Product";
	}

	@GetMapping("/updateProduct/{productId}")
	public String updateProduct(@PathVariable("productId") int id, Model model) {
		model.addAttribute("product", this.productServices.getProduct(id));
		return "Update_Product";
	}

	@GetMapping("/addUser")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "Add_User";
	}

	@GetMapping("/updateUser/{userId}")
	public String updateUserPage(@PathVariable("userId") int id, Model model) {
		model.addAttribute("user", this.services.getUser(id));
		return "Update_User";
	}

	@PostMapping("/product/order")
	public String orderHandler(@ModelAttribute Orders order, Principal principal, Model model) {
		User user = currentUser(principal);
		double totalAmount = Logic.countTotal(order.getoPrice(), order.getoQuantity());
		order.setTotalAmmout(totalAmount);
		order.setUser(user);
		order.setOrderDate(new Date());
		this.orderServices.saveOrder(order);
		model.addAttribute("amount", totalAmount);
		return "Order_success";
	}

	@GetMapping("/product/back")
	public String back(Principal principal, Model model) {
		User user = currentUser(principal);
		List<Orders> orders = this.orderServices.getOrdersForUser(user);
		model.addAttribute("orders", orders);
		model.addAttribute("name", user.getUname());
		return "BuyProduct";
	}
}
