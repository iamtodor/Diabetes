package com.todor.diabetes.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.todor.diabetes.R;
import com.todor.diabetes.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.edt_glycemic_index) EditText edtGlycemicIndex;
    @Bind(R.id.btn_update) Button btnUpdateGlycemicIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUpdateGlycemicIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = edtGlycemicIndex.getText().toString();
                float valueFloat = 0;
                try {
                    valueFloat = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    Toast.makeText(SettingsActivity.this, R.string.edit_correct_value, Toast.LENGTH_SHORT).show();
                }
                Utils.setGlycemicIndex(SettingsActivity.this, valueFloat);
                Toast.makeText(SettingsActivity.this, R.string.index_was_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return (true);
        }
        return false;
    }
}
