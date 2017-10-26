package nas509.groceries.controller;

/**
 * Created by matha_000 on 10/15/2017.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyListFragment tab1 = new MyListFragment();
                return tab1;
            case 1:
                SharedListFragment tab2 = new SharedListFragment();
                return tab2;
            case 2:
                PaymentsFragment tab3 = new PaymentsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}