package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 부분 작성 SQL 수행
	 * @param inputBoard
	 * @return
	 */
	public int boardInsert(Board inputBoard);

	/** 게시글 이미지 삽입 SQL 수행
	 * @param uploadList
	 * @return
	 */
	public int insertUploadList(List<BoardImg> uploadList);

	/** 게시글 부문(제목, 내용) 수정 SQL 
	 * @param inputBoard
	 * @return
	 */
	public int boardUpdate(Board inputBoard);

	/** 게시글 이미지 삭제 SQL
	 * @param map
	 * @return
	 */
	public int deleteImage(Map<String, Object> map);

	/** 게시글 이미지 수정 SQL
	 * @param img
	 * @return
	 */
	public int updateImage(BoardImg img);

	/** 게시글 이미지 삽입 SQL
	 * @param img
	 * @return
	 */
	public int insertImage(BoardImg img);

	/** 게시글 삭제 SQL
	 * @param inputBoard
	 * @return
	 */
	public int boardDelete(Board inputBoard); 

}
