package com.github.robining.helper.version;

import android.content.Context;

import com.github.robining.config.interfaces.ui.dialog.AlertDialog;
import com.github.robining.config.interfaces.ui.dialog.ProgressDialog;

/**
 * Created by Administrator on 2017\11\29 0029.
 */

public interface VersionStyleProvider {
    ProgressDialog provideProgressDialog(Context context);

    AlertDialog provideFoundNewVersionDialog(Context context);
}
