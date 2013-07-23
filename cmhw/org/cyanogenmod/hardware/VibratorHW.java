package org.cyanogenmod.hardware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class VibratorHW {

    private static String AMP_PATH = "/sys/class/timed_output/vibrator/amp";

    public static boolean isSupported() { return true; }

    public static int getMaxIntensity()  {
        return 100;
    }
    public static int getMinIntensity()  {
        return 50;
    }
    public static int getWarningThreshold()  {
        return -1;
    }
    public static int getCurIntensity()  {
        return Integer.parseInt(readOneLine(AMP_PATH));
    }
    public static int getDefaultIntensity()  {
        return 70;
    }
    public static boolean setIntensity(int intensity)  {
        writeValue(String.valueOf(intensity));
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

    private static void writeValue(String value) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(AMP_PATH));
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
