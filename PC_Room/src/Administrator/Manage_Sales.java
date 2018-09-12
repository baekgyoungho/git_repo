package Administrator;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import DB.SalesDAO;
import DB.SalesDTO;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Manage_Sales extends JDialog implements ActionListener{
	
	/**����*/
	JDialog dialog;
	Container dialogContainer;
	SalesDAO sdao = new SalesDAO();
	SalesDTO sdto = new SalesDTO();
	
	//�Ⱓ�� ���� �˻�
	Manage manage = Manage.getInstance();
	JButton button;
	UtilDateModel model1; //�˻� ���� ��¥
	JDatePanelImpl datePanel1;
	JDatePickerImpl datePicker1;
	UtilDateModel model2; //�˻� ���� ��¥
	JDatePanelImpl datePanel2;
	JDatePickerImpl datePicker2;
	JButton b_list; 
	JPanel p_showAll;
	JTable table;
	
	
	/**�ʱ�ȭ*/
	//1. ���� ���� Ȯ��
	public Manage_Sales() {
		dialog = new JDialog();
		dialogContainer = dialog.getContentPane();
		dialog.setTitle("���� ���� ��Ȳ");
		dialog.setBounds(300, 150, 300, 200);
		
		init1();
		
		dialog.setResizable(false);
		dialog.setVisible(true);
		menuDisable();
		manage.setEnabled(false);
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menuEnable();
				manage.setEnabled(true);
				super.windowClosing(e);
			}
		});
	}
	
	//2. �Ⱓ�� ���� Ȯ��
	public Manage_Sales(Manage manage) {
		this.manage = manage;
		dialog = new JDialog();
		dialogContainer = dialog.getContentPane();
		dialog.setTitle("���� �Ⱓ ����");
		dialog.setBounds(300, 150, 300, 200);
		
		init2();
		
		dialog.setResizable(false);
		dialog.setVisible(true);
		menuDisable();
		manage.setEnabled(false);
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menuEnable();
				manage.setEnabled(true);
				super.windowClosing(e);
			}
		});
	}

	
	
	/**ȭ�� ����*/
	//1. ���� ���� Ȯ�ο� ȭ��
	private void init1() {
	
		dialogContainer.setLayout(new BorderLayout());
		
		//salesDAO���� ���� �޾ƿ���
		sdto = sdao.select_day(); //���� ���� dto ��ȯ
		int computer = sdto.getComputer();
		int food = sdto.getFood();
		int tot_sales = sdto.getTot_sales();
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		/*
		//�гο� �� �߰�
		JPanel panel2 = new JPanel() { //���� �г� (�� �г��� dialogContainer Center�� ����
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponents(g);
				setOpaque(false);  //panel�� �����ϰ� ����
				g.drawLine(30, 80, 200, 80); //�۾� ��ġ ���� �� ���� ��ġ �����ؾ� ��
			}
		};*/

		//Ÿ��Ʋ
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String date = String.valueOf(month) +"��"+String.valueOf(day)+"��";

		JLabel title = new JLabel("[  " +date + "   ���� ��Ȳ  ]", JLabel.CENTER);
		title.setPreferredSize(new Dimension(300, 50));


		//����
		JPanel p_Left = new JPanel(new GridLayout(3, 1));
		JPanel p_Right = new JPanel(new GridLayout(3, 1));

		JLabel l_com = new JLabel("PC ����: ", JLabel.RIGHT);
		JLabel l_product = new JLabel("��ǰ ����: ", JLabel.RIGHT);
		JLabel l_tot = new JLabel("�� ����: ", JLabel.RIGHT);

		JLabel com = new JLabel("  "+String.valueOf(computer)+"��");
		JLabel product = new JLabel("  "+String.valueOf(food)+"��");
		JLabel tot = new JLabel("  "+String.valueOf(tot_sales)+"��");

		p_Left.add(l_com);
		p_Left.add(l_product);
		p_Left.add(l_tot);

		p_Right.add(com);
		p_Right.add(product);
		p_Right.add(tot);

		panel.add("West", p_Left);
		panel.add("Center", p_Right);		

		JLabel empty = new JLabel(" ");
		dialogContainer.add("North",title);
		dialogContainer.add("Center",panel);
		//dialogContainer.add("Center",panel2);
		dialogContainer.add("South", empty);
	}
	
	
	//2. �Ⱓ�� ���� ��Ȳ ȭ�� - ��¥ ���� ���
	private void init2() {
		dialogContainer.setLayout(new GridLayout(1, 1));
		
		LineBorder lineborder = new LineBorder(new Color(0X5D5D5D),6);
		EtchedBorder etchedBorder = new EtchedBorder();
		CompoundBorder compound = new CompoundBorder(lineborder,etchedBorder);
		
		//���� �г�
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBorder(compound);
		panel.setBackground(new Color(0X5D5D5D));
		
		//�μ� �г�
		JPanel p_west = new JPanel(new GridLayout(2, 1));
		JPanel p_center = new JPanel(new GridLayout(2, 1));
		p_west.setBackground(new Color(0X5D5D5D));
		p_center.setBackground(new Color(0X5D5D5D));
		
		//����
		JLabel l_title = new JLabel("[ Ȯ���Ͻ� �Ⱓ�� �������ּ��� ]",JLabel.CENTER);
		l_title.setBackground(Color.DARK_GRAY);
		l_title.setForeground(Color.LIGHT_GRAY);
		l_title.setOpaque(true);
		l_title.setPreferredSize(new Dimension(300, 50));		
		
		//��ư
		button = new JButton("�Ⱓ ���� �Ϸ�");
		button.setPreferredSize(new Dimension(300, 40));
		button.setBackground(Color.DARK_GRAY);
		button.setForeground(Color.LIGHT_GRAY);
		button.setBorder(new BevelBorder(BevelBorder.RAISED));
		button.addActionListener(this);  //��ư ������ �� ������ ��¥ �о���� �ش� ������ ���
		button.setFocusPainted(false);
		
		//������, ������ ��
		JLabel l_start = new JLabel("   ������: ", JLabel.RIGHT);
		JLabel l_end = new JLabel("   ������: ", JLabel.RIGHT);
		l_start.setForeground(Color.LIGHT_GRAY);
		l_end.setForeground(Color.LIGHT_GRAY);
		//l_start.setPreferredSize(new Dimension(80, 30));
		//l_end.setPreferredSize(new Dimension(80, 30));
		
		p_west.add(l_start);
		p_west.add(l_end);
		
		
		//jDatePicker
		//�˻� ������
		model1 = new UtilDateModel();
		datePanel1 = new JDatePanelImpl(model1);
		datePicker1 = new JDatePickerImpl(datePanel1);
		//datePicker1.setBounds(100,170,200,30);
		//String start=(String)datePicker1.getModel().getValue();
		//System.out.println(start);
		
		//�˻� ������
		model2 = new UtilDateModel();
		datePanel2 = new JDatePanelImpl(model2);
		datePicker2 = new JDatePickerImpl(datePanel2);
		//String end=(String)datePicker2.getModel().getValue();
		//System.out.println(end);
		
		p_center.add(datePicker1);
		p_center.add(datePicker2);
		panel.add("North",l_title);
		panel.add("West",p_west);
		panel.add("Center",p_center);
		panel.add("South",button);
		dialogContainer.add(panel);
	}

	
	//3. �Ⱓ�� ���� ��Ȳ ȭ�� - �˻� ��� ��� ��� (���̺�)
	public void showSales(String start, String end) {
		System.out.println("start: "+start);
		System.out.println("end: "+end);
		
		List<SalesDTO> list = new ArrayList<SalesDTO>();
		list = sdao.select_day(start, end); //sql ����
		
		//����� ���� �����Ͱ� �������� ���� ���
		String no_data = "���������� �������� �ʽ��ϴ�.";
		System.out.println(list.get(0).getDays());
		if (list.get(0).getDays().equals(no_data)) {
			JOptionPane.showMessageDialog(manage, no_data);
			menuEnable();
			manage.setEnabled(true);
			return;
		}
		
		//����� ���� �����Ͱ� ���� ���
		manage.p_seatLayout.setVisible(false);
		
		b_list = new JButton("Ȯ  ��");
		p_showAll = new JPanel(new BorderLayout());

		//���� �г� ����
		p_showAll = new JPanel(new BorderLayout(5,5)); //�¼� ���̾ƿ� ��� ������ �г�
		p_showAll.setBorder(manage.compoundBorder2);
		p_showAll.setBackground(new Color(0X5D5D5D));

		//���� ����
		JLabel title = new JLabel("<�˻� �Ⱓ ���� ���>", JLabel.CENTER);
		title.setFont(new Font("Malgun Gothic",Font.BOLD,14));
		title.setPreferredSize(new Dimension(500, 50));
		//title.setBorder(compoundBorder2);
		title.setForeground(Color.LIGHT_GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setOpaque(true);
		
		//��ư ����
		b_list.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_list.setBackground(Color.DARK_GRAY);
		b_list.setForeground(Color.LIGHT_GRAY);
		b_list.setPreferredSize(new Dimension(30, 30));
		b_list.addActionListener(this); //��ư ������ ��� ����ǰ� �ٽ� seat ���̵��� �̺�Ʈ ����
		b_list.setFocusPainted(false);
		
		JPanel p_list = new JPanel(new GridLayout(0,1)); //���̺��� �߰��� �г�
		p_list.setBackground(new Color(0X5D5D5D));
		
		//JTable ����
		String[] str = {"��¥","PC �̿� ����","��ǰ �Ǹ� ����","�� ����"};
		DefaultTableModel model = new DefaultTableModel(str, 0);
		System.out.println(list.size());
		table = new JTable(model);
		
		int tot_com=0;
		int tot_food=0;
		int tot_tot=0;
		for (int i=0;i<list.size();i++) {
			String date = list.get(i).getDays();
			date = date.substring(0, 8); //��¥ ������ �߶󳻱�
			String computer = "  "+list.get(i).getComputer();
			String food = "  " +list.get(i).getFood();
			String tot_sales = "  " +list.get(i).getTot_sales();
			String[] data = {date, computer, food, tot_sales};
			model.addRow(data);
			
			tot_com += Integer.parseInt(computer.trim());
			tot_food += Integer.parseInt(food.trim());
			tot_tot += Integer.parseInt(tot_sales.trim());
		}
		String[] empty = {" "," "," "," "};
		String[] total = {"�� ����", "  "+String.valueOf(tot_com), "  "+String.valueOf(tot_food), "  "+String.valueOf(tot_tot)};
		model.addRow(empty);
		model.addRow(total);

		//JTable ����
		//table.getColumnModel().getColumn(0).setMaxWidth(40); //ù �� �ʺ� ����
		table.setRowHeight(30); //�� ���� ����
		table.getTableHeader().setFont(new Font("Malgun Gothic",Font.BOLD,13)); //������ ��Ʈ ����
		table.getTableHeader().setBackground(Color.DARK_GRAY);
		table.getTableHeader().setForeground(Color.LIGHT_GRAY);
		table.getTableHeader().setPreferredSize(new Dimension(500, 30)); //������ ���� ����
		table.getTableHeader().setReorderingAllowed(false); //�巡�׾ص������ �÷� �̵� ����
		//�巡�׾ص������ ������ �� �ʺ� ���� �Ұ� ����
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.setBackground(Color.LIGHT_GRAY); //���ڵ� ������ ����
		table.setEnabled(false); //���� �Ұ� & Ŭ�� �Ұ�

		//ù �� ��� ����
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setCellRenderer(dtcr);
		
		//�ٸ� �� ���� ����
		/*DefaultTableCellRenderer dtcr2 = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
		TableColumnModel tcm2 = table.getColumnModel();
		tcm2.getColumn(1).setCellRenderer(dtcr2);
		tcm2.getColumn(2).setCellRenderer(dtcr2);
		tcm2.getColumn(3).setCellRenderer(dtcr2);*/

		//��ũ�� ����
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setWheelScrollingEnabled(true); //���콺�ٷ� ��ũ�� �̵� ����
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setOpaque(true);
		p_list.add(scrollPane);
		//p_list.setBorder(compoundBorder2);
		p_list.setBackground(Color.DARK_GRAY);


		//�гο� ���̺� & ��ư ����
		p_showAll.add("North",title);
		p_showAll.add("Center",p_list);
		p_showAll.add("South", b_list);

		//�г� �߰�
		manage.container.add("Center",p_showAll);
		manage.container.revalidate();
		manage.container.repaint();
	}
	
	//�޴��� ����
	public void menuDisable() {
		manage.menuUser.setEnabled(false);  //���̾�α� ��µȵ��� �޴��� ���� �Ұ�
		manage.menuSales.setEnabled(false);
		manage.menuOrder.setEnabled(false);
	}
	
	public void menuEnable() {
		manage.menuUser.setEnabled(true);  //���̾�α� ����Ǹ� �ٽ� �޴��� ���� ����
		manage.menuSales.setEnabled(true);
		manage.menuOrder.setEnabled(true);
	}
	
	
	/**�̺�Ʈ ����*/
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//�Ⱓ ���� �� Ȯ�� ��ư ������ ��
		if (e.getSource()==button) {
			SimpleDateFormat date_format = new SimpleDateFormat("yy/MM/dd"); //sql�� ����
			SimpleDateFormat date_format2 = new SimpleDateFormat("yyyyMMdd"); //��¥ �񱳿� ����
			Date start=(Date)datePicker1.getModel().getValue();
			Date end=(Date)datePicker2.getModel().getValue();
			
			//��¥�� �������� �ʾ��� ���
			if (start==null || end==null) {
				JOptionPane.showInternalMessageDialog(dialogContainer, "��¥�� �������ּ���");
				return;
			}
			
			//�˻� �������� �˻� ������ ���� ���� ��� �ٽ� �����ϵ��� ����
			int start_num = Integer.parseInt(date_format2.format(start));
			int end_num = Integer.parseInt(date_format2.format(end));
			System.out.println("start_num"+start_num);
			System.out.println("end_num"+end_num);
			
			if (start_num>end_num) {
				JOptionPane.showInternalMessageDialog(dialogContainer, "�˻� �������� �˻� �����Ϻ���\n ���� �� �����ϴ�");
			}
			
			//�˻� ������ & �������� �ùٸ� ���
			else {
			
			//DB �˻��ϱ����� ��¥�� ���� ���缭 string������ ��ȯ (yy/MM/dd)
			String start_str = date_format.format(start).toString();
			String end_str = date_format.format(end).toString();
			
			dialog.dispose();
			manage.setEnabled(true);
			showSales(start_str, end_str); //���̺� ��� �Լ�
			}
		}
		
		//������ �Ⱓ ���� ���̺� ��� �� Ȯ�� ��ư ������ �� (������ �ڸ� ���̾ƿ����� ����)
		if (e.getSource() == b_list) {
			menuEnable();
			
			manage.container.remove(p_showAll);
			manage.container.invalidate();
			manage.p_seatLayout.setVisible(true);
			
			manage.container.revalidate();
			manage.container.repaint();
		}
	}
}
