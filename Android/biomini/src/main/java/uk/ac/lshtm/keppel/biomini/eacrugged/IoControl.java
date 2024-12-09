package uk.ac.lshtm.keppel.biomini.eacrugged;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This code was copied from the EAC Rugged BioMini sample app.
 */
class IoControl {
    private static final String TAG = "IoControlUtils";
    public static final String AFVDD28_PATH = "/sys/dy_control/afvdd28_en";
    public static final String SMARTCARD_PATH = "/sys/dy_control/smartcard_en";
    public static final String USB_A1_PATH = "/sys/dy_control/usb_A1_en";
    public static final String SCAN_ENGINE_PATH = "/sys/dy_control/scan_engine_en";
    public static final String WK2124_PATH = "/sys/dy_control/wk2124_en";
    public static final String IC_CARDS_PATH = "/sys/dy_control/IC_cards_en";
    public static final String SCAN_ENGINE_RST_PATH = "/sys/dy_control/scan_engine_rst";
    public static final String CAM_POWER_CTRL_PATH = "/sys/dy_control/cam_power_ctrl";
    public static final String PSAM2_PATH = "/sys/dy_control/psam2_en";
    public static final String USB_A2_PATH = "/sys/dy_control/usb_A2_en";
    public static final String TYPE_C_PATH = "/sys/dy_control/TYPE_C_en";
    public static final String POGO_PORT_PATH = "/sys/dy_control/POGO_port_en";
    public static final String SCAN_ENGINE_TRIG_PATH = "/sys/dy_control/scan_engine_trig";
    public static final String ID_CARD_PATH = "/sys/dy_control/ID_card_en";
    public static final String DM9051_RST_CTRL_PATH = "/sys/dy_control/dm9051_rst_ctrl";
    public static final String PSAM1_PATH = "/sys/dy_control/psam1_en";
    public static final String DM9051_EN_CTRL_PATH = "/sys/dy_control/dm9051_en_ctrl";
    public static final String USBB_PATH = "/sys/dy_usbdev_control/usbb_en";
    public static final String USBA_PATH = "/sys/dy_usbdev_control/usba_en";
    public static final String USBFP_PATH = "/sys/dy_usbdev_control/usbfp_en";

    private static IoControl instance = new IoControl();

    private IoControl() {
    }

    public static IoControl getInstance() {
        return instance;
    }

    public enum IOSTATUS {
        DISABLE, ENABLE
    }

    /**
     * Set IO port status
     *
     * @param path
     * @param status
     * @return true=setok,flase=setnok
     */
    public boolean setIoStatus(String path, IOSTATUS status) {
        return write(path, status.ordinal());
    }

    /**
     * Get IO port status
     *
     * @param path
     * @return 0=off,1=on
     */
    public int getIoStatus(String path) {
        return read(path);
    }

    private boolean write(String path, int value) {
        boolean success = false;
        BufferedWriter bufWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path);
            bufWriter = new BufferedWriter(fileWriter);
            bufWriter.write(String.valueOf(value));
            bufWriter.close();
            success = true;
            Log.d(TAG, "write success to value=" + value);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "can't write the " + path);
        } finally {
            try {
                if (null != bufWriter) {
                    bufWriter.close();
                }
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    private int read(String path) {
        String value = "0";
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            value = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
                if (null != fileReader) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "read value=" + value);
        return Integer.parseInt(value);
    }
}

