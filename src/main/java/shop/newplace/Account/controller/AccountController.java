package shop.newplace.Account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @PostMapping("/signup")
    public ResponseEntity signup() {
        return ResponseEntity.ok("성공!");
    }




}
