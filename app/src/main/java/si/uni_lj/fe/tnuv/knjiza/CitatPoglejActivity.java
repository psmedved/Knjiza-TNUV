package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashMap;

public class CitatPoglejActivity extends MainActivity  {

    private static final String TAG = CitatPoglejActivity.class.getSimpleName();
    private ArrayList<Citat> seznamCitatov;
    private ListView prikazovalnikCitatov;
    private int indeksCitata;
    private ArrayList<HashMap<Integer, Integer>> preslikavaIndeksov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citat_poglej);
        poimenujStran(R.string.text_citat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);

        prikazovalnikCitatov = findViewById(R.id.seznam_citatov_knjige);
        prikazovalnikCitatov.setOnItemClickListener((aV, v, indeks, id) -> {
            Intent odpriCitatPoglej = new Intent(CitatPoglejActivity.this, CitatPoglejActivity.class);
            odpriCitatPoglej.putExtra("indeks", preslikavaIndeksov.get(indeks).get(indeks));
            startActivity(odpriCitatPoglej);
        });
        indeksCitata = getIntent().getIntExtra("indeks", -1);
        findViewById(R.id.btn_c_uredi).setOnClickListener(v -> {
            Intent odpriCitatUredi = new Intent(CitatPoglejActivity.this, CitatUrediActivity.class);
            odpriCitatUredi.putExtra("indeks", indeksCitata);
            startActivity(odpriCitatUredi);
        });
    }

    @Override
    protected  void onStart() {
        super.onStart();
        PreberiPodatke bralnik = new PreberiPodatke(this);
        seznamCitatov = bralnik.preberiCitate();
        //Log.d(TAG, "Št. citatov: " + seznamCitatov.size());
        prikaziPodatke();
    }

    private void prikaziPodatke() {
        try {
            TextView knjiga = findViewById(R.id.citat_naslov);
            TextView avtor = findViewById(R.id.citat_avtor);
            TextView citat = findViewById(R.id.citat_citat);
            knjiga.setText(seznamCitatov.get(indeksCitata).getKnjiga());
            avtor.setText(seznamCitatov.get(indeksCitata).getAvtor());
            citat.setText(seznamCitatov.get(indeksCitata).getCitat());
            String naslovKnjige = seznamCitatov.get(indeksCitata).getKnjiga();

            ArrayList<HashMap<String, String>> seznamZaAdapter = new ArrayList<>();
            preslikavaIndeksov = new ArrayList<>();
            int iSeznamCel = 0;
            int iSeznamKnjiga = 0;
            for (Citat c : seznamCitatov) {
                if(c.getKnjiga().equals(naslovKnjige) && iSeznamCel != indeksCitata) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("citat", c.getCitat());
                    map.put("knjiga", c.getKnjiga());
                    map.put("avtor", c.getAvtor());
                    map.put("leto", c.getLetoToString());
                    seznamZaAdapter.add(map);

                    HashMap<Integer, Integer> indeksi = new HashMap<>();
                    indeksi.put(iSeznamKnjiga, iSeznamCel);
                    preslikavaIndeksov.add(indeksi);
                    iSeznamKnjiga++;
                }
                iSeznamCel++;
            }
            SimpleAdapter pretvornikIzgleda = new SimpleAdapter(
                    this,
                    seznamZaAdapter,
                    R.layout.citat_element_seznama_knjige,
                    new String[] {"citat"},
                    new int[] {R.id.izpis_citata}
            );
            prikazovalnikCitatov.setAdapter(pretvornikIzgleda);

        } catch (Exception e) {
            Log.e(TAG, "Napaka" + e.getMessage(), e);
        }
    }
}
