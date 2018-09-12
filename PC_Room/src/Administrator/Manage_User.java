package Administrator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import DB.MemberDAO;
import DB.MemberDTO;

//ȸ�� �˻� / ��ü ȸ�� �˻� ��� ��� Ŭ����
public class Manage_User extends JDialog implements ActionListener, MouseListener {
	
	/**����*/
	JDialog dialog;
	Container dialogContainer;
	MemberDTO dto = new MemberDTO();
	MemberDAO dao = new MemberDAO();
	Font labelFont = new Font("SansSherif", 1, 15); //1�� ȸ������ ��ȸ �� �� �׸�� ������ ��Ʈ
	
	// - ��ü ȸ�� ���� ��¿� ���̺�
	JTable table;
	
	Color color;
	
	// - ��ü ����� ��ȸ
	Manage manage = Manage.getInstance();
	JButton b_list; 
	JPanel p_showAll;
	
	
	
	/**�ʱ�ȭ1 - ��ü ����� ��ȸ��*/
	public Manage_User(Manage manage) {
		this.manage = manage;
		showAll(manage);
	}
	
	
	/**�ʱ�ȭ2 - ȸ�� ��ȸ*/
	public Manage_User(Manage manage, MemberDTO dto) {
		dialog = new JDialog(manage,"ȸ�� ����");
		//color = new Color(0X353535);
		
		dialogContainer =  dialog.getContentPane();
		this.dto = dto;
		String check = dto.getId();
		
		dialog.setResizable(false);
		
		if (check.equals("not found")) { //��ġ�ϴ� ���̵� ���� ��� init1() ����
			dialog.setBounds(300,150,400,150);
			init1();
		} else { //��ġ�ϴ� ���̵� ���� ��� init2() ����
			dialog.setBounds(300,100,300,300);
			init2();
		}
			
		dialog.setVisible(true);
		menuDisable(); //���̾�α�â ���� ���� �޴��� ��Ȱ��ȭ
		
		//x��ư ������ �ٽ� �޴��� Ȱ��ȭ
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menuEnable();//���̾�α�â ���� �� �޴��� Ȱ��ȭ
				super.windowClosing(e);
			}
		});
	}
	
	
	//��ü ȸ�� ��ȸ���� ���̺� ���ڵ� ���� �� ����
	public Manage_User(String id) {
		dialog = new JDialog(manage,"ȸ�� ����");
		
		dialogContainer =  dialog.getContentPane();
		dialog.setResizable(false);
		
		dto = dao.search_user(id);
		System.out.println(id);
		System.out.println(dto.getId());
		dialog.setBounds(300,100,300,300);
		init2();
		
		dialog.setVisible(true);
		manage.setEnabled(false);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				manage.setEnabled(true);;//���̾�α�â ���� �� �޴��� Ȱ��ȭ
				super.windowClosing(e);
			}
		});
	}
	
	
	//ȸ�� 1�� ��ȸ - ���� ���̵��� ��� ȭ�� ����
	private void init1() {
		dialogContainer.setLayout(new BorderLayout());
		JLabel label = new JLabel("��ϵ��� ���� ���̵��Դϴ�",  JLabel.CENTER);
		//label.setFont(labelFont);
		
		dialogContainer.add("Center",label);
	}
	
	
	//ȸ�� 1�� ��ȸ - ��ϵ� ���̵��� ��� ȭ�� ����
	private void init2() {
		color = new Color(213, 213, 213);
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.setBackground(color);
		
		LineBorder lineborder = new LineBorder(color,7,true);
		LineBorder lineborder3 = new LineBorder(color,14,true);
		
		
		JLabel title = new JLabel("< ȸ �� �� �� >", JLabel.CENTER);
		title.setOpaque(true);
		title.setPreferredSize(new Dimension(300,40));
		title.setBackground(color.DARK_GRAY);
		title.setForeground(color.LIGHT_GRAY);
		title.setFont(labelFont);
		JPanel p_Left = new JPanel(new GridLayout(8, 1));  //�׸�� ���� �г�
		p_Left.setBorder(lineborder3);
		p_Left.setBackground(color);
		
		JPanel p_Right = new JPanel(new GridLayout(8, 1)); //��³��� ���� �г�
		p_Right.setBorder(lineborder3);
		p_Right.setBackground(color);
		//JPanel p_button = new JPanel(); //Ȯ�� ��ư ���� �г�
		
		
		//�׸�� 
		JLabel l_id = new JLabel("���̵�: ", JLabel.RIGHT);
		JLabel l_pw = new JLabel("��й�ȣ: ", JLabel.RIGHT);
		JLabel l_name = new JLabel("��   ��: ", JLabel.RIGHT);
		JLabel l_phone = new JLabel("����ó: ", JLabel.RIGHT);
		JLabel l_joinDate = new JLabel("������: ", JLabel.RIGHT);
		JLabel l_money = new JLabel("��  ��: ", JLabel.RIGHT);
		JLabel l_totTime = new JLabel("�� �̿�ð�: ", JLabel.RIGHT);
		JLabel l_totMoney = new JLabel("�� �̿�ݾ�: ", JLabel.RIGHT);
		
		p_Left.add(l_id);
		p_Left.add(l_pw);
		p_Left.add(l_name);
		p_Left.add(l_joinDate);
		p_Left.add(l_phone);
		p_Left.add(l_money);
		p_Left.add(l_totTime);
		p_Left.add(l_totMoney);
		
		
		//��� ����
		JLabel output_id = new JLabel(dto.getId(), JLabel.CENTER );
		JLabel output_pw = new JLabel(dto.getPw(), JLabel.CENTER);
		JLabel output_name = new JLabel(dto.getName(), JLabel.CENTER);
		JLabel output_joinDate = new JLabel(dto.getJoinDate(), JLabel.CENTER);
		JLabel output_phone = new JLabel(dto.getPhone(), JLabel.CENTER);
		JLabel output_money = new JLabel(String.valueOf(dto.getMoney())+"��", JLabel.CENTER);
		String time = time_make(dto.getTot_time()/20);
		JLabel output_tottime = new JLabel(time, JLabel.CENTER);
		//JLabel output_tottime = new JLabel(String.valueOf(dto.getTot_time()), JLabel.CENTER);
		JLabel output_totmoney = new JLabel(String.valueOf(dto.getTot_money())+"��", JLabel.CENTER);
		
		p_Right.add(output_id);
		p_Right.add(output_pw);
		p_Right.add(output_name);
		p_Right.add(output_joinDate);
		p_Right.add(output_phone);
		p_Right.add(output_money);
		p_Right.add(output_tottime);
		p_Right.add(output_totmoney);
		

		dialogContainer.add("North",title);
		dialogContainer.add("West",p_Left);
		dialogContainer.add("Center",p_Right);
	}
	
	
	//��üȸ�� ��ȸ
	public void showAll(Manage manage) {
		//p_seatLayout.setVisible(false);
		
		//container.remove(p_seatLayout);
		//container.invalidate();
		manage.menuUser.setEnabled(false);
		manage.menuSales.setEnabled(false);
		manage.menuOrder.setEnabled(false);
		manage.p_seatLayout.setVisible(false);
		
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		list = dao.selectAll();
		
		b_list = new JButton("Ȯ  ��");
		p_showAll = new JPanel(new BorderLayout());
		
		//���� �г� ����
		p_showAll = new JPanel(new BorderLayout(5,5)); //�¼� ���̾ƿ� ��� ������ �г�
		p_showAll.setBorder(manage.compoundBorder2);
		p_showAll.setBackground(new Color(0X5D5D5D));
		
		//���� ����
		JLabel title = new JLabel("<��ü ȸ�� ���>", JLabel.CENTER);
		title.setFont(new Font("Malgun Gothic",Font.BOLD,14));
		title.setPreferredSize(new Dimension(500, 50));
		//title.setBorder(compoundBorder2);
		title.setForeground(Color.LIGHT_GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setOpaque(true);
		
		//��ư ����
		//b_list = new JButton("Ȯ��");
		b_list.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_list.setBackground(Color.DARK_GRAY);
		b_list.setForeground(Color.LIGHT_GRAY);
		b_list.setPreferredSize(new Dimension(30, 30));
		b_list.addActionListener(this); //��ư ������ ��� ����ǰ� �ٽ� seat ���̵��� �̺�Ʈ ����
		b_list.setFocusPainted(false);
		
	
		JPanel p_list = new JPanel(new GridLayout(0,1)); //���̺��� �߰��� �г�
		p_list.setBackground(new Color(0X5D5D5D));
		
		
		//JTable ����
		String[] str = {"No.","���̵�","��  ��","����ó"};
		DefaultTableModel model = new DefaultTableModel(str, 0) {
			@Override //����Ŭ���ؼ� ���� ���� ����
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		System.out.println(list.size());
		table = new JTable(model);
		
		
		for (int i=0;i<list.size();i++) {
			String no = String.valueOf(i+1);
			String id = "  "+list.get(i).getId();
			String name = "  " +list.get(i).getName();
			String phone = "  " +list.get(i).getPhone();
			String[] data = {no,id,name,phone};
			model.addRow(data);
		}
		
		//JTable ����
		table.getColumnModel().getColumn(0).setMaxWidth(40); //ù �� �ʺ� ����
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

		//ù �� ��� ����
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setCellRenderer(dtcr);

		//���̺� ���ڵ� Ŭ�� �� �ش� ȸ�� ���� �˾� (=ȸ�� ���� ��� �Լ� ���)
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //���� ���� ����
		table.addMouseListener(this);
		
		
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
	
	//���� �ð����� ��ȯ �Լ�
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
	
	
	
	//�̺�Ʈ ó�� - actionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//��ü ȸ�� ��� ��µ� ���¿��� Ȯ�� ��ư ������ ������ �ڸ� ���̾ƿ����� ����
		if (e.getSource() == b_list) {
			manage.menuUser.setEnabled(true);  //�ٽ� �޴����� ����
			manage.menuSales.setEnabled(true);
			manage.menuOrder.setEnabled(true);
			
			manage.container.remove(p_showAll);
			manage.container.invalidate();
			manage.p_seatLayout.setVisible(true);
			
			manage.container.revalidate();
			manage.container.repaint();
		}
	}

	
	//�̺�Ʈ ó�� - mouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow(); //������ �� ��ȣ ��ȯ
		//int column = table.getSelectedColumn(); //������ �� ��ȣ ��ȯ
		String id = (String)table.getValueAt(row, 1);
		id = id.trim(); //���� ����
		new Manage_User(id);
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
		
	}
}
