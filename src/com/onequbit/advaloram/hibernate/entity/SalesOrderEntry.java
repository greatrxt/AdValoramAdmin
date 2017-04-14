package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "sales_order_entry")
public class SalesOrderEntry extends AbstractAdValoramEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 956114781810678706L;
	
	public int entryId;
	
	public StockKeepingUnit stockKeepingUnit;
	
	public long quantity;

	@Column(name="entry_id")
	public int getEntryId() {
		return entryId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.JOIN)
	@JoinColumn(name="sku_ean_code", nullable = true, referencedColumnName = "ean_code")
	public StockKeepingUnit getStockKeepingUnit() {
		return stockKeepingUnit;
	}

	@Column(name="quantity")
	public long getQuantity() {
		return quantity;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public void setStockKeepingUnit(StockKeepingUnit stockKeepingUnit) {
		this.stockKeepingUnit = stockKeepingUnit;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
	
	
}
