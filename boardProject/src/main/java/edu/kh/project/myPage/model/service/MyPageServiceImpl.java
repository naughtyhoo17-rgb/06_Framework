package edu.kh.project.myPage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MyPageServiceImpl implements MyPageService{

	@Autowired
	private MyPageMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공

		if(!inputMember.getMemberAddress().equals(",,")) { // 주소가 입력되었을 때
			
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
			
		} else { // 주소가 입력되지 않았을 때
			
			inputMember.setMemberAddress(null);
		
		}
		
		// inputMember : 수정 닉네임, 수정 전화번호, 수정 주소, 회원 번호
		
		return mapper.updateInfo(inputMember);
	
	}

	// 비밀번호 변경
	@Override
	public int changPw(String newPw, String currentPw, Member loginMember) {
		
		String storePw = mapper.checkPw(loginMember.getMemberNo());
		
		if(!bcrypt.matches(currentPw, storePw)) return 0;
		
		String encPw = bcrypt.encode(newPw);
		loginMember.setMemberPw(encPw);
		
		return mapper.changePw(loginMember);
		
	}
	
}
