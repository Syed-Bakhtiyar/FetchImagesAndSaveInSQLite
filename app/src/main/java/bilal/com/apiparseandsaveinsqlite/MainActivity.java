package bilal.com.apiparseandsaveinsqlite;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;

import bilal.com.apiparseandsaveinsqlite.Database.SQLiteDB;
import bilal.com.apiparseandsaveinsqlite.For_Custom_Adapters.MyCustomAdapter;

public class MainActivity extends AppCompatActivity {

    SQLiteDB sqLiteDB;

    AsyncHttpClient asyncHttpClient;

    private DownloadManager downloadManager;

    ProgressDialog progressDialog;

    File folder;

    File[] files;

    Button button;

    ArrayList<File> arrayList;

    GridView gridView;


    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadManager = (DownloadManager) getSystemService(Activity.DOWNLOAD_SERVICE);

        progressDialog = new ProgressDialog(this);

        gridView = (GridView) findViewById(R.id.grid);

        progressDialog.setMessage("Please wait");

        asyncHttpClient = new AsyncHttpClient();

        arrayList = new ArrayList<>();


        button = (Button) findViewById(R.id.showdata);

        button.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {

                        arrayList.add(files[i]);

//                        System.out.println("File " + files[i].getName());
                    }

                }

                myCustomAdapter = new MyCustomAdapter(arrayList,getApplicationContext());

                gridView.setAdapter(myCustomAdapter);



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


                        try{

                            JSONArray jsonArray = response.getJSONArray("details");

                            for(int i=0 ; i<jsonArray.length();i++){

                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                String value = jsonObject.getString("image");


                                Uri uri = Uri.parse(value);

                                File ff = new File(String.valueOf(value));

                                DownloadManager.Request request = new DownloadManager.Request(uri);



                                request.setDescription("Downloading.. " + ff.getName()).setTitle("Wait");


                                Toast.makeText(getApplicationContext(), "Downloading" , Toast.LENGTH_SHORT).show();

                                request.setDestinationInExternalPublicDir("/Hello/images", ff.getName());


                                request.setVisibleInDownloadsUi(true);

                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                                        | DownloadManager.Request.NETWORK_MOBILE);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                downloadManager.enqueue(request);




                                Log.d("data ", "onSuccess: "+value);

                                myfunction(value);



                            }

                            folder =  new File(Environment.getRootDirectory()+ "/Hello/images");

                            if(folder.isDirectory()){

                                Log.d("logd", "onSuccess: "+folder.listFiles().toString());

                            }

//                            files = folder.listFiles();
//
//                            button.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();

                        }catch (Exception e){


                            Log.d("error", "onSuccess: "+e);

                            progressDialog.dismiss();

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




    public void myfunction(String uri){


        sqLiteDB = new SQLiteDB(this);

        if(sqLiteDB.save("hello")){

            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Not Inserted", Toast.LENGTH_SHORT).show();

        }


    }




}
