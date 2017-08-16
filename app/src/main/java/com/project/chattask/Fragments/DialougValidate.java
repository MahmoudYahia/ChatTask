package com.project.chattask.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/**
 * Created by mah_y on 8/15/2017.
 */

public class DialougValidate extends DialogFragment {
    public static DialougValidate newInstance() {
        DialougValidate addListDialogFragment = new DialougValidate();
        Bundle bundle = new Bundle();
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return super.onCreateDialog(savedInstanceState);
    }
}
