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

//회원 검색 / 전체 회원 검색 기능 담당 클래스
public class Manage_User extends JDialog implements ActionListener, MouseListener {
	
	/**선언*/
	JDialog dialog;
	Container dialogContainer;
	MemberDTO dto = new MemberDTO();
	MemberDAO dao = new MemberDAO();
	Font labelFont = new Font("SansSherif", 1, 15); //1명 회원정보 조회 시 각 항목명에 적용할 폰트
	
	// - 전체 회원 정보 출력용 테이블
	JTable table;
	
	Color color;
	
	// - 전체 사용자 조회
	Manage manage = Manage.getInstance();
	JButton b_list; 
	JPanel p_showAll;
	
	
	
	/**초기화1 - 전체 사용자 조회용*/
	public Manage_User(Manage manage) {
		this.manage = manage;
		showAll(manage);
	}
	
	
	/**초기화2 - 회원 조회*/
	public Manage_User(Manage manage, MemberDTO dto) {
		dialog = new JDialog(manage,"회원 정보");
		//color = new Color(0X353535);
		
		dialogContainer =  dialog.getContentPane();
		this.dto = dto;
		String check = dto.getId();
		
		dialog.setResizable(false);
		
		if (check.equals("not found")) { //일치하는 아이디가 없을 경우 init1() 실행
			dialog.setBounds(300,150,400,150);
			init1();
		} else { //일치하는 아이디가 있을 경우 init2() 실행
			dialog.setBounds(300,100,300,300);
			init2();
		}
			
		dialog.setVisible(true);
		menuDisable(); //다이얼로그창 실행 동안 메뉴바 비활성화
		
		//x버튼 눌러야 다시 메뉴바 활성화
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menuEnable();//다이얼로그창 종료 시 메뉴바 활성화
				super.windowClosing(e);
			}
		});
	}
	
	
	//전체 회원 조회에서 테이블 레코드 선택 시 실행
	public Manage_User(String id) {
		dialog = new JDialog(manage,"회원 정보");
		
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
				manage.setEnabled(true);;//다이얼로그창 종료 시 메뉴바 활성화
				super.windowClosing(e);
			}
		});
	}
	
	
	//회원 1명 조회 - 없는 아이디일 경우 화면 구성
	private void init1() {
		dialogContainer.setLayout(new BorderLayout());
		JLabel label = new JLabel("등록되지 않은 아이디입니다",  JLabel.CENTER);
		//label.setFont(labelFont);
		
		dialogContainer.add("Center",label);
	}
	
	
	//회원 1명 조회 - 등록된 아이디일 경우 화면 구성
	private void init2() {
		color = new Color(213, 213, 213);
		dialogContainer.setLayout(new BorderLayout());
		dialogContainer.setBackground(color);
		
		LineBorder lineborder = new LineBorder(color,7,true);
		LineBorder lineborder3 = new LineBorder(color,14,true);
		
		
		JLabel title = new JLabel("< 회 원 정 보 >", JLabel.CENTER);
		title.setOpaque(true);
		title.setPreferredSize(new Dimension(300,40));
		title.setBackground(color.DARK_GRAY);
		title.setForeground(color.LIGHT_GRAY);
		title.setFont(labelFont);
		JPanel p_Left = new JPanel(new GridLayout(8, 1));  //항목명 넣을 패널
		p_Left.setBorder(lineborder3);
		p_Left.setBackground(color);
		
		JPanel p_Right = new JPanel(new GridLayout(8, 1)); //출력내용 넣을 패널
		p_Right.setBorder(lineborder3);
		p_Right.setBackground(color);
		//JPanel p_button = new JPanel(); //확인 버튼 넣을 패널
		
		
		//항목명 
		JLabel l_id = new JLabel("아이디: ", JLabel.RIGHT);
		JLabel l_pw = new JLabel("비밀번호: ", JLabel.RIGHT);
		JLabel l_name = new JLabel("이   름: ", JLabel.RIGHT);
		JLabel l_phone = new JLabel("연락처: ", JLabel.RIGHT);
		JLabel l_joinDate = new JLabel("가입일: ", JLabel.RIGHT);
		JLabel l_money = new JLabel("잔  액: ", JLabel.RIGHT);
		JLabel l_totTime = new JLabel("총 이용시간: ", JLabel.RIGHT);
		JLabel l_totMoney = new JLabel("총 이용금액: ", JLabel.RIGHT);
		
		p_Left.add(l_id);
		p_Left.add(l_pw);
		p_Left.add(l_name);
		p_Left.add(l_joinDate);
		p_Left.add(l_phone);
		p_Left.add(l_money);
		p_Left.add(l_totTime);
		p_Left.add(l_totMoney);
		
		
		//출력 내용
		JLabel output_id = new JLabel(dto.getId(), JLabel.CENTER );
		JLabel output_pw = new JLabel(dto.getPw(), JLabel.CENTER);
		JLabel output_name = new JLabel(dto.getName(), JLabel.CENTER);
		JLabel output_joinDate = new JLabel(dto.getJoinDate(), JLabel.CENTER);
		JLabel output_phone = new JLabel(dto.getPhone(), JLabel.CENTER);
		JLabel output_money = new JLabel(String.valueOf(dto.getMoney())+"원", JLabel.CENTER);
		String time = time_make(dto.getTot_time()/20);
		JLabel output_tottime = new JLabel(time, JLabel.CENTER);
		//JLabel output_tottime = new JLabel(String.valueOf(dto.getTot_time()), JLabel.CENTER);
		JLabel output_totmoney = new JLabel(String.valueOf(dto.getTot_money())+"원", JLabel.CENTER);
		
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
	
	
	//전체회원 조회
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
		
		b_list = new JButton("확  인");
		p_showAll = new JPanel(new BorderLayout());
		
		//바탕 패널 설정
		p_showAll = new JPanel(new BorderLayout(5,5)); //좌석 레이아웃 대신 보여줄 패널
		p_showAll.setBorder(manage.compoundBorder2);
		p_showAll.setBackground(new Color(0X5D5D5D));
		
		//제목 설정
		JLabel title = new JLabel("<전체 회원 목록>", JLabel.CENTER);
		title.setFont(new Font("Malgun Gothic",Font.BOLD,14));
		title.setPreferredSize(new Dimension(500, 50));
		//title.setBorder(compoundBorder2);
		title.setForeground(Color.LIGHT_GRAY);
		title.setBackground(Color.DARK_GRAY);
		title.setOpaque(true);
		
		//버튼 설정
		//b_list = new JButton("확인");
		b_list.setBorder(new BevelBorder(BevelBorder.RAISED));
		b_list.setBackground(Color.DARK_GRAY);
		b_list.setForeground(Color.LIGHT_GRAY);
		b_list.setPreferredSize(new Dimension(30, 30));
		b_list.addActionListener(this); //버튼 누르면 목록 종료되고 다시 seat 보이도록 이벤트 설정
		b_list.setFocusPainted(false);
		
	
		JPanel p_list = new JPanel(new GridLayout(0,1)); //테이블이 추가될 패널
		p_list.setBackground(new Color(0X5D5D5D));
		
		
		//JTable 생성
		String[] str = {"No.","아이디","이  름","연락처"};
		DefaultTableModel model = new DefaultTableModel(str, 0) {
			@Override //더블클릭해서 내용 편집 금지
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
		
		//JTable 설정
		table.getColumnModel().getColumn(0).setMaxWidth(40); //첫 열 너비 지정
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

		//첫 행 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setCellRenderer(dtcr);

		//테이블 레코드 클릭 시 해당 회원 정보 팝업 (=회원 정보 출력 함수 사용)
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //단일 선택 설정
		table.addMouseListener(this);
		
		
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
	
	//돈을 시간으로 변환 함수
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
		manage.menuUser.setEnabled(false);  //다이얼로그 출력된동안 메뉴바 선택 불가
		manage.menuSales.setEnabled(false);
		manage.menuOrder.setEnabled(false);
	}
	
	public void menuEnable() {
		manage.menuUser.setEnabled(true);  //다이얼로그 종료되면 다시 메뉴바 선택 가능
		manage.menuSales.setEnabled(true);
		manage.menuOrder.setEnabled(true);
	}
	
	
	
	//이벤트 처리 - actionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//전체 회원 목록 출력된 상태에서 확인 버튼 누르면 원래의 자리 레이아웃으로 복귀
		if (e.getSource() == b_list) {
			manage.menuUser.setEnabled(true);  //다시 메뉴선택 가능
			manage.menuSales.setEnabled(true);
			manage.menuOrder.setEnabled(true);
			
			manage.container.remove(p_showAll);
			manage.container.invalidate();
			manage.p_seatLayout.setVisible(true);
			
			manage.container.revalidate();
			manage.container.repaint();
		}
	}

	
	//이벤트 처리 - mouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow(); //선택한 열 번호 반환
		//int column = table.getSelectedColumn(); //선택한 행 번호 반환
		String id = (String)table.getValueAt(row, 1);
		id = id.trim(); //공백 삭제
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
