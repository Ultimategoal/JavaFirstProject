package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MyPageDao;
import dao.UserDao;

public class UserService {
	
	//싱글톤 패턴
	private UserService(){}
	private static UserService instance;
	
	public static UserService getInstance(){
		if(instance == null){
			instance = new UserService();
		}
		return instance;
	}
	
	private UserDao userDao = UserDao.getInstance();
	private MyPageService myPage = MyPageService.getInstance();
	private ReviewService rs = ReviewService.getInstance();
	private MyPageDao myPageDao = MyPageDao.getInstance();
	
	public int join(){ //리턴타입 이동할 화면
		System.out.println("💙💙💙💙💙💙회원가입💙💙💙💙💙💙");
		String userId = joinIdCheck();
		String password = joinPasswordCheck();
		
		System.out.print("💙이름 > ");
		String userName = ScanUtil.nextLine();
		String telNo = joinTelNoCheck();
		
			
		String author = "N";
		
		//아이디 중복 확인 및 유효성검사
		//비밀번호 확인 및 유효성 검사
		//정규 표현식(유효성 검사) 생략
		
		Map<String, Object> param = new HashMap<>();
		param.put("USID", userId);
		param.put("PASSWORD", password);
		param.put("USER_NM", userName);
		param.put("AUTHOR", author);
		param.put("TELNO", telNo);
		int result = userDao.insertUser(param);
		
		if(result > 0){
			System.out.println("✪ ω ✪ 💘 회원가입 성공 💘 ✪ ω ✪");
		}else{
			System.out.println("💢회원가입 실패");
		}
		return View.HOME;
	}

	private String joinTelNoCheck() {
		String telNo = null;
		re: while(true){
			
			System.out.print("💙전화 번호 > ex)010-9999-9999(-포함)");
			telNo = ScanUtil.nextLine();
		    String telNoRegex = "01[0-9]-[0-9]{3,4}-[0-9]{4}";
		         Pattern p = Pattern.compile(telNoRegex);
		         Matcher m = p.matcher(telNo);
		          if(!m.matches()){
		             System.out.println("❌ 형식에 맞지 않습니다. 다시 입력해주세요");
		             continue re;}
		          else	break;
		}
		return telNo;
	}

	//비밀번호 확인
	private String joinPasswordCheck() {
		String password = null;
		String password2 = null;
		
		while(true){
			System.out.print("💙비밀번호 > ");
			password = ScanUtil.nextLine();
			System.out.print("💙비밀번호 확인 > ");
			password2 = ScanUtil.nextLine();
			
			if(!password.equals(password2)){
				System.out.println("❌입력하신 두 비밀번호가 일치하지 않습니다❌");
				continue;
			}else{
				break;
			}
		}
		return password;
	}

	
	public int login() {
		System.out.println("⏩⏩⏩⏩⏩⏩로그인 ⏪⏪⏪⏪⏪⏪");
		System.out.print("▶ 아이디 >");
		String userId = ScanUtil.nextLine();
		System.out.print("▶ 비밀번호>");
		String password = ScanUtil.nextLine();
		Map<String, Object> user = userDao.selectUser(userId, password);
		
		if(user == null){
			System.out.println("❌아이디 혹은 비밀번호를 잘못 입력하셨습니다❌");
		}else{
			System.out.println("o((>ω< ))o ❣ 로그인 성공 ❣ o((>ω< ))o");
			Controller.LoginUser = user;
			String AUTHOR = (String)user.get("AUTHOR");
			if(AUTHOR.equals("YM")){
				return View.MOVIEADMIN;
			}else{
			return View.MAIN_HOME;

			}
		}
		return View.LOGIN;
	}
	
	//아이디 중복 및 유효성 체크
	public String joinIdCheck(){
		String id = null;
		while(true){
			boolean overlap = true;
			boolean effectiveness = false;
			String regexId = "[a-z0-9-_]{5,20}";
			
			System.out.print("💙아이디>");
			id = ScanUtil.nextLine();
			List<Map<String,Object>> usersId = userDao.checkIdDao();
			
			for(Map<String, Object> users : usersId){
				if(users.get("USID").equals(id)){
					overlap = false;
				}
			}
			Pattern p_id = Pattern.compile(regexId);
			Matcher m_id = p_id.matcher(id);
			effectiveness = m_id.matches();
			
			if(!effectiveness){
				System.out.println("❌아이디는 소문자,숫자 5~20글자 입니다❌");
			}else if(!overlap){
				System.out.println("❌이미 존재하는 아이디 입니다❌");
			}else{
				break;
			}
		}
		return id;
	}


	// 메인 홈
	public int mainHome() {
		System.out.println("====================== DAEDEOK MOVIE ======================");
		System.out.println("1.예매하기          2.마이페이지          3.게시판보기          4.푸드코트          5.로그아웃");
		System.out.println("===========================================================");
		System.out.print("선택 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1 : // 예매하기
			return View.RESERVATION;
			
		case 2 : // 마이페이지
			return View.MYPAGE;
			
		case 3: // 게시판 보기
			if(Controller.LoginUser.get("AUTHOR").equals("ys")||Controller.LoginUser.get("AUTHOR").equals("YS")){
				return View.REVIEWADMIN;
			}else{
				return View.REVIEW;
			}
//			return rs.reviewList();
			
		case 4 : // 푸트코트
			return View.FOOD;
			
		case 5 : // 로그아웃
			Controller.LoginUser = null;
			return View.HOME;
			
		default :
			break;	
		}
		return View.MYPAGE;
	}
	
	// 마이페이지
	public int myPage() {
		System.out.println("================================== 마이페이지 ===================================");
		System.out.println("1.내 정보 보기          2.예매내역          3.캐쉬충전          4.상품구매내역          5.내가 쓴 글 보기          0.뒤로가기");
		System.out.println("=============================================================================");
		System.out.print("입력 > ");
		int num = ScanUtil.nextInt();
		switch(num) {
		case 1 : // 내 정보 보기
			return myPage.updateMyPage();
			
		case 2 : // 예매 내역
			myPage.reservationSelect();
			return View.MYPAGE;
			
		case 3 : //캐쉬충전
			myPage.cashCharger();
			return View.MYPAGE;
			
		case 4 : // 상품구매내역
			myPage.cartList();
			return View.MYPAGE;
			
		case 5 : // 내가 쓴 글 보기
			return View.MYREVIEW;
			
		case 0 :
			return View.MAIN_HOME;
		}
		return View.MAIN_HOME;
	}
	
	public int reservationSelect() {
		return View.MYPAGE;
	}
	
//	private void cashCharger() {
//		System.out.print("충전금액>");
//		int money = ScanUtil.nextInt();
//		
//		int result = myPageDao.cashChargerDao(money);
//		
//		if(result > 0){
//			System.out.println("충전이 완료 되었습니다");
//		}else{
//			System.out.println("충전에 실패하였습니다");
//		}
//	}
	
}

