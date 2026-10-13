package com.example.demo.config;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

	private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

	/** BCrypt hashes always carry one of these version prefixes. */
	private static boolean isBcrypt(String value) {
		return value != null
				&& (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
	}

	@Bean
	CommandLineRunner initDatabase(ProductRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				Product p1 = new Product();
				p1.setPname("Hyderabadi Chicken Biryani");
				p1.setPprice(350.0);
				p1.setPdescription("Authentic long-grain basmati rice cooked with tender chicken and secret spices.");

				Product p2 = new Product();
				p2.setPname("Paneer Butter Masala");
				p2.setPprice(280.0);
				p2.setPdescription("Rich and creamy cottage cheese cubes served in a velvet tomato gravy.");

				Product p3 = new Product();
				p3.setPname("Chicken Tikka Tandoori");
				p3.setPprice(320.0);
				p3.setPdescription("Smoky charcoal-grilled chicken marinated in yogurt and traditional tandoori tikka masala.");

				Product p4 = new Product();
				p4.setPname("Veg Manchurian");
				p4.setPprice(220.0);
				p4.setPdescription("Savory vegetable balls tossed in a tangy and spicy oriental soy-based sauce.");

				Product p5 = new Product();
				p5.setPname("Gulab Jamun (2pcs)");
				p5.setPprice(120.0);
				p5.setPdescription("Warm, soft milk-solid dumplings dipped in fragrant cardamom-infused sugar syrup.");

				repository.saveAll(List.of(p1, p2, p3, p4, p5));
				log.info("Sample products data seeded into database.");
			}
		};
	}

	/**
	 * One-time migration for rows written before passwords were hashed. Any
	 * credential that is not already a BCrypt hash is encoded in place, so
	 * existing accounts keep working after the switch to Spring Security.
	 */
	@Bean
	CommandLineRunner migratePlaintextPasswords(AdminRepository adminRepository,
			UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			List<Admin> admins = new ArrayList<>();
			for (Admin admin : adminRepository.findAll()) {
				if (!isBcrypt(admin.getAdminPassword()) && admin.getAdminPassword() != null) {
					admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
					admins.add(admin);
				}
			}
			if (!admins.isEmpty()) {
				adminRepository.saveAll(admins);
				log.info("Re-hashed {} admin password(s) that were stored in plaintext.", admins.size());
			}

			List<User> users = new ArrayList<>();
			for (User user : userRepository.findAll()) {
				if (!isBcrypt(user.getUpassword()) && user.getUpassword() != null) {
					user.setUpassword(passwordEncoder.encode(user.getUpassword()));
					users.add(user);
				}
			}
			if (!users.isEmpty()) {
				userRepository.saveAll(users);
				log.info("Re-hashed {} user password(s) that were stored in plaintext.", users.size());
			}
		};
	}

	/**
	 * Seeds the first admin so a fresh database is reachable. Credentials come
	 * from the environment; the dashboard is otherwise unreachable without
	 * inserting a row by hand.
	 */
	@Bean
	CommandLineRunner seedAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder,
			@Value("${foodfiesta.bootstrap-admin.email:}") String email,
			@Value("${foodfiesta.bootstrap-admin.password:}") String password) {
		return args -> {
			if (email.isBlank() || password.isBlank()) {
				log.info("No bootstrap admin configured; set FOODFIESTA_ADMIN_EMAIL and "
						+ "FOODFIESTA_ADMIN_PASSWORD to seed one.");
				return;
			}
			if (adminRepository.findByAdminEmail(email) != null) {
				return;
			}
			Admin admin = new Admin();
			admin.setAdminName("Bootstrap Admin");
			admin.setAdminEmail(email);
			admin.setAdminPassword(passwordEncoder.encode(password));
			admin.setAdminNumber("0000000000");
			adminRepository.save(admin);
			log.info("Bootstrap admin account created for {}", email);
		};
	}
}
