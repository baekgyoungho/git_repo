
public class SalaryVO {
	
	private String name;//�̸�
	private String position;//����
	private int basic_pay;//�⺻��
	private int extra_pay;//����
	private double tax;//����
	private int salary;//����
	
	SalaryVO(){
		
	}
	
	
	@Override
	public String toString() {
		return String.format("%s\t%s\t%d\t%d\t%.2f\t%d"
				, name,position,basic_pay,extra_pay,tax,salary);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getBasic_pay() {
		return basic_pay;
	}

	public void setBasic_pay(int basic_pay) {
		this.basic_pay = basic_pay;
	}

	public int getExtra_pay() {
		return extra_pay;
	}

	public void setExtra_pay(int extra_pay) {
		this.extra_pay = extra_pay;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

}
