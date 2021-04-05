package dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.ParseConversionEvent;

import util.JDBCUtil;
import util.ScanUtil;
import controller.Controller;

public class ReviewDao {

	private ReviewDao(){}
	private static ReviewDao instance;
	public static ReviewDao getInstance(){
		if(instance == null){
			instance = new ReviewDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm");
	
	int deleteInput;
	
	
	public List<Map<String, Object>> selectReview(){
		String sql = "SELECT * FROM REVIEW ORDER BY RELEASE_DATE DESC";
		
		//영화 제목도 같이 보고 싶으면 아래 구문 사용
//		String sql = "SELECT R.REVIEW_CODE, R.MOVIE_CODE, M.MOVIE_SJ, R.USID, R.AVRG_SCORE, R.RELEASE_DATE, R.REVIEW_SJ, R.REVIEW_CN FROM REVIEW R, MOVIE M WHERE R.MOVIE_CODE = M.MOVIE_CODE ORDER BY R.RELEASE_DATE DESC";
		
		return jdbc.selectList(sql);
	}
	
	//글 등록
	public int insertReview(){
		String sql = "INSERT INTO REVIEW VALUES((SELECT TRIM(NVL(MAX(TO_CHAR(REVIEW_CODE, '999')),0)) + 1 FROM REVIEW), ?, ?, ?, SYSDATE, ?, ?)";
		
//		String sql = "SELECT R.REVIEW_CODE, R.MOVIE_CODE, M.MOVIE_SJ, R.USID, R.AVRG_SCORE, R.RELEASE_DATE, R.REVIEW_SJ, R.REVIEW_CN FROM REVIEW R, MOVIE M WHERE R.MOVIE_CODE = M.MOVIE_CODE AND R.REVIEW_CODE = ? ORDER BY R.RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		System.out.print("🎬영화 코드를 작성해주세요>"); Object input = ScanUtil.nextLine();  param.add(input);
		System.out.print("🎬리뷰 제목을 입력해주세요>"); input = ScanUtil.nextLine(); 		  param.add(input);
		System.out.print("🎬평점을 입력해주세요>");   boolean flag = true; while(flag){
																			input = ScanUtil.nextLine();
																			if(Integer.parseInt((String) input)<0||Integer.parseInt((String) input)>10){
																				System.out.println("0부터 10까지 입력가능합니다. 다시 입력해주세요.");
																			}
																			else{
																				flag = false;
																			}
																	} 
																	param.add(input);
		System.out.print("🎬내용을 입력해주세요>");     input = ScanUtil.nextLine(); 		  param.add(input);
		param.add(Controller.LoginUser.get("USID"));
		
		return jdbc.update(sql, param);
	}
	//리뷰 글보기
	public Map<String, Object> selectSearchReviewOne(){
//		String sql = "SELECT REVIEW_CODE, USID, AVRG_SCORE, RELEASE_DATE, REVIEW_CN FROM REVIEW WHERE REVIEW_CODE = ? ORDER BY RELEASE_DATE DESC";
		
		String sql = "SELECT R.REVIEW_CODE, R.MOVIE_CODE, M.MOVIE_SJ, R.USID, R.AVRG_SCORE, R.RELEASE_DATE, R.REVIEW_CN FROM REVIEW R, MOVIE M WHERE R.MOVIE_CODE = M.MOVIE_CODE"
				+ " AND R.REVIEW_CODE = ? ORDER BY R.RELEASE_DATE DESC";
		
//		String sql = "SELECT C.COMMENT_CN, R.REVIEW_CODE, R.REVIEW_CODE, R.MOVIE_CODE, M.MOVIE_SJ, R.USID, R.AVRG_SCORE,"
//				+ " R.RELEASE_DATE, R.REVIEW_CN FROM COMMENTREVIEW C INNER JOIN REVIEW R ON(C.REVIEW_CODE = R.REVIEW_CODE)"
//				+ " INNER JOIN MOVIE M ON(R.MOVIE_CODE = M.MOVIE_CODE) WHERE R.REVIEW_CODE = ? ORDER BY R.RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		System.out.print("🎬작성 번호를 입력해주세요>"); int input = ScanUtil.nextInt(); param.add(input);
		return jdbc.selectOne(sql, param);
	}
	
	//아이디로 글 보기(하나만)
	public List<Map<String, Object>> selectReviewOne(){
		String sql = "SELECT REVIEW_CODE, REVIEW_SJ, REVIEW_CN, AVRG_SCORE, RELEASE_DATE, USID FROM REVIEW WHERE USID = ? ORDER BY RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		System.out.print("🎬작성자 이름을 입력해주세요>"); Object input = ScanUtil.nextLine(); param.add(input);
		return jdbc.selectList(sql, param);
	}
	
	//리뷰 글 수정
	public int updateReview(){
		String sql = "UPDATE REVIEW SET REVIEW_SJ = ?, REVIEW_CN = ? WHERE REVIEW_CODE = ? AND USID = ?";
		
		List<Object> param = new ArrayList<>();
		System.out.print("🎬수정하고 싶은 본인 게시글 번호를 입력해주세요>"); Object  input1 = ScanUtil.nextLine();
		System.out.print("🎬수정할 제목을 입력해주세요>"); 		 Object input2 = ScanUtil.nextLine();
		System.out.print("🎬수정할 내용을 입력해주세요>"); 		 Object input3 = ScanUtil.nextLine();
		param.add(input2); param.add(input3); param.add(input1);
		param.add(Controller.LoginUser.get("USID"));
		
		
		return jdbc.update(sql, param);
	}
	
	//리뷰 글 삭제
	public int deleteReview(){
		String sql = "DELETE FROM REVIEW WHERE USID = ? AND REVIEW_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("USID"));
		System.out.print("🎬작성 번호를 한 번 더 입력해주세요>"); deleteInput = ScanUtil.nextInt(); param.add(deleteInput);
		return jdbc.update(sql, param);
	}
	
	//내가 올린 리뷰글 보기
	public List<Map<String, Object>> selectMyReview(){
		String sql = "SELECT REVIEW_CODE, MOVIE_CODE, REVIEW_SJ, REVIEW_CN, AVRG_SCORE, RELEASE_DATE, USID FROM REVIEW WHERE USID = ? ORDER BY RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("USID"));
		return jdbc.selectList(sql, param);
	}
	
	//영화 코드 보기
	public List<Map<String, Object>> movieChoice(){
		String sql = "SELECT DISTINCT R.MOVIE_CODE,  M.MOVIE_SJ FROM REVIEW R, MOVIE M WHERE R.MOVIE_CODE = M.MOVIE_CODE ORDER BY R.MOVIE_CODE";
		
		return jdbc.selectList(sql);
	}
	
	//영화 코드로 해당 영화 리뷰보기
	public List<Map<String, Object>> movieSelect(){
		String sql = "SELECT REVIEW_CODE, USID, AVRG_SCORE, RELEASE_DATE, REVIEW_SJ FROM REVIEW WHERE MOVIE_CODE = ? ORDER BY RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		System.out.print("🎬해당하는 영화의 코드를 작성해주세요>"); Object input = ScanUtil.nextLine(); param.add(input);
		
		return jdbc.selectList(sql, param);
	}
	
	//삭제할 때 아이디 매칭
	public Map<String, Object> deleteUser(){
		String sql = "SELECT USID FROM REVIEW WHERE REVIEW_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(deleteInput);
		
		return jdbc.selectOne(sql, param);
	}
	
//	public int commentReview(){
//		String sql = "INSERT INTO COMMENTREVIEW VALUES((SELECT TRIM(NVL(MAX(TO_CHAR(COMMENT_CODE, '999')),0)) + 1 FROM COMMENTREVIEW), ?, ?)";
//		
//		List<Object> param = new ArrayList<>();
//		System.out.print("게시글 번호를 다시 입력해주세요"); Object input = ScanUtil.nextLine(); param.add(input);
//		System.out.print("작성할 댓글을 입력해주세요"); input = ScanUtil.nextLine(); param.add(input);
//		
//		return jdbc.update(sql, param);
//	}
	
	//영화 평균 평점
	public Map<String, Object> avrgScoreOne(){
		String sql = "SELECT DISTINCT SUMM/AVRG FROM REVIEW, (SELECT SUM(AVRG_SCORE) SUMM FROM REVIEW WHERE MOVIE_CODE = ?),"
				+ " (SELECT COUNT(AVRG_SCORE) AVRG FROM REVIEW WHERE MOVIE_CODE = ?)";
		
		List<Object> param = new ArrayList<>();
		System.out.println("🎬영화를 선택하면 평점이 보입니다.");
		Object input = ScanUtil.nextLine();
		param.add(input);
		param.add(input);
		return jdbc.selectOne(sql, param);
		
	}
	
	//전체 영화 평균 평점
	public List<Map<String, Object>> avrgScore(){
		String sql = "SELECT R.MOVIE_CODE, M.MOVIE_SJ, TO_NUMBER(RTRIM(TO_CHAR(SUM(R.AVRG_SCORE)/COUNT(R.AVRG_SCORE),'FM90D00'),'.')) TOTAL FROM REVIEW R INNER JOIN MOVIE M ON(R.MOVIE_CODE = M.MOVIE_CODE) GROUP BY R.MOVIE_CODE, M.MOVIE_SJ ORDER BY TOTAL DESC";
		
		return jdbc.selectList(sql);
	}
	
	
	
	//조인해서 영화이름 가져오기
}