package shaketimer.mcfly.com.shaketimer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import shaketimer.mcfly.com.shaketimer.R;
import shaketimer.mcfly.com.shaketimer.widget.CountDownListener;
import shaketimer.mcfly.com.shaketimer.widget.NotificationPlayer;
import shaketimer.mcfly.com.shaketimer.widget.SCountDownTimer;

public class TimerService extends Service implements ITimerService, CountDownListener {

    private final static String TAG = TimerService.class.getName();

    public final static String RECEIVER_KEY = "RECEIVER";

    private SCountDownTimer countDownTimer;
    private int timeInFuture;
    private int currentTime;
    private boolean isRunning;

    public TimerService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
       return new ServiceBinder();
    }

    //------------------------------------------------
    //-- ITimerService
    //------------------------------------------------

    @Override
    public synchronized void start() {
        Log.d(TAG,"[start]");
        if(isRunning) {
            return;
        }
        isRunning = true;
        countDownTimer = new SCountDownTimer(currentTime*1000,this);
        this.countDownTimer.start();
    }

    @Override
    public synchronized void pause() {
        Log.d(TAG,"[pause]");
        if(countDownTimer==null || !isRunning) {
            return;
        }
        isRunning = false;
        countDownTimer.cancel();
    }

    @Override
    public synchronized void restart() {
       Log.d(TAG,"[restart]");
        if(isRunning) {
            isRunning = false;
            countDownTimer.cancel();
        }
       currentTime = timeInFuture;
       this.start();
    }

    @Override
    public synchronized void setTimer(int timer) {
        Log.d(TAG,"[setTimer]");
        this.timeInFuture = timer;
        this.currentTime = timeInFuture;
    }

    //--------------------------------------------------------------
    //--- CountDownListener
    //--------------------------------------------------------------
    @Override
    public void publishProgress() {
        Log.d(TAG,"[publishProgress]");
        this.currentTime--;
        Intent intent = new Intent(TimerServiceManager.class.getName());
        intent.putExtra(TimerServiceManager.KEY_SET_TIMERVIEW,currentTime);
        sendBroadcast(intent);
        showProgressInNotification();
    }

    @Override
    public void publishEndOfTimer() {
        Log.d(TAG,"[publishEndOfTimer]");
        Intent intent = new Intent(TimerServiceManager.class.getName());
        intent.putExtra(TimerServiceManager.KEY_PUBLISH_END,true);
        isRunning = false;
        sendBroadcast(intent);
        showAlarmEndNotification();
    }

    //---------------------------------------------------
    //-- ServiceBinder
    //---------------------------------------------------

    public class ServiceBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }


    //-----------------------------------------------------

    private void showProgressInNotification() {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText("counting")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setProgress(this.timeInFuture,this.currentTime,false)
                .build();
        mNotifyManager.notify(133, notification);
    }

    private void showAlarmEndNotification() {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText("alarm triggered")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                //.setFullScreenIntent()
                .build();
        mNotifyManager.notify(133, notification);
        NotificationPlayer.playDefaultRingtone(getApplicationContext());
    }




}
