package Client;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import DB.MemberDTO;

//import DB.MemberDAO;

public class Login_Find extends JFrame implements ActionListener{
Container container;
	
	JLabel seatNumberID = new JLabel("좌석번호");
	JLabel labelseatID;
	JLabel labelmessageID;
	JLabel labelsearchID;
	JLabel labelallIDname;
	JLabel labelallIDphone;
	
	JLabel labelseatPW;
	JLabel labelmessagePW;
	JLabel labelsearchPW;
	JLabel labelallPWname;
	JLabel labelallPWphone;
	
	
	JTextField textIDphone;
	JTextField textIDname;
	JTextField textPWname;
	JTextField textPWphone;
	
	JButton buttonsearchID;
	JButton buttoncancelID;
	JButton buttonID;
	JButton buttonPW;
	JButton buttoncancelPW;
	JButton buttonsearchPW;
	
	JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP,
			JTabbedPane.SCROLL_TAB_LAYOUT);
	
	JPanel paneltop;
	JPanel panelID;
	JPanel panelPW;
	
	//MemberDAO dao = new MemberDAO();
	MemberDTO dto = new MemberDTO();
	
	BevelBorder bevelborder = new BevelBorder(BevelBorder.RAISED);
	int seatNum=0; //자리번호
	
	Client client = null;
	
	Thread thread = new Thread();
	
	public Login_Find(Client client, int seatNum) {
		this.client =client;
		this.seatNum = seatNum;
		setTitle("KGITBANK");
		setLayout(null);
		setSize(500, 535);
		setLocation(950,40);
		container = getContentPane();
		
		
		init();
		start();
		
		setVisible(true);
		setResizable(false);
		
	}
	private void init() {
		container.setBackground(Color.GRAY);
		
		seatNumberID.setFont(new Font("Sherif", Font.BOLD, 14));
		seatNumberID.setForeground(Color.ORANGE);
		seatNumberID.setBounds(20,15,70,50);

		labelseatID = new JLabel(String.valueOf(seatNum)); //자리번호 설정
		labelseatID.setFont(new Font("Sherif", Font.BOLD, 48));
		labelseatID.setForeground(Color.ORANGE);
		labelseatID.setBounds(90, 15, 80, 50);
		labelmessageID = new JLabel("저희 매장에 방문하신 것을 환영합니다.");
		labelmessageID.setFont(new Font("Sherif", Font.BOLD, 16));
		labelmessageID.setForeground(Color.ORANGE);
		labelmessageID.setBounds(20, 15, 400, 150);
		paneltop = new JPanel();
		paneltop.setLayout(null);
		paneltop.setBounds(10, 10, 475, 135);
		paneltop.setBackground(Color.BLACK);
		paneltop.add(labelmessageID);
		paneltop.add(labelseatID);
		paneltop.add(seatNumberID);

		labelsearchID = new JLabel("[ 아이디 검색 ]");
		labelsearchID.setForeground(Color.BLUE);
		labelsearchID.setFont(new Font("Sherif", Font.BOLD, 18));
		labelsearchID.setBounds(40, 20, 150, 40);
		labelallIDname = new JLabel("이름을 입력하세요 : ");
		labelallIDname.setForeground(new Color(0X5D5D5D));
		labelallIDname.setFont(new Font("Sherif", Font.BOLD, 15));
		labelallIDname.setBounds(57, 40, 200, 120);
		labelallIDphone = new JLabel("전화번호를 입력하세요 : ");
		labelallIDphone.setForeground(new Color(0X5D5D5D));
		labelallIDphone.setFont(new Font("Sherif", Font.BOLD, 15));
		labelallIDphone.setBounds(20, 90, 300, 150);
		textIDphone = new JTextField();
		textIDphone.setBounds(240, 150, 180, 30);
		textIDname = new JTextField();
		textIDname.setBounds(240, 85, 180, 30);
		buttonsearchID = new JButton("아이디 검색");
		buttonsearchID.setBackground(Color.GRAY);
		buttonsearchID.setForeground(Color.WHITE);
		buttonsearchID.setBounds(180, 225, 120, 35);
		buttonsearchID.setBorder(bevelborder);
		buttonsearchID.setFocusPainted(false);
		
		buttoncancelID = new JButton("취  소");
		buttoncancelID.setBackground(Color.GRAY);
		buttoncancelID.setForeground(Color.WHITE);
		buttoncancelID.setBounds(320, 225, 120, 35);
		buttoncancelID.setBorder(bevelborder);
		buttoncancelID.setFocusPainted(false);
		panelID = new JPanel();
		panelID.setLayout(null);
		panelID.setBounds(10, 100, 475, 235);
		panelID.setBackground(Color.LIGHT_GRAY);
		panelID.add(labelsearchID);
		panelID.add(labelallIDname);
		panelID.add(textIDname);
		panelID.add(buttonsearchID);
		panelID.add(buttoncancelID);
		panelID.add(textIDphone);
		panelID.add(labelallIDphone);
		///////////////////////////////////////////////
		labelsearchPW = new JLabel("[ 비밀번호 찾기 ]");
		labelsearchPW.setForeground(Color.BLUE);
		labelsearchPW.setFont(new Font("Sherif", Font.BOLD, 18));
		labelsearchPW.setBounds(30, 20, 170, 40);
		labelallPWname = new JLabel("아이디를 입력하세요 : ");
		labelallPWname.setForeground(new Color(0X5D5D5D));
		labelallPWname.setFont(new Font("Sherif", Font.BOLD, 15));
		labelallPWname.setBounds(45, 40, 200, 120);
		labelallPWphone = new JLabel("전화번호를 입력하세요 : ");
		labelallPWphone.setForeground(new Color(0X5D5D5D));
		labelallPWphone.setFont(new Font("Sherif", Font.BOLD, 15));
		labelallPWphone.setBounds(20, 90, 300, 150);
		textPWphone = new JTextField();
		textPWphone.setBounds(240, 150, 180, 30);
		textPWname = new JTextField();
		textPWname.setBounds(240, 85, 180, 30);
		buttonsearchPW = new JButton("비밀번호 검색");
		buttonsearchPW.setBackground(Color.GRAY);
		buttonsearchPW.setForeground(Color.WHITE);
		buttonsearchPW.setBounds(180, 225, 120, 35);
		buttonsearchPW.setBorder(bevelborder);
		buttonsearchPW.setFocusPainted(false);
		buttoncancelPW = new JButton("취  소");
		buttoncancelPW.setBackground(Color.GRAY);
		buttoncancelPW.setForeground(Color.WHITE);
		buttoncancelPW.setBounds(320, 225, 120, 35);
		buttoncancelPW.setBorder(bevelborder);
		buttoncancelPW.setFocusPainted(false);
		
		panelPW = new JPanel();
		panelPW.setLayout(null);
		panelPW.setBounds(10, 220, 475, 235);
		panelPW.setBackground(Color.LIGHT_GRAY);
		panelPW.add(labelsearchPW);
		panelPW.add(labelallPWname);
		panelPW.add(textPWname);
		panelPW.add(labelallPWphone);
		panelPW.add(textPWphone);
		panelPW.add(buttonsearchPW);
		panelPW.add(buttoncancelPW);

		tp.setBounds(10, 150, 475, 350);
		tp.add("                              ID 찾기                           ", panelID);
		tp.add("                              PW 찾기                           ", panelPW);
		buttonID = new JButton("아이디 찾기");

		container.add(paneltop);
		container.add(tp);
	}
	
	private void start() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		buttoncancelID.addActionListener(this);
		buttonsearchID.addActionListener(this);
		buttoncancelPW.addActionListener(this);
		buttonsearchPW.addActionListener(this);
		textIDphone.addActionListener(this);
		textPWphone.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == buttonsearchID || e.getSource() == textIDphone){
			String IDname = textIDname.getText();
			String IDphone = textIDphone.getText();
			String result = "";
			
			if(IDname.equals("") || IDphone.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "아이디 또는 전화번호를 입력해주세요.");
				return;
			}else {
				dto.setName(IDname);
				dto.setPhone(IDphone);
				client.request_findId(dto);
				
				dto = null;
				while(true) {//Id 찾는중
					if(client.id_result==0) {
						System.out.println("Id 찾는중");
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}else break;
				}
				dto = client.getDto();
				client.id_result = 0;
				
				result = dto.getId();
				//result = dao.find_id(IDname, IDphone);
				JOptionPane.showInternalMessageDialog(container, result);
				dispose();
			}
			
		}else if(e.getSource() == buttonsearchPW || e.getSource() == textPWphone) {
			String PWID = textPWname.getText();
			String PWphone = textPWphone.getText();
			String result = null;
			if(PWID.equals("") || PWphone.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "아이디 또는 전화번호를 입력해주세요.");
				return;
			}
			else {
				dto.setId(PWID);
				dto.setPhone(PWphone);
				client.request_findPw(dto);
				
				dto = null;
				while(true) {//Id 찾는중
					if(client.pw_result==0) {
						System.out.println("Pw 찾는중");
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}else break;
				}
				dto = client.getDto();
				client.pw_result = 0;
				
				result = dto.getPw();
				
				
				//result = dao.find_pw(PWID, PWphone);
				JOptionPane.showInternalMessageDialog(container, result);
				dispose();
			}
		}else if(e.getSource() == buttoncancelID) {
			dispose();
		}else if(e.getSource() == buttoncancelPW) {
			dispose();
		}
	}
}
