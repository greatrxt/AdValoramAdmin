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

import com.onequbit.advaloram.hibernate.entity.SalesOrder.Status;

@Entity
@Table(name = "packing_list")
public class PackingList extends AbstractAdValoramEntity {
	
	/**
	 * 
	 */
	

	public long id;
	
	private static final long serialVersionUID = 7133812177437764261L;

	public long packingListId; //will be useful in case of multiple revisions
	
	public String clientNameOnPackingListDate;

	public Customer linkedCustomer;
	
	public int packingListRevisionNumber;
	
	public Date packingListDate;
	
	public String purchaseOrderReference;
	
	public Long linkedSalesOrderId;	//not linked using SalesOrder since a single sales order can have multiple entries in the form of revisions
	
	public Set<PackingListEntry> entry;
	
	public String notes;
	
	public Set<File> associatedFiles;
	
	public Status status;

	public String revisionReason;
	
	public enum Status {
		OPEN, CONFIRMED, DISPATCHED, CANCELLED 
	}
		
	@Column(name="id")
	public Long getId() {
		return id;
	}

	@Column(name="packing_list_id")
	public long getPackingListId() {
		return packingListId;
	}

	@Column(name="packing_list_revision_number")
	public int getPackingListRevisionNumber() {
		return packingListRevisionNumber;
	}

	@Column(name="packing_list_date")
	public Date getPackingListDate() {
		return packingListDate;
	}

	@Column(name="purchase_order_reference")
	public String getPurchaseOrderReference() {
		return purchaseOrderReference;
	}


	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "packinglist_entry_mapping", joinColumns = { @JoinColumn(name = "packing_list_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id", nullable = false, updatable = false) })
	public Set<PackingListEntry> getEntry() {
		return entry;
	}


	@Column(name="notes")
	public String getNotes() {
		return notes;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch (FetchMode.SELECT)
	@JoinTable(name = "packinglist_file", catalog = "public", joinColumns = { @JoinColumn(name = "packing_list_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "file_id", nullable = false, updatable = false) })
	public Set<File> getAssociatedFiles() {
		return associatedFiles;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	@Column(name="client_name_on_packing_list_date")
	public String getClientNameOnPackingListDate() {
		return clientNameOnPackingListDate;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="linked_customer", nullable = false, referencedColumnName = "company_name")
	public Customer getLinkedCustomer() {
		return linkedCustomer;
	}
	
	@Column(name="linked_sales_order_id")	//not linked using SalesOrder since a single sales order can have multiple entries in the form of revisions
	public Long getLinkedSalesOrderId() {
		return linkedSalesOrderId;
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

	public void setLinkedSalesOrderId(Long linkedSalesOrderId) {
		this.linkedSalesOrderId = linkedSalesOrderId;
	}

	public void setLinkedCustomer(Customer linkedCustomer) {
		this.linkedCustomer = linkedCustomer;
	}

	public void setClientNameOnPackingListDate(String clientNameOnPackingListDate) {
		this.clientNameOnPackingListDate = clientNameOnPackingListDate;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setEntry(Set<PackingListEntry> entry) {
		this.entry = entry;
	}

	public void setPackingListId(long packingListId) {
		this.packingListId = packingListId;
	}

	public void setPackingListRevisionNumber(int packingListRevisionNumber) {
		this.packingListRevisionNumber = packingListRevisionNumber;
	}

	public void setPackingListDate(Date packingListDate) {
		this.packingListDate = packingListDate;
	}

	public void setPurchaseOrderReference(String purchaseOrderReference) {
		this.purchaseOrderReference = purchaseOrderReference;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setAssociatedFiles(Set<File> associatedFiles) {
		this.associatedFiles = associatedFiles;
	}
	
	
}
