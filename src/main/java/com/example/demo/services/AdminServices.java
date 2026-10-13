package com.example.demo.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Admin;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.AdminRepository;

@Service
public class AdminServices {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminServices(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Admin> getAll() {
		return (List<Admin>) this.adminRepository.findAll();
	}

	public Admin getAdmin(int id) {
		return this.adminRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Admin", id));
	}

	/** Copies editable fields onto the stored row, preserving the password hash. */
	public void update(Admin admin, int id) {
		Admin existing = getAdmin(id);
		existing.setAdminName(admin.getAdminName());
		existing.setAdminEmail(admin.getAdminEmail());
		existing.setAdminNumber(admin.getAdminNumber());
		if (admin.getAdminPassword() != null && !admin.getAdminPassword().isBlank()) {
			existing.setAdminPassword(this.passwordEncoder.encode(admin.getAdminPassword()));
		}
		this.adminRepository.save(existing);
	}

	public void delete(int id) {
		if (!this.adminRepository.existsById(id)) {
			throw new ResourceNotFoundException("Admin", id);
		}
		this.adminRepository.deleteById(id);
	}

	public void addAdmin(Admin admin) {
		admin.setAdminPassword(this.passwordEncoder.encode(admin.getAdminPassword()));
		this.adminRepository.save(admin);
	}

	public boolean emailExists(String email) {
		return this.adminRepository.findByAdminEmail(email) != null;
	}
}
