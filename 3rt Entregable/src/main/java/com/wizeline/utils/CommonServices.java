/**
 * 
 */
package com.wizeline.utils;

/**
 * @author lsalgadot
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wizeline.model.ResponseDTO;
import com.wizeline.service.UserService;


/**
 * Class Description goes here.
 * Created by enrique.gutierrez on 26/09/22
 */

@Component
public class CommonServices {

    @Autowired
    UserService userService;

    public ResponseDTO login(String user, String password) {
        //UserService UserService = new UserServiceImpl();
        return userService.login(user, password);
    }

}