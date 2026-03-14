package pl.swieconek.anti_fraud_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.swieconek.anti_fraud_system.dto.DeleteUserRespond;
import pl.swieconek.anti_fraud_system.dto.UserRequest;
import pl.swieconek.anti_fraud_system.dto.UserResponse;
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

        User user = new User(userRequest.name(), username, passwordEncoder.encode(userRequest.password()));
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getUsername());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getUsername()))
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


}
