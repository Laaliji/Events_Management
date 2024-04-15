package com.example.metier.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthFilter implements Filter {

    public AuthFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession session = reqt.getSession(false);

            String reqURI = reqt.getRequestURI();

            boolean isAuthPath = reqURI.contains("/login.xhtml") || reqURI.contains("/register.xhtml");
            boolean authCheck = (session != null && session.getAttribute("user") != null);
            boolean isProtectedUrl = reqURI.contains("events") || reqURI.contains("admin");

            // redirect if is auth path and already logged in
            if (isAuthPath && authCheck) {
                resp.sendRedirect(reqt.getContextPath() + "/index.xhtml");
            }

            // redirect if protected path and not auth
            if (isProtectedUrl && !authCheck) {
                resp.sendRedirect(reqt.getContextPath() + "/auth/login.xhtml");
            } else {
                chain.doFilter(request, response);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }

}