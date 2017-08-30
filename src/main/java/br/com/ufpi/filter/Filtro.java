package br.com.ufpi.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
public class Filtro implements Filter {

	private static final List<String> urlsWebServices = Arrays.asList("/student", "/activity", "/reinforcements",
			"/lessons", "/ansewers", "/options", "/task", "/audio");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Usuario usuarioDaSessao = (Usuario) httpRequest.getSession().getAttribute("usuarioDaSessao");

		String paginaAcessada = httpRequest.getRequestURI();
		System.out.println("URL acessada: " +paginaAcessada);
		boolean requestDoLogin = paginaAcessada.contains("login.xhtml");
		if (isUrlWebService(paginaAcessada))
			chain.doFilter(request, response);
		else {
			if (usuarioDaSessao != null) {
				if (requestDoLogin || (!paginaAcessada.contains(".xhtml"))) {
					System.out.println("requestDoLogin");
					HttpServletResponse httpResponse = (HttpServletResponse) response;
					httpResponse.sendRedirect("index.xhtml");
				} else {
					chain.doFilter(request, response);
				}
			} else {
				if (!requestDoLogin && !paginaAcessada.contains("javax.faces.resource")) {
					httpRequest.getRequestDispatcher("/login.xhtml").forward(request, response);
				} else {
					chain.doFilter(request, response);
				}
			}
		}
	}

	private boolean isUrlWebService(String paginaAcessada) {
		for (String url : urlsWebServices) {
			if (paginaAcessada.contains(url))
				return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
