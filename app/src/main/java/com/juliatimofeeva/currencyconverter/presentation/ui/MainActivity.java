package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.juliatimofeeva.currencyconverter.CurrencyApplication;
import com.juliatimofeeva.currencyconverter.R;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements ConverterView,
        View.OnClickListener {


    private Spinner spFrom;
    private Spinner spTo;
    private EditText etValue;
    private Button bnProcess;
    private ProgressBar pbLoading;
    private TextView tvResult;
    private ConvertPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spFrom = (Spinner) findViewById(R.id.sp_from);
        spTo = (Spinner) findViewById(R.id.sp_to);
        etValue = (EditText) findViewById(R.id.et_value);
        bnProcess = (Button) findViewById(R.id.bn_process);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        tvResult = (TextView) findViewById(R.id.tv_result);
        bnProcess.setOnClickListener(this);
        presenter = CurrencyApplication.getFactoryProvider()
                .getPresentationLayerFactory()
                .getConvertPresenter();
    }

    private AdapterView.OnItemSelectedListener currencyAdapterListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void showCurrencyNames(@NonNull Set<String> names) {
        String[] namesString = new String[names.size()];
        ArrayAdapter<String> currencyNamesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                 names.toArray(namesString));
        spFrom.setAdapter(currencyNamesAdapter);
        spTo.setAdapter(currencyNamesAdapter);
        //spFrom.setOnItemClickListener();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showResult(String result) {
        tvResult.setText(result);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.bn_process:
                presenter.processRequest(spFrom.getSelectedItemPosition(),
                        spTo.getSelectedItemPosition(),
                        etValue.getText().toString().trim());
                break;
            default:
                break;

        }
    }
}
