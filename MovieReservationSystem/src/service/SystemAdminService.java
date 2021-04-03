package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.SystemAdminDao;

public class SystemAdminService {

	private SystemAdminService(){}
	private static SystemAdminService instance;
	public static SystemAdminService getInstance(){
		if(instance == null){
			instance = new SystemAdminService();
		}
		return instance;
	}
	
	ReviewService rs = ReviewService.getInstance();
	SystemAdminDao sad = SystemAdminDao.getInstance();
	
	//관리자 관리
//	
//	public int manageAdmin(){
//		System.out.println("1.관리자 추가\t2.관리자 수정\t3.관리자 검색\t4.관리자 삭제");
//		int input = ScanUtil.nextInt();
//		switch(input){
//		case 1: adminAdd();
//		case 2: adminModify();
//		case 3: adminSearch();
//		case 4: adminDelete();
//		}
//		return View.MANAGEADMIN;
//	}
//	
//	public int adminAdd(){ //리턴타입 이동할 화면
//		System.out.println("================관리자 추가================");
//		String adminId = adminIdCheck();
//		String password = joinPasswordCheck();
//		
//		/*System.out.print("비밀번호>");
//		String password = ScanUtill.nextLine();*/
//		
//		System.out.print("관리자 이름 > ");
//		String userName = ScanUtil.nextLine();
//		System.out.print("해당 영화관 전화번호 > ");
//		String telno = ScanUtil.nextLine();
//		String author = "YM";
//		
//		//아이디 중복 확인 및 유효성검사
//		//비밀번호 확인 및 유효성 검사
//		//정규 표현식(유효성 검사) 생략
//		
//		Map<String, Object> param = new HashMap<>();
//		param.put("USID", adminId);
//		param.put("PASSWORD", password);
//		param.put("USER_NM", userName);
//		param.put("AUTHOR", author);
//		param.put("TELNO", telno);
//		int result = sad.insertAdmin(param);
//		
//		if(result > 0){
//			System.out.println("새로운 관리자가 추가되었습니다.");
//		}else{
//			System.out.println("관리자 생성 실패");
//		}
//		return View.HOME;
//	}
//	
//	private int adminModify(){
//		return View.MANAGEADMIN;
//	}
//	
//	private int adminSearch(){
//		return View.MANAGEADMIN;
//	}
//	
//	private int adminDelete(){
//		return View.MANAGEADMIN;
//	}
//	
//	//아이디 중복 및 유효성 체크
//	public String adminIdCheck(){
//		String id = null;
//		while(true){
//			boolean overlap = true;
//			boolean effectiveness = false;
//			String regexId = "[a-z0-9-_]{5,20}";
//			
//			System.out.print("아이디>");
//			id = ScanUtil.nextLine();
//			List<Map<String,Object>> adminId = sad.checkIdDao();
//			
//			for(Map<String, Object> admin : adminId){
//				if(admin.get("USID").equals(id)){
//					overlap = false;
//				}
//			}
//			Pattern p_id = Pattern.compile(regexId);
//			Matcher m_id = p_id.matcher(id);
//			effectiveness = m_id.matches();
//			
//			if(!effectiveness){
//				System.out.println("아이디는 소문자,숫자 5~20글자 입니다");
//			}else if(!overlap){
//				System.out.println("이미 존재하는 아이디 입니다");
//			}else{
//				break;
//			}
//		}
//		return id;
//	}
//	
//	private String joinPasswordCheck() {
//		String password = null;
//		String password2 = null;
//		
//		while(true){
//			System.out.print("비밀번호>");
//			password = ScanUtil.nextLine();
//			System.out.print("비밀번호 확인>");
//			password2 = ScanUtil.nextLine();
//			
//			if(!password.equals(password2)){
//				System.out.println("입력하신 두 비밀번호가 일치하지 않습니다");
//				continue;
//			}else{
//				break;
//			}
//		}
//		
//		return password;
//	}
	
	
	//리뷰
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public int reviewList(){
		
		System.out.println("1.리뷰 게시판 목록\t2.리뷰작성\t3.영화 코드로 해당 영화 리뷰 조회하기\t4.작성자 이름으로 리뷰 조회하기\t5.내가 올린 리뷰글 보기\t6.영화 총 평점 보기\t7.뒤로 가기\t8.관리자 전용 리뷰 페이지");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1: rs.reviewBoard(); return View.REVIEWADMIN;//reviewSearchOne(); return updateAndDelete();
		case 2: rs.reviewInsert(); return View.REVIEWADMIN;
		case 3: rs.movieSelectView(); return View.REVIEWADMIN;
		case 4: rs.reviewSelectOne(); return View.REVIEWADMIN; //return View.REVIEWSELECTWRITER;
		case 5: rs.selectMyReview(); return View.REVIEWADMIN;
		case 6: rs.reviewAvrgScore(); return View.REVIEWADMIN;
		case 7: return View.MAIN_HOME;
		case 8: return adminReviewPage();
		}
		return View.HOME;
		
	}
	
	private int adminReviewPage(){
		
		System.out.println("1.부적절한 게시글 삭제\t2.뒤로 가기");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1: return adminReviewDeleteChoice();
		case 2: return View.REVIEWADMIN;
		}
		return View.REVIEWADMIN;
	}
	
	private int adminReviewDeleteChoice(){
		List<Map<String, Object>> reviewList = sad.adminReviewList();
		System.out.println("|글번호|\t|작성자|\t|리뷰제목|\t|작성일|");
		for(Map<String, Object> review : reviewList){
			System.out.println("___________________________________________________________________________________________");
			System.out.println(review.get("REVIEW_CODE") + "\t" + review.get("USID") + "\t" + review.get("REVIEW_SJ") + "\t" + review.get("RELEASE_DATE"));
			System.out.println("___________________________________________________________________________________________");
			}
		System.out.println("1.삭제할 게시글 선택하기\t2.뒤로 가기");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
			switch(input){
					case 1: adminReviewSelectOne(); return adminReviewDelete();
					case 2: return View.REVIEWADMIN;
		}
		return View.REVIEWADMIN;
	}
	
	private int adminReviewSelectOne(){
		Map<String, Object> review = sad.adminSearchReviewOne();
		System.out.println("========================================================================================================================");
		System.out.println("작성 번호 : " + review.get("REVIEW_CODE"));
		System.out.println("영화 제목 : " + review.get("MOVIE_SJ"));
		System.out.println("작성자 : " + review.get("USID"));
		System.out.println("평점 : " + review.get("AVRG_SCORE"));
		System.out.println("날짜 : " + review.get("RELEASE_DATE"));
		System.out.println("========================================================================================================================");
		System.out.println("리뷰내용 : " + review.get("REVIEW_CN"));
		System.out.println("========================================================================================================================");
		return View.REVIEWADMIN;
	}
	
	private int adminReviewDelete(){
		System.out.println("정말 삭제하시겠습니까?(Y/N)");
		System.out.print("입력 > ");
		Object input = ScanUtil.nextLine();
		if(input.equals("y") || input.equals("Y")){
			int review = sad.adminReviewDelete();
			System.out.println("정상적으로 삭제되었습니다.");
			return View.REVIEWADMIN;
		}
		else if(input.equals("n") || input.equals("N")){
			System.out.println("취소되었습니다.");
			return View.REVIEWADMIN;
		}
		else{
			System.out.println("다시 입력해주세요.");
		}
		return View.REVIEWADMIN;
	}
}