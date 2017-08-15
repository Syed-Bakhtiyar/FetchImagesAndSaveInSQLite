package bilal.com.apiparseandsaveinsqlite.For_Custom_Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import bilal.com.apiparseandsaveinsqlite.R;

/**
 * Created by BILAL on 8/15/2017.
 */

public class MyCustomAdapter extends BaseAdapter {

    ArrayList<File> arrayList;

    Context context;

    ImageView imageView;

    LayoutInflater inflater;

    public MyCustomAdapter(ArrayList<File> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.from(context).inflate(R.layout.my_image_list,viewGroup,false);




        Bitmap myBitmap = BitmapFactory.decodeFile(arrayList.get(i).getAbsolutePath());

        imageView = (ImageView) view.findViewById(R.id.img);

        imageView.setImageBitmap(myBitmap);




        return view;
    }
}
