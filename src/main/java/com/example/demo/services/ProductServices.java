package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.ProductRepository;

@Service
public class ProductServices {

	private final ProductRepository productRepository;

	public ProductServices(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void addProduct(Product p) {
		this.productRepository.save(p);
	}

	public List<Product> getAllProducts() {
		return (List<Product>) this.productRepository.findAll();
	}

	public Product getProduct(int id) {
		return this.productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
	}

	public void updateproduct(Product p, int id) {
		Product existing = getProduct(id);
		existing.setPname(p.getPname());
		existing.setPprice(p.getPprice());
		existing.setPdescription(p.getPdescription());
		this.productRepository.save(existing);
	}

	public void deleteProduct(int id) {
		if (!this.productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product", id);
		}
		this.productRepository.deleteById(id);
	}

	public Product getProductByName(String name) {
		return this.productRepository.findByPname(name);
	}
}
