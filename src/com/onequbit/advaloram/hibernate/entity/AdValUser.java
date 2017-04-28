package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "ad_val_user")
public class AdValUser extends AbstractAdValoramEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6911073405527477620L;
	
	public String username;
	public String password;	
	public String firstName;
	public String lastName;
	
	public String token;
	
	public Role role;

	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	
	
	@Column(name="first_name")
	public String getFirstName() {
		return firstName;
	}
	
	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}
	
	@Column(name="token")
	public String getToken() {
		return token;
	}
	
	@Enumerated(EnumType.STRING)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}	

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
