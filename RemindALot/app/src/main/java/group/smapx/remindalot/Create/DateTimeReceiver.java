package group.smapx.remindalot.Create;

/**
 * Created by benla on 10/6/2016.
 */

public interface DateTimeReceiver {

    public void onTimeChosen(int hour, int minute);
    public void onDateChosen(int day, int month, int year);

}
