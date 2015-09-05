package de.derandroidpro.downloadfiletutorial;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    Button btn;
    ImageView imageView;
    ProgressDialog progressDialog;
    String urlstring = "https://dl.dropboxusercontent.com/s/taylyy4cw7dscjh/derandroidpro_kanallogo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Datei wird heruntergeladen...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadFile();
                    }
                }).start();

            }
        });
    }

    private void downloadFile() {

        try {
            URL fileurl = new URL(urlstring);
            URLConnection urlConnection = fileurl.openConnection();
            urlConnection.connect();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream(),8192);

            File downloadordner = new File(Environment.getExternalStorageDirectory(), "Download");
            if(!downloadordner.exists()){
                downloadordner.mkdirs();
            }

            File downloadedFile = new File(downloadordner, "download_tutorial" + System.currentTimeMillis() + ".png");
            OutputStream outputStream = new FileOutputStream(downloadedFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            final Bitmap bitmap = BitmapFactory.decodeFile(downloadedFile.getAbsolutePath());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                    progressDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Es ist ein Fehler aufgetreten!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }

    }


}
