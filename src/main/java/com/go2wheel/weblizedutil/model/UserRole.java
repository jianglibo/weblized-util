package com.go2wheel.weblizedutil.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * @see SimpleGrantedAuthority
 * @author jianglibo@gmail.com
 *
 */
public class UserRole extends BaseModel implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String role;
	
	public UserRole() {}

	public UserRole(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SimpleGrantedAuthority) {
			return role.equals(((UserRole) obj).role);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.role.hashCode();
	}

	@Override
	public String toString() {
		return this.role;
	}
}
