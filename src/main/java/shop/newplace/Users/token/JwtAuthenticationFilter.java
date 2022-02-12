package shop.newplace.Users.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManger;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("JWT 로그인 중");

        /**
         * username, password 받아서
         * 정상인지 로그인 시도하기
         * AuthenticationManager로 로그인 시도를 하면
         * PrincipalDetailsService의 loadUserByUsername 메서드 호출 호출된다.
         *
         * PrincipalDetails를 세션에 담고 > 이걸 세션에 담지 않으면 권한 관리가 안된다.
         * > 이 때, 서버세션이 아니라 시큐리티 세션이다!
         *
         * JWT 토큰을 만들어서 응답해준다.
         */
		return authenticationManger.authenticate(super.attemptAuthentication(request, response));
	}

}
