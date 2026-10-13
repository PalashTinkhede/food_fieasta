package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Guards the fix for the shared-controller-state bug: orders must be attributed
 * to the account that placed them, not to whoever logged in most recently.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderIsolationTests {

	@Autowired private MockMvc mvc;
	@Autowired private UserServices userServices;
	@Autowired private UserRepository userRepository;
	@Autowired private OrderRepository orderRepository;

	@BeforeEach
	void seed() {
		orderRepository.deleteAll();
		userRepository.deleteAll();
		newUser("Asha", "asha@example.com", 1111111111L);
		newUser("Bala", "bala@example.com", 2222222222L);
	}

	private void newUser(String name, String email, long number) {
		User u = new User();
		u.setUname(name);
		u.setUemail(email);
		u.setUpassword("secret");
		u.setUnumber(number);
		userServices.addUser(u);
	}

	private void placeOrder(String email, String item, double price, int qty) throws Exception {
		mvc.perform(post("/product/order")
				.with(user(email).roles("USER")).with(csrf())
				.param("oName", item)
				.param("oPrice", String.valueOf(price))
				.param("oQuantity", String.valueOf(qty)))
			.andExpect(status().isOk());
	}

	@Test
	void eachOrderIsAttributedToTheAccountThatPlacedIt() throws Exception {
		// Interleaved, as two concurrent sessions would be.
		placeOrder("asha@example.com", "Veg Manchurian", 220.0, 2);
		placeOrder("bala@example.com", "Gulab Jamun (2pcs)", 120.0, 1);
		placeOrder("asha@example.com", "Paneer Butter Masala", 280.0, 1);

		User asha = userRepository.findUserByUemail("asha@example.com");
		User bala = userRepository.findUserByUemail("bala@example.com");

		List<Orders> ashaOrders = orderRepository.findOrdersByUser(asha);
		List<Orders> balaOrders = orderRepository.findOrdersByUser(bala);

		assertThat(ashaOrders).hasSize(2)
			.extracting(Orders::getoName)
			.containsExactlyInAnyOrder("Veg Manchurian", "Paneer Butter Masala");

		assertThat(balaOrders).hasSize(1)
			.extracting(Orders::getoName)
			.containsExactly("Gulab Jamun (2pcs)");
	}

	@Test
	void orderTotalIsPriceTimesQuantity() throws Exception {
		placeOrder("asha@example.com", "Veg Manchurian", 220.0, 3);
		User asha = userRepository.findUserByUemail("asha@example.com");
		assertThat(orderRepository.findOrdersByUser(asha).get(0).getTotalAmmout()).isEqualTo(660.0);
	}

	@Test
	void orderingRequiresAuthentication() throws Exception {
		mvc.perform(post("/product/order").with(csrf())
				.param("oName", "Veg Manchurian")
				.param("oPrice", "220.0")
				.param("oQuantity", "1"))
			.andExpect(status().is3xxRedirection());
		assertThat(orderRepository.count()).isZero();
	}
}
