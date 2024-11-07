package com.example.calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView display;
    int values = 0;
    int functions = 0;
    ArrayList <String> allInput = new ArrayList<>();
    Button one, two, three, four, five, six, seven, eight, nine, zero, enter, add, multiply, divide, subtract, clear, decimal, negative, FV, MI, P, evaluate;
    String currentInput = "";
    int fvStep = 0;
    int miStep = 0;
    int pStep = 0;
    boolean isFVActive = false;
    boolean isMIActive = false;
    boolean ispActive = false;
    ArrayList<Double> fvParams = new ArrayList<>();
    ArrayList <Double> miParams = new ArrayList<>();
    ArrayList <Double> pParams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setBackgroundColor(Color.BLACK);
        one = findViewById(R.id.one);
        one.setBackgroundColor(Color.WHITE);
        two = findViewById(R.id.two);
        two.setBackgroundColor(Color.WHITE);
        three = findViewById(R.id.three);
        three.setBackgroundColor(Color.WHITE);
        four = findViewById(R.id.four);
        four.setBackgroundColor(Color.WHITE);
        five = findViewById(R.id.five);
        five.setBackgroundColor(Color.WHITE);
        six = findViewById(R.id.six);
        six.setBackgroundColor(Color.WHITE);
        seven = findViewById(R.id.seven);
        seven.setBackgroundColor(Color.WHITE);
        eight = findViewById(R.id.eight);
        eight.setBackgroundColor(Color.WHITE);
        nine = findViewById(R.id.nine);
        nine.setBackgroundColor(Color.WHITE);
        zero = findViewById(R.id.zero);
        zero.setBackgroundColor(Color.WHITE);
        enter = findViewById(R.id.enter);
        enter.setBackgroundColor(Color.GRAY);
        add = findViewById(R.id.add);
        add.setBackgroundColor(Color.WHITE);
        subtract = findViewById(R.id.subtract);
        subtract.setBackgroundColor(Color.WHITE);
        multiply = findViewById(R.id.multiply);
        multiply.setBackgroundColor(Color.WHITE);
        divide = findViewById(R.id.divide);
        divide.setBackgroundColor(Color.WHITE);
        clear = findViewById(R.id.clear);
        clear.setBackgroundColor(Color.GRAY);
        decimal = findViewById(R.id.decimal);
        decimal.setBackgroundColor(Color.WHITE);
        negative = findViewById(R.id.negative);
        negative.setBackgroundColor(Color.WHITE);
        FV = findViewById(R.id.FV);
        FV.setBackgroundColor(Color.GRAY);
        MI = findViewById(R.id.MI);
        MI.setBackgroundColor(Color.GRAY);
        P = findViewById(R.id.P);
        P.setBackgroundColor(Color.GRAY);
        evaluate = findViewById(R.id.evaluate);
        evaluate.setBackgroundColor(Color.GRAY);
        display = findViewById(R.id.display);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFVActive){
                    handleFVInput();
                }else if (isMIActive){
                    handleMIInput();
                }else if (ispActive){
                    handlePInput();
                } else if (!currentInput.isEmpty()){
                    String previous = (String) textView.getText();
                    textView.setText(previous);
                    allInput.add(currentInput);
                    try{
                        double value = Double.parseDouble(currentInput);
                        values++;
                    }catch (NumberFormatException e){
                        if(isValidFunction(currentInput))
                            functions++;
                        else
                            textView.setText("Invalid input.");
                    }
                    currentInput = "";
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentInput = "";
                textView.setText(currentInput);
                allInput.clear();
                values = 0;
                functions = 0;
                fvParams.clear();
                miParams.clear();
                pParams.clear();
                add.setClickable(true);
                subtract.setClickable(true);
                multiply.setClickable(true);
                divide.setClickable(true);
                MI.setClickable(true);
                P.setClickable(true);
                negative.setClickable(true);
                FV.setClickable(true);
            }
        });
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display.setText(allInput.toString());
                if(isValidRPNOrder()){
                    evaluateMath();
                }
            }
        });
        FV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFVActive = true;
                add.setClickable(false);
                subtract.setClickable(false);
                multiply.setClickable(false);
                divide.setClickable(false);
                MI.setClickable(false);
                P.setClickable(false);
                negative.setClickable(false);
                fvStep = 1;
                currentInput = "";
                fvParams.clear(); // Clear previous FV parameters
                textView.setText("Please enter the present value (PV):");
            }
        });
        MI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMIActive = true;
                add.setClickable(false);
                subtract.setClickable(false);
                multiply.setClickable(false);
                divide.setClickable(false);
                FV.setClickable(false);
                P.setClickable(false);
                negative.setClickable(false);
                miStep = 1;
                miParams.clear();
                currentInput = "";
                textView.setText("Please enter the interest rate value (r):");
            }
        });
        P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispActive = true;
                add.setClickable(false);
                subtract.setClickable(false);
                multiply.setClickable(false);
                divide.setClickable(false);
                MI.setClickable(false);
                FV.setClickable(false);
                negative.setClickable(false);
                pStep = 1;
                pParams.clear();
                currentInput = "";
                textView.setText("Please enter your Loan Amount (L):");
            }
        });
    }
    public void onNumberClick(View view) {
        Button button = (Button) view;
        currentInput += button.getText().toString();
        if(currentInput.equals("(-)")){
            currentInput = "-";
        }
        textView.setText(currentInput);
    }
    public void onMathFunctionClick(View view) {
        Button button = (Button) view;
        currentInput += button.getText().toString();
        textView.setText(currentInput);
    }
    public boolean isValidFunction(String input){
        return input.equals("+") || input.equals("-") || input.equals("x") || input.equals("/")
                || input.equals("FV") || input.equals("MI") || input.equals("P");
    }
    public boolean isValidRPNOrder(){
        return (values == functions+1);
    }
    public double evaluateMath(){
        ArrayList<Double> results = new ArrayList<>();
        try{
            for (String input : allInput){
                try{
                    double val = Double.parseDouble(input);
                    results.add(val);
                }catch (NumberFormatException e){
                    if(isValidFunction(input)){
                        if (results.size() < 2) {
                            textView.setText("Error: Not enough values.");
                            return 0;
                        }
                        double b = results.remove(results.size() - 1);
                        double a = results.remove(results.size() - 1);
                        double result = 0;

                        switch(input){
                            case "+":
                                result = a + b;
                                break;
                            case "-":
                                result = a-b;
                                break;
                            case "x":
                                result = a * b;
                                break;
                            case "/":
                                if(b==0){
                                    textView.setText("Error: can not divide by 0");
                                    return 0;
                                }
                                result = a / b;
                                break;
                        }
                        results.add(result);
                    }
                    else{
                        textView.setText("Invalid input");
                        return 0;
                    }
                }
            }
            if(results.size()==1){
                double finalRes = results.get(0);
                textView.setText(String.valueOf(finalRes));
                return finalRes;
            }else{
                textView.setText("Error: Invalid RPN");
                return 0;
            }
        } catch (Exception e){
            textView.setText("Erro: Invalid input/calculation");
            return 0;
        }
    }
    private void handleFVInput() {
        if (!currentInput.isEmpty()) {
            try {
                double inputVal = Double.parseDouble(currentInput);
                fvParams.add(inputVal);
                currentInput = "";

                if (fvParams.size() == 1) {
                    textView.setText("Please enter the interest rate (e.g., 5 for 5%):");
                } else if (fvParams.size() == 2) {
                    textView.setText("Please enter the number of years:");
                } else if (fvParams.size() == 3) {
                    double pv = fvParams.get(0);
                    double r = fvParams.get(1) / 100;
                    double n = fvParams.get(2);
                    double fvResult = pv * Math.pow((1 + r), n);
                    textView.setText("Future Value (FV) = " + fvResult);
                    isFVActive = false;
                }
            } catch (NumberFormatException e) {
                textView.setText("Invalid number, please re-enter.");
            }
        }
    }
    private void handleMIInput() {
        if (!currentInput.isEmpty()) {
            try {
                double inputVal = Double.parseDouble(currentInput);
                miParams.add(inputVal);
                currentInput = "";

                if (miParams.size() == 1) {
                    textView.setText("Please enter the loan balance (lb):");
                } else if (miParams.size() == 2) {
                    double lb = miParams.get(1);
                    double r = miParams.get(0) / 100;
                    double miResult = r/12 * lb;

                    textView.setText("Monthly Interest (MI) = " + miResult);
                    isMIActive = false; // Reset
                }
            } catch (NumberFormatException e) {
                textView.setText("Invalid number, please re-enter.");
            }
        }
    }
    private void handlePInput() {
        if (!currentInput.isEmpty()) {
            try {
                double inputVal = Double.parseDouble(currentInput);
                pParams.add(inputVal);
                currentInput = "";

                if (pParams.size() == 1) {
                    textView.setText("Please enter the loan term in months (n):");
                } else if (pParams.size() == 2) {
                    textView.setText("Please enter the monthly interest rate (r):");
                } else if (pParams.size() == 3) {
                    double L = pParams.get(0);
                    double n = pParams.get(1);
                    double r = pParams.get(2)/100;
                    double temp = Math.pow((1+r), n);
                    double num = L * r * temp;
                    double denom = temp - 1;
                    double pResult = num/denom;

                    textView.setText("Loan Payment (P) = " + pResult);
                    ispActive = false;
                }
            } catch (NumberFormatException e) {
                textView.setText("Invalid number, please re-enter.");
            }
        }
    }
}