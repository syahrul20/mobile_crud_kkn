package com.lid.kkn.Utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.lid.kkn.R;

public class LoadingDialog {
    AlertDialog dialog;

    public AlertDialog initializeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.dialog_loading);
        dialog = builder.create();
        return dialog;
    }
}
