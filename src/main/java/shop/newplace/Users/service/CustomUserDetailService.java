package shop.newplace.Users.service;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.dto.PrincipalVo;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	
	private final UsersRepository usersRepository;
	
	@Override
	public PrincipalVo loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
 		 Users usersInfo = usersRepository.findByLoginEmail(loginEmail)
		  .orElseThrow(() -> new UsernameNotFoundException(loginEmail + "은 등록되지 않은 이메일입니다."));
			
		Users usersParam = Users.builder()
					 .id(usersInfo.getId())
					 .name(usersInfo.getName())
					 .loginEmail(usersInfo.getLoginEmail())
					 .password(usersInfo.getPassword())
					 .mainPhoneNumber(usersInfo.getMainPhoneNumber())
					 .accountNonExpired(true)
					 .accountNonLocked(true)
					 .bankId(usersInfo.getBankId())
					 .accountNumber(usersInfo.getAccountNumber())
					 .failCount(0)
					 .lastLoginTime(LocalDateTime.now())
					 .authId(usersInfo.getAuthId())
					 .build();
		
		usersRepository.save(usersParam);
		
		
		PrincipalVo principal = (PrincipalVo) usersParam;
		
		return principal;
	}

}
