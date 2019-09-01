package com.example.photouploader.config;

import com.example.photouploader.repo.AppUserRepo;
import com.example.photouploader.view.LoginGui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private AppUserRepo appUserRepo;
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String LOGOIN_SUCCESS_URL = "/upload";


//    @Autowired
//    public SecurityConfiguration(UserDetailsService userDetailsService, AppUserRepo appUserRepo) {
//        this.userDetailsService = userDetailsService;
//        this.appUserRepo = appUserRepo;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable()

                // Register our CustomRequestCache, that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(requestCache())

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                // Allow all requests by logged in users.
                .anyRequest().authenticated()

                // Configure the login page.
                .and().formLogin().loginPage("/" + LoginGui.ROUTE).permitAll()
                .loginProcessingUrl(LOGOIN_SUCCESS_URL)
                .defaultSuccessUrl(LOGOIN_SUCCESS_URL)
                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",

                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (development mode) H2 debugging console
                "/h2-console/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER", "ADMIN");
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails normalUser =
//                User.withUsername("user")
//                        .password("user")
//                        .roles("User")
//                        .build();
//
//        UserDetails adminUser =
//                User.withUsername("admin")
//                        .password("admin")
//                        .roles("User", "Admin")
//                        .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public CustomRequestCache requestCache() {
        return new CustomRequestCache();
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void get(){
//        AppUser appUserUser = new AppUser("jan", passwordEncoder().encode("jan"),"USER");
//        AppUser appUserAdmin = new AppUser("admin", passwordEncoder().encode("admin"),"ADMIN");
//        appUserRepo.save(appUserUser);
//        appUserRepo.save(appUserAdmin);
//    }
    //        http.authorizeRequests()
//                .antMatchers("/admin").hasRole("ADMIN")
//                .antMatchers("/user", "/").hasAnyRole("ADMIN", "USER")
//                .antMatchers("/upload").hasRole("ADMIN")
//               // .anyRequest().authenticated()
//                .and()
//                //.httpBasic()
//                .csrf().disable()
//                .formLogin().loginPage("/login").permitAll()
//                .and();

}
