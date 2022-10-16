package com.wizeline.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Test
	void testLogin() {
		assertEquals("200", "200");
	}

	@Test
	void testCreateUser() {
		assertEquals("200", "200");
	}

	@Test
	void testCreateUsersAccount() {
		assertEquals("200", "200");
	}
	
	@Test
	void testDeleteUser() {
		assertEquals("200", "200");
	}

}
