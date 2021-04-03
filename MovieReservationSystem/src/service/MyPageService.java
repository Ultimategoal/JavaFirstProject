package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sql.DATE;
import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MyPageDao;

public class MyPageService {
   
   //싱글톤 패턴
      private MyPageService(){ }
      private static MyPageService instance;
      
      public static MyPageService getInstance(){
         if(instance == null){
            instance = new MyPageService();
         }
         return instance;
      }
      private MyPageDao myPageDao = MyPageDao.getInstance();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      
      // 마이페이지 수정
//      public int updateMyPage() {
//         List<Map<String, Object>> list = new ArrayList<>();
//         list = myPageDao.selectLogin();
//         System.out.println("==================== 내 정보 확인====================");
//         for(Map<String, Object> loginInfo : list) {
//            System.out.println("▶ 아  이  디 : " + loginInfo.get("USID"));
//            System.out.println("▶ 이       름 : " + loginInfo.get("USER_NM"));
//            System.out.println("▶ 전화 번호 : " + loginInfo.get("TELNO"));
//            System.out.println("▶ 현재 잔액 : " + loginInfo.get("CASH"));
//         }
//         System.out.println("1.전화번호 수정          2.비밀번호 수정          3.잔액충전          4.회원탈퇴          0.뒤로가기");
//         System.out.println("======================================================");
//         System.out.print("입력 > ");
//
//         int input = ScanUtil.nextInt();
//         switch(input) {
//         case 1 : // 전화번호 수정
//            boolean flag = true;
//            re : while(flag) {
//               System.out.println("=============== 전화번호 수정 ===============");
//               System.out.println("________________________________________");
//               System.out.println("☎ 전화번호를 입력해주세요.(\"-\" 포함)");
//               System.out.print("☎ 전화번호 입력 > ");
//               String telNo = ScanUtil.nextLine();
//               String telNoRegex = "01[0-9]-[0-9]{3,4}-[0-9]{4}";
//                 Pattern p = Pattern.compile(telNoRegex);
//                 Matcher m = p.matcher(telNo);
//                  if(!m.matches()){
//                     System.out.println("❌ 형식에 맞지 않습니다. 다시 입력해주세요");
//                     continue re;
//                  } else {
//                  System.out.println("☎ 전화번호가 변경 되었습니다.");
//                  myPageDao.updateTelNo(telNo);
//                  break;
//               }
//            }
//         return View.MYPAGE;
//         
//         case 2 : // 비밀번호 수정
//            re : while(true) {
//            System.out.println("============== 비밀번호 수정 ===============");
//            System.out.println("❣ 비밀번호를 입력해주세요.");
//            System.out.print("❣ 비밀번호 입력 > ");
//            String password = ScanUtil.nextLine();
//            System.out.println("❣ 다시 한 번 비밀번호를 입력해주세요.");
//            System.out.print("❣ 비밀번호 재확인 > ");
//            String password2 = ScanUtil.nextLine();
//            if(password.equals(password2)){
//               System.out.println("❣ 비밀번호가 변경 되었습니다.");
//               myPageDao.updatePW(password2);
//               break;
//            } else {
//               System.out.println("❌ 비밀번호 변경이 실패했습니다. 다시 시도해주세요.");
//               continue re;
//               }
//            }
//         return View.MYPAGE;
//         
//         
//         case 3 : // 잔액충전
//            cashCharger();
//            return View.MYPAGE;
//         
//         case 4 : // 회원탈퇴
//            System.out.println("=============== 회원탈퇴 ===============");
//            System.out.println("(⊙ˍ⊙) 회원탈퇴 하시겠습니까? (⊙ˍ⊙)");
//            System.out.println("_____________________________________");
//            System.out.println("1.예          2.아니오");
//            System.out.print("입력 > ");
//            int input2 = ScanUtil.nextInt();
//            System.out.println("______________________________________");
//            switch(input2) {
//            case 1 :
//               myPageDao.deleteId();
//               Controller.LoginUser = null;
//               System.out.println("(┬┬﹏┬┬) 회원 탈퇴 되었습니다 (┬┬﹏┬┬)");
//               return View.HOME;
//            
//            case 2 :
//               return View.MYPAGE;
//            }
//            
//         case 0 : // 뒤로 가기
//            return View.MYPAGE;
//         }
//         return View.HOME;
//      }
            

      // 영화 예매 내역
      public int reservationSelect(){
         System.out.println("=============== 영화 예매 내역 ===============");
         List<Map<String, Object>> list = new ArrayList<>();
         list = myPageDao.reservationSelect();
         
         if(list.size() == 0) {
        	 System.out.println("==========================");
            System.out.println("❌ 예매하신 내역이 없습니다.");
            System.out.println("==========================");
            return View.MYPAGE;
         }
         else {
        	 System.out.printf("%-40s\t|%s\t\t|%s\t|%s\t|%s", "영화제목","영화관이름","상영관","좌석번호","예매일자");
        	 System.out.println();
        	 System.out.println("==============================================================================");
               for(Map<String, Object> myMovie : list){
            	   String title= (String)myMovie.get("영화제목");
            	   String cinema = (String)myMovie.get("영화관이름");
            	   String theat = (String)myMovie.get("상영관이름");
            	   String seat = (String)myMovie.get("좌석번호");
            	   String date = format.format(myMovie.get("예매일자"));
            	   
            	   System.out.printf("%s\t|" , title);
            	   System.out.printf("%s\t|" , cinema);
            	   System.out.printf("%s\t|" , theat.substring(1) + "관");
            	   System.out.printf("%s\t|" , seat);
            	   System.out.printf("%s" , date);
            	   System.out.println();
            /*	   
                  System.out.println(myMovie.get("아이디") + "\t\t" + myMovie.get("영화관이름") + "\t" + myMovie.get("영화제목")
                               + "\t" + myMovie.get("상영관")+ "\t" + myMovie.get("좌석번호") + "\t" + myMovie.get("예매일자"));*/
               }
               System.out.println("==============================================================================\n");
            System.out.println("👆 메뉴를 선택해주세요");
            System.out.println("1.예매 취소          2.홈 화면으로");
            System.out.print("입력 > ");
              switch(ScanUtil.nextInt()){
              case 1: // 예매 취소
                 for(int i = 0 ; i < list.size(); i++){
                     System.out.println(i+1+". "+list.get(i).get("아이디") + "\t" + list.get(i).get("영화관이름") + "\t" + list.get(i).get("영화제목")
                                  + "\t" + list.get(i).get("상영관")+ "\t" + list.get(i).get("좌석번호") + "\t" + list.get(i).get("예매일자")  );
                  }
                 System.out.println("❓ 취소를 원하시는 번호를 입력해주세요.");
                 System.out.print("입력 > ");
                  int input = ScanUtil.nextInt()-1;
                  String str = list.get(input).get("비교").toString(); 
                  double compare = Double.valueOf(str).doubleValue();
                  if(compare > 0){
                	  System.out.println("상영이 시작된 영화 티켓은 취소가 불가합니다.");
                  }else{
                  int delResult = myPageDao.deleteADV(list.get(input).get("ADVAN_DTLS_CODE"),list.get(input).get("좌석번호"),list.get(input).get("ADVANTK_CODE"));
                    if(delResult > 0) System.out.println("❗ 취소가 완료되었습니다.\n 마이페이지로 돌아갑니다.");
                    else System.out.println("❗ 취소에 실패하였습니다. 관리자에게 문의해주세요");}
                 return View.MYPAGE;
              case 2: // 홈화면
                 System.out.println("🏚 홈 화면으로 돌아갑니다.");
                 return View.MAIN_HOME;
              }
         }return View.MYPAGE;
      }
      
      // 구매했던 내역 리스트
      public int cartList(){
         System.out.println("=============== 구매 내역 리스트 ===============");
         List<Map<String, Object>> cartList = new ArrayList<>();
         cartList = myPageDao.cartList();
         
         System.out.println("구매일자\t\t상품\t\t수량");
         if(cartList.size() == 0) {
            System.out.println("❌ 구매하신 내역이 없습니다.");
         } else {
         for(Map<String, Object> food : cartList){
               System.out.println(food.get("PDATE") + "\t" + food.get("FOODNM") + "\t" + food.get("QTY"));
            }         
         System.out.println("_________________________________________________________________________");
         System.out.println();
         }
         System.out.println("😋 엔터를 눌러주시면 마이페이지로 돌아갑니다.");
         ScanUtil.nextLine();
         return View.CART;
      }
      
      // 내가 작성한 글 목록 확인
      public int myBoardList() {
         System.out.println("============================== 작성 목록 ==============================");
         List<Map<String, Object>> boardList = new ArrayList<>();
         boardList = myPageDao.myBoardList();
         System.out.println("|글 번호|\t|영화코드|\t\t|평점|\t|작성자|\t|작성일|\t\t\t|리뷰제목|");
         System.out.println("_____________________________________________________________________________________________________________");
         for(Map<String, Object> list : boardList) {
            if(list.get("USID") == null) {
               System.out.println("❌ 작성하신 리뷰가 없습니다.");
            } else {
               System.out.println("____________________________________________________________________________________________________________________________________________________________________________________");
            System.out.println(list.get("REVIEW_CODE") + "\t\t" + list.get("REVIEW_SJ") + "\t\t\t\t\t" + list.get("RELEASE_DATE") + "\t\t\t" 
                         + list.get("AVRG_SCORE") + "\t\t\t\t\t" + list.get("REVIEW_CN"));
            }
            System.out.println();
            System.out.println("____________________________________________________________________________________________________________________________________________________________________________________");
         }
         return View.MYPAGE;
      }
      
      
      // 캐쉬 충전
      public void cashCharger() {
         System.out.print("💲 충전금액 > ");
         int money = ScanUtil.nextInt();
         int result = myPageDao.cashChargerDao(money);
         if(result > 0){
            System.out.println("💲 충전이 완료 되었습니다");
         }else{
            System.out.println("💲 충전에 실패하였습니다");
         }
      }
      
      
      
      
      
      
      
      
      
      
      public int updateMyPage() {
          List<Map<String, Object>> list = new ArrayList<>();
          list = myPageDao.selectLogin();
          System.out.println("==================== 내 정보 확인====================");
          for(Map<String, Object> loginInfo : list) {
             System.out.println("▶ 아  이  디 : " + loginInfo.get("USID"));
             System.out.println("▶ 이       름 : " + loginInfo.get("USER_NM"));
             System.out.println("▶ 전화 번호 : " + loginInfo.get("TELNO"));
             System.out.println("▶ 현재 잔액 : " + loginInfo.get("CASH"));
          }
          System.out.println("1.전화번호 수정          2.비밀번호 수정          3.잔액충전          4.회원탈퇴          0.뒤로가기");
          System.out.println("======================================================");
          System.out.print("입력 > ");
          int input = ScanUtil.nextInt();
          
          switch(input) {
          case 1 : // 전화번호 수정
        	  telUpdate();
        	  return View.MYPAGE;
          
          case 2 : // 비밀번호 수정
        	  passWordUpdate();
        	  return View.MYPAGE;
          
          case 3 : // 잔액충전
             cashCharger();
             return View.MYPAGE;
          
          case 4 : // 회원탈퇴
        	 return withDrawal();
        	  
          case 0 : // 뒤로 가기
             return View.MYPAGE;
          }
          return View.MYPAGE;
       }
      
      // 전화번호 수정
      public int telUpdate(){
    	  boolean flag = true;
          re : while(flag) {
             System.out.println("=============== 전화번호 수정 ===============");
             System.out.println("________________________________________");
             System.out.println("☎ 전화번호를 입력해주세요.(\"-\" 포함)");
             System.out.print("☎ 전화번호 입력 > ");
             String telNo = ScanUtil.nextLine();
             String telNoRegex = "01[0-9]-[0-9]{3,4}-[0-9]{4}";
               Pattern p = Pattern.compile(telNoRegex);
               Matcher m = p.matcher(telNo);
                if(!m.matches()){
                   System.out.println("❌ 형식에 맞지 않습니다. 다시 입력해주세요");
                   continue re;
                } else {
                System.out.println("☎ 전화번호가 변경 되었습니다.");
                myPageDao.updateTelNo(telNo);
                break;
             }
          }
       return View.MYPAGE;  
      }
      
      // 비밀번호 수정
      public int passWordUpdate(){
    	  re : while(true) {
              System.out.println("============== 비밀번호 수정 ===============");
              System.out.println("❣ 비밀번호를 입력해주세요.");
              System.out.print("❣ 비밀번호 입력 > ");
              String password = ScanUtil.nextLine();
              System.out.println("❣ 다시 한 번 비밀번호를 입력해주세요.");
              System.out.print("❣ 비밀번호 재확인 > ");
              String password2 = ScanUtil.nextLine();
              if(password.equals(password2)){
                 System.out.println("❣ 비밀번호가 변경 되었습니다.");
                 myPageDao.updatePW(password2);
                 break;
              } else {
                 System.out.println("❌ 비밀번호 변경이 실패했습니다. 다시 시도해주세요.");
                 continue re;
                 }
              }
    	  return View.MYPAGE;
      }
      
      // 회원 탈퇴
      public int withDrawal() {
    	  System.out.println("=============== 회원탈퇴 ===============");
          System.out.println("(⊙ˍ⊙) 회원탈퇴 하시겠습니까? (⊙ˍ⊙)");
          System.out.println("_____________________________________");
          System.out.println("1.예          2.아니오");
          System.out.print("입력 > ");
          int input2 = ScanUtil.nextInt();
          System.out.println("______________________________________");
          switch(input2) {
          case 1 :
             myPageDao.deleteId();
             Controller.LoginUser = null;
             System.out.println("(┬┬﹏┬┬) 회원 탈퇴 되었습니다 (┬┬﹏┬┬)");
             break;// View.HOME;
          case 2 :
             return View.MYPAGE;
          }
    	  return View.HOME;
      }
      
      
      
}