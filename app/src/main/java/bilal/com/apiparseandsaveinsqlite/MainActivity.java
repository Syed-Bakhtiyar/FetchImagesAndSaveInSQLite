package bilal.com.apiparseandsaveinsqlite;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.logging.LogRecord;

import bilal.com.apiparseandsaveinsqlite.Database.SQLiteDB;
import bilal.com.apiparseandsaveinsqlite.For_Custom_Adapters.MyCustomAdapter;

public class MainActivity extends AppCompatActivity {

    SQLiteDB sqLiteDB;

    ArrayList<Long> list = new ArrayList<>();

    AsyncHttpClient asyncHttpClient;

    private DownloadManager downloadManager;

    ProgressDialog progressDialog;

    File folder;

    File[] files;

    Button button;

    ArrayList<File> arrayList;

    GridView gridView;

    private long refid;

    String api_file_name[];

    ArrayList<String> directory_file_name;

    MyCustomAdapter myCustomAdapter;

    double num_of_download, answer = 0, file_size;

    int per =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        this.deleteDatabase(SQLiteDB.DB_NAME);

        downloadManager = (DownloadManager) getSystemService(Activity.DOWNLOAD_SERVICE);

        directory_file_name = new ArrayList<>();

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        progressDialog = new ProgressDialog(this);

        gridView = (GridView) findViewById(R.id.grid);

        progressDialog.setMessage("Please wait "+per +"%");

        progressDialog.setCanceledOnTouchOutside(false);

        asyncHttpClient = new AsyncHttpClient();

        arrayList = new ArrayList<>();

        myCustomAdapter = new MyCustomAdapter(arrayList,getApplicationContext());


        button = (Button) findViewById(R.id.showdata);

        final File file = new File(Environment.getExternalStorageDirectory().toString() + "/Hello/images");

        if(file.isDirectory()){

//            File[] ff = file.listFiles();
//
//
//            for (File f : ff){
//
////                Toast.makeText(this, f.getPath(), Toast.LENGTH_SHORT).show();
//
//                f.delete();
//
//
//
//            }
//
//
//            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
//            file.delete();

            button.setVisibility(View.VISIBLE);

        }else {

            button.setVisibility(View.GONE);

        }


//        button.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gridView.setAdapter(null);
                try {
                    String path = Environment.getExternalStorageDirectory().toString() + "/Hello/images";
                    Log.d("Files", "Path: " + path);
                    File directory = new File(path);
                    File[] files = directory.listFiles();
                    Log.d("Files", "Size: " + files.length);
                    for (int i = 0; i < files.length; i++) {
                        Log.d("Files", "FileName:" + files[i].getName());

                        arrayList.add(files[i]);

                        myCustomAdapter.notifyDataSetChanged();

                        gridView.setAdapter(myCustomAdapter);


                    }
                }catch (Exception e){



                    Log.d("Error", "File Not found: "+e);
                }




            }
        });

        findViewById(R.id.fetch_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                asyncHttpClient.get("http://storeperfect.colgate-palmolive.com.pk/stagingikode/ApiNew/index.php/defaultCall/getImagePlanogram",null, new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);


                        if (Build.VERSION.SDK_INT >= 23) {


                            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                Log.v("permission", "Permission is granted");



                                return;
                            }


                            //do your check here
                        }




                            try{

                            JSONArray jsonArray = response.getJSONArray("details");

                            file_size = jsonArray.length();

                            num_of_download = jsonArray.length();

                                api_file_name = new String[jsonArray.length()];

                            for(int i=0 ; i<jsonArray.length();i++){



                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                String value = jsonObject.getString("image");


                                Uri uri = Uri.parse(value);

                                File ff = new File(String.valueOf(value));

//                                Log.d("uripath", "onSuccess: "+ff.getName());

                                api_file_name[i] = value;

                                DownloadManager.Request request = new DownloadManager.Request(uri);



                                request.setDescription("Downloading.. " + ff.getName()).setTitle("Wait");




//                                Toast.makeText(getApplicationContext(), "Downloading" , Toast.LENGTH_SHORT).show();

                                request.setDestinationInExternalPublicDir("/Hello/images", ff.getName());


                                request.setVisibleInDownloadsUi(true);

                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                        | DownloadManager.Request.NETWORK_MOBILE);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                                downloadManager.enqueue(request);


                               refid = downloadManager.enqueue(request);


                                list.add(refid);

//                                Log.d("data ", "onSuccess: "+value);



                            }


                            Log.d("ab", "onSuccess: "+jsonArray.length()+" arraylist "+list.size());
                            button.setVisibility(View.VISIBLE);

                        }catch (Exception e){


                            Log.d("error", "onSuccess: "+e);

                        }



                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }
                });





            }
        });







    }




    public void myfunction(String link){


        sqLiteDB = new SQLiteDB(this);

        if(sqLiteDB.save(link)){

//            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();

            Log.d("Inserted", "inserted");

        } else {

//            Toast.makeText(this, "Not Inserted", Toast.LENGTH_SHORT).show();

            Log.d("not_insert", "not inserted");

        }


    }

//    private boolean validDownload(long downloadId, DownloadManager dMgr) {
//
//        Log.d("ok","Checking download status for id: " + downloadId);
//
//        //Verify if download is a success
//        Cursor c= dMgr.query(new DownloadManager.Query().setFilterById(downloadId));
//
//        if(c.moveToFirst()){
//            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//
//            if(status == DownloadManager.STATUS_SUCCESSFUL){
//
//                String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
//
//
//
////                arrayList.add(new File(filePath));
////
////                myCustomAdapter.notifyDataSetChanged();
//
//                Log.d("MyPath", "validDownload: "+filePath);
//
////                title = filePath.substring( filePath.lastIndexOf('/')+1, filePath.length() );
//
//
//                return true; //Download is valid, celebrate
//            }else{
//                int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
//                Log.d("not ok", "Download not correct, status [" + status + "] reason [" + reason + "]");
//                return false;
//            }
//        }
//        return false;
//    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //    async Class

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //    async Class

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////



    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {




            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


//            list.add(referenceId);


            Log.e("IN", "" + referenceId);

           list.remove(referenceId);

            num_of_download -= 1 ;

            answer = num_of_download*file_size / 100;



            progressDialog.setMessage("Please wait "+(100-answer) +"%");

            if (list.isEmpty())
            {


                ////////////////////////////////////////////////////



                // Get Directory path

                try {
                    String path = Environment.getExternalStorageDirectory().toString() + "/Hello/images";
                    Log.d("Files", "Path: " + path);
                    File directory = new File(path);
                    File[] files = directory.listFiles();
                    Log.d("Files", "Size: " + files.length);
                    for (int i = 0; i < files.length; i++) {
                        Log.d("Files", "FileName:" + files[i].getName());

//                        arrayList.add(files[i]);

                        directory_file_name.add(files[i].getName());

                        Log.d("path","Abolute_path"+files[i].getAbsolutePath() +" Only_path "+files[i].getPath());

//                        myfunction(files[i].getAbsolutePath());

                        myfunction(files[i].getPath());


//                        myCustomAdapter.notifyDataSetChanged();
//
//                        gridView.setAdapter(myCustomAdapter);


                    }
                }catch (Exception e){



                    Log.d("Error", "File Not found: "+e);
                }

                try {
                    backupDatabase();
                } catch (Exception e) {
                    Log.d("error", "onReceive: " + e);
                }


////////////////////////////////////////////////////////////



                /*


                Comaprison between api and directory path


                 */







                ////////////////////////////////////////////////////////



                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("GadgetSaint")
                                .setContentText("All Download completed");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());



                progressDialog.dismiss();

            }

        }
    };


    @Override
    protected void onDestroy() {


        super.onDestroy();

        unregisterReceiver(onComplete);


    }


    public void backupDatabase() throws IOException
    {
        boolean success =true;
        File file = null;
        file = new File(Environment.getExternalStorageDirectory() +"/StorePerfectApp/database");

        if (file.exists()) {
            success = true;
        } else {
            success = file.mkdirs();
        }

        if (success)
        {
//            String inFileName = "/data/data/" + getPackageName() + "/databases/StorePerfectDB";
            String inFileName = "/data/data/bilal.com.apiparseandsaveinsqlite/databases/Mydb";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory()+"/Apiparse/database/localDB.db";

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer))>0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        }
    }



    }












///////////////////////////////////// Broad Cast Reciever















//                                DownloadManager manager
//                                        = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//
//                                DownloadManager.Request request = new DownloadManager.Request(uri);
//
//
//                                 long id = manager.enqueue(request
//                                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
//                                                DownloadManager.Request.NETWORK_MOBILE)
//                                        .setAllowedOverRoaming(false)
//                                        .setTitle("Please Wait")
//                                        .setDescription(ff.getName())
//                                        .setDestinationInExternalPublicDir("/Hello/images", ff.getName())
//                                        .setShowRunningNotification(true));
//
//                                if(validDownload(id,manager)){
//
//                                    myfunction(uri.toString());
//
//
//                                }



/*


class myclass extends AsyncTask<Uri,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


            Log.d("Done", "Downloaded ");

//            Toast.makeText(MainActivity.this, "Download is done", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected Boolean doInBackground(Uri... uri) {

            File ff = new File(String.valueOf(String.valueOf(uri[0].toString())));

            DownloadManager manager
                    = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Request request = new DownloadManager.Request(uri[0]);


            long id = manager.enqueue(request
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                            DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Please Wait")
                    .setDescription(ff.getName())
                    .setDestinationInExternalPublicDir("/Hello/images", ff.getName())
                    .setShowRunningNotification(true));


            if(validDownload(id,manager)){

                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();


//                myfunction(uri[0].toString());


            }


//            File ff = new File(String.valueOf(String.valueOf(uri[0].toString())));
//
//
//            DownloadManager.Request request = new DownloadManager.Request(uri[0]);
//
//
//
//            request.setDescription("Downloading.. " + ff.getName()).setTitle("Wait");
//
//
//
//
//            Toast.makeText(getApplicationContext(), "Downloading" , Toast.LENGTH_SHORT).show();
//
//            request.setDestinationInExternalPublicDir("/Hello/images", ff.getName());
//
//
//            request.setVisibleInDownloadsUi(true);
//
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
//                    | DownloadManager.Request.NETWORK_MOBILE);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            downloadManager.enqueue(request);
//




            return null;
        }
    }





 */

/*

            Thread

            class MyThread extends Thread{

       JSONArray jsonArray;

        public MyThread (JSONArray jsonArray){

            this.jsonArray = jsonArray;


        }

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {

                Toast.makeText(getApplicationContext(), "Downloading", Toast.LENGTH_SHORT).show();


                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
            }
        };


        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    String value = jsonObject.getString("image");


                    Uri uri = Uri.parse(value);

                    File ff = new File(String.valueOf(value));

                    DownloadManager.Request request = new DownloadManager.Request(uri);


                    request.setDescription("Downloading.. " + ff.getName()).setTitle("Wait");



                    request.setDestinationInExternalPublicDir("/Hello/images", ff.getName());


                    request.setVisibleInDownloadsUi(true);

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    downloadManager.enqueue(request);


                }
            }catch (Exception e){

                Log.d("exc", "run: "+e);

            }
        }
    }
.





 */