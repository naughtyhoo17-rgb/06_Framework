package edu.kh.project.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.admin.model.service.AdminService;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("admin")
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})
@Slf4j
public class AdminController {
	
	private final AdminService service;
	
	@PostMapping("login")
	public Member login(@RequestBody Member inputMember, Model model) {
		
		Member loginMember = service.login(inputMember);
		log.debug("loginMember : {}", loginMember);
		
		if(loginMember == null) return null;
		
		model.addAttribute("loginMember", loginMember);
		return loginMember;
		
	}
	
	@GetMapping("logout")
	public ResponseEntity<String> logout(HttpSession session) {
		// ResponseEntity 
		// Spring에서 제공하는 Http 응답 데이터를
		// 커스터마이징 할 수 있도록 지원하는 클래스
		// -> Http 상태코드, 헤더, 응답 본문(body)을 모두 설정 가능
		try {
			session.invalidate(); // 세션 무효화 처리
			return ResponseEntity.status(HttpStatus.OK) // 200
					.body("로그아웃이 완료되었습니다");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
					.body("로그아웃 중 예외 발생 : " + e.getMessage());
		}
		
	}
	
	/** 관리자 계정 발급
	 * @return
	 */
	@PostMapping("createAdminAccount")
	public ResponseEntity<String> createAdminAccount(@RequestBody Member member) {
		try {
			
			// 1. 이메일 중복 검사 (중복 시 발급 X)
			int checkEmail = service.checkEmail(member.getMemberEmail());
			
			if(checkEmail > 0) { // 중복된 경우
				
				// HttpStatus.CONFLICT (409) : 요청이 서버의 현재 상태와 충돌할 때 사용
				// == 이미 존재하는 리소스(email) 때문에 새로운 리소스를 만들 수 없다.
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
			}
			
			// 2. 새로 발급 -> 비밀번호 반환
			String accountPw = service.createAdminAccount(member);
			
			// HttpStatus.OK (200) : 요청이 정상적으로 처리되었으나 기존 리소스에 대한 단순 처리
			// HttpStatus.CREATED (201) : 자원이 성공적으로 생성되었음을 나타냄
			return ResponseEntity.status(HttpStatus.CREATED).body(accountPw);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자 계정 생성 중 예외 발생 (서버 문의 바람)");
		}
		
	}
	
	/** 관리자 계정 목록 조회
	 * @return
	 */
	@GetMapping("adminAccountList")
	public ResponseEntity<Object> adminAccountList() {
		
		try {
			
			List<Member> adminList = service.adminAccountList();
			
			return ResponseEntity.status(HttpStatus.OK).body(adminList);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/** 최다 조회 수 게시글 조회
	 * @return
	 */
	@GetMapping("maxReadCount")
	public ResponseEntity<Object> maxReadCount() {
		
		try {
			
			Board board = service.maxReadCount();
			return ResponseEntity.status(HttpStatus.OK).body(board);
			
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/** 탈퇴한 회원 리스트 조회
	 * @return
	 */
	@GetMapping("withdrawnMemberList")
	public ResponseEntity<Object> selectWithdrawnMemberList() {
		// 성공 시 List<Member> 반환, 에러 발생 시 String => thus <Object>
		
		try {
			
			List<Member> withdrawnMemberList = service.selectWithdrawnMemberList();
			return ResponseEntity.status(HttpStatus.OK).body(withdrawnMemberList);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴한 회원 목록 조회 중 문제 발생 : " + e.getMessage());
		}
	}
	
	/**
	 * @return
	 */
	@PutMapping("restoreMember")
	public ResponseEntity<String> restoreMember(@RequestBody Member member) {
		
		try {
			
			int result = service.restoreMember(member.getMemberNo());
			
			if(result > 0) {
				
				return ResponseEntity.status(HttpStatus.OK)
						.body(member.getMemberNo() + "번 회원 복구 완료");
			} else {
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("유효하지 않은 memberNo : " + member.getMemberNo());				
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴회원 복구 중 에러 발생" + e.getMessage());
		}
	}
	
	
	
}










