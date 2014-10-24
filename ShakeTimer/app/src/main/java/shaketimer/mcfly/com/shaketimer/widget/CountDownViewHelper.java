package shaketimer.mcfly.com.shaketimer.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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
        textView.setText(String.valueOf(time));
        return textView;
    }



}
