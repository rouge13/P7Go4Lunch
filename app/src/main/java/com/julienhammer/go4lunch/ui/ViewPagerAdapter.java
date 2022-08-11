package com.julienhammer.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.julienhammer.go4lunch.ui.list.ListFragment;
import com.julienhammer.go4lunch.ui.workmates.WorkmatesFragment;
import com.julienhammer.go4lunch.ui.map.MapsFragment;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapsFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            case 2:
                return WorkmatesFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
