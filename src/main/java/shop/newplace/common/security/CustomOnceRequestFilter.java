package shop.newplace.common.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.users.token.JwtTokenProvider;

@Slf4j
@RequiredArgsConstructor
public class CustomOnceRequestFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println(request.getRequestURI());
		if(!request.getRequestURI().contains("/users")) {
			String accessToken = jwtTokenProvider.resolveAccessToken(request);
			String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
			if(accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
				//AccessToken 유효
				this.setAuthentication(accessToken);
			}
			String userId = jwtTokenProvider.getUserIdByToken(refreshToken);
//			String refreshToken = jwtTokenProvider.resolveRefreshToken(Long.valueOf(userId));
			if(!jwtTokenProvider.validateToken(accessToken) && refreshToken != null) {
				//AccessToken은 만료 RefreshToken은 존재
				if(jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.existsRefreshTokenByUserId(Long.valueOf(userId))) {
					//유효한 RefreshToken && RefreshToken이 저장되어 있는가
					String newAccessToken = jwtTokenProvider.createAccessToken(userId, jwtTokenProvider.getLoginEmailByToken(refreshToken), jwtTokenProvider.getAuthentication(refreshToken).getAuthorities());
					jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
					this.setAuthentication(newAccessToken);
				}
			}
			
		}
		System.out.println("권한찾아보기 : " + SecurityContextHolder.getContext().getAuthentication());
		filterChain.doFilter(request, response);
	}
	
	private void setAuthentication(String token) {
		SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
		
	}
	
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//		System.out.println(req.getRequestURI());
//		String reqPathName = req.getRequestURI();
//		if ("POST".equals(req.getMethod())) {
//			if(!reqPathName.contains("/users")) {
//				String token = jwtTokenProvider.resolveToken(req);
//				log.info("Jwt 확인 : " + token);
//				if(token != null && jwtTokenProvider.validateToken(token)) {
//					Authentication authentication = jwtTokenProvider.getAuthentication(token);
//					SecurityContextHolder.getContext().setAuthentication(authentication);
//					chain.doFilter(req, res);
//				}
//			} else {
//				log.info("Filter pass");
//				chain.doFilter(req, res);
//			}
//				
//		} else {
//			log.info("Cors 요청?");
//			chain.doFilter(req, res);
//		}
//		
//		
//	}
}
