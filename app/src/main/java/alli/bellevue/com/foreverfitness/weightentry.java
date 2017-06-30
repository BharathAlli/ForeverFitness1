package alli.bellevue.com.foreverfitness;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alli on 01-10-2016.
 */
public class weightentry extends AppCompatActivity {
    private DatePicker datePicker;
    static String imgPath;
    private Calendar calendar;
    private int year, month, day;
    static SharedPreferences preferences;
    static SqliteDataBase sqliteDataBase;
    List<WightEntryData> wightEntryDatas;
    EditText currentweight,date;
    ImageView imageView;
    Button button_cancel,button_save;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weight_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqliteDataBase = new SqliteDataBase(this);
        date = (EditText) findViewById(R.id.date);
        currentweight=(EditText)findViewById(R.id.currentweight);
        imageView=(ImageView)findViewById(R.id.imageview);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        button_cancel=(Button)findViewById(R.id.button_cancel);
        button_save=(Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wightEntryDatas = sqliteDataBase.getAllEntrys();

                if (wightEntryDatas.size()>0) {
                    Collections.sort(wightEntryDatas, new Comparator<WightEntryData>() {
                        @Override
                        public int compare(WightEntryData r1, WightEntryData r2) {
                            return r1.getDate().compareTo(r2.getDate());
                        }
                    });
                }

                if (date.getText().toString().length()>0&& currentweight.getText().toString().length()>0 )
                {
                    if (getStringFromPreference(summary.PreferenceVariabls.BEGININGWEIGHT).length()>0&&getStringFromPreference(summary.PreferenceVariabls.CURRENTWEIGHT).length()>0) {
                        putStringinPreference(summary.PreferenceVariabls.CURRENTWEIGHT, currentweight.getText().toString().trim());
                        putStringinPreference(summary.PreferenceVariabls.DATEE, date.getText().toString().trim());
                        Toast.makeText(getApplicationContext(), "data Saved", Toast.LENGTH_LONG).show();

                        float w_loss;
                        if (wightEntryDatas.size() == 0) {
                            w_loss = (Float.parseFloat(getStringFromPreference(summary.PreferenceVariabls.BEGININGWEIGHT))) - (Float.parseFloat(getStringFromPreference(summary.PreferenceVariabls.CURRENTWEIGHT)));
                        } else {
                            w_loss = (Float.parseFloat(wightEntryDatas.get(wightEntryDatas.size() - 1).getCurentweight())) - (Float.parseFloat(getStringFromPreference(summary.PreferenceVariabls.CURRENTWEIGHT)));
                        }
                        sqliteDataBase.addEntry(new WightEntryData(getStringFromPreference(summary.PreferenceVariabls.CURRENTWEIGHT), getStringFromPreference(summary.PreferenceVariabls.DATEE), w_loss + "", imgPath));
                        currentweight.setText("");
                        date.setText("");
                        Toast.makeText(getApplicationContext()," Data saved ",Toast.LENGTH_LONG).show();

                        imageView.setBackgroundResource(R.drawable.anyone);
                    }else
                    {
                        Toast.makeText(getApplicationContext()," Please enter all fields ",Toast.LENGTH_LONG).show();

                    }

                }else
                {
                    Toast.makeText(getApplicationContext()," Please enter all fields ",Toast.LENGTH_LONG).show();
                }

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setText("");
                currentweight.setText("");
                imageView.setBackgroundResource(R.drawable.anyone);
            }
        });
    }
    public void capture(View view)
    {
        if (weightentry.isPermissionGranted(android.Manifest.permission.CAMERA,getApplicationContext())&& weightentry.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE,getApplicationContext())) {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, 2);
            selectImage();
        }else
        {
            ActivityCompat.requestPermissions(weightentry.this,
                    new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            Toast.makeText(getApplicationContext(),"Camera permission reuired",Toast.LENGTH_LONG).show();
        }
    }
    public static boolean isPermissionGranted(String permission,Context context) {
        int status = context.getPackageManager().checkPermission(permission, context.getPackageName());
        return status == PackageManager.PERMISSION_GRANTED;
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "calender", Toast.LENGTH_SHORT)
                .show();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(weightentry.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    //userChoosenTask="Take Photo";
                    if (weightentry.isPermissionGranted(android.Manifest.permission.CAMERA,getApplicationContext())&& weightentry.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE,getApplicationContext())&&weightentry.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,getApplicationContext()))
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    // userChoosenTask="Choose from Library";
                    if (weightentry.isPermissionGranted(android.Manifest.permission.CAMERA,getApplicationContext())&&
                            weightentry.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE,getApplicationContext())&&weightentry.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,getApplicationContext()))
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    private void galleryIntent()
    {
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }
    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };
    private void showDate(int year, int month, int day) {
        date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


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
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
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

            // Show selected date
            date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode==1) {
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();

                imgPath = cursor.getString(column_index_data);
                Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmapImage);
            }else
            {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                imgPath = c.getString(columnIndex);
                File file =new File(imgPath);
                c.close();
                Bitmap bm = decodeSampledBitmapFromUri(imgPath, 220, 220);
                imageView.setImageBitmap(bm);               // imageView.setImageURI(Uri.fromFile(file));

            }
        }
    }
    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }
    public static void putStringinPreference(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromPreference(String key){
        return preferences.getString(key,"");
    }

}