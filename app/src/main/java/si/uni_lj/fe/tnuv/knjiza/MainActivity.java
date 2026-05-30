package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        //
        Toolbar topAppMenu = findViewById(R.id.top_app_bar_menu);
        setSupportActionBar(topAppMenu);
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        //bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);
        //setSupportActionBar(bottomAppMenu);

        findViewById(R.id.btn_m_citati).setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, CitatiActivity.class));});
        findViewById(R.id.btn_m_kazalo).setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, KazaloActivity.class));});
        findViewById(R.id.btn_m_slikaj).setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, SlikajActivity.class));});
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    public void poimenujStran(int ime) {
        Toolbar topAppMenu = findViewById(R.id.top_app_bar_menu);
        setSupportActionBar(topAppMenu);
        getSupportActionBar().setTitle(ime);
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        //setSupportActionBar(bottomAppMenu);
    }

    public void obKlikuNaprej(){
        //metoda je drugačna v vsaki aktivnosti, nadomači strani ne pripelje nikamor
        Toast.makeText(this, "Uporabi gumbe.", Toast.LENGTH_LONG).show();
    }
    public boolean obKlikuSpodnjeNavigacijskeVrstice(MenuItem element) {
        int idGumba = element.getItemId();
        if (idGumba == R.id.btn_n_nazaj) {
            Toast.makeText(this, "Nazaj", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else if (idGumba == R.id.btn_n_domov) {
            Intent odpriDomov = new Intent(this, MainActivity.class);
            odpriDomov.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(odpriDomov);
            finish();
            return true;
        } else if (idGumba == R.id.btn_n_naprej) {
            Toast.makeText(this, "Naprej", Toast.LENGTH_SHORT).show();
            obKlikuNaprej();
            return true;
        }
        return false;
    }
}