package jbcu10.dev.medalert.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.db.UserRepository;
import jbcu10.dev.medalert.fragments.FirstAidFragments;
import jbcu10.dev.medalert.fragments.MapFragments;
import jbcu10.dev.medalert.fragments.MedicineFragments;
import jbcu10.dev.medalert.fragments.PatientFragments;
import jbcu10.dev.medalert.fragments.ReminderFragments;
import jbcu10.dev.medalert.model.User;


public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserRepository userRepository ;
    Fragment fragment;
    public static int selectedItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userRepository = new UserRepository(HomeActivity.this);

        if(selectedItem==0) {
            fragment = new ReminderFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
            navigationView.setCheckedItem( R.id.alarm);
        }if(selectedItem==1) {
            fragment = new MedicineFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
            navigationView.setCheckedItem( R.id.medicines);

        }if(selectedItem==2) {
            fragment = new PatientFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
            navigationView.setCheckedItem( R.id.patients);

        }if(selectedItem==3) {
            fragment = new FirstAidFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
            navigationView.setCheckedItem( R.id.firstaid);

        }

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.nav_name);
        TextView nav_email = hView.findViewById(R.id.nav_email);

        User user = userRepository.getById(1);
        if(user.getFirstName()!=null){
            nav_user.setText(user.getFirstName()+" "+user.getLastName());
            nav_email.setText(user.getEmail());

        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitApplication(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            exitApplication(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.patients) {
            fragment = new PatientFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
        } else if (id == R.id.medicines) {
            fragment = new MedicineFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
        } else if (id == R.id.firstaid) {
            fragment = new FirstAidFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
        } else if (id == R.id.alarm) {
            fragment = new ReminderFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
        }
        else if (id == R.id.map) {
            fragment = new MapFragments();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_new, fragment);
            ft.commit();
        }else if (id == R.id.nav_exit) {
            exitApplication(this);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
