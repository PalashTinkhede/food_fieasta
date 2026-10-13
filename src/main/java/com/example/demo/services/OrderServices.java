package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.OrderRepository;

@Service
public class OrderServices {

	private final OrderRepository orderRepository;

	public OrderServices(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Orders> getOrders() {
		return this.orderRepository.findAll();
	}

	public void saveOrder(Orders order) {
		this.orderRepository.save(order);
	}

	public void updateOrder(int id, Orders order) {
		if (!this.orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("Order", id);
		}
		order.setoId(id);
		this.orderRepository.save(order);
	}

	public void deleteOrder(int id) {
		if (!this.orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("Order", id);
		}
		this.orderRepository.deleteById(id);
	}

	public List<Orders> getOrdersForUser(User user) {
		return this.orderRepository.findOrdersByUser(user);
	}
}
