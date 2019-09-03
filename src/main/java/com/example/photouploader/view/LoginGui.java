package com.example.photouploader.view;


import com.example.photouploader.service.security_service_impl.UserDetailsServiceImpl;
import com.vaadin.flow.component.Tag;
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

    private LoginOverlay login = new LoginOverlay();
    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginGui(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        // configures login dialog and adds it to the main view
//        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            UI.getCurrent().navigate(requestCache.resolveRedirectUrl());
//        }
        login.setOpened(true);
        login.setTitle("Photo Cloud");
        login.setAction("login");
        login.setDescription("Free cloud service for photos");
        login.setForgotPasswordButtonVisible(false);
        add(login);

        login.addLoginListener(e -> {
            try {
                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword());
                final Authentication auth = authenticationManager.authenticate(authReq);
                // try to authenticate with given credentials, should always return !null or throw an {@link AuthenticationException}
//                final Authentication authentication = authenticationManager
//                        .authenticate(new UsernamePasswordAuthenticationToken());

                // if authentication was successful we will update the security context and redirect to the page requested first
                if(authReq != null ) {
                    login.close();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(e.getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) ;
                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//                    SecurityContext securityContext = SecurityContextHolder.getContext();
//                    securityContext.setAuthentication(auth);
//                    HttpSession session = request.getSession(true);
//                    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//                    securityContext.getAuthentication();

                }

            } catch (AuthenticationException ex) {
                // show default error message
                // Note: You should not expose any detailed information here like "username is known but password is wrong"
                // as it weakens security.
                login.setError(true);
            }
        });
    }
}
