package com.onequbit.advaloram.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@MappedSuperclass
public abstract class AbstractAdValoramEntity implements Serializable{	

	public static final long serialVersionUID = -562453167454180506L;


	public Long id = null;
    public Integer version = 0;
    public Date lastUpdate;

    public Date recordCreationTime;
	
    public AdValUser createdByUser;
    
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch (FetchMode.SELECT)
    @JoinColumn(name="created_by_user", nullable = true, referencedColumnName = "username")
    public AdValUser getCreatedBy() {
		return createdByUser;
	}

	public void setCreatedBy(AdValUser createdByUser) {
		this.createdByUser = createdByUser;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 
    @Version
    @Column(name = "version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
    
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
    
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "record_creation_time")
	public Date getRecordCreationTime() {
		return recordCreationTime;
	}

	public void setRecordCreationTime(Date recordCreationTime) {
		this.recordCreationTime = recordCreationTime;
	}
    
    
}
