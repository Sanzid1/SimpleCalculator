package com.example.simplecalculator; // Make sure this matches your package name

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;
    private String currentNumber = "";
    private double firstOperand = 0;
    private String pendingOperation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure that activity_main.xml exists in res/layout
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        // Set listeners for number buttons
        int[] numberButtonIDs = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonDecimal
        };

        for (int id : numberButtonIDs) {
            findViewById(id).setOnClickListener(this);
        }

        // Set listeners for operation buttons
        int[] operationButtonIDs = {
                R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply,
                R.id.buttonDivide, R.id.buttonEquals, R.id.buttonClear
        };

        for (int id : operationButtonIDs) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Check for number button presses
        if (id == R.id.button0 || id == R.id.button1 || id == R.id.button2 ||
                id == R.id.button3 || id == R.id.button4 || id == R.id.button5 ||
                id == R.id.button6 || id == R.id.button7 || id == R.id.button8 ||
                id == R.id.button9) {

            Button numberButton = (Button) view;
            String input = numberButton.getText().toString();
            
            currentNumber += input;
            tvResult.setText(currentNumber);

        } else if (id == R.id.buttonDecimal) {
            // Handle decimal point separately
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) {
                    currentNumber = "0.";
                } else {
                    currentNumber += ".";
                }
                tvResult.setText(currentNumber);
            }
        } else if (id == R.id.buttonPlus || id == R.id.buttonMinus ||
                id == R.id.buttonMultiply || id == R.id.buttonDivide) {

            if (!currentNumber.isEmpty()) {
                firstOperand = Double.parseDouble(currentNumber);
                currentNumber = "";
            }
            Button opButton = (Button) view;
            pendingOperation = opButton.getText().toString();
            tvResult.setText(pendingOperation);

        } else if (id == R.id.buttonEquals) {

            if (!currentNumber.isEmpty() && !pendingOperation.isEmpty()) {
                double secondOperand = Double.parseDouble(currentNumber);
                double result = 0;

                switch (pendingOperation) {
                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;
                    case "*":
                        result = firstOperand * secondOperand;
                        break;
                    case "/":
                        if (secondOperand != 0) {
                            result = firstOperand / secondOperand;
                        } else {
                            tvResult.setText(getString(R.string.error));
                            resetCalculator();
                            return;
                        }
                        break;
                }
                
                // Format the result to remove unnecessary decimal zeros
                String formattedResult;
                if (result == (long) result) {
                    formattedResult = String.format(java.util.Locale.US, "%d", (long) result);
                } else {
                    formattedResult = String.valueOf(result);
                }
                
                tvResult.setText(formattedResult);
                // Allow for chain calculations
                firstOperand = result;
                currentNumber = "";
                pendingOperation = "";
            }

        } else if (id == R.id.buttonClear) {

            resetCalculator();
            tvResult.setText(getString(R.string.initial_value));
        }
    }

    // Helper method to reset the calculator's state.
    private void resetCalculator() {
        currentNumber = "";
        pendingOperation = "";
        firstOperand = 0;
    }
}