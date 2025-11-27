package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Spring EL 같은 경우 DTO 객체 출력 시 getter가 필수로 작성되어 있어야!
 => ${Student.name} == ${Student.getName()}
 내부적으로 해당 DTO의 getter를 호출하고 있기 때문! */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	private String studentNo; // 학생번호
	private String name; // 이름
	private int age; // 나이
}
