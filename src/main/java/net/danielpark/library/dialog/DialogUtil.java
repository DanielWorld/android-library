package net.danielpark.library.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by namgyu.park on 2017. 10. 30..
 */

public final class DialogUtil {

    private DialogUtil(){}

    /**
     * Show custom {@link AlertDialog}
     * @param context
     * @param input
     */
    public static void showDefaultDialog(Context context, DialogInput input) {
        if (context == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(input.getTitle());
        builder.setMessage(input.getMessage());
        builder.setCancelable(input.isCancelable());

        if (input.hasNeutralButtonText()) {
            builder.setNeutralButton(input.getNeutralButtonText(), input.getClickListener());
        } else {
            if (input.hasPositiveButtonText())
                builder.setPositiveButton(input.getPositiveButtonText(), input.getClickListener());

            if (input.hasNegativeButtonText())
                builder.setNegativeButton(input.getNegativeButtonText(), input.getClickListener());
        }

        builder.show();
    }
}
