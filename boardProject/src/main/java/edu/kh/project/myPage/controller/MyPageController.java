package edu.kh.project.myPage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

// @SessionAttributes({"loginMember"})

/* 
 * @SessionAttributes의 역할
  - Model에 추가된 속성 중 key값이 일치하는 속성을 session scope로 변경하는 어노테이션
  - 클래스 상단에 @SessionAttributes({"loginMember"}) 작성
  
 * @SessionAttribute의 역할
  - @SessionAttributes를 통해 session에 등록된 속성을 꺼내올 때 사용하는 어노테이션
  - 메서드의 매개변수에 @SessionAttribute("loginMember") Member loginMember 작성
 */
@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {

	@Autowired
	private MyPageService service;
	
	// 내 정보 조회
	/**
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 Member 타입 매개변수에 대입
	 * @return
	 */
	@GetMapping("info") // /myPage/info GET 방식 요청 mapping
	public String info(@SessionAttribute("loginMember") Member loginMember,
						Model model) {
		
		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보 => session scope에 등록된 상태(loginMember)
		// loginMember(memberAddress도 포함)
		// 회원가입 당시 주소를 입력했다면 주소값 문자열(^^^ 구분자로 만들어진 문자열),
		// 입력하지 않았다면 null
		String memberAddress = loginMember.getMemberAddress();
		// 03189^^^서울 종로구 우정국로2길 21^^^3층 E클래스
		// 또는 null
		
		if(memberAddress != null) { // 입력한 주소가 있을 때만 동작
			// 구분자 "^^^"를 기준으로 memberAddress 값을 쪼개어 String[] 형태로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// ["03189", "서울 종로구 우정국로2길 21", "3층 E클래스"]
			
			model.addAttribute("postcode", arr[0]); // 우편번호
			model.addAttribute("address", arr[1]); // 도로명/지번 주소
			model.addAttribute("detailAddress", arr[2]); // 상세 주소
		}
		
		return "myPage/myPage-info";
	}
	
	// 프로필 이미지 변경 화면 이동
	@GetMapping("profile") // /myPage/profile GET 방식 요청 mapping
	public String profile() { 
		
		return "myPage/myPage-profile";
	}
	
	// 비밀번호 변경 화면 이동
	@GetMapping("changePw") // /myPage/changePw GET 방식 요청 mapping
	public String changePw() {
		
		return "myPage/myPage-changePw";
	}

	// 회원 탈퇴 화면 이동
	@GetMapping("secession") // /myPage/secession GET 방식 요청 mapping
	public String secession() {
		
		return "myPage/myPage-secession";
	}

	// 파일 테스트 화면 이동
	@GetMapping("fileTest") // /myPage/fileTest GET 방식 요청 mapping
	public String fileTest() {
		
		return "myPage/myPage-fileTest";
	}

	// 파일 리스트 화면 이동
	@GetMapping("fileList") // /myPage/fileList GET 방식 요청 mapping
	public String fileList(Model model,
						  @SessionAttribute("loginMember") Member loginMember) {
		
		// 파일 목록 조회 서비스 호출(현재 로그인한 회원이 올린 이미지만)
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		// model에 list 담아서 forward
		model.addAttribute("list", list);		
		
		return "myPage/myPage-fileList";
	}
	
	/** 회원 정보 수정
	 * @param inputMember : 커맨드 객체, 제출된 memberNickname, memberTel 세팅된 상태
     * @param memberAddress : 주소만 따로 배열 형태로 얻어옴
     * @param loginMember : 현재 로그인한 회원 정보(에서 PK인 회원번호 얻어와야 함)
  	 * @return
	 */
	@PostMapping("info") // /myPage/info POST 방식 요청 mapping
	public String updateInfo(@ModelAttribute Member inputMember,
							@RequestParam("memberAddress") String[] memberAddress,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) {
		
		// inputMember에 현재 로그인한 회원 번호 추가
		inputMember.setMemberNo(loginMember.getMemberNo());
		// inputMember : 수정된 회원의 닉네임, 수정된 회원의 전화번호, [주소], 회원 번호
		
		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);
		
		String message = null;
		
		if(result > 0) {
			message = "회원 정보 수정 완료";
			
			// loginMember에 DB 상 업데이트 된 내용으로 다시 세팅
			// => loginMember는 session에 저장된, 로그인한 회원 정보가 저장됨 (로그인 당시 기존 데이터)
			// => loginMember를 수정하면 session에 저장된 로그인한 회원 정보가 업데이트 됨
			// == session에 있는 회원 정보와 DB 데이터를 동기화
			
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
		} else {
			
			message = "회원 정보 수정 실패";
			
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:info"; // 재요청 경로 : /myPage/info GET 요청
	}
	
	@PostMapping("changePw")
	public String changePw(@RequestParam("newPw") String newPw,
						  @RequestParam("currentPw") String currentPw,
						  @SessionAttribute("loginMember")Member loginMember,
						  RedirectAttributes ra) {
		
		
		int result = service.changPw(newPw, currentPw, loginMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			
			message = "비밀번호 변경 완료";
			path = "info";
		
		} else {
			
			message = "현재 비밀번호가 일치하지 않아 변경 실패";
			path = "changePw";
			
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	/** 회원 탈퇴
	 * @param memberPw : 제출받은(사용자가 입력한) 비밀번호
	 * @param loginMember : 현재 로그인한 회원 정보를 가진 객체(session scope)
	 					   => 회원번호 필요! (SQL 조건)
	 * @param status : @SessionAttributes()와 함께 사용
	 * @return
	 */
	@PostMapping("secession") // /myPage/secession POST 요청 mapping
	public String secession(@RequestParam("memberPw") String memberPw,
							@SessionAttribute("loginMember") Member loginMember,
							SessionStatus status,
							RedirectAttributes ra) {

		// 로그인한 회원의 회원번호 꺼내오기
		int memberNo = loginMember.getMemberNo();
		
		// 서비스 호출(입력받은 비밀번호, 로그인한 회원번호)
		int result = service.secession(memberPw, memberNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) { // 탈퇴 성공 => 메인 페이지 재요청
			
			message = "탈퇴가 완료되었습니다";
			path = "/";
			
			status.setComplete(); // session 비우기(로그아웃 상태로 변경)
			
		} else { // 탈퇴 실패 => 회원 탈퇴 페이지 재요청
			
			message = "탈퇴 실패";
			path = "secession";
		}

		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	
	/*
	 * Spring에서 파일 처리하는 방법
	 
	  - enctype="multipart/form-data"로 클라이언트 요청을 받으면 (문자, 숫자, 파일 등이 섞인 요청)
	  	이를 MultipartResolver(FileConfig에 정의)를 이용해 섞여있는 parameter 분리
	  
	  => 문자열, 숫자 -> String
	  	 파일 -> MultipartFile
	 */
	@PostMapping("file/test1") // /myPage/file/test1 POST 요청 mapping
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile,
							 RedirectAttributes ra) {
		
		try {
			
			String path = service.fileUpload1(uploadFile);
			// /myPage/file/파일명.jpg
			
			// 파일이 실제로 서버 컴퓨터에 저장되어 웹에서 접근할 수 있는 경로가 반환된 경우
			if(path != null) {
				ra.addFlashAttribute("path", path);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.info("파일 업로드 예제1 중 예외 발생");
			
		}
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test2")  // /myPage/file/test2 POST 요청 매핑
	public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile, 
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) {
		
		try {
			
			// 로그인한 회원의 번호 얻어오기 (누가 업로드 했는가)
			int memberNo = loginMember.getMemberNo();
			
			// 업로드된 파일 정보를 DB에 INSERT 후 결과 행의 갯수 반환 받아옴
			int result = service.fileUpload2(uploadFile, memberNo);
			
			String message = null;
			
			if(result > 0) {
				message = "파일 업로드 성공";
				
			} else {
				message = "파일 업로드 실패..";
			}
			
			ra.addFlashAttribute("message", message);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 업로드 테스트2 중 예외발생");
		}
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test3") // /myPage/file/test3 POST 요청 매핑
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList, 
							@RequestParam("bbb") List<MultipartFile> bbbList,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) throws Exception {
		
		// aaa 파일 미제출 시
		// 0번, 1번 인덱스로 구성 - 파일은 모두 비어있음
		//log.debug("aaaList: "+ aaaList); // [요소, 요소]
		
		// bbb(multiple) 파일 미제출 시
		// 0번 인덱스로 구성 - 파일이 비어있음
		//log.debug("bbbList: "+ bbbList); // [요소]
		
		// 여러 파일 업로드 서비스 호출
		
		int result = service.fileUpload3(aaaList, bbbList, loginMember.getMemberNo());
		
		// result == aaaList와 bbbList에 업로드된 파일 갯수
		
		String message = null;
		
		if(result == 0) {
			message = "업로드된 파일이 없습니다";
			
		} else {
			message = result + "개의 파일이 업로드 되었습니다!";
			
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("profile") // /myPage/profile POST 요청 매핑
	public String profile(@RequestParam("profileImg") MultipartFile profileImg,
						@SessionAttribute("loginMember") Member loginMember,
						RedirectAttributes ra) throws Exception {
		
		// 서비스 호출
		int result = service.profile(profileImg, loginMember);
		
		String message = null;
		
		if(result > 0) {
			message = "변경 성공!";
		} else {
			message = "변경 실패ㅠㅠ";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile"; // 리다이렉트 - /myPage/profile GET 요청
	}
	
	
}










