package com.example.photouploader.view;


import com.example.photouploader.config.SecurityUtils;
import com.example.photouploader.service.security_service_impl.UserDetailsServiceImpl;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Route(value = LoginGui.ROUTE)
@PageTitle("Login")
public class LoginGui extends VerticalLayout {
    public static final String ROUTE = "login";

    private AuthenticationManager authenticationManager;
    private LoginOverlay login = new LoginOverlay();


    @Autowired
    public LoginGui(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        add(login);
        login.setOpened(true);
        login.setTitle("Photo Cloud");
        login.setDescription("Free cloud service for photos");
        getElement().appendChild(login.getElement());

        login.addLoginListener(e -> {
            try {
                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword());
                final Authentication auth = authenticationManager.authenticate(authReq);
                if (authReq != null) {
                    login.close();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(e.getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    UI.getCurrent().navigate(SecurityUtils.redirectGrantedUser(authentication));

                }
            } catch (AuthenticationException ex) {
                login.setError(true);
            }
        });
    }

}
