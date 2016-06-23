package com.musicforall.web;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CsrfControllerAdvice {

    @ModelAttribute("_csrf")
    public CsrfToken appendCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("org.springframework.security.web.csrf.CsrfToken");
    }
}
