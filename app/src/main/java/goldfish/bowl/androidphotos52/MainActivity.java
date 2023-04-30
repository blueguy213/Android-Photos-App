package goldfish.bowl.androidphotos52;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import goldfish.bowl.androidphotos52.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().setReorderingAllowed(true).replace(R.id.main_fragment_container, AlbumsView.class, null).commit();

        binding.searchViewFab.setOnClickListener(view -> {

            // TODO: Save all changes in the last fragment here

            // Switch to OpenAlbumView fragment here
            FragmentManager fm1 = getSupportFragmentManager();
            fm1.beginTransaction().setReorderingAllowed(true).replace(R.id.main_fragment_container, SearchView.class, null).commit();
        });

        binding.albumsViewFab.setOnClickListener(view -> {

            // TODO: Save all changes in the last fragment here

            // Switch to AlbumsView fragment here
            FragmentManager fm12 = getSupportFragmentManager();
            fm12.beginTransaction().setReorderingAllowed(true).replace(R.id.main_fragment_container, AlbumsView.class, null).commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}