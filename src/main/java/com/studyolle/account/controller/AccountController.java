package com.studyolle.account.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyolle.account.service.AccountService;
import com.studyolle.repository.AccountRepository;
import com.studyolle.repository.dto.Account;
import com.studyolle.repository.dto.SignUpForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController {

	private final SignUpFormValidator signUpFormValidator;
	private final AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@InitBinder("signUpForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpFormValidator);
	}

	@GetMapping("/sign-up")
	public String signUpForm(Model model) {
		model.addAttribute("signUpForm", new SignUpForm());
		return "account/sign-up";
	}

	@PostMapping("/sign-up")
	public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
		if (errors.hasErrors()) {
			return "account/sign-up";
		}
		accountService.processNewAccount(signUpForm);
		return "account/sign-up";
	}
	
	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		Account account = accountRepository.findByEmail(email);
		String view= "account/checked-email";
		if(account == null) {
			model.addAttribute("error","wrong.email");
			return view;
		}
		if(!account.getEmailCheckToken().equals(token)) {
			model.addAttribute("error","wrong.token");
			return view;
		}
		account.completeSignUp();
		model.addAttribute("numberOfUser",accountRepository.count());
		model.addAttribute("nickname",account.getNickname());
		return view;
	}
}
