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
	
	//공용
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
	
	// event 1000 - 빈좌석번호 송신
	public void send_seatNum(int seatNum) {// 관리자 -> 사용자
		try {
			out.writeInt(seatNum);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 1001 - 사용 중인 자리 목록 받아오기
	public void send_inuseList(List<Integer> seatList) {// 관리자 -> 사용자
		try {
			System.out.println("보냈나요?");
			out.writeObject(seatList);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// event 1100 - 중복로그인 체크결과 반환
	public void send_MultiCheck(int result) {// 관리자 -> 사용자
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	// event 1202 - 사용중인 좌석갯수 송신
	public void send_seatCount(int seatCount) {// 관리자 -> 사용자
		try {
			out.writeInt(seatCount);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//2000 부터 DB 처리
	// event 2000,2001,2002 - Login,Id찾기,Pw찾기 기능 처리 결과
	public void send_Dto(MemberDTO dto) {// 관리자 -> 사용자
		try {
			out.writeObject(dto);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2003 - ID 중복 Check 기능 처리 결과
	public void send_CheckResult(int result) {// 관리자 -> 사용자
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2100 - 회원가입
	public void send_JoinResult(int result) {// 관리자 -> 사용자
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2200 - 회원정보 수정
	public void send_editResult(int result) {// 관리자 -> 사용자
		try {
			out.writeInt(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// event 2300 - 회원정보 삭제
	public void send_removeResult(int result) {// 관리자 -> 사용자
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
				System.out.println("Manage Event 수신 대기중");
				event = in.readInt();
				System.out.println("Manage에서 수신받은 Event : " + event);
				if(event == 1000) { //빈 자리 번호 요청
					System.out.println("Manage 자리 지정 시작");
					server.response_seatNum(event, index);
					
				}else if(event == 1001) { //사용 중인 자리 목록 받아오기
					System.out.println("Manage 사용 중인 자리 목록 받아오는중 [" + index + "]");
					server.response_inuseList(event, index);
					
				}else if(event == 1100) { // 중복 로그인 확인 요청

                    MemberDTO dto = null;
					System.out.println("Manage 중복 확인중");
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("클래스를 찾을수 없습니다.");
					}
					server.response_MultiCheck(event, index, dto.getId());
					
				}else if(event == 1200) { // 사용좌석 증가 요청
					System.out.println("Manage 사용좌석 증가중");
					server.response_seatPlus();
					
				}else if(event == 1201) { // 사용좌석 감소 요청
					System.out.println("Manage 사용좌석 감소중");
					server.response_seatMinus();
					
				}else if(event == 1202) { // 사용좌석갯수 요청
					System.out.println("Manage 사용좌석 감소중");
					server.response_getseatCount(event,index);
					
				}else if(event == 1203) { // 좌석이용률 Update
					System.out.println("Manage 좌석 이용율 Update");
					server.response_seatUpdate();
					
				}else if(event == 1300) { // 관리자창에 User정보 Display
					User_infoDTO user_info = null;
					System.out.println("Manage 관리자창에 User정보 Display중");
					try {
						user_info = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					server.response_updateUserInfo(user_info);
				}else if(event == 1301) { // 관리자창에 남은시간 Update
					//System.out.println("Manage 관리자창에 남은시간 Update");
					User_infoDTO user_info = null;
					try {
						user_info = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					server.response_updateUserTime(user_info);
					
				}else if(event == 1302) { // 관리자창에서 사용자 정보 삭제
					System.out.println("Manage 관리자창에서 사용자 정보 삭제");
					int seatNum = 0;
					seatNum = in.readInt();
					server.response_deleteUserInfo(seatNum);
					
				}else if(event == 1400) { // 관리자창의 주문내역에 리스트 추가
					System.out.println("Manage관리자창의 주문내역에 리스트 추가");
					OrderListDTO orderList = null;;
					try {
						orderList = (OrderListDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_makeOrderList(orderList);
				}else if(event == 2000||event == 2001||event == 2002) { // Login, Id 찾기, Pw 찾기
					System.out.println("Manage Login기능 처리");
					MemberDTO dto = null;;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_SelectResult(event, index, dto); 

				}else if(event == 2003) { // ID 중복 Check
					System.out.println("Manage ID 중복 Check기능");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
						
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_idCheck(event, index, dto);

				}else if(event == 2100) { // 회원가입
					System.out.println("Manage 회원가입 기능");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_userJoin(event, index, dto); 

				}else if(event == 2200) { // 회원정보 수정
					System.out.println("Manage 회원정보 수정 기능");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_userUpdate(event, index, dto); 

				}else if(event == 2201) { // 종료시 회원정보 Update
					System.out.println("Manage 종료시 회원정보 Update 기능");
					User_infoDTO dto = null;
					try {
						dto = (User_infoDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_SaveAll(event, index, dto); 

				}else if(event == 2202) { // 상품구매시 금액 누적사용량에 Update
					System.out.println("Manage 상품구매시 금액 누적사용량에 Update 기능");
					MemberDTO dto = null;
					int price_sum = 0;
					try {
						dto = (MemberDTO) in.readObject();
						price_sum = in.readInt();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_totMoney(event, index, dto, price_sum); 

				}else if(event == 2300) { // 회원정보 삭제
					System.out.println("Manage 회원정보 삭제 기능");
					MemberDTO dto = null;
					try {
						dto = (MemberDTO) in.readObject();
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
					server.response_userDelete(event, index, dto); 

				}else if(event == 2500) { // Sales DB 금액 Update
					System.out.println("Manage Sales DB 금액 Update 기능");

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
