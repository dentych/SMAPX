package group.smapx.remindalot.Create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

/**
 * Created by root on 10/9/16.
 */

public class DescriptionBtnListner implements View.OnClickListener {

    EditText textInput;
    private Context context;
    String data = "";
    DescriptionReceiver receiver;

     public DescriptionBtnListner(Context context, DescriptionReceiver receiver)  {

        this.context = context;
         this.receiver = receiver;
    }

    @Override
    public void onClick(View v) {
        textInput = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle("Description")
                .setMessage("Enter description")
                .setView(textInput)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data = textInput.getText().toString();
                        receiver.onDescriptionEntered(data);
                    }
                }).show();
    }
}
