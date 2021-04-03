package service;

import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import dao.FoodDao;

public class FoodService {

	private FoodService(){}
	private static FoodService instance;
	
	public static FoodService getInstance(){
		if(instance == null){
			instance = new FoodService();
		}
		return instance;
	}
	
	private FoodDao foodDao = dao.FoodDao.getInstance();
	
	
	
	
	public int foodHome(){
		System.out.println("1.팝콘     2.음료     3.스낵     4.세트     5.결제(장바구니)   0.뒤로");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1: input = buyPopCorn();//팝콘 종류 뿌려주기
			break;
		case 2: buyDrink();//음료 종류 뿌려주기
			break;
		case 3: buySnack();//스낵 종류 뿌려주기
			break;
		case 4: buySetMenu();//스낵 종류 뿌려주기
			break;
		case 5: //장바구니 뷰로
			return View.CART;
		case 0: //메인으로
			return View.MAIN_HOME; 
		}
		
		return View.FOOD;
		
	}
	



	private int buyPopCorn() { //팝콘 구매하는 메서드
		List<Map<String, Object>> foodList = foodDao.getList("food1");
		System.out.println("===================================");
		System.out.println(" 🍿 P  O  P  C  O  R  N   M  E  N  U 🍿");
		System.out.println("-----------------------------------");
		for(int i = 0; i < foodList.size();i++){ // 팝콘메뉴 보여주는 화면
			System.out.print(i+1 + ". ");
			System.out.print(foodList.get(i).get("FOOD_NM")+"  ");
			System.out.print(foodList.get(i).get("FOOD_PC")+"원  ");
			System.out.println();
		}
		System.out.println("===================================");
		System.out.println(" 🍿 팝  콘     종  류  를     선  택     해  주  세  요 🍿");
		List<Map<String,Object>> cartList = foodDao.getCartList();
		System.out.print("입력 > ");
		int kind = ScanUtil.nextInt();
		System.out.println("👆 구  매  하  실   수  량  을   입  력   해  주  세  요");
		System.out.println("ex) 5");
		System.out.print("입력 > ");
		String qty = ScanUtil.nextLine();
		swit : switch(kind){
			case 1: // 팝콘(기본형)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food1_1", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food1_1")){
							int result = foodDao.updateQTY("food1_1", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food1_1", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
					
				
			case 2: // 팝콘 선택(캬라멜 맛)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food1_2", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food1_2")){
							int result = foodDao.updateQTY("food1_2", qty);
							if(result > 0){
								System.out.println("💡 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("💡 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food1_2", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
				
			case 3: // 팝콘 선택(무야호 맛)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food1_3", qty);
					if(result > 0){
						System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food1_3")){
							int result = foodDao.updateQTY("food1_3", qty);
							if(result > 0){
								System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
								System.out.println("🛒 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food1_3", qty);
							if(result > 0){
								System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요 ❌");
								break swit;
							}
						}
		}return View.CART; 
	} // 팝콘 구매 메서드 끝
		
	private int buyDrink() { //음료 구매하는 메서드
		List<Map<String, Object>> foodList = foodDao.getList("food2");
		System.out.println("===================================");
		System.out.println(" 🥤 D  R  I  N  K      M  E  N  U 🥤");
		System.out.println("-----------------------------------");
		for(int i = 0; i < foodList.size();i++){ // 음료메뉴 보여주는 화면
			System.out.print(i+1 + ". ");
			System.out.print(foodList.get(i).get("FOOD_NM")+"  ");
			System.out.print(foodList.get(i).get("FOOD_PC")+"원  ");
			System.out.println();
		}
		System.out.println("===================================");
		System.out.println("  🥤 음  료     종  류  를     선  택     해  주  세  요 🥤");
		List<Map<String,Object>> cartList = foodDao.getCartList();
		System.out.print("입력 > ");
		int kind = ScanUtil.nextInt();
		System.out.println("👆 구  매  하  실   수  량  을   입  력   해  주  세  요");
		System.out.println("ex) 5");
		System.out.print("입력 > ");
		String qty = ScanUtil.nextLine();
		
		swit : switch(kind){
			case 1: // 음료(콜라)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food2_1", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food2_1")){
							int result = foodDao.updateQTY("food2_1", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food2_1", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
				
			case 2: // 음료 선택(사이다)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food2_2", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food2_2")){
							int result = foodDao.updateQTY("food2_2", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food2_2", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
				
			case 3: // 음료 선택(맥주)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food2_3", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food2_3")){
							int result = foodDao.updateQTY("food2_3", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food2_3", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
		}return View.CART;
	} // 팝콘 구매 메서드 끝

	private int buySnack() {
		List<Map<String, Object>> foodList = foodDao.getList("food3");
		System.out.println("===================================");
		System.out.println(" 🍴  S  N  A  C  K     M   E   N   U 🍴");
		System.out.println("-----------------------------------");
		for(int i = 0; i < foodList.size();i++){ // 팝콘메뉴 보여주는 화면
			System.out.print(i+1 + ". ");
			System.out.print(foodList.get(i).get("FOOD_NM")+"  ");
			System.out.print(foodList.get(i).get("FOOD_PC")+"원  ");
			System.out.println();
		}
		System.out.println("===================================");
		System.out.println(" 🍴 스  낵     종  류  를     선  택     해  주  세  요. 🍴");
		System.out.print("입력 > ");
		List<Map<String,Object>> cartList = foodDao.getCartList();
		int kind = ScanUtil.nextInt();
		System.out.println("👆 구  매  하  실   수  량  을   입  력   해  주  세  요");
		System.out.println("ex) 5");
		System.out.print("입력 > ");
		String qty = ScanUtil.nextLine();
		
		swit : switch(kind){
			case 1: // 스낵선택(나쵸)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food3_1", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food3_1")){
							int result = foodDao.updateQTY("food3_1", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food3_1", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
			
			case 2: // 스낵 선택(오징어)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food3_2", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food3_2")){
							int result = foodDao.updateQTY("food3_2", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food3_2", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
		}return View.CART;
	}

	private int buySetMenu() {
		List<Map<String, Object>> foodList = foodDao.getList("food4");
		System.out.println("===================================");
		System.out.println(" 🥡  S    E    T       M   E   N   U 🥡");
		System.out.println("-----------------------------------");
		for(int i = 0; i < foodList.size();i++){ // 세트메뉴 보여주는 화면
			System.out.print(i+1 + ". ");
			System.out.print(foodList.get(i).get("FOOD_NM")+"  ");
			System.out.print(foodList.get(i).get("FOOD_PC")+"원  ");
			System.out.println();
		}
		System.out.println("===================================");
		System.out.println("  🥡 세  트     메  뉴  를     선  택     해  주  세  요 🥡");
		List<Map<String,Object>> cartList = foodDao.getCartList();
		System.out.print("입력 > ");
		int kind = ScanUtil.nextInt();
		System.out.println("👆 구  매  하  실   수  량  을   입  력   해  주  세  요");
		System.out.println("ex) 5");
		System.out.print("입력 > ");
		String qty = ScanUtil.nextLine();
		
		swit : switch(kind){
			case 1: // 세트(팝콘+콜라)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food4_1", qty);
					if(result > 0){
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food4_1")){
							int result = foodDao.updateQTY("food4_1", qty);
							if(result > 0){
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food4_1", qty);
							if(result > 0){
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}
			
			case 2: // 세트메뉴 선택(무야호 세트)
				if(cartList.size() == 0){
					int result = foodDao.insertQTY("food4_2", qty);
					if(result > 0){
						System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
						System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
						break swit;
					}else{
						System.out.println("선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
						break swit;
					}
				}else{
					for(Map<String, Object> row : cartList){
						 if(row.get("FOOD_CODE").equals("food4_2")){
							int result = foodDao.updateQTY("food4_2", qty);
							if(result > 0){
								System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
								System.out.println("❗ 선택하신 상품이 추가 구매 되었습니다.");
								break swit;
							}else{
								System.out.println("❌ 선택하신 상품 추가에 실패하였습니다. 관리자에게 문의해주세요.");
								break swit;
							}
						}}
							int result = foodDao.insertQTY("food4_2", qty);
							if(result > 0){
								System.out.println("🔊 무 ~ 야 ~ ~ 호 ~ ~ ~ 오 ~ ~ ~ ~ 🔊");
								System.out.println("🛒 선택하신 상품을 장바구니에 담았습니다.");
								break swit;
							}else{
								break swit;
							}
						}
		}return View.CART;
	}
	
	
}


