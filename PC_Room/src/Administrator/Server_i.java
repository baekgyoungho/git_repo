package Administrator;

import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;

public interface Server_i {

	// ���Ͽ���
	public void giveAndTake();
	
	// ���������
	public void addClient(Thread thread);
	
	// ���������
	public void removeClient(Thread thread);
	
	//���� - Event ������
	public void broadCasting(int event); 
	
	public void response_seatNum(int event,int index); 						//Event 1000 - ����� �ڸ���û
	public void response_inuseList(int event, int index); 					//Event 1001 - ��� ���� �ڸ� ��� �޾ƿ���
	public void response_MultiCheck(int event,int index, String id); 		//Event 1100 - �ߺ��α��� Ȯ�� ��û
	public void response_seatPlus() ;										//Event 1200 - ����¼� ���� ��û
	public void response_seatMinus();										//Event 1201 - ����¼� ���� ��û
	public void response_getseatCount(int event, int index);				//Event 1202 - ����¼����� ��û
	public void response_seatUpdate();										//Event 1203 - ����¼��̿��� Update
	public void response_updateUserInfo(User_infoDTO user_info);			//Event 1300 - ������â�� UserInfo Display
	public void response_updateUserTime(User_infoDTO user_info);			//Event 1301 - ������â�� �����ð� Update
	public void response_deleteUserInfo(int seatNum);						//Event 1302 - ������â���� ����� ���� ����
	public void response_makeOrderList(OrderListDTO orderList);				//Event 1400 - ������â �ֹ������� ����Ʈ �߰�
	
	//2000 ���� DBó��
	public void response_SelectResult(int event, int index, MemberDTO dto); 			//Event 2000,2001,2002 - Login , Idã��, Pw ã��
	public void response_idCheck(int event, int index, MemberDTO dto);					//Event 2003 - ID �ߺ� Check
	public void response_userJoin(int event, int index, MemberDTO dto);					//Event 2100 - ȸ������
	public void response_userUpdate(int event, int index, MemberDTO dto);				//Event 2200 - ȸ����������
	public void response_SaveAll(int event, int index, User_infoDTO dto);				//Event 2201 - ������� ȸ����������
	public void response_totMoney(int event, int index, MemberDTO dto, int price_sum);	//Event 2202 - ��ǰ���Ž� ȸ�� �����ݾ׻�뷮 Update
	public void response_userDelete(int event, int index, MemberDTO dto);				//Event 2300 - ȸ����������
	public void response_salesUpdate(int event, int index, int code, int money);		//Event 2500 - Sales �ݾ� update
	
}
