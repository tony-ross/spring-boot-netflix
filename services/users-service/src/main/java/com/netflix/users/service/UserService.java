package com.netflix.users.service;

import com.netflix.users.dto.CreateUserDto;
import com.netflix.users.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    UserDto create(CreateUserDto createUserDto);

    UserDto update(Long id, CreateUserDto updateUserDto);

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
