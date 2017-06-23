package com.plavatvornica.mislav.plavatvornica_zadatak.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.plavatvornica.mislav.plavatvornica_zadatak.R;

/**
 * Created by Mislav on 22.6.2017..
 */

public class MyDialogFragment extends DialogFragment {

    private static final String MESSAGE_TAG = "message";

    public static MyDialogFragment newInstance(int message) {
        MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putInt(MESSAGE_TAG, message);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int message = getArguments().getInt(MESSAGE_TAG);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.alert_dialog_title).setMessage(message)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }


}
