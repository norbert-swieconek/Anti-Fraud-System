package pl.swieconek.anti_fraud_system.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.swieconek.anti_fraud_system.dto.DeleteUserRespond;
import pl.swieconek.anti_fraud_system.dto.UserRequest;
import pl.swieconek.anti_fraud_system.dto.UserResponse;
import pl.swieconek.anti_fraud_system.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<DeleteUserRespond> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
