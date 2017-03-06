package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gender")
public class Gender extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7307264319663834608L;
	//public long id;
	public String gender;
	public String genderCode;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="gender")
	public String getGender() {
		return gender;
	}
	
	@Column(name="gender_code")
	public String getGenderCode() {
		return genderCode;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	
	
}
