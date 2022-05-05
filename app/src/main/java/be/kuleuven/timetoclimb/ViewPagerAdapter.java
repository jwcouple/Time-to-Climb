package be.kuleuven.timetoclimb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerAdapter extends FragmentStateAdapter {

    private Context context;
    int totalTabs;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch(position){
            case 0: fragment = new LoginTabFragment(); fragment.setArguments(args);return fragment;
            case 1: fragment = new SignupTabFragment();fragment.setArguments(args);return fragment;
            default: fragment = new LoginTabFragment(); return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
