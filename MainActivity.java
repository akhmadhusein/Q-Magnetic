package com.husaint2skripsi.lenovo.q_magnetic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    //inisiasi sharedpreference
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //variable kalender
    double lt,bt, desimalkalender;

    //memanggil class TSAGeoMag
    TSAGeoMag magModel = new TSAGeoMag();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklarasi sharedprefence
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mPreferences = getSharedPreferences("husein.com.sharedpreferencestest", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        //variable data kalender
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int tahun_js = c.get(Calendar.YEAR);
        int bulan_js = (c.get(Calendar.MONTH)) + 1;
        int tanggal_js = c.get(Calendar.DAY_OF_MONTH);

        //conversi georgian kalender
        GregorianCalendar cal = new GregorianCalendar(tahun_js, bulan_js, tanggal_js);

        //mendesimalkan kalender / tahun
        desimalkalender = magModel.decimalYear(cal);

        //mendapat nilai lintang
        String lintang = mPreferences.getString(getString(R.string.lintang), "");
        lt = Double.valueOf(lintang);

        //mendapat nilai bujur
        String bujur = mPreferences.getString(getString(R.string.bujur), "");
        bt = Double.valueOf(bujur);

        //save deklinasi magnetic
        double dm = magModel.getDeclination(lt, bt, desimalkalender, 0);
        mEditor.putString(getString(R.string.deklinasi_magnetic), String.valueOf(dm));
        mEditor.commit();

        //-----------------------------------------------------------------------------------------//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    //deklarasi menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //metode item menu ketika di klik
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
       switch (item.getItemId()){
           case R.id.menu_bantuan:
               Intent intent= new Intent(MainActivity.this, BantuanActivity.class);
               startActivity(intent);
               break;
           case R.id.menu_profil:
               Intent intent1= new Intent(MainActivity.this, ProfilActivity.class);
               startActivity(intent1);
               break;
       }

        return super.onOptionsItemSelected(item);
    }


    //methode View pager dan swipelayout
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new LokasiFragment();
                case 1:
                    return new WaterpassFragment();
                case 2:
                    return new KiblatFragment();
            }

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lokasi";
                case 1:
                    return "WaterPass";
                case 2:
                    return "Kiblat";
            }
            return null;
        }
    }

    //metode ketika tombol kembali di klik
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tutup Aplikasi Q-Magnetic ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent keluar = new Intent(Intent.ACTION_MAIN);
                        keluar.addCategory(Intent.CATEGORY_HOME);
                        keluar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(keluar);
                    }
                })
                .setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
    }
}
