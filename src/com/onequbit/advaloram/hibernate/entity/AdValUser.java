package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	public String userImage;
	
	public Role role;

	public Date lastLoginTime;
	
	public Date lastUserActivityTime;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_user_activity_time")
	public Date getLastUserActivityTime() {
		return lastUserActivityTime;
	}

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

	@Column(name="user_image")
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLastUserActivityTime(Date lastUserActivityTime) {
		this.lastUserActivityTime = lastUserActivityTime;
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
