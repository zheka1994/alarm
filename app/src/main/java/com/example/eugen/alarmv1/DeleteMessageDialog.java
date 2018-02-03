package com.example.eugen.alarmv1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.UUID;

/**
 * Created by Eugen on 31.01.2018.
 */

public class DeleteMessageDialog extends DialogFragment {
    private static final String ALARM_ID = "ALARM_ID";
    public interface DeleteMessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }
    public static DeleteMessageDialog newInstance(UUID id){
        Bundle args = new Bundle();
        args.putSerializable(ALARM_ID,id);
        DeleteMessageDialog dialog = new DeleteMessageDialog();
        dialog.setArguments(args);
        return dialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final UUID uuid = (UUID)getArguments().getSerializable(ALARM_ID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_string)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Удаляем запись
                        Alarm alarm = ApplicationModel.getInstance(getActivity()).getAlarm(uuid);
                        ApplicationModel.getInstance(getActivity()).deleteRow(alarm);
                        listener.onDialogPositiveClick(DeleteMessageDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Не удаляем запись
                    }
                });
        return builder.create();
    }
    DeleteMessageDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DeleteMessageDialogListener) activity;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }

    }
}
