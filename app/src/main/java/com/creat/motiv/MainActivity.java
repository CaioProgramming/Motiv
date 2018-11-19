package com.creat.motiv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Fragments.AboutFragment;
import com.creat.motiv.Fragments.HomeFragment;
import com.creat.motiv.Fragments.NewQuoteFragment;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.Fragments.SearchFragment;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

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
            R.drawable.home,
            R.drawable.search,
            R.drawable.write,
            R.drawable.user,
            R.drawable.about

    };
    private int[] tabIconsnight = {
            R.drawable.homenight,
            R.drawable.searchnight,
            R.drawable.writenight,
            R.drawable.usernight,
            R.drawable.aboutnight


    };
    private android.support.design.widget.TabLayout tabLayout;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    RealtimeBlurView rootblur;
     DotProgressBar progressBar;
    Pref preferences;
     private android.widget.ImageView offlineimage;
    private android.widget.LinearLayout offline;
    TextView offlinemessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         offline = findViewById(R.id.offline);
         offlineimage = findViewById(R.id.offlineimage);
        preferences = new Pref(this);
         tabLayout = findViewById(R.id.tabs);
        progressBar = findViewById(R.id.progress_bar);
        rootblur = findViewById(R.id.rootblur);
        offlinemessage = findViewById(R.id.offlinemssage);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.isEmailVerified()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this).setMessage("Email não verificado");
            builder.setMessage("Beleza? Verifica o email que você vai poder fazer mais que apenas ver frases");
            builder.setPositiveButton("Manda esse email aí po", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    user.sendEmailVerification();
                }
            });
            builder.setNegativeButton("Não to afim meu camarada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();}





        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (preferences.nightmodestate()){
            ConfigNightViewPager();
        }else {
            ConfigViewPager();
        }


        internetconnection();

    }

    private void internetconnection() {
        if (!isNetworkAvailable()) {
            offline.setVisibility(View.VISIBLE);
            offlinemessage.setText(Tools.offlinemessage());
            Glide.with(this).asGif().load(R.drawable.spaceguy).into(offlineimage);
            tabLayout.setVisibility(View.INVISIBLE); }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        internetconnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetconnection();
    }









    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            this.finish();
        }
    }

    private void ConfigViewPager() {
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, true);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setBackgroundResource(R.color.white);
        setupTabIcons();
    }

    private void ConfigNightViewPager() {
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, true);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setBackgroundResource(R.color.grey_900);
        setupTabIconsnight();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupTabIcons() {
        for (int i = 0 ;i < tabIcons.length;i++){
        Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(tabIcons[i]); }
    }
    private void setupTabIconsnight() {
        for (int i = 0 ;i < tabIcons.length;i++){
            Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(tabIconsnight[i]); }
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
                };
                timer.start();

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
                } ;

                  timer.start();



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
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_home, container, false);
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
         internetconnection();

    }



    public void checkPermissionREAD_EXTERNAL_STORAGE(
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
            }

        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabLayout/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:
                   return new HomeFragment();
               case 1:
                   return new SearchFragment();


               case 2:
                   return new NewQuoteFragment();
               case 3:
                   return new ProfileFragment();
               case 4:
                   return new AboutFragment();



           }
           return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
           return tabIcons.length;
        }
    }
}
