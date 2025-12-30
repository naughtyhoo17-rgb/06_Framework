package edu.kh.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingRoom {

	private int chattingRoomNo; // 채팅방 번호
	private String lastMessage; // 채팅방의 마지막 메시지
	private String sendTime; // 마지막 메시지를 전송한 시간
	private int targetNo; // 채팅방의 대상자 회원 번호
	private String targetNickName; // 대상자 닉네임
	private String targetProfile; // 대상자의 프로필 이미지 경로
	private int notReadCount; // 채팅방의 읽지 않은 메시지 개수
	
}
