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

import org.cyanogenmod.hardware.util.FileUtils;

public class DisplayGammaCalibration {
    private static final String GAMMA_R_FILE = "/sys/devices/platform/mipi_lgit.1537/kgamma_r";
    private static final String GAMMA_G_FILE = "/sys/devices/platform/mipi_lgit.1537/kgamma_g";
    private static final String GAMMA_B_FILE = "/sys/devices/platform/mipi_lgit.1537/kgamma_b";
    private static final String GAMMA_FILE_CTRL = "/sys/devices/platform/mipi_lgit.1537/kgamma_apply";

    public static boolean isSupported() {
        return true;
    }

    public static int getMaxValue()  {
        return 31;
    }
    public static int getMinValue()  {
        return 0;
    }
    public static String getCurGammaR()  {
        return FileUtils.readOneLine(GAMMA_R_FILE);
    }
    public static String getCurGammaG()  {
        return FileUtils.readOneLine(GAMMA_G_FILE);
    }
    public static String getCurGammaB()  {
        return FileUtils.readOneLine(GAMMA_B_FILE);
    }
    public static boolean setGammaR(String gammaR)  {
        if (!FileUtils.writeLine(GAMMA_R_FILE, gammaR)) {
            return false;
        }
        return FileUtils.writeLine(GAMMA_FILE_CTRL, "1");
    }
    public static boolean setGammaG(String gammaG)  {
        if (!FileUtils.writeLine(GAMMA_G_FILE, gammaG)) {
            return false;
        }
        return FileUtils.writeLine(GAMMA_FILE_CTRL, "1");
    }
    public static boolean setGammaB(String gammaB)  {
        if (!FileUtils.writeLine(GAMMA_B_FILE, gammaB)) {
            return false;
        }
        return FileUtils.writeLine(GAMMA_FILE_CTRL, "1");
    }
}
