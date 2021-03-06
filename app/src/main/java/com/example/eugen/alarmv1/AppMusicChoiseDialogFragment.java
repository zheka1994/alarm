package com.example.eugen.alarmv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Eugen on 31.01.2018.
 */

public class AppMusicChoiseDialogFragment extends DialogFragment {
    private int variant = -1;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] variantsMusic = {"alarm1","alarm2","alarm3","alarm4"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.choise_sourse))
                .setSingleChoiceItems(variantsMusic, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        variant = i;
                    }
                }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onFinishDialog(variant);
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
    public interface AppMusicChoiseDialogFragmentListener{
        void onFinishDialog(int v);
    }
    AppMusicChoiseDialogFragmentListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (AppMusicChoiseDialogFragmentListener) activity;
        }
        catch(ClassCastException e){
            e.printStackTrace();
        }

    }
}
