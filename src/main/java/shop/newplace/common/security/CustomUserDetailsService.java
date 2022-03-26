package shop.newplace.common.security;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.newplace.users.exception.NotFoundUsersException;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.util.DateUtil;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsersRepository usersRepository;
	
	@Override
	@Transactional
	public CustomUserDetails loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
		Users users = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail))
									 .orElseThrow(() -> new NotFoundUsersException("해당 유저가 존재하지 않습니다.", loginEmail));
		return CustomUserDetails.builder()
								.users(users)
								.enabled(users.getDeletedAt() == null)
								.accountNonLocked(users.getFailCount() <= 5)
								.accountNonExpired(DateUtil.isAfterToday(DateUtil.plusMonths(users.getLastLoginTime(), 3)))
								.credentialsNonExpired(DateUtil.isAfterToday(users.getPasswordExpiredTime()))
								.roles(users.getProfiles().stream().map(profiles -> profiles.getAuthId()).collect(Collectors.toSet()))
								.build();	
	}
}
