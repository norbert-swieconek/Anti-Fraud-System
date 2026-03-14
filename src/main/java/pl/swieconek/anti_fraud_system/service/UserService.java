package pl.swieconek.anti_fraud_system.service;

import pl.swieconek.anti_fraud_system.dto.DeleteUserRespond;
import pl.swieconek.anti_fraud_system.dto.UserRequest;
import pl.swieconek.anti_fraud_system.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();
    DeleteUserRespond deleteUser(String username);
}
