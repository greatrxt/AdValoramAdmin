package com.onequbit.advaloram.hibernate.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


//https://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example-annotation/
@Entity
@Table(name = "product")
public class Product extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 6641898589463808223L;
	//private long id;
	
	//private Style styleCode;
	public String styleCode;
	public Set<Color> colorCodes;
	public Set<Gender> genderCodes;
	public Set<Size> sizeCodes;
	public Season seasonCode;
	public Brand brand;
	public ProductCategory productCategory;
	public UnitOfMeasurement unitOfMeasurement;
	public Integer mrp;
	public Integer tradePrice;
	public String eanCode;
	public String productName;
	public String status;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="product_id")
	public long getId() {
		return id;
	}*/
	
/*	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="style_code", nullable = true, referencedColumnName = "style_code")
	public Style getStyleCode() {
		return styleCode;
	}*/
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="color_code", nullable = true, referencedColumnName = "color_code")
	//@OrderBy("uuidColumn")
	public Set<Color> getColorCodes() {
		return colorCodes;
	}
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "product_gender", catalog = "public", joinColumns = {
			@JoinColumn(name = "product_id", nullable = true, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "gender_code",
					nullable = true, updatable = false) })
	public Set<Gender> getGenderCodes() {
		return genderCodes;
	}
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "product_size", catalog = "public", joinColumns = {
			@JoinColumn(name = "product_id", nullable = true, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "size_code",
					nullable = true, updatable = false) })
	public Set<Size> getSizeCodes() {
		return sizeCodes;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="season_code", nullable = true, referencedColumnName = "season_code")
	public Season getSeasonCode() {
		return seasonCode;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="brand_name", nullable = true, referencedColumnName = "brand_name")
	public Brand getBrand() {
		return brand;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="category_name", nullable = true, referencedColumnName = "category_name")
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="uom_code", nullable = true, referencedColumnName = "uom_code")
	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}
	
	@Column(name="mrp")
	public Integer getMrp() {
		return mrp;
	}
	
	@Column(name="traded_price")
	public Integer getTradePrice() {
		return tradePrice;
	}
	
	@Column(name="ean_code")
	public String getEanCode() {
		return eanCode;
	}
	
	@Column(name="product_name")
	public String getProductName() {
		return productName;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
/*	public void setId(long id) {
		this.id = id;
	}*/
/*	public void setStyleCode(Style styleCode) {
		this.styleCode = styleCode;
	}*/
	
	
	public void setColorCodes(Set<Color> colorCode) {
		this.colorCodes = colorCode;
	}
	@Column(name="style_code")
	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	public void setGenderCodes(Set<Gender> genderCode) {
		this.genderCodes = genderCode;
	}
	
	public void setSizeCodes(Set<Size> sizeCode) {
		this.sizeCodes = sizeCode;
	}
	public void setSeasonCode(Season seasonCode) {
		this.seasonCode = seasonCode;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	public void setMrp(Integer mrp) {
		this.mrp = mrp;
	}
	public void setTradePrice(Integer tradePrice) {
		this.tradePrice = tradePrice;
	}
	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
