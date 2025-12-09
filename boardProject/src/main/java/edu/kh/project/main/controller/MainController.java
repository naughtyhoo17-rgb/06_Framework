package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

	@RequestMapping("/") // "/" 요청 mapping
	public String mainPage() {
		
		// 접두사, 접미사 제외
		return "common/main"; // forward
	}

	// loginFilter에서 로그인 하지 않은 경우에 온 redirect 
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		
		ra.addFlashAttribute("message", "로그인 후 이용 가능");
		
		return "redirect:/";
	}
	
	
	
	
}





