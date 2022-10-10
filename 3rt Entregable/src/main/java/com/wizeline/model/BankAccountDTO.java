/**
 * 
 */
package com.wizeline.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.wizeline.enums.AccountType;

/**
 * @author lsalgadot
 *
 */
@Document("bankAccountCollection")
public class BankAccountDTO {

	private long accountNumber;
	private String accountName;
	private String user;
	private double accountBalance;
	private AccountType accountType;
	private String country;
	private boolean accountActive;
	private LocalDateTime creationDate; // <- Cambiar a tipo de dato LocalDateTime
	private LocalDateTime lastUsage; // <- Crear nueva propiedad

	/**
	 * @return the accountNumber
	 */
	public long getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the accountBalance
	 */
	public double getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance the accountBalance to set
	 */
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * @return the accountType
	 */
	public AccountType getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the accountActive
	 */
	public boolean isAccountActive() {
		return accountActive;
	}

	/**
	 * @param accountActive the accountActive to set
	 */
	public void setAccountActive(boolean accountActive) {
		this.accountActive = accountActive;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastUsage() {
		return lastUsage;
	}

	public void setLastUsage(LocalDateTime lastUsage) {
		this.lastUsage = LocalDateTime.now();
	}

}
