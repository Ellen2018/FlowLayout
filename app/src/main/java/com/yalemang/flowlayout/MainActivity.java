package com.yalemang.flowlayout;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.yalemang.flowlayout.adapter.MyFlowAdapter;
import com.yalemang.flowlayout.databinding.ActivityMainBinding;
import com.yalemang.flowlayout.library.FlowAdapter;
import com.yalemang.flowlayout.library.FlowLayout;
import com.yalemang.flowlayout.library.FlowLayout2;
import com.yalemang.flowlayout.library.FlowViewHolder;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FlowLayout2 flowLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout2 = findViewById(R.id.flow);
        flowLayout2.setAdapter(new MyFlowAdapter());
    }


}