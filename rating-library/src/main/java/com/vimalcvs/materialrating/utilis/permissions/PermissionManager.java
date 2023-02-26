// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.vimalcvs.materialrating.utilis.permissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PermissionManager {

    public static final int REQUEST_CODE = 101;
    public static final List<String> LOCATION_PERMISSIONS = new ArrayList<>(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION));
    public static final List<String> STORAGE_PERMISSIONS = new ArrayList<>(Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    public static final List<String> NETWORK_PERMISSIONS = new ArrayList<>(Arrays.asList(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE));
    public static final List<String> CAMERA_PERMISSIONS = new ArrayList<>(Collections.singletonList(Manifest.permission.CAMERA));
    public static final List<String> CALL_PHONE_PERMISSIONS = new ArrayList<>(Collections.singletonList(Manifest.permission.CALL_PHONE));


    private final String[] permissions = null;

    Context appContext;


    public PermissionManager(Context context) {
        this.appContext = context;
    }

    public boolean checkStoragePermission() {
        return checkPermissions(STORAGE_PERMISSIONS);
    }

    public boolean checkPermissions(List<String> permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(appContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;//false


            }
        }
        return true;
    }

    public boolean checkManagerStorage(Context activity) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestStoragePermissions(AppCompatActivity activity) {
        List<String> permissions = new ArrayList<>(STORAGE_PERMISSIONS);

        if (!checkPermissions(permissions))
            requestPermissions(activity, permissions);

    }

    private void requestPermissions(AppCompatActivity activity, List<String> permissions) {
        boolean showDialog = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showDialog = true;
                break;
            }
        }
        if (showDialog) {
            ActivityCompat.requestPermissions(activity,
                    permissions.toArray(new String[0]),
                    REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), REQUEST_CODE);
        }
    }


    public String[] getPermissions() {
        return permissions;
    }
}
