package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import DB.MemberDAO;
import DB.MemberDTO;
import DB.OrderListDTO;
import DB.SalesDAO;


class Product{
    Color bg_color;
    
	JPanel product_bind;//��ǰ�ϳ�
    
	JPanel product;//��ǰ����
	JPanel product_icon_panel;//��ǰ������
	JPanel product_name_panel;//��ǰ�̸�
	JPanel product_price_bind;//��ǰ����
	JPanel product_su;//��ǰ����

	//product_bind_West
	JLabel product_icon;
	//product_bind_Center
	
	JLabel product_name;//product_price_bind
	JLabel product_price_title;//���� :
	JLabel product_price;//product_price_bind
	JLabel product_count;//product_su
	
	//0~100 ������ ���ڰ����� 50�� �⺻ ������ ��Ÿ���� 2�� ������ ���ҽ�Ų��.
	SpinnerNumberModel spinnerNumberModel; 
	JSpinner product_plus;//product_su
	
	Box price_box;
	Box count_box;
	ImageIcon img1;
	

	
	public Product() {
		price_box = Box.createHorizontalBox();
		count_box = Box.createHorizontalBox();
		
		product_bind     = new JPanel();
		
		
		img1 = new ImageIcon("Icon/�䳢.jpg");
		product_icon     = new JLabel(img1);
		product          = new JPanel();
		product_name     = new JLabel("����");
		
		product_icon_panel = new JPanel();
		product_name_panel = new JPanel();
		
		product_price_bind  = new JPanel();
		product_price_title = new JLabel("���� : ",JLabel.RIGHT);
		product_price       = new JLabel("2000");
		
		product_su          = new JPanel();
		product_count       = new JLabel("����  : ",JLabel.RIGHT);
		spinnerNumberModel  = new SpinnerNumberModel(0, 0, 10, 1); 
		product_plus        = new JSpinner();
		product_plus.setModel(spinnerNumberModel);
        
		init();
	}
	
	/**
	 * ��ǰ����
	 * @param path �̹������
	 * @param name ��ǰ��
	 * @param price ��ǰ����
	 */
	public Product(String path,String name,String price) {
		price_box = Box.createHorizontalBox();
		count_box = Box.createHorizontalBox();
		
		product_bind     = new JPanel();
		
		
		img1 = new ImageIcon(path);
		product_icon     = new JLabel(img1);
		product          = new JPanel();
		product_name     = new JLabel(name);
		
		product_icon_panel = new JPanel();
		product_name_panel = new JPanel();
		
		product_price_bind  = new JPanel();
		product_price_title = new JLabel("���� : ",JLabel.RIGHT);
		product_price       = new JLabel(price);
		
		product_su          = new JPanel();
		product_count       = new JLabel("����  : ",JLabel.RIGHT);
		spinnerNumberModel  = new SpinnerNumberModel(0, 0, 10, 1); 
		product_plus        = new JSpinner();
		product_plus.setModel(spinnerNumberModel);

		init();
	}
	
	
	
	
	public void init() {
		price_box.add(Box.createHorizontalStrut(0));
		count_box.add(Box.createHorizontalStrut(90));		
		
		product_bind.setPreferredSize(new Dimension(260,90));
		product_bind.setLayout(new BorderLayout());
		product_bind.setBackground(bg_color);
		//product_bind.setBorder(new LineBorder(Color.black,1));
		
		product_bind.add("West",product_icon_panel);
		product_icon_panel.setLayout(new BorderLayout());
		product_icon_panel.setBorder(new LineBorder(Color.black,1));
		product_icon_panel.setBackground(Color.WHITE);
		product_icon_panel.add(product_icon);
		
		product_bind.add("Center",product);
		product.setLayout(new GridLayout(3, 1));
		//product.setBorder(new LineBorder(Color.black,1));
		product.setBackground(bg_color);
		
		product.add(product_name_panel);
		product_name_panel.setLayout(new BorderLayout());
		//product_name_panel.setBorder(new LineBorder(Color.black,1));
		product_name_panel.setBackground(Color.WHITE);
		product_name_panel.add("Center",product_name);
		product_name.setBorder(new LineBorder(Color.black,1));
		
		product.add(product_price_bind);
		product_price_bind.setLayout(new BorderLayout());
		product_price_bind.setBorder(new LineBorder(Color.black,1));
		product_price_bind.setBackground(Color.WHITE);
		product_price_bind.add("West",product_price_title);
		product_price_bind.add("Center",product_price);
		product_price_bind.add("East",price_box);
		
		product.add(product_su);
		product_su.setLayout(new BorderLayout(1,1));
		product_su.setBorder(new LineBorder(Color.black,1));
		product_su.setBackground(Color.white);
		product_su.add("West",product_count);
		product_su.add("Center",product_plus);
		product_su.add("East",count_box);
	}

	public JPanel getProduct_bind() {
		return product_bind;
	}

	public String getProduct_name() {
		return product_name.getText();
	}

	public int getProduct_price() {
		return Integer.valueOf(product_price.getText());
	}


	public int getProduct_plus() {
		return Integer.valueOf(product_plus.getValue().toString());
	}

	
	
	
}

public class User_Order extends JFrame implements ActionListener,PropertyChangeListener,MouseListener{
	Container container;

	//North
    JScrollPane	category;
	JPanel all_bind,category_List;

    Box box1,box2,box3,box4;
    Color font_color,border_color,bg_color;

	JButton[] button = new JButton[7];
	
	//Center
	Product[] p = new Product[10]; 
	JPanel[] product_bind = new JPanel[10];//��ǰ

	
	JPanel product;//��ǰ����
	JPanel product_icon_panel;//��ǰ������
	JPanel product_name_panel;//��ǰ�̸�
	JPanel product_price_bind;//��ǰ����
	JPanel product_su;//��ǰ����
	JPanel product_all_bind;

	//product_bind_West
	JLabel product_icon;
	//product_bind_Center
	
	JLabel product_name;//product_price_bind
	JLabel product_price_title;//���� :
	JLabel product_price;//product_price_bind
	JLabel product_count;//product_su
	
	//0~100 ������ ���ڰ����� 50�� �⺻ ������ ��Ÿ���� 2�� ������ ���ҽ�Ų��.
	SpinnerNumberModel spinnerNumberModel; 
	JSpinner product_plus;//product_su
	
	Box price_box;
	Box count_box;
	ImageIcon img1;
	
	//JButton product_buy;//����
	JScrollPane product_scroll;
	
	
	//South
	JPanel product_buy_bind;//
	JButton product_buy;//�ֹ��ϱ�
	JButton product_cencel;//�ݱ�
	
	
	//�ֹ��ϱ�
	Container dialogContainer;
	
	JLabel cal_money;//����ҵ�
	JLabel cal_time;//����� �ð�
	JDialog buy_dialog;
	JButton money_cal;// �����
	JButton time_cal;// �ð����
	
	JPanel dialog_button_bind;
	Box dialog_box;
	int[] index = new int[10];//��ǰ �ε���
	

	
	//User ��¥
	User user;
	int price_sum = 0;
	int price_min = 0;
	
	//SalesDAO sao = new SalesDAO();
	//MemberDAO dao = new MemberDAO();
	Client client = null;
	
	public User_Order() { }
    public User_Order(Client client, User user) {

    	this.client = client;
    	this.user = user;
    	
		setTitle("��ǰ ���� ȭ��");
		setSize(550, 500);
		setLocation(960, 0);
		setResizable(false);	
		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createVerticalBox();
		box4 = Box.createVerticalBox();
		
		font_color = new Color(235,235,235);
		border_color = new Color(50,50,50);
		bg_color = new Color(90,90,95);
		
		all_bind = new JPanel();
		category_List = new JPanel();
		category = new JScrollPane(category_List);


		//North
		button[0] = new JButton("�����");     
		button[1] = new JButton("����");      
		button[2] = new JButton("���/��");    
		button[3] = new JButton();          
		button[4] = new JButton();          
		button[5] = new JButton();          
		button[6] = new JButton();          
		
		for(int i=0;i<button.length;i++) {
			button[i].setEnabled(false);
		}
		
		//Center
		price_box = Box.createHorizontalBox();
		count_box = Box.createHorizontalBox();
		
		
		product_all_bind = new JPanel();
		
		p[0] = new Product("Img/�ݶ�.jpg", "�ݶ�", "1500");
		p[1] = new Product("Img/�ݶ�(����).jpg", "�����ݶ�", "1500");
		p[2] = new Product("Img/ȯŸ.jpg", "ȯŸ", "1200");
		p[3] = new Product("Img/����.jpg", "���䷹��", "1200");
		p[4] = new Product("Img/������(��������).jpg", "������(��������)", "1000");
		p[5] = new Product("Img/������(ī���).jpg", "������(ī���)", "1000");
		
		for(int i=0;i<p.length;i++) {//��ǰ ����
			if (p[i] == null)
				break;
			product_bind[i] = p[i].getProduct_bind();
		}

		product_scroll      = new JScrollPane(product_all_bind);
		
		//South
		product_buy_bind = new JPanel();
		product_buy = new JButton("�ֹ��ϱ�");
		product_cencel = new JButton("�ݱ�");
		
		//�ֹ��ϱ�
		buy_dialog = new JDialog(this,"���� ����â");
		cal_money = new JLabel("",JLabel.CENTER);
		cal_time  = new JLabel("",JLabel.CENTER);
		money_cal = new JButton("�������� ����");
		time_cal = new JButton("�ð����� ����");
		
		buy_dialog.setSize(300, 150);
		buy_dialog.setLocation(1200, 300);
		dialog_button_bind = new JPanel();
		
		dialog_box = Box.createVerticalBox();
			
		init();
		start();
		
		setVisible(true);
	}
    
public void init() {
	
		container = getContentPane();
		container.setBackground(border_color);
		container.setLayout(new BorderLayout(10,10));
		
		all_bind.setLayout(new BorderLayout(10,10));
		all_bind.setBackground(border_color);

		container.add("Center",all_bind);
		container.add("West",box1);
		container.add("East",box2);
		container.add("North",box3);
		container.add("South",box4);
		
		box1.add(Box.createHorizontalStrut(0));
		box2.add(Box.createHorizontalStrut(0));
		box3.add(Box.createVerticalStrut(0));
		box4.add(Box.createVerticalStrut(5));
		dialog_box.add(Box.createVerticalStrut(35));
		
		//North
		all_bind.add("North",category);
		category.setPreferredSize(new Dimension(550, 78));
		category.setHorizontalScrollBarPolicy(category.HORIZONTAL_SCROLLBAR_ALWAYS);//�׻� ����
		category.setVerticalScrollBarPolicy(category.VERTICAL_SCROLLBAR_NEVER);//�׻� ����
		category_List.setLayout(new GridLayout(1, 5));
		//category_List.setLayout(new FlowLayout(FlowLayout.LEFT,1,0));
		
		for(int i=0; i<button.length;i++) {
			if(button[i].getText()=="") break;
			category_List.add(button[i]);
			button[i].setBackground(new Color(255,100,0));
			button[i].setForeground(Color.white);
			button[i].setPreferredSize(new Dimension(100,60));
			button[i].setFocusPainted(false);
			button[i].setEnabled(true);
		}
		
		button[0].setForeground(Color.black);
		button[0].setBackground(Color.WHITE);
		
		//Center
		price_box.add(Box.createHorizontalStrut(0));
		count_box.add(Box.createHorizontalStrut(90));
		
		all_bind.add("Center",product_scroll);

		//product_scroll.setPreferredSize(new Dimension(550, 400));
		product_scroll.setHorizontalScrollBarPolicy(product_scroll.HORIZONTAL_SCROLLBAR_NEVER);//�׻� ����
		product_scroll.setVerticalScrollBarPolicy(product_scroll.VERTICAL_SCROLLBAR_ALWAYS);//�׻� ����
		
		
		product_all_bind.setPreferredSize(new Dimension(550, 350));
		product_all_bind.setLayout(new FlowLayout(FlowLayout.LEFT,1,1));
		//product_all_bind.setLayout(new GridLayout(3, 2));
		product_all_bind.setBackground(Color.WHITE);
		
		for(int i=0;i<product_bind.length;i++) {
			if(product_bind[i]==null)break;
			product_all_bind.add(product_bind[i]);
		}	
		
		//South
		all_bind.add("South",product_buy_bind);
		product_buy_bind.setLayout(new FlowLayout(FlowLayout.RIGHT));
		product_buy_bind.setBackground(border_color);
		product_buy_bind.add(product_buy);
		product_buy.setPreferredSize(new Dimension(300, 40));
		product_buy.setBorder(new BevelBorder(BevelBorder.RAISED,Color.white,Color.DARK_GRAY));
		product_buy.setBackground(new Color(255,100,0));
		product_buy.setForeground(font_color);
		product_buy.setFocusPainted(false);
		
		product_buy_bind.add(product_cencel);
		product_cencel.setPreferredSize(new Dimension(100, 40));
		product_cencel.setBorder(new BevelBorder(BevelBorder.RAISED,Color.white,Color.DARK_GRAY));
		product_cencel.setBackground(Color.DARK_GRAY);
		product_cencel.setForeground(font_color);
		product_cencel.setFocusPainted(false);
		
		//�ֹ��ϱ�
		dialogContainer = buy_dialog.getContentPane();
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.add("North",dialog_box);
		dialogContainer.add("Center",dialog_button_bind);
		
		dialogContainer.setBackground(Color.DARK_GRAY);
		dialog_button_bind.setBackground(Color.DARK_GRAY);
		dialog_button_bind.setLayout(new FlowLayout(FlowLayout.CENTER,15,0));

		dialog_button_bind.add(cal_money);
		dialog_button_bind.add(cal_time);
		cal_money.setForeground(font_color);
		cal_time.setForeground(font_color);
		cal_money.setPreferredSize(new Dimension(250, 30));
		cal_time.setPreferredSize(new Dimension(250, 30));
		
		dialog_button_bind.add(money_cal);
		dialog_button_bind.add(time_cal);
		
		money_cal.setBackground(new Color(51,89,135));
		money_cal.setForeground(font_color);
		money_cal.setBorder(new BevelBorder(BevelBorder.RAISED,Color.white,Color.DARK_GRAY));
		money_cal.setFocusPainted(false);
		money_cal.setPreferredSize(new Dimension(110, 30));
		
		time_cal.setBackground(new Color(98,153,37));
		time_cal.setForeground(font_color);
		time_cal.setBorder(new BevelBorder(BevelBorder.RAISED,Color.white,Color.DARK_GRAY));
		time_cal.setFocusPainted(false);
		time_cal.setPreferredSize(new Dimension(110, 30));
	}
	public void start() {
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		// JDialog�� x��ư�� ���� ����� ����ϴ� �޼ҵ��̴�. �����ɸ� ó���Ѵ�.
		buy_dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		button[0].addActionListener(this);
		button[1].addActionListener(this);
		button[2].addActionListener(this);
		button[3].addActionListener(this);
		button[4].addActionListener(this);
		button[5].addActionListener(this);
		button[6].addActionListener(this);
		
		
		product_cencel.addActionListener(this);
		product_buy.addActionListener(this);
		
		money_cal.addActionListener(this);
		time_cal.addActionListener(this);
		
		user.remain_time.addPropertyChangeListener(this);
		
		button[0].addMouseListener(this);
		button[1].addMouseListener(this);
		button[2].addMouseListener(this);
		button[3].addMouseListener(this);
		button[4].addMouseListener(this);
		button[5].addMouseListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==button[0]) {
			product_all_bind.removeAll();
			for(int i =0;i<p.length;i++) p[i] = null;
			invalidate();
			//�����Ϸ�
			p[0] = new Product("Img/�ݶ�.jpg", "�ݶ�", "1500");
			p[1] = new Product("Img/�ݶ�(����).jpg", "�����ݶ�", "1500");
			p[2] = new Product("Img/ȯŸ.jpg", "ȯŸ", "1200");
			p[3] = new Product("Img/����.jpg", "���䷹��", "1200");
			p[4] = new Product("Img/������(��������).jpg", "������(��������)", "1000");
			p[5] = new Product("Img/������(ī���).jpg", "������(ī���)", "1000");
			
			refresh_product();
			
		}else if(e.getSource()==button[1]) {
			product_all_bind.removeAll();
			for(int i =0;i<p.length;i++) p[i] = null;
			invalidate();
			//�����Ϸ�
			p[0] = new Product("Img/��.jpg", "����", "1200");
			p[1] = new Product("Img/���.jpg", "��Ϲ���Ĩ", "1500");
			p[2] = new Product("Img/����.jpg", "������ (���޴��� ��)", "1500");
			p[3] = new Product("Img/����.jpg", "��¡���", "1500");
			p[4] = new Product("Img/���ĸ�.jpg", "���ĸ�", "1400");
			p[5] = new Product("Img/�ܲ�.jpg", "�ܲʹ��", "1400");

			
			refresh_product();
		}else if(e.getSource()==button[2]) {
			product_all_bind.removeAll();
			for(int i =0;i<p.length;i++) p[i] = null;
			invalidate();
			//�����Ϸ�
			p[0] = new Product("Img/�Ҵ�.jpg", "�Ҵߺ�����", "2500");
			p[1] = new Product("Img/����.jpg", "�������", "2500");
			p[2] = new Product("Img/¥��.jpg", "¥�İ�Ƽ", "2500");
			p[3] = new Product("Img/�ع���.jpg", "�ع�����", "2500");
			p[4] = new Product("Img/�ҹ�.jpg", "�ҹ�", "1500");
			p[5] = new Product("Img/���.jpg", "ġŲ����", "1500");

			
			refresh_product();
			
		}else if(e.getSource()==product_cencel) { //�ݱ� ��ư
			dispose();

		}else if(e.getSource()==product_buy) {
			price_sum = 0;
			price_min = 0;
			for(int i=0, j=0;i<p.length;i++) {
				if(p[i]==null) break;
				if(p[i].getProduct_plus()!=0) {
					price_sum += p[i].getProduct_plus()*p[i].getProduct_price();
					price_min = price_sum/20;
					System.out.println(p[i].getProduct_plus());
					System.out.println(p[i].getProduct_price());
					index[j] = i;
					j++;
				}
			}
            
			if(price_sum>0) {
				dialog_box.removeAll();
				dialog_box.add(Box.createVerticalStrut(10));
				cal_money.setText("����Ͻ� �ݾ���"+price_sum+"�� �Դϴ�.");
				cal_time.setText("����Ͻ� �ð���"+price_min+"���Դϴ�.");

				money_cal.setVisible(true);
				time_cal.setVisible(true);
			} else {
				dialog_box.removeAll();
				dialog_box.add(Box.createVerticalStrut(10));
				cal_money.setText("");
				cal_time.setText("�����Ͻ� ��ǰ�� ���� �������ּ���");
				money_cal.setVisible(false);
				time_cal.setVisible(false);
			}
			buy_dialog.setVisible(true);

			
		}else if(e.getSource()==money_cal) { 
			for(int i=0;i<index.length;i++) {
				if(p[index[i]].getProduct_plus()==0) break;
				OrderListDTO order_list = new OrderListDTO(Integer.valueOf(user.seat_num.getText()),
						p[index[i]].product_name.getText(), Integer.valueOf(p[index[i]].product_plus.getValue().toString())
						, (p[index[i]].getProduct_plus()*p[index[i]].getProduct_price()));
	            
				p[index[i]].product_plus.setValue(0);
				
				client.request_makeList(order_list);
				//Manage.getInstance().make_list(order_list);
				MemberDTO dto = new MemberDTO();
				dto.setId(user.dto.getId());
				client.request_totUpdate(dto,order_list.getProduct_price());
				//dao.update_totMoney(user.dto.getId(), price_sum);  //users DB-�� ��� �ݾ׿� ���� ���ž� ������Ʈ	
				index[i] = 0;
			}
            client.request_salesUpdate(2, price_sum);
			//sao.update(2, price_sum);
			buy_dialog.setVisible(false);
			
		}else if(e.getSource()==time_cal) {
			
			int result = 0;
			result = user.product_payment(price_min*20);
			if(result!=1) {
				for(int i=0;i<index.length;i++) {
					if(p[index[i]].getProduct_plus()==0) break;//���� �ε����� ������ break
					OrderListDTO order_list = new OrderListDTO(Integer.valueOf(user.seat_num.getText()),
							p[index[i]].product_name.getText(), Integer.valueOf(p[index[i]].product_plus.getValue().toString()), 
							(p[index[i]].getProduct_plus()*p[index[i]].getProduct_price())/20);
					
					p[index[i]].product_plus.setValue(0);
					
					client.request_makeList(order_list);
		            //Manage.getInstance().make_list(order_list);
		            
					MemberDTO dto = new MemberDTO();
					dto.setId(user.dto.getId());
					client.request_totUpdate(dto,order_list.getProduct_price()*20);
					//dao.update_totMoney(user.dto.getId(), price_sum);

					index[i] = 0;//����Ʈ�߰��� �ε��� ����
				}
	            client.request_salesUpdate(2, price_sum);
				//sao.update(2, price_sum); 
				buy_dialog.setVisible(false);
			}

		}
		
	}
	
	public void refresh_product() {

		for(int i=0;i<product_bind.length;i++) {
			product_bind[i] = null;
		}
		
		for(int i=0;i<p.length;i++) {//��ǰ ����
			if(p[i]==null) break;
			product_bind[i] = p[i].getProduct_bind();
			product_all_bind.add(product_bind[i]);
		}
		
		//�����Ϸ�
		product_all_bind.repaint();
		product_all_bind.revalidate();  //��ȿȭ
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getNewValue()==user.remain_time.getText()) {
			if(user.remain_time_value==0) {
				setVisible(false);
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()==button[0]) {
			button[0].setBackground(Color.white);
			button[1].setBackground(new Color(255,100,0));
			button[2].setBackground(new Color(255,100,0));
			
			button[0].setForeground(Color.black);
			button[1].setForeground(Color.white);
			button[2].setForeground(Color.white);
			
/*			button[0].repaint();
			button[0].revalidate();
			button[1].repaint();
			button[1].revalidate();
			button[2].repaint();
			button[2].revalidate();*/
			
		}else if(e.getSource()==button[1]) {
			button[0].setBackground(new Color(255,100,0));
			button[1].setBackground(Color.white);
			button[2].setBackground(new Color(255,100,0));
			
			button[0].setForeground(Color.white);
			button[1].setForeground(Color.black);
			button[2].setForeground(Color.white);
/*			button[0].repaint();
			button[0].revalidate();
			button[1].repaint();
			button[1].revalidate();
			button[2].repaint();
			button[2].revalidate();*/
		}else if(e.getSource()==button[2]) {
			button[0].setBackground(new Color(255,100,0));
			button[1].setBackground(new Color(255,100,0));
			button[2].setBackground(Color.white);
			
			button[0].setForeground(Color.white);
			button[1].setForeground(Color.white);
			button[2].setForeground(Color.black);
/*			button[0].repaint();
			button[0].revalidate();
			button[1].repaint();
			button[1].revalidate();
			button[2].repaint();
			button[2].revalidate();*/
		}
	}
}
