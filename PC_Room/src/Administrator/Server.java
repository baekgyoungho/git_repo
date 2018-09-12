package Administrator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import DB.MemberDAO;
import DB.MemberDTO;
import DB.OrderListDTO;
import DB.SalesDAO;
import DB.User_infoDTO;

public class Server implements Server_i{
	
	ServerSocket serverSocket;
	Socket socket;
	List<Thread> list;
	MemberDAO dao = new MemberDAO();
	SalesDAO sao = new SalesDAO();
	int i = 0;
	
	public Server() {
		list = new ArrayList<Thread>();
		System.out.println("서버가 시작되었습니다.");
	}
	
	public void giveAndTake() {//1
		try {
			serverSocket = new ServerSocket(5420);
			//ServerSocket port 바로 다시사용
			serverSocket.setReuseAddress(true);
			
			while(true) {
				socket = serverSocket.accept();
				ServerSocketThread sst = new ServerSocketThread(this, socket,i);
				i++;
				//리스트객체에 저장
				addClient(sst);
				//쓰레드 시작
				sst.start();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
	public synchronized void addClient(Thread thread) {
		list.add(thread);
	}
	public synchronized void removeClient(Thread thread) {	
		list.remove(thread);
	}
	
	//공용 - Event 보내기
	public synchronized void broadCasting(int event) {
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			sst.send_Event(event);
		}
	}
	
	//Event 1000 - 사용자 자리요청
	public synchronized void response_seatNum(int event,int index) {
		
		int seat_num = 0;
		seat_num = Manage.getInstance().send_seatNum();
		System.out.println("Manage 자리확정");
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage 확정자리 보내주기");
				sst.send_Event(event);
				sst.send_seatNum(seat_num);
			}
		}
	}
	
	//Event 1001 - 사용 중인 자리 목록 받아오기
	public synchronized void response_inuseList(int event, int index) {

		List<Integer> seatList = Manage.getInstance().seat_inUse();
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage 사용 중인 자리 목록  보내주기 : [" + index+ "]");
				sst.send_Event(event);
				sst.send_inuseList(seatList);
			}
		}
	}
	
	//Event 1100 - 중복로그인 확인 요청
	public synchronized void response_MultiCheck(int event,int index, String id) {
		int result = 2;// Default : 2 (미사용)
		for(int i=0;i<Manage.getInstance().seat_id.length;i++) {
			if (Manage.getInstance().seat_id[i].getText().equals(id)) {
				result = 1;
			}
		}
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage 중복로그인 체크 결과 반환");
				sst.send_Event(event);
				sst.send_MultiCheck(result);
			}
		}
		
	}
	//Event 1200 - 사용좌석 증가 요청
	public synchronized void response_seatPlus() {
		Login.currentSeat++;
	}
	//Event 1201 - 사용좌석 감소 요청
	public synchronized void response_seatMinus() {
		Login.currentSeat--;
	}
	//Event 1202 - 사용좌석갯수 요청
	public synchronized void response_getseatCount(int event, int index) {
		int seatCount = 0;
		seatCount = Login.currentSeat;
		System.out.println("Manage seatCount : " + Login.currentSeat);
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage 확정자리 보내주기");
				sst.send_Event(event);
				sst.send_seatCount(seatCount);
			}
		}
	}
	//Event 1203 - 사용좌석이용율 Update
	public synchronized void response_seatUpdate() {
	    Manage.getInstance().seat_update();
	}
	//Event 1300 - 관리자창에 UserInfo Display
	public synchronized void response_updateUserInfo(User_infoDTO user_info) {
		Manage.getInstance().update_userInfo(user_info.getDto(),
				user_info.getSeatNum(),user_info.getRemain_time()); //관리자창에 유저 정보 업데이트
	}
	//Event 1301 - 관리자창에 남은시간 Update
	public synchronized void response_updateUserTime(User_infoDTO user_info) {
		Manage.getInstance().update_time(user_info.getSeatNum(), 
				user_info.getRemain_time(), user_info.getR_time(),user_info.getU_time());
	}
	//Event 1302 - 관리자창에서 사용자 정보 삭제
	public synchronized void response_deleteUserInfo(int seatNum) {
		Manage.getInstance().delete_userInfo(seatNum);
	}
	//Event 1400 - 관리자창 주문내역에 리스트 추가
	public synchronized void response_makeOrderList(OrderListDTO orderList) {
		Manage.getInstance().make_list(orderList);
	}
	
	
	//2000 부터 DB처리
	//Event 2000,2001,2002 - Login , Id찾기, Pw 찾기
	public synchronized void response_SelectResult(int event, int index, MemberDTO dto) {
		MemberDTO result_dto = null;
		
		if(event == 2000) result_dto=dao.login(dto.getId(), dto.getPw());
		else if(event == 2001) {
			result_dto = new MemberDTO();
			result_dto.setId(dao.find_id(dto.getName(), dto.getPhone()));
			
		}else if(event == 2002) {
			result_dto = new MemberDTO();
			result_dto.setPw(dao.find_pw(dto.getId(), dto.getPhone()));
			
		}
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB 검색 결과 처리 완료");
				sst.send_Event(event);
				sst.send_Dto(result_dto);
			}
		}
	}
	//Event 2003 - ID 중복 Check
	public synchronized void response_idCheck(int event, int index, MemberDTO dto) {
		int result = 0;
		
		result=dao.check_id(dto.getId());//0이 사용가능 1이 중복
		System.out.println("server"+dto.getId());
		System.out.println(result);
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB 검색 결과 처리 완료");
				sst.send_Event(event);
				sst.send_CheckResult(result);
			}
		}
	}
	//Event 2100 - 회원가입
	public synchronized void response_userJoin(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.insert(dto.getId(), dto.getPw(), dto.getName(), dto.getPhone());//0이 사용가능 1이 중복
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB 검색 결과 처리 완료");
				sst.send_Event(event);
				sst.send_JoinResult(result);
			}
		}
	}
	
	//Event 2200 - 회원정보수정
	public synchronized void response_userUpdate(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.update(dto.getId(),dto.getPw(), dto.getPhone());
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB 회원정보수정 완료");
				sst.send_Event(event);
				sst.send_editResult(result);
			}
		}
	}
	
	//Event 2201 - 사용종료 회원정보수정
	public synchronized void response_SaveAll(int event, int index, User_infoDTO dto) {
		
		String id = "";
		int rtime = 0;
		int utime = 0;
		
		id = dto.getDto().getId();
		rtime = dto.getR_time();
		utime = dto.getU_time();
		
		System.out.println("id : "+ id +"rtime : "+rtime+"utime : "+utime);
		dao.getConnection1();
		dao.save_all(id, rtime, utime);
		dao.close();

	}
	//Event 2202 - 상품구매시 회원 누적금액사용량 Update
	public synchronized void response_totMoney(int event, int index, MemberDTO dto, int price_sum) {
		dao.update_totMoney(dto.getId(),price_sum);
	}
	
	
	
	
	//Event 2300 - 회원정보삭제
	public synchronized void response_userDelete(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.delete(dto.getId(), dto.getPw());
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB 회원정보수정 삭제 완료");
				sst.send_Event(event);
				sst.send_removeResult(result);
			}
		}
	}
	
	//Event 2500 - Sales 금액 update
	public synchronized void response_salesUpdate(int event, int index, int code, int money) {
		sao.update(code, money);
	}
	
	
	
	
	
	
	

	
	
}
