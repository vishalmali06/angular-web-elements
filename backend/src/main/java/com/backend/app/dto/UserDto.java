package com.backend.app.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.backend.app.deserializer.UserDeserializer;
import com.backend.app.model.User;
import com.backend.app.model.User.Gender;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UserDeserializer.class)
public class UserDto {

	private long userId;
	private String name;
	private String emailId;
	private String mobileNo;
	private String secondaryMobileNo;
	private Date dob;
	private Gender gender;
	private Set<AccountDto> accounts = new HashSet<>();

	public UserDto(User other) {
		this.userId = other.getUserId();
		this.name = other.getName();
		this.emailId = other.getEmailId();
		this.mobileNo = other.getMobileNo();
		this.secondaryMobileNo = other.getSecondaryMobileNo();
		this.dob = other.getDob();
		this.gender = other.getGender();
		this.accounts = new HashSet<>();
		other.getAccounts().forEach(account -> this.accounts.add(account.convertToDto()));
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getSecondaryMobileNo() {
		return secondaryMobileNo;
	}

	public void setSecondaryMobileNo(String secondaryMobileNo) {
		this.secondaryMobileNo = secondaryMobileNo;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Set<AccountDto> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountDto> accounts) {
		this.accounts = accounts;
	}

}
