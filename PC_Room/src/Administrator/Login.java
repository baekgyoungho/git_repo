package Administrator;
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

import DB.MemberDAO;
import DB.MemberDTO;

public class Login extends JFrame implements ActionListener{
	public static int currentSeat=0;
	public final static int TOT_SEAT=30;
	
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
	
	MemberDTO dto;
	MemberDAO dao = new MemberDAO();
	
	//Manage manage = null;
	

	public Login() { //�����ڿ� �α���

		setTitle("KGITBANK");
		setLayout(null);
		setSize(400, 400);
		setLocation(1450, 40);
		container = getContentPane();
		dto = new MemberDTO();

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
		
		labelmember = new JLabel("> ������ �α���");

			labelmember.setBounds(10,10,300,15);
			labelmember.setForeground(Color.BLUE);
			labelmember.setFont(new Font("Sherif", Font.BOLD, 15));
			buttonstart= new JButton(new ImageIcon("img/btLogin_hud.png"));
			buttonstart.setBorder(bevelborder);
			buttonstart.setBounds(70,135, 90, 40);
			buttonstart.setBackground(Color.YELLOW);
			buttonstart.setForeground(Color.WHITE);
			buttonstart.setFocusPainted(false);
			buttonpcoff = new JButton("PC����");
			buttonpcoff.setBorder(bevelborder);
			buttonpcoff.setBounds(190,135,90,40);
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
			panelmid.add(buttonpcoff);

			container.add("North",paneltop);
			container.add("Center", panelmid);
	}
	private void start() {							
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		buttonstart.addActionListener(this);
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
				
			} 	else if(seatNum==0 && id.equals("system") &&  pw.equals("1234") ) { //������ �α���
				Manage.getInstance();//������ ȭ�� ȣ��
				dispose();

			}else{
				labelmember.setText("> ������ �α��� - ������ ���Ӹ� �����մϴ�");
				labelmember.setForeground(Color.RED);
				textID.setText("");
				textPW.setText("");
				textID.requestFocus();  //textID�� Ŀ�� �̵�
				return;
			}
		}
		
		if (e.getSource()==buttonpcoff) {
			setVisible(false);
		}
	}

}