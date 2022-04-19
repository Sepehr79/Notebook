package com.kucess.notebook.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty("security.enable")
@Slf4j
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
                .mvcMatchers("/signup").permitAll()
                .and()
                .addFilterAfter((request, response, chain) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (!authentication.getName().equals("anonymousUser")) {
                        log.info("User successfully authenticated with the username: {}\n authorities: {}", authentication.getName(), authentication.getAuthorities());
                    }
                    chain.doFilter(request, response);
                } ,FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/notebook/v1", "/resources/templates/**");
    }
}
