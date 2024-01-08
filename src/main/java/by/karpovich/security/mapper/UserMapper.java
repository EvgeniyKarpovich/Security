package by.karpovich.security.mapper;

import by.karpovich.security.domain.User;
import by.karpovich.security.web.dto.user.UserDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    User toEntity(UserDto userDto, @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    UserDto toDto(User userEntity);

}
