package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_category")
public class ProductCategory extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -5815975080938929954L;
	//private long id;
	public String categoryName;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="category_name")
	public String getCategoryName() {
		return categoryName;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
