package shop.newplace.users.service;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.common.security.CustomUserDetails;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.util.RedisUtil;
import shop.newplace.users.exception.NotFoundUsersException;
import shop.newplace.users.model.dto.ProfilesDto;
import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.users.token.JwtTokenProvider;
import shop.newplace.users.token.model.dto.JwtDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final ModelMapper modelMapper;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RedisUtil redisService;
	
	private final ProfilesService profilesService;
	
	@Transactional
	public void signUp(UsersDto.RequestSignUp usersSignUpForm) {
		String name = usersSignUpForm.getName();
		usersSignUpForm
			.setName(CipherUtil.Name.encrypt(usersSignUpForm.getName()))
			.setLoginEmail(CipherUtil.Email.encrypt(usersSignUpForm.getLoginEmail()))
			.setMainPhoneNumber(CipherUtil.Phone.encrypt(usersSignUpForm.getMainPhoneNumber()))
			.setPassword(passwordEncoder.encode(usersSignUpForm.getPassword()))
			.setBankId(CipherUtil.BankId.encrypt(usersSignUpForm.getBankId()))
			.setAccountNumber(CipherUtil.AccountNumber.encrypt(usersSignUpForm.getAccountNumber()));
		Users users = usersRepository.save(modelMapper.map(usersSignUpForm, Users.class));
		ProfilesDto.RequestSignUp profiles = new ProfilesDto.RequestSignUp();
		if(usersSignUpForm.getProfilesSignUp() == null) {
			profiles = ProfilesDto.RequestSignUp.builder()
												.userId(users.getId())
												.users(users)
												.nickName(name)
												.build();
		} else {
			profiles = usersSignUpForm.getProfilesSignUp()
									  .setUserId(users.getId())
									  .setUsers(users)
									  .setNickName(Optional.ofNullable(usersSignUpForm.getProfilesSignUp().getNickName()).orElse(name));
		}
		profilesService.profileSignUp(profiles);
	}
	
	@Transactional
	public JwtDto.AccessToken logIn(Authentication authentication, HttpServletResponse response) {
		CustomUserDetails securityUsers = (CustomUserDetails) authentication.getPrincipal();
		Users usersInfo = securityUsers.getUsers();
		usersInfo.successLogin();
		usersRepository.save(usersInfo);
		String loginEmail = CipherUtil.Email.decrypt(usersInfo.getLoginEmail());
		String accessToken = jwtTokenProvider.createAccessToken(usersInfo.getId().toString(), loginEmail, authentication.getAuthorities());
		String refreshToken = jwtTokenProvider.createRefreshToken(usersInfo.getId().toString(), authentication.getAuthorities());
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);
		jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
		JwtDto.AccessToken jwtAceessToken = JwtDto.AccessToken.builder()
												  	  .accessToken(accessToken)
								  	  				  .build();
		JwtDto.RefreshToken jwtRefreshToken = JwtDto.RefreshToken.builder()
																  .id(usersInfo.getId())
																  .refreshToken(refreshToken)
																  .expirationTime(100L * 60 * 60 * 24 * 7)
																  .build();
		redisService.setValues(jwtRefreshToken);
		return jwtAceessToken;
	}

	public UsersDto.ResponseInfo getUserInfo(Long userId) {
		Users usersInfo = usersRepository.findById(userId)
							   .orElseThrow(() -> new NotFoundUsersException("해당 유저는 존재하지 않습니다", "userId : " + userId) );
		return UsersDto.ResponseInfo.builder()
							.userId(usersInfo.getId())
							.loginEmail(CipherUtil.Email.decrypt(usersInfo.getLoginEmail()))
							.name(CipherUtil.Name.decrypt(usersInfo.getName()))
							.bankId(CipherUtil.BankId.decrypt(usersInfo.getBankId()))
							.accountNumber(CipherUtil.AccountNumber.decrypt(usersInfo.getAccountNumber()))
							.mainPhoneNumber(CipherUtil.Phone.decrypt(usersInfo.getMainPhoneNumber()))
							.build();
	}
	
}
