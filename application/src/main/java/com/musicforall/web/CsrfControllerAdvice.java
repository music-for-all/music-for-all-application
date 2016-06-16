package com.musicforall.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CsrfControllerAdvice {
	@Autowired
	private HttpServletRequest request;

	@ModelAttribute("_csrf")
	public CsrfToken appendCsrfToken() {
		return (CsrfToken) request.getAttribute("org.springframework.security.web.csrf.CsrfToken");
	}
}
