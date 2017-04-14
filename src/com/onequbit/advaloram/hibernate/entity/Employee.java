package com.onequbit.advaloram.hibernate.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "employee")
public class Employee extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -5059377149015218370L;

	public long id;
	
	public String salutation, firstName, lastName, fatherName, motherName, permanentAddress, correspondenceAddress;
	
	public Location city;
	
	public String pinCode;
	
	public String contactNumber, emailAddress, alternateContactName, alternateContactNumber;
	
	public String 	designation, employeeType, jobBand, grade, department;
	
	public Employee reportingTo;
	
	public String jobRolesAndResponsibilities;
	
	public String bankName, bankAccountNumber, ifscCode, permanentAccountNumber, employeeProvidentFundNumber, universalAccountNumber;
	
	public String notes, filesMetadata;

	public Set<File> associatedFiles;
	
	//public Date recordCreationTime;

	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	
	@Column(name="salutation")
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	@Column(name="first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name="father_name")
	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	@Column(name="mother_name")
	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	@Column(name="permanent_address")
	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	@Column(name="correspondence_address")
	public String getCorrespondenceAddress() {
		return correspondenceAddress;
	}

	public void setCorrespondenceAddress(String correspondenceAddress) {
		this.correspondenceAddress = correspondenceAddress;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="city_name", nullable = false, referencedColumnName = "city_name")
	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}

	@Column(name="pincode")
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
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

	@Column(name="alternate_contact_name")
	public String getAlternateContactName() {
		return alternateContactName;
	}

	public void setAlternateContactName(String alternateContactName) {
		this.alternateContactName = alternateContactName;
	}

	@Column(name="alternate_contact_number")
	public String getAlternateContactNumber() {
		return alternateContactNumber;
	}

	public void setAlternateContactNumber(String alternateContactNumber) {
		this.alternateContactNumber = alternateContactNumber;
	}

	@Column(name="designation")
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Column(name="employee_type")
	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	@Column(name="job_band")
	public String getJobBand() {
		return jobBand;
	}

	public void setJobBand(String jobBand) {
		this.jobBand = jobBand;
	}
	
	@Column(name="grade")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Column(name="department")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="reporting_to", nullable = true, referencedColumnName = "id")
	public Employee getReportingTo() {
		return reportingTo;
	}

	public void setReportingTo(Employee reportingTo) {
		this.reportingTo = reportingTo;
	}

	@Column(name="job_roles_and_responsibilities")
	public String getJobRolesAndResponsibilities() {
		return jobRolesAndResponsibilities;
	}

	public void setJobRolesAndResponsibilities(String jobRolesAndResponsibilities) {
		this.jobRolesAndResponsibilities = jobRolesAndResponsibilities;
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

	@Column(name="ifsc_code")
	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Column(name="permanent_account_number")
	public String getPermanentAccountNumber() {
		return permanentAccountNumber;
	}

	public void setPermanentAccountNumber(String permanentAccountNumber) {
		this.permanentAccountNumber = permanentAccountNumber;
	}

	@Column(name="employee_provident_fund_number")
	public String getEmployeeProvidentFundNumber() {
		return employeeProvidentFundNumber;
	}

	public void setEmployeeProvidentFundNumber(String employeeProvidentFundNumber) {
		this.employeeProvidentFundNumber = employeeProvidentFundNumber;
	}

	@Column(name="universal_account_number")
	public String getUniversalAccountNumber() {
		return universalAccountNumber;
	}

	public void setUniversalAccountNumber(String universalAccountNumber) {
		this.universalAccountNumber = universalAccountNumber;
	}

	@Column(name="notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "employee_file", catalog = "public", joinColumns = { @JoinColumn(name = "employee_id", nullable = true, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "file_id", nullable = true, updatable = false) })
	public Set<File> getAssociatedFiles() {
		return associatedFiles;
	}
	

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
	}
	
	@Column(name="files_meta_data")
	public String getFilesMetadata() {
		return filesMetadata;
	}

	public void setFilesMetadata(String filesMetadata) {
		this.filesMetadata = filesMetadata;
	}

}
