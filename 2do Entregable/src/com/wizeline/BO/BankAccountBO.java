/**
 * 
 */
package com.wizeline.BO;

import java.util.List;

import com.wizeline.DTO.BankAccountDTO;

/**
 * @author lsalgadot
 *
 */
public interface BankAccountBO {
	List<BankAccountDTO> getAccounts(); // <- Regresa una lista de tipo BankAccountDTO
	BankAccountDTO getAccountDetails(String user, String lastUsage);
}
