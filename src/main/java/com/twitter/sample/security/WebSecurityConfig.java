package com.twitter.sample.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//		.ldapAuthentication()
//			.userDnPatterns("uid={0},ou=people")
//			.groupSearchBase("ou=groups")
//			.contextSource()
//				.url("ldap://localhost:8389/dc=springframework,dc=org")
//				.and()
//			.passwordCompare()
//				.passwordEncoder(new LdapShaPasswordEncoder())
//				.passwordAttribute("userPassword");
//    }
}
