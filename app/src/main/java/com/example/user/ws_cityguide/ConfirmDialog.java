package com.example.user.ws_cityguide;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ConfirmDialog extends DialogFragment {

      enum Button {
            Negative,
            Positive
      }

      public ConfirmDialog() { }

      public static ConfirmDialog newInstance(String msg, String negText, String posText) {
            ConfirmDialog dialog = new ConfirmDialog();
            Bundle args = new Bundle();
            args.putString("message", msg);
            args.putString("negText", negText);
            args.putString("posText", posText);
            dialog.setArguments(args);
            return dialog;
      }

      @Override
      public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = getArguments().getString("message");
            String posText = getArguments().getString("posText");
            String negText = getArguments().getString("negText");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setNegativeButton(negText, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                                mListener.onFinishDialog(Button.Negative);
                          }
                    })
                    .setPositiveButton(posText, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                                mListener.onFinishDialog(Button.Positive);
                          }
                    });

            return builder.create();
      }

      interface OnFinishDialogListener {
            void onFinishDialog(ConfirmDialog.Button button);
      }

      private OnFinishDialogListener mListener;

      public void setOnFinishDialogListener(OnFinishDialogListener listener) {
            mListener = listener;
      }
}

