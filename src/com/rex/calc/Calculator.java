package com.rex.calc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Calculator {

	JFrame frame;
	JPanel panelTop;
	JPanel panelBottom, panelSTD, panelSCI, panelPRO;
	JTextField textField;
	JLabel label, about;
	CardLayout card;
	JCheckBoxMenuItem STD, SCI, PRO;

	JButton[][] STDButton;
	String[][] STDButtonText = { { "Clear", "\u042FR", "\u2190", "\u00F7" }, { "7", "8", "9", "\u00D7" },
			{ "4", "5", "6", "\u2212" }, { "1", "2", "3", "+" }, { "0", ".", "Ans", "=" } };

	JButton[][] SCIButton;
	String[][] SCIButtonText = { { "sin", "cos", "tan", "Rad", "\u2191" },
			{ "\u03C0", "\u042FR", "\u2302", "\u2190", "Clear" }, { "\u221A", "x\u00B2", "x\u207F", "x!", "\u2211x" },
			{ "7", "8", "9", "(", ")" }, { "4", "5", "6", "\u00D7", "\u00F7" }, { "1", "2", "3", "+", "\u2212" },
			{ "0", ".", "\u1D07", "Ans", "=" } };

	JButton[][] PROButton;
	String[][] PROButtonText = { { "Bin", "Oct", "Dec", "Hex", "\u2191" }, { "Mod", "Lsh", "Rsh", "\u2190", "Clear" },
			{ "A", "B", "C", "And", "Or" }, { "D", "E", "F", "Xor", "Not" }, { "7", "8", "9", "(", ")" },
			{ "4", "5", "6", "\u00D7", "\u00F7" }, { "1", "2", "3", "+", "\u2212" },
			{ "0", ".", "\u1D07", "Ans", "=" } };

	Stack<Character> operator;
	Stack<Double> expression;
	boolean calculated, infoShow, type, inverse, Sh_Ro;
	short opType = 10;
	char[] exp;

	public Calculator() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch (Exception e) {
			System.out.println("Sorry !! Look and Feel not Applied");
		}
		frame = new JFrame("Calculator");
		panelTop = new JPanel();
		panelBottom = new JPanel();
		panelSTD = new JPanel();
		panelSCI = new JPanel();
		panelPRO = new JPanel();
		textField = new JTextField();
		label = new JLabel("0", SwingConstants.RIGHT);
		about = new JLabel(
				"<html><p><b>Created By:</b> Rizwan Raza.<br><b>From:</b> University Polytechnic.<br>Jamia Millia Islamia New Delhi 25.</p></html>",
				SwingConstants.CENTER);
		STDButton = new JButton[5][4];
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 4; ++j) {
				STDButton[i][j] = new JButton(STDButtonText[i][j]);
				STDButton[i][j].setFont(new Font("Calibri", 0, 20));
				panelSTD.add(STDButton[i][j]);
				if ((j < 3 && i == 0) || (j > 1 && i == 4)) {
					continue;
				}
				STDButton[i][j].addActionListener(new PrintAction());
			}
		}
		STDButton[0][0].addActionListener(new ResetAction());
		STDButton[0][1].addActionListener(new InfoAction());
		STDButton[0][2].addActionListener(new BackSpaceAction());
		STDButton[4][2].addActionListener(new AnswerAction());
		STDButton[4][3].addActionListener(new EqualAction());

		SCIButton = new JButton[7][5];
		for (int i = 0; i < 7; ++i) {
			for (int j = 0; j < 5; ++j) {
				SCIButton[i][j] = new JButton(SCIButtonText[i][j]);
				SCIButton[i][j].setFont(new Font("Calibri", 0, 20));
				panelSCI.add(SCIButton[i][j]);
				if ((j > 2 && i == 0) || (j > 0 && (i == 1 || i == 2)) || (j > 2 && i == 6)) {
					continue;
				}
				SCIButton[i][j].addActionListener(new PrintAction());
			}
		}
		SCIButton[0][3].addActionListener(new D_R_G_Action());
		SCIButton[0][4].addActionListener(new ShiftAction());
		SCIButton[1][1].addActionListener(new InfoAction());
		SCIButton[1][2].addActionListener(new QuitAction());
		SCIButton[1][3].addActionListener(new BackSpaceAction());
		SCIButton[1][4].addActionListener(new ResetAction());
		SCIButton[2][1].addActionListener(new SquareAction());
		SCIButton[2][2].addActionListener(new PowerAction());
		SCIButton[2][3].addActionListener(new FactorialAction());
		SCIButton[2][4].addActionListener(new SummationAction());
		SCIButton[6][3].addActionListener(new AnswerAction());
		SCIButton[6][4].addActionListener(new EqualAction());

		PROButton = new JButton[8][5];
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 5; ++j) {
				PROButton[i][j] = new JButton(PROButtonText[i][j]);
				PROButton[i][j].setFont(new Font("Calibri", 0, 20));
				panelPRO.add(PROButton[i][j]);
				if ((i == 0) || (j > 2 && i == 1) || (j > 2 && i == 7)) {
					continue;
				}
				PROButton[i][j].addActionListener(new ActionListener() { // Printable Key Clicked Event
					public void actionPerformed(ActionEvent e) {
						textField.setText(textField.getText() + ((JButton) e.getSource()).getText());
					}
				});
			}
		}

		PROButton[0][0].addActionListener(new BinaryAction());
		PROButton[0][1].addActionListener(new OctalAction());
		PROButton[0][2].addActionListener(new DecimalAction());
		PROButton[0][3].addActionListener(new HexadecimalAction());
		PROButton[0][4].addActionListener(new PROShitfAction());
		// PROButton[1][3].addActionListener(new D_R_G_Action());
		// PROButton[1][4].addActionListener(new ShiftAction());
		// PROButton[2][1].addActionListener(new InfoAction());
		// PROButton[2][2].addActionListener(new QuitAction());
		PROButton[1][3].addActionListener(new BackSpaceAction());
		PROButton[1][4].addActionListener(new ResetAction());
		// PROButton[3][1].addActionListener(new SquareAction());
		// PROButton[3][2].addActionListener(new PowerAction());
		// PROButton[3][3].addActionListener(new FactorialAction());
		// PROButton[3][4].addActionListener(new SummationAction());
		PROButton[7][3].addActionListener(new AnswerAction());
		PROButton[7][4].addActionListener(new EqualAction());

		operator = new Stack<Character>();
		expression = new Stack<Double>();

		panelSCI.setLayout(new GridLayout(7, 5, 5, 5));
		panelSTD.setLayout(new GridLayout(5, 4, 5, 5));
		panelPRO.setLayout(new GridLayout(8, 5, 5, 5));
		card = new CardLayout(0, 0);
		panelPRO.setVisible(false);
		panelSCI.setVisible(false);
		panelBottom.add(panelSTD, "Standard");
		panelBottom.add(panelSCI, "Scientific");
		panelBottom.add(panelPRO, "Programmer");
		panelBottom.setLayout(card);
		panelBottom.setBackground(Color.RED);

		textField.setFont(new Font("Calibri", 0, 24));
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate();
			}
		});

		about.setFont(new Font("Calibri", 0, 16));
		about.setVisible(false);
		about.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		label.setFont(new Font("Calibri", 0, 48));
		label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		panelTop.add(textField);
		panelTop.add(label);
		panelTop.add(about);
		panelTop.setBackground(new Color(200, 200, 200));
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));

		frame.setJMenuBar(createMenuBar());
		frame.add(panelTop, BorderLayout.NORTH);
		frame.add(panelBottom, BorderLayout.CENTER);
		frame.setBackground(new Color(224, 224, 224));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JMenuBar createMenuBar() {
		JMenuBar jmb = new JMenuBar();

		JMenu calcMode = new JMenu("Mode");
		calcMode.setMnemonic(KeyEvent.VK_M);

		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);

		JMenuItem quit = new JMenuItem("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.addActionListener(new QuitAction());

		STD = new JCheckBoxMenuItem("Standard");
		STD.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		STD.setMnemonic(KeyEvent.VK_S);
		STD.addActionListener(new STDAction());

		SCI = new JCheckBoxMenuItem("Scientific");
		SCI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		SCI.setMnemonic(KeyEvent.VK_C);
		SCI.addActionListener(new SCIAction());

		PRO = new JCheckBoxMenuItem("Programmer");
		PRO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
		PRO.setMnemonic(KeyEvent.VK_P);
		PRO.addActionListener(new PROAction());

		JMenuItem about = new JMenuItem("About");
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		about.setMnemonic(KeyEvent.VK_A);

		JMenuItem version = new JMenuItem("Version");
		version.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		version.setMnemonic(KeyEvent.VK_V);

		ButtonGroup bg = new ButtonGroup();
		bg.add(STD);
		bg.add(SCI);
		bg.add(PRO);
		calcMode.add(STD);
		calcMode.add(SCI);
		calcMode.add(PRO);
		calcMode.add(quit);
		help.add(about);
		help.add(version);
		jmb.add(calcMode);
		jmb.add(help);
		return jmb;
	}

	public void evaluate() {
		if (expression.empty() || operator.empty()) {
			label.setText("Syntax Error");
			operator.pop();
			calculated = false;
			return;
		}
		double secondOperand = expression.pop();
		if ((expression.empty() || operator.empty())
				&& (operator.peek() == '^' || operator.peek() == '+' || operator.peek() == '/'
						|| operator.peek() == '\u00F7' || operator.peek() == '*' || operator.peek() == 'X'
						|| operator.peek() == '\u00D7' || operator.peek() == '-' || operator.peek() == '\u2212')) {
			label.setText("Syntax Error");
			operator.pop();
			calculated = false;
			return;
		}
		calculated = true;
		switch (operator.pop()) {
		case 's':
			expression.push(type ? Math.sin(Math.toRadians(secondOperand)) : Math.sin(secondOperand));
			return;
		case 'S':
			expression.push(type ? Math.toDegrees(Math.asin(secondOperand)) : Math.asin(secondOperand));
			return;
		case 'c':
			expression.push(type ? Math.cos(Math.toRadians(secondOperand)) : Math.cos(secondOperand));
			return;
		case 'C':
			expression.push(type ? Math.toDegrees(Math.acos(secondOperand)) : Math.acos(secondOperand));
			return;
		case 't':
			expression.push(type ? Math.tan(Math.toRadians(secondOperand)) : Math.tan(secondOperand));
			return;
		case 'T':
			expression.push(type ? Math.toDegrees(Math.atan(secondOperand)) : Math.atan(secondOperand));
			return;
		case '$':
		case '\u2211':
			expression.push((Double) summation((int) secondOperand));
			return;
		case '!':
			expression.push((Double) factorial((int) secondOperand));
			return;
		case 'E': // Power of 10 Exponent
		case '\u1D07': // Power of 10 Exponent
			expression.push((Double) Math.pow(10, secondOperand));
			return;
		case 'V': // Square Root
		case '\u221A': // Square Root
			expression.push((Double) Math.sqrt(secondOperand));
			return;
		case '\u00B2': // Square
			expression.push((Double) secondOperand * secondOperand);
			return;
		case '^': // Power
			expression.push((Double) Math.pow(expression.pop(), secondOperand));
			return;
		case '/': // Division
		case '\u00F7': // Division
			expression.push((Double) expression.pop() / secondOperand);
			return;
		case '*': // Multiplication
		case 'X': // Multiplication
		case '\u00D7': // Multiplication
			expression.push((Double) expression.pop() * secondOperand);
			return;
		case '-': // Subtraction
		case '\u2212': // Subtraction
			expression.push((Double) expression.pop() - secondOperand);
			return;
		case '+': // Addition
			expression.push((Double) expression.pop() + secondOperand);
			return;
		default:
			return;
		}
	}

	public int precedence(char operator) {
		switch (operator) {
		case 's': // sin()
		case 'S': // sin-1()
		case 'c': // cos()
		case 'C': // cos-1()
		case 't': // tan()
		case 'T': // tan-1()
			return 5;
		case '^': // Power
		case '\u00B2': // Square ^2
		case 'V': // Square Root
		case 'E': // Power of 10 Exponent
		case '\u1D07': // Power of 10 Exponent
		case '\u221A': // Square Root
		case '!': // Factorial
		case '$': // Summation
		case '\u2211': // Summation
			return 4;
		case '/': // Divide
		case '\u00F7': // Divide
		case '*': // Multiplication
		case 'X': // Multiplication
		case '\u00D7': // Multiplication
			return 3;
		case '-': // Subtract
		case '\u2212': // Subtract
		case '+':
			return 2;
		default:
			return 1;
		}
	}

	public static void main(String[] args) {
		new Calculator();
	}

	public Double factorial(int number) {
		if (number <= 1) {
			return 1.0;
		}
		return number * factorial(number - 1);
	}

	public Double summation(int number) {
		if (number == 1) {
			return 1.0;
		} else if (number == 0) {
			return 0.0;
		}
		return number + summation(number - 1);
	}

	public void infoToggle() {
		label.setVisible(infoShow);
		about.setVisible(infoShow = !infoShow);
	}

	public void admin() {
		label.setFont(new Font("Old English Text MT", Font.BOLD, 50));
		label.setText("Creater. RR");
		calculated = false;
	}

	public boolean getValidNumber(int i) {
		switch (opType) {
		case 16:
			if (Character.isDigit(exp[i])
					|| (exp[i] >= 'A' && exp[i] <= 'F' && !(exp[i + 1] > 'F' && exp[i + 1] <= 'Z')))
				return true;
			else
				return false;
		case 10:
		case 8:
		case 2:
			if (Character.isDigit(exp[i]) || exp[i] == '.')
				return true;
			else
				return false;
		default:
			return false;
		}
	}

	public void calculate() {
		exp = (textField.getText() + ")").toCharArray();
		for (int i = 0; i < exp.length; ++i) {
			if (exp[i] >= 'a' && exp[i] <= 'z') {
				exp[i] -= 32;
			}
		}
		operator.push('(');
		for (int i = 0; i < exp.length;) {
			if (getValidNumber(i)) {
				String x = new String("");
				do {
					x += exp[i++];
				} while (getValidNumber(i));
				expression.push(Double.parseDouble(x));
			} else {
				switch (exp[i]) {
				case '(':
					if (i != 0)
						if (Character.isDigit(exp[i - 1]) || exp[i - 1] == ')')
							operator.push('*');
					operator.push(exp[i]);
					break;
				case ')':
					while (operator.peek() != '(') {
						evaluate();
					}
					operator.pop();
					break;
				/////////////////////////////// Administrator Panel
				/////////////////////////////// /////////////////////////////////////////////
				case 'R':
					if (exp[i + 1] == 'I' || exp[i + 1] == 'A' || exp[i + 1] == 'E') {
						if (exp[i + 2] == 'Z' || exp[i + 2] == 'J' || exp[i + 2] == 'X') {
							if (exp[i + 3] == 'Z' || exp[i + 3] == 'J' || exp[i + 3] == 'A' || exp[i + 3] == 'W') {
								if (exp[i + 4] == 'U' || exp[i + 4] == 'O' || exp[i + 4] == 'A') {
									if (exp[i + 5] == 'N') {
										i += 5;
										admin();
									}
									i += 4;
									admin();
								}
								i += 3;
								admin();
							}
							i += 2;
							admin();
						}
					}
					break;
				case 'B':
					if (exp[i + 1] == 'I' && exp[i + 2] == 'R' && exp[i + 3] == 'J' && exp[i + 4] == 'U') {
						admin();
						i += 4;
					}
					break;
				case 'Z':
					if (exp[i + 1] == 'E' && exp[i + 2] == 'D') {
						admin();
						i += 2;
					}
					break;
				case 'G':
					if (exp[i + 1] == 'O' && exp[i + 2] == 'L' && exp[i + 3] == 'D' && exp[i + 4] == 'Y') {
						admin();
						i += 4;
					}
					break;
				case 'L':
					if (exp[i + 1] == 'E' && exp[i + 2] == 'E' && exp[i + 3] == 'C' && exp[i + 4] == 'H'
							&& exp[i + 5] == 'U') {
						admin();
						i += 5;
					}
					break;
				////////////////////////////////////////////////////////////////////////////////////////////////
				case 'P':
				case '\u03C0':
					if (exp[i + 1] == 'I')
						i++;
					if (i > 1) {
						if (Character.isDigit(exp[i - 1])
								|| (Character.isDigit(exp[i - 2]) && (exp[i - 1] == 'p' || exp[i - 1] == 'P'))) {
							operator.push('*');
						}
					}
					expression.push(Math.PI);
					calculated = true;
					break;
				case 'S':
					if (exp[i + 1] == 'I' && exp[i + 2] == 'N') {
						if (exp[i + 3] == '\u207B' && exp[i + 4] == '\u00B9') {
							i += 4;
							operator.push('S');
							break;
						}
						i += 2;
						operator.push('s');
					}
					break;
				case 'C':
					if (exp[i + 1] == 'O' && exp[i + 2] == 'S') {
						if (exp[i + 3] == '\u207B' && exp[i + 4] == '\u00B9') {
							i += 4;
							operator.push('C');
							break;
						}
						i += 2;
						operator.push('c');
					}
					break;
				case 'T':
					if (exp[i + 1] == 'A' && exp[i + 2] == 'N') {
						if (exp[i + 3] == '\u207B' && exp[i + 4] == '\u00B9') {
							i += 4;
							operator.push('T');
							break;
						}
						i += 2;
						operator.push('t');
					}
					break;
				case 'V': // Square Root
				case '\u221A': // Square Root
				case 'E': // Power of 10 Exponent
				case '\u1D07': // Power of 10 Exponent
					if (i != 0)
						if (Character.isDigit(exp[i - 1]) || exp[i - 1] == ')')
							operator.push('*');
					operator.push(exp[i]);
					break;
				case '-': // Subtraction
				case '\u2212': // Subtraction
					if (i == 0) {
						expression.push(-1.0);
						operator.push('*');
						break;
					} else {
						if (!(Character.isDigit(exp[i - 1]))) {
							expression.push(-1.0);
							operator.push('*');
							break;
						}
					}
				case '^': // Power
				case '/': // Division
				case '*': // Multiplication
				case 'X': // Multiplication
				case '+': // Addition
				case '\u00D7': // Multiplication
				case '\u00B2': // Square
				case '\u00F7': // Division
				case '!': // Factorial
				case '$': // Summation
				case '\u2211': // Summation
					if (precedence(operator.peek()) < precedence(exp[i])) {
						operator.push(exp[i]);
					} else {
						do {
							evaluate();
						} while (precedence(operator.peek()) >= precedence(exp[i]));
						operator.push(exp[i]);
					}
					break;
				}
				i++;
			}
		}
		if (calculated) {
			label.setFont(new Font("Calibri", 0, 48));
			label.setText(String.valueOf(expression.pop()));
			calculated = false;
		}
	}

	//////////////////////////////////////// Action Classes
	//////////////////////////////////////// ///////////////////////////////////////////////////

	class D_R_G_Action implements ActionListener { // Degree or Radian Toggle Key
		public void actionPerformed(ActionEvent e) {
			SCIButton[0][3].setText(type ? "Rad" : "Deg");
			type = !type;
		}
	}

	class PrintAction implements ActionListener { // Printable Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + ((JButton) e.getSource()).getText());
		}
	}

	class ShiftAction implements ActionListener { // Shift Key
		public void actionPerformed(ActionEvent e) {
			String temp = new String("");
			inverse = !inverse;
			temp = inverse ? "\u207B\u00B9" : "";
			SCIButton[0][0].setText("sin" + temp);
			SCIButton[0][1].setText("cos" + temp);
			SCIButton[0][2].setText("tan" + temp);
			SCIButton[0][4].setText(inverse ? "\u2193" : "\u2191");
		}
	}

	class SquareAction implements ActionListener { // Square Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + "\u00B2");
		}
	}

	class InfoAction implements ActionListener { // Info Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			infoToggle();
		}
	}

	class BackSpaceAction implements ActionListener { // Backspace Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			if (textField.getText().length() > 0)
				textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
		}
	}

	class ResetAction implements ActionListener { // Reset Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText("");
			label.setText("0");
		}
	}

	class AnswerAction implements ActionListener { // Answer getting Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + label.getText());
		}
	}

	class EqualAction implements ActionListener { // Equal Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			calculate();
		}
	}

	class PowerAction implements ActionListener { // Power Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + "^");
		}
	}

	class FactorialAction implements ActionListener { // Factorial Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + "!");
		}
	}

	class SummationAction implements ActionListener { // Summation Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + "\u2211");
		}
	}

	class BinaryAction implements ActionListener { // Summation Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			opType = 2;
			PROButton[2][0].setEnabled(false);
			PROButton[2][1].setEnabled(false);
			PROButton[2][2].setEnabled(false);
			PROButton[3][0].setEnabled(false);
			PROButton[3][1].setEnabled(false);
			PROButton[3][2].setEnabled(false);
			PROButton[4][0].setEnabled(false);
			PROButton[4][1].setEnabled(false);
			PROButton[4][2].setEnabled(false);
			PROButton[5][0].setEnabled(false);
			PROButton[5][1].setEnabled(false);
			PROButton[5][2].setEnabled(false);
			PROButton[6][1].setEnabled(false);
			PROButton[6][2].setEnabled(false);
			PROButton[7][1].setEnabled(false);
		}
	}

	class DecimalAction implements ActionListener { // Summation Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			opType = 10;
			PROButton[2][0].setEnabled(false);
			PROButton[2][1].setEnabled(false);
			PROButton[2][2].setEnabled(false);
			PROButton[3][0].setEnabled(false);
			PROButton[3][1].setEnabled(false);
			PROButton[3][2].setEnabled(false);
			PROButton[4][0].setEnabled(true);
			PROButton[4][1].setEnabled(true);
			PROButton[4][2].setEnabled(true);
			PROButton[5][0].setEnabled(true);
			PROButton[5][1].setEnabled(true);
			PROButton[5][2].setEnabled(true);
			PROButton[6][1].setEnabled(true);
			PROButton[6][2].setEnabled(true);
			PROButton[7][1].setEnabled(false);
		}
	}

	class OctalAction implements ActionListener { // Summation Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			opType = 8;
			PROButton[2][0].setEnabled(false);
			PROButton[2][1].setEnabled(false);
			PROButton[2][2].setEnabled(false);
			PROButton[3][0].setEnabled(false);
			PROButton[3][1].setEnabled(false);
			PROButton[3][2].setEnabled(false);
			PROButton[4][1].setEnabled(false);
			PROButton[4][2].setEnabled(false);
			PROButton[4][0].setEnabled(true);
			PROButton[5][0].setEnabled(true);
			PROButton[5][1].setEnabled(true);
			PROButton[5][2].setEnabled(true);
			PROButton[6][1].setEnabled(true);
			PROButton[6][2].setEnabled(true);
			PROButton[7][1].setEnabled(false);
		}
	}

	class HexadecimalAction implements ActionListener { // Summation Key Clicked Event
		public void actionPerformed(ActionEvent e) {
			opType = 16;
			PROButton[2][0].setEnabled(true);
			PROButton[2][1].setEnabled(true);
			PROButton[2][2].setEnabled(true);
			PROButton[3][0].setEnabled(true);
			PROButton[3][1].setEnabled(true);
			PROButton[3][2].setEnabled(true);
			PROButton[4][0].setEnabled(true);
			PROButton[4][1].setEnabled(true);
			PROButton[4][2].setEnabled(true);
			PROButton[5][0].setEnabled(true);
			PROButton[5][1].setEnabled(true);
			PROButton[5][2].setEnabled(true);
			PROButton[6][1].setEnabled(true);
			PROButton[6][2].setEnabled(true);
			PROButton[7][1].setEnabled(false);
		}
	}

	class PROShitfAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (Sh_Ro = !Sh_Ro) {
				PROButton[1][1].setText("RoL");
				PROButton[1][2].setText("RoR");
			} else {
				PROButton[1][1].setText("Lsh");
				PROButton[1][2].setText("Rsh");
			}
			PROButton[0][4].setText(Sh_Ro ? "\u2193" : "\u2191");
		}
	}

	class STDAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			card.first(panelBottom);
			frame.pack();
		}
	}

	class SCIAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			card.first(panelBottom);
			card.next(panelBottom);
			frame.pack();
		}
	}

	class PROAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			card.last(panelBottom);
			card.last(panelBottom);
			frame.pack();
			PROButton[7][1].setEnabled(false);
		}
	}

	class QuitAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

}
