package DB;

import java.io.Serializable;

import DB.MemberDTO;

public class User_infoDTO implements Serializable{
	private MemberDTO dto;      // 회원정보
	private int seatNum;        // 자리번호
	private String remain_time; // 남은시간 - String
	private int r_time;         // 남은시간 - Int
	private int u_time;         // 사용시간
	
	public User_infoDTO() {};
	public User_infoDTO(MemberDTO dto,int seatNum,String remain_time,int r_time, int u_time){
		this.dto = dto;
		this.seatNum = seatNum;
		this.remain_time = remain_time;	
		this.r_time = r_time;	
		this.u_time = u_time;	
		
	}

	public MemberDTO getDto() {
		return dto;
	}

	public void setDto(MemberDTO dto) {
		this.dto = dto;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getRemain_time() {
		return remain_time;
	}

	public void setRemain_time(String remain_time) {
		this.remain_time = remain_time;
	}

	public int getR_time() {
		return r_time;
	}

	public void setR_time(int r_time) {
		this.r_time = r_time;
	}

	public int getU_time() {
		return u_time;
	}

	public void setU_time(int u_time) {
		this.u_time = u_time;
	}
	
	
}
