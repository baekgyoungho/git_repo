package DB;

import java.io.Serializable;

public class MemberDTO implements Serializable{
	//private int num; ///회원 번호 -> setter 없음 (DB 시퀀스 설정)
	private String id;
	private String pw;
	private String name;
	private String phone; 
	private String joinDate; //가입일
	private int money;  //잔액
	private int tot_time;  //총 사용 시간
	private int tot_money;  //총 사용 금액
	
	//생성자
	public MemberDTO() {}
	public MemberDTO(String id, String pw, String name, String phone) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone = phone;
	}	
	
	//getter & setter
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getTot_time() {
		return tot_time;
	}
	public void setTot_time(int tot_time) {
		this.tot_time = tot_time;
	}
	public int getTot_money() {
		return tot_money;
	}
	public void setTot_money(int tot_money) {
		this.tot_money = tot_money;
	}
}
