package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unit_of_measurement")
public class UnitOfMeasurement extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 8955632550187767669L;
	//private long id;
	public String description;
	public String uomCode;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	
	@Column(name="uom_code")
	public String getUomCode() {
		return uomCode;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}
	
}
