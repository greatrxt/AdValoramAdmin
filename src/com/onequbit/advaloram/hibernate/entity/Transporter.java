package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "transporter")
public class Transporter extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -6908383759672766436L;
	//public long id;
	
	public String companyName;
	public String permitType;
	public String billingAddress, localHubAddress;
	
	public Location city; //not saving district and state. Can be figured out from location
	public String pinCode;
	
	public String contactPerson, contactNumber, alternateContactPerson, alternateContactNumber;
	
	public String emailAddress;
	
	public Integer numberOfVehicles;
	
	public String bankingName, bankName, bankAccountNumber, bankIfscCode, serviceTaxNumber, panNumber, modeOfPayment;
	
	public boolean taxIsInclusive, taxIsExclusive;
	
	public String notes, filesMetaData;
	public Set<File> associatedFiles;
	
	//public Date recordCreationTime;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}*/

	@Column(name="company_name")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name="permit_type")
	public String getPermitType() {
		return permitType;
	}

	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}

	@Column(name="billing_address")
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Column(name="local_hub_address")
	public String getLocalHubAddress() {
		return localHubAddress;
	}

	public void setLocalHubAddress(String localHubAddress) {
		this.localHubAddress = localHubAddress;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="city_name", nullable = false, referencedColumnName = "city_name")
	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "transporter_file", catalog = "public", joinColumns = { @JoinColumn(name = "transporter_id", nullable = true, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "file_id", nullable = true, updatable = false) })
	public Set<File> getAssociatedFiles() {
		return associatedFiles;
	}
	

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
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

	@Column(name="alternate_contact_person")
	public String getAlternateContactPerson() {
		return alternateContactPerson;
	}

	public void setAlternateContactPerson(String alternateContactPerson) {
		this.alternateContactPerson = alternateContactPerson;
	}

	@Column(name="alternate_contact_number")
	public String getAlternateContactNumber() {
		return alternateContactNumber;
	}

	public void setAlternateContactNumber(String alternateContactNumber) {
		this.alternateContactNumber = alternateContactNumber;
	}

	@Column(name="email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name="number_of_vehicles")
	public Integer getNumberOfVehicles() {
		return numberOfVehicles;
	}

	public void setNumberOfVehicles(Integer numberOfVehicles) {
		this.numberOfVehicles = numberOfVehicles;
	}

	@Column(name="banking_name")
	public String getBankingName() {
		return bankingName;
	}

	public void setBankingName(String bankingName) {
		this.bankingName = bankingName;
	}

	@Column(name="bank_name")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name="bank_account_number")
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	@Column(name="bank_ifsc_code")
	public String getBankIfscCode() {
		return bankIfscCode;
	}

	public void setBankIfscCode(String bankIfscCode) {
		this.bankIfscCode = bankIfscCode;
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

	@Column(name="mode_of_payment")
	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	@Column(name="is_tax_inclusive")
	public boolean isTaxIsInclusive() {
		return taxIsInclusive;
	}

	public void setTaxIsInclusive(boolean taxIsInclusive) {
		this.taxIsInclusive = taxIsInclusive;
	}

	@Column(name="is_tax_exclusive")
	public boolean isTaxIsExclusive() {
		return taxIsExclusive;
	}

	public void setTaxIsExclusive(boolean taxIsExclusive) {
		this.taxIsExclusive = taxIsExclusive;
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
}
