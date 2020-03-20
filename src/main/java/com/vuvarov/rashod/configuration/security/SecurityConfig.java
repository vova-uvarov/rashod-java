package com.vuvarov.rashod.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
                .antMatchers("/docs/**", "/actuator/**", "/admin/system/state/*", "/v2/api-docs",
                        "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui.html", "/csfr", "/webjars/**");
    }
}
