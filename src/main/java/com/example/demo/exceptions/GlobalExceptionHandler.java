package com.example.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleNotFound(ResourceNotFoundException ex, Model model) {
		log.warn("Resource not found: {}", ex.getMessage());
		model.addAttribute("message", ex.getMessage());
		return "not_found";
	}

	/**
	 * Spring's own web exceptions (405, 415, malformed body, ...) already carry a
	 * meaningful status. Preserve it instead of flattening everything to a 500,
	 * which is what the previous catch-all handler did.
	 */
	@ExceptionHandler({
			HttpRequestMethodNotSupportedException.class,
			HttpMediaTypeException.class,
			MissingRequestValueException.class,
			NoHandlerFoundException.class,
			ErrorResponseException.class })
	public ModelAndView handleErrorResponse(ErrorResponse ex) {
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		log.warn("Request failed with {}: {}", status, ex.getBody().getDetail());

		ModelAndView mav = new ModelAndView(status == HttpStatus.NOT_FOUND ? "not_found" : "exception");
		mav.addObject("message", ex.getBody().getDetail());
		mav.setStatus(status);
		return mav;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String handleUnexpected(Exception ex, Model model) {
		// Log the real cause; the previous handler discarded it entirely.
		log.error("Unhandled exception while serving request", ex);
		model.addAttribute("message", ex.getMessage());
		return "exception";
	}
}
