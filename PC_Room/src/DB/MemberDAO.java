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
	
	
	/**1.드라이버  로딩*/
	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
			e.printStackTrace();
		}
	}
	
	//close 처리
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
	
	/**2.DB에 연결하기*/
	
	public Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "jspexam";
		String password = "m1234";
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("접속 실패");
			e.printStackTrace();
		}
		//this.conn = conn;
		return conn;
	}
	
	//select_all 일 때 사용
	public void getConnection1() {
		//Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "jspexam";
		String password = "m1234";
		
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("접속 실패");
			e.printStackTrace();
		}
		//this.conn = conn;
		//return conn;
	}
	
	
	/**3.SQL문 처리*/
	//1. 회원 등록
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
			close(null, psmt, conn); //전달할 값이 없을 경우 null 입력
		}
		
		return result;
	}
	
	//2. ID 검색 (=ID 찾기)
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
					result = "아이디: "+rs.getString("id");
				}
				if (result.equals("")) result = "등록되지 않은 아이디입니다";
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs, psmt, conn);
			}
			return result;		
		}
		
	//2-1. 회원정보 검색 (아이디로 검색하고 회원정보 dto 형태로 반환)
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
					dto.setId("not found"); //등록되지 않은 아이디일 경우 00반환
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs, psmt, conn);
			}
			return dto;		
		}
	
	//3. 비밀번호(pw) 검색 (=비밀번호 찾고 1111로 변환)
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
				result = "임시 비밀번호: 1111";
				update_pw(id);   //비밀번호 수정 함수 호출
			} else {
				result = "해당 ID를 검색할 수 없습니다";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return result;		
	}
	
	//4. 로그인
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
	
	//5. 아이디 중복체크 (사용 가능한 아이디=0 반환 / 중복 아이디=1 반환)
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
			
			if (str.equals(id)) result=1; //중복 아이디일 경우 1 리턴
			else result=0;  //사용 가능한 아이디일 경우 0 리턴
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return result;		
	}
	
	//6. 회원정보 수정
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
	
	//7. 비밀번호만 수정 (비밀번호 찾기 시 호출. 외부 클래스에서 호출 불가)
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
	
	//8. 회원 탈퇴 - 탈퇴 성공 시 1 반환
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
	
	//9. 요금 결제
	/*
	public void update_money(String id, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;

		int moneyBefore = check_money(id); //잔액 확인 함수 호출
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
	
	//10. 현재 잔액 확인
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
				result = rs.getInt("money"); //DB에서 잔액 받아옴
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		return result;
	}
	
	//11. 전체 회원 리스트 출력
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
	
	//12. 잔액 저장하기
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
	
	//13. 총 사용 금액 업데이트 (상품 현금 결제 & 시간으로 결제 시 호출)
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
	
	

	//14. 사용자 로그아웃/관리자 로그아웃 시 잔액, 총  사용 금액 업데이트 (시간을 돈으로 환산해서 저장)
	public int save_all(String id, int remain_time, int use_time) {
						//매개변수: id, 잔액(=남은 시간), 사용금액 (=사용 시간)
		int check=1;
		
		//Connection conn = getConnection();
		if (conn==null) {
			getConnection1();  //반복문 아닐 경우
			check=2;
		}
		
		PreparedStatement psmt = null;
		int result = 0;
		String sql = "update users set money=?, tot_time=tot_time+?, tot_money=tot_money+? where id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, remain_time); //잔액
			psmt.setInt(2, use_time); //총 사용 시간
			psmt.setInt(3, use_time); //총 사용 금액
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
