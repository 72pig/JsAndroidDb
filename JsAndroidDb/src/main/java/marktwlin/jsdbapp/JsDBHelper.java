package marktwlin.jsdbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsDBHelper extends SQLiteOpenHelper {
	private static final String dbName = "data.db";
	private static String dbPath;
	private Context context;
	private SQLiteDatabase database = null;

	public JsDBHelper(Context context) {
		super(context, dbName, null, 1);
		this.context = context;
		this.dbPath = "/data/data/" + context.getPackageName() + "/";
		initDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public int getTotalCounter(String table) {
		String sql = "select count(*) from " + table;
		Cursor cursor = database.rawQuery(sql, null);
		int totalCounter = 0;
		if (cursor.moveToNext()) {
			totalCounter = cursor.getInt(0);
		}
		cursor.close();
		return totalCounter;
	}

	public String[][] query(String sql) {
		Cursor cursor = database.rawQuery(sql, null);

		int sizeResult = cursor.getCount();
		int sizeColumn = cursor.getColumnCount();
		String result[][] = new String[sizeResult][sizeColumn];

		int idxResult = 0;
		while (cursor.moveToNext()) {
			for (int idx = 0; idx < sizeColumn; ++idx) {
				result[idxResult][idx] = cursor.getString(idx);
			}
			idxResult++;
		}

		cursor.close();
		return result;
	}

	public long insert(String table, String[] columns, String[] values) {
		ContentValues cv = new ContentValues();
		for (int idx = 0; idx < columns.length; idx ++) {
			cv.put(columns[idx], values[idx]);
		}
		return database.insert(table, null, cv);
	}

	public long update(String table, String[] columns, String[] values, String whereCondition) {
		ContentValues cv = new ContentValues();
		for (int idx = 0; idx < columns.length; idx ++) {
			cv.put(columns[idx], values[idx]);
		}
		return database.update(table, cv, whereCondition, null);
	}

	public long delete(String table, String whereCondition) {
		return database.delete(table, whereCondition, null);
	}

	public void close() {
		if (this.database != null)
			this.database.close();
		super.close();
	}

	private void initDatabase() {
		if (checkDatabase()) {
			this.database = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
			return ;
		}

		this.getReadableDatabase();
		try {
			copyDatabase(dbName, dbPath + dbName);
			System.out.println("copy files");
			this.database = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}

	private boolean checkDatabase() {
		File file = new File(dbPath + dbName);
		if (!file.exists()) {
			System.out.println(file.getAbsolutePath() + " file exists");
			return false;
		}
		SQLiteDatabase database = null;
		try {
			String path = dbPath + dbName;
			System.out.println(path);
			database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			return database != null;
		} catch (SQLiteCantOpenDatabaseException ex) {
			file.delete();
			return false;
		} catch (SQLiteException e) {
			return false;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	private void copyDatabase(String sourceFilePath, String targetFilePath) throws IOException {
		InputStream is = context.getAssets().open(sourceFilePath);
		OutputStream os = new FileOutputStream(targetFilePath);

		byte[] buffer = new byte[4096];
		int length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}

		os.flush();
		os.close();
		is.close();
	}

}