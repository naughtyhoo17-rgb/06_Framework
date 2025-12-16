package edu.kh.project.myPage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	/** 회원 정보 수정 SQL 실행
	 * @param inputMember
	 * @return
	 */
	public int updateInfo(Member inputMember);

	/** 비밀번호 수정 SQL 실행
	 * @param newPw
	 * @return
	 */
	public int changePw(Member loginMember);

	/** 비밀번호 확인 SQL 실행
	 * @param memberNo
	 * @return
	 */
	public String checkPw(int memberNo);

	/** 회원 탈퇴 SQL (UPDATE, 곧바로 DELETE 아님)
	 * @param memberNo
	 * @return
	 */
	public int secession(int memberNo);

	/** 파일 정보를 DB에 삽입 SQL (insert)
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회하는 SQL
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);

	/** 프로필 이미지 변경 SQL
	 * @param member
	 * @return
	 */
	int profile(Member member);

	

}
