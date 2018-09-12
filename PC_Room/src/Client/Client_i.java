package Client;

import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;

public interface Client_i {
	
	public void request_seatNum();											//event 1000 : 사용자 자리배정 - 빈자리 받아오기
	public void request_seatList();											//event 1001  사용 중인 자리 목록 받아오기
	public void request_Multiple(MemberDTO dto); 							//event 1100  : 사용자 중복로그인 방지
	public void request_seatPlus();											//event 1200 : 사용좌석 증가 요청
	public void request_seatMinus();										//event 1201 : 사용좌석 감소 요청
	public void request_getseatCount();										//event 1202 : 사용좌석갯수 요청
	public void request_seatUpdate();										//event 1203 : 좌석이용율 Update
	public void request_update_UserInfo(int event, User_infoDTO user_info); //event 1300,1301 : User정보, UserTime정보
	public void request_deleteUserInfo(int seatNum);						//event 1302 관리자창에서 사용자 정보 삭제
	public void request_makeList(OrderListDTO order_list);					//event 1400 관리자화면 주문내역에 리스트 추가

    //2000부터 DB
	public void request_login(MemberDTO dto) ;								//event 2000 Login기능 처리
	public void request_findId(MemberDTO dto);								//event 2001 Id 찾기 기능
	public void request_findPw(MemberDTO dto);								//event 2002 Pw 찾기 기능
	public void request_checkId(MemberDTO dto);								//event 2003  ID 중복 check 기능
	public void request_Join(MemberDTO dto);								//event 2100  회원가입 기능
	public void request_update(MemberDTO dto);								//event 2200  회원정보 수정
	public void request_saveAll(User_infoDTO dto);							//event 2201 종료시 회원정보 Update
	public void request_totUpdate(MemberDTO dto,int price_sum);				//event 2202 상품구매시 회원 금액 누적사용량 Update
	public void request_delete(MemberDTO dto);								//event 2300  회원탈퇴
	public void request_salesUpdate(int code, int money);					//event 2500 sales DBUpdate

}
