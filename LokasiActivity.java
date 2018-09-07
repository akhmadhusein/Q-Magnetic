package com.husaint2skripsi.lenovo.q_magnetic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LokasiActivity extends AppCompatActivity implements LocationListener {

    Button blanjut;
    ProgressBar pg;
    LocationManager locationManager;
    TextView tisi;
    String isi;

    //inisiasi sharedpreference
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //variable default lintang dan bujur
    double lintang = 0;
    double bujur = 0;
    double h=0;
    String x = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);

        //deklarasi sharedprefence
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mPreferences = getSharedPreferences("husein.com.sharedpreferences", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        //inisiasi item layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        blanjut = (Button) findViewById(R.id.btnnext);
        tisi = (TextView) findViewById(R.id.txtisi);


        //deklarasi penggunan GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        //inisisasi variable lokasi

        //save lintang
        lintang = location.getLatitude();
        mEditor.putString(getString(R.string.lintang), String.valueOf(lintang));
        mEditor.commit();

        //save bujur
        bujur = location.getLongitude();
        mEditor.putString(getString(R.string.bujur), String.valueOf(bujur));
        mEditor.commit();

        //save tinggi
        h = location.getAltitude();
        mEditor.putString(getString(R.string.tinggi), String.valueOf(h));
        mEditor.commit();

        //save lokasi
        x = location.getProvider();
        mEditor.putString(getString(R.string.lokasi), x);
        mEditor.commit();


        tisi = (TextView) findViewById(R.id.txtisi);
        isi = "Lokasi telah berhasil ditemukan";
        tisi.setText(String.valueOf(isi));
        pg = (ProgressBar) findViewById(R.id.pgilang);
        pg.setVisibility(View.INVISIBLE);
        blanjut = (Button) findViewById(R.id.btnnext);
        blanjut.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void tekan(View view) {
        Intent i = new Intent(LokasiActivity.this, MainActivity.class);
        startActivity(i);
    }

}
