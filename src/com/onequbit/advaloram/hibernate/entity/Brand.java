package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "brand")
public class Brand extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -1744185554531654447L;

	
	public String brandName;
	
	@Column(name="brand_name")
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}	
}
