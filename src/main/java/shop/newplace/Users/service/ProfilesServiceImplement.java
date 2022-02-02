package shop.newplace.Users.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.ValidFailureException;
import shop.newplace.common.role.Role;
import shop.newplace.common.util.CipherUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfilesServiceImplement implements ProfilesService {
	
	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	@Override
	@Transactional
	public Profiles profileSignUp(ProfileSignUpForm profileSignUpForm) {
		
		Long userId			  	 = profileSignUpForm.getUserId();
		String nickName			 = profileSignUpForm.getNickName();
		String introduction		 = profileSignUpForm.getIntroduction();
		String email 	 		 = CipherUtil.Email.encrypt(Optional.ofNullable(profileSignUpForm.getEmail()).orElse(""));
		String phoneNumber 	 	 = CipherUtil.Phone.encrypt(Optional.ofNullable(profileSignUpForm.getPhoneNumber()).orElse(""));
		String bankId 			 = CipherUtil.BankId.encrypt(Optional.ofNullable(profileSignUpForm.getBankId()).orElse(""));
		String accountNumber  	 = CipherUtil.AccountNumber.encrypt(Optional.ofNullable(profileSignUpForm.getAccountNumber()).orElse(""));
		Users users = usersRepository.findById(userId)
									.orElseThrow(()
												-> new ValidFailureException("존재하지 않는 사용자입니다."));
		
		Users userParam = Users.builder()
							   .id(users.getId())
							   .bankId(users.getBankId())
							   .loginEmail(users.getLoginEmail())
							   .bankId(users.getBankId())
							   .mainPhoneNumber(users.getMainPhoneNumber())
							   .name(users.getName())
							   .password(users.getPassword())
							   .authId(users.getAuthId())
							   .build();
		
		
		Profiles profilesParam = Profiles.builder()
										 .users(userParam)
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
