package shop.newplace.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	//인증이 안 된 경우에 해당 이벤트가 발생함
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
		
	}
	
}
