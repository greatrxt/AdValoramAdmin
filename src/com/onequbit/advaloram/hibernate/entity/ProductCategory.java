package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product_category")
public class ProductCategory extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -5815975080938929954L;
	public long id;
	public String categoryName;
	
	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	@Column(name="category_name")
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
