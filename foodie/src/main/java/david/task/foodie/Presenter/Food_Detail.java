package david.task.foodie.Presenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import david.task.foodie.Model.Common;
import david.task.foodie.Model.DataBase;
import david.task.foodie.Model.FoodData;
import david.task.foodie.foodie.R;

public class Food_Detail extends AppCompatActivity {
    Integer food_Count,food_Price,food_Image,food_Total_Items,food_Total_Price;
    String food_Name,food_Catagory;
    Common common = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        try{
            FoodData foodData = (FoodData) getIntent().getSerializableExtra("Food");

            //Food Catagory
            food_Catagory = foodData.getFood_catagory();

            //Food Image
            ImageView food_image = (ImageView) findViewById(R.id.food_image);
            food_Image = common.getFoodImage(foodData.getFood_name());
            food_image.setBackground(getResources().getDrawable(food_Image));

            //Food Name
            food_Name = foodData.getFood_name();
            getTextView(R.id.food_name).setText(food_Name);

            //Food Price
            food_Price = foodData.getFood_price();
            getTextView(R.id.price).setText(food_Price.toString());

            //Food Count
            food_Count = foodData.getFood_order_count();
            getTextView(R.id.food_count).setText(food_Count.toString());

            //Food Total Count
            food_Total_Items = foodData.getFood_order_count();
            getTextView(R.id.food_total_items).setText("Add "+food_Total_Items.toString()+" to cart");

            //Food Total Price
            food_Total_Price = foodData.getFood_order_count()*foodData.getFood_price();
            getTextView(R.id.food_total_price).setText(String.valueOf(food_Total_Price));

            if(food_Count == 0)
                getTextView(R.id.decrease_count).setVisibility(View.INVISIBLE);
        }catch (Exception e){
            Log.d("","");
        }
    }

    public TextView getTextView(Integer id){
        return (TextView) findViewById(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void decrease_count(View view) {
        if(food_Count == 1){
            getTextView(R.id.decrease_count).setVisibility(View.INVISIBLE);
        }else{
            food_Count -= 1;
            food_Total_Items = food_Count;
            food_Total_Price = food_Count*food_Price;


            getTextView(R.id.food_count).setText(food_Count.toString());
            getTextView(R.id.food_total_items).setText("Add "+food_Total_Items.toString()+" to cart");
            getTextView(R.id.food_total_price).setText(String.valueOf(food_Total_Price));
        }
    }

    public void increase_count(View view) {
        if(getTextView(R.id.decrease_count).getVisibility()== View.INVISIBLE)
            getTextView(R.id.decrease_count).setVisibility(View.VISIBLE);

        food_Count += 1;
        food_Total_Items = food_Count;
        food_Total_Price = food_Count*food_Price;


        getTextView(R.id.food_count).setText(food_Count.toString());
        getTextView(R.id.food_total_items).setText("Add "+food_Total_Items.toString()+" to cart");
        getTextView(R.id.food_total_price).setText(String.valueOf(food_Total_Price));
        getTextView(R.id.decrease_count).setVisibility(View.VISIBLE);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    public void addCart(View view) {
        try {
            DataBase dataBase = new DataBase(this);
            common.UpdateOrderCount(dataBase,food_Count,food_Name,food_Catagory);
        }catch (Exception e){
            Log.d("",e.toString());
        }
        Toast toast = Toast.makeText(this, food_Count.toString()+" "+ food_Name+" successfully added to cart.", Toast.LENGTH_LONG);
        toast.show();
    }
}
