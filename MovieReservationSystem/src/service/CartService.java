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
		if(list.size() == 0){System.out.println("🛒 장바구니에 담긴 상품이 없습니다.");}
		for(int i = 0; i < list.size();i++){
			System.out.print(i+1 + ". ");
			System.out.print(list.get(i).get("FOOD_NM")+"  ");
			System.out.print(list.get(i).get("QTY")+"개  ");
			System.out.print(list.get(i).get("SUM")+"원");
			System.out.println();
		}
		System.out.println("---------------------------------------------------");
		System.out.println("1.결제          2.갯수 수정          3.삭제          4.상품구매          0.홈화면");
		System.out.print("입력 > ");
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
		if(list.size() == 0){System.out.println("🛒 장바구니에 담긴 상품이 없습니다.");}
		for(Map<String,Object> row:list){
			System.out.print(row.get("FOOD_NM")+"  ");
			System.out.print(row.get("QTY")+"개  ");
			System.out.print(row.get("SUM")+"원");
			System.out.println();}
		System.out.println("-----------------------------------");
		while(true){System.out.println("🗑 삭제하실 상품의 이름을 입력해 주세요.");
		String del = ScanUtil.nextLine();
		System.out.println("🗑 삭제하시겠습니까? Y/N");
		System.out.print("입력 > ");
		String yn = ScanUtil.nextLine();
		if(yn.equals("Y")||yn.equals("y")){
			int result = cartDao.deleteQTY(del);
			if(result>0){System.out.println("❗ 삭제 되었습니다.");
			return View.CART;}
			else{System.out.println("❗ 삭제 실패하였습니다. 상품명을 정확하게 입력해주세요.");}
		}else if(yn.equals("N")||yn.equals("n")){
			System.out.println("🛒 장바구니 화면으로 돌아갑니다.");
			break;
		}
		break;
		}
		return View.CART;
	}

	private int updateQTY(List<Map<String,Object>> list) {
		System.out.println("============ 장 바 구 니 ==============");
		System.out.println("-----------------------------------");
		if(list.size() == 0){System.out.println("🛒 장바구니에 담긴 상품이 없습니다.");}
		for(Map<String,Object> row:list){
			System.out.print(row.get("FOOD_NM")+"  ");
			System.out.print(row.get("QTY")+"개  ");
			System.out.print(row.get("SUM")+"원");
			System.out.println();}
		System.out.println("-----------------------------------");
		System.out.println("💡 수정하실 상품의 이름을 입력해 주세요.");
		System.out.print("입력 > ");
		String foodNM = ScanUtil.nextLine();
		System.out.println("💡 수정하실 상품의 갯수을 입력해 주세요.");
		System.out.print("입력 > ");
		String foodQTY = ScanUtil.nextLine();
		int result = cartDao.updateQTY(foodNM,foodQTY);
		if(result>0){System.out.println("❗ 수정 되었습니다.");}
		else{System.out.println("❌ 수정 실패하였습니다. 상품명을 정확하게 입력해주세요.");}
		return View.CART;
	}
	private int purchase() { // 결제 기능
		List<Map<String,Object>> list = cartDao.getList();
		int view = 0;
		Map<String,Object> row = cartDao.purchaseAll(); // 결제할 내역
		String count = String.valueOf(row.get("COUNT")); // 총 개수 : 밑에 와일문 돌릴때 계속된 접근을 방지하기 위해 변수에 저장
		String sum = String.valueOf(row.get("SUM")); // 총 합계 금액 
		loop : while(true){
			if(list.size() == 0){System.out.println("❗ 결제하실 사항이 없습니다.");break;}
			System.out.println("총 " + count + "개의 합계 금액 "+ sum.trim() + "원을 결제하시겠습니까?");
			System.out.print("Y/N >"); 
			String input = ScanUtil.nextLine();
			boolean matches = regex("[YyNn]",input); //정규식 넣어서 다른 문자 안들어오게 막기
			if(matches){
				switch(input){
				case "Y" : // Y혹은 y 입력시
					int userCash = Integer.parseInt(String.valueOf(Controller.LoginUser.get("CASH")));
					if(Integer.parseInt(sum.trim()) > userCash){
						System.out.println("💲 충전금액이 부족합니다. \t 홈화면으로 돌아갑니다.");
						return View.MAIN_HOME;
					}else{
						int result= cartDao.updateCash(Integer.parseInt(sum.trim()));
						if(result > 0){ // 캐시 차감 완료 시
							result = cartDao.transferData(list);
							if(result > 0){ // 전송 완료 시
								result = cartDao.deleteCart(); // 장바구니 삭제 여부
								 if(result > 0){ // 삭제시
									 System.out.println("💲 결제가 완료되었습니다.");
									view = View.MAIN_HOME;
									break loop;
								 }
							 }
						}else{// 결제 실패시
							System.out.println("💲 결제가 되지 않았습니다. 관리자에게 문의해주세요.");
							view = View.CART;}
					}
				case "y" :
					int userCash1 = Integer.parseInt(String.valueOf(Controller.LoginUser.get("CASH")));
					if(Integer.parseInt(sum.trim()) > userCash1){
						System.out.println("💲 충전금액이 부족합니다. \t 홈화면으로 돌아갑니다.");
						return View.MAIN_HOME;
					}else{
						int result= cartDao.updateCash(Integer.parseInt(sum.trim()));
						if(result > 0){ // 캐시 차감 완료 시
							result = cartDao.transferData(list);
							if(result > 0){ // 전송 완료 시
								result = cartDao.deleteCart(); // 장바구니 삭제 여부
								 if(result > 0){ // 삭제시
									 System.out.println("💲 결제가 완료되었습니다.");
									view = View.MAIN_HOME;
									break loop;
								 }
							 }
						}else{// 결제 실패시
							System.out.println("💲 결제가 되지 않았습니다. 관리자에게 문의해주세요.");
							view = View.CART;}
					}
				case "N" :
					System.out.println("🏚 홈 화면으로 돌아갑니다.");
					break loop;
				case "n" :
					break loop;
				}
			}else // 정규식에 맞지 않으면 루한루프
				System.out.println("❌ 잘못 입력하셨습니다.");
			
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
