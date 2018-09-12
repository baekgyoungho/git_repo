package DB;

import java.io.Serializable;

public class OrderListDTO implements Serializable{
	private int seatno;
	private String product_name;  //상품이름
	private int product_count; //상품 수량
	private int product_price; //상품 총가격
	
	public OrderListDTO() {}
	public OrderListDTO(int seatno, String product_name, int product_count, int product_price) {
		super();
		this.seatno = seatno;
		this.product_name = product_name;
		this.product_count = product_count;
		this.product_price = product_price;
	}
	
	public int getSeatno() {
		return seatno;
	}
	public void setSeatno(int seatno) {
		this.seatno = seatno;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getProduct_count() {
		return product_count;
	}
	public void setProduct_count(int product_count) {
		this.product_count = product_count;
	}
	public int getProduct_price() {
		return product_price;
	}
	public void setProduct_price(int product_price) {
		this.product_price = product_price;
	}
}
