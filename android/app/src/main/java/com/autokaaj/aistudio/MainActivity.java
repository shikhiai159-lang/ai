package com.autokaaj.aistudio;

import android.os.Bundle;
import android.util.Log;
import com.getcapacitor.BridgeActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File outFile = new File(getFilesDir(), "ollama");
                    if (!outFile.exists()) {
                        InputStream in = getAssets().open("ollama");
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        in.close();
                        out.close();
                    }
                    outFile.setExecutable(true);

                    ProcessBuilder processBuilder = new ProcessBuilder(outFile.getAbsolutePath(), "serve");
                    processBuilder.directory(getFilesDir());
                    Process process = processBuilder.start();
                    
                    Log.d("OllamaService", "Ollama background server started successfully!");
                } catch (Exception e) {
                    Log.e("OllamaService", "Error starting Ollama: " + e.getMessage());
                }
            }
        }).start();
    }
}
