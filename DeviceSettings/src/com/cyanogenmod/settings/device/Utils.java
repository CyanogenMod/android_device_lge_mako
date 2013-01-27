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

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Utils {
    private static final String TAG = "MakoPartsUtils";

    /**
     * Write a string value to the specified file.
     *
     * @param filename The filename
     * @param value The value
     */
    public static void writeValue(String filename, String value) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename), false);
            fos.write(value.getBytes());
            fos.flush();
        } catch (IOException e) {
            Log.w(TAG, "Could not write to " + filename);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.d(TAG, "Could not close " + filename, e);
                }
            }
        }

    }

    /**
     * Check if the specified file exists.
     *
     * @param filename The filename
     * @return Whether the file exists or not
     */
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    /**
     * Read one line from the specified file
     *
     * @param filename The filename
     * @return The first line of the file or null
     */
    public static String readOneLine(String filename) {
        BufferedReader reader = null;
        String line = null;

        try {
            reader = new BufferedReader(new FileReader(filename), 512);
            line = reader.readLine();
        } catch (Exception e) {
            Log.e(TAG, "Could not read from " + filename, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "Could not close " + filename, e);
                }
            }
        }

        return line;
    }
}
