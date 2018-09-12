package Administrator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class Manage_OrderList{
	
	JDialog dialog = new JDialog();
	Container dialogContainer = dialog.getContentPane();
    Box box1,box2,box3,box4;
    
	Color font_color = new Color(235,235,235);
	Color border_color = new Color(50,50,50);
	Color bg_color = new Color(90,90,95);
	
	JPanel all_bind   = new JPanel();
	

	
	JLabel info = new JLabel("주문내역 확인",JLabel.CENTER);
	
	JPanel order_list_title = new JPanel();
	
	JLabel seat_no       = new JLabel("seat_no",JLabel.CENTER);
	JLabel product_name  = new JLabel("상품명",JLabel.CENTER);
	JLabel product_count = new JLabel("수량",JLabel.CENTER);
	JLabel product_price = new JLabel("   가격",JLabel.LEFT);
	
	
	JPanel order_list_panel = new JPanel();
	JPanel info_panel = new JPanel();
	JList<JPanel> order_list;//Manager에 추가해야함
	//Manage_OrderList manage_OrderList = null;

	JScrollPane product_scroll;

    public Manage_OrderList(JList<JPanel> order_list) {
        this.order_list = order_list;
        product_scroll = new JScrollPane(order_list);
		//setUndecorated(true);  //제목 표시줄 출력 x
		dialog.setBounds(443,500,360,200);
		dialog.setResizable(false);
		dialog.setTitle("주문내역 확인");
		
		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createVerticalBox();
		box4 = Box.createVerticalBox();
		
		
		init();
		start();
		
		dialog.setVisible(true);
		

	}


	public void init() {
		
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.setBackground(new Color(0X5D5D5D));

		dialogContainer.add("Center",all_bind);
		dialogContainer.add("West",box1);
		dialogContainer.add("East",box2);
		dialogContainer.add("North",box3);
		dialogContainer.add("South",box4);
		
		all_bind.setLayout(new BorderLayout());
		all_bind.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		all_bind.setBackground(bg_color);
		all_bind.add("North",info_panel);
		info_panel.setBackground(Color.DARK_GRAY);
		info_panel.add(info);
		info.setForeground(font_color);
		
		all_bind.add("Center",order_list_panel);
	
		product_scroll.setHorizontalScrollBarPolicy(product_scroll.HORIZONTAL_SCROLLBAR_NEVER);//항상 없음
		product_scroll.setVerticalScrollBarPolicy(product_scroll.VERTICAL_SCROLLBAR_ALWAYS);//항상 존재
		
		
		order_list_panel.setLayout(new BorderLayout());
		order_list_panel.add("North",order_list_title);
		order_list_panel.add("Center",product_scroll);
		order_list_panel.setBackground(Color.white);
		
		order_list_title.setLayout(new GridLayout(1, 4));
		order_list_title.add(seat_no);
		seat_no.setForeground(Color.black);
		order_list_title.add(product_name);
		product_name.setForeground(Color.black);
		order_list_title.add(product_count);
		product_count.setForeground(Color.black);
		order_list_title.add(product_price);
		product_price.setForeground(Color.black);
		
		order_list_title.setBackground(Color.WHITE);
		order_list_title.setBorder(new LineBorder(Color.black));
		order_list_title.setPreferredSize(new Dimension(200,20));
		order_list.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

		order_list.setBackground(Color.lightGray);
		order_list.setOpaque(false);
		order_list.setPreferredSize(new Dimension(200,order_list.getComponentCount()*20));
		
		box1.add(Box.createHorizontalStrut(10));
		box2.add(Box.createHorizontalStrut(10));
		box3.add(Box.createVerticalStrut(10));
		box4.add(Box.createVerticalStrut(10));
		
		
		
	}
	
	public void start() {
	}
	
	public void repainted() {
		order_list.setPreferredSize(new Dimension(200,order_list.getComponentCount()*20));
		order_list.repaint();
		order_list.revalidate();
	}
	
}
