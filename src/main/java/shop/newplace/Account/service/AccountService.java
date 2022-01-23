package shop.newplace.Account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.newplace.Account.model.dto.SignUpForm;
import shop.newplace.Account.model.entity.Account;
import shop.newplace.Account.repository.AccountRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    // 회원가입
    public SignupResponse insertAccount(SignUpForm signUpForm){

        Optional<Account> byId = accountRepository.findById(11L);

        byId.orElseThrow(new 권한익셉션).getUserId();

        return SignupResponse.builder
                .id
                .email
                .build
    }
}
