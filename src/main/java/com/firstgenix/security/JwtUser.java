package com.firstgenix.security;

import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {

    private final String id;
    private final String username;
//    private final String firstname;
//    private final String middlename;
//    private final String lastname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetDate;
	private boolean enabled;

    public JwtUser(
          String id,
          String username,
//          String firstname,
//          String middlename,
//          String lastname,
          String password, 
          Collection<? extends GrantedAuthority> authorities,
          boolean enabled,
          Date lastPasswordResetDate
    ) {
        this.id = id;
        this.username = username;
//        this.firstname = firstname;
//        this.middlename = middlename;
//        this.lastname = lastname;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	//@JsonIgnore
	public String getId() {
		return id;
	}

//	public String getFirstname() {
//		return firstname;
//	}
//
//	public String getMiddlename() {
//		return middlename;
//	}
//
//	public String getLastname() {
//		return lastname;
//	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
  
}
