package fooderie.CookingAssistant.views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;

import java.util.Locale;

public class CookingAssistantTimerView extends AppCompatActivity
{
    private TextView countDownText;
    private EditText editTextInput;
    private Button btnStartTimer;
    private Button btnResetTimer;
    private Button btnSetTimer;

    private CountDownTimer countDownTimer;

    private long startTime;
    private long endTime;
    private long timeLeftInMillisecond;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_timer);

        //Find elements
        editTextInput = findViewById(R.id.editTextInput);
        countDownText = findViewById(R.id.txtCounter);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnSetTimer = findViewById(R.id.btnSetTimer);
        btnResetTimer = findViewById(R.id.btnResetTimer);

        btnStartTimer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (timerRunning)
                    pauseTimer();
                else
                    startTimer();
            }
        });

        btnResetTimer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                resetTimer();
            }
        });

        btnSetTimer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String input = editTextInput.getText().toString();

                if (input.length() < 1)
                {
                    Toast.makeText(CookingAssistantTimerView.this, "Please set a number of minutes to run the timer for", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millInput = Long.parseLong(input) * 60000; //Number of milliseconds from minutes
                if (millInput == 0)
                {
                    Toast.makeText(CookingAssistantTimerView.this, "Please enter a positive number of minutes to run the timer for", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTotalTime(millInput);
                editTextInput.setText("");
            }
        });

        setCountDownText();
    }

    public void setTotalTime(long milliseconds)
    {
        startTime = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    //Hides the popup keyboard from the edit text field
    public void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Updates the screen layout
    public void updateTimerLayout()
    {
        if (timerRunning)
        {
            btnResetTimer.setVisibility(View.INVISIBLE);
            btnStartTimer.setText("Pause");
            btnSetTimer.setVisibility(View.INVISIBLE);
            editTextInput.setVisibility(View.INVISIBLE);
        }
        else
        {
            editTextInput.setVisibility(View.VISIBLE);
            btnSetTimer.setVisibility(View.VISIBLE);
            btnStartTimer.setText("Start");

            //If greater than 1 second left show pause
            if (timeLeftInMillisecond < 1000)
                btnStartTimer.setVisibility(View.INVISIBLE);
            else
                btnStartTimer.setVisibility(View.VISIBLE);

            //If are started, hide reset and set time buttons
            if (timeLeftInMillisecond < startTime)
            {
                btnResetTimer.setVisibility(View.VISIBLE);
            }
            else
            {
                btnResetTimer.setVisibility(View.INVISIBLE);
            }

        }
    }

    //Starts the timer
    public void startTimer()
    {
        endTime = System.currentTimeMillis() + timeLeftInMillisecond;

        //Create a new countdown timer
        countDownTimer = new CountDownTimer(timeLeftInMillisecond, 1000)
        {
            @Override
            public void onTick(long millisecondsLeft)   //Every tick
            {
                timeLeftInMillisecond = millisecondsLeft;
                setCountDownText();
            }

            @Override
            public void onFinish()  //Timer done
            {
                timerRunning = false;
                updateTimerLayout();
                vibratePhone();
            }
        }.start();

        timerRunning = true;
        updateTimerLayout();
    }

    //Pause the timer
    public void pauseTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;
        updateTimerLayout();
    }

    //Reset the timer
    public void resetTimer()
    {
        timeLeftInMillisecond = startTime;
        setCountDownText();
        updateTimerLayout();
    }

    public void setCountDownText()
    {
        int hours = (int) (timeLeftInMillisecond / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillisecond / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillisecond / 1000) % 60;

        String formatedTime;
        if (hours > 0)
            formatedTime = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, seconds);
        else
            formatedTime = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        countDownText.setText(formatedTime);
    }

    public void vibratePhone()
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        else
            v.vibrate(500); //For depreciated from before API 26

    }


    /*
    //Override when app stops to save our timer data
    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMills", startTime);
        editor.putLong("millsLeft", timeLeftInMillisecond);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);

        editor.apply();

        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    //Override when app starts again and retrieves the saved state
    protected void onStart()
    {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        startTime = prefs.getLong("startTimeInMills", 600000);
        timeLeftInMillisecond = prefs.getLong("millsLeft", startTime);
        timerRunning = prefs.getBoolean("timerRunning", false);

        setCountDownText();
        updateTimerLayout();

        //If our timer was running before, start it running again
        if (timerRunning)
        {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMillisecond = endTime - System.currentTimeMillis();

            if (timeLeftInMillisecond < 0)
            {
                timeLeftInMillisecond = 0;
                timerRunning = false;

                setCountDownText();
                updateTimerLayout();
            }
            else
                startTimer();
        }
    }*/

}




