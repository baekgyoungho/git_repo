package Administrator;

import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;

public interface Server_i {

	// 소켓연결
	public void giveAndTake();
	
	// 사용자접속
	public void addClient(Thread thread);
	
	// 사용자퇴장
	public void removeClient(Thread thread);
	
	//공용 - Event 보내기
	public void broadCasting(int event); 
	
	public void response_seatNum(int event,int index); 						//Event 1000 - 사용자 자리요청
	public void response_inuseList(int event, int index); 					//Event 1001 - 사용 중인 자리 목록 받아오기
	public void response_MultiCheck(int event,int index, String id); 		//Event 1100 - 중복로그인 확인 요청
	public void response_seatPlus() ;										//Event 1200 - 사용좌석 증가 요청
	public void response_seatMinus();										//Event 1201 - 사용좌석 감소 요청
	public void response_getseatCount(int event, int index);				//Event 1202 - 사용좌석갯수 요청
	public void response_seatUpdate();										//Event 1203 - 사용좌석이용율 Update
	public void response_updateUserInfo(User_infoDTO user_info);			//Event 1300 - 관리자창에 UserInfo Display
	public void response_updateUserTime(User_infoDTO user_info);			//Event 1301 - 관리자창에 남은시간 Update
	public void response_deleteUserInfo(int seatNum);						//Event 1302 - 관리자창에서 사용자 정보 삭제
	public void response_makeOrderList(OrderListDTO orderList);				//Event 1400 - 관리자창 주문내역에 리스트 추가
	
	//2000 부터 DB처리
	public void response_SelectResult(int event, int index, MemberDTO dto); 			//Event 2000,2001,2002 - Login , Id찾기, Pw 찾기
	public void response_idCheck(int event, int index, MemberDTO dto);					//Event 2003 - ID 중복 Check
	public void response_userJoin(int event, int index, MemberDTO dto);					//Event 2100 - 회원가입
	public void response_userUpdate(int event, int index, MemberDTO dto);				//Event 2200 - 회원정보수정
	public void response_SaveAll(int event, int index, User_infoDTO dto);				//Event 2201 - 사용종료 회원정보수정
	public void response_totMoney(int event, int index, MemberDTO dto, int price_sum);	//Event 2202 - 상품구매시 회원 누적금액사용량 Update
	public void response_userDelete(int event, int index, MemberDTO dto);				//Event 2300 - 회원정보삭제
	public void response_salesUpdate(int event, int index, int code, int money);		//Event 2500 - Sales 금액 update
	
}
