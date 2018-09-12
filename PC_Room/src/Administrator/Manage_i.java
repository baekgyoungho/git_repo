package Administrator;

import java.util.List;

import javax.swing.JPanel;

import DB.MemberDTO;
import DB.OrderListDTO;

public interface Manage_i {

	    // �̿�� ���̾ƿ� ���� �Լ�
		public void seatPercent(JPanel panel);	
		
		// ����� ���� ��� ���̾ƿ� �Լ�
		public void setUserInfo(JPanel panel);	
		
		// �ڸ� �̿�� ������Ʈ �Լ�
		public void seat_update();	
		
		// �ڸ� ���̾ƿ��� ����� ���� ��� �Լ�
		public void update_userInfo(MemberDTO dto, int seatNum, String time);
		
		// �ڸ� ���̾ƿ��� ���� �ð� ������Ʈ
		public void update_time(int seatNum, String time, int rtime, int utime);
		
		// �α׾ƿ� �� �ڸ� ���̾ƿ����� ����� ���� ����
		public void delete_userInfo(int seatNum);
		
		// �α׾ƿ��� ������� ������ ����� ����â�� ��µǾ� ���� ��� ���� ����
		public void delete_userText(int seatNum);
		
		// ����� �ڸ� ���� �� ���� ��� ���� �ڸ� ����
		public List<Integer> seat_inUse();
		
		// ���� �¼��� ù��° �¼� ����
		public int send_seatNum();
		
		// �ֹ���� List �ۼ�
		public void make_list(OrderListDTO dto);
		
		
}
