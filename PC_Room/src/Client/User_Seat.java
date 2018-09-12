package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import DB.MemberDTO;
import DB.User_infoDTO;

//사용자가 자리 이동 버튼 시 팝업으로 띄울 클래스
public class User_Seat extends JFrame implements ActionListener, PropertyChangeListener {
	
	/**선언*/
	//JDialog dialog = new JDialog();
	//Container dialogContainer = dialog.getContentPane();
	Container dialogContainer = getContentPane();
	
	// - 자리 레이아웃 관련
	JPanel p_seatLayout = new JPanel(new GridLayout(6, 5, 3,3));
	JToggleButton[] seat = new JToggleButton[30]; //한 자리만 선택할 수 있도록 토글버튼 사용
	JLabel[] seat_no=new JLabel[30];
	ButtonGroup buttonGroup = new ButtonGroup();
	
	// - 선택, 취소 버튼, 경고 문구
	JButton b_confirm = new JButton(" 선택 완료 ");
	JButton b_cancel = new JButton(" 취      소 ");
	JLabel label = new JLabel("");
	
	// - 테두리
	LineBorder lineBorder2 = new LineBorder(new Color(0X5D5D5D), 5);
	EtchedBorder etchedBorder = new EtchedBorder();
	CompoundBorder compoundBorder2 = new CompoundBorder(etchedBorder, lineBorder2);
	
	String updateNum = ""; //변경한 자리
	List<Integer> seatList; //사용 중인 자리 목록 (manage 클래스에서 받아온 값 저장)
	User user;
	int seatNum=0;//현재 사용 중인 자리
	
	Client client = null;
	Thread thread = new Thread();
	
	/**초기화*/
	public User_Seat(Client client, User user, int seatNum) {
		this.client = client;
		this.user =user;
		this.seatNum = seatNum;
		//setUndecorated(true);  //제목 표시줄 출력 x
		setBounds(1000,0,500,400);
		setResizable(false);
		setTitle("자리 이동");
		
		client.request_seatList();//사용 중인 자리 목록 받아오기
		
		while(true) {//좌석번호 가져오는데 걸리는시간 처리
			if(client.getSeatList()==null) {
				System.out.println("List가 Null입니다");
				try {
					thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else break;
		}
		seatList = client.getSeatList();
		client.setSeatList(null);
		//seatList = Manage.getInstance().seat_inUse(); //사용 중인 자리 목록 받아오기
		init();
		start();
		
		setVisible(true);
		//setVisible(true); //메인이 true가 아니면 다이얼로그창도 보이지 않는다
	}
	
	
	//다이얼로그창 화면 구성
	private void init() {
		//다이얼로그창 화면 구성
		
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.setBackground(new Color(0X5D5D5D));
		
		//- North
		JLabel direction = new JLabel("  이동을 원하시는 자리를 선택하세요 (이미 사용 중인 자리는 선택하실 수 없습니다)");
		direction.setForeground(Color.YELLOW);
		direction.setPreferredSize(new Dimension(500, 30));
		dialogContainer.add("North",direction);
		
		// - Center
		for (int i=0;i<seat.length;i++) {
			seat[i] = new JToggleButton(String.valueOf(i+1));
			//seat_no[i] = new JLabel(String.valueOf(i+1));
			
			seat[i].setBackground(Color.DARK_GRAY);
			seat[i].setForeground(Color.GRAY);
			seat[i].setBorder(etchedBorder);
			seat[i].setFocusPainted(false); //버튼 클릭 시 버튼 text에 테두리 없애도록 설정
			//seat[i].setOpaque(true);
			
			//seat_no[i].setForeground(Color.GRAY);
			//seat[i].add(seat_no[i]);
			buttonGroup.add(seat[i]);
			p_seatLayout.add(seat[i]);
		}
		
		//사용중인 자리: 버튼 색 변경, 클릭 불가 설정
		for (int i=0;i<seatList.size();i++) {
			int num = seatList.get(i);
			System.err.println(num);
			seat[num].setBackground(new Color(0X5D5D5D));
			seat[num].setEnabled(false); //클릭 불가 설정
		}
		
		p_seatLayout.setBackground(new Color(0X5D5D5D));
		p_seatLayout.setBorder(compoundBorder2);
		
		dialogContainer.add("Center",p_seatLayout);
		
		
		//- South
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		b_confirm.setBackground(Color.GRAY);
		b_confirm.setForeground(Color.WHITE);
		b_confirm.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_cancel.setBackground(Color.GRAY);
		b_cancel.setForeground(Color.WHITE);
		b_cancel.setBorder(new BevelBorder(BevelBorder.RAISED));
		panel.add(label);
		panel.add(b_confirm);
		panel.add(b_cancel);
		panel.setBackground(new Color(0X5D5D5D));
		dialogContainer.add("South",panel);
	}

	private void start() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			//창 숨김 기능
		
		b_confirm.addActionListener(this);
		b_cancel.addActionListener(this);
		
		for(int i =0;i<seat.length;i++) {
			seat[i].addActionListener(this);
			//Manage.getInstance().seat_id[i].addPropertyChangeListener(this);
		}
		client.change1.addPropertyChangeListener(this);
		client.change2.addPropertyChangeListener(this);
	}

	//변경된 자리 getter
	public String getUpdateNum() {
		return updateNum;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		//선택한 자리 번호 얻어오기
		for(int i =0;i<seat.length;i++) {
			if (e.getSource()==seat[i]) {
				updateNum = seat[i].getText();
				System.out.println("변경한 자리: "+updateNum);
			}
		}
		
		
		//확인 버튼
		if (e.getSource()==b_confirm) {

			if (updateNum==null || updateNum.equals("")) {
				//자리 선택하지 않은 경우 문구 출력
				label.setText("변경할 자리를 선택해주세요!");
				label.setForeground(Color.ORANGE);
				
			} else {
				//자리를 선택했다면 다이얼로그창 종료되면서 회원창/관리자창에 새로운 자리번호 업데이트
                client.request_deleteUserInfo(seatNum);
				//Manage.getInstance().delete_userInfo(seatNum); //관리자창에서 기존 자리 지우기
				seatNum = Integer.parseInt(updateNum); //다이얼로그창 변수 설정: 기존 자리에 변경한 자리번호 업데이트
				user.seatNum = Integer.parseInt(updateNum); //User창 변수 설정: 기존 자리에 변경한 자리번호 업데이트
				//관리자창에서 새 자리 활성화
				User_infoDTO user_info = new User_infoDTO(user.dto, user.seatNum, 
						user.remain_time.getText(),user.remain_time_value*20,user.use_time_value*20); //관리자창에 유저 정보 업데이트
				client.request_update_UserInfo(1300, user_info);
				//Manage.getInstance().update_userInfo(user.dto, seatNum, user.remain_time.getText()); 
				user.seat_num.setText(String.valueOf(seatNum)); //사용자화면에 자리번호 업데이트
				
				client.request_seatUpdate();//좌석 이용률 업데이트
				
				dispose();
			}

		}
		if (e.getSource()==b_cancel) {
			//seat[Integer.parseInt(updateNum)-1].setSelected(false);
			dispose();
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
		if(e.getNewValue()==client.change1.getText()) {
			//if(e.getNewValue()==Manage.getInstance().seat_id[i].getText()) {
									
			dialogContainer.remove(p_seatLayout);
			dialogContainer.invalidate();
			
			// - Center
			for (int j=0;j<seat.length;j++) {
				seat[j].setBackground(Color.DARK_GRAY);
				seat[j].setEnabled(true);
			}
			client.request_seatList();//사용 중인 자리 목록 받아오기
		}
		else if(e.getNewValue()==client.change2.getText()) {//쓰레드작동중이라 else로 묶지 않을지 2가지 조건 모든 충족하는경우 발생시 
			                                                // 1번 기능 작동후 2번기능이 작동해야하는데 오작동 함
			                                                // Property 문제점 : 해당 이벤트를 발생시킨 오브젝트가 
			                                                // properyListener 동작이 끝날떄까지 작동 정지 

			while(true) {//좌석번호 가져오는데 걸리는시간 처리
				if(client.getSeatList()==null) {
					System.out.println("List가 Null입니다");
					try {
						thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else break;
			}
			
			seatList = client.getSeatList();
			client.setSeatList(null);
			//seatList = Manage.getInstance().seat_inUse(); //사용 중인 자리 목록 받아오기
			
			//사용중인 자리: 버튼 색 변경, 클릭 불가 설정
			for (int k=0;k<seatList.size();k++) {
				int num = seatList.get(k);
				System.err.println(num);
				if(updateNum.equals(String.valueOf(num+1))) {//좌석을 선택한후 버튼 누르기 직전에 다른 사용자가 로그인했을경우 처리
					updateNum = "";
				}
				seat[num].setBackground(new Color(0X5D5D5D));
				seat[num].setEnabled(false); //클릭 불가 설정
			}
			dialogContainer.add("Center",p_seatLayout);
			dialogContainer.repaint();
			dialogContainer.revalidate();
			
		}
	}
}
