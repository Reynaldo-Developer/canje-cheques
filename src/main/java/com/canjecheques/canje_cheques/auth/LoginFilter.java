package com.canjecheques.canje_cheques.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        // Permitir libre
        if (path.equals("/") || path.startsWith("/login") || path.startsWith("/css") || path.startsWith("/images") || path.startsWith("/js")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean logged = session != null && session.getAttribute("usuario") != null;

        if (!logged) {
            res.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, res);
    }
}