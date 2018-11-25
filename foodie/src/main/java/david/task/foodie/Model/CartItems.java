package david.task.foodie.Model;

import android.app.Activity;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import david.task.foodie.Presenter.Cart_List;
import david.task.foodie.foodie.R;


public class CartItems extends RecyclerView.Adapter<CartItems.ViewHolder> {
    Activity activity;
    ArrayList<CartData> cartList;
    Common common = new Common();

    public CartItems(Activity activity, ArrayList<CartData> cartList){
        this.activity = activity;
        this.cartList = cartList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView product_name,product_price,remove_item;

        public ViewHolder(View itemView){
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.cart_item_name);
            product_price = (TextView) itemView.findViewById(R.id.cart_item_price);
            remove_item = (TextView) itemView.findViewById(R.id.remove_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final CartData data = (CartData) cartList.get(position);
        viewHolder.product_name.setText(data.getFood_name()+"  * "+data.getFood_order_count().toString());
        viewHolder.product_price.setText("Rs. "+String.valueOf(Float.parseFloat(String.valueOf(data.getFood_order_count() * data.getFood_price()))));

        viewHolder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.isDeSelected();
                Cart_List cart_list = new Cart_List();
                cart_list.removeItem(activity,data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}

