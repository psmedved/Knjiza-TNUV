package si.uni_lj.fe.tnuv.knjiza;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class KazaloActivity extends MainActivity  {

    private static final String TAG = KazaloActivity.class.getSimpleName();
    private ArrayList<Kazalo> seznamKazala;
    private ArrayList<Knjiga> seznamKnjige;
    private ListView prikazovalnikKazala;
    private ListView prikazovalnikKnjige;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kazalo);
        poimenujStran(R.string.text_kazalo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        findViewById(R.id.btn_k_dodaj_kazalo).setOnClickListener(v -> shraniKazalo());
        findViewById(R.id.btn_k_dodaj_knjigo).setOnClickListener(v -> shraniKnjigo());
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        prikazovalnikKazala = findViewById(R.id.seznam_kazala);
        prikazovalnikKnjige = findViewById(R.id.seznam_knjige);

    }

    @Override
    protected  void onStart() {
        super.onStart();
        PreberiPodatke bralnik = new PreberiPodatke(this);
        seznamKazala = bralnik.preberiKazala();
        seznamKnjige = bralnik.preberiKnjige();

        prikaziPodatke();
    }

    public void  shraniKazalo() {
        LayoutInflater inflater = getLayoutInflater();
        View dodajKazalo = inflater.inflate(R.layout.dodaj_kazalo, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_naslov_kazalo)
                .setView(dodajKazalo)
                .setCancelable(false)
                .setPositiveButton(R.string.text_shrani, (dialog, which) -> {
                    EditText vnosnoPoljeKnjiga = dodajKazalo.findViewById(R.id.polje_knjiga_kazalo);
                    String vpisKnjiga = vnosnoPoljeKnjiga.getText().toString();
                    EditText vnosnoPoljeStran = dodajKazalo.findViewById(R.id.polje_stran_kazalo);
                    String vpisStran = vnosnoPoljeStran.getText().toString();
                    try {
                        FileOutputStream datoteka = openFileOutput(getString(R.string.datoteka_kazala), MODE_APPEND);
                        //Zapisovanje v NDJSON
                        OutputStreamWriter zapisovalec = new OutputStreamWriter(datoteka, StandardCharsets.UTF_8);
                        JSONObject objektCitat = new JSONObject();
                        objektCitat.put("knjiga", vpisKnjiga);
                        objektCitat.put("stran", vpisStran);
                        zapisovalec.write(objektCitat.toString());
                        zapisovalec.write("\n");
                        zapisovalec.close();
                        dialog.dismiss();
                        potrdiSranjevanjeKazala();
                    } catch (Exception e) {}
                })
                .setNegativeButton(R.string.navigacija_preklici, null)
                .show();
    }

    public void  shraniKnjigo() {
        LayoutInflater inflater = getLayoutInflater();
        View dodajKnjigo = inflater.inflate(R.layout.dodaj_knjigo, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_naslov_knjiga)
                .setView(dodajKnjigo)
                .setCancelable(false)
                .setPositiveButton(R.string.text_shrani, (dialog, which) -> {
                    EditText vnosnoPoljeKnjiga = dodajKnjigo.findViewById(R.id.polje_knjiga_knjiga);
                    String vpisKnjiga = vnosnoPoljeKnjiga.getText().toString();
                    EditText vnosnoPoljeAvtor = dodajKnjigo.findViewById(R.id.polje_avtor_knjiga);
                    String vpisAvtor = vnosnoPoljeAvtor.getText().toString();
                    EditText vnosnoPoljeLeto = dodajKnjigo.findViewById(R.id.polje_leto_knjiga);
                    String vpisLeto = vnosnoPoljeLeto.getText().toString();
                    try {
                        FileOutputStream datoteka = openFileOutput(getString(R.string.datoteka_knjiga), MODE_APPEND);
                        //Zapisovanje v NDJSON
                        OutputStreamWriter zapisovalec = new OutputStreamWriter(datoteka, StandardCharsets.UTF_8);
                        JSONObject objektCitat = new JSONObject();
                        objektCitat.put("knjiga", vpisKnjiga);
                        objektCitat.put("avtor", vpisAvtor);
                        objektCitat.put("leto", vpisLeto);
                        zapisovalec.write(objektCitat.toString());
                        zapisovalec.write("\n");
                        zapisovalec.close();
                        dialog.dismiss();
                        potrdiSranjevanjeKnjige();
                    } catch (Exception e) {}
                })
                .setNegativeButton(R.string.navigacija_preklici, null)
                .show();
    }

    private void prikaziPodatke() {
        prikaziPodatkeKazalo();
        prikaziPodatkeKnjiga();
    }

    private void prikaziPodatkeKazalo() {
        try {
            //seznamCitatov = new ContactsJsonParser().parseToArrayList(podatki);
            ArrayList<HashMap<String, String>> seznamZaAdapter = new ArrayList<>();
            for (Kazalo k : seznamKazala) {
                HashMap<String, String> map = new HashMap<>();
                map.put("knjiga", k.getKnjiga());
                map.put("stran", k.getStranToString());
                seznamZaAdapter.add(map);
            }
            SimpleAdapter pretvornikIzgleda = new SimpleAdapter(
                    this,
                    seznamZaAdapter,
                    R.layout.kazalo_element_seznama,
                    new String[] {"knjiga", "stran"},
                    new int[] {R.id.izpis_knjiga_kazalo, R.id.izpis_stran_kazalo}
            );
            prikazovalnikKazala.setAdapter(pretvornikIzgleda);
            Log.d("TAG", "Izpis podatkov kazal");
        } catch (Exception e) {
            Log.e(TAG, "Napaka" + e.getMessage(), e);
        }
    }

    private void prikaziPodatkeKnjiga() {
        try {
            //seznamCitatov = new ContactsJsonParser().parseToArrayList(podatki);
            ArrayList<HashMap<String, String>> seznamZaAdapter = new ArrayList<>();
            for (Knjiga k : seznamKnjige) {
                HashMap<String, String> map = new HashMap<>();
                map.put("knjiga", k.getKnjiga());
                map.put("avtor", k.getAvtor());
                map.put("leto", k.getLetoToString());
                seznamZaAdapter.add(map);
            }
            SimpleAdapter pretvornikIzgleda = new SimpleAdapter(
                    this,
                    seznamZaAdapter,
                    R.layout.knjiga_element_seznama,
                    new String[] {"knjiga", "avtor"},
                    new int[] {R.id.izpis_knjiga_knjiga, R.id.izpis_avtor_knjiga}
            );
            prikazovalnikKnjige.setAdapter(pretvornikIzgleda);
            Log.d("TAG", "Izpis podatkov knjig");
        } catch (Exception e) {
            Log.e(TAG, "Napaka" + e.getMessage(), e);
        }
    }

    public void potrdiSranjevanjeKazala() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_naslov_citat) //ponovno uporabljen zaradi iste vsebine
                .setMessage(R.string.text_sporocilo_kazalo)
                .setPositiveButton(R.string.navigacija_potrdi, null)
                .show();
    }

    public void potrdiSranjevanjeKnjige() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_naslov_citat) //ponovno uporabljen zaradi iste vsebine
                .setMessage(R.string.text_sporocilo_knjiga)
                .setPositiveButton(R.string.navigacija_potrdi, null)
                .show();
    }
}
