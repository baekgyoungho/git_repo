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
	
	/**1.드라이버  로딩*/
	public SalesDAO() {
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
		
		return conn;
	}
	
	
	/**3.SQL문 처리*/
	/**
	 * 입력 : 없음
	 * 출력 : 없음
	 */
	public void insert() {
		// DB 처리
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "insert into sales values (to_date(sysdate, 'yyyy-mm-dd'), 0, 0, 0)";
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate(); //실행 -> 처리된 결과 개수가 리턴됨
		} catch (SQLException e) {
			System.out.println("salesDAO insert 입력 에러");
			//e.printStackTrace();
		} finally {
			close(null, pstmt, conn);
		}
		System.out.println(result+"개의 행이 만들어졌습니다.");	
	}// - insert end
	
	//  - 당일 매출확인
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
			
			//검색 결과가 없을 경우
			if (list.size()==0) {
				dto = new SalesDTO();
				dto.setDays("매출정보가 존재하지 않습니다.");
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		return list;	
	}
	
	//매출정보 업데이트
	/**
	 * 
	 * @param code 1.computer 2.food
	 * @param money 사용금액(분)
	 * @return
	 */
	public int update(int code, int money) {
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = null;
		SalesDTO dto = select_day();
		
		if(code ==1) { //pc이용시간
			money = money + dto.getComputer();
			sql = "update sales set computer =?, tot_sales =food+? where days like sysdate";
		}else if(code==2) {//상품 구매
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
	 * 입력 : 날짜
	 * 출력 : 없음
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
			System.out.println("삭제실패");
			//e.printStackTrace();
		} finally {
            close(null,pstmt,conn);
		}
		System.out.println(result+"개의 행을 삭제하였습니다.");
	}// - delete end

}
