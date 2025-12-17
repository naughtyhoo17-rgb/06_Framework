package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	Member login(Member inputMember) throws Exception;

	/** 이메일 중복 검사 서비스
	 * @param memberEmail
	 * @return
	 * @throws Exception
	 */
	int checkEmail(String memberEmail);

	/** 닉네임 중복 검사 서비스
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

	/** 회원가입 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int signup(Member inputMember, String[] memberAddress);

	/** 전체 회원 목록 조회 서비스
	 * @return
	 */
	List<Member> selectMemberList();

	/** 특정 회원 비밀번호 초기화 서비스
	 * @param memberNo
	 * @return
	 */
	int resetPw(int memberNo);

	/** 특정 회원 탈퇴 복구 서비스
	 * @param memberNo
	 * @return
	 */
	int restoreDelFl(int memberNo);

}
