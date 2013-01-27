/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;

/**
 * Special preference type that allows configuration of vibrator intensity settings on Nexus
 * Devices
 */
public class VibratorTuningPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "VibratorTuningPreference";

    private static final String FILE_PATH = "/sys/devices/virtual/timed_output/vibrator/amp";
    private static final int MAX_VALUE = 100;
    private static final int WARNING_THRESHOLD = 88;
    private static final int DEFAULT_VALUE = 70;

    private SeekBar mSeekBar;
    private TextView mValue;

    private String mOriginalValue;

    private Drawable mProgressDrawable;
    private Drawable mProgressThumb;
    private LightingColorFilter mRedFilter;

    public VibratorTuningPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_dialog_vibrator_tuning);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setNeutralButton(R.string.defaults_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mSeekBar = (SeekBar) view.findViewById(R.id.vibrator_seekbar);
        mValue = (TextView) view.findViewById(R.id.vibrator_value);

        Drawable progressDrawable = mSeekBar.getProgressDrawable();
        if (progressDrawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) progressDrawable;
            mProgressDrawable = ld.findDrawableByLayerId(android.R.id.progress);
        }

        mProgressThumb = mSeekBar.getThumb();
        mRedFilter = new LightingColorFilter(Color.BLACK,
                getContext().getResources().getColor(android.R.color.holo_red_light));

        mOriginalValue = Utils.readOneLine(FILE_PATH);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setProgress(Integer.valueOf(mOriginalValue));
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        // Can't use onPrepareDialogBuilder for this as we want the dialog
        // to be kept open on click
        AlertDialog d = (AlertDialog) getDialog();
        Button defaultsButton = d.getButton(DialogInterface.BUTTON_NEUTRAL);
        defaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(DEFAULT_VALUE);
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            Editor editor = getEditor();
            editor.putString(FILE_PATH, String.valueOf(mSeekBar.getProgress()));
            editor.commit();
        } else {
            Utils.writeValue(FILE_PATH, mOriginalValue);
        }
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String value = prefs.getString(FILE_PATH, null);

        if (value != null) {
            Log.d(TAG, "Restoring vibration setting: " + value);
            Utils.writeValue(FILE_PATH, value);
        }
    }

    public static boolean isSupported() {
        return Utils.fileExists(FILE_PATH);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        boolean shouldWarn = progress >= WARNING_THRESHOLD;
        if (mProgressDrawable != null) {
            mProgressDrawable.setColorFilter(shouldWarn ? mRedFilter : null);
        }
        if (mProgressThumb != null) {
            mProgressThumb.setColorFilter(shouldWarn ? mRedFilter : null);
        }
        Utils.writeValue(FILE_PATH, String.valueOf(progress));
        mValue.setText(String.format("%d%%", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(200);
    }
}
