package edu.kh.project.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@PropertySource("classpath:/config.properties")
@Slf4j
public class EditBoardServiceImpl implements EditBoardService {

	@Autowired
	private EditBoardMapper mapper;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	// 게시글 작성 서비스
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {

		// 1. 게시글 부분(inputBoard)을 먼저 BOARD 테이블에 INSERT 하기
		// -> INSERT된 게시글의 번호(시퀀스 번호) 반환 받기
		int result = mapper.boardInsert(inputBoard);
		
		// result의 결과 행의 개수를 가지고 다음 과정 진행 여부 결정
		
			// 삽입 실패 시 return 0; (image 쪽은 고려할 필요가 없는 셈)
		if(result == 0) return 0;
		
			// 삽입 성공 시 삽입된 게시글의 번호를 변수로 저장
		int boardNo = inputBoard.getBoardNo();
		// => mapper.xml에서 <selectKey> 태그를 이용해서 생성된 boardNo가
		// inputBoard에 세팅된 상태!! (얕은 복사 개념 이해 필수)
		
		// 2. 업로드된 이미지가 실제로 존재할 경우
		// 업로드된 이미지만 별도로 저장하여 BOARD_IMG 테이블에 삽입
		
		// 실제로 업로드된 이미지만 모아둘 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
		
		// images List에서 요소를 하나 씩 꺼내 실제 파일 존재 여부 검사
		for(int i = 0; i < images.size(); i++) {
			
			// 실제 파일이 제출된 겨우
			if(!images.get(i).isEmpty()) {
				
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				// 모든 값을 저장할 BoardImg DTO로 객체 생성
				BoardImg img = BoardImg.builder()
							  .imgOriginalName(originalName)
							  .imgRename(rename)
							  .imgPath(webPath)
							  .boardNo(boardNo)
							  .imgOrder(i)
							  .uploadFile(images.get(i))
							  .build();
				
				// uploadList에 추가
				uploadList.add(img);
			}
		}
		
		// uploadList가 비어있는 경우 == 실제로 제출된 파일이 한 개도 없는 경우
		if(uploadList.isEmpty()) {
			return boardNo;
		}
		
		// 제출된 파일이 존재하는 경우
		// => DB에 "BOARD_IMG" 테이블에 INSERT & 서버에 파일 저장(transferTo())
		result = mapper.insertUploadList(uploadList);
		// result == 실제 삽입된 행의 개수가 담겨야 함 == 제출한 이미지 파일의 개수(uploadList.size())
		
		// 다중 INSERT 성공 확인
		if(result == uploadList.size()) {
			
			// 서버에 파일 저장
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}
			
		} else { // 1개라도 삽입 실패한 경우 => 전체 서비스 실패(성공한 부분 무의미)
			// 삽입 성공한 내용도 모두 rollback
			
			// rollback 하는 방법 => RuntimeException 강제 발생(@Transactional 기본 RuntimeException)
			throw new RuntimeException();
			
			
		}
		
		
		
		return boardNo;
		
	}
	
	
	
	
	
	
}





