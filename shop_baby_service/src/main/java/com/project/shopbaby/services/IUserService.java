package com.project.shopbaby.services;

import com.project.shopbaby.dtos.UserDTO;
import com.project.shopbaby.dtos.UserLoginDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String Login(UserLoginDTO userLoginDTO) throws Exception;
}
