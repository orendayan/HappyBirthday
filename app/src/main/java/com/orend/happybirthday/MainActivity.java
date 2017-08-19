package com.orend.happybirthday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orend.happybirthday.fragments.SetBirthdayFragment;
import com.orend.happybirthday.model.Child;

public class MainActivity extends AppCompatActivity
        implements SetBirthdayFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, SetBirthdayFragment.newInstance(), "childBirthday")
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Child child) {

    }

}
