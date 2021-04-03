package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.MyPageService;
import util.JDBCUtil;
import controller.Controller;

public class MyPageDao {
   
   private MyPageDao(){ }
   private static MyPageDao instance;
   
   public static MyPageDao getInstance(){
      if(instance == null){
         instance = new MyPageDao();
      }
      return instance;
   }
   
   private JDBCUtil jdbc = JDBCUtil.getInstance();
   
   // 비밀번호 수정
   public int updatePW(String param){ 
      String sql = "Update member set password = '"+ param + "' where usid = '" + Controller.LoginUser.get("USID") + "'";
      
      return jdbc.update(sql);
   }
   
   // 전화번호 수정
   public int updateTelNo(String param){ 
      String sql = "Update member set telNo = '"+ param + "' where usid = '" + Controller.LoginUser.get("USID") + "'";
      
      return jdbc.update(sql);
   }
   
   // 회원 탈퇴
   public int deleteId() { 
      String sql = "Delete from member where usid = " +"'" + Controller.LoginUser.get("USID")+ "'";
      
      return jdbc.update(sql);
   }
   
   // 예매 내역
   public List<Map<String, Object>> reservationSelect() {
      List<Map<String, Object>> myMovie = new ArrayList<>();
      String sql = "SELECT distinct A.USID 아이디, c.CINEMA_NM 영화관이름, CAST(m.MOVIE_sj AS CHAR(40)) 영화제목, A.SEAT_NO_CODE 좌석번호 ,"
            + " A.SCRINNG_CODE 상영관, A.THEAT_CODE 상영관이름, A.SETLE_DATE 예매일자, A.ADVAN_DTLS_CODE, A.ADVANTK_CODE,"
            + " SC.SCRINNG_DATE 상영날짜, SC.SCRINNG_VWPOINT 상영시간, SYSDATE - TO_DATE(20||sc.scrinng_date||sc.scrinng_vwpoint, 'YYYY/MM/DD HH24:MI') 비교"
            + " FROM ADVAN_DTLS A, SCRINNG sc, movie m, cinema c "
            + "where a.scRinng_code = sc.scrinng_code and sc.movie_code = M.MOVIE_CODE and sc.cinema_code = c.cinema_code and a.usid ="
            + " " + "'" + Controller.LoginUser.get("USID")+ "'"
            + "ORDER BY 예매일자";
      
      myMovie = jdbc.selectList(sql);
      
      return myMovie;
      }
   
   // 장바구니 내역
   public List<Map<String, Object>> cartList(){
      List<Map<String, Object>> myCart = new ArrayList<>();
      String sql = "SELECT DISTINCT P.PURCHS_DTLS_CODE, A.FOODNM, P.QTY, P.USID, P.SETLE_DATE , TO_CHAR(P.SETLE_DATE, 'yy-mm-dd') pdate, SYSDATE " 
            + " FROM PURCHS_DTLS P, "
            + " (SELECT F.FOOD_NM FOODNM, F.FOOD_CODE FROM CART C, FOOD F WHERE C.FOOD_CODE(+) = F.FOOD_CODE) A "
            + " WHERE P.FOOD_CODE = A.FOOD_CODE and p.usid = " + "'" + Controller.LoginUser.get("USID")+ "'"
            + " ORDER BY pdate";
      myCart = jdbc.selectList(sql);
      
      return myCart;
   }
   
   // 내가 쓴 글 보기
   public List<Map<String, Object>> myBoardList() {
      List<Map<String, Object>> list = new ArrayList<>();
      String sql = "SELECT REVIEW_CODE, USID, REVIEW_SJ, AVRG_SCORE, RELEASE_DATE, REVIEW_CN FROM REVIEW WHERE USID = " +
            "'" + Controller.LoginUser.get("USID")+ "'";
      list = jdbc.selectList(sql);
      return  list;      
   }
   
   // 로그인 정보
   public List<Map<String, Object>> selectLogin() {
      String sql = "SELECT *"
            + " FROM MEMBER WHERE USID = " + "'" + Controller.LoginUser.get("USID")+ "'";
      List<Map<String, Object>> loginInFo = new ArrayList<>();
      
      loginInFo = jdbc.selectList(sql);
      
      return loginInFo;
   }
   
   //예매 시 결제
   public int cashChargerDao(int money) {
         String sql = "UPDATE MEMBER SET CASH = (CASH + " + money + " ) "
               + " WHERE USID = '" + Controller.LoginUser.get("USID") + "'"; 
      return jdbc.update(sql);
   }
   
   //예매 취소
   public int deleteADV(Object advanCode, Object seatCode, Object tkCode) {
      int result = deleteTK(tkCode);
      if (result > 0){
      updateCash(advanCode);   
      String sql = "Delete from ADVAN_DTLS where ADVAN_DTLS_CODE = " + "'" + advanCode + "'";
      
      return jdbc.update(sql);}
      return result;
   }
   
   private int deleteTK(Object tkCode) {
      String sql = "Delete from ADVANTK where ADVANTK_CODE = " + "'" + tkCode + "'";
      
      return jdbc.update(sql);
      
   }

   // 예매 취소시 환불
   public int updateCash(Object advanCode) {
      String sql ="SELECT S.PC_CODE , P.PC FROM ADVAN_DTLS A, SEAT S, PC_TABLE P "
         + "WHERE  A.SEAT_NO_CODE = S.SEAT_NO_CODE AND S.PC_CODE = P.PC_CODE"
         + " AND A.ADVAN_DTLS_CODE = '"+ advanCode +"'";
      Map<String,Object> row = jdbc.selectOne(sql);
      
      String sql2 = "UPDATE MEMBER SET CASH = CASH + " + row.get("PC") 
               + " WHERE USID = '" + Controller.LoginUser.get("USID") + "'" ; 
            
      return jdbc.update(sql2);
   }
}
