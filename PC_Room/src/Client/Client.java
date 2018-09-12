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
	int seatNum = 0;//자리번호
	int seatCount = 0; // 사용중인 좌석 갯수
	int check = 0; // 중복로그인 체크 결과
	List<Integer> seatList = null;//사용중인 자리 목록
	JLabel change1 = new JLabel("0"); // 자리이동 실시간처리용
	JLabel change2 = new JLabel("0"); // 자리이동 실시간처리용
	boolean socket_state = true;
	int user_code = 0;     // 관리자가 실행시켰는지 사용자가 직접 실행했는지
	MemberDTO dto = null;  // dto 받기
	int login_result = 0;  // 로그인결과
	int id_result = 0;     // 아이디찾기
	int pw_result = 0;     // Pw찾기
	int idCheck_result = 2;// 아이디 중복 체크 결과
	int join_result = 2;   // 회원가입
	int edit_result = 2;   // 회원정보 수정
	int remove_result = 2; // 회원탈퇴
	

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
		
		//통신 초기화
		try {
			socket = new Socket(ip,port);
		} catch (IOException e) {
			socket_state = false;
			System.out.println("소켓 생성 실패 !!");
		}
	}
	
	//event 1000 : 사용자 자리배정 - 빈자리 받아오기
	public void request_seatNum(){ // 사용자 -> 관리자
		System.out.println("Manage에 자리번호 요청");
		try {
			out.writeInt(1000);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1001  사용 중인 자리 목록 받아오기
	public void request_seatList(){ // 사용자 -> 관리자
		System.out.println("Manage에 사용 중인 자리 목록  요청 ");
		try {
			out.writeInt(1001);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//event 1100  : 사용자 중복로그인 방지
	public void request_Multiple(MemberDTO dto) {// 사용자 -> 관리자
		System.out.println("Manage에 아이디 중복 확인 요청");
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
	
	//event 1200 : 사용좌석 증가 요청
	public void request_seatPlus() {// 사용자 -> 관리자
		System.out.println("Manage에 사용좌석 증가요청");
		try {
			out.writeInt(1200);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1201 : 사용좌석 감소 요청
	public void request_seatMinus() {// 사용자 -> 관리자
		System.out.println("Manage에 사용좌석 감소요청");
		try {
			out.writeInt(1201);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//event 1202 : 사용좌석갯수 요청
	public void request_getseatCount() {// 사용자 -> 관리자
		System.out.println("Manage에 사용좌석갯수 요청");
		try {
			out.writeInt(1202);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1203 : 좌석이용율 Update
	public void request_seatUpdate() {// 사용자 -> 관리자
		System.out.println("Manage의 좌석이용율 Update");
		try {
			out.writeInt(1203);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//event 1300 & 1301
	//event 1300 :  관리자화면에 유저정보 Display
	//event 1301 :  관리자화면에 유저Time Update
	public void request_update_UserInfo(int event, User_infoDTO user_info) {// 사용자 -> 관리자
		//System.out.println("Manage의 좌석이용율 Update");
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
	//event 1302 관리자창에서 사용자 정보 삭제
	public void request_deleteUserInfo(int seatNum) {// 사용자 -> 관리자
		System.out.println("Manage 관리자창에서 사용자 정보 삭제");
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
	
	//event 1400 관리자화면 주문내역에 리스트 추가
	public void request_makeList(OrderListDTO order_list) {// 사용자 -> 관리자
		System.out.println("Manage 관리자화면 주문내역에 리스트 추가");
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
	
	
	// DB event 처리
	//event 2000 Login기능 처리
	public void request_login(MemberDTO dto) {// 사용자 -> 관리자
		System.out.println("Manage에서 Login기능 처리");
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
	
	    //event 2001 Id 찾기 기능
		public void request_findId(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에서 ID 찾기기능 처리");
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
		
		//event 2002 Pw 찾기 기능
		public void request_findPw(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에서 Pw 찾기기능 처리");
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
		
		//event 2003  ID 중복 check 기능
		public void request_checkId(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에서 ID 중복 check");
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
		
		
		//event 2100  회원가입 기능
		public void request_Join(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에 회원가입 요청");

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

		
		
		//event 2200  회원정보 수정
		public void request_update(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에 회원정보 수정 요청");
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
		//event 2201 종료시 회원정보 Update
		public void request_saveAll(User_infoDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에 종료시 회원정보 Update 요청");
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
		//event 2202 상품구매시 회원 금액 누적사용량 Update
		public void request_totUpdate(MemberDTO dto,int price_sum) {// 사용자 -> 관리자
			System.out.println("Manage에 상품구매시 회원 금액 누적사용량 Update 요청");
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
		
		
		
		//event 2300  회원탈퇴
		public void request_delete(MemberDTO dto) {// 사용자 -> 관리자
			System.out.println("Manage에 회원정보 삭제 요청");
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
		public void request_salesUpdate(int code, int money) {// 사용자 -> 관리자
			System.out.println("Manage에 sales 금액 Update 요청");
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
	//응답 처리
	public void run() {
		try {
			while(true) {
				System.out.println("User Event 수신 대기중");
	            event = in.readInt();
	            System.out.println("User에서 수신받은 Event : " + event);
	            if(event == 1000) {
	            	System.out.println("User 자리번호 Set");
	            	seatNum = in.readInt();
	            	System.out.println("User 자리번호 : " + seatNum);
	            	
	            }else if(event == 1001) {
	            	System.out.println("User 사용중인 목록 Set");
	            	try {
						seatList =  (List<Integer>)in.readObject();
						
	                    if(change2.getText().equals("0")) change2.setText("1");
	                    else change2.setText("0");
	                    
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						System.out.println("클래스가 존재하지 않습니다.");
					}
	            	
	            }else if(event == 1002) {
	            	System.out.println("User Change 값 Set");

                    if(change1.getText().equals("0")) change1.setText("1");
                    else change1.setText("0");
	            	System.out.println("User Change 변경후  : " + change1.getText());
	            	
	            }else if(event == 1100) {
	            	System.out.println("User 중복로그인 체크 결과Set");
	            	check = in.readInt();
	            	System.out.println("User 중복로그인 체크 : " + check);//1:사용  2:미사용
	            	
	            }else if(event == 1202) {
	            	System.out.println("User 사용좌석 갯수");
	            	seatCount = in.readInt();
	            	System.out.println("User 사용좌석 갯수 : " + seatCount);
	            	
	            }else if(event == 2000) {//2000부터 DB 처리
	            	System.out.println("User Login기능 처리 결과");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)login_result = 1;
						else login_result = 2;
						System.out.println("정상 수신 완료");
					} catch (ClassNotFoundException e) {
						System.out.println("클래스를 찾을수 없습니다.");
						//e.printStackTrace();
					}
	            }else if(event == 2001) {
	            	System.out.println("User ID 찾기기능 처리 결과");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)id_result = 1;
						else id_result = 2;
						System.out.println("정상 수신 완료");
					} catch (ClassNotFoundException e) {
						System.out.println("클래스를 찾을수 없습니다.");
						//e.printStackTrace();
					}
	            }else if(event == 2002) {
	            	System.out.println("User Pw 찾기 기능 처리 결과");
	            	try {
						dto = (MemberDTO) in.readObject();
						if(dto != null)pw_result = 1;
						else pw_result = 2;
						System.out.println("정상 수신 완료");
					} catch (ClassNotFoundException e) {
						System.out.println("클래스를 찾을수 없습니다.");
						//e.printStackTrace();
					}
	            	
	            }else if(event == 2003) {
	            	System.out.println("User ID 중복 CHeck 처리 결과");
					idCheck_result = in.readInt();//0 사용가능 1중복
					System.out.println("정상 수신 완료");

	            }else if(event == 2100) {
	            	System.out.println("회원가입 처리 결과");
	            	join_result = in.readInt();//0 사용가능 1중복
					System.out.println("정상 수신 완료");

	            }else if(event == 2200) {
	            	System.out.println("회원정보 수정 결과");
	            	edit_result = in.readInt();//0 사용가능 1중복
					System.out.println("정상 수신 완료");

	            }else if(event == 2300) {
	            	System.out.println("회원정보 탈퇴 결과");
	            	remove_result = in.readInt();//0 사용가능 1중복
					System.out.println("정상 수신 완료");

	            }

			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("연결 이상");
		} finally {
			try {
				socket.close();//입력 완료후 소켓 종료
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

	// 중복로그인 체크 결과 출력
	public int getCheck() {
		return check;
	}

	//사용좌석 갯수 출력
	public int getSeatCount() {
		return seatCount;
	}

	//자리번호 출력
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
			Thread thread = new Thread(this);//run을 사용하기위해 run이 들어가있는 클래스를 매개변수로 줌
			//클라이언트 스레드 시작
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
