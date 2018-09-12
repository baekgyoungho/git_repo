package Administrator;

import java.util.List;

import javax.swing.JPanel;

import DB.MemberDTO;
import DB.OrderListDTO;

public interface Manage_i {

	    // 이용률 레이아웃 구성 함수
		public void seatPercent(JPanel panel);	
		
		// 사용자 정보 출력 레이아웃 함수
		public void setUserInfo(JPanel panel);	
		
		// 자리 이용률 업데이트 함수
		public void seat_update();	
		
		// 자리 레이아웃에 사용자 정보 출력 함수
		public void update_userInfo(MemberDTO dto, int seatNum, String time);
		
		// 자리 레이아웃에 남은 시간 업데이트
		public void update_time(int seatNum, String time, int rtime, int utime);
		
		// 로그아웃 후 자리 레이아웃에서 사용자 정보 삭제
		public void delete_userInfo(int seatNum);
		
		// 로그아웃한 사용자의 정보가 사용자 정보창에 출력되어 있을 경우 내용 삭제
		public void delete_userText(int seatNum);
		
		// 사용자 자리 변경 시 현재 사용 중인 자리 전달
		public List<Integer> seat_inUse();
		
		// 남은 좌석중 첫번째 좌석 리턴
		public int send_seatNum();
		
		// 주문목록 List 작성
		public void make_list(OrderListDTO dto);
		
		
}
