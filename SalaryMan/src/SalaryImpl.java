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
		int sum_pay;//�⺻�� + ����
		System.out.print("�̸� �Է� : ");
		so.setName(sc.next());
		System.out.print("���� �Է� : ");
		so.setPosition(sc.next());
		System.out.print("�⺻�� �Է� : ");
		so.setBasic_pay(sc.nextInt());
		System.out.print("���� �Է� : ");
		so.setExtra_pay(sc.nextInt());
		
		sum_pay = so.getBasic_pay() + so.getExtra_pay();
		
		so.setTax(calc_tax(sum_pay));//����
		so.setSalary(sum_pay-(int)(sum_pay*so.getTax()));//����
		
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
		System.out.print("������ �̸� �Է� : ");
		String name = sc.next();
		int i = 0;
		while(it.hasNext()) {
			if(it.next().getName().equals(name)) {
				so = list.get(i);
				list.remove(i);
				System.out.print("���� �Է� : ");
				so.setPosition(sc.next());
				System.out.print("�⺻�� �Է� : ");
				so.setBasic_pay(sc.nextInt());
				System.out.print("���� �Է� : ");
				so.setExtra_pay(sc.nextInt());
				
				sum_pay = so.getBasic_pay() + so.getExtra_pay();
				
				so.setTax(calc_tax(sum_pay));//����
				so.setSalary(sum_pay-(int)(sum_pay*so.getTax()));//����

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
		System.out.print("�̸� �Է� : ");
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
		System.out.println("���α׷� ����");
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
		System.out.printf("�̸�\t����\t�⺻��\t����\t����\t����\n");
		
	}
	
	

}
