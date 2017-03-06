package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "style")
public class Style extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8626826511075223858L;
	//private long id;
	private String styleCode;
	private String styleDetails;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="style_code")
	public String getStyleCode() {
		return styleCode;
	}
	
	@Column(name="style_details")
	public String getStyleDetails() {
		return styleDetails;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	
	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}
	
	public void setStyleDetails(String styleDetails) {
		this.styleDetails = styleDetails;
	}
	
	
}
