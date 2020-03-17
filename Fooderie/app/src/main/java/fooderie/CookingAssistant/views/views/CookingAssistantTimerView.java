package fooderie.CookingAssistant.views.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooderie.R;

public class CookingAssistantTimerView extends AppCompatActivity
{
    private TextView countDownText;
    private Button countDownButton;
    private Button setButton;
    private Button resetButton;

    private CountDownTimer countDownTimer;
    private long startTime = 60000;
    private long timeLeftInMillisecond = 60000; // 10 min
    private boolean timerRunning;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_assistant_timer);

        countDownText = findViewById(R.id.txtCounter);
        countDownButton = findViewById(R.id.btnStartTimer);
        setButton = findViewById(R.id.btnSetTimer);
        resetButton = findViewById(R.id.btnResetTimer);

        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                timeLeftInMillisecond = 60000;
            }
        });

        countDownButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startStop();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                newTime();
            }
        });

        myDialog = new Dialog(this);
        updateTimer();
    }

    //Set new timer time
    public void newTime()
    {
        NumberPicker numberPicker;
        Button setTime;
        RadioButton radioSeconds;
        RadioButton radioMinutes;
        String timeType;

        myDialog.setContentView(R.layout.activity_cooking_assistant_timer_popup);   //Create popup window
        numberPicker = (NumberPicker) myDialog.findViewById(R.id.numPicker);
        setTime = (Button) myDialog.findViewById(R.id.btnTimerSetTime);
        radioSeconds = (RadioButton) myDialog.findViewById(R.id.rdoSecond);
        radioMinutes = (RadioButton) myDialog.findViewById(R.id.rdoMinute);

        if (numberPicker != null)
        {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(1000);
            numberPicker.setWrapSelectorWheel(false);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal)
                {
                    //Get the time type
                    String timeType = "Seconds";
                    if (radioMinutes.isSelected())
                        timeType = "Minutes";

                    String text = "CurrentTimer: " + newVal + " " + timeType;
                    Toast.makeText(CookingAssistantTimerView.this, text, Toast.LENGTH_SHORT).show();
                }
            });
        }

        setTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Get the time value
                int selectedTime = numberPicker.getValue();

                //Get the time type
                String timeType = "Seconds";
                if (radioMinutes.isSelected())
                    timeType = "Minutes";

                newTime(selectedTime, timeType);
                //Remove the popup
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void newTime(int selectedTime, String timeType)
    {
        int multiplier = 1000;  //1000 ms per second * 60 for minutes
        if (timeType == "Minutes")
            multiplier *= 60;

        timeLeftInMillisecond = selectedTime * multiplier;  //Set the number of miliseconds on the timer remaining
        changeTimer("Start");   //Starts the timer again if its not started
        updateTimer();  //Updates current time
    }

    public void startStop()
    {
        if (timerRunning)
            changeTimer("Stop");
        else
            changeTimer("Start");
    }

    public void changeTimer(String which)
    {
        if (which == "Start")
        {
            countDownTimer = new CountDownTimer(timeLeftInMillisecond, 1000)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    timeLeftInMillisecond = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish()
                {

                }
            }.start();

            countDownButton.setText("PAUSE");
            timerRunning = true;
        }
        else
        {
            countDownTimer.cancel();
            countDownButton.setText("START");
            timerRunning = false;
        }

    }

    public void updateTimer()
    {
        int minutes = (int) timeLeftInMillisecond / 60000;  //Turns milliseconds into minutes
        int seconds = (int) timeLeftInMillisecond % 60000 / 10000;  // Get remainder of seconds

        String timeLeft = "";
        timeLeft += minutes + ":";

        if (seconds < 10)
            timeLeft += "0";
        timeLeft += seconds;

        countDownText.setText(timeLeft);
    }
}




