package shaketimer.mcfly.com.shaketimer.widget;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by mcfly on 22/10/2014.
 */
public class CountDownViewHelper {

    private TextView textView;

    public CountDownViewHelper(Context context, TextView textView) {
        this.textView = textView;
    }

    public TextView showTime(int time) {
        if(textView==null) {
            return null;
        }
        int minutes = time / 60;
        int seconds = time - (minutes*60);
        String result = String.format("%02d:%02d",minutes,seconds);
        textView.setText(result);
        return textView;
    }


}
