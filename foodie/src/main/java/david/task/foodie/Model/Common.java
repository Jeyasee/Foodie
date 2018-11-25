package david.task.foodie.Model;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import david.task.foodie.foodie.R;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Common {
    public final String DATABASE_NAME = "FOOD_ITEMS";
    final Integer DATABASE_VERSION = 6;
    public final String TABLE_NAME = "FOOD_LIST";
    final String TABLE_FOOD_CATAGORY = "FOOD_CATAGORY";
    final String TABLE_FOOD_NAME = "FOOD_NAME";
    final String TABLE_FOOD_PRICE = "FOOD_PRICE";
    final String TABLE_CART = "FOOD_CART";
    final String TABLE_TIMESTAMP = "FOOD_DATE";
    final String TABLE_ORDER_COUNT = "FOOD_COUNT";
    //static HashMap<String,Integer> Food_Items = new HashMap<String,Integer>();
    //static HashMap<String,HashMap> pairs = new HashMap<String,HashMap>();
    static HashMap<String,HashMap> Food_Items = new HashMap<String,HashMap>();

    public Common(){
        //Indian
        Food_Items.put("Indian_1",getItem("Biryani",200));
        Food_Items.put("Indian_2",getItem("Dosa",100));
        Food_Items.put("Indian_3",getItem("Chapati",100));

        //Chinese
        Food_Items.put("Chinese_1",getItem("Spinach Noodles",150));
        Food_Items.put("Chinese_2",getItem("Fried Mashi",90));
        Food_Items.put("Chinese_3",getItem("Dumplings",60));

        //Italian
        Food_Items.put("Italian_1",getItem("Panzenella",200));
        Food_Items.put("Italian_2",getItem("Pasta",100));
        Food_Items.put("Italian_3",getItem("Margherita Pizza",200));
    }

    public HashMap<String,Integer> getItem(String Item,Integer Price){
        HashMap<String,Integer> temp = new HashMap<String,Integer>();
        temp.put(Item,Price);
        return temp;
    }

    public HashMap<String,HashMap> getFood_Items(){
        return Food_Items;
    }

    public void insertFoodItems(DataBase dataBase){
        try {
            //DataBase equipmentDataBase = new DataBase(activity);

            SQLiteDatabase db = dataBase.getWritableDatabase();
            db.delete(TABLE_NAME,null,null);

            int stage = 1;//,list_size = Food_Items.size(),count = Food_Items.size();
            String catgory = null;

            for ( HashMap.Entry<String, HashMap> entry : Food_Items.entrySet()) {
                catgory = entry.getKey().split("_")[0];
                Integer food_price = Integer.parseInt(getHashMapValue(entry.getValue())[1]);
                String food_name  = getHashMapValue(entry.getValue())[0];

                ContentValues data = new ContentValues();
                data.put(TABLE_FOOD_CATAGORY,catgory);
                data.put(TABLE_FOOD_NAME,food_name);
                data.put(TABLE_FOOD_PRICE,food_price);
                data.put(TABLE_ORDER_COUNT,0);
                data.put(TABLE_CART,FALSE);
                db.insert(TABLE_NAME,null,data);
            }
            db.close();
        }catch (Exception e){
            //Log.d("InsertEquipment_Common",e.toString());
        }
    }

    public String[] getHashMapValue(HashMap entry){
        String[] values = new String[2];

        HashMap<String,Integer> temp = entry;
        for(HashMap.Entry<String,Integer> temp_2 : temp.entrySet()){
            values[1] = temp_2.getValue().toString();
            values[0] = temp_2.getKey();
        }

        return values;
    }


    public String GetCreateTable(String TableName){
        String query = "";
        switch (TableName){
            case TABLE_NAME:{
                query = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+TABLE_FOOD_CATAGORY+" TEXT,"+
                        TABLE_FOOD_NAME+" TEXT NOT NULL,"+TABLE_FOOD_PRICE+" INTEGER NOT NULL,"+TABLE_CART+" BOOLEAN,"+
                        TABLE_ORDER_COUNT+" INTEGER,"+
                        TABLE_TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
            }break;
        }

        return query;
    }

    public void UpdateOrderCount(DataBase dataBase,Integer count,String Food_Name,String Food_Catagory){
        SQLiteDatabase db = dataBase.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(TABLE_ORDER_COUNT,count);
        data.put(TABLE_CART,TRUE);

        db.update(TABLE_NAME,data,TABLE_FOOD_NAME+"=? AND "+TABLE_FOOD_CATAGORY+"=?",new String[]{
           Food_Name,Food_Catagory
        });
        db.close();
    }

    public void RemoveFromCart(DataBase dataBase,String Food_Name,String Food_Catagory){
        SQLiteDatabase db = dataBase.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(TABLE_ORDER_COUNT,0);
        data.put(TABLE_CART,FALSE);

        db.update(TABLE_NAME,data,TABLE_FOOD_NAME+"=? AND "+TABLE_FOOD_CATAGORY+"=?",new String[]{
                Food_Name,Food_Catagory
        });
        db.close();
    }

    public ArrayList<CartData> getCartItems(DataBase dataBase){
        ArrayList<CartData> list = new ArrayList<CartData>();
        try{
            SQLiteDatabase db = dataBase.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+TABLE_CART+"= ?",new String[]{String.valueOf(1)});
            if(cursor != null && cursor.getCount() != 0){
                cursor.moveToFirst();
                do {
                    CartData cartData = new CartData();
                    cartData.setFood_catagory(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_CATAGORY)));
                    cartData.setFood_name(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_NAME)));
                    cartData.isDeSelected();
                    cartData.setFood_order_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TABLE_ORDER_COUNT))));
                    cartData.setFood_order_status(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(TABLE_CART))));
                    cartData.setFood_price(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_PRICE))));
                    list.add(cartData);
                }while (cursor.moveToNext());
            }
            db.close();
        }catch (Exception e){

        }

        return list;
    }

    public long getCartDataCount(DataBase dataBase){
        long result = 0;
        try{
            SQLiteDatabase db = dataBase.getReadableDatabase();
            String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "+TABLE_CART+"= ?";
            Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(1)});
            int count = cursor.getCount();
            cursor.close();
            if(db != null || db.isOpen()){
                db.close();
            }
            return count;
        }catch (Exception e){

        }

        return  result;
    }

    public long getTableDataCount(DataBase dataBase,String TableName){
        long result = 0;
        try{
            SQLiteDatabase db = dataBase.getReadableDatabase();
            result = DatabaseUtils.queryNumEntries(db, TABLE_NAME);

            if(db != null || db.isOpen()){
                db.close();
            }

        }catch (Exception e){
            Log.d("",e.toString());
        }

        return  result;
    }

    public ArrayList<FoodData> getFoodItems(DataBase dataBase,String Food_Catagory){
        ArrayList<FoodData> list = new ArrayList<FoodData>();
        try{


            SQLiteDatabase db = dataBase.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+TABLE_FOOD_CATAGORY+"= ?",new String[]{Food_Catagory});
            if(cursor != null && cursor.getCount() != 0){
                cursor.moveToFirst();
                do {
                    FoodData foodData = new FoodData();
                    foodData.setFood_catagory(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_CATAGORY)));
                    foodData.setFood_name(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_NAME)));
                    foodData.isDeSelected();
                    foodData.setFood_order_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TABLE_ORDER_COUNT))));
                    foodData.setFood_order_status(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(TABLE_CART))));
                    Log.d(cursor.getString(cursor.getColumnIndex(TABLE_CART)).toString(),"cart status");
                    foodData.setFood_price(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TABLE_FOOD_PRICE))));
                    list.add(foodData);
                }while (cursor.moveToNext());
            }
            db.close();
        }catch (Exception e){

        }

        return list;
    }

    public Integer getFoodImage(String FoodName){
        Integer name = 0;

        switch (FoodName){
            case "Biryani":{name = R.drawable.briyani;}break;
            case "Dosa":{name = R.drawable.dosa;}break;
            case "Chapati":{name = R.drawable.chapati;}break;
            case "Spinach Noodles":{name = R.drawable.spinach_noodles;}break;
            case "Fried Mashi":{name = R.drawable.fried_mashi;}break;
            case "Dumplings":{name = R.drawable.dumplings;}break;
            case "Panzenella":{name = R.drawable.panzenella;}break;
            case "Pasta":{name = R.drawable.pasta;}break;
            case "Margherita Pizza":{name = R.drawable.margherita_pizza;}break;
            default:{name = R.drawable.no_image;}
        }

        return name;
    }

    public String getFoodCatagory(Integer tab_position){
        String catagory = "";
        switch (tab_position){
            case 1:{catagory="Indian";}break;
            case 2:{catagory="Chinese";}break;
            case 3:{catagory="Italian";}break;
        }
        return catagory;
    }
}
