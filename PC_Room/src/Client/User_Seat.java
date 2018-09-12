package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import DB.MemberDTO;
import DB.User_infoDTO;

//����ڰ� �ڸ� �̵� ��ư �� �˾����� ��� Ŭ����
public class User_Seat extends JFrame implements ActionListener, PropertyChangeListener {
	
	/**����*/
	//JDialog dialog = new JDialog();
	//Container dialogContainer = dialog.getContentPane();
	Container dialogContainer = getContentPane();
	
	// - �ڸ� ���̾ƿ� ����
	JPanel p_seatLayout = new JPanel(new GridLayout(6, 5, 3,3));
	JToggleButton[] seat = new JToggleButton[30]; //�� �ڸ��� ������ �� �ֵ��� ��۹�ư ���
	JLabel[] seat_no=new JLabel[30];
	ButtonGroup buttonGroup = new ButtonGroup();
	
	// - ����, ��� ��ư, ��� ����
	JButton b_confirm = new JButton(" ���� �Ϸ� ");
	JButton b_cancel = new JButton(" ��      �� ");
	JLabel label = new JLabel("");
	
	// - �׵θ�
	LineBorder lineBorder2 = new LineBorder(new Color(0X5D5D5D), 5);
	EtchedBorder etchedBorder = new EtchedBorder();
	CompoundBorder compoundBorder2 = new CompoundBorder(etchedBorder, lineBorder2);
	
	String updateNum = ""; //������ �ڸ�
	List<Integer> seatList; //��� ���� �ڸ� ��� (manage Ŭ�������� �޾ƿ� �� ����)
	User user;
	int seatNum=0;//���� ��� ���� �ڸ�
	
	Client client = null;
	Thread thread = new Thread();
	
	/**�ʱ�ȭ*/
	public User_Seat(Client client, User user, int seatNum) {
		this.client = client;
		this.user =user;
		this.seatNum = seatNum;
		//setUndecorated(true);  //���� ǥ���� ��� x
		setBounds(1000,0,500,400);
		setResizable(false);
		setTitle("�ڸ� �̵�");
		
		client.request_seatList();//��� ���� �ڸ� ��� �޾ƿ���
		
		while(true) {//�¼���ȣ �������µ� �ɸ��½ð� ó��
			if(client.getSeatList()==null) {
				System.out.println("List�� Null�Դϴ�");
				try {
					thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else break;
		}
		seatList = client.getSeatList();
		client.setSeatList(null);
		//seatList = Manage.getInstance().seat_inUse(); //��� ���� �ڸ� ��� �޾ƿ���
		init();
		start();
		
		setVisible(true);
		//setVisible(true); //������ true�� �ƴϸ� ���̾�α�â�� ������ �ʴ´�
	}
	
	
	//���̾�α�â ȭ�� ����
	private void init() {
		//���̾�α�â ȭ�� ����
		
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.setBackground(new Color(0X5D5D5D));
		
		//- North
		JLabel direction = new JLabel("  �̵��� ���Ͻô� �ڸ��� �����ϼ��� (�̹� ��� ���� �ڸ��� �����Ͻ� �� �����ϴ�)");
		direction.setForeground(Color.YELLOW);
		direction.setPreferredSize(new Dimension(500, 30));
		dialogContainer.add("North",direction);
		
		// - Center
		for (int i=0;i<seat.length;i++) {
			seat[i] = new JToggleButton(String.valueOf(i+1));
			//seat_no[i] = new JLabel(String.valueOf(i+1));
			
			seat[i].setBackground(Color.DARK_GRAY);
			seat[i].setForeground(Color.GRAY);
			seat[i].setBorder(etchedBorder);
			seat[i].setFocusPainted(false); //��ư Ŭ�� �� ��ư text�� �׵θ� ���ֵ��� ����
			//seat[i].setOpaque(true);
			
			//seat_no[i].setForeground(Color.GRAY);
			//seat[i].add(seat_no[i]);
			buttonGroup.add(seat[i]);
			p_seatLayout.add(seat[i]);
		}
		
		//������� �ڸ�: ��ư �� ����, Ŭ�� �Ұ� ����
		for (int i=0;i<seatList.size();i++) {
			int num = seatList.get(i);
			System.err.println(num);
			seat[num].setBackground(new Color(0X5D5D5D));
			seat[num].setEnabled(false); //Ŭ�� �Ұ� ����
		}
		
		p_seatLayout.setBackground(new Color(0X5D5D5D));
		p_seatLayout.setBorder(compoundBorder2);
		
		dialogContainer.add("Center",p_seatLayout);
		
		
		//- South
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		b_confirm.setBackground(Color.GRAY);
		b_confirm.setForeground(Color.WHITE);
		b_confirm.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_cancel.setBackground(Color.GRAY);
		b_cancel.setForeground(Color.WHITE);
		b_cancel.setBorder(new BevelBorder(BevelBorder.RAISED));
		panel.add(label);
		panel.add(b_confirm);
		panel.add(b_cancel);
		panel.setBackground(new Color(0X5D5D5D));
		dialogContainer.add("South",panel);
	}

	private void start() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			//â ���� ���
		
		b_confirm.addActionListener(this);
		b_cancel.addActionListener(this);
		
		for(int i =0;i<seat.length;i++) {
			seat[i].addActionListener(this);
			//Manage.getInstance().seat_id[i].addPropertyChangeListener(this);
		}
		client.change1.addPropertyChangeListener(this);
		client.change2.addPropertyChangeListener(this);
	}

	//����� �ڸ� getter
	public String getUpdateNum() {
		return updateNum;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		//������ �ڸ� ��ȣ ������
		for(int i =0;i<seat.length;i++) {
			if (e.getSource()==seat[i]) {
				updateNum = seat[i].getText();
				System.out.println("������ �ڸ�: "+updateNum);
			}
		}
		
		
		//Ȯ�� ��ư
		if (e.getSource()==b_confirm) {

			if (updateNum==null || updateNum.equals("")) {
				//�ڸ� �������� ���� ��� ���� ���
				label.setText("������ �ڸ��� �������ּ���!");
				label.setForeground(Color.ORANGE);
				
			} else {
				//�ڸ��� �����ߴٸ� ���̾�α�â ����Ǹ鼭 ȸ��â/������â�� ���ο� �ڸ���ȣ ������Ʈ
                client.request_deleteUserInfo(seatNum);
				//Manage.getInstance().delete_userInfo(seatNum); //������â���� ���� �ڸ� �����
				seatNum = Integer.parseInt(updateNum); //���̾�α�â ���� ����: ���� �ڸ��� ������ �ڸ���ȣ ������Ʈ
				user.seatNum = Integer.parseInt(updateNum); //Userâ ���� ����: ���� �ڸ��� ������ �ڸ���ȣ ������Ʈ
				//������â���� �� �ڸ� Ȱ��ȭ
				User_infoDTO user_info = new User_infoDTO(user.dto, user.seatNum, 
						user.remain_time.getText(),user.remain_time_value*20,user.use_time_value*20); //������â�� ���� ���� ������Ʈ
				client.request_update_UserInfo(1300, user_info);
				//Manage.getInstance().update_userInfo(user.dto, seatNum, user.remain_time.getText()); 
				user.seat_num.setText(String.valueOf(seatNum)); //�����ȭ�鿡 �ڸ���ȣ ������Ʈ
				
				client.request_seatUpdate();//�¼� �̿�� ������Ʈ
				
				dispose();
			}

		}
		if (e.getSource()==b_cancel) {
			//seat[Integer.parseInt(updateNum)-1].setSelected(false);
			dispose();
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
		if(e.getNewValue()==client.change1.getText()) {
			//if(e.getNewValue()==Manage.getInstance().seat_id[i].getText()) {
									
			dialogContainer.remove(p_seatLayout);
			dialogContainer.invalidate();
			
			// - Center
			for (int j=0;j<seat.length;j++) {
				seat[j].setBackground(Color.DARK_GRAY);
				seat[j].setEnabled(true);
			}
			client.request_seatList();//��� ���� �ڸ� ��� �޾ƿ���
		}
		else if(e.getNewValue()==client.change2.getText()) {//�������۵����̶� else�� ���� ������ 2���� ���� ��� �����ϴ°�� �߻��� 
			                                                // 1�� ��� �۵��� 2������� �۵��ؾ��ϴµ� ���۵� ��
			                                                // Property ������ : �ش� �̺�Ʈ�� �߻���Ų ������Ʈ�� 
			                                                // properyListener ������ ���������� �۵� ���� 

			while(true) {//�¼���ȣ �������µ� �ɸ��½ð� ó��
				if(client.getSeatList()==null) {
					System.out.println("List�� Null�Դϴ�");
					try {
						thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else break;
			}
			
			seatList = client.getSeatList();
			client.setSeatList(null);
			//seatList = Manage.getInstance().seat_inUse(); //��� ���� �ڸ� ��� �޾ƿ���
			
			//������� �ڸ�: ��ư �� ����, Ŭ�� �Ұ� ����
			for (int k=0;k<seatList.size();k++) {
				int num = seatList.get(k);
				System.err.println(num);
				if(updateNum.equals(String.valueOf(num+1))) {//�¼��� �������� ��ư ������ ������ �ٸ� ����ڰ� �α���������� ó��
					updateNum = "";
				}
				seat[num].setBackground(new Color(0X5D5D5D));
				seat[num].setEnabled(false); //Ŭ�� �Ұ� ����
			}
			dialogContainer.add("Center",p_seatLayout);
			dialogContainer.repaint();
			dialogContainer.revalidate();
			
		}
	}
}
