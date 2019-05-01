package com.example.waterpipe.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.waterpipe.Database.Statistics;
import com.example.waterpipe.Database.StatsViewModel;
import com.example.waterpipe.R;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static StatsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int difficulty = 0;


        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
            TextView textView0 = (TextView) rootView.findViewById(R.id.section_label0);
            TextView textView1 = (TextView) rootView.findViewById(R.id.section_label1);
            TextView textView2 = (TextView) rootView.findViewById(R.id.section_label2);
            TextView textView3 = (TextView) rootView.findViewById(R.id.section_label3);
            TextView textView4 = (TextView) rootView.findViewById(R.id.section_label4);
            difficulty = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (difficulty) {
                case 1:
                    setTextForView(textView0, textView1, textView2, textView3, textView4, "Beginner");
                    break;
                case 2:
                    setTextForView(textView0, textView1, textView2, textView3, textView4, "Intermediate");
                    break;
                case 3:
                    setTextForView(textView0, textView1, textView2, textView3, textView4, "Expert");
                    break;
                default:
                    throw new RuntimeException("Unknown button ID");
            }
            return rootView;
        }


        private void setTextForView(final TextView textView0, final TextView textView1, final TextView textView2, final TextView textView3, final TextView textView4, final String difficulty) {
            final LiveData<List<Statistics>> statsList = mViewModel.getAllStats();
            statsList.observe(this, new Observer<List<Statistics>>() {
                @Override
                public void onChanged(@Nullable List<Statistics> statistics) {
                    String numComp = "Number of completed puzzles: " + getNumComp(statistics, difficulty);
                    if (!numComp.equals("Number of completed puzzles: null")) {
                        textView0.setText(numComp);
                        textView1.setText("Average time taken: " + getAvgTime(statistics, difficulty));
                        textView2.setText("Best time: " + getBestTime(statistics, difficulty));
                        textView3.setText("Average number of rotations: " + getAvgRots(statistics, difficulty));
                        textView4.setText("Least number of rotations: " + getLeastRots(statistics, difficulty));
                    } else {
                        textView0.setText("There are currently no statistics for this difficulty.");
                    }
                }
            });
        }

        private String getNumComp(List<Statistics> statistics, String difficulty) {
            for (Statistics s : statistics) {
                if (s.getDifficulty().equals(difficulty)) {
                    return s.getNumComp();
                }
            }
            return null;
        }

        private String getAvgTime(List<Statistics> statistics, String difficulty) {
            for (Statistics s : statistics) {
                if (s.getDifficulty().equals(difficulty)) {
                    return s.getAvgTime();
                }
            }
            return null;
        }

        private String getBestTime(List<Statistics> statistics, String difficulty) {
            for (Statistics s : statistics) {
                if (s.getDifficulty().equals(difficulty)) {
                    return s.getBestTime();
                }
            }
            return null;
        }

        private String getAvgRots(List<Statistics> statistics, String difficulty) {
            for (Statistics s : statistics) {
                if (s.getDifficulty().equals(difficulty)) {
                    return s.getAvgRots();
                }
            }
            return null;
        }

        private String getLeastRots(List<Statistics> statistics, String difficulty) {
            for (Statistics s : statistics) {
                if (s.getDifficulty().equals(difficulty)) {
                    return s.getLeastRots();
                }
            }
            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
