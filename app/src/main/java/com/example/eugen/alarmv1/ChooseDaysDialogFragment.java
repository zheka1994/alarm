package com.example.eugen.alarmv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.UUID;



public class ChooseDaysDialogFragment extends DialogFragment {
    private char[] daysOfWeek;
    public static ChooseDaysDialogFragment newInstance(UUID uuid,int action){
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.UUID,uuid);
        args.putInt(MainActivity.ACTION,action);
        ChooseDaysDialogFragment dialog = new ChooseDaysDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] variantDays = {getString(R.string.monday),getString(R.string.tuesday),getString(R.string.wednesday),getString(R.string.thursday),
                getString(R.string.friday),getString(R.string.saturday),getString(R.string.sunday)};
        boolean[] CheckedItems = new boolean[7];
        for(int i = 0;i<CheckedItems.length;i++){
            CheckedItems[i] = false;
        }
        int action = getArguments().getInt(MainActivity.ACTION);
        if(action == 0){
            daysOfWeek = new char[7];
        }
        else if(action==1){
            UUID uuid = (UUID)getArguments().getSerializable(MainActivity.UUID);
            if(uuid != null){
                Alarm alarm = ApplicationModel.getInstance(getActivity()).getAlarm(uuid);
                String str = alarm.getDaysOfWeek();
                daysOfWeek = str.toCharArray();
                for(int i = 0;i<daysOfWeek.length;i++){
                    switch (daysOfWeek[i]) {
                        case '1':
                            CheckedItems[i] = true;
                            break;
                        case '0':
                            CheckedItems[i] = false;
                            break;
                    }
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.chooseDays))
                .setMultiChoiceItems(variantDays, CheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            daysOfWeek[i] = '1';
                        }
                        else{
                            daysOfWeek[i] = '0';
                        }
                    }
                }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onFinishChooseDay(new String(daysOfWeek));
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
    public interface ChooseDaysOfWeekListener{
        void onFinishChooseDay(String s);
    }
    ChooseDaysOfWeekListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ChooseDaysOfWeekListener)activity;
        }
        catch(ClassCastException e){
            e.printStackTrace();
        }
    }
}
