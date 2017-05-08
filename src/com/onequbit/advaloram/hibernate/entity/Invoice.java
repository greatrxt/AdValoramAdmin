package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "invoice", 
		uniqueConstraints = {@UniqueConstraint(columnNames = {"linked_sales_order", "linked_packing_list"})}	//implies that combination of salesOrder and packing list mut be unique. 
		)
public class Invoice extends AbstractAdValoramEntity {

	/**
	 * If sales order and packing list is simply linked to Invoice, then any changes in SO or PL would change Invoice. This is not desirable. Invoice data must be freezed once
	 * issued. A way to achieve this would be to store SO and PL data again in Invoice. However this would result in unnecessarily storing same data.  
	 * Instead of storing all Sales Order info again, associate Invoice with the revision number of the 
	 * sales order and packing list. So that even if anything is changed in SO or PL, the changes will not be seen in an Invoice that has already been created since Invoice is linked to
	 * a particular revision of SO and PL  
	 */
	private static final long serialVersionUID = 6162514716706356606L;
	
	/*
	Instead of linking using id and revision number, directly link with the etity 
	public long linkedPackingListId;
	
	public int linkedPackingListRevisionNumber;	
	
	public long linkedSalesOrderId;
	
	public int linkedSalesOrderRevisionNumber;*/
	
	public SalesOrder linkedSalesOrder;
	
	public PackingList linkedPackingList;
	
	public long invoiceId;
	
	public Employee salesEmployee;
	
	public Customer refereePartnerName;

	public String roadPermitOrLicenseNumber;
	
	public long numberOfPackages;
	
	public Double grossWeight;
	
	public String lorryReceiptNumber;

	public Status status;
	
	public int invoiceRevisionNumber;
	
	public Date invoiceDate;
	
	public Double transportationCharges;
	
	public Double loadingCharges;
	
	public enum Status {
		OPEN, ISSUED, REVISED, CANCELLED 
	}
	
	@Column(name="invoice_id")
	public long getInvoiceId() {
		return invoiceId;
	}

	/*
	@Column(name="packing_list_id")
	public long getLinkedPackingListId() {
		return linkedPackingListId;
	}

	@Column(name="sales_order_id")
	public long getLinkedSalesOrderId() {
		return linkedSalesOrderId;
	}
	
	
	@Column(name="linked_packing_list_revision_number")
	public int getLinkedPackingListRevisionNumber() {
		return linkedPackingListRevisionNumber;
	}

	@Column(name="linked_sales_order_revision_number")
	public int getLinkedSalesOrderRevisionNumber() {
		return linkedSalesOrderRevisionNumber;
	}
	
	public void setLinkedPackingListRevisionNumber(int linkedPackingListRevisionNumber) {
		this.linkedPackingListRevisionNumber = linkedPackingListRevisionNumber;
	}

	public void setLinkedSalesOrderRevisionNumber(int linkedSalesOrderRevisionNumber) {
		this.linkedSalesOrderRevisionNumber = linkedSalesOrderRevisionNumber;
	}
	
	public void setLinkedPackingListId(long linkedPackingListId) {
		this.linkedPackingListId = linkedPackingListId;
	}

	public void setLinkedSalesOrderId(long linkedSalesOrderId) {
		this.linkedSalesOrderId = linkedSalesOrderId;
	}
	
	*/

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="sales_employee", nullable = true, referencedColumnName = "id")
	public Employee getSalesEmployee() {
		return salesEmployee;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="referee_partner_name", nullable = true, referencedColumnName = "company_name")
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
	public Double getGrossWeight() {
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

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_sales_order", nullable = true, referencedColumnName = "id")
	public SalesOrder getLinkedSalesOrder() {
		return linkedSalesOrder;
	}

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_packing_list", nullable = true, referencedColumnName = "id")
	public PackingList getLinkedPackingList() {
		return linkedPackingList;
	}
	
	@Column(name="transportation_charges")
	public Double getTransportationCharges() {
		return transportationCharges;
	}
	
	@Column(name="loading_charges")
	public Double getLoadingCharges() {
		return loadingCharges;
	}

	public void setTransportationCharges(Double transportationCharges) {
		this.transportationCharges = transportationCharges;
	}

	public void setLoadingCharges(Double loadingCharges) {
		this.loadingCharges = loadingCharges;
	}

	public void setLinkedSalesOrder(SalesOrder linkedSalesOrder) {
		this.linkedSalesOrder = linkedSalesOrder;
	}

	public void setLinkedPackingList(PackingList linkedPackingList) {
		this.linkedPackingList = linkedPackingList;
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

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public void setLorryReceiptNumber(String lorryReceiptNumber) {
		this.lorryReceiptNumber = lorryReceiptNumber;
	}
	
}
