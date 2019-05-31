package com.davidlacho.stormy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by david.lacho on 2019-05-31
 */
public class AlertDialogFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Context context = getActivity();
    AlertDialog.Builder builder = new Builder(context);

    builder.setTitle(getString(R.string.error_title)).setMessage(getString(R.string.error_message));
    builder.setPositiveButton(getString(R.string.error_button_ok_text),null);

    return builder.create();
  }
}
