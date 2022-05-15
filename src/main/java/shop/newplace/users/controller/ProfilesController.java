package shop.newplace.users.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.common.response.ResponseMessage;
import shop.newplace.users.model.dto.ProfilesRequestDto;
import shop.newplace.users.model.entity.Profiles;
import shop.newplace.users.model.validator.ProfilesValidator;
import shop.newplace.users.service.ProfilesService;

@Slf4j
@RestController
@RequiredArgsConstructor 
@RequestMapping("/users/{userId}/profiles")
public class ProfilesController {
	
	private final ProfilesService profilesService;
	private final ProfilesValidator.SignUp profileSignUpFormValidator;
	
    ///users/1/profiles
    @PostMapping
    public ResponseEntity goCreateProfile(@PathVariable(name = "userId") Long userId, @Valid @RequestBody ProfilesRequestDto.SignUp profileSignUpForm, BindingResult bindingResult) {
    	profileSignUpForm.setUserId(userId);
    	profileSignUpFormValidator.validate(profileSignUpForm, bindingResult);
    	profilesService.profileSignUp(profileSignUpForm);
    	Profiles profiles = null;
//    	Profiles profiles = profilesService.profileSignUp(profileSignUpForm);
    	ResponseMessage body = ResponseMessage.OK(201, "프로필 생성", "프로필 생성 성공", profiles);
		return ResponseEntity.ok().body(body);
    }
    

}
