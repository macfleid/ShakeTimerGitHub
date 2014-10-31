package shaketimer.mcfly.com.shaketimer;

import android.app.Activity;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import shaketimer.mcfly.com.shaketimer.fragments.TimerPickerDialog;
import shaketimer.mcfly.com.shaketimer.services.IServiceConnected;
import shaketimer.mcfly.com.shaketimer.services.TimerService;
import shaketimer.mcfly.com.shaketimer.services.TimerServiceManager;
import shaketimer.mcfly.com.shaketimer.widget.CountDownViewHelper;

/**
 *
 */
public class TimerActivity extends Activity implements IServiceConnected, SensorEventListener {

    private final static String TAG = TimerActivity.class.getName();

    private TimerServiceManager timerServiceManager;
    private CountDownViewHelper countDownViewHelper;
    private TextView chronometer;

    private int configuratedTimer = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        this.timerServiceManager = new TimerServiceManager(this,this);
        this.chronometer = (TextView) findViewById(R.id.chronometer);
        this.countDownViewHelper = new CountDownViewHelper(this,chronometer);

        chronometer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                openTimePickerDialog();
                return false;
            }
        });

        setShakeEvent();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void startButtonAction(View v) {
        this.timerServiceManager.getService().start();
        startProgressBar();
    }

    public void stopButtonAction(View v) {
        this.timerServiceManager.getService().pause();
        stopProgressBar();
    }

    public void resetButtonAction(View v) {
        this.timerServiceManager.getService().restart();
        startProgressBar();
    }

    public void setTimer(int timer) {
        configuratedTimer = timer;
        this.timerServiceManager.getService().setTimer(configuratedTimer);
        this.countDownViewHelper.showTime(configuratedTimer);
    }

    //---------------------------------------------------------------

    private void openTimePickerDialog() {
        TimerPickerDialog pickerFragment = new TimerPickerDialog();
        pickerFragment.show(getFragmentManager(),"TIMEPICKER");
    }

    private void setShakeEvent() {
        SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mAccelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMgr.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void startProgressBar() {
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgressBar() {
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    //--------------------------------------------------------
    //-- IServiceConnected
    //--------------------------------------------------------
    @Override
    public void onServiceConnected() {
        Log.d(TAG,"[onServiceConnected]");
        this.timerServiceManager.getService().setTimer(this.configuratedTimer);
        this.countDownViewHelper.showTime(configuratedTimer);
    }

    @Override
    public void publishTimer(int timer) {
        this.countDownViewHelper.showTime(timer);
    }

    @Override
    public void signalEnd() {
        setTimer(configuratedTimer);
        this.countDownViewHelper.showTime(this.configuratedTimer);
        stopProgressBar();
    }



    //--------------------------------------------------------
    //-- IServiceConnected
    //--------------------------------------------------------

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float axisX = sensorEvent.values[0];
        float axisY = sensorEvent.values[1];
        float axisZ = sensorEvent.values[2];
        double omegaMagnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        if(omegaMagnitude>30) {
            this.timerServiceManager.getService().restart();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
