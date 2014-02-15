package sparkjolt.contacts;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

public class Adapter {
	static final String KEY_ROWID = "id";
	static final String KEY_NAME = "name";
	static final String KEY_EMAIL = "email";
	static final String KEY_PHONE = "phone";
	static final String TAG = "Adapter";
	
	static final String DATABASE_NAME = "Database";
	static final String DATABASE_TABLE = "contacts";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = "create table contacts (id integer primary key autoincrement, name text not null, email text, phone text)";
	final Context context;
	DatabaseHelper helper;
	public static SQLiteDatabase db;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}
	
	public Adapter(Context context) {
		this.context = context;
		helper = new DatabaseHelper(context);
	}
	
	public Adapter open() throws SQLException {
		db = helper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		helper.close();
	}
	
	public long insertContact(String name, String email, String phone) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_PHONE, phone);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public void deleteAllContacts() {
		db.execSQL("DROP TABLE IF EXISTS contacts");
		helper.onCreate(db);
	}
	
	public boolean deleteContact(long rowID) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor getAllContacts() {
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL, KEY_PHONE}, null, null, null, null, KEY_NAME);
	}
	
	public Cursor searchContacts(String keyword) {
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL, KEY_PHONE}, "NAME LIKE '%" + keyword + "%'", null, null, null, KEY_NAME);
	}
	
	public Cursor getContact(long rowID) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_EMAIL, KEY_PHONE}, KEY_ROWID + "=" + rowID, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateContact(long rowID, String name, String email, String phone) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_EMAIL, email);
		args.put(KEY_PHONE, phone);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;
	}
}
