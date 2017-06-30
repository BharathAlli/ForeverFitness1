package alli.bellevue.com.foreverfitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDataBase extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "Fitness";

	private static final String WEIGHT_TABLE = "weightTable";

	private static final String NAME = "name";
	private static final String CURRENT_WEIGHT = "currentWeight";
	private static final String DATE = "dateoftheweight";
	private static final String WEIGHT_LOSS = "loss";
	private static final String IMAGE = "picture";

	public SqliteDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + WEIGHT_TABLE + "("
				+ NAME + " TEXT," + CURRENT_WEIGHT + " TEXT,"
				+ WEIGHT_LOSS + " TEXT,"+ DATE + " TEXT,"+ IMAGE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}


	void addEntry(WightEntryData wightEntryData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CURRENT_WEIGHT, wightEntryData.getCurentweight());
		values.put(DATE, wightEntryData.getDate());
		values.put(WEIGHT_LOSS, wightEntryData.getWeight_loss());
		values.put(IMAGE, wightEntryData.getImagePath());

		db.insert(WEIGHT_TABLE, null, values);
		db.close();
	}


	public List<WightEntryData> getAllEntrys() {
		List<WightEntryData> contactList = new ArrayList<WightEntryData>();
		String selectQuery = "SELECT  * FROM " + WEIGHT_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				WightEntryData wightEntryData = new WightEntryData();
				wightEntryData.set_id(Integer.parseInt(cursor.getString(0)));
				wightEntryData.setCurentweight(cursor.getString(1));
				wightEntryData.setWeight_loss(cursor.getString(2));
				wightEntryData.setDate(cursor.getString(3));
				wightEntryData.setImagePath(cursor.getString(4));

				contactList.add(wightEntryData);
			} while (cursor.moveToNext());
		}

		return contactList;
	}


	public int deleteContact(int id) {
		int isDeleted=0;
		SQLiteDatabase db = this.getWritableDatabase();
		isDeleted=db.delete(WEIGHT_TABLE, NAME + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
		return isDeleted;
	}

}
