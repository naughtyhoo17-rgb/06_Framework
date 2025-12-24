package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// Pointcut을 모아두는 클래스
public class PointcutBundle {

	// 작성하기 어려운 Pointcut을 미리 작성해두고 필요한 곳에서 클래스명.메서드명() 과 같이 호출
	
	// @Before("execution(* edu.kh.project..*Controller*.*(..))")
	// == @Before("PointcutBundle.controllerPointCut()") => 필요한 곳에서 이렇게 활용
	
	
	@Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerPointCut() {}
	
	@Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {}
	
	
}
