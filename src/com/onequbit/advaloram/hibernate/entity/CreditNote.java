package com.onequbit.advaloram.hibernate.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "credit_note")
public class CreditNote extends AbstractAdValoramEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = -5397276579704165490L;
	
	public long id;
	
	public Invoice linkedInvoice;
	
	public long creditNoteId;
	
	public int creditNoteRevisionNumber;
	
	public long numberOfPackages;
	
	public double grossWeight;
	
	public String lorryReceiptNumber;

	public Status status;
	
	public String returnReason;
	
	public String returnNote;
	
	public Date returnDate;
	
	public Set<CreditNoteEntry> entry;

	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_invoice", nullable = true, referencedColumnName = "id")
	public Invoice getLinkedInvoice() {
		return linkedInvoice;
	}

	@Column(name="credit_note_id")
	public long getCreditNoteId() {
		return creditNoteId;
	}

	@Column(name="credit_note_revision_number")
	public int getCreditNoteRevisionNumber() {
		return creditNoteRevisionNumber;
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

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	@Column(name="return_reason")
	public String getReturnReason() {
		return returnReason;
	}

	@Column(name="return_note")
	public String getReturnNote() {
		return returnNote;
	}

	@Column(name="return_date")
	public Date getReturnDate() {
		return returnDate;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "creditnote_entry_mapping", joinColumns = { @JoinColumn(name = "id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id", nullable = false, updatable = false) })
	public Set<CreditNoteEntry> getEntry() {
		return entry;
	}

	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setLinkedInvoice(Invoice linkedInvoice) {
		this.linkedInvoice = linkedInvoice;
	}

	public void setCreditNoteId(long creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public void setCreditNoteRevisionNumber(int creditNoteRevisionNumber) {
		this.creditNoteRevisionNumber = creditNoteRevisionNumber;
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

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public void setEntry(Set<CreditNoteEntry> entry) {
		this.entry = entry;
	}

	public enum Status {
		OPEN, ISSUED, CANCELLED 
	}
	
	
}
