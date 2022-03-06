package shop.newplace.common.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.common.advice.exception.DisabledUsersException;
import shop.newplace.common.advice.exception.ExpiredPasswordException;
import shop.newplace.common.advice.exception.ExpiredUsersException;
import shop.newplace.common.advice.exception.LockedUsersException;
import shop.newplace.common.advice.exception.NotMatchPasswordException;
import shop.newplace.common.security.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final CustomUserDetailsService customUserDetailsService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String loginEmail = authentication.getName();
		String userPassword = authentication.getCredentials().toString();
		
		log.info("loginEmail : " + loginEmail);
		log.info("userPassword : " + userPassword);
		
		Users users = customUserDetailsService.loadUserByUsername(loginEmail);
		
		if(!passwordEncoder.matches(userPassword, users.getPassword())) {
			throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다", userPassword);
		} 
		if (!users.isCredentialsNonExpired()) {
			throw new ExpiredPasswordException("비밀번호가 만료되었습니다.", userPassword);
		}
		if (!users.isAccountNonExpired()) {
			throw new ExpiredUsersException("계정이 만료되었습니다.", loginEmail);
		}
		if (!users.isAccountNonLocked()) {
			throw new LockedUsersException("잠긴 계정입니다.", loginEmail);
		} 
		if (!users.isEnabled()) {
			throw new DisabledUsersException("사용 불가능한 계정입니다.", loginEmail);
		} 
		
		return new UsernamePasswordAuthenticationToken(
				users, userPassword, users.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
