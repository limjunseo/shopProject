package com.example.demo.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping(value = "/new")
	public String memberForm(ModelMap model) {
		model.put("memberFormDto", new MemberFormDto());
		return "/member/memberForm";
	}
	
	@PostMapping(value = "/new")
	public String saveMember(@ModelAttribute @Valid MemberFormDto memberFormDto, BindingResult bindingResult, ModelMap model) {
		
		if(bindingResult.hasErrors()) {
			return "/member/memberForm";
		}
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.put("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		return "redirect:/";
	}
	
	@GetMapping(value ="/login")
	public String loginMember() {
		return "/member/memberLoginForm";
	}
	
	@GetMapping(value ="/login/error")
	public String loginError(ModelMap model) {
		model.put("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "/member/memberLoginForm";
	}
	
	
	


}
