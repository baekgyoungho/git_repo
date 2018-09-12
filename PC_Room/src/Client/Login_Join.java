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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import DB.MemberDAO;
import DB.MemberDTO;

public class Login_Join extends JFrame implements ActionListener{
	Container container;
	
	JLabel labelseat;
	JLabel labelmessage;
	JLabel labelID;
	JLabel labelPW;
	JLabel labelname;
	JLabel labelphone;
	JLabel clikedlabel;
	JLabel seatNumber;
	
	JPanel paneltop;
	JPanel panelmid;
	
	JButton buttonID;
	JButton buttonjoin;
	JButton buttoncancel;
	
	JTextField textID;
	JTextField textname;
	JTextField textPhone;
	
	JPasswordField textPW;
	
	BevelBorder bevelborder = new BevelBorder(BevelBorder.RAISED);
	//MemberDAO memberDao;
	int seatNum=0;
	
	Client client = null;
	
	MemberDTO dto = new MemberDTO();
	
	Thread thread = new Thread();
	
	public Login_Join() {}
	public Login_Join(Client client, int seatNum) {
		this.client = client;
		this.seatNum = seatNum;
		setTitle("KGITBANK");
		setLayout(null);
		setSize(500, 540);
		setLocation(950,40);
		container = getContentPane();
		//memberDao = new MemberDAO();
		
		init();
		start();
		
		setVisible(true);
		setResizable(false);
	}
	private void init() {
		container.setBackground(Color.GRAY);
		seatNumber = new JLabel("좌석번호");
		seatNumber.setFont(new Font("Sherif", Font.BOLD, 14));
		seatNumber.setForeground(Color.ORANGE);
		seatNumber.setBounds(20,15,70,50);

		labelseat = new JLabel();
		labelseat.setFont(new Font("Sherif", Font.BOLD, 35));
		labelseat.setForeground(Color.ORANGE);
		labelseat.setBounds(90, 15, 80, 50);
		labelseat.setText(String.valueOf(seatNum));
		labelmessage = new JLabel("저희 매장에 방문하신 것을 환영합니다.");
		labelmessage.setFont(new Font("Sherif", Font.BOLD, 16));
		labelmessage.setForeground(Color.orange);
		labelmessage.setBounds(20, 15, 400, 150);
		paneltop = new JPanel();
		paneltop.setLayout(null);
		paneltop.setBounds(20, 10, 455, 120);
		paneltop.setBackground(Color.BLACK);
		paneltop.add(labelmessage);
		paneltop.add(labelseat);
		paneltop.add(seatNumber);

		labelID = new JLabel(" 아이디");
		labelID.setForeground(new Color(0X5D5D5D));
		labelID.setFont(new Font("Sherif", Font.BOLD, 15));
		labelID.setBounds(35, 60, 50, 25);
		textID = new JTextField(15);
		//textID.setText("");
		
		clikedlabel = new JLabel("*  중복확인을 부탁드립니다.");
		clikedlabel.setFont(new Font("Sherif", Font.BOLD, 15));
		clikedlabel.setForeground(Color.red);
		clikedlabel.setBounds(40,88,220,30);
		textID.setBounds(100, 60, 150, 30);
		labelPW = new JLabel("비밀번호");
		labelPW.setForeground(new Color(0X5D5D5D));
		labelPW.setFont(new Font("Sherif", Font.BOLD, 15));
		labelPW.setBounds(30, 120, 80, 25);
		textPW = new JPasswordField();
		textPW.setText("");
		textPW.setBounds(100, 120, 150, 30);
		buttonID = new JButton("중복확인");
		buttonID.setForeground(Color.WHITE);
		buttonID.setBackground(Color.gray);
		buttonID.setBounds(280, 60, 120, 30);
		buttonID.setBorder(bevelborder);
		buttonID.setFocusPainted(false);
		labelname = new JLabel("이  름");
		labelname.setForeground(new Color(0X5D5D5D));
		labelname.setFont(new Font("Sherif", Font.BOLD, 15));
		labelname.setBounds(45, 180, 60, 30);
		textname = new JTextField();
		textname.setBounds(100, 180, 150, 30);
		labelphone = new JLabel("휴대폰번호");
		labelphone.setForeground(new Color(0X5D5D5D));
		labelphone.setFont(new Font("Sherif", Font.BOLD, 15));
		labelphone.setBounds(10, 240, 150, 30);
		textPhone = new JTextField();
		textPhone.setBounds(100, 240, 150, 30);
		buttonjoin = new JButton("회원가입완료");
		buttonjoin.setBackground(Color.gray);
		buttonjoin.setForeground(Color.WHITE);
		buttonjoin.setBounds(80, 300, 120, 40);
		buttonjoin.setBorder(bevelborder);
		buttonjoin.setFocusPainted(false);
		buttoncancel = new JButton("취  소");
		buttoncancel.setBackground(Color.GRAY);
		buttoncancel.setForeground(Color.white);
		buttoncancel.setBounds(260, 300, 120, 40);
		buttoncancel.setBorder(bevelborder);
		buttoncancel.setFocusPainted(false);
		panelmid = new JPanel();
		panelmid.setLayout(null);
		panelmid.setBounds(20, 120, 455, 375);
		panelmid.setBackground(Color.LIGHT_GRAY);
		panelmid.add(labelID);
		panelmid.add(textID);
		panelmid.add(labelPW);
		panelmid.add(textPW);
		panelmid.add(buttonID);
		panelmid.add(labelname);
		panelmid.add(textname);
		panelmid.add(labelphone);
		panelmid.add(textPhone);
		panelmid.add(buttonjoin);
		panelmid.add(buttoncancel);
		panelmid.add(clikedlabel);

		textname.setEditable(false);
		textPhone.setEditable(false);
		textPW.setEditable(false);
		buttonjoin.setEnabled(false);
		
		container.add(paneltop);
		container.add(panelmid);
	}
	
	private void start() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		buttonID.addActionListener(this);
		buttonjoin.addActionListener(this);
		buttoncancel.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String id = textID.getText();
		int chk_result = 0;
		
		//아이디 중복체크
		if (e.getSource()==buttonID) {
			
			if (id==null ||id.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "아이디를 입력해주세요");
				
			} else {
				dto = new MemberDTO();
				dto.setId(id);
				client.request_checkId(dto); //아이디중복
				dto = null;
				while(true) {//아이디 중복 Check
					if(client.getIdCheck_result()==2) {
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else break;
				}
				System.out.println(client.getIdCheck_result());
				chk_result = client.getIdCheck_result();
				client.setIdCheck_result(2);
				//chk_result = memberDao.check_id(id); //아이디 중복 확인
				
				if (chk_result==0) {//중복 X (사용할 수 있는 아이디)
					JOptionPane.showInternalMessageDialog(container, "사용하실 수 있는 아이디입니다");
					clikedlabel.setText("");
					textname.setEditable(true);
					textPhone.setEditable(true);
					textPW.setEditable(true);
					buttonjoin.setEnabled(true);
				} else if (chk_result==1) {//중복 id
					JOptionPane.showInternalMessageDialog(container, "이미 등록된 아이디입니다. \n다른 아이디를 입력해주세요");
					textID.setText("");
				}
			}
		}
		
		//회원 가입
		if (e.getSource()==buttonjoin) {
			//입력 안한 항목이 있을 경우
			id = textID.getText();
			String pw = new String(textPW.getPassword());
			String name = textname.getText();
			String phone = textPhone.getText();

			if (id==null || id.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "아이디를 입력해주세요.");
			} else if (pw==null || pw.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "비밀번호를 입력해주세요.");
			} else if (name==null || name.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "이름을 입력해주세요.");
			} else if (phone==null || phone.equals("")) {
				JOptionPane.showInternalMessageDialog(container, "전화번호를 입력해주세요.");
			}

			if (!id.equals("")&&!pw.equals("")&&!name.equals("")&&!phone.equals("")) {
				
				dto = new MemberDTO();
				dto.setId(id);
				dto.setPw(pw);
				dto.setName(name);
				dto.setPhone(phone);
				client.request_Join(dto);
				dto = null;
				
				while(true) {//회원가입
					if(client.getJoin_result()==2) {
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else break;
				}
				int result = client.getJoin_result();
				client.setJoin_result(2);
				
				//int result = memberDao.insert(id, pw, name, phone);
				if (result == 1) {
					JOptionPane.showInternalMessageDialog(container, "회원등록 성공!");
					dispose();
				} else {
					JOptionPane.showInternalMessageDialog(container, "회원등록 실패...");
					textID.setText("");
					textPW.setText("");
					textname.setText("");
					textPhone.setText("");
				}
			}
		}
		
		if (e.getSource()==buttoncancel) {
			dispose(); //창 닫기
		}
	}
}