package alli.bellevue.com.foreverfitness;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Alli on 04-10-2016.
 */
public class settings extends AppCompatActivity {
    EditText Name,beginningweight,height,targeted_weight,targeted_date;
    int year,month,day;
    RadioGroup myRadioGroup;
    RadioButton rbtn_male,rbtn_female;
    boolean gender=true;
    public static SharedPreferences preferences;
    public static SqliteDataBase sqliteDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqliteDataBase = new SqliteDataBase(this);
        Name=(EditText)findViewById(R.id.Name);
        beginningweight =(EditText)findViewById(R.id.beginningweight);
        height =(EditText)findViewById(R.id.height);
        targeted_weight =(EditText)findViewById(R.id.targeted_weight);
        targeted_date =(EditText)findViewById(R.id.targeted_date);
        myRadioGroup=(RadioGroup)findViewById(R.id.myRadioGroup);
        rbtn_male=(RadioButton)findViewById(R.id.rbtn_male);
        rbtn_female=(RadioButton)findViewById(R.id.rbtn_female);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        targeted_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(1);
            }
        });
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rbtn_male)
                {
                    gender=true;
                }else
                {
                    gender=false;

                }
            }
        });

    }
    public void saveButton(View view)
    {
        if (Name.getText().toString().trim().length()>0&&beginningweight.getText().toString().trim().length()>0&& height.getText().toString().trim().length()>0
                &&targeted_date.getText().toString().trim().length()>0&&targeted_weight.getText().toString().trim().length()>0)
        {
            savedata();
            Toast.makeText(getApplicationContext(), " All data saved ", Toast.LENGTH_LONG).show();
            Name.setEnabled(false);
            beginningweight.setEnabled(false);
            height.setEnabled(false);
            targeted_date.setEnabled(false);
            targeted_weight.setEnabled(false);

            sqliteDataBase.close();
            Name.setText("");
            beginningweight.setText("");
            height.setText("");
            targeted_date.setText("");
            targeted_weight.setText("");
            return;

        }else
        {
            Toast.makeText(getApplicationContext(), " enter all fields ", Toast.LENGTH_LONG).show();
        }
    }
    public void editButton(View view)
    {
        if (Name.getText().toString().trim().length()>0)
        {
            editdata();
            Name.setEnabled(true);
            beginningweight.setEnabled(true);
            height.setEnabled(true);
            targeted_date.setEnabled(true);
            targeted_weight.setEnabled(true);
        }

    }

    private void savedata() {
        putStringinPreference(summary.PreferenceVariabls.BEGININGWEIGHT, beginningweight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.CURRENTWEIGHT, beginningweight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.HEIGHT, height.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.TARGETED_WEIGHT, targeted_weight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.TARGETED_DATE, targeted_date.getText().toString().trim());
        putBooleanPreference(summary.PreferenceVariabls.GENDER, gender);
        putStringinPreference(summary.PreferenceVariabls.NAME, Name.getText().toString().trim());

    }

    private void editdata()
    {
        putStringinPreference(summary.PreferenceVariabls.BEGININGWEIGHT, beginningweight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.CURRENTWEIGHT, beginningweight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.HEIGHT, height.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.TARGETED_WEIGHT, targeted_weight.getText().toString().trim());
        putStringinPreference(summary.PreferenceVariabls.TARGETED_DATE, targeted_date.getText().toString().trim());
        putBooleanPreference(summary.PreferenceVariabls.GENDER, gender);

        Toast.makeText(getApplicationContext(), " updated ", Toast.LENGTH_LONG).show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:


                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            targeted_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
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
    public static boolean getBooleanFromPreference(String key){
        return preferences.getBoolean(key,false);
    }
    public  void putBooleanPreference(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static void putStringinPreference(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromPreference(String key){
        return preferences.getString(key,"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        beginningweight.setText(getStringFromPreference("beginingWeight"));
        height.setText(getStringFromPreference("height"));
        targeted_weight.setText(getStringFromPreference("targetedWeight"));
        targeted_date.setText(getStringFromPreference("targetedDate"));
        Name.setText(getStringFromPreference("name"));
        if (getBooleanFromPreference("gender"))
        {
            rbtn_male.setChecked(true);
            rbtn_female.setChecked(false);
        }else
        {
            rbtn_female.setChecked(true);
            rbtn_male.setChecked(false);
        }
    }

}
