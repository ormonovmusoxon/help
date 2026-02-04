package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private StringBuilder currentInput = new StringBuilder();
    private double storedValue = 0.0;
    private String pendingOperator = null;
    private boolean resetOnNextDigit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        setDigitClickListener(R.id.btn0, "0");
        setDigitClickListener(R.id.btn1, "1");
        setDigitClickListener(R.id.btn2, "2");
        setDigitClickListener(R.id.btn3, "3");
        setDigitClickListener(R.id.btn4, "4");
        setDigitClickListener(R.id.btn5, "5");
        setDigitClickListener(R.id.btn6, "6");
        setDigitClickListener(R.id.btn7, "7");
        setDigitClickListener(R.id.btn8, "8");
        setDigitClickListener(R.id.btn9, "9");
        setDigitClickListener(R.id.btnDot, ".");

        setOperatorClickListener(R.id.btnAdd, "+");
        setOperatorClickListener(R.id.btnSubtract, "-");
        setOperatorClickListener(R.id.btnMultiply, "×");
        setOperatorClickListener(R.id.btnDivide, "÷");

        Button clear = findViewById(R.id.btnClear);
        clear.setOnClickListener(v -> clearAll());

        Button delete = findViewById(R.id.btnDelete);
        delete.setOnClickListener(v -> deleteLast());

        Button equals = findViewById(R.id.btnEquals);
        equals.setOnClickListener(v -> evaluate());

        updateDisplay("0");
    }

    private void setDigitClickListener(int buttonId, String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> appendDigit(value));
    }

    private void setOperatorClickListener(int buttonId, String operator) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> selectOperator(operator));
    }

    private void appendDigit(String digit) {
        if (resetOnNextDigit) {
            currentInput.setLength(0);
            resetOnNextDigit = false;
        }

        if (".".equals(digit) && currentInput.indexOf(".") != -1) {
            return;
        }

        if (currentInput.length() == 1 && currentInput.charAt(0) == '0' && !".".equals(digit)) {
            currentInput.setLength(0);
        }

        currentInput.append(digit);
        updateDisplay(currentInput.toString());
    }

    private void selectOperator(String operator) {
        if (currentInput.length() == 0 && pendingOperator == null) {
            storedValue = 0.0;
        } else if (currentInput.length() > 0) {
            if (pendingOperator != null) {
                storedValue = applyOperation(storedValue, parseInput(), pendingOperator);
            } else {
                storedValue = parseInput();
            }
        }

        pendingOperator = operator;
        resetOnNextDigit = true;
        updateDisplay(formatNumber(storedValue));
    }

    private void evaluate() {
        if (pendingOperator == null || currentInput.length() == 0) {
            return;
        }

        double result = applyOperation(storedValue, parseInput(), pendingOperator);
        pendingOperator = null;
        storedValue = result;
        currentInput.setLength(0);
        currentInput.append(formatNumber(result));
        resetOnNextDigit = true;
        updateDisplay(formatNumber(result));
    }

    private double parseInput() {
        try {
            return Double.parseDouble(currentInput.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double applyOperation(double left, double right, String operator) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "×":
                return left * right;
            case "÷":
                if (right == 0) {
                    return 0.0;
                }
                return left / right;
            default:
                return right;
        }
    }

    private void clearAll() {
        storedValue = 0.0;
        pendingOperator = null;
        currentInput.setLength(0);
        resetOnNextDigit = false;
        updateDisplay("0");
    }

    private void deleteLast() {
        if (resetOnNextDigit) {
            return;
        }
        int length = currentInput.length();
        if (length > 0) {
            currentInput.deleteCharAt(length - 1);
        }
        if (currentInput.length() == 0) {
            updateDisplay("0");
        } else {
            updateDisplay(currentInput.toString());
        }
    }

    private void updateDisplay(String value) {
        display.setText(value);
    }

    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
