package david.task.foodie.Model;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import david.task.foodie.Presenter.Food_Detail;
import david.task.foodie.foodie.R;


public class FoodItems extends RecyclerView.Adapter<FoodItems.ViewHolder> {
    Activity activity;
    ArrayList<FoodData> foodList;
    Common common = new Common();

    public FoodItems(Activity activity, ArrayList<FoodData> foodList){
        this.activity = activity;
        this.foodList = foodList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView food_name,food_price;
        public ImageView food_image;
        public CardView food_item_card;

        public ViewHolder(View itemView){
            super(itemView);
            food_image = (ImageView) itemView.findViewById(R.id.food_image);
            food_name = (TextView) itemView.findViewById(R.id.food_name);
            food_price = (TextView) itemView.findViewById(R.id.food_price);
            food_item_card = (CardView) itemView.findViewById(R.id.food_item_card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final FoodData data = (FoodData) foodList.get(position);
        viewHolder.food_image.setImageDrawable(activity.getResources().getDrawable(common.getFoodImage(data.getFood_name())));
        viewHolder.food_name.setText(data.getFood_name());
        viewHolder.food_price.setText("Rs. "+(Float.parseFloat(data.getFood_price().toString())));

        viewHolder.food_item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.isSelected();
                Intent intent = new Intent(activity,Food_Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Food", (Serializable) data);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
