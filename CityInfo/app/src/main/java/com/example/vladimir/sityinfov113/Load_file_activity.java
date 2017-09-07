package com.example.vladimir.sityinfov113;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

public class Load_file_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_file_activity);
        Button load_file = (Button)findViewById(R.id.load_file_button);

        final ProgressBar progress_bar = (ProgressBar)findViewById(R.id.progressBar2);
        final ThinDownloadManager downloadManager = new ThinDownloadManager(5);

        final DownloadStatusListener downloadListener = new DownloadStatusListener() {
            @Override
            public void onDownloadComplete(int id) {
                progress_bar.setVisibility(View.INVISIBLE);
                Intent complete_download_start_activity_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(complete_download_start_activity_main);
                Log.e("W", "Загрузка завершена");
            }

            @Override
            public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                //тут выводим ошибку при ошибке загрузки файла
                Log.e("W", "Ошибка загрузки");
            }

            @Override
            public void onProgress(int id, long totalBytes, long arg3, int progress) {
                Log.w("W", "ПРОГРЕСС");
            }

        };

        load_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","Load_file_button click");
                Uri downloadUri = Uri.parse("https://drive.google.com/uc?export=download&id=0Bztp3hEbtAAAS0NzRmhwYXNCZ0U"); //адрес скачиваемого файла
                Uri destinationUri = Uri.parse( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/123"); // директория скачивания
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri).setDestinationURI(destinationUri);
                downloadManager.add(downloadRequest);
                progress_bar.setVisibility(View.VISIBLE);
                downloadRequest.setDownloadListener(downloadListener);
                //downloadRequest.cancel();
            }
        });
    }
}
