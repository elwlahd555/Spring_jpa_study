package com.studyolle.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studyolle.repository.dto.SignUpForm;

@Controller
public class AccountController {
	
	@GetMapping("sign-up")
	public String signUpForm(Model model) {
		model.addAttribute("signUpForm",new SignUpForm());
		return "account/sign-up";
	}
	
}
