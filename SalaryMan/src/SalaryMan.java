import java.util.Scanner;

public class SalaryMan {

	public static void main(String[] args) {
		
		Scanner sc= new Scanner(System.in);
		Salary so = new SalaryImpl();
		int select;
		
		while(true) {
			do {
				System.out.println("1.등록");
				System.out.println("2.출력");
				System.out.println("3.수정");
				System.out.println("4.검색");
				System.out.println("5.종료");
				System.out.print("선택 : ");
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
