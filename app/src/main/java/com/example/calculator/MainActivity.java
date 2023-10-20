package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        buttons = new MaterialButton[]{
                findViewById(R.id.button_0),
                findViewById(R.id.button_1),
                findViewById(R.id.button_2),
                findViewById(R.id.button_3),
                findViewById(R.id.button_4),
                findViewById(R.id.button_5),
                findViewById(R.id.button_6),
                findViewById(R.id.button_7),
                findViewById(R.id.button_8),
                findViewById(R.id.button_9),
                findViewById(R.id.button_dot),
                findViewById(R.id.button_plus),
                findViewById(R.id.button_minus),
                findViewById(R.id.button_multiply),
                findViewById(R.id.button_divide),
                findViewById(R.id.button_open_bracket),
                findViewById(R.id.button_close_bracket),
                findViewById(R.id.button_ac),
                findViewById(R.id.button_c),
                findViewById(R.id.button_equals)
        };

        for (MaterialButton button : buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String currentExpression = solutionTv.getText().toString();

        switch (buttonText) {
            case "AC":
                solutionTv.setText("");
                resultTv.setText("0");
                break;
            case "C":
                if (!currentExpression.isEmpty()) {
                    currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                    solutionTv.setText(currentExpression);
                }
                break;
            case "=":
                String finalResult = getResult(currentExpression);
                if (!finalResult.equals("Err")) {
                    resultTv.setText(finalResult);
                }
                break;
            default:
                currentExpression += buttonText;
                solutionTv.setText(currentExpression);
                break;
        }
    }

    String getResult(String expression) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();

            // Evaluate the expression
            Object result = context.evaluateString(scope, expression, "JavaScript", 1, null);

            // Check if the result is undefined
            if (result == Context.getUndefinedValue()) {
                return "Err"; // Return an error message
            }

            // Convert the result to a string
            String resultString = Context.toString(result);

            // If the result is an integer, remove the ".0" (if present)
            if (resultString.endsWith(".0")) {
                resultString = resultString.replace(".0", "");
            }

            return resultString;
        } catch (Exception e) {
            return "Err"; // Return an error message
        } finally {
            Context.exit();
        }
    }
}
