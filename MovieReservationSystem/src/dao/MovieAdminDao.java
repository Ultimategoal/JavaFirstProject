package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MovieAdminDao {
	
	private MovieAdminDao(){}
	private static MovieAdminDao instance;
	
	public static MovieAdminDao getInstance(){
		if(instance == null){
			instance = new MovieAdminDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	List<Object> param;
	
	
	public List<Map<String, Object>> showMovie() {
		String sql = "SELECT MOVIE_CODE 영화코드, CAST(MOVIE_SJ AS CHAR(50)) 영화제목, CAST(RELEASE_DATE AS CHAR(10)) 개봉일자,"
				+ " CAST(VIEWING_GRADE AS CHAR(20)) 관람등급, MNG_NAME 감독명"
				+ " FROM MOVIE";
		
		
		return jdbc.selectList(sql);
	}

	
	
	public int movieInsertDao(List<String> insertMovieInfo) {
		String sql = "INSERT INTO MOVIE VALUES ("
				+ " (SELECT  SUBSTR(MAX(MOVIE_CODE),1,2) || (SUBSTR(MAX(MOVIE_CODE),3) + 1) FROM MOVIE),"
				+ "?, ?, ?,"
				+ "( SELECT SUBSTR(REVIEW_CODE,1,5) || (MAX(TO_CHAR(SUBSTR(REVIEW_CODE,6),'999')) + 1) "
				+ "FROM MOVIE GROUP BY SUBSTR(REVIEW_CODE,1,5)), "
				+ "?)";
		
		param = new ArrayList<>();
		for(int i = 0; i < insertMovieInfo.size(); i++){
			param.add(insertMovieInfo.get(i));
		}
		
		return jdbc.update(sql, param);
	}



	public int movieDeleteDao(String movieCode) {
		String sql = "DELETE FROM MOVIE WHERE MOVIE_CODE = '" + movieCode +"'";
		return jdbc.update(sql);
	}



	public List<Map<String, Object>> selectCity() {

		String sql = "SELECT DISTINCT SUBSTR(ADRES,1,2) 지역 FROM CINEMA";
		
		return jdbc.selectList(sql);
	}



	public List<Map<String, Object>> selectCinema(String city) {
		String sql = "SELECT CINEMA_NM,CINEMA_CODE FROM CINEMA"
				+ " WHERE SUBSTR(ADRES,1,2) = ?"
				+ " ORDER BY 1";
		
		param = new ArrayList<>();
		param.add(city);
		return jdbc.selectList(sql, param);
	}



	public List<Map<String, Object>> showScreen(String cinemaCode) {
		String sql = "SELECT A.SCRINNG_CODE 상영코드, CAST(B.MOVIE_SJ AS CHAR(50)) 영화제목, A.SCRINNG_VWPOINT 상영시간,"
				+ " A.SCRINNG_DATE 상영날짜,"
				+ " SUBSTR(A.THEAT_CODE,2) 상영관  "
				+ " FROM SCRINNG A INNER JOIN MOVIE B ON (A.MOVIE_CODE = B.MOVIE_CODE) "
				+ " INNER JOIN CINEMA C ON (A.CINEMA_CODE = C.CINEMA_CODE) "
				+ " WHERE A.CINEMA_CODE ='" + cinemaCode +"'"
				+ " ORDER BY 5";
		
		
		return jdbc.selectList(sql);
	}



	public Map<String, Object> showCinema(String cinemaCode) {
		String sql = "SELECT CINEMA_NM FROM CINEMA WHERE CINEMA_CODE = '" + cinemaCode +"'";
		return jdbc.selectOne(sql);
	}



	public List<Map<String, Object>> showMovieCode() {
		String sql = "SELECT MOVIE_CODE 영화코드, MOVIE_SJ 영화제목 FROM MOVIE";
		return jdbc.selectList(sql);
	}



	public int screenInsert(List<Object> screen) {
		String sql = "INSERT INTO SCRINNG VALUES ("
				+ "(SELECT MAX(TO_CHAR(SCRINNG_CODE,'999')) + 1  FROM SCRINNG),"
				+ "?,?,?,?,?)";
		param = new ArrayList<>();
		for(int i = 0; i < screen.size(); i++){
			param.add(screen.get(i));
		}
		return jdbc.update(sql, screen);
	}



	public List<Map<String, Object>> showTheat(String cinemaCode) {
		String sql = "SELECT THEAT_NM FROM THEAT WHERE CINEMA_CODE = '" + cinemaCode + "'";
		return jdbc.selectList(sql);
	}



	public int screenDeleteDao(String screenCode) {
		String sql = "DELETE FROM SCRINNG WHERE SCRINNG_CODE = '" + screenCode + "'";
		
		return jdbc.update(sql);
	}



	public List<Map<String, Object>> getAllScreenDao() {
		String sql = "SELECT SCRINNG_CODE 상영코드, SCRINNG_DATE 상영날짜, SCRINNG_VWPOINT 상영시간"
				+ " FROM SCRINNG";
		return jdbc.selectList(sql);
	}



	public int beforeDeleteScreen(Object object) {
		String sql = "DELETE FROM SCRINNG WHERE SCRINNG_CODE = '" + object + "'";
		return jdbc.update(sql);
	}



	
	
	
}
