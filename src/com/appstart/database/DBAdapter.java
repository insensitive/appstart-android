package com.appstart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	private static final String DATABASE_NAME = "database";

	private static final int DATABASE_VERSION = 1;

	private final Context context;

	private DatabaseHelper DBHelper;
	private static SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);

		}

	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	public boolean check_login() throws SQLException {

		Cursor mCursor = db.rawQuery("select * from tbl_Login", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
			if (mCursor.getCount() > 0) {

				mCursor.close();

				return true;
			}
		}
		mCursor.close();

		return false;
	}

	public String getDefaultLanguage() throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Customer_Language", null,
				"IsDefault" + "='1'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			return mCursor.getString(1);

		}
		return "";
	}

	public String getDefaultLanguageCode(String LanguageID) throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Language", null, "LanguageID"
				+ "='" + LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			return mCursor.getString(1);

		}
		return "en";
	}

	public long insertContact(String CustomerID, String AppAcessID,
			String Password, String Status) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("AppAccessID", AppAcessID);
		initialValues.put("Password", Password);
		initialValues.put("Status", Status);

		return db.insert("tbl_Login", null, initialValues);

	}

	public long insertCustomerConfiguration(String CustomerID,
			String FontsType, String FontColor, String FontSize) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("FontsType", FontsType);
		initialValues.put("FontColor", FontColor);
		initialValues.put("FontSize", FontSize);

		return db.insert("tbl_Customer_Configuration", null, initialValues);

	}

	public long insertConfiguration(String ConfigurationID, String CustomerID,
			String FontType, String FontColor, String FontSize, String Spacing,
			String ThemeColor, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("FontType", FontType);
		initialValues.put("FontColor", FontColor);
		initialValues.put("FontSize", FontSize);

		initialValues.put("Spacing", Spacing);
		initialValues.put("ThemeColor", ThemeColor);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Configuration", null, initialValues);

	}

	public long insertCustomerLanguage(String CustomerID, String LanguageID,
			String IsDefault) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("IsDefault", IsDefault);

		return db.insert("tbl_Customer_Language", null, initialValues);

	}

	public long insertLanguage(String LanguageID, String Lang, String Title) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Lang", Lang);
		initialValues.put("Title", Title);

		return db.insert("tbl_Language", null, initialValues);

	}

	public long insertResolution(String ResolutionID, String Title,
			String Description, String Width, String Height,double ratio) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ResolutionID", ResolutionID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("Width", Width);
		initialValues.put("Height", Height);
		initialValues.put("Ratio", ratio);
		
		return db.insert("tbl_Resolution", null, initialValues);

	}

	public long insertModule(String ModuleID, String Name, String Description,
			String Status, String Path) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ModuleID", ModuleID);
		initialValues.put("Name", Name);
		initialValues.put("Description", Description);
		initialValues.put("Status", Status);
		initialValues.put("Path", Path);

		return db.insert("tbl_Module", null, initialValues);

	}

	public long insertCustomerModule(String RecordID, String ModuleID,
			String CustomerID, String OrderNumber, String Visibility,
			String Status, String Share, String Icon, String SynchDateAndTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ModuleID", ModuleID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("OrderNumber", OrderNumber);
		initialValues.put("Visibility", Visibility);
		initialValues.put("Status", Status);
		initialValues.put("Share", Share);
		initialValues.put("Icon", Icon);
		initialValues.put("SynchDateAndTime", SynchDateAndTime);

		return db.insert("tbl_Customer_Module", null, initialValues);

	}

	public long insertCustomerModuleDetails(String RecordID, String ModuleID,
			String CustomerID, String LanguageID, String ScreenName,
			String BackgroundImage,String BackgroundColor,String BackgroundType) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ModuleID", ModuleID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("ScreenName", ScreenName);
		initialValues.put("BackgroundImage", BackgroundImage);
		initialValues.put("BackgroundColor", BackgroundColor);
		initialValues.put("BackgroundType", BackgroundType);
		

		return db.insert("tbl_Customer_Module_Details", null, initialValues);

	}

	public long insertHomeWallpaper(String WallpaperID, String CustomerID,
			String Status, String isHomeWallpaper, String Ord,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WallpaperID", WallpaperID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("isHomeWallpaper", isHomeWallpaper);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Home_Wallpaper", null, initialValues);

	}

	public long insertModuleHomeWallpaperDetails(String RecordID,
			String WallpaperID, String LanguageID, String ImageTitle,
			String ImagePathIpad, String ImagePathIphone,
			String ImagePathAndroid, String ImagePathIpad3,
			String ImagePathIphone5, String LinkToModule, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WallpaperID", WallpaperID);
		initialValues.put("LanguageID", LanguageID);

		initialValues.put("ImageTitle", ImageTitle);
		initialValues.put("ImagePathIpad", ImagePathIpad);
		initialValues.put("ImagePathIphone", ImagePathIphone);
		initialValues.put("ImagePathAndroid", ImagePathAndroid);
		initialValues.put("ImagePathIpad3", ImagePathIpad3);
		initialValues.put("ImagePathIphone5", ImagePathIphone5);

		initialValues.put("LinkToModule", LinkToModule);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Module_Home_Wallpaper_Details", null,
				initialValues);

	}

	public long insertModuleContact(String ContactID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ContactID", ContactID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Module_Contact", null, initialValues);

	}

	public long insertModuleContactDetails(String RecordID, String ContactID,
			String LanguageID, String LocationName, String Address,
			String Phone1, String Phone2, String Phone3, String Fax,
			String Latitude, String Longitude, String Email1, String Email2,
			String Email3, String Website, String Timings, String Logo,
			String Informaion, String LastUpdateBy, String LastUpdateDateTime,
			String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ContactID", ContactID);
		initialValues.put("LanguageID", LanguageID);

		initialValues.put("LocationName", LocationName);
		initialValues.put("Address", Address);
		initialValues.put("Phone1", Phone1);
		initialValues.put("Phone2", Phone2);
		initialValues.put("Phone3", Phone3);
		initialValues.put("Fax", Fax);
		initialValues.put("Latitude", Latitude);
		initialValues.put("Longitude", Longitude);
		initialValues.put("Email1", Email1);
		initialValues.put("Email2", Email2);
		initialValues.put("Email3", Email3);
		initialValues.put("Website", Website);
		initialValues.put("Timings", Timings);
		initialValues.put("Logo", Logo);

		initialValues.put("Information", Informaion);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdateDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Module_Contact_Details", null, initialValues);

	}

	public long insertPushMessage(String PushMessageID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("PushMessageID", PushMessageID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Push_Message", null, initialValues);

	}

	public long insertPushMessageDetails(String RecordID, String PushMessageID,
			String LanguageID, String Title, String Description,
			String MessageDate) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("PushMessageID", PushMessageID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("MessageDate", MessageDate);

		return db.insert("tbl_Push_Message_Details", null, initialValues);

	}

	public long insertWebsite(String WebsiteID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Module_Website", null, initialValues);

	}

	public long insertWebsiteDetails(String RecordID, String WebsiteID,
			String LanguageID, String Title, String Url, String Description,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("Description", Description);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Website_Details", null, initialValues);

	}
	
	public long insertWebsite1(String WebsiteID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Module_Website1", null, initialValues);

	}

	public long insertWebsiteDetails1(String RecordID, String WebsiteID,
			String LanguageID, String Title, String Url, String Description,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("Description", Description);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Website_Details1", null, initialValues);

	}

	public long insertSocialMedia(String SocialMediaID, String CustomerID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("SocialMediaID", SocialMediaID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_SocialMedia", null, initialValues);

	}

	public long insertSocialMediaDetails(String RecordID, String SocialMediaID,
			String LanguageID, String SocialMediaTypeID, String Title,
			String Url, String IconPath, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("SocialMediaID", SocialMediaID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("SocialMediaTypeID", SocialMediaTypeID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("IconPath", IconPath);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_SocialMedia_Details", null, initialValues);

	}

	public long insertCms(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Cms", null, initialValues);

	}

	public long insertCms1(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Cms1", null, initialValues);

	}

	public long insertCms2(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Cms2", null, initialValues);

	}

	public long insertCmsDetails(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.insert("tbl_Cms_Details", null, initialValues);

	}

	public long insertCmsDetails1(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.insert("tbl_Cms_Details1", null, initialValues);

	}

	public long insertCmsDetails2(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.insert("tbl_Cms_Details2", null, initialValues);

	}

	public long insertEvents(String EventID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("EventID", EventID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Events", null, initialValues);

	}

	public long insertEventsDetails(String RecordID, String EventID,
			String LanguageID, String StartDateTime, String EndDateTime,
			String Title, String Description, String Image, String Street,
			String Zip, String Country, String Notes, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("EventID", EventID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("StartDateTime", StartDateTime);
		initialValues.put("EndDateTime", EndDateTime);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("Image", Image);
		initialValues.put("Street", Street);
		initialValues.put("Zip", Zip);
		initialValues.put("Country", Country);
		initialValues.put("Notes", Notes);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Events_Details", null, initialValues);

	}

	public long insertDocument(String DocumentID, String CategoryID,
			String CustomerID, String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("DocumentID", DocumentID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Document", null, initialValues);

	}

	public long insertDocumentDetails(String RecordID, String DocumentID,
			String LanguageID, String Title, String Description,
			String DocumentPath, String Keywords, String Type, String Size,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("DocumentID", DocumentID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("DocumentPath", DocumentPath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Type", Type);
		initialValues.put("Size", Size);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Document_Details", null, initialValues);

	}

	public long insertDocumentCategory(String CategoryID, String ParentID,
			String CustomerID, String Ord, String Status, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CategoryID", CategoryID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Ord", Ord);
		initialValues.put("Status", Status);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Document_Category", null, initialValues);

	}

	public long insertDocumentCategoryDetails(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Document_Category_Details", null, initialValues);

	}

	public long insertImageGallery(String ImageGalleryID,
			String ImageGalleryCategoryID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("ImageGalleryCategoryID", ImageGalleryCategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery", null, initialValues);

	}

	public long insertImageGallery1(String ImageGalleryID,
			String ImageGalleryCategoryID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("ImageGalleryCategoryID", ImageGalleryCategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery1", null, initialValues);

	}

	public long insertImageGalleryDetails(String RecordID,
			String ImageGalleryID, String LanguageID, String Title,
			String Description, String ImagePath, String Keywords, String Size) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("ImagePath", ImagePath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Size", Size);

		return db.insert("tbl_Image_Gallery_Details", null, initialValues);

	}

	public long insertImageGalleryDetails1(String RecordID,
			String ImageGalleryID, String LanguageID, String Title,
			String Description, String ImagePath, String Keywords, String Size) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("ImagePath", ImagePath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Size", Size);

		return db.insert("tbl_Image_Gallery_Details1", null, initialValues);

	}

	public long insertImageGalleryCategory(String CategoryID,
			String CustomerID, String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("CategoryID", CategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery_Category", null, initialValues);

	}
	public Boolean updateCustomerModuleOrderNumber(String RecordID,
  			String ModuleID, String CustomerID, String OrderNumber,String Status) {
  		ContentValues initialValues = new ContentValues();
  
  		initialValues.put("RecordID", RecordID);
  		initialValues.put("ModuleID", ModuleID);
  		initialValues.put("CustomerID", CustomerID);
  		initialValues.put("OrderNumber", OrderNumber);
  		initialValues.put("Status", Status);
  
  		System.out
  				.println("Only Order Number Updation in CustomerModule table");
  
  		return db.update("tbl_Customer_Module", initialValues, "RecordID"
  				+ "='" + RecordID + "'", null) > 0;
  
  	} 


	public long insertImageGalleryCategory1(String CategoryID,
			String CustomerID, String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();
		
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery_Category1", null, initialValues);

	}

	public long insertImageGalleryCategoryDetails(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery_Category_Details", null,
				initialValues);

	}

	public long insertImageGalleryCategoryDetails1(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Image_Gallery_Category_Details1", null,
				initialValues);

	}

	public long insertMusic(String MusicID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("MusicID", MusicID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Music", null, initialValues);

	}

	public long insertMusicDetails(String RecordID, String MusicID,
			String LanguageID, String Title, String Artist, String Album,
			String TrackUrl, String PreviewUrl, String AlbumArtUrl,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("MusicID", MusicID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Artist", Artist);
		initialValues.put("Album", Album);
		initialValues.put("TrackUrl", TrackUrl);
		initialValues.put("PreviewUrl", PreviewUrl);
		initialValues.put("AlbumArtUrl", AlbumArtUrl);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.insert("tbl_Music_Details", null, initialValues);

	}

	// --- update table methods ---
	public Boolean updateCustomerConfiguration(String CustomerID,
			String FontsType, String FontColor, String FontSize) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("FontsType", FontsType);
		initialValues.put("FontColor", FontColor);
		initialValues.put("FontSize", FontSize);

		System.out.println("Updation of ModuleContact table");

		return db.update("tbl_Customer_Configuration", initialValues,
				"CustomerID" + "='" + CustomerID + "'", null) > 0;

	}

	public Boolean updateCustomerLanguage(String CustomerID, String LanguageID,
			String IsDefault) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("IsDefault", IsDefault);

		System.out.println("Updation of CustomerLanguage table");

		return db.update("tbl_Customer_Language", initialValues, "LanguageID"
				+ "='" + LanguageID + "'", null) > 0;

	}

	public Boolean updateConfiguration(String ConfigurationID,
			String CustomerID, String FontType, String FontColor,
			String FontSize, String Spacing, String ThemeColor,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("CustomerID", CustomerID);
		initialValues.put("FontType", FontType);
		initialValues.put("FontColor", FontColor);
		initialValues.put("FontSize", FontSize);

		initialValues.put("Spacing", Spacing);
		initialValues.put("ThemeColor", ThemeColor);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Configuration table");

		return db.update("tbl_Configuration", initialValues, "CustomerID"
				+ "='" + CustomerID + "'", null) > 0;

	}

	public Boolean updateModule(String ModuleID, String Name,
			String Description, String Status, String Path) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ModuleID", ModuleID);
		initialValues.put("Name", Name);
		initialValues.put("Description", Description);
		initialValues.put("Status", Status);
		initialValues.put("Path", Path);

		System.out.println("Updation of Module table");

		return db.update("tbl_Module", initialValues, "ModuleID" + "='"
				+ ModuleID + "'", null) > 0;

	}

	public Boolean updateModuleIcon(String RecordID, String Icon) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("Icon", Icon);

		System.out.println("icon Updation of CustomerModule table");

		return db.update("tbl_Customer_Module", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateCustomerModule(String RecordID, String ModuleID,
			String CustomerID, String OrderNumber, String Visibility,
			String Status, String Share, String Icon, String SynchDateAndTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ModuleID", ModuleID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("OrderNumber", OrderNumber);
		initialValues.put("Visibility", Visibility);
		initialValues.put("Status", Status);
		initialValues.put("Share", Share);
		initialValues.put("Icon", Icon);
		initialValues.put("SynchDateAndTime", SynchDateAndTime);

		System.out.println("Updation of CustomerModule table");

		return db.update("tbl_Customer_Module", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateCustomerModuleDetails(String RecordID,
			String ModuleID, String CustomerID, String LanguageID,
			String ScreenName, String BackgroundImage,String BackgroundColor,String Backgroundtype) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ModuleID", ModuleID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("ScreenName", ScreenName);
		initialValues.put("BackgroundImage", BackgroundImage);
		initialValues.put("BackgroundColor", BackgroundColor);
		initialValues.put("BackgroundType", Backgroundtype);

		System.out.println("Updation of CustomerModuleDetails table");

		return db.update("tbl_Customer_Module_Details", initialValues,
				"RecordID" + "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateHomeWallpaper(String WallpaperID, String CustomerID,
			String Status, String isHomeWallpaper, String Ord,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WallpaperID", WallpaperID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("isHomeWallpaper", isHomeWallpaper);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of HomeWallpaper table");

		return db.update("tbl_Home_Wallpaper", initialValues, "WallpaperID"
				+ "='" + WallpaperID + "'", null) > 0;

	}

	public Boolean updateModuleHomeWallpaperDetails(String RecordID,
			String WallpaperID, String LanguageID, String ImageTitle,
			String ImagePathIpad, String ImagePathIphone,
			String ImagePathAndroid, String ImagePathIpad3,
			String ImagePathIphone5, String LinkToModule, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WallpaperID", WallpaperID);
		initialValues.put("LanguageID", LanguageID);

		initialValues.put("ImageTitle", ImageTitle);
		initialValues.put("ImagePathIpad", ImagePathIpad);
		initialValues.put("ImagePathIphone", ImagePathIphone);
		initialValues.put("ImagePathAndroid", ImagePathAndroid);
		initialValues.put("ImagePathIpad3", ImagePathIpad3);
		initialValues.put("ImagePathIphone5", ImagePathIphone5);

		initialValues.put("LinkToModule", LinkToModule);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of ModuleHomeWallpaperDetails table");

		return db.update("tbl_Module_Home_Wallpaper_Details", initialValues,
				"RecordID" + "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateModuleContact(String ContactID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ContactID", ContactID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of ModuleContact table");

		return db.update("tbl_Module_Contact", initialValues, "ContactID"
				+ "='" + ContactID + "'", null) > 0;

	}

	public Boolean updateModuleContactDetails(String RecordID,
			String ContactID, String LanguageID, String LocationName,
			String Address, String Phone1, String Phone2, String Phone3,
			String Fax, String Latitude, String Longitude, String Email1,
			String Email2, String Email3, String Website, String Timings,
			String Logo, String Informaion, String LastUpdateBy,
			String LastUpdateDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ContactID", ContactID);
		initialValues.put("LanguageID", LanguageID);

		initialValues.put("LocationName", LocationName);
		initialValues.put("Address", Address);
		initialValues.put("Phone1", Phone1);
		initialValues.put("Phone2", Phone2);
		initialValues.put("Phone3", Phone3);
		initialValues.put("Fax", Fax);
		initialValues.put("Latitude", Latitude);
		initialValues.put("Longitude", Longitude);
		initialValues.put("Email1", Email1);
		initialValues.put("Email2", Email2);
		initialValues.put("Email3", Email3);
		initialValues.put("Website", Website);
		initialValues.put("Timings", Timings);
		initialValues.put("Logo", Logo);
		initialValues.put("Information", Informaion);

		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdateDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of ModuleContactDetails table");

		return db.update("tbl_Module_Contact_Details", initialValues,
				"RecordID" + "='" + RecordID + "'", null) > 0;

	}

	public Boolean updatePushMessage(String PushMessageID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("PushMessageID", PushMessageID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Push Message table");

		return db.update("tbl_Push_Message", initialValues, "PushMessageID"
				+ "='" + PushMessageID + "'", null) > 0;

	}

	public Boolean UpdatePushMessageDetails(String RecordID,
			String PushMessageID, String LanguageID, String Title,
			String Description, String MessageDate) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("PushMessageID", PushMessageID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("MessageDate", MessageDate);

		return db.update("tbl_Push_Message_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateWebsite(String WebsiteID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdateBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Push Message table");

		return db.update("tbl_Module_Website", initialValues, "WebsiteID"
				+ "='" + WebsiteID + "'", null) > 0;

	}

	public Boolean updateWebsiteDetails(String RecordID, String WebsiteID,
			String LanguageID, String Title, String Url, String Description,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("Description", Description);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Website_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}
	
	public Boolean updateWebsite1(String WebsiteID, String CustomerID,
			String Status, String Ord, String LastUpdateBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		

		return db.update("tbl_Module_Website1", initialValues, "WebsiteID"
				+ "='" + WebsiteID + "'", null) > 0;

	}

	public Boolean updateWebsiteDetails1(String RecordID, String WebsiteID,
			String LanguageID, String Title, String Url, String Description,
			String LastUpdateBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("WebsiteID", WebsiteID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("Description", Description);
		initialValues.put("LastUpdatedBy", LastUpdateBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Website_Details1", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateSocialMedia(String SocialMediaID, String CustomerID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("SocialMediaID", SocialMediaID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Social media table");

		return db.update("tbl_SocialMedia", initialValues, "SocialMediaID"
				+ "='" + SocialMediaID + "'", null) > 0;

	}

	public Boolean updateSocialMediaDetails(String RecordID,
			String SocialMediaID, String LanguageID, String SocialMediaTypeID,
			String Title, String Url, String IconPath, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("SocialMediaID", SocialMediaID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("SocialMediaTypeID", SocialMediaTypeID);
		initialValues.put("Title", Title);
		initialValues.put("Url", Url);
		initialValues.put("IconPath", IconPath);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_SocialMedia_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateCms(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of cms table");

		return db.update("tbl_Cms", initialValues,
				"CmsID" + "='" + CmsID + "'", null) > 0;

	}

	public Boolean updateCms1(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of cms table");

		return db.update("tbl_Cms1", initialValues, "CmsID" + "=" + CmsID
				+ "", null) > 0;

	}

	public Boolean updateCms2(String CmsID, String CustomerID, String ParentID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CmsID", CmsID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of cms table");

		return db.update("tbl_Cms2", initialValues, "CmsID" + "='" + CmsID
				+ "'", null) > 0;

	}

	public Boolean updateCmsDetails(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.update("tbl_Cms_Details", initialValues, "RecordID" + "='"
				+ RecordID + "'", null) > 0;

	}

	public Boolean updateCmsDetails1(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.update("tbl_Cms_Details1", initialValues, "RecordID" + "='"
				+ RecordID + "'", null) > 0;

	}

	public Boolean updateCmsDetails2(String RecordID, String CmsID,
			String LanguageID, String Title, String Thumb, String Content) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CmsID", CmsID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Thumb", Thumb);
		initialValues.put("Content", Content);

		return db.update("tbl_Cms_Details2", initialValues, "RecordID" + "='"
				+ RecordID + "'", null) > 0;

	}

	public Boolean updateEvents(String EventID, String CustomerID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("EventID", EventID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Events table");

		return db.update("tbl_Events", initialValues, "EventID" + "='"
				+ EventID + "'", null) > 0;

	}

	public Boolean updateEventsDetails(String RecordID, String EventID,
			String LanguageID, String StartDateTime, String EndDateTime,
			String Title, String Description, String Image, String Street,
			String Zip, String Country, String Notes, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("EventID", EventID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("StartDateTime", StartDateTime);
		initialValues.put("EndDateTime", EndDateTime);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("Image", Image);
		initialValues.put("Street", Street);
		initialValues.put("Zip", Zip);
		initialValues.put("Country", Country);
		initialValues.put("Notes", Notes);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Events_Details", initialValues, "RecordID" + "='"
				+ RecordID + "'", null) > 0;

	}

	public Boolean updateDocument(String DocumentID, String CustomerID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("DocumentID", DocumentID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Document table");

		return db.update("tbl_Document", initialValues, "DocumentID" + "='"
				+ DocumentID + "'", null) > 0;

	}

	public Boolean updateDocumentDetails(String RecordID, String DocumentID,
			String LanguageID, String Title, String Description,
			String DocumentPath, String Keywords, String Type, String Size,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("DocumentID", DocumentID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("DocumentPath", DocumentPath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Type", Type);
		initialValues.put("Size", Size);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Document_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}
	
	
	public Boolean updateDocumentCategory(String CategoryID, String ParentID,
			String CustomerID, String Ord, String Status, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CategoryID", CategoryID);
		initialValues.put("ParentID", ParentID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Ord", Ord);
		initialValues.put("Status", Status);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		 
		return db.update("tbl_Document_Category", initialValues, "CategoryID"
				+ "='" + CategoryID + "'", null) > 0;

	}

	public Boolean updateDocumentCategoryDetails(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);
		
		return db.update("tbl_Document_Category_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}
	

	public Boolean updateImageGallery(String ImageGalleryID,
			String ImageGalleryCategoryID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("ImageGalleryCategoryID", ImageGalleryCategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery", initialValues, "ImageGalleryID"
				+ "='" + ImageGalleryID + "'", null) > 0;

	}

	public Boolean updateImageGallery1(String ImageGalleryID,
			String ImageGalleryCategoryID, String CustomerID, String Status,
			String Ord, String LastUpdatedBy, String LastUpdatedDateTime,
			String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("ImageGalleryCategoryID", ImageGalleryCategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery1", initialValues, "ImageGalleryID"
				+ "='" + ImageGalleryID + "'", null) > 0;

	}

	public Boolean updateImageGalleryDetails(String RecordID,
			String ImageGalleryID, String LanguageID, String Title,
			String Description, String ImagePath, String Keywords, String Size) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("ImagePath", ImagePath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Size", Size);

		return db.update("tbl_Image_Gallery_Details", initialValues, "RecordID"
				+ "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateImageGalleryDetails1(String RecordID,
			String ImageGalleryID, String LanguageID, String Title,
			String Description, String ImagePath, String Keywords, String Size) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("ImageGalleryID", ImageGalleryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Description", Description);
		initialValues.put("ImagePath", ImagePath);
		initialValues.put("Keywords", Keywords);
		initialValues.put("Size", Size);

		return db.update("tbl_Image_Gallery_Details1", initialValues,
				"RecordID" + "='" + RecordID + "'", null) > 0;

	}

	public Boolean updateImageGalleryCategory(String CategoryID,
			String CustomerID, String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CategoryID", CategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery_Category", initialValues,
				"CategoryID" + "=" + CategoryID + "", null) > 0;

	}

	public Boolean updateImageGalleryCategory1(String CategoryID,
			String CustomerID, String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("CategoryID", CategoryID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery_Category1", initialValues,
				"CategoryID" + "=" + CategoryID + "", null) > 0;

	}

	public Boolean updateImageGalleryCategoryDetails(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery_Category_Details", initialValues,
				"RecordID" + "=" + RecordID + "", null) > 0;

	}

	public Boolean updateImageGalleryCategoryDetails1(String RecordID,
			String CategoryID, String LanguageID, String Title,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("CategoryID", CategoryID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Image_Gallery_Category_Details1", initialValues,
				"RecordID" + "=" + RecordID + "", null) > 0;

	}

	public Boolean updateMusic(String MusicID, String CustomerID,
			String Status, String Ord, String LastUpdatedBy,
			String LastUpdatedDateTime, String CreatedBy, String CreatedDateTime) {
		ContentValues initialValues = new ContentValues();

		initialValues.put("MusicID", MusicID);
		initialValues.put("CustomerID", CustomerID);
		initialValues.put("Status", Status);
		initialValues.put("Ord", Ord);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		System.out.println("Updation of Music table");

		return db.update("tbl_Music", initialValues, "MusicID" + "='" + MusicID
				+ "'", null) > 0;

	}

	public Boolean updateMusicDetails(String RecordID, String MusicID,
			String LanguageID, String Title, String Artist,
			String Album, String TrackUrl, String PreviewUrl, String AlbumArtUrl,
			String LastUpdatedBy, String LastUpdatedDateTime, String CreatedBy,
			String CreatedDateTime) {

		ContentValues initialValues = new ContentValues();

		initialValues.put("RecordID", RecordID);
		initialValues.put("MusicID", MusicID);
		initialValues.put("LanguageID", LanguageID);
		initialValues.put("Title", Title);
		initialValues.put("Artist", Artist);
		initialValues.put("Album", Album);
		initialValues.put("TrackUrl", TrackUrl);
		initialValues.put("PreviewUrl", PreviewUrl);
		initialValues.put("AlbumArtUrl", AlbumArtUrl);
		initialValues.put("LastUpdatedBy", LastUpdatedBy);
		initialValues.put("LastUpdatedDateTime", LastUpdatedDateTime);
		initialValues.put("CreatedBy", CreatedBy);
		initialValues.put("CreatedDateTime", CreatedDateTime);

		return db.update("tbl_Music_Details", initialValues, "RecordID" + "='"
				+ RecordID + "'", null) > 0;

	}

	// ---get last update date methods---

	public Cursor getCustomerModuleDate(String RecordID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Customer_Module", new String[] {
				"ModuleID", "Icon", "SynchDateAndTime" }, "RecordID" + "='"
				+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getHomeWallpaperDate(String WallpaperID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Home_Wallpaper",
				new String[] { "LastUpdatedDateTime" }, "WallpaperID" + "='"
						+ WallpaperID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getModuleContact(String ContactID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Contact",
				new String[] { "LastUpdatedDateTime" }, "ContactID" + "='"
						+ ContactID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getPushMessageDate(String PushMessageID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Push_Message",
				new String[] { "LastUpdatedDateTime" }, "PushMessageID" + "='"
						+ PushMessageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getWebsiteDate(String WebsiteID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Website",
				new String[] { "LastUpdatedDateTime" }, "WebsiteID" + "='"
						+ WebsiteID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getWebsiteDetailDate(String RecordID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	
	public Cursor getWebsiteDate1(String WebsiteID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Website1",
				new String[] { "LastUpdatedDateTime" }, "WebsiteID" + "='"
						+ WebsiteID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getWebsiteDetailDate1(String RecordID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details1",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	

	public Cursor getSocialMediaDate(String SocialMediaID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_SocialMedia",
				new String[] { "LastUpdatedDateTime" }, "SocialMediaID" + "='"
						+ SocialMediaID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getSocialMediaDetailDate(String RecordID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_SocialMedia_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getCmsDate(String CmsID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Cms",
				new String[] { "LastUpdatedDateTime" }, "CmsID" + "='" + CmsID
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getCmsDate1(String CmsID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Cms1",
				new String[] { "LastUpdatedDateTime" }, "CmsID" + "=" + CmsID
						+ "", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getCmsDate2(String CmsID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Cms2",
				new String[] { "LastUpdatedDateTime" }, "CmsID" + "='" + CmsID
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getModuleHomeWallpaperDetailsDate(String RecordID)
			throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Module_Home_Wallpaper_Details",
				new String[] { "ImagePathAndroid", "LastUpdatedDateTime" },
				"RecordID" + "='" + RecordID + "'", null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getModuleContactDetailsDate(String RecordID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Contact_Details",
				new String[] { "LastUpdateDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getPushMessageDetailsDate(String RecordID)
			throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Module_Home_Wallpaper_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getEventsDate(String EventID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Events",
				new String[] { "LastUpdatedDateTime" }, "EventID" + "='"
						+ EventID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getEventsDetailsDate(String RecordID) throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Events_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getDocumentDate(String DocumentID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Document",
				new String[] { "LastUpdatedDateTime" }, "DocumentID" + "='"
						+ DocumentID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getDocumentDetailsDate(String RecordID) throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Document_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getDocumentCategoryDate(String DocumentID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Document_Category",
				new String[] { "LastUpdatedDateTime" }, "CategoryID" + "='"
						+ DocumentID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getDocumentCategoryDetailsDate(String RecordID) throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Document_Category_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getMusicDate(String MusicID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Music",
				new String[] { "LastUpdatedDateTime" }, "MusicID" + "='"
						+ MusicID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getMusicDetailsDate(String RecordID) throws SQLException {

		Cursor mCursor = db.query(true, "tbl_Music_Details",
				new String[] { "LastUpdatedDateTime" }, "RecordID" + "='"
						+ RecordID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getImageGalleryDate(String ImageGalleyID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Image_Gallery",
				new String[] { "LastUpdatedDateTime" }, "ImageGalleryID" + "="
						+ ImageGalleyID +"", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getImageGalleryDate1(String ImageGalleyID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Image_Gallery1",
				new String[] { "LastUpdatedDateTime" }, "ImageGalleryID" + "="
						+ ImageGalleyID + "", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getImageGalleryCategoryDate(String CategoryID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Image_Gallery_Category",
				new String[] { "LastUpdatedDateTime" }, "CategoryID" + "='"
						+ CategoryID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getImageGalleryCategoryDate1(String CategoryID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Image_Gallery_Category1",
				new String[] { "LastUpdatedDateTime" }, "CategoryID" + "='"
						+ CategoryID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---get contact data methods ---

	// public Cursor getContact() throws SQLException {
	// Cursor mCursor = db.query(true, "tbl_Module_Contact",
	// new String[] { "ContactID" }, null, null, null, null, null,
	// null);
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }

	public Cursor getContactDetails(String ContactID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Contact_Details",
				new String[] { "LocationName" }, "ContactID" + "='" + ContactID
						+ "' and LanguageID='" + LanguageID + "'", null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getAllContactDetails(String ContactID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Contact_Details", null,
				"ContactID" + "='" + ContactID + "' and LanguageID='"
						+ LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// public Cursor getSocialMedia() throws SQLException {
	// Cursor mCursor = db.query(true, "tbl_SocialMedia",
	// new String[] { "SocialMediaID" }, null, null, null, null, null,
	// null);
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }

	public Cursor getSocialMediaDetails(String SocialMediaID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_SocialMedia_Details", null,
				"SocialMediaID" + "='" + SocialMediaID + "' and LanguageID='"
						+ LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	// --get push message---
	public Cursor getPushMessage() throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Push_Message",
				new String[] { "PushMessageID" }, null, null, null, null,
				"Ord", null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getPushMessagetDetails(String PushMessageID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Push_Message_Details",
				new String[] { "Title" }, "PushMessageID" + "='"
						+ PushMessageID + "' and LanguageID='" + LanguageID
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getAllPushMessageDetails(String PushMessageID,
			String LanguageID) throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Push_Message_Details", null,
				"PushMessageID" + "='" + PushMessageID + "' and LanguageID='"
						+ LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---get website info---
	public Cursor getWebsite() throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Module_Website",
				new String[] { "WebsiteID" }, null, null, null, null,"Ord",
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	// ---get website info---
		public Cursor getWebsite1() throws SQLException {
			Cursor mCursor = db.query(true, "tbl_Module_Website1",
					new String[] { "WebsiteID" }, null, null, null, null, "Ord",
					null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}
		

	public Cursor getWebsiteDetails(String WebsiteID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details", new String[] {
				"Title", "Url" }, "WebsiteID" + "='" + WebsiteID
				+ "' and LanguageID='" + LanguageID + "'", null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getWebsiteDetails1(String WebsiteID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details1", new String[] {
				"Title", "Url" }, "WebsiteID" + "='" + WebsiteID
				+ "' and LanguageID='" + LanguageID + "'", null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getAllWebsiteDetails(String WebsiteID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details", null,
				"WebsiteID" + "='" + WebsiteID + "' and LanguageID='"
						+ LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	

	
	public Cursor getAllWebsiteDetails1(String WebsiteID, String LanguageID)
			throws SQLException {
		Cursor mCursor = db.query(true, "tbl_Website_Details1", null,
				"WebsiteID" + "='" + WebsiteID + "' and LanguageID='"
						+ LanguageID + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---get contact data---
	public Cursor getContacts(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT * FROM tbl_Module_Contact_Details INNER JOIN tbl_Module_Contact ON tbl_Module_Contact_Details.ContactID=tbl_Module_Contact.ContactID and tbl_Module_Contact_Details.LanguageID='"
								+ LanguageID + "' and tbl_Module_Contact.Status=1 order by tbl_Module_Contact.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get contact data---
	public Cursor getPushMessages(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT * FROM tbl_Push_Message_Details INNER JOIN tbl_Push_Message ON tbl_Push_Message_Details.PushMessageID=tbl_Push_Message.PushMessageID and tbl_Push_Message_Details.LanguageID='"
								+ LanguageID + "' order by tbl_Push_Message.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get event data---
	public Cursor getEvents(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Events_Details.EventID,StartDateTime,EndDateTime,Title,Description,Image,Street,Zip,Country,Notes FROM tbl_Events_Details INNER JOIN tbl_Events ON tbl_Events_Details.EventID=tbl_Events.EventID and tbl_Events_Details.LanguageID='"
								+ LanguageID + "' and tbl_Events.Status='1' order by tbl_Events.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get cms data---
	public Cursor getCms(String LanguageID, String ParentID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Cms_Details.CmsID,Title,Thumb,Content FROM tbl_Cms_Details INNER JOIN tbl_Cms ON tbl_Cms_Details.CmsID=tbl_Cms.CmsID and tbl_Cms_Details.LanguageID='"
								+ LanguageID
								+ "' and tbl_Cms.ParentID='"
								+ ParentID
								+ "' and tbl_Cms.Status='1' order by tbl_Cms.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get cms1 data---
	public Cursor getCms1(String LanguageID, String ParentID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Cms_Details1.CmsID,Title,Thumb,Content FROM tbl_Cms_Details1 INNER JOIN tbl_Cms1 ON tbl_Cms_Details1.CmsID=tbl_Cms1.CmsID and tbl_Cms_Details1.LanguageID="
								+ LanguageID
								+ " and tbl_Cms1.ParentID="
								+ ParentID
								+ " and tbl_Cms1.Status=1 order by tbl_Cms1.Ord",
						null);

		System.out
				.println("SELECT tbl_Cms_Details1.CmsID,Title,Thumb,Content FROM tbl_Cms_Details1 INNER JOIN tbl_Cms1 ON tbl_Cms_Details1.CmsID=tbl_Cms1.CmsID and tbl_Cms_Details1.LanguageID="
						+ LanguageID
						+ " and tbl_Cms1.ParentID="
						+ ParentID
						+ " and tbl_Cms1.Status=1 order by tbl_Cms1.Ord");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get cms2 data---
	public Cursor getCms2(String LanguageID, String ParentID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Cms_Details2.CmsID,Title,Thumb,Content FROM tbl_Cms_Details2 INNER JOIN tbl_Cms2 ON tbl_Cms_Details2.CmsID=tbl_Cms2.CmsID and tbl_Cms_Details2.LanguageID="
								+ LanguageID
								+ " and tbl_Cms2.ParentID="
								+ ParentID
								+ " and tbl_Cms2.Status=1 order by tbl_Cms2.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get document category
	public Cursor getDocumentCategory(String ParentID, String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Document_Category_Details.CategoryID,Title FROM tbl_Document_Category_Details INNER JOIN tbl_Document_Category ON tbl_Document_Category.CategoryID=tbl_Document_Category_Details.CategoryID and tbl_Document_Category.ParentID="
								+ ParentID
								+ " and tbl_Document_Category_Details.LanguageID='"
								+ LanguageID
								+ "' order by tbl_Document_Category.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get document data---
	public Cursor getDocument(String CategoryID, String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Document_Details.DocumentID,Title,Description,DocumentPath,Keywords,Type,Size FROM tbl_Document_Details INNER JOIN tbl_Document ON tbl_Document_Details.DocumentID=tbl_Document.DocumentID and tbl_Document_Details.LanguageID='"
								+ LanguageID
								+ "' and tbl_Document.CategoryID='"
								+ CategoryID + "' order by tbl_Document.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}
	// ---get document category
  	public Cursor getDocumentCategoryCursor(String LanguageID) {
  
  		Cursor mCursor = db
  				.rawQuery(
  
  						"SELECT tbl_Document_Category_Details.CategoryID,Title FROM tbl_Document_Category_Details INNER JOIN tbl_Document_Category ON tbl_Document_Category.CategoryID=tbl_Document_Category_Details.CategoryID and tbl_Document_Category_Details.LanguageID='"
  								+ LanguageID
  								+ "' order by tbl_Document_Category.Ord", null);
  
  		if (mCursor != null) {
  			mCursor.moveToFirst();
  		}
  
  		return mCursor; 
  	}

	// ---get social media data---
	public Cursor getSocialMedia(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_SocialMedia_Details.Url, tbl_SocialMedia_Details.Title,tbl_SocialMedia_Details.IconPath  FROM tbl_SocialMedia_Details INNER JOIN tbl_SocialMedia ON tbl_SocialMedia_Details.SocialMediaID=tbl_SocialMedia.SocialMediaID and tbl_SocialMedia_Details.LanguageID='"
								+ LanguageID + "' order by tbl_SocialMedia.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get image gallery---

	public Cursor getImageGallery(String CategoryID,String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Image_Gallery_Details.ImageGalleryID, Title,Description,ImagePath,Keywords,Size FROM tbl_Image_Gallery_Details INNER JOIN tbl_Image_Gallery ON tbl_Image_Gallery_Details.ImageGalleryID=tbl_Image_Gallery.ImageGalleryID and tbl_Image_Gallery_Details.LanguageID='"
								+ LanguageID
								+ "' and tbl_Image_Gallery.ImageGalleryCategoryID='"+CategoryID+"' and tbl_Image_Gallery.Status='1' order by tbl_Image_Gallery.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	public Cursor getImageGallery1(String CategoryID,String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Image_Gallery_Details1.ImageGalleryID, Title,Description,ImagePath,Keywords,Size FROM tbl_Image_Gallery_Details1 INNER JOIN tbl_Image_Gallery1 ON tbl_Image_Gallery_Details1.ImageGalleryID=tbl_Image_Gallery1.ImageGalleryID and tbl_Image_Gallery_Details1.LanguageID='"
								+ LanguageID
								+ "' and tbl_Image_Gallery1.ImageGalleryCategoryID='"+CategoryID+"' and tbl_Image_Gallery1.Status='1' order by tbl_Image_Gallery1.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;

	}

	public Cursor getImageGalleryCategory(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Image_Gallery_Category_Details.CategoryID, Title FROM tbl_Image_Gallery_Category_Details INNER JOIN tbl_Image_Gallery_Category ON tbl_Image_Gallery_Category_Details.CategoryID=tbl_Image_Gallery_Category.CategoryID and tbl_Image_Gallery_Category_Details.LanguageID='"
								+ LanguageID
								+ "' and tbl_Image_Gallery_Category.Status=1 order by tbl_Image_Gallery_Category.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	public Cursor getImageGalleryCategory1(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT tbl_Image_Gallery_Category_Details1.CategoryID, Title FROM tbl_Image_Gallery_Category_Details1 INNER JOIN tbl_Image_Gallery_Category1 ON tbl_Image_Gallery_Category_Details1.CategoryID=tbl_Image_Gallery_Category1.CategoryID and tbl_Image_Gallery_Category_Details1.LanguageID='"
								+ LanguageID
								+ "' and tbl_Image_Gallery_Category1.Status=1 order by tbl_Image_Gallery_Category1.Ord",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	public Cursor getMusic(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"SELECT * FROM tbl_Music_Details INNER JOIN tbl_Music ON tbl_Music_Details.MusicID=tbl_Music.MusicID and tbl_Music_Details.LanguageID='"
								+ LanguageID + "' and tbl_Music.Status=1 order by tbl_Music.Ord", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// select * from table_name where image_name = 1

	public boolean isNameAvailable(String table_name, String column_name,
			String value) throws SQLException {

		Cursor mCursor = db.rawQuery("select * from " + table_name + " where "
				+ column_name + "=" + value + "", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
			if (mCursor.getCount() > 0) {

				mCursor.close();

				return true;
			}
		}
		mCursor.close();

		return false;
	}

	// ---get configuration information---
	public String[] getConfigration() throws SQLException {

		String[] gps = new String[5];

		Cursor mCursor = db.query(true, "tbl_Configuration", null, null, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();

			gps[0] = mCursor.getString(2);
			gps[1] = mCursor.getString(3);
			gps[2] = mCursor.getString(4);
			gps[3] = mCursor.getString(5);
			gps[4] = mCursor.getString(6);

			return gps;
		}

		return null;
	}

	// ---get m---
	public Cursor getModules(String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"select tbl_Customer_Module.ModuleID,tbl_Customer_Module.Icon,ScreenName,BackgroundImage from tbl_Customer_Module_Details INNER JOIN tbl_Customer_Module where tbl_Customer_Module.RecordID=tbl_Customer_Module_Details.ModuleID and LanguageID='"
								+ LanguageID
								+ "' and tbl_Customer_Module.Visibility=1 order by tbl_Customer_Module.OrderNumber",
						null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---get language code from locale---
	public Cursor getLanguageFromLocale(String locale) throws SQLException {

		Cursor mCursor = db
				.rawQuery(
						"select tbl_Customer_Language.LanguageID from tbl_Customer_Language INNER JOIN tbl_Language where tbl_Customer_Language.LanguageID=tbl_Language.LanguageID and tbl_Language.Lang='"
								+ locale + "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ---screen name---
	public Cursor getScreenName(String ModuleId, String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"select ScreenName from tbl_Customer_Module_Details INNER JOIN tbl_Customer_Module where tbl_Customer_Module.RecordID=tbl_Customer_Module_Details.ModuleID and tbl_Customer_Module.ModuleID='"
								+ ModuleId
								+ "' and LanguageID='"
								+ LanguageID
								+ "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	// ---background image---
	// public Cursor getBackgroundImage(String ModuleId, String LanguageID) {
	//
	// Cursor mCursor = db
	// .rawQuery("select tbl_Customer_Module_Details.BackgroundImage,tbl_Module_Home_Wallpaper_Details.ImagePathAndroid from tbl_Customer_Module_Details INNER JOIN tbl_Customer_Module ON (tbl_Customer_Module.RecordID=tbl_Customer_Module_Details.ModuleID) INNER JOIN tbl_Module_Home_Wallpaper_Details ON(tbl_Module_Home_Wallpaper_Details.RecordID=tbl_Customer_Module_Details.BackgroundImage) where tbl_Customer_Module.ModuleID='"+ModuleId+"' and tbl_Customer_Module_Details.LanguageID='"+LanguageID+"'",
	// null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	//
	// }
	//
	// return mCursor;
	// }
	
	public Cursor getBackgroundColor(String ModuleId, String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"select BackgroundColor from tbl_Customer_Module_Details INNER JOIN tbl_Customer_Module where tbl_Customer_Module.RecordID=tbl_Customer_Module_Details.ModuleID and tbl_Customer_Module.ModuleID='"
								+ ModuleId
								+ "' and  tbl_Customer_Module_Details.BackgroundType='0' and LanguageID='"
								+ LanguageID
								+ "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}
	
	public Cursor getBackgroundImage(String ModuleId, String LanguageID) {

		Cursor mCursor = db
				.rawQuery(
						"select BackgroundImage from tbl_Customer_Module_Details INNER JOIN tbl_Customer_Module where tbl_Customer_Module.RecordID=tbl_Customer_Module_Details.ModuleID and tbl_Customer_Module.ModuleID='"
								+ ModuleId
								+ "' and  tbl_Customer_Module_Details.BackgroundType='1' and LanguageID='"
								+ LanguageID
								+ "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	// ---delete records from table---
	public boolean deleteLogin() {

		return db.delete("tbl_Login", null, null) > 0;
	}

	public boolean deleteCustomerModule() {

		return db.delete("tbl_Customer_Module", null, null) > 0;
	}

	public boolean deleteCustomerModuleDetails() {

		return db.delete("tbl_Customer_Module_Details", null, null) > 0;
	}

	public boolean deleteModule() {

		return db.delete("tbl_Module", null, null) > 0;
	}

	public boolean deleteResolution() {

		return db.delete("tbl_Resolution", null, null) > 0;
	}

	public boolean deleteModuleHomeWallpaper() {

		return db.delete("tbl_Module_Home_Wallpaper", null, null) > 0;

	}

	public boolean deletePushMessage() {

		return db.delete("tbl_Push_Message", null, null) > 0;
	}

	public boolean deleteModuleContact() {

		return db.delete("tbl_Module_Contact", null, null) > 0;
	}

	public boolean deleteCustomerConfiguration() {

		return db.delete("tbl_Customer_Configuration", null, null) > 0;
	}

	public boolean deleteModuleHomeWallpaperDetails() {

		return db.delete("tbl_Module_Home_Wallpaper_Details", null, null) > 0;
	}

	public boolean deletePushMessageDetails() {

		return db.delete("tbl_Push_Message_Details", null, null) > 0;
	}

	public boolean deleteModuleContactDetails() {

		return db.delete("tbl_Module_Contact_Details", null, null) > 0;
	}

	public boolean deleteSocialMedia() {

		return db.delete("tbl_SocialMedia", null, null) > 0;
	}

	public boolean deleteSocialMediaDetails() {

		return db.delete("tbl_SocialMedia_Details", null, null) > 0;
	}

	public boolean deleteCustomerLanguage() {

		return db.delete("tbl_Customer_Language", null, null) > 0;
	}

	public boolean deleteLanguage() {

		return db.delete("tbl_Language", null, null) > 0;
	}

	public boolean deleteCms() {

		return db.delete("tbl_Cms", null, null) > 0;
	}

	public boolean deleteCmsDetails() {

		return db.delete("tbl_Cms_Details", null, null) > 0;
	}

	public boolean deleteCms1() {

		return db.delete("tbl_Cms1", null, null) > 0;
	}

	public boolean deleteCmsDetails1() {

		return db.delete("tbl_Cms_Details1", null, null) > 0;
	}

	public boolean deleteCms2() {

		return db.delete("tbl_Cms2", null, null) > 0;
	}

	public boolean deleteCmsDetails2() {

		return db.delete("tbl_Cms_Details2", null, null) > 0;
	}

	public boolean deleteEvents() {

		return db.delete("tbl_Events", null, null) > 0;
	}

	public boolean deleteEventsDetails() {

		return db.delete("tbl_Events_Details", null, null) > 0;
	}

	public boolean deleteDocument() {

		return db.delete("tbl_Document", null, null) > 0;
	}

	public boolean deleteDocumentDetails() {

		return db.delete("tbl_Document_Details", null, null) > 0;
	}

	public boolean deleteImageGallery() {

		return db.delete("tbl_Image_Gallery", null, null) > 0;
	}

	public boolean deleteImageGalleryDetails() {

		return db.delete("tbl_Image_Gallery_Details", null, null) > 0;
	}

	public boolean deleteMusic() {

		return db.delete("tbl_Music", null, null) > 0;
	}

	public boolean deleteMusicDetails() {

		return db.delete("tbl_Music_Details", null, null) > 0;
	}

	public Cursor row_query(String query) throws SQLException {

		Cursor mCursor = db.rawQuery(query, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

}