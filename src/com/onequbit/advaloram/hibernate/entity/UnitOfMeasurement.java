package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "unit_of_measurement")
public class UnitOfMeasurement extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 8955632550187767669L;
	public Long id;
	
	@Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String description;
	
	public String uomCode;
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	
	@Column(name="uom_code")
	public String getUomCode() {
		return uomCode;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	
}
