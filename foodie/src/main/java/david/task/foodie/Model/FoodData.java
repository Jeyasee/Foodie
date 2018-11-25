package david.task.foodie.Model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FoodData implements Serializable{
    public String food_name,food_catagory;
    Integer food_order_count;
    Boolean food_order_status;
    Boolean food_selected = false;
    Integer food_price;

    public void setFood_name(String food_name){
        this.food_name = food_name;
    }

    public String getFood_name(){
        return this.food_name;
    }

    public void setFood_catagory(String food_catagory){
        this.food_catagory = food_catagory;
    }

    public String getFood_catagory(){
        return this.food_catagory;
    }

    public void setFood_order_count(Integer food_order_count){
        this.food_order_count = food_order_count;
    }

    public Integer getFood_order_count() {
        return this.food_order_count;
    }

    public void setFood_order_status(Boolean food_order_status) {
        this.food_order_status = food_order_status;
    }

    public Boolean getFood_order_status(){
        return this.food_order_status;
    }

    public void isSelected(){
        this.food_selected = true;
    }

    public void isDeSelected(){
        this.food_selected = false;
    }

    public Boolean getFoodSelected(){
        return this.food_selected;
    }

    public void setFood_price(Integer food_price){
        this.food_price = food_price;
    }

    public Integer getFood_price(){
        return this.food_price;
    }
}
