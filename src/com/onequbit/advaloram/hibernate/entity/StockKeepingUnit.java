package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(
		name = "stock_keeping_unit",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"style_code", "color_code", "gender", "size_code"})}
		)
public class StockKeepingUnit extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1990654178733383990L;
	public String styleCode;
	public ColorCode colorCode;
	public Gender genderCode;
	public Size sizeCode;
	public String eanCode;
	public Product product;
	
	public Long quantityInStock;

	public StockKeepingUnit(){
		super();
		this.quantityInStock = (long) 0;
	}
	
	public StockKeepingUnit(String styleCode, ColorCode colorCode, Gender genderCode, Size sizeCode) {
		this.styleCode = styleCode;
		this.colorCode = colorCode;
		this.genderCode = genderCode;
		this.sizeCode = sizeCode;
		this.eanCode = colorCode.getColorCode() + styleCode + sizeCode.getSizeCode() + genderCode.getGenderCode(); //colorCode + styleCode + "28" + gender;
		this.quantityInStock = (long) 0;
	}
	
	@Column(name = "style_code")
	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="color_code", nullable = false, referencedColumnName = "color_code")
	public ColorCode getColorCode() {
		return colorCode;
	}

	public void setColorCode(ColorCode colorCode) {
		this.colorCode = colorCode;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="gender", nullable = false, referencedColumnName = "gender")
	public Gender getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(Gender genderCode) {
		this.genderCode = genderCode;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name="size_code", nullable = false, referencedColumnName = "size_code")
	public Size getSizeCode() {
		return sizeCode;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setSizeCode(Size sizeCode) {
		this.sizeCode = sizeCode;
	}
	
	@Column(name = "ean_code")
	public String getEanCode() {
		return eanCode;
	}

	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}

	@Column(name = "quantity_in_stock")
	public Long getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(Long quantityInStock) {
		this.quantityInStock = quantityInStock;
	}
	
	
}
