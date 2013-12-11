package cmu.hci.maintenaid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RequestsDatabaseHelper extends SQLiteOpenHelper {
	static final String dbName="maintenaidDB";
	static final String requestsTable = "Requests";
	static final String colRequestID="_id";
	static final String colRequestName="request_name";
	static final String colRequestPriority="request_priority";
	static final String colRequestDateAdded="request_date_added";
	static final String colRequestDateCompleted="request_date_completed";
	static final String colRequestBuilding="request_building";
	static final String colRequestApartment="request_apartment";
	static final String colRequestDetails="request_details";
	static final String colRequestComments="request_comments";
	static final String colRequestStatus="request_status";

	static final int PRIORITY_NORMAL = 0;
	static final int PRIORITY_HIGH = 1;
	static final int STATUS_INCOMPLETE = 0;
	static final int STATUS_IN_PROGRESS = 1;
	static final int STATUS_COMPLETE = 2;
	
	public RequestsDatabaseHelper(Context context) {
		super(context, dbName, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+requestsTable+"("
				+colRequestID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			        colRequestName+" TEXT, "+
			        	colRequestPriority+" INTEGER, "+
			        	colRequestDateAdded+" STRING, "+
			        	colRequestDateCompleted+" STRING, "+
			        	colRequestBuilding+" TEXT, "+
			        	colRequestApartment+" TEXT, "+
			        	colRequestDetails + " TEXT, "+
			        	colRequestComments + " TEXT, "+
			        	colRequestStatus + " INTEGER"+
			        	")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+requestsTable);
		onCreate(db);
	}

}
