package com.husaint2skripsi.lenovo.q_magnetic;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class KiblatFragment extends Fragment implements SensorEventListener {

    TextView t_azmkiblat_k, t_dekm_k;
    ImageView img_kiblat, img_kompas;

    //deklarasi Variabel
    public Double lt = Double.valueOf(0);
    public Double bt = Double.valueOf(0);
    public double dm = 0;
    public double azkiblt = Double.valueOf(0);

    //// record the compass picture angle turned
    float degreekompas, degreekiblat;
    private float currentDegreekompas = 0f;
    private float currentDegreekiblat = 0f;

    // fungsi menderajat bilangan
    public String bderajat(double a) {

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

    // device sensor manager
    private SensorManager mSensorManager;



    @Override
    public void onAttach(Context context) {

        //deklarasi penggunaan kompass
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        super.onAttach(context);
    }


    public KiblatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    public void onPause() {

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kiblat, container, false);

        t_azmkiblat_k = (TextView) view.findViewById(R.id.txt_azmkiblat_k);
        t_dekm_k = (TextView) view.findViewById(R.id.txt_dekmag_k);
        img_kiblat = (ImageView) view.findViewById(R.id.imagekiblat);
        img_kompas = (ImageView) view.findViewById(R.id.imagekompas);

        //deklarasi Shared prefrences
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //mendapatkan nilai lintang
        String lintang = mPreferences.getString(getString(R.string.lintang), "");
        lt = Double.valueOf(lintang);

        //mendapatkan nilai bujur
        String bujur = mPreferences.getString(getString(R.string.bujur), "");
        bt = Double.valueOf(bujur);

        // mendapatkan nilai deklinasi magnetik
        String dek_mag = mPreferences.getString(getString(R.string.deklinasi_magnetic),"");
        dm= Double.valueOf(dek_mag);

        //deklarasi penggunaan kompass
        // initialize your android device sensor capabilities
        //cek sensor pada ponsel
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
            img_kompas.setVisibility(View.VISIBLE);
            img_kiblat.setVisibility(View.VISIBLE);
        } else {
            img_kiblat.setVisibility(View.INVISIBLE);
            img_kompas.setVisibility(View.INVISIBLE);
        }

        //memanggil fungsi arah kiblat
        azkiblt=arah_kiblat(lt,bt);

        //setting text view azimut kiblat dan deklinasi magnetik
        t_azmkiblat_k.setText(": "+bderajat(azkiblt));
        t_dekm_k.setText(": "+bderajat(dm));

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        degreekiblat = Math.round(event.values[0]) - (float) azkiblt - (float)dm;
        degreekompas = Math.round(event.values[0]) - (float)dm;

        // create a rotation animation (reverse turn degree degrees)Kompas
        RotateAnimation rakompas = new RotateAnimation(
                currentDegreekompas,
                -degreekompas,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // Start the animation Kompas
        img_kompas.startAnimation(rakompas);
        currentDegreekompas = -degreekompas;

        // create a rotation animation (reverse turn degree degrees)kiblat
        RotateAnimation rakiblat = new RotateAnimation(
                currentDegreekiblat,
                -degreekiblat,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // Start the animation Kiblat
        img_kiblat.startAnimation(rakiblat);
        currentDegreekiblat = -degreekiblat;


        // how long the animation will take place
        rakiblat.setDuration(210);
        rakompas.setDuration(210);

        // set the animation after the end of the reservation status
        rakiblat.setFillAfter(true);
        rakompas.setFillAfter(true);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // fungsi perhitungan arah kiblat
    public double arah_kiblat(double ltx, double btx) {
        //perhitungan arah kiblat
        double lk = 21.42254722;//lintang ka'bah
        double bk = 39.82626667;//bujur ka'bah

        //mencari C (selisih bujur ka'bah dengan daerah)
        double c = 0;
        String arah = "barat";
        if (btx >= 0 && btx > bk) {
            c = btx - bk;
            arah = "barat";
        } else if (btx >= 0 && btx < bk) {
            c = bk - btx;
            arah = "timur";
        } else if (btx < 0 && btx < 140.1722222) {
            c = btx + bk;
            arah = "timur";
        } else if (btx < 0 && btx > 140.172222) {
            c = 360 - btx - bk;
            arah = "barat";
        }

        double tanlk = Math.tan(lk * Math.PI / 180);
        double coslt = Math.cos(ltx * Math.PI / 180);
        double sinc = Math.sin(c * Math.PI / 180);
        double sinlt = Math.sin(ltx * Math.PI / 180);
        double tanc = Math.tan(c * Math.PI / 180);

        //rumuskiblat
        double kiblat = Math.toDegrees(Math.atan(1 / ((tanlk * coslt / sinc) - sinlt / tanc)));

        double azmkiblt=0;
        //mencari azimut kiblat
        if (kiblat >= 0 && arah == "timur") {
            azmkiblt = kiblat;
        } else if (kiblat >= 0 && arah == "barat") {
            azmkiblt = 360 - kiblat;
        } else if (kiblat < 0 && arah == "timur") {
            azmkiblt = 180 - Math.abs(kiblat);
        } else if (kiblat < 0 && arah == "barat") {
            azmkiblt = 180 + Math.abs(kiblat);
        }

        return azmkiblt;

    }
}
