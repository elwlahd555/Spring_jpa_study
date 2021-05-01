package com.studyolle.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.studyolle.config.AppProperties;
import com.studyolle.email.service.EmailService;
import com.studyolle.repository.AccountRepository;
import com.studyolle.repository.dto.Account;
import com.studyolle.repository.dto.EmailMessage;
import com.studyolle.repository.dto.SignUpForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
	
	@Transactional
	public void processNewAccount(SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		sendSignUpConfirmEmail(newAccount);
	}
	
	public Account saveNewAccount(SignUpForm signUpForm) {
		Account account = Account.builder()
				.email(signUpForm.getEmail())
				.nickname(signUpForm.getNickname())
				.password(passwordEncoder.encode(signUpForm.getPassword()))
				.studyCreatedByWeb(true).studyEnrollmentResultByWeb(true).studyUpdatedByWeb(true).build();
		Account newAccount = accountRepository.save(account);

		newAccount.generateEmailCheckToken();
		return newAccount;
	}

	public void sendSignUpConfirmEmail(Account newAccount) {

//        Context context = new Context();
//        context.setVariable("link", );
//        context.setVariable("nickname", newAccount.getNickname());
//        context.setVariable("linkName", "이메일 인증하기");
//        context.setVariable("message", "스터디올래 서비스를 사용하려면 링크를 클릭하세요.");
//        context.setVariable("host", appProperties.getHost());
//        String message = templateEngine.process("mail/simple-link", context);
        
        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("스터디올래, 회원 가입 인증")
                .message("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                        "&email=" + newAccount.getEmail())
                .build();
		emailService.sendEmail(emailMessage);
	}
}
