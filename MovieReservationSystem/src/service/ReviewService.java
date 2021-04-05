package service;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;
import dao.ReviewDao;

public class ReviewService {

	private ReviewService(){}
	private static ReviewService instance;
	public static ReviewService getInstance(){
		if(instance == null){
			instance = new ReviewService();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	ReviewDao rd = ReviewDao.getInstance();
	
	SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm");
	
	public int reviewBoard(){
		List<Map<String, Object>> reviewList = rd.selectReview();
		System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		System.out.println("★=========================================================리뷰 게시판=======================================================★");
		System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		System.out.println("|글 번호|\t|영화코드|\t|평점|\t|작성자|\t|작성일|\t\t\t|리뷰제목|");
		for(Map<String, Object> review : reviewList){
			System.out.println("____________________________________________________________________________________________________________________________________________________________________________________");
			System.out.print(review.get("REVIEW_CODE") + "\t " + review.get("MOVIE_CODE") + "\t\t " + review.get("AVRG_SCORE") + "\t" 
		 +  review.get("USID") + "\t" + review.get("RELEASE_DATE") + "\t" + review.get("REVIEW_SJ"));
			System.out.println();
			System.out.println("____________________________________________________________________________________________________________________________________________________________________________________");
		}
		System.out.println("=============================");
		System.out.println("1.게시글 번호로 선택      2.뒤로 가기");
		System.out.println("=============================");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1: reviewSearchOne(); return updateAndDelete();
		case 2: return View.REVIEW;
		}
		return View.REVIEW;
	}
	
	public int reviewList(){
//		List<Map<String, Object>> reviewList = rd.selectReview();
//		System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
//		System.out.println("★=========================================================리뷰 게시판=======================================================★");
//		System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
//		System.out.println("글 번호\t영화코드\t평점\t작성자\t작성일\t\t\t리뷰제목");
//		for(Map<String, Object> review : reviewList){
//			System.out.print(review.get("REVIEW_CODE") + "\t" + review.get("MOVIE_CODE") + "\t" + review.get("AVRG_SCORE") + "\t" +  review.get("USID") + "\t" + review.get("RELEASE_DATE") + "\t" + review.get("REVIEW_SJ"));
//			System.out.println();
//		}
		System.out.println("=============================================================================================================");
		System.out.println("1.리뷰 게시판 목록     2.리뷰작성     3.영화 코드로 리뷰 조회하기     4.작성자 이름으로 리뷰 조회하기     5. My리뷰글 보기     6.영화 총 평점 보기     7.뒤로 가기");
		System.out.println("=============================================================================================================");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1: reviewBoard(); return View.REVIEW;//reviewSearchOne(); return updateAndDelete();
		case 2: reviewInsert(); return View.REVIEW;
		case 3: movieSelectView(); return View.REVIEW;
		case 4: reviewSelectOne(); return View.REVIEW; //return View.REVIEWSELECTWRITER;
		case 5: selectMyReview(); return View.REVIEW;
		case 6: return reviewAvrgScore();//return View.MAIN;
		case 7: return View.MAIN_HOME;
		}
		return View.HOME;
		
	}
	
	public int reviewInsert(){//리뷰 작성
		int review = rd.insertReview();
		System.out.println("👍정상적으로 업로드 되었습니다👍");
		return View.REVIEW;
	}
	
	public int reviewSearchOne(){//게시글 번호로 리뷰글 보기
		Map<String, Object> review = rd.selectSearchReviewOne();
//		System.out.println("작성번호\t작성자\t평점\t날짜\t\t\t리뷰내용");
//		System.out.println(review.get("REVIEW_CODE") + "\t" + review.get("USID") + "\t" + review.get("AVRG_SCORE") + "\t" + review.get("RELEASE_DATE") + "\t" + review.get("REVIEW_CN"));
		System.out.println("========================================================================================================================");
		System.out.println("작성 번호 : " + review.get("REVIEW_CODE"));
		System.out.println("영화 제목 : " + review.get("MOVIE_SJ"));
		System.out.println("작성자 : " + review.get("USID"));
		System.out.println("평점 : " + review.get("AVRG_SCORE"));
		System.out.println("날짜 : " + review.get("RELEASE_DATE"));
		System.out.println("========================================================================================================================");
		System.out.println("리뷰내용 : " + review.get("REVIEW_CN"));
		System.out.println("========================================================================================================================");
		return View.REVIEW;
	}
	
	public int movieSelectView(){//영화 코드 조회
		System.out.println("============================");
		System.out.println("1.바로 조회하기     2.영화 코드보기");
		System.out.println("============================");
//		List<Map<String, Object>> reviewList = rd.selectReview();
		List<Map<String, Object>> reviewList = rd.movieChoice();
		while(true){
			System.out.print("입력 > ");
			int input = ScanUtil.nextInt();
			switch(input){
			case 1: movieSelect(); return View.REVIEW;
			case 2: for(Map<String, Object> movieSelect : reviewList){
					System.out.println(movieSelect.get("MOVIE_CODE")+ " : " + movieSelect.get("MOVIE_SJ"));
					}
					return movieSelect();
			}
		}
		
	}
	
	public int movieSelect(){//영화코드로 조회
		
		List<Map<String, Object>> review = rd.movieSelect();
		System.out.println("|글번호|\t|작성자|\t|평점|\t|작성일|\t\t\t|리뷰제목|");
		for(Map<String, Object> movieReview : review){
			System.out.println("________________________________________________________________________");
			System.out.println(movieReview.get("REVIEW_CODE") + "\t " + movieReview.get("USID") + "\t " + movieReview.get("AVRG_SCORE") + "\t" + movieReview.get("RELEASE_DATE") + "\t " + movieReview.get("REVIEW_SJ"));
			System.out.println("________________________________________________________________________");
		}
		return View.REVIEW;
	}
	
	public int reviewSelectOne(){//작성자 이름으로 리뷰 조회하기
		List<Map<String, Object>> review = rd.selectReviewOne();
		System.out.println("|글번호|\t|작성일|\t\t\t|평점|\t|리뷰제목|");
		for(Map<String, Object> view : review){
			System.out.println("________________________________________________________________________");
			System.out.println(view.get("REVIEW_CODE") + "\t|" + view.get("RELEASE_DATE") + "\t|" + view.get("AVRG_SCORE") +"  |" + "\t|" + view.get("REVIEW_SJ"));
			System.out.println("________________________________________________________________________");
		}
		return View.REVIEW;
	}
	
	public int selectMyReview(){//내가 올린 리뷰글 보기
		List<Map<String, Object>> review = rd.selectMyReview();
		System.out.println("|글번호|\t|작성일|\t\t\t|평점|\t|리뷰제목|");
		for(Map<String, Object> myReview : review){
			System.out.println("________________________________________________________________________");
			System.out.println(myReview.get("REVIEW_CODE") + "\t" + myReview.get("RELEASE_DATE") + "\t" + myReview.get("AVRG_SCORE") + "\t" + myReview.get("REVIEW_SJ"));
			System.out.println("________________________________________________________________________");
		}
		return updateAndDelete();
	}
	
	private int updateAndDelete(){//리뷰 수정 또는 삭제
//			if(Controller.LoginUser.get("USID").equals(rd.selectSearchReviewOne().get("USID"))){
//				return View.MODIFY1;
		System.out.println("=====================================");
		System.out.println("1.수정     2.삭제      0.메인화면으로 돌아가기");
		System.out.println("=====================================");
		System.out.println("입력 > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1: reviewUpdate(); return View.REVIEW;
		case 2: reviewDelete(); return View.REVIEW;
		case 0: return View.MAIN_HOME;
		}
		return View.HOME;
	}
	
	public int reviewUpdate(){//리뷰 수정
		int review = rd.updateReview();
		System.out.println("👍 업로드 되었습니다 👍");
		return View.REVIEW;
	}
	
	public int reviewDelete(){//리뷰 삭제
		System.out.println("❓ 정말 삭제하시겠습니까 ❓(Y/N)");
		System.out.print("입력 > ");
		String input = ScanUtil.nextLine();
		if(input.equals("y") || input.equals("Y")){
			if(Controller.LoginUser.get("USID").equals(rd.selectSearchReviewOne().get("USID"))){
				int view = rd.deleteReview();
				System.out.println("정상적으로 삭제되었습니다💯");
			}
			else{
				System.out.println("🚨작성자 본인이 아닙니다.확인 후 다시 시도해주시기 바랍니다🚨");
			}
		}
		else if(input.equals("n") || input.equals("N")){
			System.out.println("❌취소되었습니다❌");
		}
		else{
			return View.REVIEW;
		}
		return View.REVIEW;
	}
	
	public int reviewAvrgScore(){
		List<Map<String, Object>> review = rd.avrgScore();
		System.out.println("영화평점\t\t영화제목");
		for(Map<String, Object> score : review){
			if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>0 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=0.5){
				System.out.print("⬜︎⬜︎⬜︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>0.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=1.5){
				System.out.print("◧⬜︎⬜︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>1.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=2.5){
				System.out.print("⬛︎⬜︎⬜︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>2.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=3.5){
				System.out.print("⬛︎◧⬜︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>3.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=4.5){
				System.out.print("⬛︎⬛︎⬜︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>4.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=5.5){
				System.out.print("⬛︎⬛︎◧⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>5.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=6.5){
				System.out.print("⬛︎⬛︎⬛︎⬜︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>6.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=7.5){
				System.out.print("⬛︎⬛︎⬛︎◧⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>7.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=8.5){
				System.out.print("⬛︎⬛︎⬛︎⬛︎⬜︎\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>8.5 && Double.parseDouble((String.valueOf(score.get("TOTAL"))))<=9.5){
				System.out.print("⬛︎⬛︎⬛︎⬛︎◧\t");
			}
			else if(Double.parseDouble((String.valueOf(score.get("TOTAL"))))>9.5){
				System.out.print("⬛︎⬛︎⬛︎⬛︎⬛︎\t");
			}
			System.out.print("(" + score.get("TOTAL") + ")" + "\t" + score.get("MOVIE_SJ"));
			System.out.println();
		}
		return View.REVIEW;
	}
}

