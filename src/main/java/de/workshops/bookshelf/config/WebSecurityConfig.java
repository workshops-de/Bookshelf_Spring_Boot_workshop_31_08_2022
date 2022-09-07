package de.workshops.bookshelf.config;

import de.workshops.bookshelf.user.BookshelfUser;
import de.workshops.bookshelf.user.BookshelfUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final BookshelfUserRepository bookshelfUserRepository;

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
                .csrf().disable()
                .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            BookshelfUser bookshelfUser = bookshelfUserRepository.findByUsername(username);

            if (bookshelfUser != null) {
                return User.withUsername(bookshelfUser.getUsername())
                        .password(bookshelfUser.getPassword())
                        .roles("USER")
                        .build();
            }

            throw new UsernameNotFoundException("User not found!");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
