package co.sistemcobro.webservice.auth;


import java.io.IOException;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.sistemcobro.all.exception.LogicaException;
import co.sistemcobro.hermes.bean.UsuarioBean;
import co.sistemcobro.hermes.ejb.UsuarioEJB;
import co.sistemcobro.webservice.services.AuthenticationService;

@Stateless
public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";
	@EJB
	private UsuarioEJB usuarioEJB;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String a = httpServletRequest.getAuthType();
			Enumeration<String> c = httpServletRequest.getHeaderNames();
			Enumeration<String> p = httpServletRequest.getHeaders(AUTHENTICATION_HEADER);
			String authCredentials = httpServletRequest
					.getHeader(AUTHENTICATION_HEADER);

			// better injected
			AuthenticationService authenticationService = new AuthenticationService();

			
			
			boolean authenticationStatus = authenticationService
					.authenticate(authCredentials);

			if (authenticationStatus) {
				filter.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse
							.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}