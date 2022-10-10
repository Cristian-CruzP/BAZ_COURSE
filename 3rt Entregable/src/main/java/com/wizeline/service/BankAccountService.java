/**
 * 
 */
package com.wizeline.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wizeline.model.BankAccountDTO;

/**
 * @author lsalgadot
 *
 */
@Component
public interface BankAccountService {
	List<BankAccountDTO> getAccounts(); // <- Regresa una lista de tipo BankAccountDTO
	BankAccountDTO getAccountDetails(String user, String lastUsage);
	void deleteAccounts();
	List<BankAccountDTO> getAccountByUser(String user);

}
