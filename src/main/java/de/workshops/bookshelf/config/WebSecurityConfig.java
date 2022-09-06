package de.workshops.bookshelf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .antMatchers("/h2-console/**").permitAll()
                                        .antMatchers(
                                                "/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/webjars/swagger-ui/**"
                                        ).permitAll()
                                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .headers().frameOptions().disable().and()
                .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            String sql = "SELECT * FROM bookshelf_user WHERE username = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    Collections.singletonList(
                            new SimpleGrantedAuthority(rs.getString("role"))
                    )
            ), username);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
