package group.smapx.remindalot.Create.ReciverInterfaces;

public interface DateTimeReceiver {

    public void onTimeChosen(int hour, int minute);
    public void onDateChosen(int day, int month, int year);

}
