package shaketimer.mcfly.com.shaketimer.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import shaketimer.mcfly.com.shaketimer.R;
import shaketimer.mcfly.com.shaketimer.TimerActivity;

/**
 * Created by mcfly on 23/10/2014.
 */
public class TimerPickerDialog extends DialogFragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
//		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.timer_picker_dialog,container, false);
        NumberPicker minutesPicker = (NumberPicker) view.findViewById(R.id.minutes_picker);
        NumberPicker secondsPicker = (NumberPicker) view.findViewById(R.id.seconds_picker);

        minutesPicker.setMaxValue(60);
        secondsPicker.setMaxValue(60);
        minutesPicker.setMinValue(0);
        secondsPicker.setMinValue(0);

        getDialog().setTitle("Timer settings");
        getDialog().setCancelable(true);

        setListener(minutesPicker);
        setListener(secondsPicker);
        return view;
    }

    private void setListener(NumberPicker picker) {
        final NumberPicker minutesPicker = (NumberPicker) view.findViewById(R.id.minutes_picker);
        final NumberPicker secondsPicker = (NumberPicker) view.findViewById(R.id.seconds_picker);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                TimerActivity activity = (TimerActivity) getActivity();
                int minutes = minutesPicker.getValue();
                int seconds = secondsPicker.getValue();
                activity.setTimer(minutes*60+seconds);
            }
        });
    }
}
