package com.example.photouploader.config;


import com.example.photouploader.model.User;
import com.example.photouploader.repo.UserRepo;
import com.example.photouploader.view.LoginGui;
import com.example.photouploader.view.AdminGui;
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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private UserRepo userRepo;
    private static final String LOGIN_PROCESSING_URL = "/" + LoginGui.ROUTE;
    private static final String LOGIN_FAILURE_URL = "/" + LoginGui.ROUTE + "?errors";
    private static final String LOGIN_URL = "/" + LoginGui.ROUTE;
    private static final String LOGOUT_SUCCESS_URL = "/" + LoginGui.ROUTE;
    private static final String LOGIN_SUCCESS_DEFAULT_URL = "/" + AdminGui.ROUTE;


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

        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest)
                    .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
                .userDetailsService(userDetailsService)
                .formLogin()
                    .loginPage(LOGIN_URL)
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .defaultSuccessUrl(LOGIN_SUCCESS_DEFAULT_URL,true)
                    .failureUrl(LOGIN_FAILURE_URL)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                    .and()
                .csrf()
                    .disable();
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
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
                "/frontend-es5/**", "/frontend-es6/**");
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
//        authBuilder.inMemoryAuthentication()
//                .withUser("user").password("{noop}user").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}admin").roles("USER", "ADMIN");
//    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CustomRequestCache requestCache() {
//        return new CustomRequestCache();
//    }

//    @Bean
//    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
//        return new MyAuthenticationSuccessHandler();
//    }

    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        User appUserUser = new User("jan", passwordEncoder().encode("jan"), "USER");
        User appUserAdmin = new User("admin", passwordEncoder().encode("admin"), "ADMIN");
        userRepo.save(appUserUser);
        userRepo.save(appUserAdmin);
    }

}
