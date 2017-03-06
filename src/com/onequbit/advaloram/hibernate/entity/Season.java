package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "season")
public class Season extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -7095574745711279460L;
	//private long id;
	public String code;
	public String season;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}*/
	
	@Column(name="season_code")
	public String getCode() {
		return code;
	}
	
	@Column(name="season")
	public String getSeason() {
		return season;
	}
	
/*	public void setId(long id) {
		this.id = id;
	}*/
	public void setCode(String code) {
		this.code = code;
	}
	public void setSeason(String season) {
		this.season = season;
	}	
}
