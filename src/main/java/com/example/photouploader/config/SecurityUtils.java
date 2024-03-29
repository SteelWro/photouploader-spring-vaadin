package com.example.photouploader.config;

import com.example.photouploader.model.Role;
import com.example.photouploader.view.*;
import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


public final class SecurityUtils {

    private SecurityUtils() {
    }

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    static boolean isUserLoggedIn(Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static boolean isAccessGranted(Class<?> securedClass) {
        final boolean LoginView = LoginGui.class.equals(securedClass);
        final boolean publicView = MainGui.class.equals(securedClass);
        final boolean RegistrationView = RegistrationGui.class.equals(securedClass);


        if (LoginView || publicView || RegistrationView) {
            return true;
        }

        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isUserLoggedIn(userAuthentication)) {
            return false;
        }

        Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        if (secured == null) {
            return true;
        }

        List<String> allowedRoles = Arrays.asList(secured.value());
        return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);
    }

    public static String redirectGrantedUser(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(Role.USER)) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals(Role.ADMIN)) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            return UserGui.ROUTE;
        } else if (isAdmin) {
            return AdminGui.ROUTE;
        } else {
            throw new IllegalStateException();
        }
    }
}
