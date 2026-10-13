package com.example.demo.exceptions;

/** Thrown when a lookup by id finds no row. Rendered as a 404 page. */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String resource, Object id) {
		super(resource + " with id " + id + " was not found");
	}
}
