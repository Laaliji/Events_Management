package com.example.metier.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.metier.entities.User;
import com.example.metier.entities.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// filters are used to ensure the request or the user that
// is performing the request has some specific roles or something else.
//this filter is used for allowing only the admin to access every endpoints starts with /admin
@WebFilter(filterName = "AdminFilter", urlPatterns = { "/admin/*" })
public class AdminFilter implements Filter {

    public AdminFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession session = reqt.getSession(false);

            // Check if the user is logged in and has admin role
            if (session != null && session.getAttribute("user") != null) {
                User loggedUser = (User) session.getAttribute("user");
                if (loggedUser.getRole().equals(Role.ADMIN)) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            // Redirect to error page if not admin
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect(reqt.getContextPath() + "/401.xhtml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }
}
