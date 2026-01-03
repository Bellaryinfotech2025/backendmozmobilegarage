package com.mozworld.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "issues")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String address;
    private String mobileBrand;
    private String mobileModel;
    private String issueType;
    private String customIssue;
    private String trackingId;
    private LocalDateTime createdDate;
    private String status = "Pending";   
    private int progress = 0;  
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobileBrand() {
		return mobileBrand;
	}
	public void setMobileBrand(String mobileBrand) {
		this.mobileBrand = mobileBrand;
	}
	public String getMobileModel() {
		return mobileModel;
	}
	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getCustomIssue() {
		return customIssue;
	}
	public void setCustomIssue(String customIssue) {
		this.customIssue = customIssue;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	 
	
}