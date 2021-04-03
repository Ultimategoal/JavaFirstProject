package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import controller.Controller;


public class CartDao {
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	private CartDao(){}
	private static CartDao instance;
	
	public static CartDao getInstance() {
		if(instance == null){
			instance = new CartDao();
		}
		return instance;
	}

	
	public int getDrinkPC(){
		int result = 0;
		
		return result;
	}


	public Map<String,Object> purchaseAll() {
		String sql = "SELECT COUNT(*) COUNT, SUM(C.QTY*F.FOOD_PC) SUM "
				+ " FROM CART C, FOOD F WHERE C.FOOD_CODE = F.FOOD_CODE AND USID = '"+Controller.LoginUser.get("USID")+"'";
		
	
		return jdbc.selectOne(sql);
	}

	public Map<String, Object> myCashDao() {
		
		String sql = "SELECT CASH FROM MEMBER WHERE USID = '" + Controller.LoginUser.get("USID") + "'";
		return jdbc.selectOne(sql);
	}

	public int transferData(List<Map<String,Object>> list) { // 고쳐야함.
		Map<String,Object> param = new HashMap<>();
		int resultSum = 0;
		
		for(Map<String,Object> row : list){//받아온 리스트에서 ?에 들어갈 값 추출해서 맵으로 넘기기 
			param.put("USID",row.get("USID"));
			param.put("FOOD_CODE",row.get("FOOD_CODE")); 
			param.put("QTY",row.get("QTY")); 
			String sql = "INSERT INTO PURCHS_DTLS (PURCHS_DTLS_CODE,setle_date,USID,FOOD_CODE,QTY) " //컬럼 값 추가
					+ " VALUES (TRIM('purCode_'||to_char((NVL((SELECT max(substr(purchs_dtls_code,instr(purchs_dtls_Code,'_')+1)) FROM purchs_dtls),0)+1),'000')), "
					+ "SYSDATE, ?, ?, ? )";

			int result = jdbc.transfer(sql,param);
			
			resultSum +=  result;
		}
		return resultSum;
	}


	public int deleteCart() {
		String sql = "DELETE FROM CART WHERE USID = '"+ Controller.LoginUser.get("USID")+"'";
		return jdbc.update(sql);
	}


	public List<Map<String, Object>> getList() {
		String sql = "SELECT C.USID, F.FOOD_NM, F.FOOD_CODE, C.QTY, F.FOOD_PC, sum(C.QTY*F.FOOD_PC) sum "
				+ " FROM CART C, FOOD F"
				+ " WHERE C.FOOD_CODE = F.FOOD_CODE AND USID = '"
				+ ""+ Controller.LoginUser.get("USID")+"'"
				+ " Group BY C.USID, F.FOOD_NM, C.QTY, F.FOOD_PC, F.FOOD_CODE";
		return jdbc.selectList(sql);
	}


	public int deleteQTY(String foodName) {
				
		String sql = "DELETE FROM CART c "
				+ " WHERE FOOD_code ="
				+ " (select food_code from food where food_nm = '"
				+ foodName +"')";
		return jdbc.update(sql);
	}


	public int updateQTY(String foodName,String foodQTY) {
		
		String sql = "UPDATE CART c "
				+ " SET QTY = "+ foodQTY 
				+ " WHERE Food_CODE = (select food_code from food where food_nm = '"
				+ foodName +"')";
				
		return jdbc.update(sql);
	}


	public int updateCash(int parseInt) {
		String sql = "UPDATE MEMBER SET CASH = CASH - " + parseInt + " WHERE USID = '"+ Controller.LoginUser.get("USID")+"'";
		
		return jdbc.update(sql);
	}
	
	


	
	
	
}


