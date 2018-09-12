package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {
	
	/**1.����̹�  �ε�*/
	public SalesDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����");
			e.printStackTrace();
		}
	}
	
	//close ó��
	public void close(ResultSet rs, PreparedStatement psmt, Connection conn) {
		try {
			if (rs!=null) rs.close();
			if (psmt!=null) psmt.close();
			if (conn!=null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**2.DB�� �����ϱ�*/
	public Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "jspexam";
		String password = "m1234";
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	/**3.SQL�� ó��*/
	/**
	 * �Է� : ����
	 * ��� : ����
	 */
	public void insert() {
		// DB ó��
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "insert into sales values (to_date(sysdate, 'yyyy-mm-dd'), 0, 0, 0)";
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate(); //���� -> ó���� ��� ������ ���ϵ�
		} catch (SQLException e) {
			System.out.println("salesDAO insert �Է� ����");
			//e.printStackTrace();
		} finally {
			close(null, pstmt, conn);
		}
		System.out.println(result+"���� ���� ����������ϴ�.");	
	}// - insert end
	
	//  - ���� ����Ȯ��
	public SalesDTO select_day() {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs=null;
		
		SalesDTO dto = null;
		String sql = "select * from sales where days like sysdate";
		
		try {
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				dto=new SalesDTO();
				dto.setDays(rs.getString("days"));
				dto.setComputer(rs.getInt("computer"));
				dto.setFood(rs.getInt("food"));
				dto.setTot_sales(rs.getInt("tot_sales"));
			} 
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return dto;	
	}
	
	public List<SalesDTO> select_day(String begin_date,String end_date) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs=null;
		SalesDTO dto = null;
		List<SalesDTO> list = new ArrayList<>();

		//String sql = "select * from sales where days between ? and ?";
		String sql = "select * from sales where days between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') order by days";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, begin_date);
			psmt.setString(2, end_date);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
					dto=new SalesDTO();
					dto.setDays(rs.getString("days"));
					dto.setComputer(rs.getInt("computer"));
					dto.setFood(rs.getInt("food"));
					dto.setTot_sales(rs.getInt("tot_sales"));
					list.add(dto);
			} 
			
			//�˻� ����� ���� ���
			if (list.size()==0) {
				dto = new SalesDTO();
				dto.setDays("���������� �������� �ʽ��ϴ�.");
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return list;	
	}
	
	//�������� ������Ʈ
	/**
	 * 
	 * @param code 1.computer 2.food
	 * @param money ���ݾ�(��)
	 * @return
	 */
	public int update(int code, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = null;
		SalesDTO dto = select_day();
		
		if(code ==1) { //pc�̿�ð�
			money = money + dto.getComputer();
			sql = "update sales set computer =?, tot_sales =food+? where days like sysdate";
		}else if(code==2) {//��ǰ ����
			money = money + dto.getFood();
			sql = "update sales set food =?, tot_sales =computer+? where days like sysdate";
		}
		
		try {
			psmt = conn.prepareStatement(sql);
			//System.out.println(money);
			//System.out.println(dto.getTot_sales());
			psmt.setInt(1, money);
			psmt.setInt(2, money);
			
			result = psmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	
	
	/**
	 * �Է� : ��¥
	 * ��� : ����
	 */
	public void delete(String day) {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;;

		int result = 0;
		String sql = "delete sales where name like ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+day+"%");
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("��������");
			//e.printStackTrace();
		} finally {
            close(null,pstmt,conn);
		}
		System.out.println(result+"���� ���� �����Ͽ����ϴ�.");
	}// - delete end

}
