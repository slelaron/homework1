package ru.ifmo.android_2016.calc;

import android.util.Pair;
import android.widget.TextView;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by nikita on 27.09.16.
 */

public class Parser {
	TextView text;
	ArrayList <Integer> exp;
	int pointer;
	boolean calculatedResult;
	BigDecimal result;
	private boolean checkOnNumber() {
		return (exp.get(pointer) >= 0 && exp.get(pointer) <= 9);
	}
	private BigDecimal getNumber() throws Exception {
		StringBuilder builder = new StringBuilder();
		boolean was = false;
		while (exp.get(pointer) >= 0 && exp.get(pointer) <= 9 || exp.get(pointer) == 17) {
			if (exp.get(pointer) == 17 && !was) {
				builder.append('.');
				was = true;
			} else if (exp.get(pointer) == 17 && was) {
				throw new Exception("Wrong sequence of characters");
			} else {
				builder.append(exp.get(pointer));
			}
			pointer++;
		}
		return new BigDecimal(builder.toString());
	}
	private BigDecimal forthAct() throws Exception {
		if (exp.get(pointer) == 12) {
			pointer++;
			return forthAct().negate();
		}
		if (checkOnNumber()) {
			return getNumber();
		}
		if (exp.get(pointer) == 18) {
			pointer++;
			BigDecimal result = firstAct();
			if (exp.get(pointer) == -1) {
				return result;
			} else if (exp.get(pointer) != 19) {
				throw new Exception("No right parenthesis");
			} else {
				pointer++;
			}
			return result;
		}
		throw new Exception("No number in the last position");
	}
	private BigDecimal thirdAct() throws Exception {
		if (exp.get(pointer) == 16) {
			pointer++;
			BigDecimal res = firstAct();
			if (res.compareTo(BigDecimal.ZERO) >= 0) {
				BigDecimal l = new BigDecimal(0);
				BigDecimal acc = new BigDecimal(0.00000001);
				BigDecimal r = res;
				BigDecimal two = new BigDecimal(2);
				while (l.subtract(r).compareTo(acc) > 0) {
					BigDecimal m = l.add(r).divide(two, 9);
					if (m.multiply(m).compareTo(res) > 0) {
						r = m;
					} else {
						l = m;
					}
				}
				return l;
			} else {
				throw new Exception("Negative argument");
			}
		}
		return forthAct();
	}
	private BigDecimal secondAct() throws Exception {
		BigDecimal that = thirdAct();
		while (true) {
			if (exp.get(pointer) == 13) {
				pointer++;
				that = that.multiply(thirdAct());
			} else if (exp.get(pointer) == 14) {
				pointer++;
				BigDecimal res = thirdAct();
				if (res.equals(BigDecimal.ZERO)) {
					throw new Exception("Division by zero");
				} else {
					that = that.divide(res, 5, BigDecimal.ROUND_HALF_UP);
				}
			} else if (exp.get(pointer) == 15) {
				pointer++;
				BigDecimal res = thirdAct();
				if (res.equals(BigDecimal.ZERO)) {
					throw new Exception("Division by zero");
				}
				that = that.subtract(that.divideToIntegralValue(res).multiply(res));
			} else {
				return that;
			}
		}
	}
	private BigDecimal firstAct() throws Exception {
		BigDecimal that = secondAct();
		while (true) {
			if (exp.get(pointer) == 11) {
				pointer++;
				that = that.add(secondAct());
			} else if (exp.get(pointer) == 12) {
				pointer++;
				that = that.subtract(secondAct());
			} else {
				return that;
			}
		}
	}

	private void Print() {
		if (exp.size() == 1) {
			text.setText("");
		} else {
			if (!checkOnCorrectness())
				return;
			text.setText(result.toPlainString());
		}
	}

	Parser(TextView tmp) {
		exp = new ArrayList<>();
		pointer = 0;
		exp.add(-1);
		calculatedResult = false;
		text = tmp;
	}
	public void add(int operation) {
		exp.set(exp.size() - 1, operation);
		exp.add(-1);
		calculatedResult = false;
		Print();
	}

	public void addNumber(BigDecimal tmp) {
		exp.clear();
		String s = tmp.toPlainString();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
				exp.add(s.charAt(i) - '0');
			}
			if (s.charAt(i) == '.') {
				exp.add(17);
			}
		}
		exp.add(-1);
		Print();
	}

	public void clearLast() {
		if (exp.size() > 1) {
			exp.set(exp.size() - 2, -1);
			exp.remove(exp.size() - 1);
			calculatedResult = false;
			Print();
		}
	}

	public void clear() {
		exp.clear();
		exp.add(-1);
		result = BigDecimal.ZERO;
		calculatedResult = false;
		pointer = 0;
		Print();
	}

	public boolean checkOnCorrectness() {
		if (exp.size() == 1) {
			result = BigDecimal.ZERO;
			calculatedResult = false;
			return false;
		}
		if (calculatedResult) {
			return true;
		}
		try {
			pointer = 0;
			result = firstAct();
			calculatedResult = true;
			return true;
		} catch (Exception exp) {
			text.setText(exp.getMessage());
			return false;
		}
	}

	public Pair <ArrayList<Integer>, BigDecimal> toSaveExp() {
		return new Pair <>(exp, result);
	}

	public void toRestoreInitialState(ArrayList <Integer> tmp, BigDecimal lastResult) {
		exp = tmp;
		result = lastResult;
		Print();
	}

	public void toRestoreExp(ArrayList <Integer> tmp) {
		exp = tmp;
		Print();
	}

	public BigDecimal calculate() {
		return result;
	}
}

