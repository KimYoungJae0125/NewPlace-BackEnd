package shop.newplace.Account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.newplace.Account.model.dto.request.SignUpRequestDto;
import shop.newplace.Account.model.dto.response.SignupResponseDto;
import shop.newplace.Account.model.entity.Account;
import shop.newplace.Account.repository.AccountRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    //회원가입
    @Transactional
    public SignupResponseDto insertAccount(SignUpRequestDto signUpRequestDto){
        Account account = signUpRequestDto.toEntity();
        accountRepository.save(account);
        return SignupResponseDto.builder()
                .userId(account.getUserId())
                .email(account.getEmail())
                .password(account.getPassword())
                .name(account.getName())
//                .bankId(account.getBankId())
                .accountNumber(account.getAccountNumber())
                .failCount(account.getFailCount())
                .accountExpired(account.isAccountExpired())
                .accountLocked(account.isAccountLocked())
                .lastLoginAt(account.getLastLoginAt())
                .mainPhoneNumber(account.getMainPhoneNumber())
                .joinedAt(account.getJoinedAt())
                .mainPhoneNumber(account.getMainPhoneNumber())
                .joinedAt(account.getJoinedAt())
                .build();
    }
    
    // 회원가입
//    public SignupResponseDto insertAccount(SignUpRequestDto signUpRequestDto){
//
//        Optional<Account> byId = accountRepository.findById(11L);
//
//        byId.orElseThrow(new 권한익셉션).getUserId();
//
//        return SignupResponse.builder
//                .id
//                .email
//                .build
//    }
}
