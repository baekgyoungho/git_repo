package Client;

import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;

public interface Client_i {
	
	public void request_seatNum();											//event 1000 : ����� �ڸ����� - ���ڸ� �޾ƿ���
	public void request_seatList();											//event 1001  ��� ���� �ڸ� ��� �޾ƿ���
	public void request_Multiple(MemberDTO dto); 							//event 1100  : ����� �ߺ��α��� ����
	public void request_seatPlus();											//event 1200 : ����¼� ���� ��û
	public void request_seatMinus();										//event 1201 : ����¼� ���� ��û
	public void request_getseatCount();										//event 1202 : ����¼����� ��û
	public void request_seatUpdate();										//event 1203 : �¼��̿��� Update
	public void request_update_UserInfo(int event, User_infoDTO user_info); //event 1300,1301 : User����, UserTime����
	public void request_deleteUserInfo(int seatNum);						//event 1302 ������â���� ����� ���� ����
	public void request_makeList(OrderListDTO order_list);					//event 1400 ������ȭ�� �ֹ������� ����Ʈ �߰�

    //2000���� DB
	public void request_login(MemberDTO dto) ;								//event 2000 Login��� ó��
	public void request_findId(MemberDTO dto);								//event 2001 Id ã�� ���
	public void request_findPw(MemberDTO dto);								//event 2002 Pw ã�� ���
	public void request_checkId(MemberDTO dto);								//event 2003  ID �ߺ� check ���
	public void request_Join(MemberDTO dto);								//event 2100  ȸ������ ���
	public void request_update(MemberDTO dto);								//event 2200  ȸ������ ����
	public void request_saveAll(User_infoDTO dto);							//event 2201 ����� ȸ������ Update
	public void request_totUpdate(MemberDTO dto,int price_sum);				//event 2202 ��ǰ���Ž� ȸ�� �ݾ� ������뷮 Update
	public void request_delete(MemberDTO dto);								//event 2300  ȸ��Ż��
	public void request_salesUpdate(int code, int money);					//event 2500 sales DBUpdate

}
