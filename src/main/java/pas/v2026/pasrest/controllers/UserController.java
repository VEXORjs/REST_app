package pas.v2026.pasrest.controllers;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pas.v2026.pasrest.dto.request.user.CreateUserDto;
import pas.v2026.pasrest.dto.request.user.UpdateUserDto;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;
import pas.v2026.pasrest.model.User;
import pas.v2026.pasrest.services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        User user = mapToEntity(dto);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(mapToDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers(
            @RequestParam(required = false) String exact,
            @RequestParam(required = false) String partial) {
        List<User> users;

        if (exact != null) {
            users = List.of(userService.getUserByLoginExact(exact));
        } else if (partial != null) {
            users = userService.getUsersByLoginPartial(partial);
        } else {
            users = userService.getAllUsers();
        }

        List<UserResponseDto> response = users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> changeStatus(@PathVariable UUID id, @RequestParam boolean active) {
        User updatedUser = userService.changeUserStatus(id, active);
        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDto dto) {
        User updatedUser = userService.updateUser(id, dto.getPassword());
        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    private User mapToEntity(CreateUserDto dto) {
        return dto.toEntity();
    }

    private UserResponseDto mapToDto(User user) {
        return user.toDto();
    }
}
