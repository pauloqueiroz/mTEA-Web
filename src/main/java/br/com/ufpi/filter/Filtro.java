package br.com.ufpi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ufpi.model.Usuario;

/**
 * 
 * @author Paulo Sergio
 *
 */
@WebFilter(urlPatterns = { "/*" })
public class Filtro implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Usuario usuarioDaSessao = (Usuario) httpRequest.getSession().getAttribute(
				"usuarioDaSessao");

		String paginaAcessada = httpRequest.getRequestURI();
		boolean requestDoLogin = paginaAcessada.contains("login.xhtml");

		if (usuarioDaSessao != null) {
			if (requestDoLogin) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendRedirect("index.xhtml");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			if (!requestDoLogin
					&& !paginaAcessada.contains("javax.faces.resource")) {
				httpRequest.getRequestDispatcher("/login.xhtml").forward(
						request, response);
			} else {
				chain.doFilter(request, response);
			}
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
