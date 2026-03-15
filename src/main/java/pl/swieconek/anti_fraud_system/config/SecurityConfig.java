package pl.swieconek.anti_fraud_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.swieconek.anti_fraud_system.model.Role;
import pl.swieconek.anti_fraud_system.security.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(handing -> handing
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasRole(Role.MERCHANT.toString())
                        .requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyRole(Role.ADMINISTRATOR.toString(), Role.SUPPORT.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole(Role.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole(Role.ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/auth/access").hasRole(Role.ADMINISTRATOR.toString())
                        .requestMatchers("/api/antifraud/suspicious-ip/**").hasRole(Role.SUPPORT.toString())
                        .requestMatchers("/api/antifraud/stolencard/**").hasRole(Role.SUPPORT.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }
}
