package ca.sheridancollege.hoodsi.controllers;

import ca.sheridancollege.hoodsi.beans.CheckUser;
import ca.sheridancollege.hoodsi.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@GetMapping("/")
	public String index() {
	    return "index";
	}
	
	@GetMapping("/secure")
	public String secureIndex(Authentication authentication) {
		System.out.println(authentication.getDetails());
		System.out.println(authentication.getName());
		return "/secure/index";
	}

	@GetMapping("/login")
	public String login() {
	    return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("userBean", new CheckUser());
		return "register";
	}

	@PostMapping("/")
	public String checkUserAndRedirectToHome(
			@ModelAttribute("user") CheckUser user,
			Model m
	) {
		userDetailsService.insertion(user);
		return "login";
	}


	@GetMapping("/permission-denied")
	public String permissionDenied() {
	    return "/error/permission-denied";
	}
	
}
