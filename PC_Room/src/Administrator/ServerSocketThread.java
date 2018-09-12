package Administrator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;

public class ServerSocketThread extends Thread{
	
	Socket socket;
	Server server;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	int event = 0;
	int index = 0;
	String threadName = "Thread";
	
	public ServerSocketThread(Server server,Socket socket,int index) {
		this.index = index;
		this.socket = socket;
		this.server = server;
		threadName = getName();
		
		
	}
	
	//����
	public void send_Event(int event) {
		try {
			out.writeInt(event);
			out.flush();
			System.out.println("ServerSocketThread : " + event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// event 1000 - ���¼���ȣ �۽�
	public void send_seatNum(int seatNum) {// ������ -> �����
		try {
			out.writeInt(seatNum);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 1001 - ��� ���� �ڸ� ��� �޾ƿ���
	public void send_inuseList(List<Integer> seatList) {// ������ -> �����
		try {
			System.out.println("���³���?");
			out.writeObject(seatList);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// event 1100 - �ߺ��α��� üũ��� ��ȯ
	public void send_MultiCheck(int result) {// ������ -> �����
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	// event 1202 - ������� �¼����� �۽�
	public void send_seatCount(int seatCount) {// ������ -> �����
		try {
			out.writeInt(seatCount);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//2000 ���� DB ó��
	// event 2000,2001,2002 - Login,Idã��,Pwã�� ��� ó�� ���
	public void send_Dto(MemberDTO dto) {// ������ -> �����
		try {
			out.writeObject(dto);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2003 - ID �ߺ� Check ��� ó�� ���
	public void send_CheckResult(int result) {// ������ -> �����
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2100 - ȸ������
	public void send_JoinResult(int result) {// ������ -> �����
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2200 - ȸ������ ����
	public void send_editResult(int result) {// ������ -> �����
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2300 - ȸ������ ����
	public void send_removeResult(int result) {// ������ -> �����
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			while(true) {
				System.out.println("Manage Event ���� �����");
				event = in.readInt();
				System.out.println("Manage���� ���Ź��� Event : " + event);
				if(event == 1000) { //�� �ڸ� ��ȣ ��û
					System.out.println("Manage �ڸ� ���� ����");
					server.response_seatNum(event, index);
					
				}else if(event == 1001) { //��� ���� �ڸ� ��� �޾ƿ���
					System.out.println("Manage ��� ���� �ڸ� ��� �޾ƿ����� [" + index + "]");
					server.response_inuseList(event, index);
					
				}else if(event == 1100) { // �ߺ� �α��� Ȯ�� ��û

                    MemberDTO dto = null;
					System.out.println("Manage �ߺ� Ȯ����");
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("Ŭ������ ã���� �����ϴ�.");
					}
					server.response_MultiCheck(event, index, dto.getId());
					
				}else if(event == 1200) { // ����¼� ���� ��û
					System.out.println("Manage ����¼� ������");
					server.response_seatPlus();
					
				}else if(event == 1201) { // ����¼� ���� ��û
					System.out.println("Manage ����¼� ������");
					server.response_seatMinus();
					
				}else if(event == 1202) { // ����¼����� ��û
					System.out.println("Manage ����¼� ������");
					server.response_getseatCount(event,index);
					
				}else if(event == 1203) { // �¼��̿�� Update
					System.out.println("Manage �¼� �̿��� Update");
					server.response_seatUpdate();
					
				}else if(event == 1300) { // ������â�� User���� Display
					User_infoDTO user_info = null;
					System.out.println("Manage ������â�� User���� Display��");
					try {
						user_info = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					server.response_updateUserInfo(user_info);
				}else if(event == 1301) { // ������â�� �����ð� Update
					//System.out.println("Manage ������â�� �����ð� Update");
					User_infoDTO user_info = null;
					try {
						user_info = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					server.response_updateUserTime(user_info);
					
				}else if(event == 1302) { // ������â���� ����� ���� ����
					System.out.println("Manage ������â���� ����� ���� ����");
					int seatNum = 0;
					seatNum = in.readInt();
					server.response_deleteUserInfo(seatNum);
					
				}else if(event == 1400) { // ������â�� �ֹ������� ����Ʈ �߰�
					System.out.println("Manage������â�� �ֹ������� ����Ʈ �߰�");
					OrderListDTO orderList = null;;
					try {
						orderList = (OrderListDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_makeOrderList(orderList);
				}else if(event == 2000||event == 2001||event == 2002) { // Login, Id ã��, Pw ã��
					System.out.println("Manage Login��� ó��");
					MemberDTO dto = null;;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_SelectResult(event, index, dto); 

				}else if(event == 2003) { // ID �ߺ� Check
					System.out.println("Manage ID �ߺ� Check���");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
						
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_idCheck(event, index, dto);

				}else if(event == 2100) { // ȸ������
					System.out.println("Manage ȸ������ ���");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_userJoin(event, index, dto); 

				}else if(event == 2200) { // ȸ������ ����
					System.out.println("Manage ȸ������ ���� ���");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_userUpdate(event, index, dto); 

				}else if(event == 2201) { // ����� ȸ������ Update
					System.out.println("Manage ����� ȸ������ Update ���");
					User_infoDTO dto = null;
					try {
						dto = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_SaveAll(event, index, dto); 

				}else if(event == 2202) { // ��ǰ���Ž� �ݾ� ������뷮�� Update
					System.out.println("Manage ��ǰ���Ž� �ݾ� ������뷮�� Update ���");
					MemberDTO dto = null;
					int price_sum = 0;
					try {
						dto = (MemberDTO) in.readObject();
						price_sum = in.readInt();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_totMoney(event, index, dto, price_sum); 

				}else if(event == 2300) { // ȸ������ ����
					System.out.println("Manage ȸ������ ���� ���");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
					server.response_userDelete(event, index, dto); 

				}else if(event == 2500) { // Sales DB �ݾ� Update
					System.out.println("Manage Sales DB �ݾ� Update ���");

					int code = in.readInt();
					int	money = in.readInt();

					server.response_salesUpdate(event, index, code, money); 

				}
				
			}
		} catch (IOException e) {
			server.removeClient(this);
		} finally {
			try {
				socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
