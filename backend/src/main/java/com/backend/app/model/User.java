package com.backend.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.backend.app.deserializer.UserDeserializer;
import com.backend.app.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
@JsonDeserialize(using = UserDeserializer.class)
public class User {

	public enum Gender {
		M, F
	}

	@Id
	@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", initialValue = 10000, allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@Column(name = "user_id", columnDefinition = "bigint(10)")
	private long userId;

	@Pattern(regexp = "^[a-zA-Z0-9]{0,40}$", message = "Special characters are not allowed")
	@NotEmpty(message = "Name may not be empty")
	@Size(min = 2, max = 40, message = "Name must be between 2-40 characters long")
	@Column(name = "name", columnDefinition = "varchar(40)")
	private String name;

	@NotEmpty(message = "Email ID may not be empty")
	@Email(message = "Invalid email ID format")
	@Column(name = "email_id", columnDefinition = "varchar(100)")
	private String emailId;

	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digit long")
	@NotEmpty(message = "Mobile number may not be empty")
	@Column(name = "mobile_no", columnDefinition = "bigint(10)")
	private String mobileNo;

	@Column(name = "secondary_mobile_no", columnDefinition = "bigint(10)")
	private String secondaryMobileNo = "0";

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "mm/dd/yyyy", lenient = OptBoolean.FALSE)
	@Column(name = "dob")
	private Date dob;

	@NotNull(message = "Gender may not be empty")
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", columnDefinition = "char(1)")
	private Gender gender;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Account> accounts = new HashSet<>();

	public void addAccount(Account account) {
		this.accounts.add(account);
	}

	public UserDto convertToDto() {
		return new UserDto(this);
	}

}
