package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IzberiActivity extends MainActivity {

    private static final String TAG = IzberiActivity.class.getSimpleName();
    private String prebranoBesedilo;
    private EditText prikazBesedila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_izberi);
        poimenujStran(R.string.text_izberi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);

        findViewById(R.id.btn_i_ponovi).setOnClickListener(v -> {startActivity(new Intent(IzberiActivity.this, SlikajActivity.class));});
        findViewById(R.id.btn_i_nadaljuj).setOnClickListener(v -> {
            String izbranoBesedilo = prikazBesedila.getText().toString();
            if(izbranoBesedilo.trim().isEmpty()) {
                obvestiloONapaki(getString(R.string.obvestilo_sporocilo_napaka_citat_prazno_izberi));
                return;
            }
            bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(true);
            //Log.d(TAG, izbranoBesedilo);
        });
        prebranoBesedilo = getIntent().getStringExtra("Besedilo");
        prikazBesedila = findViewById(R.id.polje_besedilo_izberi);
        prikazBesedila.setText(prebranoBesedilo);
    }

    @Override
    protected void onSaveInstanceState(Bundle izhodnoStanje) {
        super.onSaveInstanceState(izhodnoStanje);
        izhodnoStanje.putString("besedilo", prikazBesedila.getText().toString());
        izhodnoStanje.putString("prebranoBesedilo", prebranoBesedilo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle shranjenoStanje) {
        super.onRestoreInstanceState(shranjenoStanje);
        prikazBesedila.setText(shranjenoStanje.getString("besedilo", ""));
        prebranoBesedilo = shranjenoStanje.getString("prebranoBesedilo", "");
    }

    @Override
    public void obKlikuNaprej() {
        String izbranoBesedilo = prikazBesedila.getText().toString();
        Log.d(TAG, izbranoBesedilo);
        if(izbranoBesedilo.trim().isEmpty()) {
            obvestiloONapaki(getString(R.string.obvestilo_sporocilo_napaka_citat_prazno_izberi));
            return;
        }
        Intent odpriShrani = new Intent(IzberiActivity.this, ShraniActivity.class);
        odpriShrani.putExtra("Besedilo", izbranoBesedilo);
        startActivity(odpriShrani);
    }
}
