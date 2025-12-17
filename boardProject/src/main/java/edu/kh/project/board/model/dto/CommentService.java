package edu.kh.project.board.model.dto;

import java.util.List;

public interface CommentService {

	List<Comment> select(int boardNo);

	int insert(Comment comment);

	int delete(int commentNo);

	int update(Comment comment);

}
