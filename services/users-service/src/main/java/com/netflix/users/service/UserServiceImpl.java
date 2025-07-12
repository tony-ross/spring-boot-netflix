package com.netflix.users.service;

import com.netflix.users.dto.CreateUserDto;
import com.netflix.users.dto.UserDto;
import com.netflix.users.entity.User;
import com.netflix.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return convertToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return convertToDto(user);
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new IllegalArgumentException("Username already exists: " + createUserDto.username());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new IllegalArgumentException("Email already exists: " + createUserDto.email());
        }

        User user = convertToEntity(createUserDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public UserDto update(Long id, CreateUserDto updateUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(updateUserDto.username()) &&
            userRepository.existsByUsername(updateUserDto.username())) {
            throw new IllegalArgumentException("Username already exists: " + updateUserDto.username());
        }

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(updateUserDto.email()) &&
            userRepository.existsByEmail(updateUserDto.email())) {
            throw new IllegalArgumentException("Email already exists: " + updateUserDto.email());
        }

        existingUser.setUsername(updateUserDto.username());
        existingUser.setEmail(updateUserDto.email());
        existingUser.setPassword(updateUserDto.password());
        existingUser.setFirstName(updateUserDto.firstName());
        existingUser.setLastName(updateUserDto.lastName());

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User convertToEntity(CreateUserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password()); // In a real application, this should be hashed
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        return user;
    }
}
