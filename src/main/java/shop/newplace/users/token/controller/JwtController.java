package shop.newplace.users.token.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.response.ResponseMessage;
import shop.newplace.users.token.model.dto.JwtDto;
import shop.newplace.users.token.service.JwtService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/jwt")
public class JwtController {
	
	private final JwtService jwtService;
	
	@GetMapping
	public ResponseEntity getUserByAccessToken(@RequestBody JwtDto.AccessToken jwtAccessToken) {
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "유저 정보 조회에 성공하였습니다.", jwtService.getUserInfo(jwtAccessToken)));
	}

}
