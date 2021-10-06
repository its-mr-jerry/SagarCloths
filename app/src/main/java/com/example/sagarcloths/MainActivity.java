package com.example.sagarcloths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sagarcloths.fragments.Categories;
import com.example.sagarcloths.fragments.Chat;
import com.example.sagarcloths.fragments.HomeFragment;
import com.example.sagarcloths.fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNav);


        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragTransaction=getSupportFragmentManager().beginTransaction() ;
                switch (item.getItemId()) {
                    case R.id.nav_home: fragTransaction.replace(R.id.fragmentContainerView2,new HomeFragment());
                        break;

                    case R.id.nav_categories:fragTransaction.replace(R.id.fragmentContainerView2,new Categories());
                        break;
                    case R.id.nav_chat:fragTransaction.replace(R.id.fragmentContainerView2,new Chat());
                        break;
                    case R.id.nav_profile:fragTransaction.replace(R.id.fragmentContainerView2,new Profile());
                        break;

                }
                fragTransaction.commit();
                return true;
            }
        });

    }
}