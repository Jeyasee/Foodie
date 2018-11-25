package david.task.foodie.Presenter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import david.task.foodie.Model.CartData;
import david.task.foodie.Model.CartItems;
import david.task.foodie.Model.Common;
import david.task.foodie.Model.DataBase;
import david.task.foodie.foodie.R;

public class Cart_List extends AppCompatActivity {
    Common common = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);

        try{
            RecyclerView mRecycularView = (RecyclerView) findViewById(R.id.cart_list);
            mRecycularView.setHasFixedSize(true);

            RecyclerView.LayoutManager linerLayout = new LinearLayoutManager(this);
            mRecycularView.setLayoutManager(linerLayout);
            DataBase database = new DataBase(this);
            ArrayList<CartData> cartData = common.getCartItems(database);
            RecyclerView.Adapter<CartItems.ViewHolder> adapter = new CartItems(this,common.getCartItems(database));

            mRecycularView.setAdapter(adapter);

            Integer price = 0;
            for(CartData x: cartData){
                price += x.getFood_order_count()*x.getFood_price();
            }

            TextView total_price = (TextView) findViewById(R.id.food_total_price);
            total_price.setText("Rs. "+price.toString());
        }catch (Exception e){

        }
    }

    public void backArrow(View view) {
        super.onBackPressed();
    }

    public void removeItem(Activity activity,CartData data) {

        DataBase database = new DataBase(activity);
        Common common = new Common();

        common.RemoveFromCart(database,data.getFood_name(),data.getFood_catagory());

        RecyclerView mRecycularView = (RecyclerView) activity.findViewById(R.id.cart_list);
        mRecycularView.setHasFixedSize(true);

        RecyclerView.LayoutManager linerLayout = new LinearLayoutManager(activity);
        mRecycularView.setLayoutManager(linerLayout);
        ArrayList<CartData> cartData = common.getCartItems(database);
        RecyclerView.Adapter<CartItems.ViewHolder> adapter = new CartItems(activity,common.getCartItems(database));

        mRecycularView.setAdapter(adapter);

        Integer price = 0;
        for(CartData x: cartData){
            price += x.getFood_order_count()*x.getFood_price();
        }

        TextView total_price = (TextView) activity.findViewById(R.id.food_total_price);
        total_price.setText("Rs. "+price.toString());
    }
}
