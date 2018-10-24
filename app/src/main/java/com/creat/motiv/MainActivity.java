package com.creat.motiv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.creat.motiv.Fragments.HomeFragment;
import com.creat.motiv.Fragments.NewQuoteFragment;
import com.creat.motiv.Fragments.ProfileFragment;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;

import de.mateware.snacky.Snacky;

public class MainActivity extends AppCompatActivity {

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
    private int[] tabIcons = {
            R.drawable.write,
            R.drawable.home,
            R.drawable.user
    };
    private int[] tabIconsnight = {
            R.drawable.writenight,
            R.drawable.homenight,
            R.drawable.usernight
    };
    private Toolbar toolbar;
    private android.support.design.widget.TabLayout tabLayout;
    private ViewPager container;
    private RelativeLayout maincontent;
    private android.widget.EditText search;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private android.support.design.widget.FloatingActionButton floatingActionButton2;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences setings;
    RealtimeBlurView rootblur;
    ProgressBar progressBar;
    Pref preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = new Pref(this);
         tabLayout = findViewById(R.id.tabs);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress_bar);
        rootblur = findViewById(R.id.rootblur);


        maincontent = findViewById(R.id.main_content);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (preferences.nightmodestate() == true){
            ConfigNightViewPager();
        }else {
            ConfigViewPager();
        }



    }

    private void ConfigViewPager() {
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1,true);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setBackgroundResource(R.color.white);
        setupTabIcons();
    }

    private void ConfigNightViewPager() {
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1,true);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setBackgroundResource(R.color.grey_900);
        setupTabIconsnight();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void setupTabIconsnight() {
        tabLayout.getTabAt(0).setIcon(tabIconsnight[0]);
        tabLayout.getTabAt(1).setIcon(tabIconsnight[1]);
        tabLayout.getTabAt(2).setIcon(tabIconsnight[2]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.theme) {
            setings = PreferenceManager.getDefaultSharedPreferences(this);

            if (preferences.nightmodestate()){
                preferences.setNight(false);
                rootblur.setBlurRadius(50);
                progressBar.setVisibility(View.VISIBLE);
                ConfigViewPager();
                CountDownTimer timer = new CountDownTimer(3000,100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        progressBar.setVisibility(View.INVISIBLE);
                        rootblur.setBlurRadius(0 );
                        rootblur.setOverlayColor(Color.TRANSPARENT);
                    }
                }.start();

            }else {
                preferences.setNight(true);
                ConfigNightViewPager();
                rootblur.setBlurRadius(50);
                progressBar.setVisibility(View.VISIBLE);
                  CountDownTimer timer = new CountDownTimer(3000,100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        ConfigNightViewPager();
                        progressBar.setVisibility(View.INVISIBLE);
                        rootblur.setBlurRadius(0);
                        rootblur.setOverlayColor(Color.TRANSPARENT);
                    }
                }.start();



            }
            // Commit the edits!

         }else if (id == R.id.exit){
            FirebaseAuth.getInstance().signOut();
            Snacky.builder().setActivity(this).info().setText("Voce encerrou sua sessão, o aplicativo será encerrado").show();
            this.finish();
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
             return rootView;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permissão");
        alertBuilder.setMessage(msg + " permissão necessária");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissionREAD_EXTERNAL_STORAGE(this);
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Armazenamento externo", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabLayout/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:
                   NewQuoteFragment newQuoteFragment = new NewQuoteFragment();
                   return  newQuoteFragment;
               case 1:
                   HomeFragment homeFragment = new HomeFragment();
                   return  homeFragment;
               case 2:
                   ProfileFragment profileFragment = new ProfileFragment();
                   return profileFragment;

           }
           return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
