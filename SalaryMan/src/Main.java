class Calculator {
	public int evaluate(String expression) {
		int sum = expression.charAt(0)-48;
		char op= ' ';
		for(int i=1;i<expression.length();i++){
			if(expression.charAt(i)=='+'){
				op = '+';
				continue;
			}
			if(expression.charAt(i)=='-'){
				op = '-';
				continue;
			}
			if(expression.charAt(i)=='*'){
				op = '*';
				continue;
			}
			if(expression.charAt(i)=='/'){
				op = '/';
				continue;
			}
				if(op=='+') {
					sum = sum + (expression.charAt(i)-48);
					op = ' ';
				}
				if(op=='-'){ 
					sum = sum -  (expression.charAt(i)-48);
					op = ' ';
				}
				if(op=='*') {
					sum = sum * (expression.charAt(i)-48);
					op = ' ';
				}
				if(op=='/'){
					sum = sum / (expression.charAt(i)-48);
					op = ' ';
				}

		}
		return sum;
	}
}

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculator calculator = new Calculator();
		int sum = calculator.evaluate("1-2*3");
		
		System.out.println("1-2*3 = " + sum);
	}
}