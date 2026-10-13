package com.example.demo.security;

import com.example.demo.entities.Admin;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Resolves a login email against both the admin and the customer tables.
 * Admins win when the same address exists in both, so an operator never
 * gets silently downgraded to customer privileges.
 */
@Service
public class DbUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;
	private final UserRepository userRepository;

	public DbUserDetailsService(AdminRepository adminRepository, UserRepository userRepository) {
		this.adminRepository = adminRepository;
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = this.adminRepository.findByAdminEmail(email);
		if (admin != null && admin.getAdminPassword() != null) {
			return org.springframework.security.core.userdetails.User
					.withUsername(admin.getAdminEmail())
					.password(admin.getAdminPassword())
					.roles("ADMIN")
					.build();
		}

		User user = this.userRepository.findUserByUemail(email);
		if (user != null && user.getUpassword() != null) {
			return org.springframework.security.core.userdetails.User
					.withUsername(user.getUemail())
					.password(user.getUpassword())
					.roles("USER")
					.build();
		}

		throw new UsernameNotFoundException("No account registered for " + email);
	}
}
