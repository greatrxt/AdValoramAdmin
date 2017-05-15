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
		uniqueConstraints = {@UniqueConstraint(columnNames = {"style_code", "color_code", "gender", "size_code", "product_category"})}
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
	public String skuCode;
	public Product product;
	public ProductCategory productCategory;
	public Long quantityInStock;
	public String eanCode;	//For display purpose only

	private StockKeepingUnit(){
		super();
		this.quantityInStock = (long) 0;
	}
	
	public StockKeepingUnit(String styleCode, ColorCode colorCode, Gender genderCode, Size sizeCode, Integer mrp, ProductCategory productCategory) {
		this.styleCode = styleCode;
		this.colorCode = colorCode;
		this.genderCode = genderCode;
		this.sizeCode = sizeCode;
		this.productCategory = productCategory;
		this.skuCode = generateSkuCode(productCategory.getId(), colorCode.getColorCode(), styleCode, sizeCode.getSizeCode(), genderCode.getGenderCode()); 
		//EAN code for display purpose only. Not to be used internally. Use SKU Code instead
		this.eanCode = generateEanCode(productCategory.getId(), colorCode.getColorCode(), styleCode, sizeCode.getSizeCode(), genderCode.getId(), mrp);
		this.quantityInStock = (long) 0;
	}
	
	/**
	 * The auto generated product code to be in-line with the Bar code generator viz.
		Colour code (2 digits-AN)+Category code (1 digit-N)+Style Code(4 digits-N)+MRP(4 digits-N) + Size code (2 digits-N)+Gender code (1 digit-N)
		Where N - Numeric, AN - Alphanumeric
		e.g. C2130012199321
	 * @param productCategory 
	 * @param colorCode
	 * @param styleCode
	 * @param sizeCode
	 * @param genderCode
	 * @param mrp 
	 * @return
	 * 
	 * 
	 * String formatted = String.format("%03d", num);
		0 - to pad with zeros
		3 - to set width to 3
	 */
	public static String generateEanCode(Long productCategory, String colorCode, String styleCode, String sizeCode, Long genderCodeId, Integer mrp) {
		return colorCode + productCategory + String.format("%04d", Integer.valueOf(styleCode)) + String.format("%04d", mrp) + sizeCode + genderCodeId;
	}

	public static String generateSkuCode(Long productCategory, String colorCode, String styleCode, String sizeCode, String genderCode) {
		return productCategory + String.format("%04d", Integer.valueOf(styleCode)) + colorCode + genderCode  + sizeCode;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
	@JoinColumn(name="product_category", nullable = false, referencedColumnName = "id")
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@Column(name = "style_code")
	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
	@JoinColumn(name="color_code", nullable = false, referencedColumnName = "color_code")
	public ColorCode getColorCode() {
		return colorCode;
	}

	public void setColorCode(ColorCode colorCode) {
		this.colorCode = colorCode;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
	@JoinColumn(name="gender", nullable = false, referencedColumnName = "gender")
	public Gender getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(Gender genderCode) {
		this.genderCode = genderCode;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
	@JoinColumn(name="size_code", nullable = false, referencedColumnName = "size_code")
	public Size getSizeCode() {
		return sizeCode;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
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
	
	@Column(name = "sku_code")
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
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
