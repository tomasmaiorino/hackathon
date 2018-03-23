package com.skip.hackathon.security;

import static com.skip.hackathon.security.SecurityConstants.HEADER_STRING;
import static com.skip.hackathon.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.service.CustomerService;
import com.skip.hackathon.service.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		String userEmail = null;
		String authToken = null;
		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			try {
				userEmail = jwtTokenUtil.getUserEmailFromToken(authToken);
			} catch (IllegalArgumentException e) {
				log.error("An error occured during getting username from token.", e);
			} catch (ExpiredJwtException e) {
				log.warn("The token is expired and not valid anymore.", e);
			} catch (MalformedJwtException e) {
				log.warn(String.format("The token is malformed [%s].", authToken), e);
			}
		} else {
			log.warn("Couldn't find bearer string, will ignore the header.");
		}
		if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

			Customer customer = customerService.findByEmail(userEmail);

			if (jwtTokenUtil.validateToken(authToken, customer.getEmail())) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						customer.getEmail(), null, Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				log.info("Authenticated user [{}] setting security context.", userEmail);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(req, res);
	}
}