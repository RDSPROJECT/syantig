package com.example.syantig.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.syantig.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //view
    private FragmentContainerView nav;
    private View bottom_navigationview;
    private TextView title;

    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findview
        nav=findViewById(R.id.nav_host_fragment);
        bottom_navigationview=findViewById(R.id.bottom_navigation);
        title=findViewById(R.id.title_home);

navController=Navigation.findNavController(this,R.id.nav_host_fragment);

        bottom_navigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener());
        public boolean onNavigationItemSelected(@NonNull MenuItem Item)
        {


            switch (item.getItemId())
            {


                case R.id.nav_home:
                    title.setText("R.string.Home");
                    navController.navigate(R.id.homeFragment);
                    return true;

                case R.id.nav_search:
                    title.setText("R.string.Search");
                    navController.navigate(R.id.searchFragment);
                    return true;

                case R.id.nav_favorites:
                    title.setText("R.string.Favorites");
                    navController.navigate(R.id.favoritFragment);
                    return true;

                case R.id.nav_profile:
                    title.setText("R.string.Profile");
                    navController.navigate(R.id.profileFragment);
                    return true;
            }


            return false;

    });


}
