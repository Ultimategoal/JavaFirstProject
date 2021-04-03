package service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.ReservationDao;

public class ReservationService {

	private ReservationService(){}
	private static ReservationService instance;
	List<Map<String,Object>> list;
	SimpleDateFormat format = new SimpleDateFormat("MM-dd");
	SimpleDateFormat RemoveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	Date nowTime = new Date();
	//현재시간을 원하는 데이터 포맷으로 변경하기 위해 String 타입으로 변환
	String nowTimeStr = RemoveFormat.format(nowTime);

	
	int input;

	
	
	public static ReservationService getInstance(){
		if(instance == null){
			instance = new ReservationService();
		}
		return instance;
	}
	
	private ReservationDao reservationDao = ReservationDao.getInstance();



	public int reservation() {
		
		String city = selectCity();
		if(city == null){
			return View.MAIN_HOME;
		}
		String cinemaCode = selectCinema(city);
		Map<String,Object> movieInfo = selectMovie(cinemaCode);
		String screenCode = (String)movieInfo.get("screenCode");
		String theatCode = (String)movieInfo.get("theatCode");
		String selectSeat = theatCode + "_" + selectSeat(screenCode);
		int result = payment(selectSeat);
		
		if(result == 1){
		ticketing(screenCode,selectSeat);
		
		}else{
			System.out.println("예매가 취소되었습니다");
		}
		return View.MAIN_HOME;
	}
	
	


	//지역선택
	private String selectCity() {
		String city;
		list = new ArrayList<>();
		list = reservationDao.cinemaOfCityDao();
		System.out.println("원하는 지역을 선택하세요(이전으로 돌아가려면 0번 입력)");
		int sequence = 1;
		System.out.println("=============================");
		System.out.println("번호\t|지역");
		System.out.println("-----------------------------");
		for(int i = 0; i < list.size(); i++){
			System.out.println(sequence + "\t|" + list.get(i).get("지역"));
			sequence++;
		}
		System.out.println("=============================");
		while(true){
			System.out.print("입력 > ");
			System.out.println();
			input = ScanUtil.nextInt();
			if(input > sequence){
				System.out.println("해당하지 않는 번호 입니다");
				continue;
			}else{
				break;
			}
		}
		if(input == 0){
			city = null;
		}else{
			city = (String)list.get(input - 1).get("지역");
		}
		return city;
	}


		//영화관 선택
		private String selectCinema(String city) {
			System.out.println("원하는 영화관을 선택하여 주세요");
			System.out.print("입력 > ");
			list = new ArrayList<>();
			list = reservationDao.showCinema(city);
			System.out.println("=============================");
			System.out.println("번호\t|영화관");
			System.out.println("-----------------------------");
			for(int i = 0; i < list.size(); i++){
				System.out.println((i + 1) + "\t|" + list.get(i).get("CINEMA_NM"));
			}
			System.out.println("=============================");
			System.out.print("입력 > ");
			input = ScanUtil.nextInt();
			String cinemaCode = (String)list.get(input - 1).get("CINEMA_CODE");
			return cinemaCode;
		}

	
	

	//영화 및 영화 시간 선택
	private Map<String, Object> selectMovie(String cinemaCode) {
		
		list = new ArrayList<>();
		list = reservationDao.screen(cinemaCode);
		
		Map<String,Object> cinema = new HashMap<>();
		cinema = reservationDao.cinemaDao(cinemaCode);
		String cinemaName = (String)cinema.get("CINEMA_NM");
		
		
		//서비스 이용시 해당 시간보다 지난 영화 상영 리스트 제외
		for(int i = 0; i < list.size(); i++){
			
		//영화시간을 원하는 포맷으로 받기 위해 String타입으로 저장
		String Time = RemoveFormat.format(list.get(i).get("상영날짜")) +" " + (String)list.get(i).get("상영시간") + ":00";
		try {
			//영화시간
			Date movieTime = RemoveFormat.parse(Time);
			//현재시간
			Date NowTime = RemoveFormat.parse(nowTimeStr);
			
		    long result = movieTime.getTime() - NowTime.getTime();
	
		    if(result < 0){
		    	list.remove(i);
		    }
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		}
		
		
		System.out.println("*************************************"+ cinemaName + "*************************************");
		System.out.printf("%s\t|%-40s\t|%s\t|%s\t|%s\t|%s", "번호", "영화제목", "상영날짜", "상영시간", "상영관", "남은좌석");
		System.out.println();
		System.out.println("====================================================================================");
		int squence = 1;
		for(int i = 0; i < list.size(); i++){
			String movieTitle = (String)list.get(i).get("영화제목");
			String movieDate = format.format(list.get(i).get("상영날짜"));
			String movieTime = (String)list.get(i).get("상영시간");
			String theat = (String)list.get(i).get("상영관") + "관";
			int seat = ((BigDecimal)list.get(i).get("남은좌석수")).intValue();
			int seatTotal = ((BigDecimal)list.get(i).get("총좌석수")).intValue();
			System.out.printf("%s\t|", squence);
			System.out.printf("%s\t|", movieTitle);
			System.out.printf("%s\t|", movieDate);
			System.out.printf("%s\t|", movieTime);
			System.out.printf("%s\t|", theat);
			System.out.printf("%s/%s", seat, seatTotal);
			if(i < list.size() - 1)
			System.out.println("\n------------------------------------------------------------------------------------");
			squence++;
		}
		System.out.println("\n====================================================================================");
		System.out.print("🕓 원하시는 영화와 시간대를 선택하세요 > ");
		input = ScanUtil.nextInt();
		
		//상영관 코드
		String theatCode = (String)cinemaCode + list.get(input - 1).get("상영관");
		//상영시간표 코드
		Object screenCode = list.get(input - 1).get("상영코드");
		
		Map<String,Object> movieInfo = new HashMap<String, Object>();
		movieInfo.put("theatCode", theatCode);
		movieInfo.put("screenCode", screenCode);
		return movieInfo;
		
	}

		//좌석선택
		private String selectSeat(String screenCode) {
			
			List<Map<String, Object>> seat = reservationDao.seatDao(screenCode);
			
			//해당 상영관의 좌석 정보를 얻어와 행 열의 크기 구하는 코드
			int rowMax = 0;
			char columnMax = 0;
			for(int i = 0; i < seat.size(); i++){
				String rowcheck = String.valueOf(seat.get(i).get("행")).trim();
				if(rowMax < Integer.parseInt(rowcheck)){
					rowMax = Integer.parseInt(rowcheck);
				}
				
				String check = (String)seat.get(i).get("열");
				if(columnMax < check.charAt(0)){
					columnMax = check.charAt(0);
				}
			}
			int rowSize = rowMax;
			int columnSize = columnMax - 'A' + 1;
			
			String[][] nowSeat = new String[rowSize][columnSize];
			
			
			System.out.println("========================================================");
			System.out.println("                 S   C   R   E   E   N                  ");
			System.out.println("========================================================");
			
			
			char cs = 'A';
			int rs = 1;
			System.out.print("\t");
			for(int i = 0; i < columnSize; i++){
				System.out.print(cs + "\t");
				cs++;
			}
			System.out.println();
			
			int cnt = 0;
			//예매가 되어있는 자리는 색깔로 표시
			for(int i = 0; i < rowSize; i++){
				System.out.print(rs + "\t");
				for(int j = 0; j < columnSize; j++){
					String at = (String)seat.get(cnt).get("예매여부");
					if(at.equals("N")){
						nowSeat[i][j] = "□";
					}else if(at.equals("Y")){
						nowSeat[i][j] = "■";
					}
						System.out.print(nowSeat[i][j] + "\t");
					cnt++;
				}
				rs++;
				System.out.println();
			}
			System.out.println("========================================================");
			String selectSeat;
			
			while(true){
				boolean flag = false;
				
				System.out.print("\n원하는 좌석을 선택하여 주세요[ex)A1,B10] > ");
				selectSeat = ScanUtil.nextLine();
				
				//입력한 좌석번호 존재여부 체크
				int inputRow = Integer.parseInt(selectSeat.substring(1));
				char inputColumn = selectSeat.charAt(0);
				
				if(inputRow < 0 || inputRow > rowMax || inputColumn < 'A' || inputColumn > columnMax){
					System.out.println("❌ 해당 좌석번호는 존재하지 않습니다 ❌");
					continue;
				}
				
				
				
				System.out.println("========================================================");
				//이미 예매된 좌석 여부 체크
				for(int i = 0; i < seat.size(); i++){

					String checkSeat = (String)seat.get(i).get("좌석");
					String checkAT = (String)seat.get(i).get("예매여부");
					if(checkSeat.equals(selectSeat) && checkAT.equals("Y")){
						System.out.println("이미 예매된 좌석입니다. 다시 선택하여 주세요\n");
						break;
					}else if(checkSeat.equals(selectSeat) && checkAT.equals("N")){
						flag = true;
					}
						
					
				}
				
				if(flag){
					break;
				}
			}	
			
			return selectSeat;
		
		}
	
	

	//예매
		private void ticketing(Object screenCode, String selectSeat) {
			reservationDao.insertAtTicketDao(screenCode, selectSeat);
			
			//기정 : 여기부터
	         List<Map<String,Object>> advanlist = reservationDao.selectAdvOne(); 
	         int resultTransfer = reservationDao.transferData(advanlist);
	         
	            if(resultTransfer > 0){
	               System.out.println("👍 예매가 완료 되었습니다.");
	            }else {
	               System.out.println("❌ 예매가 취소되었습니다. 관리자에게 문의해주세요 > 삭제 에러");
	            }
	            
				
				
				
		}

	//결제
		private int payment(String selectSeat) {
			Map<String, Object> price = new HashMap<>();
			price = reservationDao.priceDao(selectSeat);
			System.out.println("**********************************************************");
			System.out.println("🎬 선택하신 좌석의 등급은 " + price.get("가격코드") + " 이며  가격은 " + price.get("가격") + "원 입니다");
			System.out.println("**********************************************************");
			
			
			Map<String, Object> myCash = new HashMap<>();
			
			myCash = reservationDao.myCashDao();
			int payPrice = ((BigDecimal)price.get("가격")).intValue();
			int myPayCash = ((BigDecimal)myCash.get("CASH")).intValue();
			
			if(myPayCash < payPrice){
				System.out.println("💲 현재 보유캐쉬는 "+ myPayCash + "원 입니다. 충전 후 예매를 재진행하여 주세요");
				return 2;
			}
			System.out.println("💲 현재 보유 캐쉬 : " + myPayCash);
			System.out.println("💲 결제하시겠습니까?(1.Yes 2.No) > ");
			System.out.print("입력 > ");
			
			input = ScanUtil.nextInt();
			if(input == 1){
				if(myPayCash < payPrice){
					System.out.println("💲 보유 캐쉬가 부족합니다. 충전 후 예매를 재진행하여 주세요");
					return 2;
				}else{
					int cnt = 0;
					while(true){
						cnt++;
						if(cnt == 4){
							System.out.println("❌ 입력 3회 오류되었습니다. 메인화면으로 이동합니다 ❌");
							return 2;
						}
						System.out.print("▶ 비밀번호 입력 > ");
						String password = ScanUtil.nextLine();
						
						String check = (String)Controller.LoginUser.get("PASSWORD");
						
						if(!password.equals(check)){
							System.out.println("❌ 입력하신 비밀번호가 올바르지 않습니다 ❌");
						}else{
							break;
						}
					}
					int result = reservationDao.payDao(payPrice);
					return 1;
				}
				
			}else{
				return 2;
			}
			
			
		}



	
	
	
	
}


