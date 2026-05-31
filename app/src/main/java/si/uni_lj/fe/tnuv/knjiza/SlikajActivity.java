package si.uni_lj.fe.tnuv.knjiza;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;

public class SlikajActivity extends MainActivity  {

    private static final String TAG = SlikajActivity.class.getSimpleName();
    static final int POSNEMI_SLIKO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slikaj);
        poimenujStran(R.string.text_slikaj);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);

        findViewById(R.id.btn_s_slikaj).setOnClickListener(v -> odpriKamero());
        odpriKamero();
    }

    private void odpriKamero() {
        Intent posnemiSliko = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(posnemiSliko, POSNEMI_SLIKO);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Napaka" + e.getMessage(), e);
        }
    }

    @Override
    protected void onActivityResult(int poizvedbaKoda, int rezultatKoda, Intent zajetaSlika) {
        super.onActivityResult(poizvedbaKoda, rezultatKoda, zajetaSlika);
        if (poizvedbaKoda == POSNEMI_SLIKO && rezultatKoda == RESULT_OK) {
            if (zajetaSlika == null || zajetaSlika.getExtras() == null) {
                Log.e("Camera", "No data returned from camera");
                return;
            }
            Bitmap slika = (Bitmap) zajetaSlika.getExtras().get("data");;
            if (slika == null) {
                Log.e("Camera", "Bitmap is null");
                return;
            }
            //Pripravi sliko za posiljanje v novo aktivnost - pretvorba v bitno tabelo
            ByteArrayOutputStream slikaVTok = new ByteArrayOutputStream();
            slika.compress(Bitmap.CompressFormat.PNG, 100, slikaVTok);
            byte[] bitniTok = slikaVTok.toByteArray();
            //Odpri novo aktivnost - PretvoriActivity
            Intent odpriPretvorbo = new Intent(SlikajActivity.this, PretvoriActivity.class);
            odpriPretvorbo.putExtra("slika", bitniTok);
            startActivity(odpriPretvorbo);
        }
    }
}