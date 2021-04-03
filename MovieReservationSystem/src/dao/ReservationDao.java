package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import controller.Controller;

public class ReservationDao {
	
	
	private ReservationDao(){}
	private static ReservationDao instance;
	
	public static ReservationDao getInstance(){
		if(instance == null){
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	List<Object> param;

	

	public List<Map<String, Object>> showCinema(String city) {
		String sql = "SELECT CINEMA_NM,CINEMA_CODE FROM CINEMA"
				+ " WHERE SUBSTR(ADRES,1,2) = ?"
				+ " ORDER BY 1";
		
		param = new ArrayList<>();
		param.add(city);
		return jdbc.selectList(sql,param);
	}



	public List<Map<String, Object>> screen(Object cinemaCode) {
		String sql = "SELECT CAST(B.MOVIE_SJ AS CHAR(50)) 영화제목, A.SCRINNG_DATE 상영날짜, A.SCRINNG_VWPOINT 상영시간, SUBSTR(A.THEAT_CODE,2) 상영관 "
				+ ", SCRINNG_CODE 상영코드, FNC_GET_SCREEN(SCRINNG_CODE) 남은좌석수, FNC_GET_SCREENTOTAL(SCRINNG_CODE) 총좌석수"
				+ " FROM  SCRINNG A, MOVIE B  WHERE A.MOVIE_CODE = B.MOVIE_CODE AND A.CINEMA_CODE = ?"
				+ " ORDER BY 1,2,3";
		
		param = new ArrayList<>();
		param.add(cinemaCode);
		
		return jdbc.selectList(sql, param);
	}

	public int transferData(List<Map<String,Object>> list) {
		Map<String,Object> param = new HashMap<>();
		int resultSum = 0;
		
		for(Map<String,Object> row : list){//받아온 리스트에서 ?에 들어갈 값 추출해서 맵으로 넘기기 
			param.put("ADVANTK_CODE",row.get("ADVANTK_CODE"));
			param.put("SEAT_NO_CODE",row.get("SEAT_NO_CODE")); 
			param.put("SCRINNG_CODE",row.get("SCRINNG_CODE")); 
			param.put("USID",row.get("USID")); 
			param.put("ADVANTK_AT",row.get("ADVANTK_AT")); 
			param.put("THEAT_CODE",row.get("THEAT_CODE")); 
			String sql = "INSERT INTO ADVAN_DTLS (ADVAN_DTLS_CODE, SETLE_DATE, ADVANTK_CODE, SEAT_NO_CODE, SCRINNG_CODE, USID, ADVANTK_AT, THEAT_CODE, CINEMA_CODE)" 
			            +" VALUES ('purCode_'||TRIM(to_char((NVL((SELECT max(substr(ADVAN_DTLS_CODE,instr(ADVAN_DTLS_CODE,'_')+1)) FROM ADVAN_dtls),0)+1),'000')),"
					    +" SYSDATE, ? ,? , ? , ? , ? , ? , ? )";

			int result = jdbc.transferAdv(sql,param);
			
			resultSum +=  result;
		}
		return resultSum;
	}


	public List<Map<String, Object>> seatDao(Object screenCode) {
		String sql = "SELECT A.SEAT_COLUMN 열, TO_CHAR(A.SEAT_ROW,'999') 행, NVL(B.ADVANTK_AT,'N') 예매여부,"
				+ " A.SEAT_COLUMN || A.SEAT_ROW 좌석"
				+ " FROM SEAT A LEFT OUTER JOIN ADVANTK B ON A.SEAT_NO_CODE = B.SEAT_NO_CODE  AND B.SCRINNG_CODE = ?"
				+ " AND A.THEAT_CODE = B.THEAT_CODE "
				+ " WHERE A.THEAT_CODE = (SELECT THEAT_CODE FROM SCRINNG WHERE SCRINNG_CODE = ?) "
				+ " ORDER BY 2,1";
		
		param = new ArrayList<>();
		param.add(screenCode);
		param.add(screenCode);
		return jdbc.selectList(sql,param);
	}

	


	public int insertAtTicketDao(Object screenCode, String selectSeat) {
		String sql = "INSERT INTO ADVANTK VALUES("
				+ " (SELECT NVL(MAX(TO_CHAR(ADVANTK_CODE,'999')),0) + 1 FROM ADVANTK),"
				+ "?,?,?,?,"
				+ "(SELECT THEAT_CODE FROM SCRINNG WHERE SCRINNG_CODE = ?))";
			
		param = new ArrayList<>();
		param.add(selectSeat);
		param.add(screenCode);
		param.add(Controller.LoginUser.get("USID"));
		param.add("Y");
		param.add(screenCode);
		
		return jdbc.update(sql, param);
		
	}



	public List<Map<String, Object>> cinemaOfCityDao() {
		
		String sql = "SELECT DISTINCT SUBSTR(ADRES,1,2) 지역 FROM CINEMA";
		 
		
		return jdbc.selectList(sql);
	}



	public Map<String, Object> priceDao(String selectSeat) {
		
		String sql = "SELECT A.SEAT_NO_CODE 좌석번호, B.PC_CODE 가격코드, B.PC 가격 "
				+ "FROM SEAT A INNER JOIN PC_TABLE B ON(A.PC_CODE = B.PC_CODE)"
				+ " WHERE A.SEAT_NO_CODE = '" + selectSeat +"'";
		return jdbc.selectOne(sql);
	}

	  public List<Map<String, Object>> selectAdvList() {
	      
	      
	      String sql = "SELECT * FROM ADVANTK where usid = '" + Controller.LoginUser.get("USID") + "' AND ADVANTK_CODE = (SELECT NVL(MAX(ADVANTK_CODE),0) FROM ADVANTK)";
	      
	      return jdbc.selectList(sql);
	   }
	
   public List<Map<String, Object>> selectAdvOne() {
  
  
      String sql = "SELECT * FROM ADVANTK where usid = '" + Controller.LoginUser.get("USID") + "' and ADVANTK_CODE = (SELECT MAX(ADVANTK_CODE) FROM ADVANTK)";
      
      return jdbc.selectList(sql);
   }

//	public int deleteData(List<Map<String, Object>> advanlist) {
//	String sql = "DELETE FROM ADVANTK where usid = '" + Controller.LoginUser.get("USID") + "'";
//	
//	return jdbc.update(sql);
//	}	



	public Map<String, Object> myCashDao() {
		
		String sql = "SELECT CASH FROM MEMBER WHERE USID = '" + Controller.LoginUser.get("USID") + "'";
		return jdbc.selectOne(sql);
	}



	public int payDao(int payPrice) {
		String sql = "UPDATE MEMBER SET CASH = (CASH -" + payPrice +") WHERE USID = '" +  Controller.LoginUser.get("USID") + "'";
		return jdbc.update(sql);
	}



	public Map<String, Object> cinemaDao(String cinemaCode) {
		String sql = "SELECT CINEMA_NM FROM CINEMA WHERE CINEMA_CODE = '" + cinemaCode + "'";
		return jdbc.selectOne(sql);
	}



	
	
	
}

