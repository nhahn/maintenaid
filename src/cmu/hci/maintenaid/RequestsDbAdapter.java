package cmu.hci.maintenaid;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RequestsDbAdapter {
	private Context context;
	private SQLiteDatabase database;
	private RequestsDatabaseHelper dbHelper;

	public RequestsDbAdapter(Context context) {
		this.context = context;
	}

	public RequestsDbAdapter open() throws SQLException {
		dbHelper = new RequestsDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	
/**
	 * Create a new Request.
	 * If the Request is successfully created return the new
	 * Request for that note, otherwise return a -1 to indicate failure.
	 */

	public long createRequest(String name, int priority, String dateSubmitted, String dateCompleted,
			String building, String apartment, String details, String comments, int status) {
		ContentValues initialValues = createContentValues(name, priority,dateSubmitted, dateCompleted,
				building, apartment, details, comments, status);
		return database.insert(RequestsDatabaseHelper.requestsTable, null, initialValues);
	}

	
	/**
	 * Update the Request.
	 * @return true if at least one row affected.
	 */

	public boolean updateRequest(long rowId, String name, int priority, String dateAdded, String dateCompleted,
			String building, String apartment, String details, String comments, int status) {
		ContentValues updateValues = createContentValues(name, priority, dateAdded, dateCompleted,
				building, apartment, details, comments, status);

		return database.update(RequestsDatabaseHelper.requestsTable, updateValues, RequestsDatabaseHelper.colRequestID + "="
				+ rowId, null) > 0;
	}

	
	/**
	 * Deletes Request
	 */

	public boolean deleteRequest(long rowId) {
		return database.delete(RequestsDatabaseHelper.requestsTable, RequestsDatabaseHelper.colRequestID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete all Requests
	 * Returns TRUE if any rows were deleted.
	 */

	public boolean deleteAllRequests() {
		return database.delete(RequestsDatabaseHelper.requestsTable, null, null) > 0;
	}

	
	/**
	 * Return a Cursor over the list of all Requests in the database sorted by Date
	 * 
	 * @return Cursor over all Requests
	 */

	public Cursor fetchAllIncompleteRequestsOrderByDate() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_INCOMPLETE, 
			null, 
			null,
			null, 
			RequestsDatabaseHelper.colRequestDateAdded + " ASC" // top result is oldest pending request (result with the earliest add date)
		);
	}
	
	/**
	 * Return a Cursor over the list of all Requests in the database sorted by Priority
	 * 
	 * @return Cursor over all Requests
	 */

	public Cursor fetchAllIncompleteRequestsOrderByPriority() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_INCOMPLETE, 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestPriority + " DESC"
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all incomplete Requests in the database for the given building, sorted by date
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllIncompleteRequestsForBuildingOrderByDate(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_INCOMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateAdded + " ASC" // top result is oldest pending request (result with the earliest add date)
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all incomplete Requests in the database for the given building, sorted by date
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllIncompleteRequestsForBuildingOrderByPriority(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus+ " = " + RequestsDatabaseHelper.STATUS_INCOMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestPriority + " DESC"
		);
		
	}

	/**
	 * Return a Cursor over the list of all In Progress Requests in the database sorted by date.
	 * 
	 * @return Cursor over all In Progress requests, sorted by date.
	 */
	public Cursor fetchAllInProgressRequestsOrderByDate() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_IN_PROGRESS, 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateAdded + " ASC" // top result is oldest pending request (result with the earliest add date)
		);
	}
	
	/**
	 * Return a Cursor over the list of all In Progress Requests in the database sorted by priority.
	 * 
	 * @return Cursor over all In Progress requests, sorted by priority.
	 */
	public Cursor fetchAllInProgressRequestsOrderByPriority() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_IN_PROGRESS, 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestPriority + " DESC"
		);
	}
	
	public Cursor fetchAllInProgressRequestsForBuildingOrderByDate(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_IN_PROGRESS + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateAdded + " ASC" // top result is oldest pending request (result with the earliest add date)
		);
	}
	
	public Cursor fetchAllInProgressRequestsForBuildingOrderByPriority(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_IN_PROGRESS + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestPriority + " DESC"
		);
	}
	
	/**
	 * Return a Cursor over the list of all Requests in the database sorted by Priority
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllCompleteRequestsOrderByDate() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE, 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateCompleted + " DESC" // top result is the request completed most recently
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all Requests in the database sorted by Apartment
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllCompleteRequestsOrderByApartment() {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE, 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestBuilding + " ASC, " + RequestsDatabaseHelper.colRequestApartment + " ASC"
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all completed Requests in the database for the given building, sorted by date
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllCompleteRequestsForBuildingOrderByDate(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateCompleted + " DESC" // top result is the request completed most recently
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all completed Requests in the database for the given building, sorted by Building and Apartment (a.k.a. unit number)
	 * 
	 * @return Cursor over all Requests
	 */
	public Cursor fetchAllCompleteRequestsForBuildingOrderByApartment(String building) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
				RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestApartment + " ASC" // sort first by building, then by apartment
		);
		
	}
	
	public Cursor fetchAllCompleteRequestsForBuildingAndFloorOrderByDate(String building, String floor) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'" + " AND " + 
				"SUBSTR(" + RequestsDatabaseHelper.colRequestApartment + ",1,1) = '" + floor + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateCompleted + " DESC" // top result is the request completed most recently
		);
		
	}
	
	public Cursor fetchAllCompleteRequestsForBuildingAndFloorOrderByApartment(String building, String floor) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
				RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'" + " AND " + 
				"SUBSTR(" + RequestsDatabaseHelper.colRequestApartment + ",1,1) = '" + floor + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestApartment + " ASC" // sort first by building, then by apartment
		);
		
	}
	
	public Cursor fetchAllCompleteRequestsForBuildingAndApartmentOrderByDate(String building, String apartment) {
		return database.query(RequestsDatabaseHelper.requestsTable, 
			new String[] { RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus}, 
			RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
				RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "'" + " AND " + 
				RequestsDatabaseHelper.colRequestApartment + " = '" + apartment + "'", 
			null, 
			null, 
			null, 
			RequestsDatabaseHelper.colRequestDateCompleted + " DESC" // top result is the request completed most recently
		);
		
	}
	
	/**
	 * Return a Cursor over the list of all Requests in the database sorted by Priority
	 * 
	 * @return Cursor over all Requests
	 */

	public Cursor fetchAllCompleteRequestBuildings() {
		return database.rawQuery(
				"select distinct " + RequestsDatabaseHelper.colRequestBuilding + " from " + 
					RequestsDatabaseHelper.requestsTable + " where " + RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_INCOMPLETE + " order by " + RequestsDatabaseHelper.colRequestBuilding, 
				null);
	}
	
	/**
	 * Return a Cursor over the list of all buildings in the database that have in progress requests
	 * 
	 * @return Cursor over buildings
	 */

	public Cursor fetchAllInProgressRequestBuildings() {
		return database.rawQuery(
				"select distinct " + RequestsDatabaseHelper.colRequestBuilding + " from " + 
					RequestsDatabaseHelper.requestsTable + " where " + RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_IN_PROGRESS + " order by " + RequestsDatabaseHelper.colRequestBuilding, 
				null);
	}
	
	/**
	 * Return a Cursor over the list of all floors for a building
	 * @return Cursor over buildings
	 */

	public Cursor fetchAllCompleteApartmentFloorsForBuilding(String building) {
		return database.rawQuery(
				"select distinct SUBSTR(" + RequestsDatabaseHelper.colRequestApartment + ",1,1) " + " from " + 
					RequestsDatabaseHelper.requestsTable + " where " + RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
					RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "' order by SUBSTR(" + RequestsDatabaseHelper.colRequestApartment + ",1,1)", 
				null);
	}
	
	public Cursor fetchAllCompleteApartmentsForBuildingAndFloor(String building, String floor) {
		return database.rawQuery(
				"select distinct " + RequestsDatabaseHelper.colRequestApartment + " from " + 
					RequestsDatabaseHelper.requestsTable + " where " + RequestsDatabaseHelper.colRequestStatus + " = " + RequestsDatabaseHelper.STATUS_COMPLETE + " AND " +
					RequestsDatabaseHelper.colRequestBuilding + " = '" + building + "' AND " + 
					"SUBSTR(" + RequestsDatabaseHelper.colRequestApartment + ",1,1) = '" + floor + "' " +
					"ORDER BY " + RequestsDatabaseHelper.colRequestApartment, 
				null);
	}
	
/**
	 * Return a Cursor positioned at the defined Request
	 */

	public Cursor fetchRequest(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, RequestsDatabaseHelper.requestsTable, new String[] {
				RequestsDatabaseHelper.colRequestID,
				RequestsDatabaseHelper.colRequestName,
				RequestsDatabaseHelper.colRequestPriority,
				RequestsDatabaseHelper.colRequestDateAdded,
				RequestsDatabaseHelper.colRequestDateCompleted,
				RequestsDatabaseHelper.colRequestBuilding,
				RequestsDatabaseHelper.colRequestApartment,
				RequestsDatabaseHelper.colRequestDetails,
				RequestsDatabaseHelper.colRequestComments,
				RequestsDatabaseHelper.colRequestStatus},
				RequestsDatabaseHelper.colRequestID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String name, int priority, String dateAdded, String dateCompleted,
			String building, String apartment, String details, String comments, int status) {
		ContentValues values = new ContentValues();
		values.put(RequestsDatabaseHelper.colRequestName, name);
		values.put(RequestsDatabaseHelper.colRequestPriority, priority);
		values.put(RequestsDatabaseHelper.colRequestDateAdded, dateAdded);
		values.put(RequestsDatabaseHelper.colRequestDateCompleted, dateCompleted);
		values.put(RequestsDatabaseHelper.colRequestBuilding, building);
		values.put(RequestsDatabaseHelper.colRequestApartment, apartment);
		values.put(RequestsDatabaseHelper.colRequestDetails, details);
		values.put(RequestsDatabaseHelper.colRequestComments, comments);
		values.put(RequestsDatabaseHelper.colRequestStatus, status);
		return values;
	}
}
