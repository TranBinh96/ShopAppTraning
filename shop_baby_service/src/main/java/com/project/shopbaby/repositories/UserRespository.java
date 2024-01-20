package com.project.shopbaby.repositories;

import com.project.shopbaby.models.Role;
import com.project.shopbaby.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRespository extends JpaRepository<User,Long> {

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);





}
