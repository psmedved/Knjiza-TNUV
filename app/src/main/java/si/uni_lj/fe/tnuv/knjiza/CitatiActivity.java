package si.uni_lj.fe.tnuv.knjiza;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class CitatiActivity extends MainActivity  {

    private static final String TAG = CitatiActivity.class.getSimpleName();
    private ArrayList<Citat> seznamCitatov;
    private ListView prikazovalnikCitatov;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citati);
        poimenujStran(R.string.text_citati);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        prikazovalnikCitatov = findViewById(R.id.seznam_citatov);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        PreberiPodatke bralnik = new PreberiPodatke(this);
        seznamCitatov = bralnik.preberiCitate();
        prikaziPodatke();
    }

    private void prikaziPodatke() {
        try {
            //seznamCitatov = new ContactsJsonParser().parseToArrayList(podatki);
            ArrayList<HashMap<String, String>> seznamZaAdapter = new ArrayList<>();
            for (Citat c : seznamCitatov) {
                HashMap<String, String> map = new HashMap<>();
                map.put("citat", c.getCitat());
                map.put("knjiga", c.getKnjiga());
                map.put("avtor", c.getAvtor());
                map.put("leto", c.getLetoToString());
                seznamZaAdapter.add(map);
            }
            SimpleAdapter pretvornikIzgleda = new SimpleAdapter(
                    this,
                    seznamZaAdapter,
                    R.layout.citat_element_seznama,
                    new String[] {"knjiga", "avtor"},
                    new int[] {R.id.izpis_knjiga_citati, R.id.izpis_avtor_citati}
            );
            prikazovalnikCitatov.setAdapter(pretvornikIzgleda);
            Log.d("TAG", "Izpis podatkov citatov");
        } catch (Exception e) {
            Log.e(TAG, "Napaka" + e.getMessage(), e);
        }
    }
}
