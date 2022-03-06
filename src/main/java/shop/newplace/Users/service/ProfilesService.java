package shop.newplace.Users.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.dto.ProfilesDto;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.role.Role;
import shop.newplace.common.util.CipherUtil;

@Service
@RequiredArgsConstructor
public class ProfilesService {

	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	private final ModelMapper modelMapper;

	@Transactional
	public Profiles profileSignUp(ProfilesDto.SignUp profileSignUpForm) {
		Users users = usersRepository.findById(profileSignUpForm.getUserId()).get();
		profileSignUpForm
			.setUsers(users)
			.setEmail(CipherUtil.Email.encrypt(profileSignUpForm.getEmail()))
			.setPhoneNumber(CipherUtil.Phone.encrypt(profileSignUpForm.getPhoneNumber()))
			.setBankId(CipherUtil.BankId.encrypt(profileSignUpForm.getBankId()))
			.setAccountNumber(CipherUtil.AccountNumber.encrypt(profileSignUpForm.getAccountNumber()))
			.setAuthId(Optional.ofNullable(profileSignUpForm.getAuthId()).orElse(Role.USER.getValue()));
//		.setEmail(CipherUtil.Email.encrypt(Optional.ofNullable(profileSignUpForm.getEmail()).orElse("")))
//		.setPhoneNumber(CipherUtil.Phone.encrypt(Optional.ofNullable(profileSignUpForm.getPhoneNumber()).orElse("")))
//		.setBankId(CipherUtil.BankId.encrypt(Optional.ofNullable(profileSignUpForm.getBankId()).orElse("")))
//		.setAccountNumber(CipherUtil.AccountNumber.encrypt(Optional.ofNullable(profileSignUpForm.getAccountNumber()).orElse("")))
//		.setAuthId(Optional.ofNullable(profileSignUpForm.getAuthId()).orElse(Role.USER.getValue()));
		return profilesRepository.save(modelMapper.map(profileSignUpForm, Profiles.class));
	}

}
