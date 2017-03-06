package com.onequbit.advaloram.hibernate.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bank")
public class Bank extends AbstractAdValoramEntity {

	//public long id;
	
	/**
	 * 
	 */
	public static final long serialVersionUID = -8261363292250117393L;
	public String bankName, branchName, 
					branchAddress, emailAddress, 
					relationshipManagerName, 
					contactNumber, alternateContact, alternateContactNumber, ifscCode;
	public Location city;
	//public Date recordCreationTime;
	
/*	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}*/

	@Column(name="bank_name")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name="alternate_contact_number")
	public String getAlternateContactNumber() {
		return alternateContactNumber;
	}

	public void setAlternateContactNumber(String alternateContactNumber) {
		this.alternateContactNumber = alternateContactNumber;
	}

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="city_name", nullable = false, referencedColumnName = "city_name")
	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}

	@Column(name="branch_name")
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Column(name="branch_address")
	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	@Column(name="email_address")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name="relationship_manager_name")
	public String getRelationshipManagerName() {
		return relationshipManagerName;
	}

	public void setRelationshipManagerName(String relationshipManagerName) {
		this.relationshipManagerName = relationshipManagerName;
	}

	@Column(name="contact_number")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Column(name="alternate_contact")
	public String getAlternateContact() {
		return alternateContact;
	}

	public void setAlternateContact(String alternateContact) {
		this.alternateContact = alternateContact;
	}

	@Column(name="ifsc_code")
	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
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
