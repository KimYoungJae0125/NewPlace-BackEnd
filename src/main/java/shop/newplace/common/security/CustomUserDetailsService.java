package shop.newplace.common.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.NotFoundUsersException;
import shop.newplace.common.util.CipherUtil;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsersRepository usersRepository;
	
	@Override
	public Users loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
		return usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail))
				.orElseThrow(() -> new NotFoundUsersException("로그인을 실패하였습니다", loginEmail));
		
	}
	
}
