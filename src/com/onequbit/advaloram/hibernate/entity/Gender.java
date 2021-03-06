package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gender")
public class Gender extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 7307264319663834608L;
	public long id;
	public String gender;
	public String genderCode;
	
	
	@Column(name="gender", unique = true)
	public String getGender() {
		return gender;
	}
	
	@Column(name="gender_code", unique = true)
	public String getGenderCode() {
		return genderCode;
	}
	
	@Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	
	
}
