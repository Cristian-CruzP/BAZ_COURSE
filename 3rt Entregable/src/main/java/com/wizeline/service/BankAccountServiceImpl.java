/**
 * 
 */
package com.wizeline.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.wizeline.enums.Country;
import com.wizeline.model.BankAccountDTO;
import com.wizeline.repository.BankingAccountRepository;
import com.wizeline.repository.UserRepositoryImpl;
import com.wizeline.utils.Utils;

/**
 * @author lsalgadot
 *
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {
	
	@Autowired
	private BankingAccountRepository bankAccountRepository;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final Logger LOGGER = Logger.getLogger(BankAccountServiceImpl.class.getName());

	@Override
	public List<BankAccountDTO> getAccounts() {
	    // Definicion de lista con la informacion de las cuentas existentes.
	    List<BankAccountDTO> accountDTOList = new ArrayList<>();
	    BankAccountDTO bankAccountOne = buildBankAccount("user3@wizeline.com", true, Country.MX, LocalDateTime.now().minusDays(7));
	    accountDTOList.add(bankAccountOne);

	    //Guardar cada record en la db de mongo (en la coleccion bankAccountCollection)
	    mongoTemplate.save(bankAccountOne);

	    BankAccountDTO bankAccountTwo = buildBankAccount("user1@wizeline.com", false, Country.FR, LocalDateTime.now().minusMonths(2));
	    accountDTOList.add(bankAccountTwo);

	    //Guardar cada record en la db de mongo (en la coleccion bankAccountCollection)
	    mongoTemplate.save(bankAccountTwo);

	    BankAccountDTO bankAccountThree = buildBankAccount("user2@wizeline.com" ,false, Country.US, LocalDateTime.now().minusYears(4));
	    accountDTOList.add(bankAccountThree);

	    //Guardar cada record en la db de mongo (en la coleccion bankAccountCollection)
	    mongoTemplate.save(bankAccountThree);

	    //Imprime en la Consola cuales son los records encontrados en la coleccion
	    //bankAccountCollection de la mongo db
	    mongoTemplate.findAll(BankAccountDTO.class).stream().map(bankAccountDTO -> bankAccountDTO.getUser()).forEach(
	            (user) -> {
	                    LOGGER.info("User stored in bankAccountCollection " + user );
	            });

	    //Esta es la respuesta que se retorna al Controlador
	    //y que sera desplegada cuando se haga la llamada a los
	    //REST endpoints que la invocan (un ejemplo es el endpoint de  getAccounts)
	    return accountDTOList;
	}

	@Override
	public BankAccountDTO getAccountDetails(String user, String lastUsage) {
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate usage = LocalDate.parse(lastUsage, dateformatter);
		return buildBankAccount(user, true, Country.MX, usage.atStartOfDay());
	}

// Creaci�n de tipo de dato BankAccount
	private BankAccountDTO buildBankAccount(String user, boolean isActive, Country country, LocalDateTime lastUsage) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setAccountNumber(Utils.randomAcountNumber());
        bankAccountDTO.setAccountName("Dummy Account ".concat(Utils.randomInt()));
        bankAccountDTO.setUser(user);
        bankAccountDTO.setAccountBalance(Utils.randomBalance());
        bankAccountDTO.setAccountType(Utils.pickRandomAccountType());
        bankAccountDTO.setCountry(Utils.getCountry(country));
        bankAccountDTO.getLastUsage(); // <- Se invoca al metodo de acceso get() para obtener la fecha actual
        bankAccountDTO.setCreationDate(lastUsage);
        bankAccountDTO.setAccountActive(isActive);
	    
		return bankAccountDTO;
	}
	

	@Override
	public void deleteAccounts() {
		// Deleting all records inside of bankAccountCollection in the mongo db
		bankAccountRepository.deleteAll();
	}
	
	@Override
	   public List<BankAccountDTO> getAccountByUser(String user) {
	       //Buscamos todos aquellos registros de tipo BankAccountDTO
	       //que cumplen con la criteria de que el userName haga match
	       //con la variable user
	       Query query = new Query();
	       query.addCriteria(Criteria.where("user").is(user));
	       return mongoTemplate.find(query, BankAccountDTO.class);
	   }

}