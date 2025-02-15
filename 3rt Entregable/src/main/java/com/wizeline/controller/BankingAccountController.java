/**
 * 
 */
package com.wizeline.controller;

/**
 * @author lsalgadot
 *
 */
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.kafka.core.KafkaTemplate;

import com.wizeline.LearningjavagradleApplication;
import com.wizeline.client.AccountsJSONClient;
import com.wizeline.model.BankAccountDTO;
import com.wizeline.model.Post;
import com.wizeline.model.ResponseDTO;
import com.wizeline.service.BankAccountService;
import com.wizeline.utils.CommonServices;
import com.wizeline.utils.Utils;

/**
 * Class Description goes here.
 * Created by enrique.gutierrez on 26/09/22
 */

@RequestMapping("/api")
@RestController
public class BankingAccountController {

    private static final String SUCCESS_CODE = "OK000";

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CommonServices commonServices;
    
    private AccountsJSONClient accountsJSONClient;
    
    @Autowired
    private KafkaTemplate<Object, Object> template;

    private static final Logger LOGGER = Logger.getLogger(LearningjavagradleApplication.class.getName());
    String msgProcPeticion = "LearningJava - Inicia procesamiento de peticion ...";

    @GetMapping("/getUserAccount")
    public ResponseEntity<?> getUserAccount(@RequestParam String user, @RequestParam String password, @RequestParam String date) {
        LOGGER.info(msgProcPeticion);
        Instant inicioDeEjecucion = Instant.now();
        ResponseDTO response = new ResponseDTO();
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseText = "";
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        if (Utils.isDateFormatValid(date)) {
            // Valida el password del usuario (password)
            if (Utils.isPasswordValid(password)) {
                response = commonServices.login(user, password);
                if (response.getCode().equals(SUCCESS_CODE)) {
                    BankAccountDTO bankAccountDTO = getAccountDetails(user, date);
                    Instant finalDeEjecucion = Instant.now();
                    LOGGER.info("LearningJava - Cerrando recursos ...");
                    String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
                    LOGGER.info("Tiempo de respuesta: ".concat(total));
                    return new ResponseEntity<>(bankAccountDTO, responseHeaders, HttpStatus.OK);
                }
            } else {
                Instant finalDeEjecucion = Instant.now();
                LOGGER.info("LearningJava - Cerrando recursos ...");
                String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
                LOGGER.info("Tiempo de respuesta: ".concat(total));
                responseText = "Password Incorrecto";
                return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.OK);
            }
        } else {
            responseText = "Formato de Fecha Incorrecto";
        }
        Instant finalDeEjecucion = Instant.now();
        LOGGER.info("LearningJava - Cerrando recursos ...");
        String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
        LOGGER.info("Tiempo de respuesta: ".concat(total));
        return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/getAccounts")
    public ResponseEntity<List<BankAccountDTO>> getAccounts() {
        LOGGER.info(msgProcPeticion);
        Instant inicioDeEjecucion = Instant.now();
        LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
        List<BankAccountDTO> accounts = bankAccountService.getAccounts();

        Instant finalDeEjecucion = Instant.now();

        LOGGER.info("LearningJava - Cerrando recursos ...");
        String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
        LOGGER.info("Tiempo de respuesta: ".concat(total));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);

    }

    @GetMapping("/getAccountsGroupByType")
    public ResponseEntity<Map<String, List<BankAccountDTO>>> getAccountsGroupByType() {

        LOGGER.info(msgProcPeticion);
        Instant inicioDeEjecucion = Instant.now();

        LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
        List<BankAccountDTO> accounts = bankAccountService.getAccounts();

        // Aqui implementaremos la programación funcional
        Map<String, List<BankAccountDTO>> groupedAccounts;
        Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
        groupedAccounts = accounts.stream().collect(Collectors.groupingBy(groupFunction));
        Instant finalDeEjecucion = Instant.now();

        LOGGER.info("LearningJava - Cerrando recursos ...");
        String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
        LOGGER.info("Tiempo de respuesta: ".concat(total));

        return new ResponseEntity<>(groupedAccounts, HttpStatus.OK);
    }


    private BankAccountDTO getAccountDetails(String user, String lastUsage) {
        return bankAccountService.getAccountDetails(user, lastUsage);
    }
    
    @GetMapping("/getAccountByUser")
    public ResponseEntity<List<BankAccountDTO>> getAccountByUser(@RequestParam String user) {
        LOGGER.info(msgProcPeticion);
        Instant inicioDeEjecucion = Instant.now();
        LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
        List<BankAccountDTO> accounts = bankAccountService.getAccountByUser(user);

        Instant finalDeEjecucion = Instant.now();

        LOGGER.info("LearningJava - Cerrando recursos ...");
        String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
        LOGGER.info("Tiempo de respuesta: ".concat(total));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);
    }
    
  //The usage of FeignClient for demo purposes
	@GetMapping("/getExternalUser/{userId}")
	public ResponseEntity<Post> getExternalUser(@PathVariable Long userId) {

		Post postTest = accountsJSONClient.getPostById(userId);
		LOGGER.info("Getting post userId..." + postTest.getUserId());
		LOGGER.info("Getting post body..." + postTest.getBody());
		LOGGER.info("Getting post title..." + postTest.getTitle());
		postTest.setUserId("External user " + Utils.randomAcountNumber());
		postTest.setBody("No info in accountBalance since it is an external user");
		postTest.setTitle("No info in title since it is an external user");
		LOGGER.info("Setting post userId..." + postTest.getUserId());
		LOGGER.info("Setting post body..." + postTest.getBody());
		LOGGER.info("Setting post title...." + postTest.getTitle());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>(postTest, responseHeaders, HttpStatus.OK);
	}
	
	@PostMapping(path = "/send/{userId}")
	public void sendUserAccount(@PathVariable Integer userId) {
		List<BankAccountDTO> accounts = bankAccountService.getAccounts();
		BankAccountDTO account = accounts.get(userId);
		this.template.send("useraccount-topic", account);
	}

}