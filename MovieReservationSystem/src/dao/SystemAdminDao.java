package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class SystemAdminDao {
	
	private SystemAdminDao(){}
	private static SystemAdminDao instance;
	public static SystemAdminDao getInstance(){
		if(instance == null){
			instance = new SystemAdminDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	
	//관리자 관리
	public int insertAdmin(Map<String, Object> param){
		String sql = "INSERT INTO MEMBER (USID, PASSWORD, USER_NM, TELNO, AUTHOR)"
				+ " VALUES (?, ? ,?, ?, ?)";
		/*컬럼의 개수가 많아지면 오류가 생길 확률이 높아지므로 
		Map형태로 키-값 형태로 담은후 List로 변환*/
		List<Object> p = new ArrayList<>();
		p.add(param.get("USID"));
		p.add(param.get("PASSWORD"));
		p.add(param.get("USER_NM"));
		p.add(param.get("TELNO"));
		p.add(param.get("AUTHOR"));
		
		return jdbc.update(sql, p);
	}

	public Map<String, Object> selectUser(String userId, String password) {
		String sql = "SELECT USID, PASSWORD, USER_NM, TELNO"
				+ " FROM MEMBER"
				+ " WHERE USID = ?"
				+ " AND PASSWORD = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		
		return jdbc.selectOne(sql, param);
	}
	
	

	public List<Map<String,Object>> checkIdDao() {
		String sql = "SELECT USID FROM MEMBER";
		
		List<Map<String,Object>> id = jdbc.selectList(sql);
		
		return id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//리뷰
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public List<Map<String, Object>> adminReviewList(){
		String sql = "SELECT * FROM REVIEW";
		
		return jdbc.selectList(sql);
	}
	
	public Map<String, Object> adminSearchReviewOne(){
		String sql = "SELECT R.REVIEW_CODE, R.MOVIE_CODE, M.MOVIE_SJ, R.USID, R.AVRG_SCORE, R.RELEASE_DATE, R.REVIEW_CN FROM REVIEW R, MOVIE M WHERE R.MOVIE_CODE = M.MOVIE_CODE"
				+ " AND R.REVIEW_CODE = ? ORDER BY R.RELEASE_DATE DESC";
		
		List<Object> param = new ArrayList<>();
		System.out.print("작성 번호를 입력해주세요>"); int input = ScanUtil.nextInt(); param.add(input);
		return jdbc.selectOne(sql, param);
	}
	
	public int adminReviewDelete(){
		String sql = "DELETE FROM REVIEW WHERE REVIEW_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		System.out.print("삭제할 번호를 입력해주세요>"); int input = ScanUtil.nextInt(); param.add(input);
		return jdbc.update(sql, param);
	}
}
