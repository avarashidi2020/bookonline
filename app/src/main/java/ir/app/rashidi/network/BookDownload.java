package ir.app.rashidi.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ir.app.rashidi.R;
import ir.app.rashidi.activity.ShowListBookFroDownloadActivity;
import ir.app.rashidi.activity.ShowMayBookActivity;


@SuppressLint("StaticFieldLeak")
public class BookDownload extends AsyncTask<String, Integer, Boolean> {
    private ProgressDialog progressDialog;
    private String fileName;
    private String folder;
    private Context context;
    private static final String TAG = ShowListBookFroDownloadActivity.class.getSimpleName();

    public BookDownload(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lengthOfFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            if (isCancelled())
                return false;
            else{
                 fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);
                ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
                File directory = contextWrapper.getDir(context.getFilesDir().getName(), Context.MODE_PRIVATE);
                 folder =directory.getAbsolutePath() + "/avabook/book/";
                File directory1 = new File(folder);

                if (!directory1.exists()){
                    directory1.mkdirs();
                }
                OutputStream output = new FileOutputStream(folder + fileName);

                byte[] data = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return true;
            }

        } catch (Exception e) {
            Log.e("Error =>",e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);
        progressDialog.setTitle("دانلود کتاب");
        this.progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.i("VAlue =>",values[0] + "");
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            this.progressDialog.dismiss();
            File f = new File(folder, fileName);
            long fileSizeInBytes = f.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;
            int requestID = (int) System.currentTimeMillis();

            NotificationManager mNotificationManager;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, "notify_001");
            Intent ii = new Intent(context, ShowMayBookActivity.class);
            ii.putExtra("storage",folder);
            ii.putExtra("name",fileName);
            ii.putExtra("size",fileSizeInMB);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestID, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.setBigContentTitle("کتابخانه ی رشیدی");
            bigText.setSummaryText("");

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("");
            mBuilder.setContentText("کتاب شما با موفقت دانلود شد");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);

            mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "دانلود",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}
