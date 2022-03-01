package com.kucess.notebook.security;

import com.kucess.notebook.model.entity.AuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
        httpSecurity.httpBasic().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .antMatchers("/notebook/v1/admins/{username}/**").access("@authorizationGuard.checkUserId(authentication, #username)")
                .antMatchers("/notebook/v1/admins/**").hasAuthority(AuthorityType.ADMIN.name());

        httpSecurity.formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/notebook/v1/employees/{username}").access("@authorizationGuard.checkUserId(authentication, #username)")
                .regexMatchers(HttpMethod.GET ,"/notebook/v1/employees/(\\d+)").hasAnyAuthority(AuthorityType.EMPLOYEE.name(), AuthorityType.ADMIN.name())
                .regexMatchers(HttpMethod.POST ,"/notebook/v1/employees/(\\d+)").hasAnyAuthority(AuthorityType.EMPLOYEE.name())
                .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST,"/notebook/v1/admins");
    }
}
