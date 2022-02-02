package shop.newplace.Users.token;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtBeforeFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		System.out.println(req.getRequestURI());
		String reqPathName = req.getRequestURI();
		if(reqPathName.contains("swagger") || reqPathName.contains("api-docs") ) {
			chain.doFilter(req, res);
			System.out.println("swaggerTest");
		} else if ("POST".equals(req.getMethod())) {
			if(!reqPathName.contains("/users")) {
				String headerAuth = req.getHeader("Authorization");
				log.info("Jwt 확인 : " + headerAuth);
				if("cos".equals(headerAuth)) {
					chain.doFilter(req, res);
				}
			} else {
				log.info("Filter pass");
				chain.doFilter(req, res);
			}
				
		}
		
		
	}
}
