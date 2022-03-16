package task.emango.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import task.emango.R;
import task.emango.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        addFragment(HomeFragment.newInstance(),false);

    }

    public void addFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.container,fragment);
        if (addToBackStack) transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() > 1){
            fragmentManager.popBackStack();
            return;
        }

        super.onBackPressed();
    }
}