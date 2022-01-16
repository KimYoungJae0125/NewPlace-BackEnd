package shop.newplace.Account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.newplace.Account.model.dto.SignUpForm;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    @PostMapping("/signup")
    public ResponseEntity signup(SignUpForm signUpForm) {
        return ResponseEntity.ok("성공!");
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("테스트입니다.");
    }
}
