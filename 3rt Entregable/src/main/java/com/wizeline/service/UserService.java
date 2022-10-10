package com.wizeline.service;

import org.springframework.stereotype.Component;

import com.wizeline.model.ResponseDTO;

@Component
public interface UserService {

	ResponseDTO createUser(String user, String password);
	
	ResponseDTO login(String user, String password);
}
