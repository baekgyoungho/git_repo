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
	
	/**선언*/
	JDialog dialog;
	Container dialogContainer;
	SalesDAO sdao = new SalesDAO();
	SalesDTO sdto = new SalesDTO();
	
	//기간별 매출 검색
	Manage manage = Manage.getInstance();
	JButton button;
	UtilDateModel model1; //검색 시작 날짜
	JDatePanelImpl datePanel1;
	JDatePickerImpl datePicker1;
	UtilDateModel model2; //검색 종료 날짜
	JDatePanelImpl datePanel2;
	JDatePickerImpl datePicker2;
	JButton b_list; 
	JPanel p_showAll;
	JTable table;
	
	
	/**초기화*/
	//1. 당일 매출 확인
	public Manage_Sales() {
		dialog = new JDialog();
		dialogContainer = dialog.getContentPane();
		dialog.setTitle("금일 매출 현황");
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
	
	//2. 기간별 매출 확인
	public Manage_Sales(Manage manage) {
		this.manage = manage;
		dialog = new JDialog();
		dialogContainer = dialog.getContentPane();
		dialog.setTitle("매출 기간 선택");
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

	
	
	/**화면 구성*/
	//1. 당일 매출 확인용 화면
	private void init1() {
	
		dialogContainer.setLayout(new BorderLayout());
		
		//salesDAO에서 정보 받아오기
		sdto = sdao.select_day(); //오늘 매출 dto 반환
		int computer = sdto.getComputer();
		int food = sdto.getFood();
		int tot_sales = sdto.getTot_sales();
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		/*
		//패널에 선 추가
		JPanel panel2 = new JPanel() { //바탕 패널 (이 패널을 dialogContainer Center에 넣음
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponents(g);
				setOpaque(false);  //panel을 투명하게 설정
				g.drawLine(30, 80, 200, 80); //글씨 위치 잡은 후 밑줄 위치 조정해야 함
			}
		};*/

		//타이틀
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String date = String.valueOf(month) +"월"+String.valueOf(day)+"일";

		JLabel title = new JLabel("[  " +date + "   매출 현황  ]", JLabel.CENTER);
		title.setPreferredSize(new Dimension(300, 50));


		//매출
		JPanel p_Left = new JPanel(new GridLayout(3, 1));
		JPanel p_Right = new JPanel(new GridLayout(3, 1));

		JLabel l_com = new JLabel("PC 매출: ", JLabel.RIGHT);
		JLabel l_product = new JLabel("상품 매출: ", JLabel.RIGHT);
		JLabel l_tot = new JLabel("총 매출: ", JLabel.RIGHT);

		JLabel com = new JLabel("  "+String.valueOf(computer)+"원");
		JLabel product = new JLabel("  "+String.valueOf(food)+"원");
		JLabel tot = new JLabel("  "+String.valueOf(tot_sales)+"원");

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
	
	
	//2. 기간별 매출 현황 화면 - 날짜 선택 기능
	private void init2() {
		dialogContainer.setLayout(new GridLayout(1, 1));
		
		LineBorder lineborder = new LineBorder(new Color(0X5D5D5D),6);
		EtchedBorder etchedBorder = new EtchedBorder();
		CompoundBorder compound = new CompoundBorder(lineborder,etchedBorder);
		
		//바탕 패널
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBorder(compound);
		panel.setBackground(new Color(0X5D5D5D));
		
		//부속 패널
		JPanel p_west = new JPanel(new GridLayout(2, 1));
		JPanel p_center = new JPanel(new GridLayout(2, 1));
		p_west.setBackground(new Color(0X5D5D5D));
		p_center.setBackground(new Color(0X5D5D5D));
		
		//제목
		JLabel l_title = new JLabel("[ 확인하실 기간을 선택해주세요 ]",JLabel.CENTER);
		l_title.setBackground(Color.DARK_GRAY);
		l_title.setForeground(Color.LIGHT_GRAY);
		l_title.setOpaque(true);
		l_title.setPreferredSize(new Dimension(300, 50));		
		
		//버튼
		button = new JButton("기간 선택 완료");
		button.setPreferredSize(new Dimension(300, 40));
		button.setBackground(Color.DARK_GRAY);
		button.setForeground(Color.LIGHT_GRAY);
		button.setBorder(new BevelBorder(BevelBorder.RAISED));
		button.addActionListener(this);  //버튼 눌렀을 시 선택한 날짜 읽어오고 해당 데이터 출력
		button.setFocusPainted(false);
		
		//시작일, 종료일 라벨
		JLabel l_start = new JLabel("   시작일: ", JLabel.RIGHT);
		JLabel l_end = new JLabel("   종료일: ", JLabel.RIGHT);
		l_start.setForeground(Color.LIGHT_GRAY);
		l_end.setForeground(Color.LIGHT_GRAY);
		//l_start.setPreferredSize(new Dimension(80, 30));
		//l_end.setPreferredSize(new Dimension(80, 30));
		
		p_west.add(l_start);
		p_west.add(l_end);
		
		
		//jDatePicker
		//검색 시작일
		model1 = new UtilDateModel();
		datePanel1 = new JDatePanelImpl(model1);
		datePicker1 = new JDatePickerImpl(datePanel1);
		//datePicker1.setBounds(100,170,200,30);
		//String start=(String)datePicker1.getModel().getValue();
		//System.out.println(start);
		
		//검색 종료일
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

	
	//3. 기간별 매출 현황 화면 - 검색 결과 출력 기능 (테이블)
	public void showSales(String start, String end) {
		System.out.println("start: "+start);
		System.out.println("end: "+end);
		
		List<SalesDTO> list = new ArrayList<SalesDTO>();
		list = sdao.select_day(start, end); //sql 실행
		
		//출력할 매출 데이터가 존재하지 않을 경우
		String no_data = "매출정보가 존재하지 않습니다.";
		System.out.println(list.get(0).getDays());
		if (list.get(0).getDays().equals(no_data)) {
			JOptionPane.showMessageDialog(manage, no_data);
			menuEnable();
			manage.setEnabled(true);
			return;
		}
		
		//출력할 매출 데이터가 있을 경우
		manage.p_seatLayout.setVisible(false);
		
		b_list = new JButton("확  인");
		p_showAll = new JPanel(new BorderLayout());

		//바탕 패널 설정
		p_showAll = new JPanel(new BorderLayout(5,5)); //좌석 레이아웃 대신 보여줄 패널
		p_showAll.setBorder(manage.compoundBorder2);
		p_showAll.setBackground(new Color(0X5D5D5D));

		//제목 설정
		JLabel title = new JLabel("<검색 기간 매출 목록>", JLabel.CENTER);
		title.setFont(new Font("Malgun Gothic",Font.BOLD,14));
		title.setPreferredSize(new Dimension(500, 50));
		//title.setBorder(compoundBorder2);
		title.setForeground(Color.LIGHT_GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setOpaque(true);
		
		//버튼 설정
		b_list.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_list.setBackground(Color.DARK_GRAY);
		b_list.setForeground(Color.LIGHT_GRAY);
		b_list.setPreferredSize(new Dimension(30, 30));
		b_list.addActionListener(this); //버튼 누르면 목록 종료되고 다시 seat 보이도록 이벤트 설정
		b_list.setFocusPainted(false);
		
		JPanel p_list = new JPanel(new GridLayout(0,1)); //테이블이 추가될 패널
		p_list.setBackground(new Color(0X5D5D5D));
		
		//JTable 생성
		String[] str = {"날짜","PC 이용 매출","상품 판매 매출","총 매출"};
		DefaultTableModel model = new DefaultTableModel(str, 0);
		System.out.println(list.size());
		table = new JTable(model);
		
		int tot_com=0;
		int tot_food=0;
		int tot_tot=0;
		for (int i=0;i<list.size();i++) {
			String date = list.get(i).getDays();
			date = date.substring(0, 8); //날짜 정보만 잘라내기
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
		String[] total = {"총 매출", "  "+String.valueOf(tot_com), "  "+String.valueOf(tot_food), "  "+String.valueOf(tot_tot)};
		model.addRow(empty);
		model.addRow(total);

		//JTable 설정
		//table.getColumnModel().getColumn(0).setMaxWidth(40); //첫 열 너비 지정
		table.setRowHeight(30); //행 높이 지정
		table.getTableHeader().setFont(new Font("Malgun Gothic",Font.BOLD,13)); //제목행 폰트 지정
		table.getTableHeader().setBackground(Color.DARK_GRAY);
		table.getTableHeader().setForeground(Color.LIGHT_GRAY);
		table.getTableHeader().setPreferredSize(new Dimension(500, 30)); //제목행 높이 지정
		table.getTableHeader().setReorderingAllowed(false); //드래그앤드롭으로 컬럼 이동 방지
		//드래그앤드롭으로 각각의 행 너비 조절 불가 설정
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.setBackground(Color.LIGHT_GRAY); //레코드 바탕색 지정
		table.setEnabled(false); //편집 불가 & 클릭 불가

		//첫 행 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setCellRenderer(dtcr);
		
		//다른 행 우측 정렬
		/*DefaultTableCellRenderer dtcr2 = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
		TableColumnModel tcm2 = table.getColumnModel();
		tcm2.getColumn(1).setCellRenderer(dtcr2);
		tcm2.getColumn(2).setCellRenderer(dtcr2);
		tcm2.getColumn(3).setCellRenderer(dtcr2);*/

		//스크롤 설정
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setWheelScrollingEnabled(true); //마우스휠로 스크롤 이동 가능
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setOpaque(true);
		p_list.add(scrollPane);
		//p_list.setBorder(compoundBorder2);
		p_list.setBackground(Color.DARK_GRAY);


		//패널에 테이블 & 버튼 조립
		p_showAll.add("North",title);
		p_showAll.add("Center",p_list);
		p_showAll.add("South", b_list);

		//패널 추가
		manage.container.add("Center",p_showAll);
		manage.container.revalidate();
		manage.container.repaint();
	}
	
	//메뉴바 설정
	public void menuDisable() {
		manage.menuUser.setEnabled(false);  //다이얼로그 출력된동안 메뉴바 선택 불가
		manage.menuSales.setEnabled(false);
		manage.menuOrder.setEnabled(false);
	}
	
	public void menuEnable() {
		manage.menuUser.setEnabled(true);  //다이얼로그 종료되면 다시 메뉴바 선택 가능
		manage.menuSales.setEnabled(true);
		manage.menuOrder.setEnabled(true);
	}
	
	
	/**이벤트 설정*/
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//기간 선택 후 확인 버튼 눌렀을 때
		if (e.getSource()==button) {
			SimpleDateFormat date_format = new SimpleDateFormat("yy/MM/dd"); //sql용 포맷
			SimpleDateFormat date_format2 = new SimpleDateFormat("yyyyMMdd"); //날짜 비교용 포맷
			Date start=(Date)datePicker1.getModel().getValue();
			Date end=(Date)datePicker2.getModel().getValue();
			
			//날짜를 선택하지 않았을 경우
			if (start==null || end==null) {
				JOptionPane.showInternalMessageDialog(dialogContainer, "날짜를 선택해주세요");
				return;
			}
			
			//검색 종료일이 검색 시작일 보다 빠를 경우 다시 선택하도록 설정
			int start_num = Integer.parseInt(date_format2.format(start));
			int end_num = Integer.parseInt(date_format2.format(end));
			System.out.println("start_num"+start_num);
			System.out.println("end_num"+end_num);
			
			if (start_num>end_num) {
				JOptionPane.showInternalMessageDialog(dialogContainer, "검색 종료일은 검색 시작일보다\n 빠를 수 없습니다");
			}
			
			//검색 시작일 & 종료일이 올바른 경우
			else {
			
			//DB 검색하기위해 날짜를 포맷 맞춰서 string형으로 변환 (yy/MM/dd)
			String start_str = date_format.format(start).toString();
			String end_str = date_format.format(end).toString();
			
			dialog.dispose();
			manage.setEnabled(true);
			showSales(start_str, end_str); //테이블 출력 함수
			}
		}
		
		//선택한 기간 매출 테이블 출력 후 확인 버튼 눌렀을 때 (원래의 자리 레이아웃으로 복귀)
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
