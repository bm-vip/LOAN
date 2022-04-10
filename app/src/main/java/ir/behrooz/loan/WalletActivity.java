package ir.behrooz.loan;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mojtaba.materialdatetimepicker.date.DatePickerDialog;
import com.mojtaba.materialdatetimepicker.utils.LanguageUtils;
import com.mojtaba.materialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

import ir.behrooz.loan.common.BaseActivity;
import ir.behrooz.loan.common.CompleteListener;
import ir.behrooz.loan.common.DateUtil;
import ir.behrooz.loan.common.sql.DBUtil;
import ir.behrooz.loan.entity.CashtEntity;
import ir.behrooz.loan.entity.PersonEntity;
import ir.behrooz.loan.entity.PersonEntityDao;
import ir.behrooz.loan.entity.WalletEntity;
import ir.behrooz.loan.entity.WalletEntityDao;
import ir.behrooz.loan.fragment.PersonSearchFragment;
import ir.behrooz.loan.model.PersonModel;

import static ir.behrooz.loan.common.DateUtil.addZero;
import static ir.behrooz.loan.common.DateUtil.toGregorian;
import static ir.behrooz.loan.common.StringUtil.fixWeakCharacters;
import static ir.behrooz.loan.common.StringUtil.isNullOrEmpty;
import static ir.behrooz.loan.common.StringUtil.moneySeparator;
import static ir.behrooz.loan.common.StringUtil.onChangedEditText;
import static ir.behrooz.loan.common.StringUtil.removeSeparator;

public class WalletActivity extends BaseActivity {

    private EditText date, value, personFullName, description;
    private Switch statusSwitch;
    private Long walletId;
    private DateListener dateListener;
    private List<PersonModel> personModels;
    WalletEntityDao walletEntityDao;
    PersonEntityDao personEntityDao;
    CashtEntity cashtEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        cashtEntity = new CashtEntity(this);
//        titleBar.setText(getString(R.string.title_activity_person));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#81C784"));
        }

        walletEntityDao = DBUtil.getReadableInstance(this).getWalletEntityDao();
        personEntityDao = DBUtil.getReadableInstance(this).getPersonEntityDao();
        statusSwitch = findViewById(R.id.statusSwitch);
        value = findViewById(R.id.value);
        personFullName = findViewById(R.id.fullNameValue);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{Color.parseColor("#81C784"), Color.parseColor("#81C784"), Color.GRAY}
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusSwitch.setThumbTintList(colorStateList);
            statusSwitch.setTrackTintList(colorStateList);
        }
        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    statusSwitch.setText(getString(R.string.deposit));
                else statusSwitch.setText(getString(R.string.withdrawal));
            }
        });
        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangedEditText(context, this, value, charSequence, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateListener = new DateListener();
        PersianCalendar now = new PersianCalendar();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                dateListener,
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.vibrate(true);
        dpd.setTitle(getString(R.string.date));
        dpd.setAccentColor(Color.parseColor("#81C784"));
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && dpd != null) {
                    dpd.show(getFragmentManager(), "dateDialog");
                    description.requestFocus();
                }
            }
        });

        if (getIntent().hasExtra("walletId")) {
            walletId = getIntent().getExtras().getLong("walletId");
            loadForm();
        } else {
            walletId = null;
        }
        if (getIntent().hasExtra("personId")) {
            PersonEntity personEntity = personEntityDao.load(getIntent().getExtras().getLong("personId"));
            personModels = new ArrayList<>();
            personModels.add(PersonModel.getModel(personEntity));
            personFullName.setText(String.format("%s %s", personEntity.getName(), personEntity.getFamily()));
        }
        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveWallet(v);
                    handled = true;
                }
                return handled;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("dateDialog");
        if (dpd != null) dpd.setOnDateSetListener(dateListener);
    }

    public class DateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String d = year + "/" + addZero(++monthOfYear) + "/" + addZero(dayOfMonth);
            date.setText(LanguageUtils.getPersianNumbers(fixWeakCharacters(d)));
        }
    }

    private void loadForm() {
        WalletEntity walletEntity = walletEntityDao.load(walletId);

        value.setText(moneySeparator(context, walletEntity.getValue()));
        date.setText(DateUtil.toPersianString(walletEntity.getDate(), false));
        PersonEntity personEntity = personEntityDao.load(walletEntity.getPersonId());
        personFullName.setText(String.format("%s %s", personEntity.getName(), personEntity.getFamily()));
        description.setText(walletEntity.getDescription());
        personModels = new ArrayList<>();
        personModels.add(PersonModel.getModel(personEntity));
        statusSwitch.setChecked(walletEntity.getStatus());
    }

    public void saveWallet(View view) {
        date.setError(null);
        value.setError(null);

        View focusView = null;
        boolean cancel = focusView != null;

        if (TextUtils.isEmpty(date.getText())) {
            date.setError(getString(R.string.error_field_required));
            focusView = date;
            cancel = true;
        } else if (TextUtils.isEmpty(personFullName.getText())) {
            personFullName.setError(getString(R.string.error_field_required));
            focusView = personFullName;
            cancel = true;
        } else if (TextUtils.isEmpty(value.getText())) {
            value.setError(getString(R.string.error_field_required));
            focusView = value;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            for (PersonModel personModel : personModels) {
                WalletEntity walletEntity;
                if (walletId == null) {
                    walletEntity = new WalletEntity();
                    walletEntity.setCashId(cashtEntity.getId());
                } else
                    walletEntity = walletEntityDao.load(walletId);

                walletEntity.setPersonId(personModel.getId());
                walletEntity.setDescription(description.getText().toString());
                walletEntity.setStatus(statusSwitch.isChecked());
                walletEntity.setDate(toGregorian(date.getText().toString()));
                walletEntity.setValue(removeSeparator(context, value.getText().toString()));

                walletEntityDao.save(walletEntity);
            }
            finish();
        }
    }

    public void addPerson(View view) {
        FragmentManager fm = getSupportFragmentManager();
        PersonSearchFragment personSearchFragment = PersonSearchFragment.newInstance("#81C784", cashtEntity.getId());
        personSearchFragment.setCompleteListener(new CompleteListener() {
            @Override
            public void onComplete(Object obj) {
                personModels = (List<PersonModel>) obj;
                if (personModels != null && !personModels.isEmpty()) {
                    StringBuilder builder = new StringBuilder(personModels.get(0).getFullName());
                    if (personModels.size() > 1) {
                        builder.append(" و ");
                        builder.append(personModels.size() - 1);
                        builder.append(" عضو دیگر");
                    }
                    personFullName.setText(builder.toString());
                }
            }
        });
        personSearchFragment.show(fm, "fragment_add_person");
    }

}
