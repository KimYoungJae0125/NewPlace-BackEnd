package shop.newplace.Users.controller;

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
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.validator.ProfileSignUpFormValidator;
import shop.newplace.Users.service.ProfilesService;
import shop.newplace.common.response.ResponseMessage;

@Slf4j
@RestController
@RequiredArgsConstructor 
@RequestMapping("/users/{userId}/profiles")
public class ProfilesController {
	
	private final ProfilesService profilesService;
	private final ProfileSignUpFormValidator profileSignUpFormValidator;
	
    ///users/1/profiles
    @PostMapping
    public ResponseEntity goCreateProfile(@PathVariable(name = "userId") Long userId, @Valid @RequestBody ProfileSignUpForm profileSignUpForm, BindingResult bindingResult) {
    	profileSignUpForm.setUserId(userId);
    	profileSignUpFormValidator.validate(profileSignUpForm, bindingResult);
    	Profiles profiles = profilesService.profileSignUp(profileSignUpForm);
    	ResponseMessage body = ResponseMessage.OK(201, "프로필 생성", "프로필 생성 성공", profiles);
		return ResponseEntity.ok().body(body);
    }
    

}
