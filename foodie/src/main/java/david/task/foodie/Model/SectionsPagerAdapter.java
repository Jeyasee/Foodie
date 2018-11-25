package david.task.foodie.Model;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import david.task.foodie.Presenter.Home;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Activity activity;

    public SectionsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Home.PlaceholderFragment.newInstance(position + 1,activity);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
