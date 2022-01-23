package shop.newplace.Users.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.JwtForm;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.Users.token.JwtTokenProvider;
import shop.newplace.common.role.Role;
import shop.newplace.common.util.CipherUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImplement implements UsersService {
	
	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final CipherUtil.Email cipherEmail;
	private final CipherUtil.Name cipherName;
	private final CipherUtil.Phone cipherPhone;
	private final CipherUtil.BankId cipherBankId;
	private final CipherUtil.AccountNumber cipherAccountNumber;
	
	@Override
	@Transactional
	public Users signUp(SignUpForm signUpForm) throws Exception {
		Optional<Users> userLoginEmail = usersRepository.findByLoginEmail(signUpForm.getLoginEmail());
		log.info("userLoginEmail : " + userLoginEmail);
		log.info("encoder before : " + signUpForm.getPassword());
		String name				 = cipherName.encrypt(signUpForm.getName());
		String loginEmail 		 = cipherEmail.encrypt(signUpForm.getLoginEmail());
		String mainPhoneNumber 	 = cipherPhone.encrypt(signUpForm.getMainPhoneNumber());
		String encodedPassword 	 = passwordEncoder.encode(signUpForm.getPassword());
		String bankId 			 = cipherBankId.encrypt(signUpForm.getBankId());
		String accountNumber  	 = cipherAccountNumber.encrypt(signUpForm.getAccountNumber());
		Users usersParam = Users.builder()
								.name(name)
								.loginEmail(loginEmail)
								.password(encodedPassword)
								.mainPhoneNumber(mainPhoneNumber)
								.accountNonExpired(true)
								.accountNonLocked(true)
								.bankId(bankId)
								.accountNumber(accountNumber)
								.failCount("0")
								.lastLoginTime(null)
								.authId(Role.USER.getRoleValue())
								.build();
		log.info("encoder after: " + encodedPassword);
		Users result = usersRepository.save(usersParam);
		
		log.info("==============================");
		log.info(result.toString());
		log.info("==============================");
		
//		accountEmail.orElseThrow(() -> new UsernameNotFoundException("not found : " + accountEmail));
		
		return result;
	}
	
	@Override
	public JwtForm signIn(SignInForm logInForm) throws Exception {
		
		Users usersInfo = usersRepository.findByLoginEmail(cipherEmail.encrypt(logInForm.getLoginEmail()))
												   .orElseThrow(() -> new UsernameNotFoundException(logInForm.getLoginEmail() + "해당 사용자가 존재하지 않습니다"));

		Users joinUsersInfo = usersRepository.findAllByLoginEmail(cipherEmail.encrypt(logInForm.getLoginEmail()))
				.orElseThrow();

		String name 			= cipherName.decrypt(usersInfo.getName());
		String loginEmail 		= cipherEmail.decrypt(usersInfo.getLoginEmail());
		String mainPhoneNumber 	= cipherPhone.decrypt(usersInfo.getMainPhoneNumber());
		String bankId 			= cipherBankId.decrypt(usersInfo.getBankId());
		String accountNumber 	= cipherAccountNumber.decrypt(usersInfo.getAccountNumber());
		
		//modelMapper
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
						.failCount("0")
						.lastLoginTime(LocalDateTime.now())
						.authId(usersInfo.getAuthId())
						.build();
		
		usersRepository.save(usersParam);
		
		Users users = Users.builder()
						   .name(name)
						   .loginEmail(loginEmail)
						   .mainPhoneNumber(mainPhoneNumber)
						   .bankId(bankId)
						   .accountNumber(accountNumber)
						   .build();
		
		String token = jwtTokenProvider.createToken(loginEmail, usersInfo.getAuthorities());
		String resCd = "0000";
		String resMsg = "로그인 성공";
				
		JwtForm jwtForm = JwtForm.builder()
		  						 .token(token)
								 .resCd(resCd)
								 .resMsg(resMsg)
								 .build();
		
		return jwtForm;
	}
	
	@Override
	@Transactional
	public Profiles profileSignUp(ProfileSignUpForm profileSignUpForm) throws Exception {
		
		Long userId			  	 = profileSignUpForm.getUserId();
		String nickName			 = profileSignUpForm.getNickName();
		String introduction		 = profileSignUpForm.getIntroduction();
		String email 	 		 = cipherEmail.encrypt(Optional.ofNullable(profileSignUpForm.getEmail()).orElse(""));
		String phoneNumber 	 	 = cipherPhone.encrypt(Optional.ofNullable(profileSignUpForm.getPhoneNumber()).orElse(""));
		String bankId 			 = cipherBankId.encrypt(Optional.ofNullable(profileSignUpForm.getBankId()).orElse(""));
		String accountNumber  	 = cipherAccountNumber.encrypt(Optional.ofNullable(profileSignUpForm.getAccountNumber()).orElse(""));
		Users users = usersRepository.findById(userId).get();
		
		
		Profiles profilesParam = Profiles.builder()
										 .users(users)
										 .nickName(nickName)
										 .email(email)
										 .phoneNumber(phoneNumber)
										 .bankId(bankId)
										 .accountNumber(accountNumber)
										 .introduction(introduction)
										 .authId(Role.USER.getRoleValue())
										 .build();
		Profiles result = profilesRepository.save(profilesParam);
		
		log.info("==============================");
		log.info(result.toString());
		log.info("==============================");
		
		
		return result;
	}
	

}
