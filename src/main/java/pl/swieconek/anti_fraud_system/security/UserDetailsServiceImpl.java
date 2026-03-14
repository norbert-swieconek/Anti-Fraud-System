package pl.swieconek.anti_fraud_system.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.swieconek.anti_fraud_system.repository.UserRepository;
import pl.swieconek.anti_fraud_system.model.User;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username.toLowerCase());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Nie ma takiego użytkownika");
        }

        return new UserDetailsImpl(user.get());
    }
}
