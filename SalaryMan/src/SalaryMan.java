import java.util.Scanner;

public class SalaryMan {

	public static void main(String[] args) {
		
		Scanner sc= new Scanner(System.in);
		Salary so = new SalaryImpl();
		int select;
		
		while(true) {
			do {
				System.out.println("1.���");
				System.out.println("2.���");
				System.out.println("3.����");
				System.out.println("4.�˻�");
				System.out.println("5.����");
				System.out.print("���� : ");
				select = sc.nextInt(); 
			}while(select<1 || select >5);
			
			switch(select) {
			case 1: so.input();
				break;
			case 2: so.print();
				break;
			case 3: so.update();
				break;
			case 4: so.select();
				break;
			case 5:	so.end();
			}
		}
	}
}
