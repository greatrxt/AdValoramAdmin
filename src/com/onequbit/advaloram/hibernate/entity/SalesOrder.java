package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sales_order")
public class SalesOrder extends AbstractAdValoramEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8688511054388668328L;
	
	public Customer client;
	
	public String notes;
	
	
}
