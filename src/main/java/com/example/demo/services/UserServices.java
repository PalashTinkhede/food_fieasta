package com.example.demo.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;

@Service
public class UserServices {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServices(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> getAllUser() {
		return (List<User>) this.userRepository.findAll();
	}

	public User getUser(int id) {
		return this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", id));
	}

	public User getUserByEmail(String email) {
		return this.userRepository.findUserByUemail(email);
	}

	/**
	 * Applies the edited fields onto the stored row. The admin edit form has no
	 * password input, so the existing hash is carried over rather than nulled.
	 */
	public void updateUser(User user, int id) {
		User existing = getUser(id);
		existing.setUname(user.getUname());
		existing.setUemail(user.getUemail());
		existing.setUnumber(user.getUnumber());
		if (user.getUpassword() != null && !user.getUpassword().isBlank()) {
			existing.setUpassword(this.passwordEncoder.encode(user.getUpassword()));
		}
		this.userRepository.save(existing);
	}

	public void deleteUser(int id) {
		if (!this.userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User", id);
		}
		this.userRepository.deleteById(id);
	}

	public void addUser(User user) {
		user.setUpassword(this.passwordEncoder.encode(user.getUpassword()));
		this.userRepository.save(user);
	}

	public boolean emailExists(String email) {
		return this.userRepository.findUserByUemail(email) != null;
	}
}
