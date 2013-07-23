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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class DisplayColorCalibration {
    private static final String COLOR_FILE = "/sys/devices/platform/kcal_ctrl.0/kcal";
    private static final String COLOR_FILE_CTRL = "/sys/devices/platform/kcal_ctrl.0/kcal_ctrl";

    public static boolean isSupported() { return true; }

    public static int getMaxValue()  {
        return 255;
    }
    public static int getMinValue()  {
        return 0;
    }
    public static String getCurColors()  {
        return readOneLine(COLOR_FILE);
    }
    public static boolean setColors(String colors)  {
        writeValue(COLOR_FILE, colors);
        writeValue(COLOR_FILE_CTRL, "1");
        return true;
    }

    private static String readOneLine(String sFile) {
        BufferedReader brBuffer;
        String sLine = null;

        try {
            brBuffer = new BufferedReader(new FileReader(sFile), 512);
            try {
                sLine = brBuffer.readLine();
            } finally {
                brBuffer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sLine;
    }

    private static void writeValue(String filepath, String value) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filepath));
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
