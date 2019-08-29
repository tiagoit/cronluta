package com.example.cronluta;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Integer[] roundCountOptions = {1, 2, 3, 4, 5, 6 ,7, 8, 9, 10, 11, 12};
    String[] roundDurationOptions = {"0:30", "1:00", "1:30", "2:00", "3:00", "4:00", "5:00", "7:00"};
    String[] pauseDurationOptions = {"0:15", "0:30", "0:45", "1:00", "1:30", "2:00", "3:00"};

    Boolean countdownForPause = false;
    Integer selectedRoundCount;
    private Integer selectedRoundMinutes = 0, selectedRoundSeconds = 30;
    private Integer selectedPauseMinutes = 0, selectedPauseSeconds = 15;

    private Integer countdownRounds = null;
    private TextView countdownTimer;
    private Integer countdownMinutes = 0, countdownSeconds = 30;

    public static CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSpinners();
        countdownTimer = findViewById(R.id.countdownTimer);
    }

    protected void initSpinners() {
        Spinner roundCountSpinner = findViewById(R.id.roundCountSpinner);
        ArrayAdapter<Integer> roundCountAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roundCountOptions);
        roundCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roundCountSpinner.setAdapter(roundCountAdapter);
        roundCountSpinner.setOnItemSelectedListener(this);

        Spinner roundDurationSpinner = findViewById(R.id.roundDurationSpinner);
        ArrayAdapter<String> roundDurationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roundDurationOptions);
        roundDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roundDurationSpinner.setAdapter(roundDurationAdapter);
        roundDurationSpinner.setOnItemSelectedListener(this);

        Spinner pauseDurationSpinner = findViewById(R.id.pauseDurationSpinner);
        ArrayAdapter<String> pauseDurationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pauseDurationOptions);
        pauseDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pauseDurationSpinner.setAdapter(pauseDurationAdapter);
        pauseDurationSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        switch(arg0.getId()) {
            case R.id.roundCountSpinner:
                this.selectedRoundCount = roundCountOptions[position];
                break;
            case R.id.roundDurationSpinner:
                selectedRoundMinutes = Integer.parseInt(roundDurationOptions[position].split(":")[0]);
                selectedRoundSeconds = Integer.parseInt(roundDurationOptions[position].split(":")[1]);
                break;
            case R.id.pauseDurationSpinner:
                selectedPauseMinutes = Integer.parseInt(pauseDurationOptions[position].split(":")[0]);
                selectedPauseSeconds = Integer.parseInt(pauseDurationOptions[position].split(":")[1]);
                break;
        }
        this.reset(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }


    public void start(View view) {
        this.countdownStart();
    }

    public void pause(View view) {

    }

    public void reset(View view) {
        if(countDownTimer != null) countDownTimer.cancel();
        countdownForPause = false;
        countdownRounds = selectedRoundCount;
        countdownMinutes = selectedRoundMinutes;
        countdownSeconds = selectedRoundSeconds;
        countdownTimer.setText(String.format("%02d", countdownMinutes) + ':' +
                String.format("%02d", countdownSeconds));
    }

    public void startButtonClickHandler() {

        countdownStart();
    }


    public void countdownStart() {
//        Toast.makeText(getApplicationContext(), "countdown start: ", Toast.LENGTH_SHORT).show();
        if (countdownRounds == null) {
            countdownRounds = selectedRoundCount;
        }

        if (countdownForPause) {
            countdownMinutes = selectedPauseMinutes;
            countdownSeconds = selectedPauseSeconds;
        } else {
            countdownMinutes = selectedRoundMinutes;
            countdownSeconds = selectedRoundSeconds;
            countdownRounds--;
        }

        countdownForPause = !countdownForPause;


        countDownTimer = new CountDownTimer((countdownMinutes * 60 + countdownSeconds) * 100, 100) {

            public void onTick(long millisUntilFinished) {
                countdownTimer.setText(String.format("%02d", countdownMinutes) + ':' + String.format("%02d", countdownSeconds));
                if (countdownSeconds > 0) {
                    countdownSeconds--;
                } else if (countdownMinutes > 0) {
                    countdownMinutes--;
                    countdownSeconds = 59;
                }
            }

            public void onFinish() {
                if (countdownForPause || countdownRounds > 0) {
                    countdownStart();
                } else if (countdownRounds == 0 && !countdownForPause) {
                    countdownFinished();
                }
            }
        };

        countDownTimer.start();
    }

    public void countdownFinished() {
        Toast.makeText(getApplicationContext(),"countdown finished: ", Toast.LENGTH_SHORT).show();
        reset(null);
    }
}
