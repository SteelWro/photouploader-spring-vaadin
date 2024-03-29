package com.example.photouploader.config;


import com.example.photouploader.model.User;
import com.example.photouploader.model.Role;
import com.example.photouploader.repo.UserRepo;
import com.example.photouploader.view.LoginGui;
import com.example.photouploader.view.AdminGui;
import com.example.photouploader.view.MainGui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private UserRepo userRepo;
    private static final String LOGIN_PROCESSING_URL = "/" + LoginGui.ROUTE;
    private static final String LOGIN_FAILURE_URL = "/" + LoginGui.ROUTE + "?error=true";
    private static final String LOGIN_URL = "/" + MainGui.ROUTE;
    private static final String LOGOUT_SUCCESS_URL = "/" + MainGui.ROUTE;
    private static final String LOGIN_SUCCESS_DEFAULT_URL = "/" + AdminGui.ROUTE;
    private static final String LOGOUT_URL = "/" + MainGui.LOGOUT_ROUTE;


    @Autowired
    public SecurityConfiguration(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepo userRepo) {
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers("/main").permitAll()
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .userDetailsService(userDetailsService)
                .formLogin()
                .loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                //.defaultSuccessUrl(LOGIN_SUCCESS_DEFAULT_URL,true)
                //.successHandler(myAuthenticationSuccessHandler())
                .failureUrl(LOGIN_FAILURE_URL).permitAll()
                .and()
                .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL).logoutUrl(LOGOUT_URL).permitAll()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/icons/**",
                "/images/**",
                "/frontend/**",
                "/webjars/**",
                "/h2-console/**",
                "/frontend-es5/**",
                "/frontend-es6/**");
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
