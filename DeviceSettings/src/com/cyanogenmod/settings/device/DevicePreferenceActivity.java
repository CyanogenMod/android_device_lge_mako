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

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import com.cyanogenmod.settings.device.R;

public class DevicePreferenceActivity extends PreferenceFragment {

    public static final String SHARED_PREFERENCES_BASENAME = "com.cyanogenmod.settings.device";
    public static final String ACTION_UPDATE_PREFERENCES = "com.cyanogenmod.settings.device.UPDATE";
    public static final String KEY_COLOR_TUNING = "color_tuning";
    public static final String KEY_VIBRATOR_TUNING = "vibrator_tuning";

    private ColorTuningPreference mColorTuning;
    private VibratorTuningPreference mVibratorTuning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mColorTuning = (ColorTuningPreference) findPreference(KEY_COLOR_TUNING);
        mColorTuning.setEnabled(ColorTuningPreference.isSupported());

        mVibratorTuning = (VibratorTuningPreference) findPreference(KEY_VIBRATOR_TUNING);
        mVibratorTuning.setEnabled(VibratorTuningPreference.isSupported());
    }
}
