package service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.CartDao;

public class CartService {

	private CartService(){}
	private static CartService instance;
	
	public static CartService getInstance(){
		if(instance == null){
			instance = new CartService();
		}
		return instance;
	}
	
	private CartDao cartDao = CartDao.getInstance();
	
	
	public int CartHome(){
		int view = 0;
		List<Map<String,Object>> list = cartDao.getList();
		System.out.println("-----------------------------------");
		if(list.size() == 0){System.out.println("π μ₯λ°κ΅¬λμ λ΄κΈ΄ μνμ΄ μμ΅λλ€.");}
		for(int i = 0; i < list.size();i++){
			System.out.print(i+1 + ". ");
			System.out.print(list.get(i).get("FOOD_NM")+"  ");
			System.out.print(list.get(i).get("QTY")+"κ°  ");
			System.out.print(list.get(i).get("SUM")+"μ");
			System.out.println();
		}
		System.out.println("---------------------------------------------------");
		System.out.println("1.κ²°μ           2.κ°―μ μμ           3.μ­μ           4.μνκ΅¬λ§€          0.ννλ©΄");
		System.out.print("μλ ₯ > ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1 :
			view = purchase(); break;
		case 2 :
			view = updateQTY(list); break;
		case 3 :
			view = deleteQTY(list); break;
		case 4 :
			view = View.FOOD; break;
		case 0 :
			view = View.MAIN_HOME; break;
		}
		return view;
	}
	
	private int deleteQTY(List<Map<String,Object>> list) {
		System.out.println("-----------------------------------");
		if(list.size() == 0){System.out.println("π μ₯λ°κ΅¬λμ λ΄κΈ΄ μνμ΄ μμ΅λλ€.");}
		for(Map<String,Object> row:list){
			System.out.print(row.get("FOOD_NM")+"  ");
			System.out.print(row.get("QTY")+"κ°  ");
			System.out.print(row.get("SUM")+"μ");
			System.out.println();}
		System.out.println("-----------------------------------");
		while(true){System.out.println("π μ­μ νμ€ μνμ μ΄λ¦μ μλ ₯ν΄ μ£ΌμΈμ.");
		String del = ScanUtil.nextLine();
		System.out.println("π μ­μ νμκ² μ΅λκΉ? Y/N");
		System.out.print("μλ ₯ > ");
		String yn = ScanUtil.nextLine();
		if(yn.equals("Y")||yn.equals("y")){
			int result = cartDao.deleteQTY(del);
			if(result>0){System.out.println("β μ­μ  λμμ΅λλ€.");
			return View.CART;}
			else{System.out.println("β μ­μ  μ€ν¨νμμ΅λλ€. μνλͺμ μ ννκ² μλ ₯ν΄μ£ΌμΈμ.");}
		}else if(yn.equals("N")||yn.equals("n")){
			System.out.println("π μ₯λ°κ΅¬λ νλ©΄μΌλ‘ λμκ°λλ€.");
			break;
		}
		break;
		}
		return View.CART;
	}

	private int updateQTY(List<Map<String,Object>> list) {
		System.out.println("============ μ₯ λ° κ΅¬ λ ==============");
		System.out.println("-----------------------------------");
		if(list.size() == 0){System.out.println("π μ₯λ°κ΅¬λμ λ΄κΈ΄ μνμ΄ μμ΅λλ€.");}
		for(Map<String,Object> row:list){
			System.out.print(row.get("FOOD_NM")+"  ");
			System.out.print(row.get("QTY")+"κ°  ");
			System.out.print(row.get("SUM")+"μ");
			System.out.println();}
		System.out.println("-----------------------------------");
		System.out.println("π‘ μμ νμ€ μνμ μ΄λ¦μ μλ ₯ν΄ μ£ΌμΈμ.");
		System.out.print("μλ ₯ > ");
		String foodNM = ScanUtil.nextLine();
		System.out.println("π‘ μμ νμ€ μνμ κ°―μμ μλ ₯ν΄ μ£ΌμΈμ.");
		System.out.print("μλ ₯ > ");
		String foodQTY = ScanUtil.nextLine();
		int result = cartDao.updateQTY(foodNM,foodQTY);
		if(result>0){System.out.println("β μμ  λμμ΅λλ€.");}
		else{System.out.println("β μμ  μ€ν¨νμμ΅λλ€. μνλͺμ μ ννκ² μλ ₯ν΄μ£ΌμΈμ.");}
		return View.CART;
	}
	private int purchase() { // κ²°μ  κΈ°λ₯
		List<Map<String,Object>> list = cartDao.getList();
		int view = 0;
		Map<String,Object> row = cartDao.purchaseAll(); // κ²°μ ν  λ΄μ­
		String count = String.valueOf(row.get("COUNT")); // μ΄ κ°μ : λ°μ μμΌλ¬Έ λλ¦΄λ κ³μλ μ κ·Όμ λ°©μ§νκΈ° μν΄ λ³μμ μ μ₯
		String sum = String.valueOf(row.get("SUM")); // μ΄ ν©κ³ κΈμ‘ 
		loop : while(true){
			if(list.size() == 0){System.out.println("β κ²°μ νμ€ μ¬ν­μ΄ μμ΅λλ€.");break;}
			System.out.println("μ΄ " + count + "κ°μ ν©κ³ κΈμ‘ "+ sum.trim() + "μμ κ²°μ νμκ² μ΅λκΉ?");
			System.out.print("Y/N >"); 
			String input = ScanUtil.nextLine();
			boolean matches = regex("[YyNn]",input); //μ κ·μ λ£μ΄μ λ€λ₯Έ λ¬Έμ μλ€μ΄μ€κ² λ§κΈ°
			if(matches){
				switch(input){
				case "Y" : // YνΉμ y μλ ₯μ
					int userCash = Integer.parseInt(String.valueOf(Controller.LoginUser.get("CASH")));
					if(Integer.parseInt(sum.trim()) > userCash){
						System.out.println("π² μΆ©μ κΈμ‘μ΄ λΆμ‘±ν©λλ€. \t ννλ©΄μΌλ‘ λμκ°λλ€.");
						return View.MAIN_HOME;
					}else{
						int result= cartDao.updateCash(Integer.parseInt(sum.trim()));
						if(result > 0){ // μΊμ μ°¨κ° μλ£ μ
							result = cartDao.transferData(list);
							if(result > 0){ // μ μ‘ μλ£ μ
								result = cartDao.deleteCart(); // μ₯λ°κ΅¬λ μ­μ  μ¬λΆ
								 if(result > 0){ // μ­μ μ
									 System.out.println("π² κ²°μ κ° μλ£λμμ΅λλ€.");
									view = View.MAIN_HOME;
									break loop;
								 }
							 }
						}else{// κ²°μ  μ€ν¨μ
							System.out.println("π² κ²°μ κ° λμ§ μμμ΅λλ€. κ΄λ¦¬μμκ² λ¬Έμν΄μ£ΌμΈμ.");
							view = View.CART;}
					}
				case "y" :
					int userCash1 = Integer.parseInt(String.valueOf(Controller.LoginUser.get("CASH")));
					if(Integer.parseInt(sum.trim()) > userCash1){
						System.out.println("π² μΆ©μ κΈμ‘μ΄ λΆμ‘±ν©λλ€. \t ννλ©΄μΌλ‘ λμκ°λλ€.");
						return View.MAIN_HOME;
					}else{
						int result= cartDao.updateCash(Integer.parseInt(sum.trim()));
						if(result > 0){ // μΊμ μ°¨κ° μλ£ μ
							result = cartDao.transferData(list);
							if(result > 0){ // μ μ‘ μλ£ μ
								result = cartDao.deleteCart(); // μ₯λ°κ΅¬λ μ­μ  μ¬λΆ
								 if(result > 0){ // μ­μ μ
									 System.out.println("π² κ²°μ κ° μλ£λμμ΅λλ€.");
									view = View.MAIN_HOME;
									break loop;
								 }
							 }
						}else{// κ²°μ  μ€ν¨μ
							System.out.println("π² κ²°μ κ° λμ§ μμμ΅λλ€. κ΄λ¦¬μμκ² λ¬Έμν΄μ£ΌμΈμ.");
							view = View.CART;}
					}
				case "N" :
					System.out.println("π ν νλ©΄μΌλ‘ λμκ°λλ€.");
					break loop;
				case "n" :
					break loop;
				}
			}else // μ κ·μμ λ§μ§ μμΌλ©΄ λ£¨νλ£¨ν
				System.out.println("β μλͺ» μλ ₯νμ¨μ΅λλ€.");
			
		}
		return view;
	}


	private boolean regex(String regexForm, String checkThings) {
		Pattern p = Pattern.compile(regexForm);
		Matcher m = p.matcher(checkThings);
		return m.matches();
	}

	public int buyFood(){
		
		int input = ScanUtil.nextInt();
		switch(input){
		case 1:
	
//			int drinkPc = cartDao.getDrinkPC();
		break;	
		
		case 0:
		break;
		}	
		
		return View.FOOD;
	}
}
