package group.smapx.remindalot.Create.ClickListeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import group.smapx.remindalot.Create.ReciverInterfaces.DescriptionReceiver;

/**
 * Created by root on 10/9/16.
 */

public class DescriptionBtnListner implements View.OnClickListener {

    EditText textInput;
    String data = "";
    String initalDescription;
    DescriptionReceiver receiver;
    private final Context context;

    public DescriptionBtnListner(Context context, DescriptionReceiver receiver) {
        this.context = context;
        this.receiver = receiver;

    }

    @Override
    public void onClick(View v) {
        this.initalDescription = this.receiver.getInitialDescription();
        Log.d("Debug", "Initial: " + this.initalDescription);
        this.textInput = new EditText(this.context);
        if (this.initalDescription != null && !this.initalDescription.matches(""))
            this.textInput.setText(this.initalDescription);

        new AlertDialog.Builder(this.context)
                .setTitle("Description")
                .setMessage("Enter description")
                .setView(this.textInput)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DescriptionBtnListner.this.data = DescriptionBtnListner.this.textInput.getText().toString();
                        DescriptionBtnListner.this.receiver.onDescriptionEntered(DescriptionBtnListner.this.data);
                    }
                }).show();
    }
}
