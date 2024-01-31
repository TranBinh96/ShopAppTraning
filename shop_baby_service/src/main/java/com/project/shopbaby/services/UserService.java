package com.project.shopbaby.services;

import com.project.shopbaby.components.JwtTokenUtil;
import com.project.shopbaby.dtos.UserDTO;
import com.project.shopbaby.dtos.UserLoginDTO;
import com.project.shopbaby.exceptions.DataNotFoundException;
import com.project.shopbaby.models.Role;
import com.project.shopbaby.models.User;
import com.project.shopbaby.repositories.RoleRespository;
import com.project.shopbaby.repositories.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements   IUserService{
    private  final UserRespository userRespository;
    private  final RoleRespository roleRespository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtTokenUtil jwtTokenUtil;
    private  final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {

        Role existingRole = roleRespository
                .findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role already exits"));

        if ( userRespository.existsByPhoneNumber(userDTO.getPhoneNumber()) == null){
            throw  new DataIntegrityViolationException("Phone number already exits");
        }
        User newUser = User.fromData(userDTO);
        newUser.setRole(existingRole);

        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }
        return  userRespository.save(newUser);
    }

    @Override
    public String Login(UserLoginDTO userLoginDTO) throws Exception {
        Optional<User> optionalUser = userRespository.findByPhoneNumber(userLoginDTO
                .getPhoneNumber());
        if (optionalUser.isEmpty()){
            throw  new DataNotFoundException("Invalid phoneNumber / password");
        }

        User existUser = optionalUser.get();

        //check_Password
        if (existUser.getFacebookAccountId() == 0 && existUser.getGoogleAccountId()==0){
           if (!passwordEncoder.matches(userLoginDTO.getPassword(),existUser.getPassword()))
            {
               throw new BadCredentialsException("invalid phoneNumber / password");
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
                userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword(),existUser.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(optionalUser.get());
    }

}
