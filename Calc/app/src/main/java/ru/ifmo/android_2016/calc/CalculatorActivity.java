package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;

import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by alexey.nikitin on 13.09.16.
 */

public class CalculatorActivity extends Activity {

    private StringBuilder strBuilder = new StringBuilder();
    private Parser parser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        parser = new Parser((TextView)findViewById(R.id.Debug));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        TextView expressionText = (TextView)findViewById(R.id.ExpressionText);
        expressionText.setText(savedState.getString("Expression text"));
        if (savedState.getBoolean("Created")) {
            parser.toRestoreInitialState(savedState.getIntegerArrayList("Coded expression"), new BigDecimal(savedState.getString("Last result")));
        }
        else {
            parser.toRestoreExp(savedState.getIntegerArrayList("Coded expression"));
        }
        strBuilder.append(savedState.getString("Expression text"));
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        TextView expressionText = (TextView)findViewById(R.id.ExpressionText);
        saveInstanceState.putString("Expression text", expressionText.getText().toString());
        Pair<ArrayList<Integer>, BigDecimal> tmp = parser.toSaveExp();
        saveInstanceState.putIntegerArrayList("Coded expression", tmp.first);
        if (tmp.second != null) {
            saveInstanceState.putBoolean("Created", true);
            saveInstanceState.putString("Last result", tmp.second.toString());
        }
        else {
            saveInstanceState.putBoolean("Created", false);
        }
        super.onSaveInstanceState(saveInstanceState);
    }


    public void ClearAll(View view) {
        parser.clear();
        TextView text = (TextView) findViewById(R.id.ExpressionText);
        strBuilder.setLength(0);
        text.setText("");
    }

    public void AddSymbol(View view) {
        TextView text = (TextView)findViewById(R.id.ExpressionText);

        switch(view.getId()) {
            case R.id.LeftParenthesis:
                parser.add(18);
                strBuilder.append('(');
                break;
            case R.id.RightParenthesis:
                parser.add(19);
                strBuilder.append(')');
                break;
            case R.id.Add:
                parser.add(11);
                strBuilder.append('+');
                break;
            case R.id.Subtract:
                parser.add(12);
                strBuilder.append('-');
                break;
            case R.id.Modulo:
                parser.add(15);
                strBuilder.append('%');
                break;
            case R.id.Divide:
                parser.add(14);
                strBuilder.append('/');
                break;
            case R.id.Multiply:
                parser.add(13);
                strBuilder.append('*');
                break;
            case R.id.Zero:
                parser.add(0);
                strBuilder.append('0');
                break;
            case R.id.One:
                parser.add(1);
                strBuilder.append('1');
                break;
            case R.id.Two:
                parser.add(2);
                strBuilder.append('2');
                break;
            case R.id.Three:
                parser.add(3);
                strBuilder.append('3');
                break;
            case R.id.Four:
                parser.add(4);
                strBuilder.append('4');
                break;
            case R.id.Five:
                parser.add(5);
                strBuilder.append('5');
                break;
            case R.id.Six:
                parser.add(6);
                strBuilder.append('6');
                break;
            case R.id.Seven:
                parser.add(7);
                strBuilder.append('7');
                break;
            case R.id.Eight:
                parser.add(8);
                strBuilder.append('8');
                break;
            case R.id.Nine:
                parser.add(9);
                strBuilder.append('9');
                break;
            case R.id.Point:
                parser.add(17);
                strBuilder.append('.');
                break;
        }
        text.setText(strBuilder.toString());
    }

    public void Evaluate(View view) {
        TextView text = (TextView)findViewById(R.id.ExpressionText);
        boolean s = parser.checkOnCorrectness();
        if (s) {
            BigDecimal res = parser.calculate();
            text.setText(res.toPlainString());
            strBuilder.setLength(0);
            strBuilder.append(res.toPlainString());
            parser.clear();
            parser.addNumber(res);
        }
    }



    public void ClearLast(View view) {
        if (strBuilder.length() == 0) {
            return;
        }
        TextView text = (TextView)findViewById(R.id.ExpressionText);
        strBuilder.deleteCharAt(strBuilder.length() - 1);
        text.setText(strBuilder.toString());
        parser.clearLast();
    }
}

