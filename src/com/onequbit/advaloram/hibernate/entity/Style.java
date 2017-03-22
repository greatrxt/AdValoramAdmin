package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "style")
public class Style extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -8626826511075223858L;
	//private long id;
	public String styleCode;
	public String styleDetails;
	
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
