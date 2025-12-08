package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberMapper mapper;
	
	// Bcrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) throws Exception{
		
		// 암호화 테스트
		// bcrypt.encode(문자열) : 평문을 암호화하여 반환
		// String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		// log.debug("bcryptPassword : " + bcryptPassword);
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원의 비밀번호 조회
		Member loginMember =  mapper.login(inputMember.getMemberEmail());

		// 2. 일치하는 이메일이 없는 경우 => 조회 결과 null
		if(loginMember == null)	return null;
		
		// 3. 일치하는 이메일이 있는 경우 
		// => 입력 받은 비밀번호(평문 inputMember.getMemberPw())와 
		//	암호화 된 비밀번호(loginMember.getMemberPw()) 일치 여부 확인
		
		// bcrypt.matches(평문, 암호화) : 내부적으로 일치한다고 판단되면 true, 아니면 false
			// 평문과 암호화된 비밀번호 일치하지 않는 경우
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) return null;
			// 평문과 암호화된 비밀번호 일치하는 경우
			// => 로그인한 회원 정보에서 비밀번호 제거
			// 결국 클라이언트에게 보여질 때 비밀번호는 보이지 않게끔
		loginMember.setMemberPw(null);
		
		return loginMember;
		
	}

	// 이메일 중복 검사 서비스
	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}

	// 닉네임 중복 검사 서비스
	@Override
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}

}











