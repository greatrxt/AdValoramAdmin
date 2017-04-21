package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Invoice extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6162514716706356606L;
	
	public long linkedPackingListId;
	
	public long linkedSalesOrderId;
	
	public long invoiceId;
	
	public Employee salesEmployee;
	
	public Customer refereePartnerName;

	public String roadPermitOrLicenseNumber;
	
	public long numberOfPackages;
	
	public double grossWeight;
	
	public String lorryReceiptNumber;

	public Status status;
	
	public int invoiceRevisionNumber;
	
	public Date invoiceDate;
	
	public enum Status {
		OPEN, ISSUED, REVISED, CANCELLED 
	}
	
	
	@Column(name="invoice_id")
	public long getInvoiceId() {
		return invoiceId;
	}

	@Column(name="packing_list_id")
	public long getLinkedPackingListId() {
		return linkedPackingListId;
	}

	@Column(name="sales_order_id")
	public long getLinkedSalesOrderId() {
		return linkedSalesOrderId;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="sales_employee", nullable = false, referencedColumnName = "id")
	public Employee getSalesEmployee() {
		return salesEmployee;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="referee_partner_name", nullable = false, referencedColumnName = "company_name")
	public Customer getRefereePartnerName() {
		return refereePartnerName;
	}

	@Column(name="road_permit_or_license_number")
	public String getRoadPermitOrLicenseNumber() {
		return roadPermitOrLicenseNumber;
	}

	@Column(name="number_of_packages")
	public long getNumberOfPackages() {
		return numberOfPackages;
	}

	@Column(name="gross_weight")
	public double getGrossWeight() {
		return grossWeight;
	}

	@Column(name="lorry_receipt_number")
	public String getLorryReceiptNumber() {
		return lorryReceiptNumber;
	}

	@Column(name="status")
	public Status getStatus() {
		return status;
	}

	@Column(name="invoice_revision_number")
	public int getInvoiceRevisionNumber() {
		return invoiceRevisionNumber;
	}

	@Column(name="invoice_date")
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setInvoiceRevisionNumber(int invoiceRevisionNumber) {
		this.invoiceRevisionNumber = invoiceRevisionNumber;
	}
	
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setLinkedPackingListId(long linkedPackingListId) {
		this.linkedPackingListId = linkedPackingListId;
	}

	public void setLinkedSalesOrderId(long linkedSalesOrderId) {
		this.linkedSalesOrderId = linkedSalesOrderId;
	}

	public void setSalesEmployee(Employee salesEmployee) {
		this.salesEmployee = salesEmployee;
	}

	public void setRefereePartnerName(Customer refereePartnerName) {
		this.refereePartnerName = refereePartnerName;
	}

	public void setRoadPermitOrLicenseNumber(String roadPermitOrLicenseNumber) {
		this.roadPermitOrLicenseNumber = roadPermitOrLicenseNumber;
	}

	public void setNumberOfPackages(long numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}

	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public void setLorryReceiptNumber(String lorryReceiptNumber) {
		this.lorryReceiptNumber = lorryReceiptNumber;
	}
	
}
