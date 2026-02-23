package com.canjecheques.canje_cheques.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(JdbcTemplate jdbc) {
        return username -> {

            var user = jdbc.query(
                "SELECT id, username, password, enabled FROM app_user WHERE username = ?",
                (rs, rowNum) -> new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getBoolean("enabled")
                },
                username
            );

            if (user.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }

            Object[] row = user.get(0);
            int userId = (int) row[0];
            String uname = (String) row[1];
            String password = (String) row[2];
            boolean enabled = (boolean) row[3];

            List<String> roles = jdbc.query(
                "SELECT r.name FROM app_role r " +
                "JOIN app_user_role ur ON ur.role_id = r.id " +
                "WHERE ur.user_id = ?",
                (rs, i) -> rs.getString(1),
                userId
            );

            if (roles.isEmpty()) {
                throw new UsernameNotFoundException("Usuario sin roles");
            }

            return User.withUsername(uname)
                    .password(password) // BCrypt de BD
                    .disabled(!enabled)
                    .authorities(roles.toArray(new String[0])) // ROLE_ADMIN, ROLE_USER
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}