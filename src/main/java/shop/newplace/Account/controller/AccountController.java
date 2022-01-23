package shop.newplace.Account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.newplace.Account.model.dto.request.SignUpRequestDto;
import shop.newplace.Account.service.AccountService;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity signup(SignUpRequestDto signUpRequestDto) {
        //return ResponseEntity.created(URI.create("/users/" + signUpRequestDto.getUserId())).body(accountService.insertAccount(signUpRequestDto));
        System.out.println("지점1");
        return ResponseEntity.status(HttpStatus.OK).body(accountService.insertAccount(signUpRequestDto));
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("테스트입니다.");
    }
}
