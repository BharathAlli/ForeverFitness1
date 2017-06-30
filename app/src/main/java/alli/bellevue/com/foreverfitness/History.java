package alli.bellevue.com.foreverfitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alli on 01-10-2016.
 */
public class History extends AppCompatActivity {
    ListView listView;
    ImageView imageView;
    public static SharedPreferences preferences;
    public static SqliteDataBase sqliteDataBase;
    List<WightEntryData> wightEntryDatas;
    ListData listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.list_view);
        sqliteDataBase=new SqliteDataBase(History.this);
        wightEntryDatas = sqliteDataBase.getAllEntrys();
        Collections.sort(wightEntryDatas, new Comparator<WightEntryData>() {
            @Override
            public int compare(WightEntryData r1, WightEntryData r2) {
                return r1.getDate().compareTo(r2.getDate());
            }
        });
        listData=new ListData(wightEntryDatas);
        listView.setAdapter(listData);
    }
    class ListData extends BaseAdapter
    {
        TextView delete, date, weight, loss;
        List<WightEntryData> entry;
        ListData(List<WightEntryData> entry)
        {
            this.entry = entry;

        }
        @Override
        public int getCount() {
            return entry.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.history_data,null);



            delete =(TextView)v.findViewById(R.id.delete);
            date =(TextView)v.findViewById(R.id.date);
            weight =(TextView)v.findViewById(R.id.weight);
            loss =(TextView)v.findViewById(R.id.loss);
            imageView=(ImageView)v.findViewById(R.id.imageview);
            date.setText("Date: "+entry.get(position).getDate()+"");
            weight.setText("Weight : "+entry.get(position).getCurentweight()+"");
            loss.setText("Loss : "+entry.get(position).getWeight_loss());

            Bitmap bitmap = getBitmapObject(entry.get(position).getImagePath());
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setBackgroundResource(R.drawable.anyone);
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = entry.get(position).get_id();
                    int deleted = sqliteDataBase.deleteContact(id);
                    if (deleted > 0) {
                        entry.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            return v;
        }
    }
      public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, settings.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingsIntent);
                return true;


            case R.id.action_home:
                Intent homeIntent = new Intent(this, summary.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }
    private Bitmap getBitmapObject(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;

                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }
}
