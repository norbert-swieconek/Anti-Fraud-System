package pl.swieconek.anti_fraud_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.swieconek.anti_fraud_system.dto.AccessResponse;
import pl.swieconek.anti_fraud_system.dto.DeleteUserRespond;
import pl.swieconek.anti_fraud_system.dto.UserRequest;
import pl.swieconek.anti_fraud_system.dto.UserResponse;
import pl.swieconek.anti_fraud_system.model.Operation;
import pl.swieconek.anti_fraud_system.model.Role;
import pl.swieconek.anti_fraud_system.model.User;
import pl.swieconek.anti_fraud_system.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        String username = userRequest.username().toLowerCase();
        Optional<User> userExist = userRepository.findByUsername(username);

        if (userExist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        User user;

        if (userRepository.count() == 0) {
            user = new User(userRequest.name(), username, passwordEncoder.encode(userRequest.password()), Role.ADMINISTRATOR, false);
        } else {
            user = new User(userRequest.name(), username, passwordEncoder.encode(userRequest.password()), Role.MERCHANT, true);
        }


        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getUsername(), savedUser.getRole());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getUsername(), user.getRole()))
                .sorted(Comparator.comparing(UserResponse::id))
                .toList();
    }

    @Override
    public DeleteUserRespond deleteUser(String username) {
        String userName = username.toLowerCase();
        Optional<User> userExist = userRepository.findByUsername(userName);

        if (userExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        userRepository.delete(userExist.get());
        return new DeleteUserRespond(username, "Deleted successfully!");
    }

    @Override
    public UserResponse changeRole(String username, Role role) {
        if (role == Role.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByUsername(username.toLowerCase());

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (role == user.get().getRole()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        user.get().setRole(role);
        User modifiedUser = userRepository.save(user.get());
        return new UserResponse(modifiedUser.getId(), modifiedUser.getName(), modifiedUser.getUsername(), modifiedUser.getRole());
    }

    @Override
    public AccessResponse changeLock(String username, Operation operation) {
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User foundUser = user.get();

        if (foundUser.getRole() == Role.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String operationResult;
        if (operation == Operation.LOCK) {
            foundUser.setLocked(true);
            operationResult = "locked";
        } else {
            foundUser.setLocked(false);
            operationResult = "unlocked";
        }

        User savedUser = userRepository.save(foundUser);

        return new AccessResponse("User " + username + " " + operationResult + "!" );
    }


}
