package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

//import DB.MemberDAO;
import DB.MemberDTO;

public class Client_Login extends JFrame implements ActionListener{
	
	Container container ;

	JLabel seatNumber = new JLabel("�¼���ȣ");
	JLabel labelseat = new JLabel("00");
	JLabel labelmessage;
	JLabel labelmember;
	JLabel labelID;
	JLabel labelPW;

	JButton buttonstart;
	JButton buttonIDPW;
	JButton buttonjoin;
	JButton buttonpcoff;

	JPanel paneltop;
	JPanel panelmid;

	JTextField textID;
	JPasswordField textPW;

	int seatNum = 0; //�ڸ� ��ȣ
	
	Login_Find login_find = null;
	Login_Join login_join = null;
	
	MemberDTO dto = new MemberDTO();;
	//MemberDAO dao = new MemberDAO();
	
	//Manage manage = null;
	//"127.0.0.1" : �ڱ� PC�� �⺻ IP�ּ�
	Client client = null;

	Thread thread = new Thread();
	
	public Client_Login() { //����� �α���

		client = new Client("localhost",5420);
        client.user_code = 1; // ������ ���α׷�
		//���Ͽ��� ���н�
		if(!client.socket_state) {
			return;
		}
		client.giveAndTake();
		client.request_seatNum();
		while(true) {//�¼���ȣ �������µ� �ɸ��½ð� ó��
			if(client.getSeatNum()==0) {
				System.out.println(client.getSeatNum());
				try {
					thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else break;
		}
		this.seatNum = client.getSeatNum();
		client.setSeatNum(0);
		setTitle("KGITBANK");
		setLayout(null);
		setSize(400, 400);
		setLocation(1450, 40);
		container = getContentPane();

		labelseat.setText(String.valueOf(seatNum));
		init();
		start();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
		setResizable(false);
	}
	
	public Client_Login(int seatNum) { //����� �α���
		client = new Client("localhost",5420);
		client.user_code = 2; // �����ڰ� ����
		client.giveAndTake();
		this.seatNum = seatNum;
		setTitle("KGITBANK");
		setLayout(null);
		setSize(400, 400);
		setLocation(1450, 40);
		container = getContentPane();

		labelseat.setText(String.valueOf(seatNum));
		init();
		start();

		setVisible(true);
		setResizable(false);
	}
	
	private void init() {
		container.setBackground(Color.GRAY);
		BevelBorder bevelborder = new BevelBorder(BevelBorder.RAISED);
		
		seatNumber.setFont(new Font("Sherif", Font.BOLD, 14));
		seatNumber.setForeground(Color.orange);
		seatNumber.setBounds(20,15,70,50);
		
		labelseat.setFont(new Font("Sherif", Font.BOLD, 35));
		labelseat.setForeground(Color.ORANGE);
		labelseat.setBounds(90,15,80,50);
		labelseat.setText(String.valueOf(seatNum));
		labelmessage = new JLabel("���� ���忡 �湮�Ͻ� ���� ȯ���մϴ�.");
		labelmessage.setFont(new Font("Sherif", Font.BOLD, 14));
		labelmessage.setForeground(Color.orange);
		labelmessage.setBounds(10,50,280,100);
		paneltop = new JPanel();
		paneltop.setLayout(null);
		paneltop.setBounds(10, 10, 375,130 );
		paneltop.setBackground(Color.BLACK);
		paneltop.add(labelmessage);
		paneltop.add(labelseat);
		paneltop.add(seatNumber);

		labelID = new JLabel(" ���̵�");
		labelID.setForeground(new Color(0X5D5D5D));
		labelID.setFont(new Font("Sherif", Font.BOLD, 15));
		labelID.setBounds(30,40,50,25);
		textID = new JTextField(15);
		//textID.setText("���̵� �Է�");
		textID.setBounds(100, 40, 150, 30);
		labelPW = new JLabel("��й�ȣ");
		labelPW.setForeground(new Color(0X5D5D5D));
		labelPW.setFont(new Font("Sherif", Font.BOLD, 15));
		labelPW.setBounds(30,85,80,25);
		textPW = new JPasswordField();
		textPW.setText("");
		textPW.setBounds(100,85, 150, 30);
		
		labelmember = new JLabel("> ȸ�� �α���");


		labelmember.setBounds(10,10,400,15);
		labelmember.setForeground(Color.BLUE);
		labelmember.setFont(new Font("Sherif", Font.BOLD, 15));
		buttonstart= new JButton(new ImageIcon("img/btLogin_hud.png"));
		buttonstart.setBorder(bevelborder);
		buttonstart.setBounds(10,135, 90, 40);
		buttonstart.setBackground(Color.YELLOW);
		buttonstart.setForeground(Color.WHITE);
		buttonstart.setFocusPainted(false);
		
		buttonIDPW = new JButton("ID/PW ã��");
		buttonIDPW.setBorder(bevelborder);
		buttonIDPW.setBounds(100,135,90,40);
		buttonIDPW.setBackground(Color.GRAY);
		buttonIDPW.setForeground(Color.WHITE);
		buttonIDPW.setFocusPainted(false);
		buttonjoin = new JButton("ȸ������");
		buttonjoin.setBorder(bevelborder);
		buttonjoin.setBounds(190,135,90,40);
		buttonjoin.setBackground(Color.GRAY);
		buttonjoin.setForeground(Color.WHITE);
		buttonjoin.setFocusPainted(false);
		buttonpcoff = new JButton("PC����");
		buttonpcoff.setBorder(bevelborder);
		buttonpcoff.setBounds(280,135,90,40);
		buttonpcoff.setBackground(Color.RED);
		buttonpcoff.setForeground(Color.WHITE);
		buttonpcoff.setFocusPainted(false);
		panelmid = new JPanel();
		panelmid.setLayout(null);
		panelmid.setBounds(10, 150, 375, 210);
		panelmid.setBackground(Color.LIGHT_GRAY);
		panelmid.add(labelmember);
		panelmid.add(labelID);
		panelmid.add(textID);
		panelmid.add(labelPW);
		panelmid.add(textPW);
		panelmid.add(buttonstart);
		panelmid.add(buttonIDPW);
		panelmid.add(buttonjoin);
		panelmid.add(buttonpcoff);
		
		container.add("North",paneltop);
		container.add("Center", panelmid);

	}
	private void start() {							
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if(client.user_code==1) setDefaultCloseOperation(EXIT_ON_CLOSE);
        else if(client.user_code==2) setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		buttonstart.addActionListener(this);
		buttonIDPW.addActionListener(this);
		buttonjoin.addActionListener(this);
		buttonpcoff.addActionListener(this);
		textID.addActionListener(this);
		textPW.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//�α���
		if (e.getSource()==buttonstart || e.getSource() == textPW) { 
			String id = textID.getText();
			String pw = String.valueOf(textPW.getPassword());
			String result = null;
			
			if(id.equals("")||pw.equals("")) {
				JOptionPane.showInternalMessageDialog(container,"���̵�/��й�ȣ�� �Է����ּ���");
				return;
				
			} else { //����� �α���
				dto.setId(id);
				dto.setPw(pw);
				client.request_login(dto);
				dto = null;
				while(true) {//�α��� ���� �������µ� �ɸ��½ð� ó��
					if(client.login_result==0) {
						System.out.println("�α��� ���� ����������");
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}else break;
				}
				dto = client.getDto();
				client.login_result = 0;
				//dto=dao.login(id, pw);				
				if (dto!=null) result = dto.getId();
				if (result==null) {
					JOptionPane.showInternalMessageDialog(container,"���̵�/��й�ȣ�� �ٽ� Ȯ�����ּ���");
				} else if (result.equals(id)) { //�α��� ������ ���
					
					//�ߺ� �α��� ����
					client.request_Multiple(dto);
					int check = 0;
					while(true) {//�ߺ��α��� üũ ��� �������µ� �ɸ��½ð�ó��
						if(client.getCheck()==0) {
							System.out.println(client.getCheck());
							try {
								thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}else break;
					}
					check = client.getCheck();
					client.setCheck(0);
					if (check == 1) {//�����  : 1
						JOptionPane.showInternalMessageDialog(container,"���� ��� ���� �����Դϴ�. \n�ߺ� �α��� �Ͻ� �� �����ϴ�");
						return;
					}// �̻���� : 2

					//�α��� ������ ����¼��� ����
					client.request_seatPlus();
					//Client_Login.currentSeat++;
					
					new User(client, dto, seatNum);//dto & �ڸ���ȣ �ѱ��
					dispose(); //�α��� ���� �� ����â �Ⱥ��̰� ����
				} 

			}
		}
		if (e.getSource()==buttonIDPW) {
			if (login_find!=null) {
				login_find.textIDname.setText("");
				login_find.textIDphone.setText("");
				login_find.textPWname.setText("");
				login_find.textPWphone.setText("");
				login_find.setVisible(true);
			}
			else login_find = new Login_Find(client, seatNum);
		}
		if (e.getSource()==buttonjoin) {
			if (login_join!=null) {
				login_join.textID.setText("");
				login_join.textPW.setText("");
				login_join.textname.setText("");
				login_join.textPhone.setText("");
				login_join.setVisible(true);
			}
			else login_join = new Login_Join(client, seatNum);
		}
		if (e.getSource()==buttonpcoff) {
			setVisible(false);
			if(client.user_code==1) System.exit(0);
		}
	}
}