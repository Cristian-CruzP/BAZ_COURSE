/**
 * 
 */
package com.wizeline.DAO;

/**
 * @author lsalgadot
 *
 */
public interface UserDAO {

	String createUser(String user, String password);

	String login(String user, String password);

}
