/*
    NIM     : 10119234
    NAMA    : ARHAM JUSNI INDRAWAN
    KELAS   : IF-4
 */

package com.example.uas_10119234.ui.notifications;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class NotificationsFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    public NotificationsFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {

        return fragmentList.size();
    }
}
