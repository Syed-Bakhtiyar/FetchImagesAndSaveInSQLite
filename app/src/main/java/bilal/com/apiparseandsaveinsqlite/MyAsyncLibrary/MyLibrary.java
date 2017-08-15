package bilal.com.apiparseandsaveinsqlite.MyAsyncLibrary;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.mime.Header;

/**
 * Created by BILAL on 8/15/2017.
 */

public class MyLibrary {

    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void get(){

        asyncHttpClient.get("http://storeperfect.colgate-palmolive.com.pk/stagingikode/ApiNew/index.php/defaultCall/getImagePlanogram",null, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);




            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });


    }


}
