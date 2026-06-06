package si.uni_lj.fe.tnuv.knjiza;

import android.app.Activity;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PreberiPodatke {

    private static final String TAG = PreberiPodatke.class.getSimpleName();
    private final Activity klicatelj;
    public PreberiPodatke(Activity klicatelj) {
        this.klicatelj = klicatelj;
    }

    public ArrayList<Citat> preberiCitate() {
        ArrayList<Citat> seznamCitatov = new ArrayList<>();
        try {
            InputStream datotekaCitati = klicatelj.openFileInput(klicatelj.getString(R.string.datoteka_citati));
            BufferedReader bralnik = new BufferedReader(new InputStreamReader(datotekaCitati));
            String vrstica;
            while ((vrstica = bralnik.readLine()) != null) {
                seznamCitatov.add(pretvoriVRazredCitat(vrstica));
                //Log.d(TAG, "Prebrano-citati: " + vrstica);
            }
            bralnik.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seznamCitatov;
    }

    public ArrayList<Kazalo> preberiKazala() {
        ArrayList<Kazalo> seznamKazal = new ArrayList<>();
        try {
            InputStream datotekaKazala= klicatelj.openFileInput(klicatelj.getString(R.string.datoteka_kazala));
            BufferedReader bralnik = new BufferedReader(new InputStreamReader(datotekaKazala));
            String vrstica;
            while ((vrstica = bralnik.readLine()) != null) {
                seznamKazal.add(pretvoriVRazredKazalo(vrstica));
                //Log.d(TAG,"Prebrano-kazala: " + vrstica);
            }
            bralnik.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seznamKazal;
    }

    public ArrayList<Knjiga> preberiKnjige() {
        ArrayList<Knjiga> seznamKnjig = new ArrayList<>();
        try {
            InputStream datotekaKnjige = klicatelj.openFileInput(klicatelj.getString(R.string.datoteka_knjiga));
            BufferedReader bralnik = new BufferedReader(new InputStreamReader(datotekaKnjige));
            String vrstica;
            while ((vrstica = bralnik.readLine()) != null) {
                seznamKnjig.add(pretvoriVRazredKnjiga(vrstica));
                //Log.d(TAG,"Prebrano-knjige: " + vrstica);
            }
            bralnik.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seznamKnjig;
    }

    public Citat pretvoriVRazredCitat(String vrstica) throws JSONException {
        //Log.d(TAG, "Prebrana vrstica citat: " + vrstica);
        JSONObject object = new JSONObject(vrstica);
        Citat novCitat = new Citat(
                object.getString("citat"),
                object.getString("knjiga"),
                object.getString("avtor"),
                object.getInt("leto")
        );
        return novCitat;
    }

    public Kazalo pretvoriVRazredKazalo(String vrstica) throws JSONException {
        JSONObject object = new JSONObject(vrstica);
        Kazalo novoKazalo = new Kazalo(
                object.getString("knjiga"),
                object.getInt("stran")
        );
        return novoKazalo;
    }

    public Knjiga pretvoriVRazredKnjiga(String vrstica) throws JSONException {
        JSONObject object = new JSONObject(vrstica);
        Knjiga novaKnjiga = new Knjiga(
                object.getString("knjiga"),
                object.getString("avtor"),
                object.getInt("leto")
        );
        return novaKnjiga;
    }
}
