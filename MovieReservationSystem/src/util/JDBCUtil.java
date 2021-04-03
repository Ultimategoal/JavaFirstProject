package util;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	
	//싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴

	private JDBCUtil(){
		
	} //다른 클래스에서 객체를 생성하지 못하게 private로 생성자
	
	//인스턴스를 보관할 변수
	private static JDBCUtil instance;
	
	//인스턴스를 빌려주는 메서드
	public static JDBCUtil getInstance(){
		if(instance == null){
			instance = new JDBCUtil();
		}
		return instance;
	}
	
	String url = "jdbc:oracle:thin:@192.168.44.157:1521:xe";
	String user = "mooyaho";
	String password = "java";

	
	java.sql.Connection con = null;	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	/*
	 * - 조회의 결과가 한 줄인 경우, param은 쿼리문에 ?를 처리
	 * Map<String, Object> selectOne(String sql)
	 * Map<String, Object> selectOne(String sql, List<Object> param)
	 * 
	 * - 조회의 결과가 한 줄 이상인 경우
	 * List<Map<String, Object>> selectList(String sql)
	 * List<Map<String, Object>> selectList(String sql, List<Object param)
	 * 
	 * - select외의 나머지 쿼리 사용시 
	 * int update(String sql)
	 * int update(String sql, List<Object> param)
	 * 
	 */
	
	public Map<String, Object> selectOne(String sql){
		
		Map<String, Object> row = null;
	try {
		con = DriverManager.getConnection(url, user, password);
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();
		
		int columnCnt = metaData.getColumnCount();
		
		if(rs.next()){
			row = new HashMap<>();
		for(int i = 1; i <= columnCnt; i++){
			row.put(metaData.getColumnName(i), rs.getObject(i));
		}
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	return row;
}
	
	
	
	
	public Map<String, Object> selectOne(String sql, List<Object> param){
			Map<String, Object> row = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i ++){
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCnt = metaData.getColumnCount();
			
			if(rs.next()){
				row = new HashMap<>();
			for(int i = 1; i <= columnCnt; i++){
				row.put(metaData.getColumnName(i), rs.getObject(i));
			}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return row;
	}
	
	
	
	
	
	
	
	public List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCnt = metaData.getColumnCount();
			while(rs.next()){
				Map<String, Object> row = new HashMap<String, Object>();
				for(int i = 1; i <= columnCnt; i++){
					row.put(metaData.getColumnName(i), rs.getObject(i));
				}
				list.add(row);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		return list;
		
	}
	
	
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCnt = metaData.getColumnCount();
			while(rs.next()){
				Map<String, Object> row = new HashMap<String, Object>();
				for(int i = 1; i <= columnCnt; i++){
					row.put(metaData.getColumnName(i), rs.getObject(i));
				}
				list.add(row);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		return list;
	}
	
	
	public int update(String sql){
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	
	
	
	
	
	public int update(String sql, List<Object> param){
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}

	public int transfer(String sql, Map<String,Object> param){ // 다른테이블로 데이터 넘기기
		int result = 0;
		
		try {
			
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			ps.setObject(1, param.get("USID"));
			ps.setObject(2, param.get("FOOD_CODE")); 
			ps.setObject(3, param.get("QTY")); 
		
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}

	public int transferAdv(String sql, Map<String,Object> param){ // 다른테이블로 데이터 넘기기
		int result = 0;
		
		try {
			
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			
			ps.setObject(1, param.get("ADVANTK_CODE"));
			ps.setObject(2, param.get("SEAT_NO_CODE"));
			ps.setObject(3, param.get("SCRINNG_CODE"));
			ps.setObject(4, param.get("USID"));
			ps.setObject(5, param.get("ADVANTK_AT"));
			ps.setObject(6, param.get("THEAT_CODE"));
			
			String cineCode = (String)param.get("THEAT_CODE"); // 영화관 코드 넣기 위해 상영관 코드에서 앞글자 따오기
			ps.setObject(7, cineCode.substring(0, 1));
			
			
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	
}














