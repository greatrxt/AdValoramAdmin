package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "sales_order")
public class SalesOrder extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public long id;
	
	private static final long serialVersionUID = 8688511054388668328L;
	
	public long salesOrderId; //will be useful in case of multiple revisions
	
	public int salesOrderRevisionNumber;
	
	public Date salesOrderDate;
	
	public String purchaseOrderReference;
	
	public Customer linkedCustomer; // linked by ID
	
	public String clientNameOnSalesOrderDate; // stored in separate field to avoid confusion in case client name gets edited in MASTERS later
	
	public String clientBillingAddressOnSalesOrderDate;
	
	public String clientDeliveryAddressOnSalesOrderDate;
	
	public String clientCityOnSalesOrderDate;
	
	public String clientPinCodeOnSalesOrderDate;
	
	public String clientDistrictOnSalesOrderDate;
	
	public String clientStateOnSalesOrderDate;
	
	public String clientContactPersonOnSalesOrderDate;
	
	public String clientContactNumberOnSalesOrderDate;
	
	public String clientEmailAddressOnSalesOrderDate;
	
	public Employee referredByEmployee;
	
	public Set<SalesOrderEntry> entry;
	
	public float markDownOnSalesOrderDate;
	
	public float cashDiscountOnSalesOrderDate;
	
	public float promptPaymentDiscountOnSalesOrderDate;
	
	public float specialDiscountOnSalesOrderDate;
	
	public String notes;
	
	public String revisionReason;
	
	public Set<File> associatedFiles;
	
	public Status status;
	
	public enum Status {
		OPEN, CONFIRMED, DISPATCHED, CANCELLED 
	}
	
	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	@Column(name="sales_order_id")
	public long getSalesOrderId() {
		return salesOrderId;
	}

	@Column(name="sales_order_revision_number")
	public int getSalesOrderRevisionNumber() {
		return salesOrderRevisionNumber;
	}

	@Column(name="sales_order_date")
	public Date getSalesOrderDate() {
		return salesOrderDate;
	}

	@Column(name="purchase_order_reference")
	public String getPurchaseOrderReference() {
		return purchaseOrderReference;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_customer", nullable = true, referencedColumnName = "id")
	public Customer getLinkedCustomer() {
		return linkedCustomer;
	}

	@Column(name="client_name_on_sales_order_date")
	public String getClientNameOnSalesOrderDate() {
		return clientNameOnSalesOrderDate;
	}

	@Column(name="client_billing_address_on_sales_order_date")
	public String getClientBillingAddressOnSalesOrderDate() {
		return clientBillingAddressOnSalesOrderDate;
	}

	@Column(name="client_delivery_address_on_sales_order_date")
	public String getClientDeliveryAddressOnSalesOrderDate() {
		return clientDeliveryAddressOnSalesOrderDate;
	}

	@Column(name="client_city_on_sales_order_date")
	public String getClientCityOnSalesOrderDate() {
		return clientCityOnSalesOrderDate;
	}

	@Column(name="client_pincode_on_sales_order_date")
	public String getClientPinCodeOnSalesOrderDate() {
		return clientPinCodeOnSalesOrderDate;
	}

	@Column(name="client_district_on_sales_order_date")
	public String getClientDistrictOnSalesOrderDate() {
		return clientDistrictOnSalesOrderDate;
	}

	@Column(name="client_state_on_sales_order_date")
	public String getClientStateOnSalesOrderDate() {
		return clientStateOnSalesOrderDate;
	}

	@Column(name="client_contact_person_on_sales_order_date")
	public String getClientContactPersonOnSalesOrderDate() {
		return clientContactPersonOnSalesOrderDate;
	}

	@Column(name="client_contact_number_on_sales_order_date")
	public String getClientContactNumberOnSalesOrderDate() {
		return clientContactNumberOnSalesOrderDate;
	}

	@Column(name="client_email_address_on_sales_order_date")
	public String getClientEmailAddressOnSalesOrderDate() {
		return clientEmailAddressOnSalesOrderDate;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="referred_by_employee", nullable = true, referencedColumnName = "id")
	public Employee getReferredByEmployee() {
		return referredByEmployee;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "salesorder_entry_mapping", joinColumns = { @JoinColumn(name = "sales_order_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id", nullable = false, updatable = false) })
	public Set<SalesOrderEntry> getEntry() {
		return entry;
	}

	@Column(name="mark_down_on_sales_order_date")
	public float getMarkDownOnSalesOrderDate() {
		return markDownOnSalesOrderDate;
	}

	@Column(name="cash_discount_on_sales_order_date")
	public float getCashDiscountOnSalesOrderDate() {
		return cashDiscountOnSalesOrderDate;
	}

	@Column(name="prompt_payment_discount_on_sales_order_date")
	public float getPromptPaymentDiscountOnSalesOrderDate() {
		return promptPaymentDiscountOnSalesOrderDate;
	}

	@Column(name="special_discount_on_sales_order_date")
	public float getSpecialDiscountOnSalesOrderDate() {
		return specialDiscountOnSalesOrderDate;
	}

	@Column(name="notes")
	public String getNotes() {
		return notes;
	}

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "salesorder_file", catalog = "public", joinColumns = { @JoinColumn(name = "sales_order_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "file_id", nullable = false, updatable = false) })
	public Set<File> getAssociatedFiles() {
		return associatedFiles;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}
	
	@Column(name="revision_reason")
	public String getRevisionReason() {
		return revisionReason;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setRevisionReason(String revisionReason) {
		this.revisionReason = revisionReason;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setSalesOrderId(long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public void setSalesOrderRevisionNumber(int salesOrderRevisionNumber) {
		this.salesOrderRevisionNumber = salesOrderRevisionNumber;
	}

	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public void setPurchaseOrderReference(String purchaseOrderReference) {
		this.purchaseOrderReference = purchaseOrderReference;
	}

	public void setLinkedCustomer(Customer linkedCustomer) {
		this.linkedCustomer = linkedCustomer;
	}

	public void setClientNameOnSalesOrderDate(String clientNameOnSalesOrderDate) {
		this.clientNameOnSalesOrderDate = clientNameOnSalesOrderDate;
	}

	public void setClientBillingAddressOnSalesOrderDate(String clientBillingAddressOnSalesOrderDate) {
		this.clientBillingAddressOnSalesOrderDate = clientBillingAddressOnSalesOrderDate;
	}

	public void setClientDeliveryAddressOnSalesOrderDate(String clientDeliveryAddressOnSalesOrderDate) {
		this.clientDeliveryAddressOnSalesOrderDate = clientDeliveryAddressOnSalesOrderDate;
	}

	public void setClientCityOnSalesOrderDate(String clientCityOnSalesOrderDate) {
		this.clientCityOnSalesOrderDate = clientCityOnSalesOrderDate;
	}

	public void setClientPinCodeOnSalesOrderDate(String clientPinCodeOnSalesOrderDate) {
		this.clientPinCodeOnSalesOrderDate = clientPinCodeOnSalesOrderDate;
	}

	public void setClientDistrictOnSalesOrderDate(String clientDistrictOnSalesOrderDate) {
		this.clientDistrictOnSalesOrderDate = clientDistrictOnSalesOrderDate;
	}

	public void setClientStateOnSalesOrderDate(String clientStateOnSalesOrderDate) {
		this.clientStateOnSalesOrderDate = clientStateOnSalesOrderDate;
	}

	public void setClientContactPersonOnSalesOrderDate(String clientContactPersonOnSalesOrderDate) {
		this.clientContactPersonOnSalesOrderDate = clientContactPersonOnSalesOrderDate;
	}

	public void setClientContactNumberOnSalesOrderDate(String clientContactNumberOnSalesOrderDate) {
		this.clientContactNumberOnSalesOrderDate = clientContactNumberOnSalesOrderDate;
	}

	public void setClientEmailAddressOnSalesOrderDate(String clientEmailAddressOnSalesOrderDate) {
		this.clientEmailAddressOnSalesOrderDate = clientEmailAddressOnSalesOrderDate;
	}

	public void setReferredByEmployee(Employee referredByEmployee) {
		this.referredByEmployee = referredByEmployee;
	}


	public void setEntry(Set<SalesOrderEntry> entry) {
		this.entry = entry;
	}

	public void setMarkDownOnSalesOrderDate(float markDownOnSalesOrderDate) {
		this.markDownOnSalesOrderDate = markDownOnSalesOrderDate;
	}

	public void setCashDiscountOnSalesOrderDate(float cashDiscountOnSalesOrderDate) {
		this.cashDiscountOnSalesOrderDate = cashDiscountOnSalesOrderDate;
	}

	public void setPromptPaymentDiscountOnSalesOrderDate(float promptPaymentDiscountOnSalesOrderDate) {
		this.promptPaymentDiscountOnSalesOrderDate = promptPaymentDiscountOnSalesOrderDate;
	}

	public void setSpecialDiscountOnSalesOrderDate(float specialDiscountOnSalesOrderDate) {
		this.specialDiscountOnSalesOrderDate = specialDiscountOnSalesOrderDate;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
	}
	
	
}
