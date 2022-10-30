package com.bencarlisle15.terminalhomelauncher.commands.main.raw;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bencarlisle15.terminalhomelauncher.LauncherActivity;
import com.bencarlisle15.terminalhomelauncher.R;
import com.bencarlisle15.terminalhomelauncher.commands.CommandAbstraction;
import com.bencarlisle15.terminalhomelauncher.commands.ExecutePack;

public class Flash implements CommandAbstraction {

    private static boolean isTorchOn = false;

    public static void modifyTorchMode(Context context, boolean turnOn) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = null;
            for (String currentCameraId: cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(currentCameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    cameraId = currentCameraId;
                    break;
                }
            }
            if (cameraId == null) {
                return;
            }
            cameraManager.setTorchMode(cameraId, turnOn);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String exec(ExecutePack pack) {
        if (ContextCompat.checkSelfPermission(pack.context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) pack.context, new String[]{Manifest.permission.CAMERA}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return pack.context.getString(R.string.output_waitingpermission);
        }

        isTorchOn ^= true;

        modifyTorchMode(pack.context, isTorchOn);

        return null;
    }

    @Override
    public int helpRes() {
        return R.string.help_flash;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

}
