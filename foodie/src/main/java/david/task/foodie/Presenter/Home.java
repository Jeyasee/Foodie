package david.task.foodie.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import david.task.foodie.Model.Common;
import david.task.foodie.Model.DataBase;
import david.task.foodie.Model.FoodItems;
import david.task.foodie.Model.SectionsPagerAdapter;
import david.task.foodie.View.BadgeDrawable;
import david.task.foodie.foodie.R;

public class Home extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static DataBase dataBase;
    public static Drawable reuse;
    private  Menu menu;
    Common common = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        dataBase = new DataBase(this);

        if(common.getTableDataCount(dataBase,common.TABLE_NAME) != 0){
            loadList();
        }else{
            common.insertFoodItems(dataBase);
            loadList();
        }
    }

    public void loadList(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void generateData(DataBase dataBase) {
        HashMap<String,HashMap> items = common.getFood_Items();
    }

    @Override
    public void onStart(){
        try{
            //Cart count
            long cart_count = common.getCartDataCount(dataBase);

            MenuItem itemCart = menu.findItem(R.id.cart_count);
            LayerDrawable icon2 = (LayerDrawable) itemCart.getIcon();
            reuse = icon2.findDrawableByLayerId(R.id.cart_badge);
            setBadgeCount(this, icon2, String.valueOf(cart_count),"Cart");
        }catch (Exception e){
            Log.d("Cart_Reload","reload error");
        }

        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        //Cart count
        long cart_count = common.getCartDataCount(dataBase);


        MenuItem itemCart = menu.findItem(R.id.cart_count);
        LayerDrawable icon2 = (LayerDrawable) itemCart.getIcon();
        reuse = icon2.findDrawableByLayerId(R.id.cart_badge);
        setBadgeCount(this, icon2, String.valueOf(cart_count),"Cart");


        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart_count) {
            Intent intent = new Intent(this,Cart_List.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        static Activity activity;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber, Activity current_activity) {
            activity = current_activity;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.food_list, container, false);
            RecyclerView mRecycularView = (RecyclerView) rootView.findViewById(R.id.food_list);
            mRecycularView.setHasFixedSize(true);

            RecyclerView.LayoutManager linerLayout = new LinearLayoutManager(this.getContext());
            mRecycularView.setLayoutManager(linerLayout);
            Common common = new Common();
            DataBase database = new DataBase(this.getContext());
            Integer position = getArguments().getInt(ARG_SECTION_NUMBER);
            RecyclerView.Adapter<FoodItems.ViewHolder> adapter =
                    new FoodItems(this.getActivity(),common.getFoodItems(database,common.getFoodCatagory(position)));

            mRecycularView.setAdapter(adapter);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count, String layer) {

        BadgeDrawable badge;

        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.cart_badge, badge);
    }
}
