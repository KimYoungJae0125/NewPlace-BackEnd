package shop.newplace.Account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.newplace.Account.model.dto.SignUpForm;
import shop.newplace.Account.model.entity.Account;
import shop.newplace.Account.repository.AccountRepository;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    // 회원가입
    public ResponseEntity insertAccount(SignUpForm signUpForm){

    }
}
