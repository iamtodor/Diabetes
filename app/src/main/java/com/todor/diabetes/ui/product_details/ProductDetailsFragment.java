package com.todor.diabetes.ui.product_details;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.todor.diabetes.Constants;
import com.todor.diabetes.R;
import com.todor.diabetes.db.ProductFunctionality;
import com.todor.diabetes.models.Product;
import com.todor.diabetes.ui.BaseFragment;
import com.todor.diabetes.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ProductDetailsFragment extends BaseFragment {

    @Bind(R.id.edt_product_value_for_calculation) EditText        edtProductValueForCalculation;
    @Bind(R.id.tv_product_result_value)           TextView        productResultValue;
    @Bind(R.id.btn_gram)                          RadioButton     btnGram;
    @Bind(R.id.btn_bread_unit)                    RadioButton     btnBreadUnit;
    @Bind(R.id.edt_wrapper)                       TextInputLayout edt_product_value_wrapper;

    private ProductFunctionality dbManager;
    private Product              product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, v);

        product = getActivity().getIntent().getParcelableExtra(Constants.PRODUCT_KEY);
        edt_product_value_wrapper.setHint(getString(R.string.hint_product_GL));
        productResultValue.setText(String.format(getString(R.string.value_gram), 0.0));

        edtProductValueForCalculation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = getEdtProductValueForCalculation();
                if (btnGram.isChecked()) {
                    float result = getGrammFromBreadUnits(value);
                    productResultValue.setText(String.format(getString(R.string.value_gram), result));
                } else if (btnBreadUnit.isChecked()) {
                    float result = getBreadUnitsFromGramm(value);
                    productResultValue.setText(String.format(getString(R.string.value_bread_unit), result));
                }
            }
        });

        return v;
    }

    public float getGrammFromBreadUnits(int value) {
        return value * Utils.getGlycemicIndex(getActivity()) / (product.carbohydrates / 100);
    }

    public float getBreadUnitsFromGramm(int value) {
        return value * (product.carbohydrates / 100) / Utils.getGlycemicIndex(getActivity());
    }

    private void clickChangeBreadUnit() {
        int value = getEdtProductValueForCalculation();
        float result = getBreadUnitsFromGramm(value);
        productResultValue.setText(String.format(getString(R.string.value_bread_unit), result));
        edt_product_value_wrapper.setHint(getString(R.string.hint_product_gram));
    }

    public void clickChangeBtnGram() {
        int value = getEdtProductValueForCalculation();
        float result = getGrammFromBreadUnits(value);
        productResultValue.setText(String.format(getString(R.string.value_gram), result));
        edt_product_value_wrapper.setHint(getString(R.string.hint_product_GL));
    }

    public int getEdtProductValueForCalculation() {
        try {
            return Integer.parseInt(edtProductValueForCalculation.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @OnClick(R.id.btn_bread_unit)
    public void btnBreadUnitClick() {
        clickChangeBreadUnit();
    }

    @OnCheckedChanged(R.id.btn_bread_unit)
    public void btnBreadUnitChanged() {
        clickChangeBreadUnit();
    }

    @OnClick(R.id.btn_gram)
    public void btnGramClick() {
        clickChangeBtnGram();
    }

    @OnCheckedChanged(R.id.btn_gram)
    public void btnGramChanged() {
        clickChangeBtnGram();
    }

    @OnClick(R.id.btn_plus)
    public void btnPlusClick() {
        edtProductValueForCalculation.setText(String.valueOf(getEdtProductValueForCalculation() + 1));
    }

    @OnClick(R.id.btn_minus)
    public void btnMinusClick() {
        int value = getEdtProductValueForCalculation();
        if (value - 1 < 0) {
            Toast.makeText(getActivity(), getString(R.string.edit_positive_value), Toast.LENGTH_SHORT).show();
            return;
        }
        edtProductValueForCalculation.setText(String.valueOf(value - 1));
    }

    @OnClick(R.id.btn_favorite)
    public void btnFavoriteClick() {
        if (dbManager == null) {
            dbManager = new ProductFunctionality(getActivity());
        }
        if (!product.isFavorite) {
            product.isFavorite = true;
            Toast.makeText(getActivity(), R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
        } else {
            product.isFavorite = false;
            Toast.makeText(getActivity(), R.string.deleted_from_favorite, Toast.LENGTH_SHORT).show();
        }
        dbManager.updateProduct(product);
    }

    @OnClick(R.id.btn_eatNow)
    public void btnEatNowClick() {
        // TODO 'Move current product to table fragment'
    }

    @Override
    public String getFragmentTitle() {
        return getResources().getString(R.string.title_product_details);
    }
}
