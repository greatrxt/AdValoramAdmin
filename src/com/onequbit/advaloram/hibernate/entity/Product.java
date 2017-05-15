package com.onequbit.advaloram.hibernate.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.onequbit.advaloram.hibernate.entity.SalesOrder.Status;


//https://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example-annotation/
@Entity
@Table(name = "product",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"style_code", "product_category"})}
)
public class Product extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 6641898589463808223L;
	//private long id;
	
	//private Style styleCode;
	public String styleCode;
	//public Set<Color> colorCodes;
	public Map<ColorCode, Color> colors;
	public Set<Gender> genderCodes;
	public Set<File> associatedFiles;
	public Set<Size> sizeCodes;
	public Season seasonCode;
	public Brand brand;
	public ProductCategory productCategory;
	public UnitOfMeasurement unitOfMeasurement;
	public Integer mrp;
	public Integer tradePrice;
	public Integer costPrice;
	//public String productName;
	public Status status;
	
	public static enum Status {
		ACTIVE, DEACTIVATED
	}
	
	public Set<StockKeepingUnit> stockKeepingUnits;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "product_gender", catalog = "public", joinColumns = {
			@JoinColumn(name = "product_id", nullable = true, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "gender_code",
					nullable = true, updatable = false) })
	public Set<Gender> getGenderCodes() {
		return genderCodes;
	}
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "product_size", catalog = "public", joinColumns = {
			@JoinColumn(name = "product_id", nullable = true, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "size_code",
					nullable = true, updatable = false) })
	public Set<Size> getSizeCodes() {
		return sizeCodes;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="season_code", nullable = true, referencedColumnName = "season_code")
	public Season getSeasonCode() {
		return seasonCode;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="brand_name", nullable = true, referencedColumnName = "brand_name")
	public Brand getBrand() {
		return brand;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="product_category", nullable = true, referencedColumnName = "category_name")
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "product_file", catalog = "public", joinColumns = { @JoinColumn(name = "product_id", nullable = true, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "file_id", nullable = true, updatable = false) })
	public Set<File> getAssociatedFiles() {
		return associatedFiles;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
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

	@Column(name="cost_price")
	public Integer getCostPrice() {
		return costPrice;
	}
	
	/*@Column(name="product_name")
	public String getProductName() {
		return productName;
	}*/
	
	@Column(name="status")
	public Status getStatus() {
		return status;
	}
	
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	public Set<StockKeepingUnit> getStockKeepingUnits() {
		return stockKeepingUnits;
	}


	public void setStockKeepingUnits(Set<StockKeepingUnit> stockKeepingUnits) {
		this.stockKeepingUnits = stockKeepingUnits;
	}


	@Column(name="style_code")
	public String getStyleCode() {
		return styleCode;
	}

	//http://stackoverflow.com/questions/7525320/how-to-add-a-mapstring-person-in-an-entity-class
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	public Map<ColorCode, Color> getColors() {
		return colors;
	}


	public void setColors(Map<ColorCode, Color> colorCodes) {
		this.colors = colorCodes;
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
	
	public void setCostPrice(Integer costPrice) {
		this.costPrice = costPrice;
	}
	
	public void setTradePrice(Integer tradePrice) {
		this.tradePrice = tradePrice;
	}

	/*public void setProductName(String productName) {
		this.productName = productName;
	}*/
	public void setStatus(Status status) {
		this.status = status;
	}

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
	}
	
	
}
