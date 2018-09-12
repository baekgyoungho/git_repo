package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import DB.MemberDAO;
import DB.MemberDTO;

public class User_Edit extends JFrame implements ActionListener {

	int ans = 0;
	User user = null;

	Container container;
	JLabel info; // ȸ�� ���� ����
	JLabel title1, title2, title3; // pw,pwȮ��,phone
	JTextField pw, re_pw, phone; // pw,pwȮ��,phone
	JButton user_delete, submit, cancel; // ȸ��Ż��,Ȯ��,���
	JPanel button_bind, edit_bind, all_bind; // ��ư,����,All
	JPanel pw_panel, re_pw_panel, phone_panel;// pw,re_pw,phone
	Box box1, box2, box3, box4;
	Color font_color, border_color, bg_color;
	MemberDTO dto = null;
	//MemberDAO dao = new MemberDAO();
    Client client = null;
    Thread thread = new Thread();
	
	public User_Edit(Client client, User user, MemberDTO dto) {
		this.client = client;
		this.dto = dto;
		this.user = user;
		setTitle("ȸ������ ����");
		setSize(350, 250);
		setLocation(1520, 250);
		setResizable(false);

		info = new JLabel("ȸ������ ����", JLabel.LEFT);
		title1 = new JLabel("  Password", JLabel.LEFT);
		title2 = new JLabel("  Password Ȯ��", JLabel.LEFT);
		title3 = new JLabel("  Phone", JLabel.LEFT);

		pw = new JTextField();
		re_pw = new JTextField();
		phone = new JTextField();

		user_delete = new JButton("ȸ��Ż��");
		submit = new JButton("Ȯ��");
		cancel = new JButton("���");

		button_bind = new JPanel();
		edit_bind = new JPanel();
		all_bind = new JPanel();

		pw_panel = new JPanel();
		re_pw_panel = new JPanel();
		phone_panel = new JPanel();

		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createVerticalBox();
		box4 = Box.createVerticalBox();

		font_color = new Color(235, 235, 235);
		border_color = new Color(50, 50, 50);
		bg_color = new Color(90, 90, 95);

		init();
		start();

		setVisible(true);
	}

	public void init() {
		container = getContentPane();
		container.setBackground(border_color);
		container.setLayout(new BorderLayout());
		container.add("Center", all_bind);

		container.add("West", box1);
		container.add("East", box2);
		container.add("North", box3);
		container.add("South", box4);

		all_bind.setLayout(new BorderLayout());
		all_bind.setBackground(border_color);
		all_bind.add("Center", edit_bind);
		all_bind.add("South", button_bind);

		// ���������� ������Ʈ�� �߰��ϴ� Box ��ü�� �����Ѵ�.

		box1.add(Box.createHorizontalStrut(10));// ���� ���� ����
		box2.add(Box.createHorizontalStrut(10));// ���� ���� ����
		box3.add(Box.createVerticalStrut(10));// ���� ���� ����
		box4.add(Box.createVerticalStrut(10));// ���� ���� ����

		edit_bind.setLayout(new GridLayout(4, 1, 0, 10));
		edit_bind.add(info);
		edit_bind.add(pw_panel);
		edit_bind.add(re_pw_panel);
		edit_bind.add(phone_panel);
		edit_bind.setBackground(bg_color);
		button_bind.setBackground(bg_color);

		info.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		info.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		info.setForeground(font_color);

		pw_panel.setLayout(new BorderLayout());
		re_pw_panel.setLayout(new BorderLayout());
		phone_panel.setLayout(new BorderLayout());

		pw_panel.add("West", title1);
		re_pw_panel.add("West", title2);
		phone_panel.add("West", title3);

		pw_panel.add("Center", pw);
		re_pw_panel.add("Center", re_pw);
		phone_panel.add("Center", phone);

		pw_panel.setBackground(bg_color);
		re_pw_panel.setBackground(bg_color);
		phone_panel.setBackground(bg_color);

		title1.setForeground(font_color);
		title2.setForeground(font_color);
		title3.setForeground(font_color);

		title1.setPreferredSize(new Dimension(100, 50));
		title2.setPreferredSize(new Dimension(100, 50));
		title3.setPreferredSize(new Dimension(100, 50));

		button_bind.setLayout(new FlowLayout());
		button_bind.add(user_delete);
		button_bind.add(submit);
		button_bind.add(cancel);

		user_delete.setBackground(bg_color);
		submit.setBackground(bg_color);
		cancel.setBackground(bg_color);

		user_delete.setForeground(font_color);
		submit.setForeground(font_color);
		cancel.setForeground(font_color);

		user_delete.setPreferredSize(new Dimension(60, 30));
		submit.setPreferredSize(new Dimension(60, 30));
		cancel.setPreferredSize(new Dimension(60, 30));

		user_delete.setBorder(new BevelBorder(BevelBorder.RAISED, Color.white, Color.DARK_GRAY));
		submit.setBorder(new BevelBorder(BevelBorder.RAISED, Color.white, Color.DARK_GRAY));
		cancel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.white, Color.DARK_GRAY));

		// BevelBorder(BevelBorder.RAISED,Color.DARK_GRAY,Color.li));
		user_delete.setFocusPainted(false);
		submit.setFocusPainted(false);
		cancel.setFocusPainted(false);

	}

	public void start() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		user_delete.addActionListener(this);
		submit.addActionListener(this);
		cancel.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {

		//ȸ�� Ż��
		if (e.getSource() == user_delete) {
			int ans;
			if (!pw.getText().equals("")||!re_pw.getText().equals("")||pw.getText().equals(re_pw.getText())) {
				if (pw.getText().equals(dto.getPw())) {
					ans = (JOptionPane.showConfirmDialog(this, "������ Ż���Ͻðڽ��ϱ�?", "Ȯ��", JOptionPane.YES_NO_OPTION));
					if (ans == 0) { //Ż�� ����
						client.request_delete(dto);
						//dao.delete(dto.getId(), dto.getPw());
						JOptionPane.showMessageDialog(this, "Ż��Ǿ����ϴ�.", "Ȯ��", JOptionPane.INFORMATION_MESSAGE);
						dispose();
						//ȸ��â ���� �� sales DB & ������â ������Ʈ
			            client.request_salesUpdate(1, user.use_time_value*20);
						//user.sao.update(1, user.use_time_value*20); //pc�̿� ���� ������Ʈ
						client.request_deleteUserInfo(user.seatNum);
						//Manage.getInstance().delete_userInfo(user.seatNum); //������â���� ����� ���� ����
						client.request_seatMinus();
						//Client_Login.currentSeat--; //�̿��� �� -1
						client.request_seatUpdate();
						//Manage.getInstance().seat_update(); //������â ���� ������Ʈ
						user.time_cal.interrupt();
						user.dispose();
					} else {
						JOptionPane.showMessageDialog(this, "Ż�� ���", "Ȯ��", JOptionPane.INFORMATION_MESSAGE);
						pw.setText("");
						re_pw.setText("");
						pw.requestFocus(); //��й�ȣ �Է¶����� Ŀ�� �̵�
					}
				} else { //��й�ȣ, ��й�ȣ Ȯ���� �������� ���� ���
					JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ���ϼ���", "Ȯ��", JOptionPane.ERROR_MESSAGE);
					pw.setText("");
					re_pw.setText("");
					phone.setText("");
					pw.requestFocus(); //��й�ȣ �Է¶����� Ŀ�� �̵�
				}
			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ���ϼ���", "Ȯ��", JOptionPane.ERROR_MESSAGE);
				pw.setText("");
				re_pw.setText("");
				phone.setText("");
				pw.requestFocus(); //��й�ȣ �Է¶����� Ŀ�� �̵�
			}
		}
			
		//Ȯ��
		else if (e.getSource() == submit) {
			
			//��й�ȣ & ��ȭ��ȣ ��� ����
			if (!(pw.getText().equals("")) && pw.getText().equals(re_pw.getText()) && !(phone.getText().equals(""))) {
				MemberDTO temp_dto = new MemberDTO();
				temp_dto.setId(dto.getId());
				temp_dto.setPw(pw.getText());
				temp_dto.setPhone(phone.getText());
				client.request_update(temp_dto);
				while(true) {//ȸ������ ����
					if(client.getEdit_result()==2) {
						try {
							thread.sleep(500);
						} catch (InterruptedException e1) {
							//e1.printStackTrace();
						}
					}else break;
				}
				int result = client.getEdit_result();
				client.setEdit_result(2);
				//int result = dao.update(dto.getId(), pw.getText(), phone.getText()); // pw,phone 2���� ����
				if (result==1) {
					JOptionPane.showMessageDialog(this, "��й�ȣ & ����ó ���� �Ϸ�", "ȸ������ ����", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				
			//��й�ȣ�� ����	
			} else if (!pw.getText().equals("") && !re_pw.getText().equals("") && phone.getText().equals("")) {
				// ���� ��й�ȣ�� ������
				if (pw.getText().equals(re_pw.getText()) && pw.getText().equals(dto.getPw())) {
					JOptionPane.showMessageDialog(this, "���� ��й�ȣ�� �����ϴ�", "", JOptionPane.ERROR_MESSAGE);
					pw.setText("");
					re_pw.setText("");
				//��й�ȣ ���� ����
				}	else if (!(pw.getText().equals(re_pw.getText()))) {
					JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ�����ּ���", "ȸ������ ����", JOptionPane.ERROR_MESSAGE);
				//��й�ȣ ���� ����
				}	else if (pw.getText().equals(re_pw.getText())) {
					MemberDTO temp_dto = new MemberDTO();
					temp_dto.setId(dto.getId());
					temp_dto.setPw(pw.getText());
					temp_dto.setPhone(dto.getPhone());
					client.request_update(temp_dto);
					//dao.update(dto.getId(), pw.getText(), dto.getPhone()); // pw�� ����
					JOptionPane.showMessageDialog(this, "��й�ȣ��! ���� �Ϸ�", "ȸ������ ����", JOptionPane.INFORMATION_MESSAGE);
					dto.setPw(pw.getText());
					dispose();
					
				//��й�ȣ ���� ����
				} else if (!(pw.getText().equals(re_pw.getText()) && phone.getText().equals(""))) {
					JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ�����ּ���", "ȸ������ ����", JOptionPane.ERROR_MESSAGE);
				}
				
				// pw�ؽ�Ʈ�� �����̰� �׸��� phone�ؽ�Ʈ�� ������ �ƴҶ� = ��ȭ��ȣ�� ����
			} else if (pw.getText().equals("") && !(phone.getText().equals(""))) {
				MemberDTO temp_dto = new MemberDTO();
				temp_dto.setId(dto.getId());
				temp_dto.setPw(dto.getPhone());
				temp_dto.setPhone(phone.getText());
				client.request_update(temp_dto);
				//dao.update(dto.getId(), dto.getPw(), phone.getText());// phone�� ����
				JOptionPane.showMessageDialog(this, "����ó��! ���� �Ϸ�", "ȸ������ ����", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ �Ǵ� ����ó�� �Է����ּ���", "", JOptionPane.ERROR_MESSAGE);	
			}

			//��� ��ư
		 } else if (e.getSource() == cancel) 	dispose();
	}
}

