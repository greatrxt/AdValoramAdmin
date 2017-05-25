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
public class SalesOrder extends AbstractAdValoramEntity implements Comparable<SalesOrder>{

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
	
	public Customer referredByCustomer;
	
	public Customer refereePartner;
	
	public Set<SalesOrderEntry> entry;
	
	public Double markDownOnSalesOrderDate;
	
	public Double cashDiscountOnSalesOrderDate;
	
	public Double promptPaymentDiscountOnSalesOrderDate;
	
	public Double specialDiscountOnSalesOrderDate;
	
	public Double cstRateApplicableOnSalesOrderDate;
	public Double vatRateApplicableOnSalesOrderDate;
	public Double cstRateAgainstFormCOnSalesOrderDate;
	public Double gstRateApplicableOnSalesOrderDate;
	public Double octroiLbtEntryTaxApplicableOnSalesOrderDate;
	
	public Boolean vatIsApplicableOnSalesOrderDate;
	public Boolean cstIsApplicableOnSalesOrderDate;
	public Boolean gstIsApplicableOnSalesOrderDate;
	public Boolean octroiLbtEntryTaxIsApplicableOnSalesOrderDate;
	
	public String vatTinNumberOnSalesOrderDate;
	public String gstNumberOnSalesOrderDate;
	public String cstTinNumberOnSalesOrderDate;
	public String serviceTaxNumberOnSalesOrderDate;
	public String panNumberOnSalesOrderDate;
	
	public String notes;
	
	public String revisionReason;
	
	public Set<File> associatedFiles;
	
	public Status status;
	
	public static enum Status {
		OPEN, CONFIRMED, DISPATCHED, CANCELLED, UNDER_REVISION
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
	public Double getMarkDownOnSalesOrderDate() {
		return markDownOnSalesOrderDate;
	}

	@Column(name="cash_discount_on_sales_order_date")
	public Double getCashDiscountOnSalesOrderDate() {
		return cashDiscountOnSalesOrderDate;
	}

	@Column(name="prompt_payment_discount_on_sales_order_date")
	public Double getPromptPaymentDiscountOnSalesOrderDate() {
		return promptPaymentDiscountOnSalesOrderDate;
	}

	@Column(name="special_discount_on_sales_order_date")
	public Double getSpecialDiscountOnSalesOrderDate() {
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
	

	@Column(name="cst_rate_applicable_on_sales_order_date")
	public Double getCstRateApplicableOnSalesOrderDate() {
		return cstRateApplicableOnSalesOrderDate;
	}

	@Column(name="vat_rate_applicable_on_sales_order_date")
	public Double getVatRateApplicableOnSalesOrderDate() {
		return vatRateApplicableOnSalesOrderDate;
	}


	@Column(name="cst_rate_against_form_c_on_sales_order_date")
	public Double getCstRateAgainstFormCOnSalesOrderDate() {
		return cstRateAgainstFormCOnSalesOrderDate;
	}

	@Column(name="gst_rate_applicable_on_sales_order_date")
	public Double getGstRateApplicableOnSalesOrderDate() {
		return gstRateApplicableOnSalesOrderDate;
	}

	@Column(name="octroi_lbt_entry_tax_applicable_on_sales_order_date")
	public Double getOctroiLbtEntryTaxApplicableOnSalesOrderDate() {
		return octroiLbtEntryTaxApplicableOnSalesOrderDate;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="referred_by_customer", nullable = true, referencedColumnName = "id")
	public Customer getReferredByCustomer() {
		return referredByCustomer;
	}

	@Column(name="is_vat_applicable_on_sales_order_date")
	public Boolean getVatIsApplicableOnSalesOrderDate() {
		return vatIsApplicableOnSalesOrderDate;
	}

	@Column(name="is_cst_applicable_on_sales_order_date")
	public Boolean getCstIsApplicableOnSalesOrderDate() {
		return cstIsApplicableOnSalesOrderDate;
	}

	@Column(name="is_gst_applicable_on_sales_order_date")
	public Boolean getGstIsApplicableOnSalesOrderDate() {
		return gstIsApplicableOnSalesOrderDate;
	}

	@Column(name="is_octroi_lbt_entry_tax_applicable_on_sales_order_date")
	public Boolean getOctroiLbtEntryTaxIsApplicableOnSalesOrderDate() {
		return octroiLbtEntryTaxIsApplicableOnSalesOrderDate;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="referee_partner", nullable = true, referencedColumnName = "id")
	public Customer getRefereePartner() {
		return refereePartner;
	}

	
	@Column(name="client_vat_tin_number_on_sales_order_date")
	public String getVatTinNumberOnSalesOrderDate() {
		return vatTinNumberOnSalesOrderDate;
	}

	@Column(name="client_gst_number_on_sales_order_date")
	public String getGstNumberOnSalesOrderDate() {
		return gstNumberOnSalesOrderDate;
	}

	@Column(name="client_cst_tin_number_on_sales_order_date")
	public String getCstTinNumberOnSalesOrderDate() {
		return cstTinNumberOnSalesOrderDate;
	}

	@Column(name="client_service_tax_number_on_sales_order_date")
	public String getServiceTaxNumberOnSalesOrderDate() {
		return serviceTaxNumberOnSalesOrderDate;
	}

	@Column(name="client_pan_number_on_sales_order_date")
	public String getPanNumberOnSalesOrderDate() {
		return panNumberOnSalesOrderDate;
	}

	
	public void setVatTinNumberOnSalesOrderDate(String vatTinNumberOnSalesOrderDate) {
		this.vatTinNumberOnSalesOrderDate = vatTinNumberOnSalesOrderDate;
	}

	public void setGstNumberOnSalesOrderDate(String gstNumberOnSalesOrderDate) {
		this.gstNumberOnSalesOrderDate = gstNumberOnSalesOrderDate;
	}

	public void setCstTinNumberOnSalesOrderDate(String cstTinNumberOnSalesOrderDate) {
		this.cstTinNumberOnSalesOrderDate = cstTinNumberOnSalesOrderDate;
	}

	public void setServiceTaxNumberOnSalesOrderDate(String serviceTaxNumberOnSalesOrderDate) {
		this.serviceTaxNumberOnSalesOrderDate = serviceTaxNumberOnSalesOrderDate;
	}

	public void setPanNumberOnSalesOrderDate(String panNumberOnSalesOrderDate) {
		this.panNumberOnSalesOrderDate = panNumberOnSalesOrderDate;
	}

	public void setRefereePartner(Customer refereePartner) {
		this.refereePartner = refereePartner;
	}

	public void setVatIsApplicableOnSalesOrderDate(Boolean vatIsApplicableOnSalesOrderDate) {
		this.vatIsApplicableOnSalesOrderDate = vatIsApplicableOnSalesOrderDate;
	}

	public void setCstIsApplicableOnSalesOrderDate(Boolean cstIsApplicableOnSalesOrderDate) {
		this.cstIsApplicableOnSalesOrderDate = cstIsApplicableOnSalesOrderDate;
	}

	public void setGstIsApplicableOnSalesOrderDate(Boolean gstIsApplicableOnSalesOrderDate) {
		this.gstIsApplicableOnSalesOrderDate = gstIsApplicableOnSalesOrderDate;
	}

	public void setOctroiLbtEntryTaxIsApplicableOnSalesOrderDate(Boolean octroiLbtEntryTaxIsApplicableOnSalesOrderDate) {
		this.octroiLbtEntryTaxIsApplicableOnSalesOrderDate = octroiLbtEntryTaxIsApplicableOnSalesOrderDate;
	}

	public void setReferredByCustomer(Customer referredByCustomer) {
		this.referredByCustomer = referredByCustomer;
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

	public void setOctroiLbtEntryTaxApplicableOnSalesOrderDate(Double octroiLbtEntryTaxApplicableOnSalesOrderDate) {
		this.octroiLbtEntryTaxApplicableOnSalesOrderDate = octroiLbtEntryTaxApplicableOnSalesOrderDate;
	}

	public void setGstRateApplicableOnSalesOrderDate(Double gstRateApplicableOnSalesOrderDate) {
		this.gstRateApplicableOnSalesOrderDate = gstRateApplicableOnSalesOrderDate;
	}

	public void setCstRateAgainstFormCOnSalesOrderDate(Double cstRateAgainstFormCOnSalesOrderDate) {
		this.cstRateAgainstFormCOnSalesOrderDate = cstRateAgainstFormCOnSalesOrderDate;
	}
	
	public void setVatRateApplicableOnSalesOrderDate(Double vatRateApplicableOnSalesOrderDate) {
		this.vatRateApplicableOnSalesOrderDate = vatRateApplicableOnSalesOrderDate;
	}
	
	public void setCstRateApplicableOnSalesOrderDate(Double cstRateApplicableOnSalesOrderDate) {
		this.cstRateApplicableOnSalesOrderDate = cstRateApplicableOnSalesOrderDate;
	}
	
	public void setMarkDownOnSalesOrderDate(Double markDownOnSalesOrderDate) {
		this.markDownOnSalesOrderDate = markDownOnSalesOrderDate;
	}

	public void setCashDiscountOnSalesOrderDate(Double cashDiscountOnSalesOrderDate) {
		this.cashDiscountOnSalesOrderDate = cashDiscountOnSalesOrderDate;
	}

	public void setPromptPaymentDiscountOnSalesOrderDate(Double promptPaymentDiscountOnSalesOrderDate) {
		this.promptPaymentDiscountOnSalesOrderDate = promptPaymentDiscountOnSalesOrderDate;
	}

	public void setSpecialDiscountOnSalesOrderDate(Double specialDiscountOnSalesOrderDate) {
		this.specialDiscountOnSalesOrderDate = specialDiscountOnSalesOrderDate;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
	}

	@Override
	public int compareTo(SalesOrder salesOrder) {
		long compareQuantity = ((SalesOrder) salesOrder).getSalesOrderId();

		//ascending order
		return (int) (this.salesOrderId - compareQuantity);

		//descending order
		//return compareQuantity - this.quantity;
	}
}
