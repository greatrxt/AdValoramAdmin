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
	private static final long serialVersionUID = -3631580932167147349L;
	//private long id;
	//public String code;
	public String color;
	public String colorCode;

/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
/*	@Column(name="code")
	public String getCode() {
		return code;
	}
	*/
	@Column(name="color")
	public String getColor() {
		return color;
	}
	
	@Column(name="color_code")
	public String getColorCode() {
		return colorCode;
	}
/*	public void setId(long id) {
		this.id = id;
	}*/
/*	public void setCode(String code) {
		this.code = code;
	}*/
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	
}
