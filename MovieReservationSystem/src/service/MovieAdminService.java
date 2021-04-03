package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MovieAdminDao;

public class MovieAdminService {
	
	private MovieAdminService(){}
	
	private static MovieAdminService instance; 
	
	public static MovieAdminService getInstance(){
		if(instance == null){
			instance = new MovieAdminService();
		}
		return instance;
	}
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat Format = new SimpleDateFormat("MM-dd");
	Date nowDate = new Date();
	String nowDateStr = dateFormat.format(nowDate);
	
	int input;
	 
	List<Map<String,Object>> list = new ArrayList<>();
	
	private MovieAdminDao movieAdminDao = MovieAdminDao.getInstance();
	
	
	//영화관리자 선택
	public int movieAdmin() {
		System.out.println("============= Movie Admin Page =============");
		System.out.println("🙌1.상영중 영화 관리          2.상영리스트 관리          0.로그아웃🙌");
		System.out.println("============================================");
		System.out.print(">");
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1 : return View.MOVIEMANAGEMENT;
		case 2 : return View.MOVIESCREEN;
		case 0 : Controller.LoginUser = null; 
				return View.HOME;
		}
		
			System.out.println("❌잘못 선택하셨습니다❌");
			return View.MOVIEADMIN;
		
	}

	//영화정보조작
	public int selectMovieManage() {
		
			showMovie();
		
			System.out.println("🙌1.등록          2.삭제          0.메인화면🙌");
			System.out.print("수행 할 작업을 선택하세요>");
			input = ScanUtil.nextInt();
			
			switch(input){
			case 1 : MovieInsert(); return View.MOVIEMANAGEMENT;
			
			case 2 : MovieDelete(); return View.MOVIEMANAGEMENT;
			
			case 0 : break;
			}
			
			
		return View.MOVIEADMIN;
	}
	


	//영화 목록
	private void showMovie() {
		list = movieAdminDao.showMovie();
		int squence = 1;	
			System.out.printf("%s\t|%s\t|%-40s\t|%-10s\t|%-20s\t|%s","번호","영화코드","영화제목","개봉일자","관람등급","감독명");
			System.out.println();
			System.out.println("===========================================================================================");
			for(int i = 0; i < list.size(); i++){
				String code = (String)list.get(i).get("영화코드");
				String title = (String)list.get(i).get("영화제목");
				String date = (String)list.get(i).get("개봉일자");
				String grade = (String)list.get(i).get("관람등급");
				String diector = (String)list.get(i).get("감독명");
				
				System.out.printf("%d\t|" , squence);
				System.out.printf("%s\t|" , code);
				System.out.printf("%s\t|" , title);
				System.out.printf("%s\t|" , date);
				System.out.printf("%s\t|" , grade);
				System.out.printf("%s\t" , diector);
				if(i < list.size() - 1)
				System.out.println("\n-----------------------------------------------------------------------------------");
				squence++;
			}
			System.out.println("\n===========================================================================================");
					
	}


	//영화 등록
	private void MovieInsert() {
		
		System.out.print("🎞영화제목>");
		String title = ScanUtil.nextLine();
		System.out.print("🎞개봉일자>");
		String date = ScanUtil.nextLine();
		System.out.print("👁‍🗨관람등급>");
		String grade = ScanUtil.nextLine();
		System.out.print("🎬감독>");
		String director = ScanUtil.nextLine();
		
		List<String> insertMovieInfo = new ArrayList<>();
		insertMovieInfo.add(title);
		insertMovieInfo.add(date);
		insertMovieInfo.add(grade);
		insertMovieInfo.add(director);
		int result = movieAdminDao.movieInsertDao(insertMovieInfo);
		
		if(result > 0){
			System.out.println("💡등록되었습니다💡");
		}else{
			System.out.println("❌등록되지 않았습니다❌");
		}
		
	}
	
	
	//영화 삭제
	private void MovieDelete() {
		System.out.print("❗ 삭제하고자 하는 영화의 코드를 입력하세요>");
		String movieCode = ScanUtil.nextLine();
		int result = movieAdminDao.movieDeleteDao(movieCode);
		
		if(result > 0){
			System.out.println("🗑삭제되었습니다🗑");
		}else{
			System.out.println("❌입력하신 코드의 영화는 존재하지 않습니다❌");
		}
	}

	
	//상영정보조작
	public int selectScreenManage() {
		
		System.out.print("🎞1.날짜가 지난 상영정보 자동 삭제      2.상영정보 별도 조작 및 등록     0.메인화면>");
		input = ScanUtil.nextInt();
	
		if(input == 1){
			int result = beforeTimeDelete();
				if(result != View.MOVIEADMIN)
				System.out.println("상영기간이 지난 " + result + "개의 상영정보를 삭제했습니다💥💥");
		}else if(input == 2){
			String city = selectCity();
			if(city == null){
				return View.MOVIEADMIN;
			}
			String cinemaCode = selectCinema(city);
			List<Map<String,Object>> list2= showScreen(cinemaCode);
			
			System.out.println("🙌1.해당 영화관 상영정보 등록     2.해당 상영관 상영정보 삭제🙌");
			System.out.print(">");
			input = ScanUtil.nextInt();
			
			switch(input){
			case 1 : screenInsert(cinemaCode); break;
			case 2 : screenDelete(list2); break;
			}
		}
	
		return View.MOVIEADMIN;
	}
		
	
	private int beforeTimeDelete() {
		
		System.out.print("✏관리자 계정의 비밀번호를 입력하여 주세요>");
		String password = ScanUtil.nextLine();
		String checkPass = (String)Controller.LoginUser.get("PASSWORD");
		
		if(!password.equals(checkPass)){
			System.out.println("❌비밀번호가 틀렸습니다 메인페이지로 넘어갑니다❌");
			return View.MOVIEADMIN;
		}
		
		list = new ArrayList<>(); 
		list = movieAdminDao.getAllScreenDao();
		int resultSum = 0;
		
		List<Object> deleteList = new ArrayList<>();
		for(int i = 0; i < list.size(); i++){
			String movieDate = dateFormat.format(list.get(i).get("상영날짜"));
			try {
				Date movie = dateFormat.parse(movieDate);
				Date NowTime = dateFormat.parse(nowDateStr);
				
				 long result = movie.getTime() - NowTime.getTime();
				 if(result < 0){
					 deleteList.add(list.get(i).get("상영코드"));
				 }
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < deleteList.size(); i++){
			int result = movieAdminDao.beforeDeleteScreen(deleteList.get(i));
			resultSum += result;
		}
		return resultSum;
		
	}

	//상영정보삭제
	private void screenDelete(List<Map<String, Object>> list2) {
		System.out.print("🗑삭제를 원하는 정보의 번호 입력>");
		input = ScanUtil.nextInt();
		
		String screenCode = (String)list2.get(input - 1).get("상영코드");
		int result = movieAdminDao.screenDeleteDao(screenCode);
		
		if(result > 0){
			System.out.println("🗑삭제 되었습니다\n");
		}else{
			System.out.println("❌삭제 되지 않았습니다❌");
		}
	}

	//상영정보 등록
	private void screenInsert(String cinemaCode) {
		list = new ArrayList<>();
		list = movieAdminDao.showMovieCode();
		System.out.println("영화코드\t|영화제목");
		System.out.println("=================================================");
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i).get("영화코드") + "\t|" + list.get(i).get("영화제목"));
		}
		System.out.println("=================================================");
		System.out.print("🎬영화코드 입력>");
		String movieCode = ScanUtil.nextLine();
		System.out.print("🕒날짜입력>");
		String date = ScanUtil.nextLine();
		System.out.print("🕒상영시간 입력>");
		String time = ScanUtil.nextLine();
		
		list = movieAdminDao.showTheat(cinemaCode);
		
		System.out.println("====상영관====");
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i).get("THEAT_NM"));
		}
		System.out.println("=============");
		System.out.print("🎥상영관 입력(숫자만 입력)>");
		String theat = ScanUtil.nextLine();
		theat = cinemaCode + theat;
		
		List<Object> screen = new ArrayList<>();
		screen.add(movieCode);
		screen.add(time);
		screen.add(date);
		screen.add(cinemaCode);
		screen.add(theat);
		
		int result = movieAdminDao.screenInsert(screen);
		if(result > 0){
			System.out.println("📽 등록되었습니다 📽");
		}else{
			System.out.println("❌ 등록 되지 않았습니다 ❌");
		}
		
	}

	//상영정보 표시
	private List<Map<String,Object>> showScreen(String cinemaCode) {
		list = new ArrayList<>();
		list = movieAdminDao.showScreen(cinemaCode);
		int sequence = 1;
		Map<String, Object> cinema = movieAdminDao.showCinema(cinemaCode);
		String cinemaInfo = (String)cinema.get("CINEMA_NM");
		
		System.out.println("*************************************"+ cinemaInfo + "*************************************");
		System.out.printf("%s\t|%-50s\t|%s\t|%s\t|%s","번호", "영화제목", "상영날짜", "상영시간","상영관");
		System.out.println();
		System.out.println("===============================================================");
		for(int i = 0; i < list.size(); i++){
			String title = (String)list.get(i).get("영화제목");
			String date = Format.format(list.get(i).get("상영날짜"));
			String time = (String)list.get(i).get("상영시간");
			String theat = (String)list.get(i).get("상영관");
			
			System.out.printf("%d\t|" ,sequence);
			System.out.printf("%s\t|" , title);
			System.out.printf("%s\t|" , date);
			System.out.printf("%s\t|" , time);
			System.out.printf("%s" , theat + "관");
			System.out.println();
			sequence++;
		}
		System.out.println("===============================================================");
		return list;
	}

	//상영시간표 조회할 지역 선택
	private String selectCity() {
		String city;
		list = new ArrayList<>();
		list = movieAdminDao.selectCity();
		System.out.println("\n🎞 원하는 상영정보 지역에 해당하는 번호를 선택하세요(이전으로 돌아가려면 0번 입력)");
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
			input = ScanUtil.nextInt();
			if(input >= sequence){
				System.out.println("❓ 해당하지 않는 번호 입니다 ❓");
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
	
	//상영조회 영화관 선택
		private String selectCinema(String city) {
			list = new ArrayList<>();
			list = movieAdminDao.selectCinema(city);
			System.out.println("***🎥 영화관의 번호를 입력하세요 🎥***");
			System.out.println("=============================");
			System.out.println("번호\t|영화관");
			System.out.println("-----------------------------");
			for(int i = 0; i < list.size(); i++){
				System.out.println((i + 1) + "\t|" + list.get(i).get("CINEMA_NM"));
			}
			System.out.println("==============================");
			System.out.print("입력 > ");
			input = ScanUtil.nextInt();
			String cinemaCode = (String)list.get(input - 1).get("CINEMA_CODE");
			return cinemaCode;
		}

}
