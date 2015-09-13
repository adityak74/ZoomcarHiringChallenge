package app.zmcarand;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 9/13/15.
 */
public class DatbaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "cardb";
    private static final String TABLE_CARS = "cars";

    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TYPE = "type";
    private static final String KEY_HOURLY_RATE =  "rate";
    private static final String KEY_RATING =  "rating";
    private static final String KEY_SEATER = "seater";
    private static final String KEY_AC =  "ac";
    private static final String KEY_LAT =  "lat";
    private static final String KEY_LONG =  "long";


    public DatbaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + KEY_NAME + " TEXT PRIMARY KEY,"
                + KEY_IMAGE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_HOURLY_RATE + " INTEGER,"
                + KEY_RATING + " NUMBER,"
                + KEY_SEATER + " TEXT,"
                + KEY_AC + " TEXT,"
                + KEY_LAT + " TEXT,"
                + KEY_LONG + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        // Create tables again to update the new news data
        onCreate(db);

    }

    public void addCarItem(Car car){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,car.getName());
        values.put(KEY_IMAGE,car.getImage());
        values.put(KEY_TYPE,car.getType());
        values.put(KEY_HOURLY_RATE,Integer.parseInt(car.getHourlyRate()));
        values.put(KEY_RATING,Double.parseDouble(car.getRating()));
        values.put(KEY_SEATER,car.getSeater());
        values.put(KEY_AC,car.getAc());
        values.put(KEY_LAT,car.getLat());
        values.put(KEY_LONG,car.getLong());

        db.insert(TABLE_CARS, null, values);
        db.close();


    }

    Car getCar(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CARS,new String[]{KEY_NAME,KEY_IMAGE,KEY_TYPE,
                KEY_HOURLY_RATE,KEY_RATING,KEY_SEATER,KEY_AC,KEY_LAT,KEY_LONG},KEY_NAME+"=?",new String[]{name},null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        Car car = new Car(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),
                cursor.getString(6),cursor.getString(7),cursor.getString(8));

        return car;

    }

    public List<Car> getAllRatingSortedCars(){
        List<Car> allCarList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS + " ORDER BY " + KEY_RATING + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setName(cursor.getString(0));
                car.setImage(cursor.getString(1));
                car.setType(cursor.getString(2));
                car.setHourlyRate(String.valueOf(cursor.getInt(3)));
                car.setRating(String.valueOf(cursor.getDouble(4)));
                car.setSeater(cursor.getString(5));
                car.setAc(cursor.getString(6));
                car.setLat(cursor.getString(7));
                car.setLong(cursor.getString(8));

                allCarList.add(car);

            }while (cursor.moveToNext());
        }

        db.close();

        return allCarList;
    }

    public List<Car> getAllPriceHrSortedCars(){
        List<Car> allCarList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS + " ORDER BY " + KEY_HOURLY_RATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setName(cursor.getString(0));
                car.setImage(cursor.getString(1));
                car.setType(cursor.getString(2));
                car.setHourlyRate(String.valueOf(cursor.getInt(3)));
                car.setRating(String.valueOf(cursor.getDouble(4)));
                car.setSeater(cursor.getString(5));
                car.setAc(cursor.getString(6));
                car.setLat(cursor.getString(7));
                car.setLong(cursor.getString(8));

                allCarList.add(car);

            }while (cursor.moveToNext());
        }

        db.close();

        return allCarList;
    }


    public List<Car> getAllCars(){
        List<Car> allCarList = new ArrayList<Car>();

        String selectQuery = "SELECT * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setName(cursor.getString(0));
                car.setImage(cursor.getString(1));
                car.setType(cursor.getString(2));
                car.setHourlyRate(String.valueOf(cursor.getInt(3)));
                car.setRating(String.valueOf(cursor.getDouble(4)));
                car.setSeater(cursor.getString(5));
                car.setAc(cursor.getString(6));
                car.setLat(cursor.getString(7));
                car.setLong(cursor.getString(8));

                allCarList.add(car);

            }while (cursor.moveToNext());
        }

        db.close();

        return allCarList;
    }

    public int getCarCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_CARS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int items = cursor.getCount();
        cursor.close();
        return items;
    }



}