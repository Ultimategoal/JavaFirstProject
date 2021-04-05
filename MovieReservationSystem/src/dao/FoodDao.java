package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import controller.Controller;


public class FoodDao {
	
	private FoodDao(){}
	private static FoodDao instance;
	
	public static FoodDao getInstance(){
		if(instance == null){
			instance = new FoodDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public List<Map<String, Object>> getList() {
	String sql = "SELECT * From food";
		
	return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> getList(String param) {
		String sql = "SELECT rpad(FOOD_NM,15) FOOD_NM, FOOD_PC, FOOD_CODE From food where FOOD_CODE LIKE '"+ param +"%'";
			
		return jdbc.selectList(sql);
		}
	
	public int updateQTY(String foodCode,String foodQTY) {
		
		String sql = "UPDATE CART c "
				+ " SET QTY = QTY +" + foodQTY 
				+ " WHERE Food_CODE = '" + foodCode +"'";
				
		return jdbc.update(sql);
	}

	public List<Map<String, Object>> getCartList() {
		String sql = "SELECT C.USID, F.FOOD_NM, F.FOOD_CODE, C.QTY, F.FOOD_PC, sum(C.QTY*F.FOOD_PC) sum "
				+ " FROM CART C, FOOD F"
				+ " WHERE C.FOOD_CODE = F.FOOD_CODE AND USID = '"
				+ ""+ Controller.LoginUser.get("USID")+"'"
				+ " Group BY C.USID, F.FOOD_NM, C.QTY, F.FOOD_PC, F.FOOD_CODE";
		return jdbc.selectList(sql);
	}

	public int insertQTY(String foodCode, String qty) {
		String sql = "INSERT INTO CART (USID,FOOD_CODE,QTY) VALUES('" + Controller.LoginUser.get("USID")
				+ "','" + foodCode + "'," + qty +")";
		int result = jdbc.update(sql);
		return result;
	}
}



