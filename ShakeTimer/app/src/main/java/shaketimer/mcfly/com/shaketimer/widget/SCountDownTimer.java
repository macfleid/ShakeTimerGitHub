package shaketimer.mcfly.com.shaketimer.widget;

import android.os.CountDownTimer;

/**
 * Created by mcfly on 22/10/2014.
 */
public class SCountDownTimer extends CountDownTimer {

    private CountDownListener listener;

    public SCountDownTimer(long millisInFuture, CountDownListener listener) {
        super(millisInFuture, 1000);
        this.listener = listener;
    }

    @Override
    public void onTick(long l) {
        listener.publishProgress();
    }

    @Override
    public void onFinish() {
        listener.publishEndOfTimer();
    }
}
