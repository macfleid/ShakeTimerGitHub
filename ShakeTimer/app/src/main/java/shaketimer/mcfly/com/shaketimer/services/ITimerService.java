package shaketimer.mcfly.com.shaketimer.services;

/**
 * Created by mcfly on 22/10/2014.
 */
public interface ITimerService {

    public void start();

    public void pause();

    public void restart();

    public void setTimer(int timer);
}
