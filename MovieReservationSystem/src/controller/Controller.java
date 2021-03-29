package controller;

import java.util.Map;


import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		
		
		new Controller().start();
		
	}
	
	public static Map<String, Object> LoginUser;


	private UserService userService = UserService.getInstance();
	
	
	
	//return 받은 값을 이용하여 화면이동
	private void start() {
		int view = View.HOME;
		
		while(true){
			switch(view){
			case View.HOME : view = home(); break;
			
			case View.LOGIN : view = userService.login(); break;
			
			case View.JOIN : view = userService.join(); break;
			
			
			}
		
		}
		
		
	}

	private int home() {
		System.out.println("-----------------------------");
		System.out.println("1.로그인\t2.회원가입\t0.프로그램 종료");
		System.out.println("-----------------------------");
		System.out.print("번호 입력>");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1 : //로그인
			return View.LOGIN;
		case 2 : //회원가입
			return View.JOIN;
		case 0 : //프로그램 종료
			System.out.println("프로그램이 종료되었습니다");
			System.exit(0);
		}
		return View.HOME; // 1, 2, 0 이외에 다른 숫자 입력시 다시 돌아오게 설정
	}

	
	
}





















