package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IzberiActivity extends MainActivity {

    private static final String TAG = IzberiActivity.class.getSimpleName();
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

        findViewById(R.id.btn_i_ponovi).setOnClickListener(v -> {startActivity(new Intent(IzberiActivity.this, SlikajActivity.class));});
        String prebranoBesedilo = getIntent().getStringExtra("Besedilo");
        prikazBesedila = findViewById(R.id.polje_besedilo_izberi);
        prikazBesedila.setText(prebranoBesedilo);
    }

    @Override
    public void obKlikuNaprej(){
        String izbranoBesedilo = prikazBesedila.getText().toString();
        Log.d(TAG, izbranoBesedilo);
        Intent odpriShrani = new Intent(IzberiActivity.this, ShraniActivity.class);
        odpriShrani.putExtra("Besedilo", izbranoBesedilo);
        startActivity(odpriShrani);
    }
}
