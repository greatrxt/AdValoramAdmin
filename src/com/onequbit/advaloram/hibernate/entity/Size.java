package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "size")
public class Size extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -4621899079285800805L;
	//public long id;
	public String sizeCode;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="size_code")
	public String getSizeCode() {
		return sizeCode;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
}
