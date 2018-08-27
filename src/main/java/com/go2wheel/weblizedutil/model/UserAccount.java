package com.go2wheel.weblizedutil.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.go2wheel.weblizedutil.util.ObjectUtil;

public class UserAccount extends BaseModel implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String username;
	
	private String password;
	
	@NotEmpty
	private String mobile;
	private String description;
	
	@NotEmpty
	private String email;
	
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public static class UserAccountBuilder {
		private final String username;
		private final String email;
		private final String password;
		private String mobile;
		private String description;
		
		private List<String> rolenames = new ArrayList<>();
		
		public UserAccountBuilder(String username, String email, String password) {
			super();
			this.username = username;
			this.email = email;
			this.password = password;
		}
		
		public UserAccountBuilder withMobile(String mobile) {
			this.mobile = mobile;
			return this;
		}
		
		public UserAccountBuilder withRoles(String...rolenames) {
			this.rolenames.addAll(Arrays.asList(rolenames));
			return this;
		}
		
		public UserAccountBuilder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public UserAccount build() {
			UserAccount ua = new UserAccount();
			ua.setCreatedAt(new Date());
			ua.setDescription(description);
			ua.setEmail(email);
			if (mobile == null) {
				ua.setMobile(UUID.randomUUID().toString());
			} else {
				ua.setMobile(mobile);
			}
			ua.setUsername(username);
			ua.setPassword(password);
			ua.setAccountNonExpired(true);
			ua.setAccountNonLocked(true);
			ua.setCredentialsNonExpired(true);
			ua.setEnabled(true);
			ua.setRoles(rolenames.stream().map(rn -> new UserRole(rn)).collect(Collectors.toSet()));
			return ua; 
		}

		public List<String> getRolenames() {
			return rolenames;
		}

		public void setRolenames(List<String> rolenames) {
			this.rolenames = rolenames;
		}
		
	}

	@Override
	public String toListRepresentation(String... fields) {
		if (fields.length == 0) {
			fields = new String[]{"username", "email", "mobile"};
		}
		return ObjectUtil.toListRepresentation(this, fields);
	}
	
	private Set<UserRole> roles;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (roles == null) {
			return new HashSet<>();
		}
		return roles;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

}
