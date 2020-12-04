package nl.smith.mathematics.controller;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * This method <description of functionality>
 *
 * @author m.smithhva.nl
 */
public class RequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        Cookie cookie = null;
        //TODO Iterate over options see configuration constants
        if (cookies != null) {
            cookie = Arrays.stream(cookies).filter(c -> "qwerty".equals(c.getName())).findFirst().orElse(null);
        }
        if (cookie == null) {

            cookie = new Cookie(RationalNumberOutputType.class.getSimpleName(), RationalNumberOutputType.get().name());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(365 * 24 * 60 * 60);
            LOGGER.info("Created cookie");
            response.addCookie(cookie);
            response.addHeader("Osama", "Bin Laden");
        } else {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        response.addHeader("Osama", "Bin Ladennnnnn");
        chain.doFilter(request, response);
    }

}
