package shaketimer.mcfly.com.shaketimer.services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import shaketimer.mcfly.com.shaketimer.TimerActivity;

/**
 * Created by mcfly on 22/10/2014.
 */
public class TimerServiceManager implements ServiceConnection {

    private final static String TAG = TimerServiceManager.class.getName();
    private final static String RECEIVER  = TimerServiceManager.class.getName();

    public final static String KEY_SET_TIMERVIEW = "KEY_SET_TIMERVIEW";
    public final static String KEY_PUBLISH_END = "KEY_PUBLISH_END";

    private Context context;
    private TimerService service;
    private IServiceConnected activity;
    private NotificationReceiver receiver;

    public TimerServiceManager(Context context, IServiceConnected activity) {
        this.context = context;
        this.activity = activity;
        try{
            Intent intent = new Intent(context, TimerService.class);
            intent.putExtra(TimerService.RECEIVER_KEY, RECEIVER);
            context.bindService(intent, this, Context.BIND_AUTO_CREATE);
            receiver = new NotificationReceiver();
            context.registerReceiver(receiver, new IntentFilter(RECEIVER));
        } catch (Exception e ){
            Log.e(TAG,"... unable to start service",e);
        }
    }

    public void destroy() {
        context.unregisterReceiver(receiver);
        context.unbindService(this);
    }

    public ITimerService getService() {
        return ((ITimerService) service);
    }

    //---------------------------------------------------------------------

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG,"[onServiceConnected]");
        TimerService.ServiceBinder binder = (TimerService.ServiceBinder) iBinder;
        this.service = binder.getService();
        this.activity.onServiceConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG,"[onServiceDisconnected]");
    }

    //--------------------------------------------------
    //Service notification
    //----------------------------------------------------
    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle==null) {
                return;
            }
            if(bundle.containsKey(KEY_SET_TIMERVIEW)) {
                int timer = bundle.getInt(KEY_SET_TIMERVIEW);
                activity.publishTimer(timer);
            } else if (bundle.containsKey(KEY_PUBLISH_END)) {
                activity.signalEnd();
            }
        }
    }
}
