package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import DB.MemberDAO;
import DB.MemberDTO;
import DB.SalesDAO;
import DB.User_infoDTO;


public class User extends JFrame implements ActionListener,Runnable{

	
	Container container;
	JLabel info;//�������
	JLabel title1,title2,title3,title4;             //�ڸ���ȣ,�α��νð�,���ð�,�����ð�
	JLabel seat_num,login_time,use_time,remain_time;//�ڸ���ȣ,�α��νð�,���ð�,�����ð�
    JButton order,seat,charge,edit,exit;            //��ǰ�ֹ�,�ڸ��̵�,�������,��������,�������
	JPanel info_bind,button_bind;//�������,��ư
	
	JPanel info_panel,seat_panel,lt_panel,ut_panel,rt_panel; //�������,�ڸ���ȣ,�α��νð�,���ð�,�����ð�
	JPanel all_bind;
    Box box1,box2,box3,box4;
    Color font_color,border_color,bg_color;
    Calendar cal = Calendar.getInstance();
    
    String current_time;
    int use_time_value=0;
    int remain_time_value=0;
    
    Thread time_cal = new Thread(this);
    MemberDTO dto = null;
    //MemberDAO dao = new MemberDAO();
    
    //SalesDAO sao = new SalesDAO();
    
    int seatNum=0;
    String updateSeat="�ʱⰪ";
    
    User_Order order_object = null;
    User_Seat user_seat = null;
    User_Edit user_edit = null;
   
    Client client = null;
    Thread thread = new Thread();
    
    public User() { }
    
	public User(Client client,MemberDTO dto, int seatNum) {
        

		this.client = client;
		this.dto = dto;
		this.seatNum = seatNum;
		
		setTitle("����� ȭ��");
		setSize(400, 250);
		
		client.request_getseatCount();// 1202 ��� �¼� ���� ��û
		while(true) {//��� �¼� ���� �������µ� �ɸ��� �ð�
			if(client.getSeatCount()==0) {
				System.out.println(client.getSeatCount());
				try {
					thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("thread.sleep Error");
				}
			}else break;
		}
		int currentSeat = client.getSeatCount();
		client.setSeatCount(0);
		if (currentSeat==1)	setLocation(1520, 000);
		else { //1920*1080 ����
			setLocation(1520-(currentSeat/5*400), 250*((currentSeat-1)%4));
		}
		
		setResizable(false);
		
		current_time = cal.get(Calendar.YEAR)+"�� "+(cal.get(Calendar.MONTH)+1)+"�� "
		        +cal.get(Calendar.DAY_OF_MONTH)+"��"+	cal.get(Calendar.HOUR_OF_DAY)+"�� "
				+cal.get(Calendar.MINUTE)+"��";
		
		info        = new JLabel(" �������");
		title1      = new JLabel("     �ڸ� ��ȣ    :   ");
		title2      = new JLabel("     ���� �ð�    :   ");
		title3      = new JLabel("     ��� �ð�    :   ");
		title4      = new JLabel("     ���� �ð�    :   ");
		seat_num    = new JLabel(String.valueOf(seatNum));
		login_time  = new JLabel(current_time);
		use_time    = new JLabel("00:00");
		remain_time = new JLabel("00:00");

		
		order  = new JButton("��ǰ�ֹ�");
		seat   = new JButton("�ڸ��̵�");
		charge = new JButton("��ݰ���");
		edit   = new JButton("��������");
		exit   = new JButton("�������");
		
		info_bind   = new JPanel();
		button_bind = new JPanel();
		
		seat_panel = new JPanel();
		lt_panel   = new JPanel(); //�α��νð�
		ut_panel   = new JPanel(); //���ð�
		rt_panel   = new JPanel(); //�����ð�
		info_panel = new JPanel();
		all_bind   = new JPanel();
		
		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createVerticalBox();
		box4 = Box.createVerticalBox();
		
		font_color = new Color(235,235,235);
		border_color = new Color(50,50,50);
		bg_color = new Color(90,90,95);
		
		init();
		start();
		
        if(client.user_code==1) setDefaultCloseOperation(EXIT_ON_CLOSE);
        else if(client.user_code==2) setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		time_cal.start();
		setVisible(true);
		
		
		int result = 0;
		//���� �ð�
		if(dto.getMoney()<=0) {
			result = money_charging();
		}else {
			remain_time_value = dto.getMoney()/20;
			remain_time.setText(time_make(remain_time_value));
			System.out.println(remain_time_value);
			System.out.println(use_time_value);
		}
		
		if(result!=3) {// 3�� �ƴҶ� - 3 : ���� 0�ε� ������ ��������������
			client.request_seatUpdate();//�¼� �̿�� ������Ʈ
			User_infoDTO user_info = new User_infoDTO(dto, seatNum, 
					remain_time.getText(),remain_time_value*20,use_time_value*20); //������â�� ���� ���� ������Ʈ
			client.request_update_UserInfo(1300,user_info);
			//Manage.getInstance().update_userInfo(dto, seatNum,remain_time.getText()); //������â�� ���� ���� ������Ʈ
		}
	}


	public void init() {
		
		container = getContentPane();
		container.setBackground(border_color);
		container.setLayout(new BorderLayout());
		
		all_bind.setLayout(new BorderLayout(10,10));
		all_bind.setBackground(border_color);
		all_bind.add("Center",info_bind);
		all_bind.add("South",button_bind);

		
		container.add("Center",all_bind);
		container.add("West",box1);
		container.add("East",box2);
		container.add("North",box3);
		container.add("South",box4);
		
		info_bind.setLayout(new GridLayout(5, 1,0,1));
		info_bind.add(info_panel);
		info_bind.add(seat_panel);
		info_bind.add(lt_panel);
		info_bind.add(ut_panel);
		info_bind.add(rt_panel);
		info_bind.setBorder(new LineBorder(border_color, 1));
		
		box1.add(Box.createHorizontalStrut(10));
		box2.add(Box.createHorizontalStrut(10));
		box3.add(Box.createVerticalStrut(10));
		box4.add(Box.createVerticalStrut(5));
		
		
		
		info_bind.setBackground(new Color(110, 110, 110));
		info_panel.setBackground(bg_color);
		seat_panel.setBackground(bg_color);
		lt_panel.setBackground(bg_color);
		ut_panel.setBackground(bg_color);
		rt_panel.setBackground(bg_color);


		//�������
		info_panel.setLayout(new BorderLayout());
		info_panel.add("Center",info);
		
		info.setForeground(font_color);
		info.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		info.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		
		//�ڸ���ȣ
		seat_panel.setLayout(new BorderLayout());
		seat_panel.add("West",title1);
		seat_panel.add("Center",seat_num);
		title1.setForeground(font_color);
		seat_num.setForeground(font_color);
		//�α��� �ð�
		lt_panel.setLayout(new BorderLayout());
		lt_panel.add("West",title2);
		lt_panel.add("Center",login_time);
		title2.setForeground(font_color);
		login_time.setForeground(font_color);
		//��� �ð�
		ut_panel.setLayout(new BorderLayout());
		ut_panel.add("West",title3);
		ut_panel.add("Center",use_time);
		title3.setForeground(font_color);
		use_time.setForeground(font_color);


		rt_panel.setLayout(new BorderLayout());
		rt_panel.add("West",title4);
		rt_panel.add("Center",remain_time);
		title4.setForeground(font_color);
		remain_time.setForeground(font_color);
		
		//��ư ���ε�
		button_bind.setLayout(new GridLayout(1,5,1,0));
		button_bind.setBackground(Color.black);
		button_bind.setBorder(new BevelBorder(BevelBorder.RAISED,Color.white,Color.DARK_GRAY));
	    button_bind.setPreferredSize(new Dimension(0, 40));
		button_bind.add(order);
		button_bind.add(seat);
		button_bind.add(charge);
		button_bind.add(edit);
		button_bind.add(exit);
		
		order.setBackground(Color.DARK_GRAY);
		order.setForeground(font_color);

		order.setBorder(new LineBorder(Color.black, 0));
		order.setFocusPainted(false);
		
		seat.setBackground(Color.DARK_GRAY);
		seat.setForeground(font_color);
		seat.setBorder(new LineBorder(Color.black, 0));
		seat.setFocusPainted(false);
		
		charge.setBackground(Color.DARK_GRAY);
		charge.setForeground(font_color);
		charge.setBorder(new LineBorder(Color.black, 0));
		charge.setFocusPainted(false);
		
		edit.setBackground(Color.DARK_GRAY);
		edit.setForeground(font_color);
		edit.setBorder(new LineBorder(Color.black, 0));
		edit.setFocusPainted(false);
		
		exit.setBackground(Color.RED);
		exit.setForeground(Color.WHITE);
		exit.setBorder(new LineBorder(Color.black, 0));
		exit.setFocusPainted(false);
		
	}
	public void start() {
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		order.addActionListener(this);
		seat.addActionListener(this);
		charge.addActionListener(this);
		edit.addActionListener(this);
		exit.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {

				User_infoDTO userInfodto = new User_infoDTO();
				MemberDTO temp_dto = new MemberDTO();
				userInfodto.setDto(temp_dto);
				
				userInfodto.getDto().setId(dto.getId());
				userInfodto.setR_time(remain_time_value*20);
		        userInfodto.setU_time(use_time_value*20);
				
	            client.request_saveAll(userInfodto);
	            temp_dto = null;
	            userInfodto = null;
				//dao.save_all(dto.getId(), remain_time_value*20, use_time_value*20); //user DB ������Ʈ (id, �����ð�(=�ܾ�), ���ð�(=���ݾ�))
	            
                client.request_salesUpdate(1,use_time_value*20);
				//sao.update(1, use_time_value*20);
	            
	            client.request_deleteUserInfo(seatNum);         //������â���� ����� ���� ����
				//Manage.getInstance().delete_userInfo(seatNum); //������â���� ����� ���� ����

	            client.request_seatMinus(); // �̿��� �� -1
	            client.request_seatUpdate();//������â ���� ������Ʈ
				//Client_Login.currentSeat--; //�̿��� �� -1
				//Manage.getInstance().seat_update(); //������â ���� ������Ʈ
				if (order_object!=null)	order_object.setVisible(false);
				if (user_seat!=null)	user_seat.setVisible(false);
				
				setVisible(false);
				time_cal.interrupt();
				
				
				if(client.user_code==1) System.exit(0);
			}
			
		});
		
	}

	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==order) {
			if (order_object!=null) {
				for(int i=0;i<order_object.p.length;i++) {
					if (order_object.p[i]==null) break;
					else order_object.p[i].product_plus.setValue(0);
				}
				order_object.setVisible(true);
			}
			else order_object = new User_Order(client, this);
			
		}else if(e.getSource()==seat) {
			if (user_seat!=null)	{
				user_seat.label.setText("");
				user_seat.setVisible(true);
			}
			else user_seat = new User_Seat(client, this, seatNum);			
			
		}else if(e.getSource()==charge) {
			money_charging();
			
		}else if(e.getSource()==edit) {
			if (user_edit!=null) {
				user_edit.pw.setText("");
				user_edit.re_pw.setText("");
				user_edit.phone.setText("");
				user_edit.setVisible(true);
			}
			else user_edit = new User_Edit(client, this, dto);
			
		}else if(e.getSource()==exit) {

			User_infoDTO userInfodto = new User_infoDTO();
			MemberDTO temp_dto = new MemberDTO();
			userInfodto.setDto(temp_dto);
			
			userInfodto.getDto().setId(dto.getId());
			userInfodto.setR_time(remain_time_value*20);
	        userInfodto.setU_time(use_time_value*20);
			
            client.request_saveAll(userInfodto);
            temp_dto = null;
            userInfodto = null;
			//dao.save_all(dto.getId(), remain_time_value*20, use_time_value*20);  //�ܾ� ������Ʈ
            client.request_salesUpdate(1, use_time_value*20);
            //sao.update(1, use_time_value*20);  //���� �ð� ������Ʈ
			
            client.request_deleteUserInfo(seatNum);         //������â���� ����� ���� ����
			//Manage.getInstance().delete_userInfo(seatNum); //������â���� ����� ���� ����
            
            client.request_seatMinus(); // �̿��� �� -1
            client.request_seatUpdate();//������â ���� ������Ʈ
			//Client_Login.currentSeat--; //�̿��� �� -1
			//Manage.getInstance().seat_update(); //������â ���� ������Ʈ
			
			time_cal.interrupt();
			if(order_object!=null)	order_object.setVisible(false);//��ǰ����â �α���â�� ���� ������
			if (user_seat!=null)	user_seat.setVisible(false);
			setVisible(false);
			if(client.user_code==1)System.exit(0);
		}
		
	}

	@Override
	public void run() {

		String time_str = "";
		System.out.println("%02d:%02d");
		int count = 0;

		while(true) {
			try {
				Thread.sleep(1000);//60000
				if(remain_time_value>0) {
					count = 1;
					use_time_value++;
					
					time_str = time_make(use_time_value);
					use_time.setText(time_str);
					use_time.repaint();
					
					remain_time_value = dto.getMoney()/20-use_time_value;
					
					time_str = time_make(remain_time_value);
					
					remain_time.setText(time_str);
					remain_time.repaint();
					
					User_infoDTO user_info = new User_infoDTO(dto, seatNum, 
							remain_time.getText(),remain_time_value*20,use_time_value*20); //������â�� ���� ���� ������Ʈ
					client.request_update_UserInfo(1301,user_info);
					//Manage.getInstance().update_time(seatNum, time_str, remain_time_value*20, use_time_value*20);
					
				}else if(count == 1) {
					
					User_infoDTO userInfodto = new User_infoDTO();
					MemberDTO temp_dto = new MemberDTO();
					userInfodto.setDto(temp_dto);
					
					userInfodto.getDto().setId(dto.getId());
					userInfodto.setR_time(remain_time_value*20);
			        userInfodto.setU_time(use_time_value*20);
					
		            client.request_saveAll(userInfodto);
		            temp_dto = null;
		            userInfodto = null;
		            //dao.save_all(dto.getId(), remain_time_value*20, use_time_value*20); //user DB ������Ʈ (id, �����ð�(=�ܾ�), ���ð�(=���ݾ�))
	                client.request_salesUpdate(1, use_time_value*20);
	                //sao.update(1, use_time_value*20);
					
		            client.request_deleteUserInfo(seatNum);         //������â���� ����� ���� ����
					//Manage.getInstance().delete_userInfo(seatNum); //������â���� ����� ���� ����
		            
		            client.request_seatMinus(); // �̿��� �� -1
		            client.request_seatUpdate();//������â ���� ������Ʈ
					//Client_Login.currentSeat--; //�̿��� �� -1
					//Manage.getInstance().seat_update(); //������â ���� ������Ʈ
					
					time_cal.interrupt();
					if(order_object!=null)	order_object.setVisible(false);//��ǰ����â �α���â�� ���� ������
					if (user_seat!=null)	user_seat.setVisible(false);
					setVisible(false);
					if(client.user_code==1) System.exit(0);
					
				}
			} catch (InterruptedException e) {
				System.out.println(" Timer ����");
				break;
				//e.printStackTrace();
			}
		}
		
	}
	
	//�Լ�
	public String time_make(int time) {
		int hour =0;
		int min  =0;
		String time_str;
		
		if(time!=0) {
		    min = time%60;
		    hour = time/60;
		}
		time_str = String.format("%02d:%02d", hour,min);

		return time_str;
	}
	
	//��ݰ���â
	public int money_charging() {
		int result = 0;
		Object input = null;
		input = JOptionPane.showInputDialog(container, "�����Ͻ� �ݾ��� �Է����ּ���.", "��ݰ���â",
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(""), null, null);
		
		if(input!=null&&Integer.parseInt(input.toString())>0) {
			remain_time_value = (dto.getMoney()+Integer.valueOf(input.toString()))/20;//��ȣ����
			dto.setMoney(remain_time_value*20);
			remain_time.setText(time_make((dto.getMoney()/20)-use_time_value));
			remain_time.repaint();
			
		}else {
			if(dto.getMoney()<=0) {
				JOptionPane.showMessageDialog(this, "�α׾ƿ��մϴ�.", "���� �ð��� �����ϴ�.", JOptionPane.ERROR_MESSAGE,new ImageIcon(""));
				setVisible(false);
				client.request_seatMinus(); // �̿��� �� -1
				//Client_Login.currentSeat--; //�̿��� �� -1
				result = 3;
			}
		}
		return result;
		//JOptionPane.showMessageDialog(this, "�����߻�", "����", JOptionPane.ERROR_MESSAGE);
	}
	
	//��ǰ����â
	public int product_payment(int pay_money) {

		int result =0;
		if(pay_money < (remain_time_value-10)*20) {//������ �����ð��� 10���̻��ϰ�츸 ó��
			remain_time_value = (dto.getMoney()-pay_money)/20;//��ȣ����
			dto.setMoney(remain_time_value*20);
			remain_time.setText(time_make((dto.getMoney()/20)-use_time_value));
			remain_time.repaint();
			
			JOptionPane.showMessageDialog(this, "���� �Ϸ�", "Information", JOptionPane.ERROR_MESSAGE,new ImageIcon(""));
           // sao.update(2, pay_money); // �ٸ������� ������Ʈ�ؼ� �ּ�
			
		}else {
			if(pay_money < remain_time_value*20) {
				JOptionPane.showMessageDialog(this, "������ �����ð��� 10���̻��ϰ�쿡�� ó�������մϴ�.", "Information", JOptionPane.ERROR_MESSAGE,new ImageIcon(""));
			}else {
				JOptionPane.showMessageDialog(this, "���� �ð��� �����մϴ�.", "Information", JOptionPane.ERROR_MESSAGE,new ImageIcon(""));
			}
			result = 1;
		}
		return result;
		//JOptionPane.showMessageDialog(this, "�����߻�", "����", JOptionPane.ERROR_MESSAGE);
	}
}
