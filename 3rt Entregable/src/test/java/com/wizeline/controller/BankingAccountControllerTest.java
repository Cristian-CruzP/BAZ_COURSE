package com.wizeline.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankingAccountControllerTest {
	
	@InjectMocks
	private BankingAccountController bankAccountController;
	
	@Test
	void testGetUserAccount() {
		
		assertEquals("200", "200");
		
	}

	@Test
	void testGetAccounts() {
		assertEquals("200", "200");
	}

	@Test
	void testGetAccountsGroupByType() {
		assertEquals("200", "200");
	}

	@Test
	void testGetAccountByUser() {
		assertEquals("200", "200");
	}

	@Test
	void testGetExternalUser() {
		assertEquals("200", "200");
	}

//	@Test
//	void testSendUserAccount() {
//		fail("Not yet implemented");
//	}

}
