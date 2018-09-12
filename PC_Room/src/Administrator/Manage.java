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

	//�̱��� ����
	private static Manage instance = null;
	public static Manage getInstance() {
		if (instance==null) instance=new Manage();
		return instance;
	}
	public static void freeInstance() {
		instance=null;
	}

	
	/**1. ����*/
	Container container = getContentPane();
	
	//�޴� ����//
	JMenuBar menuBar = new JMenuBar();
	
	JMenu menuUser = new JMenu("ȸ������");
	JMenuItem userOne = new JMenuItem("ȸ�� ���� ��ȸ");
	JMenuItem userAll = new JMenuItem("��ü ȸ�� Ȯ��");
	
	JMenu menuSales = new JMenu("�������");
	JMenuItem salesToday = new JMenuItem("���� ���� Ȯ��");
	JMenuItem salesSelect = new JMenuItem("�Ⱓ�� ���� Ȯ��");
	
	JMenu menuOrder = new JMenu("�ֹ�����");
	JMenuItem orderHistory = new JMenuItem("�ֹ� ���� Ȯ��");
	 
	JMenu menuEmpty = new JMenu("                                                                            "
			+ "                                                                                                         ");
	JMenu menuExit = new JMenu("����");
	
	EmptyBorder empty = new EmptyBorder(1,1,1,1);
	JLabel emptyLabel = new JLabel(" ");
	//Font menuFont = new Font("SansSherif", 1, 15);
	
	
	//�̿�� ����
	//JProgressBar progressBar;  - �ǽð����� ����� �����ִ°� �ƴϴϱ� �ʿ� ����
	JPanel p_percent = new JPanel(new BorderLayout());
	   //-west���� �̿�� �̹���, center���� �̿� ��Ȳ ���� ǥ��
	ImageIcon image_percent ;
	JLabel percent; //�̹��� ��ü ���� �ۼ�Ʈ ���
	JLabel seat_current; //���� ��� �ڸ� �� 

	
	//����� ���� ���
	JPanel p_userInfo = new JPanel(new BorderLayout());
	JLabel num = new JLabel();
	JLabel id = new JLabel();
	JLabel time = new JLabel();
	
	
	//�ڸ� ���̾ƿ� ����
	JPanel p_seatLayout = new JPanel(new GridLayout(6, 5, 3,3));
	JLabel[] seat = new JLabel[30]; //�ڸ� Ŭ���� �� ��ư�� ���ڰŷ��� �󺧷� ����
	JLabel[] seat_no=new JLabel[30];
	JLabel[] seat_id=new JLabel[30];
	JLabel[] seat_time=new JLabel[30]; //���� �ð�

	int[] uTime = new int[30]; //��� �ð� ����
	int[] rTime = new int[30]; //���� �ð� ����
	
	
	//�׵θ�, ���̾ƿ�
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
	
	
	//��ü ����� ���� ��ȸ
	//JButton b_list;
	//JPanel p_showAll;
	
	Thread thread = new Thread(this);

	Server server = null;
	
	/**2. �ʱ�ȭ*/
	private Manage() {
		
		thread.start();
		
		setTitle("KG ITBANK PC�� ���� ���α׷�");
		setBounds(0,0,800,500);
		setUndecorated(true);  //���� ǥ���� ��� x
		
		init();
		start();
		
		//Sales DB�� ���ó�¥ ���� record ���� (������ ��¥�� ��ϵǾ� ����)
		salesDAO.insert();
		
		setResizable(false);
		setVisible(true);
	}
	
	
	/**3. ȭ�� ����*/
	private void init() {
		container.setLayout(new BorderLayout(5,5));
		container.setBackground(new Color(0X5D5D5D));
		
		//�޴�
		setJMenuBar(menuBar);
		
		menuBar.setBackground(Color.BLACK);
		menuBar.setBorder(empty);
		
		// - ȸ������
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
		
		
		// - ���� ����
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
		
		
		// - �ֹ� ����
		menuOrder.setForeground(Color.GRAY);
		menuOrder.setBorder(empty);
		
		menuOrder.add(orderHistory);
		orderHistory.setBackground(Color.BLACK);
		orderHistory.setForeground(Color.GRAY);
		orderHistory.setBorder(empty);
		menuBar.add(menuOrder);
		
	
		// - ����
		menuBar.add(menuEmpty);
		menuEmpty.setEnabled(false);
		menuBar.add(menuExit);
		menuExit.setForeground(Color.GRAY);
		
		
		//�̿�� - ���� �Լ��� ����
		seatPercent(p_percent);
		//p_percent.setBorder(new LineBorder(Color.DARK_GRAY,5));
		//p_percent.setBorder(etchedBorder);
		p_percent.setBorder(compoundBorder2);
		p_percent.setBackground(new Color(0X5D5D5D));
		
	
		//����� ����  - ���� �Լ��� ����
		setUserInfo(p_userInfo);
		p_userInfo.setBorder(compoundBorder2);
		p_userInfo.setBackground(new Color(0X5D5D5D));
		
		//p_west�гο� �̿�� & ����� ���� & ���� �߰�
		ImageIcon logo = new ImageIcon("img/logo.png");
		JLabel logoLabel = new JLabel(logo);
		logoLabel.setBorder(compoundBorder2);
	
		
		JPanel p_west = new JPanel(new BorderLayout(5,5));
		p_west.setBackground(new Color(0X5D5D5D));
		p_west.add("North",p_percent);
		p_west.add("Center",p_userInfo);
		p_west.add("South",logoLabel);
		
		
		//�ڸ� ���̾ƿ�
		GridLayout seat_print = new GridLayout(4, 1);
		//EtchedBorder etchedBorder = new EtchedBorder();
		for (int i=0;i<seat.length;i++) {
			seat[i] = new JLabel();
			seat_no[i] = new JLabel(String.valueOf(i+1)); //�ڸ���ȣ ����
			seat_id[i] = new JLabel();
			seat_time[i] = new JLabel();  //���� �ð�
			
			seat[i].setLayout(seat_print);
			seat[i].setBackground(Color.DARK_GRAY);
			seat[i].setForeground(Color.GRAY);
			seat[i].setBorder(etchedBorder);
			seat[i].setOpaque(true); //�� ���� ���
			
			seat_no[i].setForeground(Color.GRAY);
			seat_id[i].setForeground(Color.GREEN);
			seat_time[i].setForeground(Color.GREEN);
			
			seat[i].add(seat_no[i]);
			seat[i].add(seat_id[i]);
			seat[i].add(seat_time[i]);
			seat_id[i].setHorizontalAlignment(SwingConstants.RIGHT);
			seat_time[i].setHorizontalAlignment(SwingConstants.RIGHT);
			//seat[i].setVerticalAlignment(SwingConstants.TOP); //�� �۾� ��ܿ� ��ġ
			p_seatLayout.add(seat[i]);
		}
		p_seatLayout.setBackground(new Color(0X5D5D5D));
		p_seatLayout.setBorder(compoundBorder2);
		
		
		emptyLabel.setBackground(new Color(0X5D5D5D));
		//container.add("North",emptyLabel);
		container.add("West",p_west);
		container.add("Center",p_seatLayout);
	
	}
	
	
	//�̿�� ���̾ƿ� ���� �Լ�
	public void seatPercent(JPanel panel) {
	
		// - 5% ������ �̹��� ���� �Լ� �ʿ� - if������ ������ ��
		image_percent = new ImageIcon("img/0%.png");
		//percent = new JLabel("0%", image_percent, SwingConstants.CENTER);
		percent=new JLabel();
		percent.setText("0%");
		percent.setIcon(image_percent);
		percent.setForeground(Color.CYAN);
		percent.setHorizontalTextPosition(SwingConstants.CENTER);
		percent.setOpaque(true);
		percent.setBackground(Color.DARK_GRAY);
		
	
		//[�̿��ڸ� �� | �� �ڸ� ��] ��� �Լ�
		JPanel p_seatPercent = new JPanel(new BorderLayout());
		JPanel p_text = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p_text.setPreferredSize(new Dimension(150, 60));
		seat_current = new JLabel(String.valueOf(Login.currentSeat)); //������ â ���� �� �¼� �̿� �� = 0
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
	
	
	//����� ���� ��� ���̾ƿ� �Լ�
	public void setUserInfo(JPanel panel) {
		JLabel title = new JLabel("< ����� ���� >",JLabel.CENTER);
		title.setOpaque(true);
		title.setForeground(Color.GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setPreferredSize(new Dimension(150, 40));
		title.setBorder(new LineBorder(new Color(0X5D5D5D), 3));
		
		JPanel p_info = new JPanel(new BorderLayout());
		p_info.setBorder(new LineBorder(new Color(0X5D5D5D), 3));
		JPanel panel1 = new JPanel(new GridLayout(3, 1)); //�ڸ���ȣ, ���̵�, �����ð�
		JPanel panel2 = new JPanel(new GridLayout(3, 1)); //panel1�� ���� ���
		panel1.setBackground(Color.DARK_GRAY);
		panel2.setBackground(Color.DARK_GRAY);
		
		// - panel1�� �߰��� ������Ʈ
		JLabel num_L = new JLabel("  �ڸ� ��ȣ : ", JLabel.RIGHT);
		num_L.setForeground(Color.GRAY);
		JLabel id_L = new JLabel("ȸ�� ID : ", JLabel.RIGHT);
		id_L.setForeground(Color.GRAY);
		JLabel time_L = new JLabel("  ���� �ð� : ", JLabel.RIGHT);
		    //���� �ð��� �ǽð����� ������Ʈ�ؾ� �Ǵµ� �����ұ�? ������ �α��� �ð����� ��ü
		time_L.setForeground(Color.GRAY);
		
		panel1.add(num_L);
		panel1.add(id_L);
		panel1.add(time_L);
		
		// - panel2�� �߰��� ������Ʈ (����ο��� ����)
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
	
	
	//�ڸ� �̿�� ������Ʈ �Լ�
	public void seat_update() {
		//p_percent.remove(percent);
		//p_percent.invalidate();
		
		seat_current.setText(String.valueOf(Login.currentSeat)); //�¼� �� ������Ʈ
		int current_percent = (int)(((double)Login.currentSeat/Login.TOT_SEAT)*100);
		String string_percent = String.valueOf(current_percent)+"%";
		System.out.println(current_percent);
		percent.setText(string_percent); //�ۼ�Ʈ ������Ʈ
		
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
	

	//�ڸ� ���̾ƿ��� ����� ���� ��� �Լ�
	public void update_userInfo(MemberDTO dto, int seatNum, String time) {
		seat_no[seatNum-1].setForeground(Color.ORANGE);
		seat_id[seatNum-1].setText(dto.getId());
		seat_time[seatNum-1].setText(time);
		seat[seatNum-1].setBorder(new LineBorder(Color.GREEN,2));
		seat[seatNum-1].setBackground(new Color(0X5D5D5D));
	}
	
	//�ڸ� ���̾ƿ��� ���� �ð� ������Ʈ
	public void update_time(int seatNum, String time, int rtime, int utime) {
		seat_time[seatNum-1].setText(time);
		rTime[seatNum-1] = rtime; //���� �ð�
		uTime[seatNum-1] =utime; //��� �ð�
	}
	
	
	//�α׾ƿ� �� �ڸ� ���̾ƿ����� ����� ���� ����
	public void delete_userInfo(int seatNum) {
		delete_userText(seatNum);
		seat_no[seatNum-1].setForeground(Color.GRAY);
		seat_id[seatNum-1].setText("");
		seat_time[seatNum-1].setText("");
		seat[seatNum-1].setBorder(etchedBorder);
		seat[seatNum-1].setBackground(Color.DARK_GRAY);
	}
	
	//�α׾ƿ��� ������� ������ ����� ����â�� ��µǾ� ���� ��� ���� ����
	public void delete_userText(int seatNum) {
		if (num.getText().equals(String.valueOf(seatNum))){
			num.setText("");
			id.setText("");
			time.setText("");
		}
	}
	
	//����� �ڸ� ���� �� ���� ��� ���� �ڸ� ����
	public List<Integer> seat_inUse() {
		List<Integer> seatList = new ArrayList<Integer>();
		for (int i=0;i<seat.length;i++) {
			if (!seat_id[i].getText().equals("")) {
				seatList.add(i); //�ڸ� �迭 �ε���# ����
			}
		}
		return seatList;
	}
	
	//���� �¼��� ù��° �¼� ����
	public int send_seatNum() {
		for (int i=0;i<seat.length;i++) {
			if (seat_id[i].getText().equals("")) {
				return  i+1;
			}
		}
		return  0;
	}
	
	// Order_List
	public void make_list(OrderListDTO dto) {//Manage�� �־�ߵ�
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
	
	
	/**4. �̺�Ʈ ����*/
	private void start() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menuExit.addMouseListener(this); //����ǥ���� �Ⱥ��̰� ������ ��� ���� ��� ���
		
		//�޴���
		userOne.addMouseListener(this);
		userAll.addMouseListener(this);
		salesToday.addMouseListener(this);
		salesSelect.addMouseListener(this);
		orderHistory.addMouseListener(this);
		
		//�ڸ�
		for (int i=0;i<seat.length;i++) {
			seat[i].addMouseListener(this);
		}
	}
	
	
	/**5. �̺�Ʈ ó��*/
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//������â ���� (=���α׷� ����)
 		if (e.getSource()==menuExit) {

 			dao.getConnection1();
 			
 			//�α��� �Ǿ� �ִ� ������ userDB / salesDB ������Ʈ
 			for (int i=0; i<seat.length;i++) {
 				if (!seat_id[i].getText().equals("")||seat_id[i].getText()!=null) { //��� ���� �ڸ��� ���		
 					dao.save_all(seat_id[i].getText(), rTime[i], uTime[i]);
 					//System.out.println("rTime[i] :"+rTime[i]);
 					//System.out.println("uTime[i] :"+uTime[i]);
 					salesDAO.update(1, uTime[i]); //pc ���� ����
 				}
 			}
 			
 			dao.close();
 			System.exit(0);  //������ â ���� �� ���α׷� ��ü ����
 			
 		}
 		
 		//�¼� Ŭ�� �� �̱� Ŭ��/���� Ŭ�� ���ο� ���� �̺�Ʈ ����
 		for (int i=0;i<seat.length;i++) {
 			if (e.getSource()==seat[i]) {
 				if (e.getClickCount()==1) { //1ȸ Ŭ��= ��� ���� �ڸ��� ��� ����� ���� ��ȸ
 					if (!seat_id[i].getText().equals("")) {
 						
 						num.setText(seat_no[i].getText());  //�ڸ���ȣ
 						num.setFont(new Font("Gothic", 1, 18));
 						id.setText(seat_id[i].getText()); //���̵�
 						id.setFont(new Font("Gothic", 1, 18));
 						time.setText(seat_time[i].getText()); //���� �ð�
 						time.setFont(new Font("Gothic", 1, 18));
 					}
 				}
 				if (e.getClickCount()==2) { //2ȸ Ŭ�� = ����� �α���â �˾�
 					if (!seat_id[i].getText().equals("")) return;  //�̹� ������� �ڸ��� �α���â ��Ÿ���� �ʵ��� ����
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
		
		//ȸ�� ���� ��ȸ
		if (e.getSource()==userOne) {
 			String searchId = JOptionPane.showInternalInputDialog(container, "�˻��� ���̵� �Է�");
 			if (searchId==null) return; //��� ��ư ���� ��� ȸ������ ����
 			
 			else {
 				dto = dao.search_user(searchId);
 				// ���̾�α�â ���� �Լ� ����
 				new Manage_User(this, dto);
 			}
 		}
		
		//��ü ȸ�� ��ȸ
		if (e.getSource()==userAll) {
			new Manage_User(this);
		}
		
		//���� ���� Ȯ��
		if(e.getSource()==salesToday) {
			new Manage_Sales();
		}
		
		//�Ⱓ�� ���� Ȯ��
		if (e.getSource()==salesSelect) {
			new Manage_Sales(this);
		}
		
		//�ֹ����� Ȯ��
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
