package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tax")
public class Tax extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6463966430818014231L;
	
	public String taxName;
	public String shortName;
	public double taxRate;
	
	@Column(name="tax_name", nullable = false)
	public String getTaxName() {
		return taxName;
	}
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	
	@Column(name="short_name", nullable = false)
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Column(name="tax_rate", nullable = false)
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
}
