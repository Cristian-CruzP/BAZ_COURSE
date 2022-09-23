package com.wizeline;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.BO.BankAccountBO;
import com.wizeline.BO.BankAccountBOImpl;
import com.wizeline.BO.UserBO;
import com.wizeline.BO.UserBoImpl;
import com.wizeline.DTO.BankAccountDTO;
import com.wizeline.DTO.ResponseDTO;
import com.wizeline.DTO.UserDTO;
import com.wizeline.utils.Utils;
import com.wizeline.utils.exceptions.ExcepcionGenerica;

public class LearningJava extends Thread {

	private static String responseTextThread="";
	private ResponseDTO response;
	private static String textThread="";
	
	private static final Logger LOGGER = Logger.getLogger(LearningJava.class.getName());
	private static final Object SUCCESS_CODE = "OK000";
	
	static String msgProcPeticion = "LearningJava - Inicia procesamiento de peticion ...";

	public static void main(String[] args) throws IOException {
		LOGGER.info("LearningJava - Iniciando servicio REST");

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/api/login", (exchange -> {
			LOGGER.info(msgProcPeticion);
			ResponseDTO response = new ResponseDTO();
			String responseText = "";

			if ("GET".equals(exchange.getRequestMethod())) {
				LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
				UserDTO user = new UserDTO();
				user = user.getParameters(splitQuery(exchange.getRequestURI()));
				response = login(user.getUser(), user.getPassword());
				JSONObject json = new JSONObject(response);
				responseText = json.toString();
				exchange.getResponseHeaders().set("contextType", "application/json; charset=UTF-8");
				exchange.sendResponseHeaders(200, responseText.getBytes().length);
			} else {
				exchange.sendResponseHeaders(405, -1);
			}

			OutputStream output = exchange.getResponseBody();

			LOGGER.info("LearningJava - Cerrando recursos ...");
			output.write(responseText.getBytes());
			output.flush();
			output.close();
			exchange.close();
		}));
		
		server.createContext("/api/createUser", (exchange -> {
			LOGGER.info(msgProcPeticion);
			ResponseDTO response = new ResponseDTO();
			String responseText = "";
			exchange.getRequestBody();
			if ("POST".equals(exchange.getRequestMethod())) {
				LOGGER.info("LearningJava - Procesando peticion HTTP de tipo POST");
				UserDTO user = new UserDTO();
				user = user.getParameters(splitQuery(exchange.getRequestURI()));
				response = createUser(user.getUser(), user.getPassword());
				JSONObject json = new JSONObject(response);
				responseText = json.toString();
				exchange.getResponseHeaders().set("contextType", "application/json; charset=UTF-8");
				exchange.sendResponseHeaders(200, responseText.getBytes().length);
			} else {
				exchange.sendResponseHeaders(405, -1);
			}
			OutputStream output = exchange.getResponseBody();

			LOGGER.info("LearningJava - Cerrando recursos ...");
			output.write(responseText.getBytes());
			output.flush();
			output.close();
			exchange.close();
		}));
		
		server.setExecutor(null);
		server.start();
		LOGGER.info("LearningJava - Server started on port 8080");

	//Consultar informacion de cuenta de un usuario
		server.createContext("/api/getUserAccount", (exchange -> {
			LOGGER.info(msgProcPeticion);
			Instant inicioEjecucion = Instant.now();
			ResponseDTO response = new ResponseDTO();
			String responseText = "";
			/** Validates the type of http request */
			if ("GET".equals(exchange.getRequestMethod())) {
				LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
				UserDTO user = new UserDTO();
				Map<String, String> params = splitQuery(exchange.getRequestURI());
				user = user.getParameters(params);
	
				// Valida formato del parametro fecha (date) [dd-mm-yyyy]
				String lastUsage = params.get("date");
				if (Utils.isDateFormatValid(lastUsage)) {
					// Valida el password del usuario (paswword)
					if (Utils.isPasswordValid(user.getPassword())) {
						response = login(user.getUser(), user.getPassword());
						if (response.getCode().equals(SUCCESS_CODE)) {
							BankAccountDTO bankAccountDTO = getAccountDetails(user.getUser(), lastUsage);
							JSONObject json = new JSONObject(bankAccountDTO);
							responseText = json.toString();
							exchange.getResponseHeaders().add("Content-type", "application/json");
							exchange.sendResponseHeaders(200, responseText.getBytes().length);
						}
					} else {
						responseText = "Password Incorrecto";
						exchange.getResponseHeaders().add("Content-type", "application/json");
						exchange.sendResponseHeaders(401, responseText.getBytes().length);
					}
				} else {
					responseText = "Formato de fecha Incorrecto";
					exchange.getResponseHeaders().add("Content-type", "application/json");
					exchange.sendResponseHeaders(400, responseText.getBytes().length);
				}
				
			}
				/**
				 * Always remember to close the resource you open. Avoid memory leaks
				 */
			OutputStream output = exchange.getResponseBody();
			Instant finalEjecucion = Instant.now();
	
			LOGGER.info("LearningJava - Cerrando recursos");
			String totalEjecucion = new String(String.valueOf(Duration.between(inicioEjecucion, finalEjecucion).toMillis()).concat(" segundos."));
			LOGGER.info("Tiempo de respuesta: ".concat(totalEjecucion));
			output.write(responseText.getBytes());
			output.flush();
			output.close();
			exchange.close();
		}));
		
		server.createContext("/api/getAccounts", (exchange -> {
			LOGGER.info(msgProcPeticion);
			BankAccountBO bankAccountBO = new BankAccountBOImpl();
			
			String responseText="";
			if("GET".equals(exchange.getRequestMethod())) {
				LOGGER.info("LearningJava - procesando peticion HTTP de tipo GET");
				List<BankAccountDTO> accounts = bankAccountBO.getAccounts();
				JSONArray json = new JSONArray(accounts);
				responseText = json.toString();
				exchange.getResponseHeaders().add("Content-type", "application/json");
				exchange.sendResponseHeaders(200, responseText.getBytes().length);
			} else {
				exchange.sendResponseHeaders(405, -1);
			}
			OutputStream output = exchange.getResponseBody();
			
			LOGGER.info("LearningJava - Cerrando recursos ...");
			output.write(responseText.getBytes());
			output.flush();
			output.close();
			exchange.close();
		}));
		
	    // Crear usuarios
	    server.createContext("/api/createUsers", (exchange -> {
	        LOGGER.info(msgProcPeticion);
	        ResponseDTO response = new ResponseDTO();
	        /** Validates the type of http request  */
	        exchange.getRequestBody();
	        if ("POST".equals(exchange.getRequestMethod())) {
	            LOGGER.info("LearningJava - Procesando peticion HTTP de tipo POST");

	            // Obtenemos el request del body que mandamos
	            StringBuilder text = new StringBuilder();
	            try (Scanner scanner = new Scanner(exchange.getRequestBody())) {
	                while(scanner.hasNext()) {
	                    text.append(scanner.next());
	                }
	            } catch (Exception e ) {
	            	LOGGER.severe(e.getMessage());
	            	throw new ExcepcionGenerica("Fallo al obtener el request del body");
	            }
	            textThread = text.toString();

	            LOGGER.info(textThread);
	            // Iniciamos thread
	            LearningJava thread = new LearningJava();
	            thread.start();

	            // Esperamos a que termine el thread
	            while(thread.isAlive());

	            exchange.getResponseHeaders().set("contextType", "application/json; charset=UTF-8");
	            exchange.sendResponseHeaders(200, responseTextThread.getBytes().length);
	        } else {
	            /** 405 Method Not Allowed */
	            exchange.sendResponseHeaders(405, -1);
	        }
	        OutputStream output = exchange.getResponseBody();
	        /**
	         * Always remember to close the resources you open.
	         * Avoid memory leaks
	         */
	        LOGGER.info("LearningJava - Cerrando recursos ...");
	        output.write(responseTextThread.getBytes());
	        output.flush();
	        output.close();
	        exchange.close();
	    }));
	    
	 // Consultar todas las cuentas y agruparlas por su tipo utilizando Programación Funcional
	    server.createContext("/api/getAccountsGroupByType", (exchange -> {
	        LOGGER.info(msgProcPeticion);
	        Instant inicioDeEjecucion = Instant.now();
	        BankAccountBO bankAccountBO = new BankAccountBOImpl();
	        String responseText = "";
	        /** Validates the type of http request  */
	        if ("GET".equals(exchange.getRequestMethod())) {
	            LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
	            List<BankAccountDTO> accounts = bankAccountBO.getAccounts();
	            // Aqui implementaremos la programación funcional
	            Map<String, List<BankAccountDTO>> groupedAccounts;
	            Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
	            groupedAccounts = accounts.stream().collect(Collectors.groupingBy(groupFunction));

	            JSONObject json = new JSONObject(groupedAccounts);
	            responseText = json.toString();
	            exchange.getResponseHeaders().add("Content-type", "application/json");
	            exchange.sendResponseHeaders(200, responseText.getBytes().length);
	        } else {
	            /** 405 Method Not Allowed */
	            exchange.sendResponseHeaders(405, -1);
	        }
	        OutputStream output = exchange.getResponseBody();
	        Instant finalDeEjecucion = Instant.now();
	        /**
	         * Always remember to close the resources you open.
	         * Avoid memory leaks
	         */
	        LOGGER.info("LearningJava - Cerrando recursos ...");
	        String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
	        LOGGER.info("Tiempo de respuesta: ".concat(total));
	        output.write(responseText.getBytes());
	        output.flush();
	        output.close();
	        exchange.close();
	    }));
		
	}

	static BankAccountDTO getAccountDetails(String user, String lastUsage) {
		BankAccountBO bankAccountBO = new BankAccountBOImpl();
		return bankAccountBO.getAccountDetails(user, lastUsage);
	}

	private static ResponseDTO login(String User, String password) {
		UserBO userBO = new UserBoImpl();
		return userBO.login(User, password);
	}

	private static ResponseDTO createUser(String User, String password) {
		UserBO userBO = new UserBoImpl();
		return userBO.createUser(User, password);
	}

	public static Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<>();
		String query = uri.getQuery();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF8"));
		}

		return query_pairs;
	}
	

		 @Override
		 public void run(){
		     try {
		         crearUsuarios();
		     } catch (Exception e) {
		         LOGGER.severe(e.getMessage());
		         throw new ExcepcionGenerica(e.getMessage());
		     }
	 }
	
	 @Deprecated(since = "Anotaciones update")
	 private void createUsers() {
	     try {
	         String user = "user";
	         String pass = "password";
	         JSONArray jsonArray = new JSONArray(textThread);
	         JSONObject user1 = new JSONObject(jsonArray.get(0).toString());
	         JSONObject user2 = new JSONObject(jsonArray.get(1).toString());
	         JSONObject user3 = new JSONObject(jsonArray.get(2).toString());

	         ResponseDTO response = createUser(user1.getString(user), user1.getString(pass));
	         responseTextThread = new JSONObject(response).toString();
	         LOGGER.info("Usuario 1: " + responseTextThread);
	         Thread.sleep(1000);

	         response = createUser(user2.getString(user), user2.getString(pass));
	         responseTextThread = new JSONObject(response).toString();
	         LOGGER.info("Usuario 2: " + responseTextThread);
	         Thread.sleep(1000);

	         response = createUser(user3.getString(user), user3.getString(pass));
	         responseTextThread = new JSONObject(response).toString();
	         LOGGER.info("Usuario 3: " + responseTextThread);
	     } catch (InterruptedException e) {
	         throw new RuntimeException(e);
	     }
	 }
	 
	 private void crearUsuarios() {
		    try {
		        String user = "user";
		        String pass = "password";
		        JSONArray jsonArray = new JSONArray(textThread);
		        JSONObject userJson;

		        ResponseDTO response = null;

		        LOGGER.info("jsonArray.length(): " + jsonArray.length());
		        for(int i = 0; i < jsonArray.length(); i++) {
		            userJson = new JSONObject(jsonArray.get(i).toString());
		            response = createUser(userJson.getString(user), userJson.getString(pass));
		            responseTextThread = new JSONObject(response).toString();
		            LOGGER.info("Usuario " + (i+1) + ": " + responseTextThread);
		            Thread.sleep(1000);
		        }
		    } catch (InterruptedException e) {
		        throw new RuntimeException(e);
		    }
		}

}
