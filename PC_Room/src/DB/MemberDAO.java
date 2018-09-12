package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

	public Connection conn = null;
	
	
	/**1.����̹�  �ε�*/
	public MemberDAO() {
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
	public void close() {
		try {
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
		//this.conn = conn;
		return conn;
	}
	
	//select_all �� �� ���
	public void getConnection1() {
		//Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "jspexam";
		String password = "m1234";
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
		//this.conn = conn;
		//return conn;
	}
	
	
	/**3.SQL�� ó��*/
	//1. ȸ�� ���
	public int insert(String id, String pw, String name, String phone) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		String sql = "insert into users values (?, ?, ?, ?, sysdate, 0, 0, 0)";
		int result = 0;

		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.setString(3, name);
			psmt.setString(4, phone);
			
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn); //������ ���� ���� ��� null �Է�
		}
		
		return result;
	}
	
	//2. ID �˻� (=ID ã��)
		public String find_id(String name, String phone) {
			Connection conn = getConnection();
			PreparedStatement psmt = null;
			ResultSet rs=null;
			String result = "";
			String sql = "select * from users where name=? and phone=?";
			
			try {
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, name);
				psmt.setString(2, phone);
				
				rs = psmt.executeQuery();
				
				while (rs.next()) {
					result = "���̵�: "+rs.getString("id");
				}
				if (result.equals("")) result = "��ϵ��� ���� ���̵��Դϴ�";
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs, psmt, conn);
			}
			return result;		
		}
		
	//2-1. ȸ������ �˻� (���̵�� �˻��ϰ� ȸ������ dto ���·� ��ȯ)
		public MemberDTO search_user(String id) {
			Connection conn = getConnection();
			PreparedStatement psmt = null;
			ResultSet rs=null;
			MemberDTO dto = new MemberDTO();
			String sql = "select * from users where id=?";
			
			try {
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, id);
				
				rs = psmt.executeQuery();
				
				while (rs.next()) {
					dto.setId(rs.getString("id"));
					dto.setPw(rs.getString("pw"));
					dto.setName(rs.getString("name"));
					dto.setPhone(rs.getString("phone"));
					dto.setJoinDate(rs.getString("joindate"));
					dto.setMoney(rs.getInt("money"));
					dto.setTot_time(rs.getInt("tot_time"));
					dto.setTot_money(rs.getInt("tot_money"));
				}
				
				if (dto==null || dto.getId()==null||dto.getId().equals("")) {
					dto.setId("not found"); //��ϵ��� ���� ���̵��� ��� 00��ȯ
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs, psmt, conn);
			}
			return dto;		
		}
	
	//3. ��й�ȣ(pw) �˻� (=��й�ȣ ã�� 1111�� ��ȯ)
	public String find_pw(String id, String phone) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs=null;
		String result = "";
		String sql = "select * from users where id=? and phone=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, phone);
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				result=rs.getString("pw");
			}
			if (!result.equals("")) {
				result = "�ӽ� ��й�ȣ: 1111";
				update_pw(id);   //��й�ȣ ���� �Լ� ȣ��
			} else {
				result = "�ش� ID�� �˻��� �� �����ϴ�";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return result;		
	}
	
	//4. �α���
	public MemberDTO login(String id, String pw) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs=null;
		MemberDTO dto = new MemberDTO();

		String sql = "select * from users where id=? and pw=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				//dto=new MemberDTO();
				//dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setJoinDate(rs.getString("joinDate"));
				dto.setMoney(rs.getInt("money"));
				dto.setTot_time(rs.getInt("tot_time"));
				dto.setTot_money(rs.getInt("tot_money"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return dto;		
	}
	
	//5. ���̵� �ߺ�üũ (��� ������ ���̵�=0 ��ȯ / �ߺ� ���̵�=1 ��ȯ)
	public int check_id(String id) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs=null;
		String sql = "select * from users where id=?";
		
		String str = "";
		int result = 0;
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				str =rs.getString("id");
			}
			
			if (str.equals(id)) result=1; //�ߺ� ���̵��� ��� 1 ����
			else result=0;  //��� ������ ���̵��� ��� 0 ����
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return result;		
	}
	
	//6. ȸ������ ����
	public int update(String id, String pw, String phone) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "update users set pw=?, phone=? where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pw);
			psmt.setString(2, phone);
			psmt.setString(3, id);
			
			result = psmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	//7. ��й�ȣ�� ���� (��й�ȣ ã�� �� ȣ��. �ܺ� Ŭ�������� ȣ�� �Ұ�)
	private void update_pw(String id) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;

		String sql = "update users set pw=1111 where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
	}
	
	//8. ȸ�� Ż�� - Ż�� ���� �� 1 ��ȯ
	public int delete (String id, String pw) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "delete from users where id=? and pw=?";
		
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			
			result = psmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	//9. ��� ����
	/*
	public void update_money(String id, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;

		int moneyBefore = check_money(id); //�ܾ� Ȯ�� �Լ� ȣ��
		String sql = "update users set money=? where id=?";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, (moneyBefore+money));
			psmt.setString(2, id);
			psmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
	}*/
	
	//10. ���� �ܾ� Ȯ��
	public int check_money(String id) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int result =0;

		String sql = "select money from users where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			while(rs.next()) {				
				result = rs.getInt("money"); //DB���� �ܾ� �޾ƿ�
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	//11. ��ü ȸ�� ����Ʈ ���
	public List<MemberDTO> selectAll (){
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		String sql = "select * from users order by id";
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				//dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setJoinDate(rs.getString("joinDate"));
				dto.setMoney(rs.getInt("money"));
				dto.setTot_time(rs.getInt("tot_time"));
				dto.setTot_money(rs.getInt("tot_money"));
				
				list.add(dto);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs,psmt,conn);
		}
		return list;		
	}
	
	//12. �ܾ� �����ϱ�
	public int save_money(String id, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "update users set money=? where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1,money);
			psmt.setString(2, id);
			result = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	//13. �� ��� �ݾ� ������Ʈ (��ǰ ���� ���� & �ð����� ���� �� ȣ��)
	public int update_totMoney(String id, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "update users set tot_money=tot_money+? where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1,money);
			psmt.setString(2, id);
			result = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	

	//14. ����� �α׾ƿ�/������ �α׾ƿ� �� �ܾ�, ��  ��� �ݾ� ������Ʈ (�ð��� ������ ȯ���ؼ� ����)
	public int save_all(String id, int remain_time, int use_time) {
						//�Ű�����: id, �ܾ�(=���� �ð�), ���ݾ� (=��� �ð�)
		int check=1;
		
		//Connection conn = getConnection();
		if (conn==null) {
			getConnection1();  //�ݺ��� �ƴ� ���
			check=2;
		}
		
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "update users set money=?, tot_time=tot_time+?, tot_money=tot_money+? where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, remain_time); //�ܾ�
			psmt.setInt(2, use_time); //�� ��� �ð�
			psmt.setInt(3, use_time); //�� ��� �ݾ�
			psmt.setString(4,  id);
			result = psmt.executeUpdate();			
		} 		catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			if (check==1) close(null, psmt, null);
			else if (check==2) close(null,psmt,conn);
		}
		return result;
	}
}
