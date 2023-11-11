package com.example.demo.auth;

import com.example.demo.Dao.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class SpringSecurityConfig
        extends WebSecurityConfigurerAdapter {

    List<UserEntity> users(){
        List<UserEntity> users = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.username = "admin";
        user1.password = "1234";
        user1.role = "ADMIN";

        users.add(user1);

        return users;
    }

    // Annotation
    @Override
    protected void
    configure(AuthenticationManagerBuilder auth)
            throws Exception
    {
        for (UserEntity user: this.users()) {
            auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser(user.username).password(passwordEncoder().encode(user.password)).roles(user.role);
        }

    }

    // Annotation
    @Bean
    // Method
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // Annotation
    @Override
    // Method
    protected void configure(HttpSecurity http)
            throws Exception
    {

        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll()
                .loginPage("/login")
                .usernameParameter("username")
                .and()
                .logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/logout"))
                .permitAll();
    }
}