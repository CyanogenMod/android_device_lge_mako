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

package org.cyanogenmod.hardware;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.cyanogenmod.hardware.util.FileUtils;

import java.io.File;

public class DisplayGammaCalibration {
    private static final String[] GAMMA_FILE_PATH = new String[] {
        "/sys/devices/platform/mipi_lgit.1537/kgamma_r",
        "/sys/devices/platform/mipi_lgit.1537/kgamma_g",
        "/sys/devices/platform/mipi_lgit.1537/kgamma_b"
    };

    private static final String GAMMA_FILE_CTRL =
        "/sys/devices/platform/mipi_lgit.1537/kgamma_apply";

    private static final long SET_DELAY = 100L; /* ms */

    private static final Handler H = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            applyGamma(msg.what, (String) msg.obj);
        }
    };

    private static String[] sLastSetValues = new String[3];

    public static boolean isSupported() {
        /* Barf out if the interface is absent */
        return new File(GAMMA_FILE_PATH[0]).exists() &&
               new File(GAMMA_FILE_CTRL).exists();
    }

    public static int getNumberOfControls() {
        return 3;
    }

    public static int getMaxValue(int controlIdx) {
        return controlIdx == 2 ? 80 : 31;
    }

    public static int getMinValue(int controlIdx) {
        return 0;
    }

    public static String getCurGamma(int control) {
        if (H.hasMessages(control)) {
            return sLastSetValues[control];
        }

        StringBuilder values = new StringBuilder();
        for (String filePath : GAMMA_FILE_PATH) {
            String[] allGammaValues = FileUtils.readOneLine(filePath).split(" ");
            if (values.length() > 0) {
                values.append(" ");
            }
            values.append(allGammaValues[5 + control]);
        }
        return values.toString();
    }

    public static boolean setGamma(int control, String gamma) {
        H.removeMessages(control);
        H.sendMessageDelayed(H.obtainMessage(control, gamma), SET_DELAY);
        sLastSetValues[control] = gamma;
        return true;
    }

    private static void applyGamma(int control, String gamma) {
        String[] valueSplit = gamma.split(" ");

        for (int i = 0; i < 3; i++) {
            String targetFile = GAMMA_FILE_PATH[i];
            String[] allGammaValues = FileUtils.readOneLine(targetFile).split(" ");
            allGammaValues[5 + control] = valueSplit[i];
            /* Calc the checksum */
            int checksum = 0;
            for (int j = 1; j < 10; j++) {
                checksum += Integer.parseInt(allGammaValues[j]);
            }
            allGammaValues[0] = String.valueOf(checksum);
            FileUtils.writeLine(targetFile, TextUtils.join(" ", allGammaValues));
        }
        FileUtils.writeLine(GAMMA_FILE_CTRL, "1");
    }
}
