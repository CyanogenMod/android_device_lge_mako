/*
 * Copyright (C) 2011 The CyanogenMod Project
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.preference.VolumePreference.SeekBarVolumizer;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Button;

/**
 * Special preference type that allows configuration of Color settings on Nexus
 * Devices
 */
public class ColorTuningPreference extends DialogPreference implements OnClickListener {

    private static final String TAG = "COLOR...";
    private static final String COLOR_FILE = "/sys/devices/platform/kcal_ctrl.0/kcal";
    private static final String COLOR_FILE_CTRL = "/sys/devices/platform/kcal_ctrl.0/kcal_ctrl";

    private static final int MAX_VALUE = 255;

    private static int sInstances = 0;

    // These arrays must all match in length and order
    private static final int[] SEEKBAR_ID = new int[] {
        R.id.color_red_seekbar,
        R.id.color_green_seekbar,
        R.id.color_blue_seekbar
    };

    private static final int[] SEEKBAR_VALUE_ID = new int[] {
        R.id.color_red_value,
        R.id.color_green_value,
        R.id.color_blue_value
    };

    private ColorSeekBar[] mSeekBars = new ColorSeekBar[SEEKBAR_ID.length];

    public ColorTuningPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_dialog_color_tuning);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        for (int i = 0; i < SEEKBAR_ID.length; i++) {
            SeekBar seekBar = (SeekBar) view.findViewById(SEEKBAR_ID[i]);
            TextView valueDisplay = (TextView) view.findViewById(SEEKBAR_VALUE_ID[i]);
            mSeekBars[i] = new ColorSeekBar(seekBar, valueDisplay, i);
        }
        SetupButtonClickListeners(view);
    }

    private void SetupButtonClickListeners(View view) {
            Button mButton1 = (Button)view.findViewById(R.id.btnColor1);
            mButton1.setOnClickListener(this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            for (ColorSeekBar csb : mSeekBars) {
                csb.save();
            }
        } else if (sInstances == 0) {
            for (ColorSeekBar csb : mSeekBars) {
                csb.reset();
            }
        }
    }

    /**
     * Restore color tuning from SharedPreferences. (Write to kernel.)
     * 
     * @param context The context to read the SharedPreferences from
     */
    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String sDefaultValue = readColors();
        Log.d(TAG,"sharedPrefs.COLOR_FILE(read)...:" + sharedPrefs.getString(COLOR_FILE, sDefaultValue));
        String[] sValue = sharedPrefs.getString(COLOR_FILE, sDefaultValue).split(" ");

        writeColors(sValue);
    }

    /**
     * Check whether the running kernel supports color tuning or not.
     * 
     * @return Whether color tuning is supported or not
     */
    public static boolean isSupported() {
        boolean supported = true;
        if (!Utils.fileExists(COLOR_FILE)) {
            supported = false;
        }

        return supported;
    }

    class ColorSeekBar implements SeekBar.OnSeekBarChangeListener {

        private int mColor;

        private int mOriginal;

        private SeekBar mSeekBar;

        private TextView mValueDisplay;

        public ColorSeekBar(SeekBar seekBar, TextView valueDisplay, int colorRGB) {
            int iValue;

            mSeekBar = seekBar;
            mValueDisplay = valueDisplay;
            mColor = colorRGB;

            SharedPreferences sharedPreferences = getSharedPreferences();

            // Read original value
            String sDefaultValue = readRGB(parseColors(), mColor);
            iValue = (int) (Long.valueOf(sDefaultValue) * 1);
            mOriginal = iValue;

            mSeekBar.setMax(MAX_VALUE);
            reset();
            mSeekBar.setOnSeekBarChangeListener(this);
        }

        public void reset() {
            mSeekBar.setProgress(mOriginal);
            updateValue(mOriginal);
        }

        public void save() {
            Editor editor = getEditor();
            editor.putString(COLOR_FILE, readColors());
            Log.d(TAG,"sharedPrefs.COLOR_FILE(write)...:" + readColors());
            editor.commit();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String[] currentColors = parseColors();
            currentColors[mColor] = String.valueOf(progress);
            writeColors(currentColors);
            updateValue(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        private void updateValue(int progress) {
            mValueDisplay.setText(String.format("%d", (int) progress));
        }

        public void SetProgressValue(int iValue) {
            mSeekBar.setProgress(iValue);
            updateValue(iValue);
        }

        public void SetNewValue(int iValue) {
            mOriginal = iValue;
            reset();
        }

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnColor1:
                    setDefaults();
                    break;
        }
    }

    private void setDefaults() {
        for (ColorSeekBar csb : mSeekBars) {
            csb.SetProgressValue(MAX_VALUE);
        }
    }

    private static String readRGB(String[] colors, int colorRGB) {
        return colors[colorRGB];
    }

    private static String[] parseColors() {
        return readColors().split(" ");
    }

    private static String readColors() {
        return Utils.readOneLine(COLOR_FILE);
    }

    private static void writeColors(String[] colors) {
        String tempColors = "";
        for (String color : colors) {
            tempColors = tempColors + " " + color;
        }
        tempColors = tempColors.trim();
        Utils.writeValue(COLOR_FILE, tempColors);
        Utils.writeValue(COLOR_FILE_CTRL, "1");
    }

}
