package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 4610067445012757575L;

	//public long id;
	
	public String companyName;
	public String customerType;
	public Customer linkedDistributor;
	public Customer linkedBroker;
	public String billingAddress;
	public String deliveryAddress;
	
	public Location city; //not saving district and state. Can be figured out from location
	public String pinCode;
	
	public String contactPerson;
	public String contactNumber;
	
	public String emailAddress;

	public float markDown;
	public float cashDiscount;
	public float promptPaymentDiscount;
	public float specialDiscount;
	public float distributorCommission;
	public float brokerCommission;
	public int creditPeriod, creditLimit;
	
	public String paymentTerms;
	
	public String financialGrouping;
	public String vatTinNumber;
	public String cstTinNumber;
	public String serviceTaxNumber;
	public String panNumber;
	
	public boolean vatIsApplicable;
	public boolean cstIsApplicable;
	public boolean gstIsApplicable;
	public boolean octroiLbtEntryTaxIsApplicable;
	
	public float cstRateApplicable;
	public float vatRateApplicable;
	public float cstRateAgainstFormC;
	public float gstRateApplicable;
	public float octroiLbtEntryTaxApplicable;
	
	public Transporter linkedTransporter;
	
	public String transportContactPerson, transportContactNumber, transportEmailAddress, transportContractTerms, transportPaymentTerms;
	
	public String specialInstructions, notes, filesMetaData;
	
	@Column(name="company_name")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_distributor", nullable = true, referencedColumnName = "id")
	public Customer getLinkedDistributor() {
		return linkedDistributor;
	}

	public void setLinkedDistributor(Customer linkedDistributor) {
		this.linkedDistributor = linkedDistributor;
	}

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_broker", nullable = true, referencedColumnName = "id")
	public Customer getLinkedBroker() {
		return linkedBroker;
	}

	public void setLinkedBroker(Customer linkedBroker) {
		this.linkedBroker = linkedBroker;
	}

	@Column(name="customer_type")
	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	@Column(name="billing_address")
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Column(name="delivery_address")
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="city_name", nullable = false, referencedColumnName = "city_name")
	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}

	@Column(name="pin_code")
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	@Column(name="contact_person")
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Column(name="contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Column(name="email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name="notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name="files_meta_data")
	public String getFilesMetaData() {
		return filesMetaData;
	}

	public void setFilesMetaData(String filesMetaData) {
		this.filesMetaData = filesMetaData;
	}

/*	@Temporal(TemporalType.DATE)		//@Temporal: must be used with a java.util.Date field to specify the actual SQL type of the column
	@Column(name="record_creation_time")
	public Date getRecordCreationTime() {
		return recordCreationTime;
	}

	public void setRecordCreationTime(Date recordCreationTime) {
		this.recordCreationTime = recordCreationTime;
	}*/

	@Column(name="mark_down")
	public float getMarkDown() {
		return markDown;
	}

	public void setMarkDown(float markDown) {
		this.markDown = markDown;
	}

	@Column(name="cash_discount")
	public float getCashDiscount() {
		return cashDiscount;
	}

	public void setCashDiscount(float cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	@Column(name="prompt_payment_discount")
	public float getPromptPaymentDiscount() {
		return promptPaymentDiscount;
	}

	public void setPromptPaymentDiscount(float promptPaymentDiscount) {
		this.promptPaymentDiscount = promptPaymentDiscount;
	}

	@Column(name="special_discount")
	public float getSpecialDiscount() {
		return specialDiscount;
	}

	public void setSpecialDiscount(float specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	@Column(name="distributor_commission")
	public float getDistributorCommission() {
		return distributorCommission;
	}

	public void setDistributorCommission(float distributorCommission) {
		this.distributorCommission = distributorCommission;
	}

	@Column(name="broker_commission")
	public float getBrokerCommission() {
		return brokerCommission;
	}

	public void setBrokerCommission(float brokerCommission) {
		this.brokerCommission = brokerCommission;
	}

	@Column(name="credit_period")
	public int getCreditPeriod() {
		return creditPeriod;
	}

	public void setCreditPeriod(int creditPeriod) {
		this.creditPeriod = creditPeriod;
	}

	@Column(name="credit_limit")
	public int getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(int creditLimit) {
		this.creditLimit = creditLimit;
	}

	@Column(name="payment_terms")
	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	@Column(name="financial_grouping")
	public String getFinancialGrouping() {
		return financialGrouping;
	}

	public void setFinancialGrouping(String financialGrouping) {
		this.financialGrouping = financialGrouping;
	}

	@Column(name="vat_tin_number")
	public String getVatTinNumber() {
		return vatTinNumber;
	}

	public void setVatTinNumber(String vatTinNumber) {
		this.vatTinNumber = vatTinNumber;
	}

	@Column(name="cst_tin_number")
	public String getCstTinNumber() {
		return cstTinNumber;
	}

	public void setCstTinNumber(String cstTinNumber) {
		this.cstTinNumber = cstTinNumber;
	}

	@Column(name="service_tax_number")
	public String getServiceTaxNumber() {
		return serviceTaxNumber;
	}

	public void setServiceTaxNumber(String serviceTaxNumber) {
		this.serviceTaxNumber = serviceTaxNumber;
	}

	@Column(name="pan_number")
	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	@Column(name="is_vat_applicable")
	public boolean isVatIsApplicable() {
		return vatIsApplicable;
	}

	public void setVatIsApplicable(boolean vatIsApplicable) {
		this.vatIsApplicable = vatIsApplicable;
	}

	@Column(name="is_cst_applicable")
	public boolean isCstIsApplicable() {
		return cstIsApplicable;
	}

	public void setCstIsApplicable(boolean cstIsApplicable) {
		this.cstIsApplicable = cstIsApplicable;
	}

	@Column(name="is_gst_applicable")
	public boolean isGstIsApplicable() {
		return gstIsApplicable;
	}

	public void setGstIsApplicable(boolean gstIsApplicable) {
		this.gstIsApplicable = gstIsApplicable;
	}

	@Column(name="is_octroi_lbt_entry_tax_applicable")
	public boolean isOctroiLbtEntryTaxIsApplicable() {
		return octroiLbtEntryTaxIsApplicable;
	}

	public void setOctroiLbtEntryTaxIsApplicable(boolean octroiLbtEntryTaxIsApplicable) {
		this.octroiLbtEntryTaxIsApplicable = octroiLbtEntryTaxIsApplicable;
	}

	@Column(name="cst_rate_applicable")
	public float getCstRateApplicable() {
		return cstRateApplicable;
	}

	public void setCstRateApplicable(float cstRateApplicable) {
		this.cstRateApplicable = cstRateApplicable;
	}

	@Column(name="vat_rate_applicable")
	public float getVatRateApplicable() {
		return vatRateApplicable;
	}

	public void setVatRateApplicable(float vatRateApplicable) {
		this.vatRateApplicable = vatRateApplicable;
	}

	@Column(name="cst_rate_against_form_c")
	public float getCstRateAgainstFormC() {
		return cstRateAgainstFormC;
	}

	public void setCstRateAgainstFormC(float cstRateAgainstFormC) {
		this.cstRateAgainstFormC = cstRateAgainstFormC;
	}

	@Column(name="gst_rate_applicable")
	public float getGstRateApplicable() {
		return gstRateApplicable;
	}

	public void setGstRateApplicable(float gstRateApplicable) {
		this.gstRateApplicable = gstRateApplicable;
	}

	@Column(name="octroi_lbt_entry_tax_applicable")
	public float getOctroiLbtEntryTaxApplicable() {
		return octroiLbtEntryTaxApplicable;
	}

	public void setOctroiLbtEntryTaxApplicable(float octroiLbtEntryTaxApplicable) {
		this.octroiLbtEntryTaxApplicable = octroiLbtEntryTaxApplicable;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_transporter", nullable = true, referencedColumnName = "company_name")
	public Transporter getLinkedTransporter() {
		return linkedTransporter;
	}

	public void setLinkedTransporter(Transporter linkedTransporter) {
		this.linkedTransporter = linkedTransporter;
	}

	@Column(name="transport_contact_person")
	public String getTransportContactPerson() {
		return transportContactPerson;
	}

	public void setTransportContactPerson(String transportContactPerson) {
		this.transportContactPerson = transportContactPerson;
	}

	@Column(name="transport_contact_number")
	public String getTransportContactNumber() {
		return transportContactNumber;
	}

	public void setTransportContactNumber(String transportContactNumber) {
		this.transportContactNumber = transportContactNumber;
	}

	@Column(name="transport_email_address")
	public String getTransportEmailAddress() {
		return transportEmailAddress;
	}

	public void setTransportEmailAddress(String transportEmailAddress) {
		this.transportEmailAddress = transportEmailAddress;
	}

	@Column(name="transport_contract_terms")
	public String getTransportContractTerms() {
		return transportContractTerms;
	}

	public void setTransportContractTerms(String transportContractTerms) {
		this.transportContractTerms = transportContractTerms;
	}

	@Column(name="transport_payment_terms")
	public String getTransportPaymentTerms() {
		return transportPaymentTerms;
	}

	public void setTransportPaymentTerms(String transportPaymentTerms) {
		this.transportPaymentTerms = transportPaymentTerms;
	}

	@Column(name="special_instructions")
	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	
}
