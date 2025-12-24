package edu.kh.project.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

	
	/** 컨트롤러 수행 전 log 출력(from which 클래스/메서드/ip)
	 * 
	 */
	@Before("PointcutBundle.controllerPointCut()")
	public void beforeController(JoinPoint jp) {
		
		// AOP 적용된 클래스 이름 얻어오기
		String className = jp.getTarget().getClass().getSimpleName(); 
				
		// 실행된 컨트롤러의 메서드명 얻어오기
		String methodName = jp.getSignature().getName();
		
		// -----------------------------------------------------------
		// 요청한 클라이언트의 ip log 출력
		
		// 요청한 클라이언트의 HttpServletRequest 객체 얻어오기
		HttpServletRequest req =
				((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 클라이언트의 ip 얻어오기
		String ip = getRemoteAddr(req);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("[%s.%s] 요청 / ip : %s", className, methodName, ip));
		
		// 로그인 상태인 경우 (추가적으로 append)
		if(req.getSession().getAttribute("loginMember") != null) {
			
			String memberEmail = ((Member)req.getSession().getAttribute("loginMember")).getMemberEmail();
								// req에서 session 객체 얻어오면 Object 타입이라 Member로 강제형변환
			
			sb.append(String.format(", 요청 회원 : %s", memberEmail));
			
		}
		
		log.info(sb.toString());
		
	}
	
	/* 
	  1) @Around 사용 시 반환형 반드시 Object
	  2) @Around 메서드 종료 시 proceed 반환값을 return 해야함
	 */
	
	/* 
	 * ProceedingJoinPoint
	  - JoinPoint 상속한 자식 객체
	  - @Around 에서 사용 가능
	  - proceed 메서드 제공 => 메서드 호출 전/후로 Before/After 구분
	 */
	
	/** 모든 서비스 수행 전/후로 동작하는 코드(클래스/메서드/parameter/실행 시간)
	 * @return
	 */
	@Around("PointcutBundle.serviceImplPointCut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable{
		
		/* cf) Throwable : 예외 처리의 최상위 클래스
		 	=> 주요 자식 - Exception (예외) : 개발자가 처리할 수 있는 문제
	 					   Error (오류) : 시스템 레벨의 심각한 문제
		 */
		
		
		// AOP 적용된 클래스 이름 얻어오기
		String className = pjp.getTarget().getClass().getSimpleName(); 
						
		// 실행된 컨트롤러의 메서드명 얻어오기
		String methodName = pjp.getSignature().getName();
		
		log.info("----------{}.{} 서비스 호출----------", className, methodName);
		
		// parameter 로그로 출력
		log.info("Parameter : {}", Arrays.toString(pjp.getArgs()));
		
		// 서비스 주요 비즈니스 로직 코드 실행 소요 시간 기록
		long startMs = System.currentTimeMillis();
		
		
		// 여기까지 Before 영역
		// -----------------------------------------------------------------------
		Object obj = pjp.proceed(); // 전/후를 나누는 기준점
		// -----------------------------------------------------------------------
		// 여기서부터 After 영역
		
		long endMs = System.currentTimeMillis();
		
		log.info("RunningTime : {}ms", endMs - startMs);
		
		log.info("========================================");
		
		return obj;
		
	}
	
	
	/** 접속자 IP 얻어오는 메서드
	 *
	 * @param request
	 * @return ip
	 */
	private String getRemoteAddr(HttpServletRequest request) {
		
		// 클라이언트(사용자)의 실제 IP 주소를 찾아내기 위한 로직
		String ip = null;
		
		ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
		
	}

	
	
	
}










