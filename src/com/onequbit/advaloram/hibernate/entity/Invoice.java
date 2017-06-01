package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;
import java.util.Set;

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
		uniqueConstraints = {@UniqueConstraint(columnNames = {"linked_sales_order", "linked_packing_list", "invoice_revision_number"})}	//implies that combination of salesOrder and packing list mut be unique. 
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
	
	public String clientNameOnInvoiceDate; // stored in separate field to avoid error in case client name gets EDITED in MASTERS later
	
	public String clientBillingAddressOnInvoiceDate;
	
	public String clientDeliveryAddressOnInvoiceDate;
	
	public String clientCityOnInvoiceDate;
	
	public String clientPinCodeOnInvoiceDate;
	
	public String clientDistrictOnInvoiceDate;
	
	public String clientStateOnInvoiceDate;
	
	public String clientContactPersonOnInvoiceDate;
	
	public String clientContactNumberOnInvoiceDate;
	
	public String clientEmailAddressOnInvoiceDate;
	
	public String transportName;
	
	public Double markDownOnInvoiceDate;
	
	public Double cashDiscountOnInvoiceDate;
	
	public Double promptPaymentDiscountOnInvoiceDate;
	
	public Double specialDiscountOnInvoiceDate;
	
	public Double cstRateApplicableOnInvoiceDate;
	public Double vatRateApplicableOnInvoiceDate;
	public Double cstRateAgainstFormCOnInvoiceDate;
	public Double gstRateApplicableOnInvoiceDate;
	public Double octroiLbtEntryTaxApplicableOnInvoiceDate;
	
	public Boolean vatIsApplicableOnInvoiceDate;
	public Boolean cstIsApplicableOnInvoiceDate;
	public Boolean gstIsApplicableOnInvoiceDate;
	public Boolean octroiLbtEntryTaxIsApplicableOnInvoiceDate;
	
	public String vatTinNumberOnInvoiceDate;
	public String gstNumberOnInvoiceDate;
	public String cstTinNumberOnInvoiceDate;
	public String serviceTaxNumberOnInvoiceDate;
	public String panNumberOnInvoiceDate;
	
	
	public enum Status {
		OPEN, CONFIRMED, REVISED, CANCELLED, UNDER_REVISION
	}
	
	@Column(name="invoice_id")
	public long getInvoiceId() {
		return invoiceId;
	}

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
	
	@Column(name="client_name_on_invoice_date")
	public String getClientNameOnInvoiceDate() {
		return clientNameOnInvoiceDate;
	}

	@Column(name="client_billing_address_on_invoice_date")
	public String getClientBillingAddressOnInvoiceDate() {
		return clientBillingAddressOnInvoiceDate;
	}
	
	@Column(name="client_delivery_address_on_invoice_date")
	public String getClientDeliveryAddressOnInvoiceDate() {
		return clientDeliveryAddressOnInvoiceDate;
	}

	@Column(name="client_city_on_invoice_date")
	public String getClientCityOnInvoiceDate() {
		return clientCityOnInvoiceDate;
	}

	@Column(name="client_pin_code_on_invoice_date")
	public String getClientPinCodeOnInvoiceDate() {
		return clientPinCodeOnInvoiceDate;
	}

	@Column(name="client_district_on_invoice_date")
	public String getClientDistrictOnInvoiceDate() {
		return clientDistrictOnInvoiceDate;
	}

	@Column(name="client_state_on_invoice_date")
	public String getClientStateOnInvoiceDate() {
		return clientStateOnInvoiceDate;
	}

	@Column(name="client_contact_person_on_invoice_date")
	public String getClientContactPersonOnInvoiceDate() {
		return clientContactPersonOnInvoiceDate;
	}

	@Column(name="client_contact_number_on_invoice_date")
	public String getClientContactNumberOnInvoiceDate() {
		return clientContactNumberOnInvoiceDate;
	}

	@Column(name="client_email_address_on_invoice_date")
	public String getClientEmailAddressOnInvoiceDate() {
		return clientEmailAddressOnInvoiceDate;
	}

	@Column(name="mark_down_on_invoice_date")
	public Double getMarkDownOnInvoiceDate() {
		return markDownOnInvoiceDate;
	}

	@Column(name="cash_discount_on_invoice_date")
	public Double getCashDiscountOnInvoiceDate() {
		return cashDiscountOnInvoiceDate;
	}

	@Column(name="prompt_payment_discount_on_invoice_date")
	public Double getPromptPaymentDiscountOnInvoiceDate() {
		return promptPaymentDiscountOnInvoiceDate;
	}

	@Column(name="special_discount_on_invoice_date")
	public Double getSpecialDiscountOnInvoiceDate() {
		return specialDiscountOnInvoiceDate;
	}

	@Column(name="cst_rate_applicable_on_invoice_date")
	public Double getCstRateApplicableOnInvoiceDate() {
		return cstRateApplicableOnInvoiceDate;
	}

	@Column(name="vat_rate_applicable_on_invoice_date")
	public Double getVatRateApplicableOnInvoiceDate() {
		return vatRateApplicableOnInvoiceDate;
	}

	@Column(name="cst_rate_against_form_c_on_invoice_date")
	public Double getCstRateAgainstFormCOnInvoiceDate() {
		return cstRateAgainstFormCOnInvoiceDate;
	}

	@Column(name="gst_rate_applicable_on_invoice_date")
	public Double getGstRateApplicableOnInvoiceDate() {
		return gstRateApplicableOnInvoiceDate;
	}

	@Column(name="octroi_lbt_entry_tax_applicable_on_invoice_date")
	public Double getOctroiLbtEntryTaxApplicableOnInvoiceDate() {
		return octroiLbtEntryTaxApplicableOnInvoiceDate;
	}

	@Column(name="is_vat_applicable_on_invoice_date")
	public Boolean getVatIsApplicableOnInvoiceDate() {
		return vatIsApplicableOnInvoiceDate;
	}

	@Column(name="is_cst_applicable_on_invoice_date")
	public Boolean getCstIsApplicableOnInvoiceDate() {
		return cstIsApplicableOnInvoiceDate;
	}

	@Column(name="is_gst_applicable_on_invoice_date")
	public Boolean getGstIsApplicableOnInvoiceDate() {
		return gstIsApplicableOnInvoiceDate;
	}

	@Column(name="is_octroi_lbt_entry_tax_applicable_on_invoice_date")
	public Boolean getOctroiLbtEntryTaxIsApplicableOnInvoiceDate() {
		return octroiLbtEntryTaxIsApplicableOnInvoiceDate;
	}

	@Column(name="vat_tin_number_on_invoice_date")
	public String getVatTinNumberOnInvoiceDate() {
		return vatTinNumberOnInvoiceDate;
	}

	@Column(name="gst_number_on_invoice_date")
	public String getGstNumberOnInvoiceDate() {
		return gstNumberOnInvoiceDate;
	}

	@Column(name="cst_tin_number_on_invoice_date")
	public String getCstTinNumberOnInvoiceDate() {
		return cstTinNumberOnInvoiceDate;
	}

	@Column(name="service_tax_number_on_invoice_date")
	public String getServiceTaxNumberOnInvoiceDate() {
		return serviceTaxNumberOnInvoiceDate;
	}

	@Column(name="pan_number_on_invoice_date")
	public String getPanNumberOnInvoiceDate() {
		return panNumberOnInvoiceDate;
	}
	
	@Column(name="transport_name")
	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public void setClientNameOnInvoiceDate(String clientNameOnInvoiceDate) {
		this.clientNameOnInvoiceDate = clientNameOnInvoiceDate;
	}

	public void setClientBillingAddressOnInvoiceDate(String clientBillingAddressOnInvoiceDate) {
		this.clientBillingAddressOnInvoiceDate = clientBillingAddressOnInvoiceDate;
	}

	public void setClientDeliveryAddressOnInvoiceDate(String clientDeliveryAddressOnInvoiceDate) {
		this.clientDeliveryAddressOnInvoiceDate = clientDeliveryAddressOnInvoiceDate;
	}

	public void setClientCityOnInvoiceDate(String clientCityOnInvoiceDate) {
		this.clientCityOnInvoiceDate = clientCityOnInvoiceDate;
	}

	public void setClientPinCodeOnInvoiceDate(String clientPinCodeOnInvoiceDate) {
		this.clientPinCodeOnInvoiceDate = clientPinCodeOnInvoiceDate;
	}

	public void setClientDistrictOnInvoiceDate(String clientDistrictOnInvoiceDate) {
		this.clientDistrictOnInvoiceDate = clientDistrictOnInvoiceDate;
	}

	public void setClientStateOnInvoiceDate(String clientStateOnInvoiceDate) {
		this.clientStateOnInvoiceDate = clientStateOnInvoiceDate;
	}

	public void setClientContactPersonOnInvoiceDate(String clientContactPersonOnInvoiceDate) {
		this.clientContactPersonOnInvoiceDate = clientContactPersonOnInvoiceDate;
	}

	public void setClientContactNumberOnInvoiceDate(String clientContactNumberOnInvoiceDate) {
		this.clientContactNumberOnInvoiceDate = clientContactNumberOnInvoiceDate;
	}

	public void setClientEmailAddressOnInvoiceDate(String clientEmailAddressOnInvoiceDate) {
		this.clientEmailAddressOnInvoiceDate = clientEmailAddressOnInvoiceDate;
	}

	public void setMarkDownOnInvoiceDate(Double markDownOnInvoiceDate) {
		this.markDownOnInvoiceDate = markDownOnInvoiceDate;
	}

	public void setCashDiscountOnInvoiceDate(Double cashDiscountOnInvoiceDate) {
		this.cashDiscountOnInvoiceDate = cashDiscountOnInvoiceDate;
	}

	public void setPromptPaymentDiscountOnInvoiceDate(Double promptPaymentDiscountOnInvoiceDate) {
		this.promptPaymentDiscountOnInvoiceDate = promptPaymentDiscountOnInvoiceDate;
	}

	public void setSpecialDiscountOnInvoiceDate(Double specialDiscountOnInvoiceDate) {
		this.specialDiscountOnInvoiceDate = specialDiscountOnInvoiceDate;
	}

	public void setCstRateApplicableOnInvoiceDate(Double cstRateApplicableOnInvoiceDate) {
		this.cstRateApplicableOnInvoiceDate = cstRateApplicableOnInvoiceDate;
	}

	public void setVatRateApplicableOnInvoiceDate(Double vatRateApplicableOnInvoiceDate) {
		this.vatRateApplicableOnInvoiceDate = vatRateApplicableOnInvoiceDate;
	}

	public void setCstRateAgainstFormCOnInvoiceDate(Double cstRateAgainstFormCOnInvoiceDate) {
		this.cstRateAgainstFormCOnInvoiceDate = cstRateAgainstFormCOnInvoiceDate;
	}

	public void setGstRateApplicableOnInvoiceDate(Double gstRateApplicableOnInvoiceDate) {
		this.gstRateApplicableOnInvoiceDate = gstRateApplicableOnInvoiceDate;
	}

	public void setOctroiLbtEntryTaxApplicableOnInvoiceDate(Double octroiLbtEntryTaxApplicableOnInvoiceDate) {
		this.octroiLbtEntryTaxApplicableOnInvoiceDate = octroiLbtEntryTaxApplicableOnInvoiceDate;
	}

	public void setVatIsApplicableOnInvoiceDate(Boolean vatIsApplicableOnInvoiceDate) {
		this.vatIsApplicableOnInvoiceDate = vatIsApplicableOnInvoiceDate;
	}

	public void setCstIsApplicableOnInvoiceDate(Boolean cstIsApplicableOnInvoiceDate) {
		this.cstIsApplicableOnInvoiceDate = cstIsApplicableOnInvoiceDate;
	}

	public void setGstIsApplicableOnInvoiceDate(Boolean gstIsApplicableOnInvoiceDate) {
		this.gstIsApplicableOnInvoiceDate = gstIsApplicableOnInvoiceDate;
	}

	public void setOctroiLbtEntryTaxIsApplicableOnInvoiceDate(Boolean octroiLbtEntryTaxIsApplicableOnInvoiceDate) {
		this.octroiLbtEntryTaxIsApplicableOnInvoiceDate = octroiLbtEntryTaxIsApplicableOnInvoiceDate;
	}

	public void setVatTinNumberOnInvoiceDate(String vatTinNumberOnInvoiceDate) {
		this.vatTinNumberOnInvoiceDate = vatTinNumberOnInvoiceDate;
	}

	public void setGstNumberOnInvoiceDate(String gstNumberOnInvoiceDate) {
		this.gstNumberOnInvoiceDate = gstNumberOnInvoiceDate;
	}

	public void setCstTinNumberOnInvoiceDate(String cstTinNumberOnInvoiceDate) {
		this.cstTinNumberOnInvoiceDate = cstTinNumberOnInvoiceDate;
	}

	public void setServiceTaxNumberOnInvoiceDate(String serviceTaxNumberOnInvoiceDate) {
		this.serviceTaxNumberOnInvoiceDate = serviceTaxNumberOnInvoiceDate;
	}

	public void setPanNumberOnInvoiceDate(String panNumberOnInvoiceDate) {
		this.panNumberOnInvoiceDate = panNumberOnInvoiceDate;
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
