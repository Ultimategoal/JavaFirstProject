package controller;

import java.util.Map;

import service.CartService;
import service.FoodService;
import service.MovieAdminService;
import service.ReservationService;
import service.ReviewService;
import service.SystemAdminService;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		
		new Controller().start();
	}
	
	public static Map<String, Object> LoginUser;
	
	private UserService userService = UserService.getInstance();
	private ReservationService reservationService = ReservationService.getInstance();
	private ReviewService rs = ReviewService.getInstance();
	private CartService cs = CartService.getInstance();
	private FoodService fs = FoodService.getInstance();
	private MovieAdminService mas = MovieAdminService.getInstance();
	private SystemAdminService sa = SystemAdminService.getInstance();
	
	//return 받은 값을 이용하여 화면이동
	private void start() {
		int view = View.HOME;
		
		showStart();
		
		while(true){
			switch(view){
			case View.HOME : view = home(); break;
			case View.MAIN_HOME : view = userService.mainHome(); break;
			case View.MYPAGE : view = userService.myPage(); break;
			case View.RESERVATION : view = reservationService.reservation(); break;
			case View.LOGIN : view = userService.login(); break;
			case View.JOIN : view = userService.join(); break;
			case View.CART : view = cs.CartHome(); break;
			
			case View.FOOD : view = fs.foodHome(); break;
			case View.REVIEW : view = rs.reviewList(); break;
			case View.MYREVIEW : view = rs.selectMyReview(); break; 
			
			//영화 관리자
			case View.MOVIEADMIN : view = mas.movieAdmin(); break;
			case View.MOVIEMANAGEMENT : view = mas.selectMovieManage(); break;
			case View.MOVIESCREEN : view = mas.selectScreenManage();  break;
			
			//시스템 관리자
			case View.REVIEWADMIN : view = sa.reviewList(); break;
			}
		}
	}
	

		
	private void showStart() {
//		 for (int i = 0; i < 70; i++) {
//	         System.out.print("🎥");
//	         try {
//	            Thread.sleep(10);
//	         } catch (InterruptedException e) {
//	            e.printStackTrace();
//	         }
//	      }  
//		
//	      for(int i = 0; i < 9; i++){
//	    	 
//	    	  if(i == 0)
//		      System.out.println();
//	    	  if(i == 1)
//		      System.out.println();
//	    	  if(i == 2)
//		      System.out.println("\t🎞🎞🎞             🎞🎞🎞                     🎞🎞                                🎞🎞                    🎞🎞             🎞🎞                         🎞🎞                               🎞🎞                🎞🎞                     🎞🎞");
//	    	  if(i == 3)
//		      System.out.println("\t🎞🎞    🎞     🎞    🎞🎞             🎞🎞      🎞🎞               🎞🎞      🎞🎞             🎞🎞           🎞🎞                🎞🎞          🎞🎞                    🎞🎞                🎞🎞             🎞🎞      🎞🎞");
//	    	  if(i == 4)
//		      System.out.println("\t🎞🎞       🎞🎞      🎞🎞           🎞🎞           🎞🎞         🎞🎞           🎞🎞             🎞🎞       🎞🎞                🎞🎞               🎞🎞                 🎞🎞🎞🎞🎞🎞🎞           🎞🎞           🎞🎞");
//	    	  if(i == 5)
//		      System.out.println("\t🎞🎞          🎞        🎞🎞           🎞🎞           🎞🎞          🎞🎞           🎞🎞                    🎞  🎞                     🎞🎞🎞🎞🎞🎞🎞🎞              🎞🎞🎞🎞🎞🎞🎞           🎞🎞           🎞🎞");
//	    	  if(i == 6)
//		      System.out.println("\t🎞🎞                       🎞🎞             🎞🎞       🎞🎞               🎞🎞       🎞🎞                         🎞                       🎞🎞                         🎞🎞            🎞🎞                🎞🎞             🎞🎞       🎞🎞");
//	    	  if(i == 7)
//		      System.out.println("\t🎞🎞                       🎞🎞                      🎞🎞                                🎞🎞                                  🎞                      🎞🎞                            🎞🎞          🎞🎞                🎞🎞                       🎞🎞 ");
//	    	  if(i == 8)
//		      System.out.println();
//	    	  try {
//		            Thread.sleep(100);
//		         } catch (InterruptedException e) {
//		            e.printStackTrace();
//		         }
//	    	  
//	      }
//	      for (int i = 0; i < 70; i++) {
//	         System.out.print("🎞");
//	         try {
//	            Thread.sleep(10);
//	         } catch (InterruptedException e) {
//	            e.printStackTrace();
//	            }
//	         }
		
	}
	
	private int home() {                  //홈 화면
	     
	System.out.println();
		System.out.println("************* ༼ つ ◕_◕ ༽つ*************");
		System.out.println(" 1.로그인🗝      2.회원가입❤      0.프로그램 종료💢");
		System.out.println("***********************************");
		System.out.print("선택 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1 : //로그인
			return View.LOGIN;
		case 2 : //회원가입
			return View.JOIN;
		case 0 : //프로그램 종료
			System.out.println("❌ 프로그램이 종료되었습니다 ❌");
			System.out.println("   0( ﾟдﾟ)つ Bye");
			System.exit(0);
		}
		return View.HOME; // 1, 2, 0 이외에 다른 숫자 입력시 다시 돌아오게 설정
	}

	
	
}





















