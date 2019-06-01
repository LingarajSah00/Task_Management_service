package com.firstgenix.security.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.firstgenix.security.JwtAuthenticationRequest;
import com.firstgenix.security.JwtTokenUtil;
import com.firstgenix.security.JwtUser;
import com.firstgenix.security.model.Authority;
import com.firstgenix.security.model.User;
import com.firstgenix.security.service.JwtAuthenticationResponse;
import com.firstgenix.security.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@CrossOrigin(origins = { "http://localhost:4200" }, maxAge = 6000)
public class AuthenticationRestController {

	@Value("${jwt.header}")
	private String tokenHeader; 

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		ResponseEntity<?> responseEntity = null;
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		// Reload password post-security so we can generate the token
		final UserDetails userDetails= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		User user= ((JwtUserDetailsService)userDetailsService).loadUserByEmail(authenticationRequest.getUsername());
		final String username = authenticationRequest.getUsername();
		ArrayList<GrantedAuthority> authoritys = (ArrayList<GrantedAuthority>) userDetails.getAuthorities();
		Iterator<GrantedAuthority> iterator = authoritys.iterator();
		while (iterator.hasNext()) {
			GrantedAuthority authority = iterator.next();
			String userType = authority.getAuthority();
			if (userType.equals(authenticationRequest.getUserType())) {
				final String token = jwtTokenUtil.generateToken(userDetails);
				responseEntity = ResponseEntity.ok(new JwtAuthenticationResponse(token, user));

			} else {

				responseEntity = (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		User user2= ((JwtUserDetailsService)userDetailsService).loadUserByEmail(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user2));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Bad credentials!", e);
		}
	}
	  @RequestMapping(value = "${route.isValidateToken}", method = RequestMethod.GET)
	    public ResponseEntity<Boolean> isValidateToken(HttpServletRequest request){
	    	Boolean validate=false;
	    	try{
	    	String authToken = request.getHeader(tokenHeader);
	  
	        final String token = authToken.substring(7);
	
	         validate=jwtTokenUtil.isValidateToken(token);
	    	}catch(ExpiredJwtException e){
	    		validate=false;
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session Expired ,Plz Login Again");
	    	}
	    	
	    	return new ResponseEntity<Boolean>(validate, HttpStatus.OK);
			
	    }
}
