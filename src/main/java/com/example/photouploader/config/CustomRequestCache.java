package com.example.photouploader.config;

import com.example.photouploader.view.LoginGui;
import com.example.photouploader.view.AdminGui;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpSessionRequestCache that avoids saving internal framework requests.
 */
public class CustomRequestCache extends HttpSessionRequestCache {
	/**
	 * {@inheritDoc}
	 *
	 * If the method is considered an internal request from the framework, we skip
	 * saving it.
	 *
	 * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
	 */
	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		if (!SecurityUtils.isFrameworkInternalRequest(request)) {
			super.saveRequest(request, response);
		}
	}

	/**
	 * Unfortunately, it's not that easy to resolve the redirect URL from the saved request. But with some
	 * casting (we always use {@link DefaultSavedRequest}) and mangling we are able to get the request URI.
	 */
	public String resolveRedirectUrl() {
		SavedRequest savedRequest = getRequest(VaadinServletRequest.getCurrent().getHttpServletRequest(), VaadinServletResponse.getCurrent().getHttpServletResponse());
		if(savedRequest instanceof DefaultSavedRequest) {
			final String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI();
			System.out.println(requestURI);
			// check for valid URI and prevent redirecting to the login view
			if (!requestURI.equals("/") && requestURI != null && !requestURI.isEmpty() && !requestURI.contains(LoginGui.ROUTE)) {
				{
					System.out.println("prawda");
					return requestURI.startsWith("/") ? requestURI.substring(1) : requestURI;
				}
			}
			return AdminGui.ROUTE;
		}
		// if everything fails, redirect to the main view
		return "";
	}

}