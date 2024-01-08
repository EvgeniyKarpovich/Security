package by.karpovich.security.service.impl;

import by.karpovich.security.domain.Role;
import by.karpovich.security.domain.User;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.exception.ResourceNotFoundException;
import by.karpovich.security.mapper.UserMapper;
import by.karpovich.security.repository.UserRepository;
import by.karpovich.security.service.UserService;
import by.karpovich.security.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto user) {
        return null;
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        checkUserAlreadyExists(userDto);

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new IllegalStateException(
                    "Password and password confirmation do not match."
            );
        }
        User mappedUserEntity = userMapper.toEntity(userDto, passwordEncoder);
        mappedUserEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = Set.of(Role.ROLE_USER);
        mappedUserEntity.setRoles(roles);

        User savedUser = userRepository.save(mappedUserEntity);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void checkUserAlreadyExists(UserDto dto) {
        userRepository.findByEmail(dto.getUsername())
                .orElseThrow(() -> new DuplicateException("Such user already exists"));
    }
}
