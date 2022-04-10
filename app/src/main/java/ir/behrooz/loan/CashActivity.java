package ir.behrooz.loan;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import ir.behrooz.loan.common.AlarmReceiver;
import ir.behrooz.loan.common.BaseActivity;
import ir.behrooz.loan.common.sql.DBUtil;
import ir.behrooz.loan.entity.CashtEntity;
import ir.behrooz.loan.entity.CashtEntityDao;

public class CashActivity extends BaseActivity {

    private EditText name, currencyType;
    private Switch withDeposit, checkCashRemain, affectNext, notifyDayOfLoan;
    private Long cashId;
    private CashtEntityDao cashtEntityDao;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        alarmReceiver = new AlarmReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }

        cashtEntityDao = DBUtil.getWritableInstance(this).getCashtEntityDao();
        name = findViewById(R.id.name);
        currencyType = findViewById(R.id.currencyType);
        withDeposit = findViewById(R.id.withDeposit);
        checkCashRemain = findViewById(R.id.checkCashRemain);
        affectNext = findViewById(R.id.affectNext);
        notifyDayOfLoan = findViewById(R.id.notifyDayOfLoan);
        notifyDayOfLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    alarmReceiver.setAlarm(context);
                } else {
                    alarmReceiver.cancelAlarm(context);
                }
            }
        });
        withDeposit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    withDeposit.setText(getString(R.string.withDeposit));
                    checkCashRemain.setChecked(true);
                    checkCashRemain.setEnabled(true);
                } else {
                    withDeposit.setText(getString(R.string.withoutDeposit));
                    checkCashRemain.setChecked(false);
                    checkCashRemain.setEnabled(false);
                }
            }
        });
        if (getIntent().hasExtra("cashId")) {
            cashId = getIntent().getExtras().getLong("cashId");
            loadForm();
        } else {
            cashId = null;
        }
    }

    private void loadForm() {
        CashtEntity cashtEntity = cashtEntityDao.load(cashId);
        name.setText(cashtEntity.getName());
        currencyType.setText(cashtEntity.getCurrencyType());
        withDeposit.setChecked(cashtEntity.getWithDeposit());
        checkCashRemain.setChecked(cashtEntity.getCheckCashRemain());
        affectNext.setChecked(cashtEntity.getAffectNext());
        notifyDayOfLoan.setChecked(cashtEntity.getNotifyDayOfLoan());
    }

    public void saveCash(View view) {
        name.setError(null);
        currencyType.setError(null);

        View focusView = null;
        boolean cancel = focusView != null;

        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        } else if (TextUtils.isEmpty(currencyType.getText())) {
            currencyType.setError(getString(R.string.error_field_required));
            focusView = currencyType;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            CashtEntity cashEntity;
            if (cashId == null) {
                cashEntity = new CashtEntity();
            } else
                cashEntity = cashtEntityDao.load(cashId);

            cashEntity.setName(name.getText().toString());
            cashEntity.setCurrencyType(currencyType.getText().toString());
            cashEntity.setWithDeposit(withDeposit.isChecked());
            cashEntity.setCheckCashRemain(checkCashRemain.isChecked());
            cashEntity.setNotifyDayOfLoan(notifyDayOfLoan.isChecked());
            cashEntity.setAffectNext(affectNext.isChecked());

            cashtEntityDao.save(cashEntity);
            finish();
        }
    }
}
