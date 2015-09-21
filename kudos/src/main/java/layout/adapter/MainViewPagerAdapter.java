package layout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import layout.tab.authentication.AuthenticationLoginTab;
import layout.tab.authentication.AuthenticationRegisterTab;
import layout.tab.main.ChallengeTab;
import layout.tab.main.EndorseTab;
import layout.tab.main.GiveKudosTab;

/**
 * Created by chc on 15.8.21.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[];
        int NumbOfTabs;


        public MainViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) {
                return new GiveKudosTab();
            }
            else if(position == 1) {
                return new EndorseTab();
            }
            return new ChallengeTab();
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
