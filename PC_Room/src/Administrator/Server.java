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
		System.out.println("������ ���۵Ǿ����ϴ�.");
	}
	
	public void giveAndTake() {//1
		try {
			serverSocket = new ServerSocket(5420);
			//ServerSocket port �ٷ� �ٽû��
			serverSocket.setReuseAddress(true);
			
			while(true) {
				socket = serverSocket.accept();
				ServerSocketThread sst = new ServerSocketThread(this, socket,i);
				i++;
				//����Ʈ��ü�� ����
				addClient(sst);
				//������ ����
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
	
	//���� - Event ������
	public synchronized void broadCasting(int event) {
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			sst.send_Event(event);
		}
	}
	
	//Event 1000 - ����� �ڸ���û
	public synchronized void response_seatNum(int event,int index) {
		
		int seat_num = 0;
		seat_num = Manage.getInstance().send_seatNum();
		System.out.println("Manage �ڸ�Ȯ��");
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage Ȯ���ڸ� �����ֱ�");
				sst.send_Event(event);
				sst.send_seatNum(seat_num);
			}
		}
	}
	
	//Event 1001 - ��� ���� �ڸ� ��� �޾ƿ���
	public synchronized void response_inuseList(int event, int index) {

		List<Integer> seatList = Manage.getInstance().seat_inUse();
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage ��� ���� �ڸ� ���  �����ֱ� : [" + index+ "]");
				sst.send_Event(event);
				sst.send_inuseList(seatList);
			}
		}
	}
	
	//Event 1100 - �ߺ��α��� Ȯ�� ��û
	public synchronized void response_MultiCheck(int event,int index, String id) {
		int result = 2;// Default : 2 (�̻��)
		for(int i=0;i<Manage.getInstance().seat_id.length;i++) {
			if (Manage.getInstance().seat_id[i].getText().equals(id)) {
				result = 1;
			}
		}
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage �ߺ��α��� üũ ��� ��ȯ");
				sst.send_Event(event);
				sst.send_MultiCheck(result);
			}
		}
		
	}
	//Event 1200 - ����¼� ���� ��û
	public synchronized void response_seatPlus() {
		Login.currentSeat++;
	}
	//Event 1201 - ����¼� ���� ��û
	public synchronized void response_seatMinus() {
		Login.currentSeat--;
	}
	//Event 1202 - ����¼����� ��û
	public synchronized void response_getseatCount(int event, int index) {
		int seatCount = 0;
		seatCount = Login.currentSeat;
		System.out.println("Manage seatCount : " + Login.currentSeat);
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage Ȯ���ڸ� �����ֱ�");
				sst.send_Event(event);
				sst.send_seatCount(seatCount);
			}
		}
	}
	//Event 1203 - ����¼��̿��� Update
	public synchronized void response_seatUpdate() {
	    Manage.getInstance().seat_update();
	}
	//Event 1300 - ������â�� UserInfo Display
	public synchronized void response_updateUserInfo(User_infoDTO user_info) {
		Manage.getInstance().update_userInfo(user_info.getDto(),
				user_info.getSeatNum(),user_info.getRemain_time()); //������â�� ���� ���� ������Ʈ
	}
	//Event 1301 - ������â�� �����ð� Update
	public synchronized void response_updateUserTime(User_infoDTO user_info) {
		Manage.getInstance().update_time(user_info.getSeatNum(), 
				user_info.getRemain_time(), user_info.getR_time(),user_info.getU_time());
	}
	//Event 1302 - ������â���� ����� ���� ����
	public synchronized void response_deleteUserInfo(int seatNum) {
		Manage.getInstance().delete_userInfo(seatNum);
	}
	//Event 1400 - ������â �ֹ������� ����Ʈ �߰�
	public synchronized void response_makeOrderList(OrderListDTO orderList) {
		Manage.getInstance().make_list(orderList);
	}
	
	
	//2000 ���� DBó��
	//Event 2000,2001,2002 - Login , Idã��, Pw ã��
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
				System.out.println("Manage DB �˻� ��� ó�� �Ϸ�");
				sst.send_Event(event);
				sst.send_Dto(result_dto);
			}
		}
	}
	//Event 2003 - ID �ߺ� Check
	public synchronized void response_idCheck(int event, int index, MemberDTO dto) {
		int result = 0;
		
		result=dao.check_id(dto.getId());//0�� ��밡�� 1�� �ߺ�
		System.out.println("server"+dto.getId());
		System.out.println(result);
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB �˻� ��� ó�� �Ϸ�");
				sst.send_Event(event);
				sst.send_CheckResult(result);
			}
		}
	}
	//Event 2100 - ȸ������
	public synchronized void response_userJoin(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.insert(dto.getId(), dto.getPw(), dto.getName(), dto.getPhone());//0�� ��밡�� 1�� �ߺ�
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB �˻� ��� ó�� �Ϸ�");
				sst.send_Event(event);
				sst.send_JoinResult(result);
			}
		}
	}
	
	//Event 2200 - ȸ����������
	public synchronized void response_userUpdate(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.update(dto.getId(),dto.getPw(), dto.getPhone());
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB ȸ���������� �Ϸ�");
				sst.send_Event(event);
				sst.send_editResult(result);
			}
		}
	}
	
	//Event 2201 - ������� ȸ����������
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
	//Event 2202 - ��ǰ���Ž� ȸ�� �����ݾ׻�뷮 Update
	public synchronized void response_totMoney(int event, int index, MemberDTO dto, int price_sum) {
		dao.update_totMoney(dto.getId(),price_sum);
	}
	
	
	
	
	//Event 2300 - ȸ����������
	public synchronized void response_userDelete(int event, int index, MemberDTO dto) {
		
		int result = 0;
		result=dao.delete(dto.getId(), dto.getPw());
		
		for(int i =0; i<list.size(); i++) {
			ServerSocketThread sst = (ServerSocketThread) list.get(i);
			if(sst.index == index) {
				System.out.println("Manage DB ȸ���������� ���� �Ϸ�");
				sst.send_Event(event);
				sst.send_removeResult(result);
			}
		}
	}
	
	//Event 2500 - Sales �ݾ� update
	public synchronized void response_salesUpdate(int event, int index, int code, int money) {
		sao.update(code, money);
	}
	
	
	
	
	
	
	

	
	
}
