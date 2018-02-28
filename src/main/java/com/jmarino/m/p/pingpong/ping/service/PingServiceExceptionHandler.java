package com.jmarino.m.p.pingpong.ping.service;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PingServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { TaskRejectedException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "En estos momentos no podemos procesar su solicitud. Gracias.";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
	}

}
