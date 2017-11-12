package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.juliatimofeeva.currencyconverter.CurrencyApplication;
import com.juliatimofeeva.currencyconverter.R;
import com.juliatimofeeva.currencyconverter.presentation.entities.ConverterUiModel;

import java.util.Set;

import static android.view.View.GONE;

public class ConverterActivity extends AppCompatActivity implements ConverterView,
        View.OnClickListener {

    private LinearLayout llScreen;
    private Spinner spFrom;
    private Spinner spTo;
    private EditText etValue;
    private Button bnProcess;
    private ProgressBar pbLoading;
    private TextView tvResult;
    private ConvertPresenter presenter;
    private  ArrayAdapter<String> currencyNamesAdapter;
    private int positionFrom;
    private int positionTo;
    private ConverterUiModel currentUiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llScreen = (LinearLayout) findViewById(R.id.ll_screen);
        spFrom = (Spinner) findViewById(R.id.sp_from);
        spTo = (Spinner) findViewById(R.id.sp_to);
        etValue = (EditText) findViewById(R.id.et_value);
        bnProcess = (Button) findViewById(R.id.bn_process);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        tvResult = (TextView) findViewById(R.id.tv_result);
        bnProcess.setOnClickListener(this);
        etValue.setOnEditorActionListener(editorActionListener);
        presenter = CurrencyApplication.getFactoryProvider()
                .getPresentationLayerFactory()
                .getConvertPresenter();
    }

    private AdapterView.OnItemSelectedListener currencyAdapterListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int viewId = adapterView.getId();
            switch (viewId) {
                case R.id.sp_from:
                    presenter.setCurrencyFrom(l);
                    break;
                case R.id.sp_to:
                    presenter.setCurrencyTo(l);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_GO) {
                presenter.processRequest(spFrom.getSelectedItemPosition(),
                        spTo.getSelectedItemPosition(),
                        etValue.getText().toString().trim());
                tvResult.setText("");
                return true;
            }
            return false;
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

    private void showCurrencyNames(@NonNull Set<String> names) {
        String[] namesString = new String[names.size()];
        currencyNamesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                 names.toArray(namesString));
        currencyNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(currencyNamesAdapter);
        spTo.setAdapter(currencyNamesAdapter);
        spFrom.setOnItemSelectedListener(currencyAdapterListner);
        spTo.setOnItemSelectedListener(currencyAdapterListner);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.bn_process:
                presenter.processRequest(spFrom.getSelectedItemPosition(),
                        spTo.getSelectedItemPosition(),
                        etValue.getText().toString().trim());
                tvResult.setText("");
                break;
            default:
                break;

        }
    }


    @Override
    public void displayUiModel(@NonNull ConverterUiModel converterUiModel) {
        currentUiModel = converterUiModel;
        if (converterUiModel.isCurrencyDataInProgress()) {
            bnProcess.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            Toast.makeText(this, "currency data in progress", Toast.LENGTH_SHORT).show();
        } else if (converterUiModel.isConvertionInProgress()) {
            bnProcess.setVisibility(GONE);
            pbLoading.setVisibility(View.VISIBLE);
            Toast.makeText(this, "convertion in progress", Toast.LENGTH_SHORT).show();
        } else if (converterUiModel.getConvertionResult() != null) {
            pbLoading.setVisibility(View.GONE);
            bnProcess.setVisibility(View.VISIBLE);
            tvResult.setText(converterUiModel.getConvertionResult());
            Toast.makeText(this, "convertion result", Toast.LENGTH_SHORT).show();
        } else if (converterUiModel.getErrorMessage() != null) {
            pbLoading.setVisibility(View.GONE);
            bnProcess.setVisibility(View.VISIBLE);
            if (converterUiModel.getCurrencyData() == null) {
                bnProcess.setEnabled(false);
            }
            Snackbar snackbar = Snackbar.make(llScreen, converterUiModel.getErrorMessage(),Snackbar.LENGTH_LONG);
            snackbar.show();

        } else if (converterUiModel.getCurrencyData() != null) {
            pbLoading.setVisibility(View.INVISIBLE);
            bnProcess.setVisibility(View.VISIBLE);
            bnProcess.setEnabled(true);
            Toast.makeText(this, "currency data success", Toast.LENGTH_SHORT).show();
        }

        if ((converterUiModel.getCurrencyData() != null)
                && (converterUiModel.getCurrencyData().size() > 0)
                && (currencyNamesAdapter == null)) {
            showCurrencyNames(converterUiModel.getCurrencyData());
            positionFrom = converterUiModel.getPositionFrom();
            positionTo = converterUiModel.getPositionTo();
            spFrom.setSelection(positionFrom);
            spTo.setSelection(positionTo);
        }

        if (currencyNamesAdapter != null) {
            if (positionFrom != converterUiModel.getPositionFrom()) {
                spFrom.setSelection(converterUiModel.getPositionFrom());
            }
            if (positionTo != converterUiModel.getPositionTo()) {
                spTo.setSelection(converterUiModel.getPositionTo());
            }
        }
    }
}
