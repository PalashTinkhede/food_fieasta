package com.example.demo.config;

import com.example.demo.security.DbUserDetailsService;
import com.example.demo.security.RoleAwareSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(DbUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			RoleAwareSuccessHandler successHandler) throws Exception {
		http
			// CSRF is on by default; every state-changing form posts a token.
			.authorizeHttpRequests(auth -> auth
				// Public brochure pages and static assets.
				.requestMatchers("/", "/home", "/products", "/location", "/about",
						"/login", "/register").permitAll()
				.requestMatchers("/css/**", "/JavaScript/**", "/Images/**", "/error").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

				// Back-office: dashboard plus every admin/user/product mutation.
				.requestMatchers("/admin/**", "/addAdmin", "/addingAdmin", "/updateAdmin/**",
						"/updatingAdmin/**", "/deleteAdmin/**",
						"/addProduct", "/addingProduct", "/updateProduct/**",
						"/updatingProduct/**", "/deleteProduct/**",
						"/addUser", "/addingUser", "/updateUser/**",
						"/updatingUser/**", "/deleteUser/**").hasRole("ADMIN")

				// Customer ordering flow.
				.requestMatchers("/product/**").hasAnyRole("USER", "ADMIN")

				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/perform-login")
				.usernameParameter("email")
				.passwordParameter("password")
				.successHandler(successHandler)
				.failureUrl("/login?error")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
				.logoutSuccessUrl("/home?logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll()
			);

		return http.build();
	}
}
