package by.karpovich.security.web.controller;

import by.karpovich.security.service.impl.UserServiceImpl;
import by.karpovich.security.web.dto.user.UserDto;
import by.karpovich.security.web.validation.OnCreate;
import by.karpovich.security.web.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    @Operation(summary = "Create user")
    public UserDto create(@Validated({OnUpdate.class, OnCreate.class}) @RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public UserDto update(@Validated(OnUpdate.class) @PathVariable Long id, @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get userDto by id")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

}

