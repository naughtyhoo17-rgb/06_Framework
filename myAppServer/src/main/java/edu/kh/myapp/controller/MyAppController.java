package edu.kh.myapp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin : Spring에서 제공하는 어노테이션, CORS 설정을 위해 사용
// CORS(Cross Origin Resource Sharing) : 클라이언트와 서버가 서로 다른 출처(origin)에서
// 										요청을 주고 받을 때 발생하는 보안 정책.
// 브라우저에서는 기본적으로 다른 출처(도메인, 프로토콜, 포트 등)의 요청을 차단
// => 클라이언트와 서버의 출처가 다를 때는 CORS 설정을 적절히 해주어야 정상적인 http 통신 가능

@CrossOrigin(origins = "http://localhost:5173") // http://localhost:5173와 자원을 공유하는 사이다.
@RestController // 비동기 요청을 처리하는 Controller 클래스
public class MyAppController {

	@GetMapping("getPortNumber")
	public List<String> getPortNumber() {
		
		return Arrays.asList("서버 포트는 80번", "클라이언트 포트는 5173번");
	}
	
	@PostMapping("getUserInfo")
	public String getUserInfo(@RequestBody Map<String, Object> map) {
		
		// message 리턴
		// 만약 요청 데이터 중 name값이 홍길동, age값이 20이면
		// "홍길동 님은 20세 입니다" 리턴
		// 그렇지 않다면 "데이터가 없습니다" 리턴
		String message = "데이터가 없습니다";
		
		if(map.get("name").equals("홍길동") && (int)map.get("age") == 20) {
			message = "홍길동 님은 20세 입니다";
		}
		
		return message;
		
	}
	
	
}










