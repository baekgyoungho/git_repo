package Administrator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import Administrator.Server;
import Client.Client_Login;
import DB.MemberDAO;
import DB.MemberDTO;
import DB.OrderListDTO;
import DB.SalesDAO;
import DB.SalesDTO;

public class Manage extends JFrame implements MouseListener,Runnable,Manage_i{

	//싱글톤 설정
	private static Manage instance = null;
	public static Manage getInstance() {
		if (instance==null) instance=new Manage();
		return instance;
	}
	public static void freeInstance() {
		instance=null;
	}

	
	/**1. 선언*/
	Container container = getContentPane();
	
	//메뉴 관련//
	JMenuBar menuBar = new JMenuBar();
	
	JMenu menuUser = new JMenu("회원관리");
	JMenuItem userOne = new JMenuItem("회원 정보 조회");
	JMenuItem userAll = new JMenuItem("전체 회원 확인");
	
	JMenu menuSales = new JMenu("매출관리");
	JMenuItem salesToday = new JMenuItem("당일 매출 확인");
	JMenuItem salesSelect = new JMenuItem("기간별 매출 확인");
	
	JMenu menuOrder = new JMenu("주문관리");
	JMenuItem orderHistory = new JMenuItem("주문 내역 확인");
	 
	JMenu menuEmpty = new JMenu("                                                                            "
			+ "                                                                                                         ");
	JMenu menuExit = new JMenu("종료");
	
	EmptyBorder empty = new EmptyBorder(1,1,1,1);
	JLabel emptyLabel = new JLabel(" ");
	//Font menuFont = new Font("SansSherif", 1, 15);
	
	
	//이용률 관련
	//JProgressBar progressBar;  - 실시간으로 진행률 보여주는게 아니니까 필요 없음
	JPanel p_percent = new JPanel(new BorderLayout());
	   //-west에는 이용률 이미지, center에는 이용 현황 숫자 표기
	ImageIcon image_percent ;
	JLabel percent; //이미지 객체 위에 퍼센트 출력
	JLabel seat_current; //현재 사용 자리 수 

	
	//사용자 정보 출력
	JPanel p_userInfo = new JPanel(new BorderLayout());
	JLabel num = new JLabel();
	JLabel id = new JLabel();
	JLabel time = new JLabel();
	
	
	//자리 레이아웃 관련
	JPanel p_seatLayout = new JPanel(new GridLayout(6, 5, 3,3));
	JLabel[] seat = new JLabel[30]; //자리 클릭할 때 버튼은 깜박거려서 라벨로 설정
	JLabel[] seat_no=new JLabel[30];
	JLabel[] seat_id=new JLabel[30];
	JLabel[] seat_time=new JLabel[30]; //남은 시간

	int[] uTime = new int[30]; //사용 시간 저장
	int[] rTime = new int[30]; //남은 시간 저장
	
	
	//테두리, 레이아웃
	LineBorder lineBorder = new LineBorder(Color.DARK_GRAY,5);
	LineBorder lineBorder2 = new LineBorder(new Color(0X5D5D5D), 5);
	EtchedBorder etchedBorder = new EtchedBorder();
	CompoundBorder compoundBorder = new CompoundBorder(etchedBorder, lineBorder);
	CompoundBorder compoundBorder2 = new CompoundBorder(etchedBorder, lineBorder2);
	
	//MemberDAO/DTO
	MemberDAO dao = new MemberDAO();
	MemberDTO dto = new MemberDTO();
	
	//SalesDAO/DTO
	SalesDAO salesDAO = new SalesDAO();
	SalesDTO salesDTO = new SalesDTO();
	
	//Order_list
	JList<JPanel> order_list = new JList<>();
	Manage_OrderList manage_OrderList = null;
	
	
	//전체 사용자 정보 조회
	//JButton b_list;
	//JPanel p_showAll;
	
	Thread thread = new Thread(this);

	Server server = null;
	
	/**2. 초기화*/
	private Manage() {
		
		thread.start();
		
		setTitle("KG ITBANK PC방 관리 프로그램");
		setBounds(0,0,800,500);
		setUndecorated(true);  //제목 표시줄 출력 x
		
		init();
		start();
		
		//Sales DB에 오늘날짜 매출 record 생성 (생성시 날짜만 등록되어 있음)
		salesDAO.insert();
		
		setResizable(false);
		setVisible(true);
	}
	
	
	/**3. 화면 구성*/
	private void init() {
		container.setLayout(new BorderLayout(5,5));
		container.setBackground(new Color(0X5D5D5D));
		
		//메뉴
		setJMenuBar(menuBar);
		
		menuBar.setBackground(Color.BLACK);
		menuBar.setBorder(empty);
		
		// - 회원관리
		menuUser.setForeground(Color.GRAY);
		menuUser.setBorder(empty);
		
		menuUser.add(userOne);
		userOne.setBackground(Color.BLACK);
		userOne.setForeground(Color.GRAY);
		userOne.setBorder(empty);
		
		//menuUser.addSeparator();
		menuUser.add(userAll);
		userAll.setBackground(Color.BLACK);
		userAll.setForeground(Color.GRAY);
		userOne.setBorder(empty);
		menuBar.add(menuUser);
		
		
		// - 매출 관리
		menuSales.setForeground(Color.GRAY);
		menuSales.setBorder(empty);
		
		menuSales.add(salesToday);
		salesToday.setBackground(Color.BLACK);
		salesToday.setForeground(Color.GRAY);
		salesToday.setBorder(empty);
		
		//menuSales.addSeparator();
		menuSales.add(salesSelect);
		salesSelect.setBackground(Color.BLACK);
		salesSelect.setForeground(Color.GRAY);
		salesSelect.setBorder(empty);
		menuBar.add(menuSales);
		
		
		// - 주문 관리
		menuOrder.setForeground(Color.GRAY);
		menuOrder.setBorder(empty);
		
		menuOrder.add(orderHistory);
		orderHistory.setBackground(Color.BLACK);
		orderHistory.setForeground(Color.GRAY);
		orderHistory.setBorder(empty);
		menuBar.add(menuOrder);
		
	
		// - 종료
		menuBar.add(menuEmpty);
		menuEmpty.setEnabled(false);
		menuBar.add(menuExit);
		menuExit.setForeground(Color.GRAY);
		
		
		//이용률 - 별도 함수로 구현
		seatPercent(p_percent);
		//p_percent.setBorder(new LineBorder(Color.DARK_GRAY,5));
		//p_percent.setBorder(etchedBorder);
		p_percent.setBorder(compoundBorder2);
		p_percent.setBackground(new Color(0X5D5D5D));
		
	
		//사용자 정보  - 별도 함수로 구현
		setUserInfo(p_userInfo);
		p_userInfo.setBorder(compoundBorder2);
		p_userInfo.setBackground(new Color(0X5D5D5D));
		
		//p_west패널에 이용률 & 사용자 정보 & 광고 추가
		ImageIcon logo = new ImageIcon("img/logo.png");
		JLabel logoLabel = new JLabel(logo);
		logoLabel.setBorder(compoundBorder2);
	
		
		JPanel p_west = new JPanel(new BorderLayout(5,5));
		p_west.setBackground(new Color(0X5D5D5D));
		p_west.add("North",p_percent);
		p_west.add("Center",p_userInfo);
		p_west.add("South",logoLabel);
		
		
		//자리 레이아웃
		GridLayout seat_print = new GridLayout(4, 1);
		//EtchedBorder etchedBorder = new EtchedBorder();
		for (int i=0;i<seat.length;i++) {
			seat[i] = new JLabel();
			seat_no[i] = new JLabel(String.valueOf(i+1)); //자리번호 지정
			seat_id[i] = new JLabel();
			seat_time[i] = new JLabel();  //남은 시간
			
			seat[i].setLayout(seat_print);
			seat[i].setBackground(Color.DARK_GRAY);
			seat[i].setForeground(Color.GRAY);
			seat[i].setBorder(etchedBorder);
			seat[i].setOpaque(true); //라벨 배경색 출력
			
			seat_no[i].setForeground(Color.GRAY);
			seat_id[i].setForeground(Color.GREEN);
			seat_time[i].setForeground(Color.GREEN);
			
			seat[i].add(seat_no[i]);
			seat[i].add(seat_id[i]);
			seat[i].add(seat_time[i]);
			seat_id[i].setHorizontalAlignment(SwingConstants.RIGHT);
			seat_time[i].setHorizontalAlignment(SwingConstants.RIGHT);
			//seat[i].setVerticalAlignment(SwingConstants.TOP); //라벨 글씨 상단에 위치
			p_seatLayout.add(seat[i]);
		}
		p_seatLayout.setBackground(new Color(0X5D5D5D));
		p_seatLayout.setBorder(compoundBorder2);
		
		
		emptyLabel.setBackground(new Color(0X5D5D5D));
		//container.add("North",emptyLabel);
		container.add("West",p_west);
		container.add("Center",p_seatLayout);
	
	}
	
	
	//이용률 레이아웃 구성 함수
	public void seatPercent(JPanel panel) {
	
		// - 5% 단위로 이미지 변경 함수 필요 - if문으로 설정할 것
		image_percent = new ImageIcon("img/0%.png");
		//percent = new JLabel("0%", image_percent, SwingConstants.CENTER);
		percent=new JLabel();
		percent.setText("0%");
		percent.setIcon(image_percent);
		percent.setForeground(Color.CYAN);
		percent.setHorizontalTextPosition(SwingConstants.CENTER);
		percent.setOpaque(true);
		percent.setBackground(Color.DARK_GRAY);
		
	
		//[이용자리 수 | 총 자리 수] 출력 함수
		JPanel p_seatPercent = new JPanel(new BorderLayout());
		JPanel p_text = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p_text.setPreferredSize(new Dimension(150, 60));
		seat_current = new JLabel(String.valueOf(Login.currentSeat)); //관리자 창 생성 시 좌석 이용 수 = 0
		JLabel seat_all = new JLabel(" | "+String.valueOf(Login.TOT_SEAT));
		
		Font font = new Font("verdana", 1, 25);
		seat_current.setForeground(Color.CYAN);
		seat_current.setFont(font);
		seat_current.setHorizontalAlignment(SwingConstants.CENTER);
		seat_all.setFont(font);
		seat_all.setForeground(Color.WHITE);
		
		p_text.add(seat_current);
		p_text.add(seat_all);
		p_text.setBackground(Color.DARK_GRAY);
		p_seatPercent.setBackground(Color.DARK_GRAY);
		p_seatPercent.add("North",emptyLabel);
		p_seatPercent.add("Center",p_text);
		
		
		panel.add("West", percent);
		panel.add("Center", p_seatPercent);
		//panel.add("Center", p_text);
	}
	
	
	//사용자 정보 출력 레이아웃 함수
	public void setUserInfo(JPanel panel) {
		JLabel title = new JLabel("< 사용자 정보 >",JLabel.CENTER);
		title.setOpaque(true);
		title.setForeground(Color.GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setPreferredSize(new Dimension(150, 40));
		title.setBorder(new LineBorder(new Color(0X5D5D5D), 3));
		
		JPanel p_info = new JPanel(new BorderLayout());
		p_info.setBorder(new LineBorder(new Color(0X5D5D5D), 3));
		JPanel panel1 = new JPanel(new GridLayout(3, 1)); //자리번호, 아이디, 남은시간
		JPanel panel2 = new JPanel(new GridLayout(3, 1)); //panel1의 내용 출력
		panel1.setBackground(Color.DARK_GRAY);
		panel2.setBackground(Color.DARK_GRAY);
		
		// - panel1에 추가할 컴포넌트
		JLabel num_L = new JLabel("  자리 번호 : ", JLabel.RIGHT);
		num_L.setForeground(Color.GRAY);
		JLabel id_L = new JLabel("회원 ID : ", JLabel.RIGHT);
		id_L.setForeground(Color.GRAY);
		JLabel time_L = new JLabel("  남은 시간 : ", JLabel.RIGHT);
		    //남은 시간은 실시간으로 업데이트해야 되는데 가능할까? 어려우면 로그인 시간으로 대체
		time_L.setForeground(Color.GRAY);
		
		panel1.add(num_L);
		panel1.add(id_L);
		panel1.add(time_L);
		
		// - panel2에 추가할 컴포넌트 (선언부에서 생성)
		num.setForeground(Color.YELLOW);
		id.setForeground(Color.YELLOW);
		time.setForeground(Color.YELLOW);
		panel2.add(num);
		panel2.add(id);
		panel2.add(time);
		
		p_info.add("West", panel1);
		p_info.add("Center", panel2);
		
		panel.add("North", title);
		panel.add("Center", p_info);
	}
	
	
	//자리 이용률 업데이트 함수
	public void seat_update() {
		//p_percent.remove(percent);
		//p_percent.invalidate();
		
		seat_current.setText(String.valueOf(Login.currentSeat)); //좌석 수 업데이트
		int current_percent = (int)(((double)Login.currentSeat/Login.TOT_SEAT)*100);
		String string_percent = String.valueOf(current_percent)+"%";
		System.out.println(current_percent);
		percent.setText(string_percent); //퍼센트 업데이트
		
		String imageName=null;
		if (current_percent==100) imageName = "img/100%.png";
		else if (current_percent>=90) imageName = "img/95%.png";
		else if (current_percent>=85) imageName = "img/90%.png";
		else if (current_percent>=80) imageName = "img/85%.png";
		else if (current_percent>=75) imageName = "img/80%.png";
		else if (current_percent>=70) imageName = "img/75%.png";
		else if (current_percent>=65) imageName = "img/70%.png";
		else if (current_percent>=60) imageName = "img/65%.png";
		else if (current_percent>=55) imageName = "img/60%.png";
		else if (current_percent>=50) imageName = "img/55%.png";
		else if (current_percent>=45) imageName = "img/50%.png";
		else if (current_percent>=40) imageName = "img/45%.png";
		else if (current_percent>=35) imageName = "img/40%.png";
		else if (current_percent>=30) imageName = "img/35%.png";
		else if (current_percent>=25) imageName = "img/30%.png";
		else if (current_percent>=20) imageName = "img/25%.png";
		else if (current_percent>=15) imageName = "img/20%.png";
		else if (current_percent>=10) imageName = "img/15%.png";
		else if (current_percent>=5) imageName = "img/10%.png";
		else if (current_percent>0) imageName = "img/5%.png";
		else if (current_percent==0) imageName = "img/0%.png";

		System.out.println(imageName);
		//image_percent.getImage().flush();
		image_percent = new ImageIcon(imageName);
		percent.setIcon(image_percent);
		
		//percent.revalidate();
		//percent.repaint();
		//p_percent.add("West", percent);
		//p_percent.revalidate();
		//p_percent.repaint();
		
		server.broadCasting(1002);
	}
	

	//자리 레이아웃에 사용자 정보 출력 함수
	public void update_userInfo(MemberDTO dto, int seatNum, String time) {
		seat_no[seatNum-1].setForeground(Color.ORANGE);
		seat_id[seatNum-1].setText(dto.getId());
		seat_time[seatNum-1].setText(time);
		seat[seatNum-1].setBorder(new LineBorder(Color.GREEN,2));
		seat[seatNum-1].setBackground(new Color(0X5D5D5D));
	}
	
	//자리 레이아웃에 남은 시간 업데이트
	public void update_time(int seatNum, String time, int rtime, int utime) {
		seat_time[seatNum-1].setText(time);
		rTime[seatNum-1] = rtime; //남은 시간
		uTime[seatNum-1] =utime; //사용 시간
	}
	
	
	//로그아웃 후 자리 레이아웃에서 사용자 정보 삭제
	public void delete_userInfo(int seatNum) {
		delete_userText(seatNum);
		seat_no[seatNum-1].setForeground(Color.GRAY);
		seat_id[seatNum-1].setText("");
		seat_time[seatNum-1].setText("");
		seat[seatNum-1].setBorder(etchedBorder);
		seat[seatNum-1].setBackground(Color.DARK_GRAY);
	}
	
	//로그아웃한 사용자의 정보가 사용자 정보창에 출력되어 있을 경우 내용 삭제
	public void delete_userText(int seatNum) {
		if (num.getText().equals(String.valueOf(seatNum))){
			num.setText("");
			id.setText("");
			time.setText("");
		}
	}
	
	//사용자 자리 변경 시 현재 사용 중인 자리 전달
	public List<Integer> seat_inUse() {
		List<Integer> seatList = new ArrayList<Integer>();
		for (int i=0;i<seat.length;i++) {
			if (!seat_id[i].getText().equals("")) {
				seatList.add(i); //자리 배열 인덱스# 저장
			}
		}
		return seatList;
	}
	
	//남은 좌석중 첫번째 좌석 리턴
	public int send_seatNum() {
		for (int i=0;i<seat.length;i++) {
			if (seat_id[i].getText().equals("")) {
				return  i+1;
			}
		}
		return  0;
	}
	
	// Order_List
	public void make_list(OrderListDTO dto) {//Manage에 넣어야됨
		JPanel list       = new JPanel();
		JLabel seat_no       = new JLabel(String.valueOf(dto.getSeatno()),JLabel.CENTER);
		JLabel product_name  = new JLabel(dto.getProduct_name(),JLabel.CENTER);
		JLabel product_count = new JLabel(String.valueOf(dto.getProduct_count()),JLabel.CENTER);
		JLabel product_price = new JLabel(String.valueOf(dto.getProduct_price()),JLabel.CENTER);
		
		list.setBackground(Color.white);
		list.setLayout(new FlowLayout(FlowLayout.LEFT,1,1));
		list.add(seat_no);
		seat_no.setPreferredSize(new Dimension(70,20));
		seat_no.setBackground(Color.white);
		list.add(product_name);
		product_name.setForeground(Color.black);
		product_name.setPreferredSize(new Dimension(100,20));
		list.add(product_count);
		product_count.setForeground(Color.black);
		product_count.setPreferredSize(new Dimension(60,20));
		list.add(product_price);
		product_price.setForeground(Color.black);
		product_price.setPreferredSize(new Dimension(75,20));

		order_list.add(list,0);
		
		if(manage_OrderList!=null) {
			manage_OrderList.order_list = this.order_list;
			manage_OrderList.repainted();
		}
		
	}
	
	
	/**4. 이벤트 설정*/
	private void start() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menuExit.addMouseListener(this); //제목표시줄 안보이게 설정할 경우 종료 기능 담당
		
		//메뉴바
		userOne.addMouseListener(this);
		userAll.addMouseListener(this);
		salesToday.addMouseListener(this);
		salesSelect.addMouseListener(this);
		orderHistory.addMouseListener(this);
		
		//자리
		for (int i=0;i<seat.length;i++) {
			seat[i].addMouseListener(this);
		}
	}
	
	
	/**5. 이벤트 처리*/
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//관리자창 종료 (=프로그램 종료)
 		if (e.getSource()==menuExit) {

 			dao.getConnection1();
 			
 			//로그인 되어 있는 계정은 userDB / salesDB 업데이트
 			for (int i=0; i<seat.length;i++) {
 				if (!seat_id[i].getText().equals("")||seat_id[i].getText()!=null) { //사용 중인 자리일 경우		
 					dao.save_all(seat_id[i].getText(), rTime[i], uTime[i]);
 					//System.out.println("rTime[i] :"+rTime[i]);
 					//System.out.println("uTime[i] :"+uTime[i]);
 					salesDAO.update(1, uTime[i]); //pc 매출 저장
 				}
 			}
 			
 			dao.close();
 			System.exit(0);  //관리자 창 종료 시 프로그램 전체 종료
 			
 		}
 		
 		//좌석 클릭 시 싱글 클릭/더블 클릭 여부에 따라 이벤트 설정
 		for (int i=0;i<seat.length;i++) {
 			if (e.getSource()==seat[i]) {
 				if (e.getClickCount()==1) { //1회 클릭= 사용 중인 자리일 경우 사용자 정보 조회
 					if (!seat_id[i].getText().equals("")) {
 						
 						num.setText(seat_no[i].getText());  //자리번호
 						num.setFont(new Font("Gothic", 1, 18));
 						id.setText(seat_id[i].getText()); //아이디
 						id.setFont(new Font("Gothic", 1, 18));
 						time.setText(seat_time[i].getText()); //남은 시간
 						time.setFont(new Font("Gothic", 1, 18));
 					}
 				}
 				if (e.getClickCount()==2) { //2회 클릭 = 사용자 로그인창 팝업
 					if (!seat_id[i].getText().equals("")) return;  //이미 사용중인 자리는 로그인창 나타나지 않도록 설정
 					else new Client_Login(i+1);
 				}
 			}
 		} 		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		//회원 정보 조회
		if (e.getSource()==userOne) {
 			String searchId = JOptionPane.showInternalInputDialog(container, "검색할 아이디 입력");
 			if (searchId==null) return; //취소 버튼 누를 경우 회원정보 종료
 			
 			else {
 				dto = dao.search_user(searchId);
 				// 다이얼로그창 생성 함수 연결
 				new Manage_User(this, dto);
 			}
 		}
		
		//전체 회원 조회
		if (e.getSource()==userAll) {
			new Manage_User(this);
		}
		
		//당일 매출 확인
		if(e.getSource()==salesToday) {
			new Manage_Sales();
		}
		
		//기간별 매출 확인
		if (e.getSource()==salesSelect) {
			new Manage_Sales(this);
		}
		
		//주문내역 확인
		if(e.getSource()==orderHistory) {
			if (manage_OrderList!=null) manage_OrderList.dialog.setVisible(true);
			else manage_OrderList =new Manage_OrderList(order_list);
		}
	}
	

	@Override
	public void run() {
		server = new Server();
		server.giveAndTake();
		
	}
}
