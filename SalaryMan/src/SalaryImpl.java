import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SalaryImpl implements Salary{
	List<SalaryVO> list = new ArrayList<SalaryVO>();
	Scanner sc = new Scanner(System.in);
	@Override
	public void input() {
		SalaryVO so = new SalaryVO();
		int sum_pay;//기본급 + 수당
		System.out.print("이름 입력 : ");
		so.setName(sc.next());
		System.out.print("직급 입력 : ");
		so.setPosition(sc.next());
		System.out.print("기본급 입력 : ");
		so.setBasic_pay(sc.nextInt());
		System.out.print("수당 입력 : ");
		so.setExtra_pay(sc.nextInt());
		
		sum_pay = so.getBasic_pay() + so.getExtra_pay();
		
		so.setTax(calc_tax(sum_pay));//세금
		so.setSalary(sum_pay-(int)(sum_pay*so.getTax()));//월급
		
		list.add(so);
		
	}
	
	

	@Override
	public void print() {
		Iterator<SalaryVO> it = list.iterator();
		print_title();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	@Override
	public void update() {
		Iterator<SalaryVO> it = list.iterator();
		SalaryVO so;
		int sum_pay;
		System.out.print("수정할 이름 입력 : ");
		String name = sc.next();
		int i = 0;
		while(it.hasNext()) {
			if(it.next().getName().equals(name)) {
				so = list.get(i);
				list.remove(i);
				System.out.print("직급 입력 : ");
				so.setPosition(sc.next());
				System.out.print("기본급 입력 : ");
				so.setBasic_pay(sc.nextInt());
				System.out.print("수당 입력 : ");
				so.setExtra_pay(sc.nextInt());
				
				sum_pay = so.getBasic_pay() + so.getExtra_pay();
				
				so.setTax(calc_tax(sum_pay));//세금
				so.setSalary(sum_pay-(int)(sum_pay*so.getTax()));//월급

				list.add(i,so);
				break;
				
			}
			i++;
		}
	}

	@Override
	public void select() {
		Iterator<SalaryVO> it = list.iterator();
		SalaryVO so;
		System.out.print("이름 입력 : ");
		String name = sc.next();
		while(it.hasNext()) {
			so = it.next();
			if(so.getName().equals(name)) {
				print_title();
				System.out.println(so.toString());
			}

		}
	}

	@Override
	public void end() {
		System.out.println("프로그램 종료");
		System.exit(0);
		
	}



	@Override
	public double calc_tax(int pay) {
		double tax = 0.03;
		if(pay<=400) tax = 0.02;
		if(pay<=200) tax = 0.01;
		return tax;
		
	}

	@Override
	public void print_title() {
		System.out.printf("이름\t직급\t기본급\t수당\t세금\t월급\n");
		
	}
	
	

}
