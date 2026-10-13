package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.entities.Admin;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AdminServices;
import com.example.demo.services.UserServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTests {

	@Autowired private MockMvc mvc;
	@Autowired private AdminServices adminServices;
	@Autowired private UserServices userServices;
	@Autowired private AdminRepository adminRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private ProductRepository productRepository;

	@BeforeEach
	void seed() {
		userRepository.deleteAll();
		adminRepository.deleteAll();

		Admin admin = new Admin();
		admin.setAdminName("Root");
		admin.setAdminEmail("root@foodfiesta.com");
		admin.setAdminPassword("adminpass");
		admin.setAdminNumber("9999999999");
		adminServices.addAdmin(admin);

		User user = new User();
		user.setUname("Asha");
		user.setUemail("asha@example.com");
		user.setUpassword("userpass");
		user.setUnumber(8888888888L);
		userServices.addUser(user);
	}

	// ---- Item 2: passwords are hashed, never stored as given ----

	@Test
	void passwordsAreStoredAsBcryptHashes() {
		Admin stored = adminRepository.findByAdminEmail("root@foodfiesta.com");
		assertThat(stored.getAdminPassword()).isNotEqualTo("adminpass").startsWith("$2");

		User user = userRepository.findUserByUemail("asha@example.com");
		assertThat(user.getUpassword()).isNotEqualTo("userpass").startsWith("$2");
	}

	@Test
	void correctCredentialsAuthenticate() throws Exception {
		mvc.perform(formLogin("/perform-login")
				.userParameter("email").user("root@foodfiesta.com")
				.passwordParam("password").password("adminpass"))
			.andExpect(authenticated().withRoles("ADMIN"));
	}

	@Test
	void wrongPasswordIsRejected() throws Exception {
		mvc.perform(formLogin("/perform-login")
				.userParameter("email").user("root@foodfiesta.com")
				.passwordParam("password").password("not-the-password"))
			.andExpect(unauthenticated());
	}

	// ---- Item 4: /admin/** requires the ADMIN role ----

	@Test
	void dashboardRedirectsAnonymousToLogin() throws Exception {
		mvc.perform(get("/admin/services"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	void dashboardForbiddenForPlainUser() throws Exception {
		mvc.perform(get("/admin/services").with(user("asha@example.com").roles("USER")))
			.andExpect(status().isForbidden());
	}

	@Test
	void dashboardAllowedForAdmin() throws Exception {
		mvc.perform(get("/admin/services").with(user("root@foodfiesta.com").roles("ADMIN")))
			.andExpect(status().isOk());
	}

	@Test
	void publicPagesStayOpen() throws Exception {
		mvc.perform(get("/home")).andExpect(status().isOk());
		mvc.perform(get("/products")).andExpect(status().isOk());
		mvc.perform(get("/login")).andExpect(status().isOk());
		mvc.perform(get("/register")).andExpect(status().isOk());
	}

	// ---- Item 3: mutations are POST-only and CSRF-protected ----

	@Test
	void deleteViaGetNoLongerExists() throws Exception {
		int id = userRepository.findUserByUemail("asha@example.com").getU_id();
		mvc.perform(get("/deleteUser/" + id).with(user("root").roles("ADMIN")))
			.andExpect(status().isMethodNotAllowed());
		assertThat(userRepository.findUserByUemail("asha@example.com")).isNotNull();
	}

	@Test
	void deleteWithoutCsrfTokenIsRejected() throws Exception {
		int id = userRepository.findUserByUemail("asha@example.com").getU_id();
		mvc.perform(post("/deleteUser/" + id).with(user("root").roles("ADMIN")))
			.andExpect(status().isForbidden());
		assertThat(userRepository.findUserByUemail("asha@example.com")).isNotNull();
	}

	@Test
	void deleteWithCsrfTokenSucceeds() throws Exception {
		int id = userRepository.findUserByUemail("asha@example.com").getU_id();
		mvc.perform(post("/deleteUser/" + id).with(user("root").roles("ADMIN")).with(csrf()))
			.andExpect(status().is3xxRedirection());
		assertThat(userRepository.findUserByUemail("asha@example.com")).isNull();
	}

	// ---- Item 6: a missing row renders 404, not a 500 ----

	@Test
	void missingRecordRendersNotFound() throws Exception {
		mvc.perform(get("/updateUser/999999").with(user("root").roles("ADMIN")))
			.andExpect(status().isNotFound());
	}

	@Test
	void productSeedingRan() {
		assertThat(productRepository.count()).isGreaterThan(0);
	}
}
