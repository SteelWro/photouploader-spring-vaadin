package com.example.photouploader.view;


import com.example.photouploader.config.MyAuthenticationSuccessHandler;
import com.example.photouploader.config.SecurityUtils;
import com.example.photouploader.service.security_service_impl.UserDetailsServiceImpl;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.BeforeEnterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Route(value = LoginGui.ROUTE)
@PageTitle("Login")
public class LoginGui extends VerticalLayout{
    public static final String ROUTE = "login";

    private MyAuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationManager authenticationManager;
    private LoginOverlay login = new LoginOverlay();
    private String route = null;


    @Autowired
    public LoginGui(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, MyAuthenticationSuccessHandler authenticationSuccessHandler) {
        add(login);
        login.setOpened(true);
        login.setTitle("Photo Cloud");
        //login.setAction("login");
        login.setDescription("Free cloud service for photos");
        getElement().appendChild(login.getElement());
//        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
//            login.close();
//            UI.getCurrent().navigate(SecurityUtils.redirectGrantedUser(SecurityContextHolder.getContext().getAuthentication()) );
//        }
        login.addLoginListener(e -> {
            try {
                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword());
                final Authentication auth = authenticationManager.authenticate(authReq);
                // try to authenticate with given credentials, should always return !null or throw an {@link AuthenticationException}
//                final Authentication authentication = authenticationManager
//                        .authenticate(new UsernamePasswordAuthenticationToken());

                // if authentication was successful we will update the security context and redirect to the page requested first
                if (authReq != null) {
                    login.close();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(e.getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    UI.getCurrent().navigate(SecurityUtils.redirectGrantedUser(authentication));
//                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//                    SecurityContext securityContext = SecurityContextHolder.getContext();
//                    securityContext.setAuthentication(auth);
//                    HttpSession session = request.getSession(true);
//                    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//                    securityContext.getAuthentication();
                }
            } catch (AuthenticationException ex) {
                login.setError(true);
            }
        });
    }

}
