package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -3970218530834203114L;
	/**
	 * 
	 */
	//public long id;
	public String cityName;
	public String district;
	public String taluka;
	public String state;
	public String country;
	//public Date recordCreationTime;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}*/

	@Column(name="city_name")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@Column(name="district")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name="taluka")
	public String getTaluka() {
		return taluka;
	}

	public void setTaluka(String taluka) {
		this.taluka = taluka;
	}

	@Column(name="state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
	
/*	@Temporal(TemporalType.DATE)		//@Temporal: must be used with a java.util.Date field to specify the actual SQL type of the column
	@Column(name="record_creation_time")
	public Date getRecordCreationTime() {
		return recordCreationTime;
	}

	public void setRecordCreationTime(Date recordCreationTime) {
		this.recordCreationTime = recordCreationTime;
	}*/
}
