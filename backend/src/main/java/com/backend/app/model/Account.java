package com.backend.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.backend.app.deserializer.AccountDeserializer;
import com.backend.app.dto.AccountDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "account")
@Data
@EqualsAndHashCode(exclude = { "user" })
@ToString(exclude = { "user" })
@JsonDeserialize(using = AccountDeserializer.class)
public class Account {

	public enum AccountType {
		S, C
	}

	@Id
	@SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", initialValue = 1000000000, allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	@Column(name = "account_id", columnDefinition = "bigint(10)")
	private long accountId;

	@NotEmpty(message = "Branch name may not be empty")
	@Size(min = 3, max = 40, message = "Branch name must be between 2 and 40 characters long")
	@Column(name = "branch_name", columnDefinition = "varchar(40)")
	private String branchName;

	@NotNull(message = "Account Type may not be empty")
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", columnDefinition = "char(1)")
	private AccountType accountType;

	@Min(value = 0L, message = "The Account Balance must be positive")
	@NotNull(message = "Account Balance may not be empty")
	@Column(name = "account_balance", columnDefinition = "Decimal(10,2)")
	private double accountBalance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	protected User user;

	public AccountDto convertToDto() {
		return new AccountDto(this);
	}
}
