package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "color")
public class Color extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -3631580932167147349L;

	public String colorName;
	
	@Column(name="color_name")
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}	
}
