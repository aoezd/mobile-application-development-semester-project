package com.aoezdemir.todoapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogMaker {

    public static void makeNeutralOkAlertDialog(Context context, String title, String message) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", ((DialogInterface dialog, int which) -> dialog.dismiss())).show();
    }
}