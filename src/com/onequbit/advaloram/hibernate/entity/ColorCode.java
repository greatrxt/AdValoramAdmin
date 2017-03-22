package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "color_code")
public class ColorCode extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -3631580932167147349L;

	public String colorCode;
	
	@Column(name="color_code")
	public String getColorCode() {
		return colorCode;
	}
	
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}	
}
