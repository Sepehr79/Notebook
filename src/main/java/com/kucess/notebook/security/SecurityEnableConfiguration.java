package com.kucess.notebook.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty("security.enable")
public class SecurityEnableConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin().defaultSuccessUrl("/notebook/v1/home", true)
                .and()
                .logout()
                .and()
                .authorizeRequests()
                .mvcMatchers("/notebook/v1/home").authenticated()
                .mvcMatchers("/notebook/v1/employees/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/signup").permitAll();


    }
}
