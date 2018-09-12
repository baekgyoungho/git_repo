package Client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.JLabel;
import DB.MemberDTO;
import DB.OrderListDTO;
import DB.User_infoDTO;


public class Client implements Runnable,Client_i{
	
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String str;
	int event = 0;
	int seatNum = 0;//�ڸ���ȣ
	int seatCount = 0; // ������� �¼� ����
	int check = 0; // �ߺ��α��� üũ ���
	List<Integer> seatList = null;//������� �ڸ� ���
	JLabel change1 = new JLabel("0"); // �ڸ��̵� �ǽð�ó����
	JLabel change2 = new JLabel("0"); // �ڸ��̵� �ǽð�ó����
	boolean socket_state = true;
	int user_code = 0;     // �����ڰ� ������״��� ����ڰ� ���� �����ߴ���
	MemberDTO dto = null;  // dto �ޱ�
	int login_result = 0;  // �α��ΰ��
	int id_result = 0;     // ���̵�ã��
	int pw_result = 0;     // Pwã��
	int idCheck_result = 2;// ���̵� �ߺ� üũ ���
	int join_result = 2;   // ȸ������
	int edit_result = 2;   // ȸ������ ����
	int remove_result = 2; // ȸ��Ż��
	

	public int getEdit_result() {
		return edit_result;
	}

	public void setEdit_result(int edit_result) {
		this.edit_result = edit_result;
	}

	public int getRemove_result() {
		return remove_result;
	}

	public void setRemove_result(int remove_result) {
		this.remove_result = remove_result;
	}

	public Client(String ip, int port) {
		
		//��� �ʱ�ȭ
		try {
			socket = new Socket(ip,port);
		} catch (IOException e) {
			socket_state = false;
			System.out.println("���� ���� ���� !!");
		}
	}
	
	//event 1000 : ����� �ڸ����� - ���ڸ� �޾ƿ���
	public void request_seatNum(){ // ����� -> ������
		System.out.println("Manage�� �ڸ���ȣ ��û");
		try {
			out.writeInt(1000);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1001  ��� ���� �ڸ� ��� �޾ƿ���
	public void request_seatList(){ // ����� -> ������
		System.out.println("Manage�� ��� ���� �ڸ� ���  ��û ");
		try {
			out.writeInt(1001);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//event 1100  : ����� �ߺ��α��� ����
	public void request_Multiple(MemberDTO dto) {// ����� -> ������
		System.out.println("Manage�� ���̵� �ߺ� Ȯ�� ��û");
		try {
			out.writeInt(1100);
			out.flush();
			out.writeObject(dto);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1200 : ����¼� ���� ��û
	public void request_seatPlus() {// ����� -> ������
		System.out.println("Manage�� ����¼� ������û");
		try {
			out.writeInt(1200);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1201 : ����¼� ���� ��û
	public void request_seatMinus() {// ����� -> ������
		System.out.println("Manage�� ����¼� ���ҿ�û");
		try {
			out.writeInt(1201);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//event 1202 : ����¼����� ��û
	public void request_getseatCount() {// ����� -> ������
		System.out.println("Manage�� ����¼����� ��û");
		try {
			out.writeInt(1202);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1203 : �¼��̿��� Update
	public void request_seatUpdate() {// ����� -> ������
		System.out.println("Manage�� �¼��̿��� Update");
		try {
			out.writeInt(1203);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1300 & 1301
	//event 1300 :  ������ȭ�鿡 �������� Display
	//event 1301 :  ������ȭ�鿡 ����Time Update
	public void request_update_UserInfo(int event, User_infoDTO user_info) {// ����� -> ������
		//System.out.println("Manage�� �¼��̿��� Update");
		try {
			if(event==1300) out.writeInt(1300);
			else if(event==1301) out.writeInt(1301);
			out.flush();
			out.writeObject(user_info);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//event 1302 ������â���� ����� ���� ����
	public void request_deleteUserInfo(int seatNum) {// ����� -> ������
		System.out.println("Manage ������â���� ����� ���� ����");
		try {
			out.writeInt(1302);
			out.flush();
			out.writeInt(seatNum);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1400 ������ȭ�� �ֹ������� ����Ʈ �߰�
	public void request_makeList(OrderListDTO order_list) {// ����� -> ������
		System.out.println("Manage ������ȭ�� �ֹ������� ����Ʈ �߰�");
		try {
			out.writeInt(1400);
			out.flush();
			out.writeObject(order_list);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// DB event ó��
	//event 2000 Login��� ó��
	public void request_login(MemberDTO dto) {// ����� -> ������
		System.out.println("Manage���� Login��� ó��");
		try {
			out.writeInt(2000);
			out.flush();
			out.writeObject(dto);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	    //event 2001 Id ã�� ���
		public void request_findId(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage���� ID ã���� ó��");
			try {
				out.writeInt(2001);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//event 2002 Pw ã�� ���
		public void request_findPw(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage���� Pw ã���� ó��");
			try {
				out.writeInt(2002);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//event 2003  ID �ߺ� check ���
		public void request_checkId(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage���� ID �ߺ� check");
			try {
				out.writeInt(2003);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//event 2100  ȸ������ ���
		public void request_Join(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage�� ȸ������ ��û");

			try {
				out.writeInt(2100);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
		//event 2200  ȸ������ ����
		public void request_update(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage�� ȸ������ ���� ��û");
			try {
				out.writeInt(2200);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//event 2201 ����� ȸ������ Update
		public void request_saveAll(User_infoDTO dto) {// ����� -> ������
			System.out.println("Manage�� ����� ȸ������ Update ��û");
			try {
				out.writeInt(2201);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//event 2202 ��ǰ���Ž� ȸ�� �ݾ� ������뷮 Update
		public void request_totUpdate(MemberDTO dto,int price_sum) {// ����� -> ������
			System.out.println("Manage�� ��ǰ���Ž� ȸ�� �ݾ� ������뷮 Update ��û");
			try {
				out.writeInt(2202);
				out.flush();
				out.writeObject(dto);
				out.flush();
				out.writeInt(price_sum);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		//event 2300  ȸ��Ż��
		public void request_delete(MemberDTO dto) {// ����� -> ������
			System.out.println("Manage�� ȸ������ ���� ��û");
			try {
				out.writeInt(2300);
				out.flush();
				out.writeObject(dto);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//event 2500 sales DBUpdate
		public void request_salesUpdate(int code, int money) {// ����� -> ������
			System.out.println("Manage�� sales �ݾ� Update ��û");
			try {
				out.writeInt(2500);
				out.flush();
				out.writeInt(code);
				out.flush();
				out.writeInt(money);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	
	
	
	@Override
	//���� ó��
	public void run() {
		try {
			while(true) {
				System.out.println("User Event ���� �����");
	            event = in.readInt();
	            System.out.println("User���� ���Ź��� Event : " + event);
	            if(event == 1000) {
	            	System.out.println("User �ڸ���ȣ Set");
	            	seatNum = in.readInt();
	            	System.out.println("User �ڸ���ȣ : " + seatNum);
	            	
	            }else if(event == 1001) {
	            	System.out.println("User ������� ��� Set");
	            	try {
						seatList =  (List<Integer>)in.readObject();
						
	                    if(change2.getText().equals("0")) change2.setText("1");
	                    else change2.setText("0");
	                    
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("Ŭ������ �������� �ʽ��ϴ�.");
					}
	            	
	            }else if(event == 1002) {
	            	System.out.println("User Change �� Set");

                    if(change1.getText().equals("0")) change1.setText("1");
                    else change1.setText("0");
	            	System.out.println("User Change ������  : " + change1.getText());
	            	
	            }else if(event == 1100) {
	            	System.out.println("User �ߺ��α��� üũ ���Set");
	            	check = in.readInt();
	            	System.out.println("User �ߺ��α��� üũ : " + check);//1:���  2:�̻��
	            	
	            }else if(event == 1202) {
	            	System.out.println("User ����¼� ����");
	            	seatCount = in.readInt();
	            	System.out.println("User ����¼� ���� : " + seatCount);
	            	
	            }else if(event == 2000) {//2000���� DB ó��
	            	System.out.println("User Login��� ó�� ���");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)login_result = 1;
						else login_result = 2;
						System.out.println("���� ���� �Ϸ�");
					} catch (ClassNotFoundException e) {
						System.out.println("Ŭ������ ã���� �����ϴ�.");
						//e.printStackTrace();
					}
	            }else if(event == 2001) {
	            	System.out.println("User ID ã���� ó�� ���");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)id_result = 1;
						else id_result = 2;
						System.out.println("���� ���� �Ϸ�");
					} catch (ClassNotFoundException e) {
						System.out.println("Ŭ������ ã���� �����ϴ�.");
						//e.printStackTrace();
					}
	            }else if(event == 2002) {
	            	System.out.println("User Pw ã�� ��� ó�� ���");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)pw_result = 1;
						else pw_result = 2;
						System.out.println("���� ���� �Ϸ�");
					} catch (ClassNotFoundException e) {
						System.out.println("Ŭ������ ã���� �����ϴ�.");
						//e.printStackTrace();
					}
	            	
	            }else if(event == 2003) {
	            	System.out.println("User ID �ߺ� CHeck ó�� ���");
					idCheck_result = in.readInt();//0 ��밡�� 1�ߺ�
					System.out.println("���� ���� �Ϸ�");

	            }else if(event == 2100) {
	            	System.out.println("ȸ������ ó�� ���");
	            	join_result = in.readInt();//0 ��밡�� 1�ߺ�
					System.out.println("���� ���� �Ϸ�");

	            }else if(event == 2200) {
	            	System.out.println("ȸ������ ���� ���");
	            	edit_result = in.readInt();//0 ��밡�� 1�ߺ�
					System.out.println("���� ���� �Ϸ�");

	            }else if(event == 2300) {
	            	System.out.println("ȸ������ Ż�� ���");
	            	remove_result = in.readInt();//0 ��밡�� 1�ߺ�
					System.out.println("���� ���� �Ϸ�");

	            }

			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("���� �̻�");
		} finally {
			try {
				socket.close();//�Է� �Ϸ��� ���� ����
			}catch(Exception e) {
				//e.getMessage();
			}
		}
		
	}
	
	public int getJoin_result() {
		return join_result;
	}

	public void setJoin_result(int join_result) {
		this.join_result = join_result;
	}

	public int getIdCheck_result() {
		return idCheck_result;
	}

	public void setIdCheck_result(int idCheck_result) {
		this.idCheck_result = idCheck_result;
	}

	public int getLogin_result() {
		return login_result;
	}

	public void setLogin_result(int login_result) {
		this.login_result = login_result;
	}

	public MemberDTO getDto() {
		return dto;
	}

	public void setDto(MemberDTO dto) {
		this.dto = dto;
	}

	public List<Integer> getSeatList() {
		return seatList;
	}

	// �ߺ��α��� üũ ��� ���
	public int getCheck() {
		return check;
	}

	//����¼� ���� ���
	public int getSeatCount() {
		return seatCount;
	}

	//�ڸ���ȣ ���
	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public void setSeatList(List<Integer> seatList) {
		this.seatList = seatList;
	}

	public void giveAndTake() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			Thread thread = new Thread(this);//run�� ����ϱ����� run�� ���ִ� Ŭ������ �Ű������� ��
			//Ŭ���̾�Ʈ ������ ����
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
