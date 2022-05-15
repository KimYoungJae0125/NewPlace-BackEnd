package shop.newplace.users.service;

import java.util.Optional;
<<<<<<< HEAD

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.users.model.dto.ProfilesDto;
import shop.newplace.users.model.entity.Profiles;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.ProfilesRepository;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.common.constant.Role;
import shop.newplace.common.util.CipherUtil;
=======
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import shop.newplace.common.constant.Role;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.ProfilesRequestDto;
import shop.newplace.users.model.entity.Profiles;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.model.repository.ProfilesRepository;
import shop.newplace.users.model.repository.UsersRepository;
>>>>>>> pre/feature/2022-03-06_signup

@Service
@RequiredArgsConstructor
public class ProfilesService {

	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	private final ModelMapper modelMapper;

	@Transactional
	public Profiles profileSignUp(ProfilesRequestDto.SignUp profileSignUpForm) {
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
