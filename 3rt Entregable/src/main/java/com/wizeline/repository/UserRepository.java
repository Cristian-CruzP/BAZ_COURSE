/**
 * 
 */
package com.wizeline.repository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author lsalgadot
 *
 */
@Repository
public interface UserRepository {

	String createUser(String user, String password);

	String login(String user, String password);

}
