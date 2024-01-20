package com.project.shopbaby.services;

import com.project.shopbaby.dtos.UserDTO;
import com.project.shopbaby.dtos.UserLoginDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.Role;
import com.project.shopbaby.models.User;
import com.project.shopbaby.repositories.RoleRespository;
import com.project.shopbaby.repositories.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements   IUserService{
    private  final UserRespository userRespository;
    private  final RoleRespository roleRespository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {

        Role existingRole = roleRespository
                .findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role already exits"));

        if ( userRespository.existsByPhoneNumber(userDTO.getPhoneNumber()) == null){
            throw  new DataIntegrityViolationException("Phone number already exits");
        }
        User user = User.fromData(userDTO);
        user.setRole(existingRole);

        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            //String encodePassword = PasswordEncode
            user.setPassword(password);
        }
         return  userRespository.save(user);
    }

    @Override
    public User Login(UserLoginDTO userLoginDTO) throws DataNotFoundException {
        User existingUser = userRespository
                .findByPhoneNumber(userLoginDTO.getPhoneNumber())
                .orElseThrow(()-> new DataNotFoundException("User already exits"));

        if (existingUser.getPassword().equals(userLoginDTO.getPassword()))
            return  existingUser;

        return  null;
    }

}
