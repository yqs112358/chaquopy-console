package com.chaquo.python.console;

import android.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends PythonConsoleActivity {

    // On API level 31 and higher, pressing Back in a launcher activity sends it to the back by
    // default, but that would make it difficult to restart the activity.
    @Override public void onBackPressed() {
        finish();
    }

    @Override protected Class<? extends Task> getTaskClass() {
        return Task.class;
    }

    public static class Task extends PythonConsoleActivity.Task {
        public Task(Application app) {
            super(app);
        }

        @Override public void run() {

            py.getModule("main").callAttr("main");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String binaryName = "libadb.so";
        String nativeLibsDir = getApplicationInfo().nativeLibraryDir;
        String binaryPath = nativeLibsDir + File.separator + binaryName;
        String cmd = binaryPath + " --version";

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            int c;
            StringBuilder output = new StringBuilder();
            while ((c = inputStream.read()) != -1) {
                output.append((char) c);
            }
            inputStream.close();
            process.waitFor();
            Toast.makeText(this, output.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
