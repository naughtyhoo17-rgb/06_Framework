package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.board.model.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* 
 * Interceptor : 요청/응답/뷰 완성 후 가로채는 객체(Spring 지원)
  
 * HandlerInterceptor 인터페이스 상속받아 구현
  - preHandle(전처리) : DispatcherServlet -> Controller 사이에 수행
  - postHandle(후처리) : Controller -> DispatcherServlet 사이에 수행
  - afterCoompletion(뷰 완성 후) : ViewResolver -> DispatcherServler 사이에 수행
 *
 * */
@Slf4j
public class BoardTypeInterceptor implements HandlerInterceptor{
	
	@Autowired // 의존성 주입 => BoardService타입이거나 상속관계인 객체(Bean) 주입
	private BoardService service;
	
	// 전처리 : 요청이 Controller로 들어오기 전 실행되는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// boardType을 DB에서 boardTypeList 형태로 얻어오기 
		// application scope : 서버 시작 ~ 종료 시까지 유지되는 Servlet 내장 객체
		// 서버 내에 딱 한개만 존재하는 객체! => 모든 클라이언트가 공용으로 사용
		
		// application scope 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// applicatio scope에 "boardTypeList"가 없을 경우
		if(application.getAttribute("boardTypeList") == null) {
			
			// boardTypeList 조회 서비스 호출
			List<Map<String, Object>> boardTypeList = service.selectBoardTypeList();
			
			log.debug("boardTypeList : " + boardTypeList);
			
			// 조회 결과를 application scope에 추가
			application.setAttribute("boardTypeList", boardTypeList);
			
		}
			
		return HandlerInterceptor.super.preHandle(request, response, handler);
	
	}
	
	// 후처리 : 요청이 실행된 후, 뷰가 렌더링 되기 전 실행되는 메서드
	// => 응답을 가지고 DispatcherServlet으로 돌아가기 전
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	
	}
	
	// 뷰 완성 후 : 뷰 렌더링이 끝난 후 실행되는 메서드
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	
	}

	
	
	
	
	
}







