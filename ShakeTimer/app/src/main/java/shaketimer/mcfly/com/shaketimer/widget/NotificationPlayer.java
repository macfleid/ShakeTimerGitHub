package shaketimer.mcfly.com.shaketimer.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by mcfly on 28/10/2014.
 */
public class NotificationPlayer {

    private final static String TAG = NotificationPlayer.class.getName();

    public static void playDefaultRingtone(Context context) {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            playMusic(context,defaultRingtoneUri);
        } catch (Exception e) {
            Log.e(TAG,"playDefaultRingtone",e);
        }
    }

    private static void playMusic(Context context, String name) throws Exception {
        Log.d( TAG, "[playMusic]");
		MediaPlayer mp = new MediaPlayer();
		AssetFileDescriptor source = context.getAssets().openFd(name);
    	mp.setDataSource( source.getFileDescriptor(), source.getStartOffset(), source.getLength());
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
		source.close();
        mp.prepare();
		mp.start();
    }

    private static void playMusic(Context context, Uri uri) throws Exception {
        Log.d( TAG, "[playMusic]");
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource( context, uri);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mp.prepare();
        mp.start();
    }
}
