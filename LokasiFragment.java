package com.husaint2skripsi.lenovo.q_magnetic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class LokasiFragment extends Fragment {

    protected TextView t_lt_lk,t_bt_lk,t_h_lk,t_dm_lk,t_lokasi_lk,t_tgl_lk;

    double lt,bt,dm,h;

    //fungsi derajat
    public String bderajat_lengkap(double a) {

        double derajat = 0;
        double menit = 0;
        double detik = 0;

        if (a >= 0) {
            derajat = (int) (Math.floor(a));
        } else {
            derajat = (int) (Math.ceil(a));
        }

        if (a >= 0) {
            menit = (int) (Math.floor((a - derajat) * 60));
        } else {
            menit = (int) (Math.ceil((a - derajat) * 60));
        }


        detik = Math.round((((a - derajat) * 60) - (menit)) * 60);

        return (int) derajat + "° " + (int) Math.abs(menit) + "' " + Math.abs(detik) + "''";

    }


    public LokasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lokasi, container, false);

        t_lt_lk=(TextView)view.findViewById(R.id.txt_lt_lk);
        t_bt_lk=(TextView)view.findViewById(R.id.txt_bt_lk);
        t_h_lk=(TextView)view.findViewById(R.id.txt_h_lk);
        t_dm_lk=(TextView)view.findViewById(R.id.txt_dm_lk);
        t_lokasi_lk=(TextView)view.findViewById(R.id.txt_lokasi_lk);
        t_tgl_lk=(TextView)view.findViewById(R.id.txt_tgl_lk);

        //deklarasi Shared prefrences
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //setting text view lintang
        String lintang = mPreferences.getString(getString(R.string.lintang), "");
        lt=Double.valueOf(lintang);
        t_lt_lk.setText(String.valueOf(bderajat_lengkap(lt)));

        //setting text view bujur
        String bujur = mPreferences.getString(getString(R.string.bujur), "");
        bt=Double.valueOf(bujur);
        t_bt_lk.setText(String.valueOf(bderajat_lengkap(bt)));

        //setting text view tinggi
        String tinggi = mPreferences.getString(getString(R.string.tinggi), "");
        h=Double.valueOf(tinggi);
        t_h_lk.setText(String.valueOf(Math.round(h)+" meter"));

        //setting text view nama lokasi
        String x = mPreferences.getString(getString(R.string.lokasi),"");
        t_lokasi_lk.setText(x);

        //setting text view deklinasi magnetic
        String dek_m= mPreferences.getString(getString(R.string.deklinasi_magnetic),"");
        dm=Double.valueOf(dek_m);
        t_dm_lk.setText(String.valueOf(bderajat_lengkap(dm)));

        //setting display kalender
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        String kalender = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        t_tgl_lk.setText(kalender);

        return view;
    }

}
