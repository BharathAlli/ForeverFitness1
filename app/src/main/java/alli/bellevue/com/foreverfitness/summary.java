package alli.bellevue.com.foreverfitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Alli on 02-10-2016.
 */

public class summary extends AppCompatActivity {

    Button weightentry_details, settings, history;
     static SharedPreferences preferences;
     static SqliteDataBase sqliteDataBase;
    List<WightEntryData> wightEntryDatas;
    float datwiseloss;
    EditText bmi,weight_lost_to_date,currentweight, weight_lost_weekly;
    long elapsedDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bmi=(EditText)findViewById(R.id.bmi);
        weight_lost_to_date=(EditText)findViewById(R.id.weight_lost_to_date);
        weight_lost_weekly=(EditText)findViewById(R.id.weight_lost_weekly);
        currentweight=(EditText)findViewById(R.id.current_weight);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqliteDataBase = new SqliteDataBase(this);
        wightEntryDatas = sqliteDataBase.getAllEntrys();

        history=(Button)findViewById(R.id.history);
        settings =  (Button)findViewById(R.id.settings);
        weightentry_details =(Button)findViewById(R.id.weightentry_details);


        weightentry_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary.this, weightentry.class));
                finish();

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary.this, settings.class));
                finish();

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary.this, History.class));
                finish();
            }
        });
        try {
            float weight = Float.parseFloat(getStringFromPreference(WightEntryData.CURRENTWEIGHT));
            float height = Float.parseFloat(getStringFromPreference(WightEntryData.HEIGHT));
            if (height>0);
            height=height/100;
            if (weight>0&&height>0)
                bmi.setText(calculateBMI(weight,height)+"");
            if (getStringFromPreference(WightEntryData.CURRENTWEIGHT).length()>0)
                currentweight.setText(getStringFromPreference(WightEntryData.CURRENTWEIGHT)+"");
        }catch (Exception e)
        {

        }
        if (wightEntryDatas.size()>0) {
            Collections.sort(wightEntryDatas, new Comparator<WightEntryData>() {
                @Override
                public int compare(WightEntryData r1, WightEntryData r2) {
                    return r1.getDate().compareTo(r2.getDate());
                }
            });
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

            try {
                Date startDate=format.parse(wightEntryDatas.get(0).getDate());
                Log.i("ambi","startDate : "+startDate);

                Date endDate=format.parse(wightEntryDatas.get(wightEntryDatas.size()-1).getDate());
                Log.i("ambi","endDate : "+endDate);
                long different = endDate.getTime() - startDate.getTime();
                Log.i("ambi","different : "+different);
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                elapsedDays = (different / daysInMilli);
                different = different % daysInMilli;
                Log.i("ambi","elapsedDays : "+elapsedDays);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            datwiseloss=Float.parseFloat(getStringFromPreference(PreferenceVariabls.BEGININGWEIGHT))-Float.parseFloat(wightEntryDatas.get(wightEntryDatas.size() - 1).getCurentweight());
            weight_lost_to_date.setText(datwiseloss + "");
            weight_lost_weekly.setText(datwiseloss/elapsedDays +"");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
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
    public float calculateBMI (float weight, float height) {
        return (float) (weight / (height * height));
    }

    public static String getStringFromPreference(String key){
        return preferences.getString(key,"");
    }
    public class PreferenceVariabls {
        public static final String BEGININGWEIGHT = "beginingWeight";
        public static final String CURRENTWEIGHT = "currentWeight";
        public static final String HEIGHT = "height";
        public static final String DATEE = "date";
        public static final String GENDER = "gender";
        public static final String TARGETED_WEIGHT = "targetedWeight";
        public static final String TARGETED_DATE = "targetedDate";
        public static final String NAME = "name";

    }
}
