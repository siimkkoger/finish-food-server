package com.ffreaky.apigw.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class HideCredentialsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String password = httpRequest.getParameter("password");

        // Hide the password from the logs.
        if (password != null) {
            password = "***";
        }

        // Set the password back in the request.
        httpRequest.setAttribute("password", password);
        chain.doFilter(request, response);
    }
}
