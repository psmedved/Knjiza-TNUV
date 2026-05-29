package si.uni_lj.fe.tnuv.knjiza;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class IzberiActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_izberi);
        poimenujStran(R.string.text_izberi);
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_domov).setEnabled(false);
        //findViewById(R.id.btn_i_nadaljuj).setOnClickListener(v -> {startActivity(new Intent(IzberiActivity.this, ShraniActivity.class));});
        findViewById(R.id.btn_i_ponovi).setOnClickListener(v -> {startActivity(new Intent(IzberiActivity.this, SlikajActivity.class));});
        String prebranoBesedilo = getIntent().getStringExtra("Besedilo");
        EditText prikazBesedila = findViewById(R.id.polje_besedilo_izberi);
        prikazBesedila.setText(prebranoBesedilo);
        pretvorjenoBesedilo = prebranoBesedilo;
    }
    @Override
    public void obKlikuNaprej(){
        Intent odpriShrani = new Intent(IzberiActivity.this, ShraniActivity.class);
        odpriShrani.putExtra("Besedilo", pretvorjenoBesedilo);
        startActivity(odpriShrani);
    }

    private String pretvorjenoBesedilo = "";
}
