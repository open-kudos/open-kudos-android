package layout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import layout.tab.authentication.AuthenticationLoginTab;
import layout.tab.authentication.AuthenticationRegisterTab;

/**
 * Created by chc on 15.8.19.
 */
public class LoginViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;


    public LoginViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            AuthenticationLoginTab tab1 = new AuthenticationLoginTab();
            return tab1;
        }
        else
        {
            AuthenticationRegisterTab tab2 = new AuthenticationRegisterTab();
            return tab2;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
