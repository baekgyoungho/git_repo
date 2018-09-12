package DB;

public class SalesDTO {
	private String days;
	private int computer; //컴퓨터 사용 요금
	private int food; //상품 주문 요금
	private int tot_sales; //총 매출 (computer + food);

	public SalesDTO() {}
	public SalesDTO(String days, int computer, int food, int tot_sales) {
		this.days = days;
		this.computer = computer;
		this.food = food;
		this.tot_sales = tot_sales;
	}

	//getter & setter
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public int getComputer() {
		return computer;
	}
	public void setComputer(int computer) {
		this.computer = computer;
	}
	public int getFood() {
		return food;
	}
	public void setFood(int food) {
		this.food = food;
	}
	public int getTot_sales() {
		return tot_sales;
	}
	public void setTot_sales(int tot_sales) {
		this.tot_sales = tot_sales;
	}
}
