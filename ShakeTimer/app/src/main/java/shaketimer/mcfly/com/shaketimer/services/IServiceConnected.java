package shaketimer.mcfly.com.shaketimer.services;

/**
 * Created by mcfly on 22/10/2014.
 */
public interface IServiceConnected {

    public void onServiceConnected();

    public void publishTimer(int timer);

    public void signalEnd();
}
