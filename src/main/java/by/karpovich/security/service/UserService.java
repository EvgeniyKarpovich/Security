package by.karpovich.security.service;

import by.karpovich.security.domain.User;
import by.karpovich.security.web.dto.user.UserDto;

public interface UserService {

    UserDto getById(Long id);

    User getByUsername(String username);

    UserDto update(Long id, UserDto user);

    UserDto create(UserDto user);

    void delete(Long id);
}
