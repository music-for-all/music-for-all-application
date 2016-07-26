package com.musicforall.web;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Puts the CSRF token as a ModelAttribute on every request on every controller.
 */
@ControllerAdvice
public class CsrfControllerAdvice {

//    @ModelAttribute("_csrf")
//    public CsrfToken appendCsrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("org.springframework.security.web.csrf.CsrfToken");
//    }
}
