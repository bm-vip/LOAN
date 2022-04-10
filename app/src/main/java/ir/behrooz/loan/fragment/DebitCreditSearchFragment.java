package ir.behrooz.loan.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mojtaba.materialdatetimepicker.date.DatePickerDialog;
import com.mojtaba.materialdatetimepicker.utils.LanguageUtils;
import com.mojtaba.materialdatetimepicker.utils.PersianCalendar;

import java.util.List;

import ir.behrooz.loan.R;
import ir.behrooz.loan.common.CompleteListener;
import ir.behrooz.loan.common.Constants;
import ir.behrooz.loan.common.DateUtil;
import ir.behrooz.loan.common.FontChangeCrawler;
import ir.behrooz.loan.model.PersonModel;

import static ir.behrooz.loan.common.DateUtil.addZero;
import static ir.behrooz.loan.common.StringUtil.fixWeakCharacters;
import static ir.behrooz.loan.common.StringUtil.isNullOrEmpty;
import static ir.behrooz.loan.common.StringUtil.moneySeparator;
import static ir.behrooz.loan.common.StringUtil.onChangedEditText;
import static ir.behrooz.loan.common.StringUtil.removeSeparator;

/**
 * Created by Behrooz Mohamadi on 16/10/28.
 */
public class DebitCreditSearchFragment extends DialogFragment {
    private CompleteListener completeListener;
    private EditText fullName, loanName, value, date,description;
    private TextView fullNameLabel, loanNameLabel, valueLabel, dateLabel,descriptionLabel;
    private ImageButton fullNameBtn;
    private Button search, cancel;
    private DateListener dateListener;
    private List<PersonModel> personModels;

    public static DebitCreditSearchFragment newInstance(String color,Long cashId) {
        DebitCreditSearchFragment frag = new DebitCreditSearchFragment();
        Bundle args = new Bundle();
        args.putString("color", color);
        args.putLong("cashId", cashId);
        frag.setArguments(args);
        return frag;
    }

    public CompleteListener getCompleteListener() {
        return completeListener;
    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_debit_credit_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fullNameBtn = view.findViewById(R.id.fullNameBtn);
        fullName = view.findViewById(R.id.fullNameValue);
        fullNameLabel = view.findViewById(R.id.fullNameLabel);
        search = view.findViewById(R.id.search);
        cancel = view.findViewById(R.id.cancel);
        date = view.findViewById(R.id.date);
        dateLabel = view.findViewById(R.id.dateLabel);
        loanName = view.findViewById(R.id.loanName);
        loanNameLabel = view.findViewById(R.id.loanNameLabel);
        value = view.findViewById(R.id.value);
        valueLabel = view.findViewById(R.id.valueLabel);
        description = view.findViewById(R.id.description);
        descriptionLabel = view.findViewById(R.id.descriptionLabel);
        TextView header = view.findViewById(R.id.actionSearchHeader);
        final String color = getArguments().getString("color");
        final Long cashId = getArguments().getLong("cashId");

        header.setBackgroundColor(Color.parseColor(color));
        search.setTextColor(Color.parseColor(color));
        fullNameLabel.setTextColor(Color.parseColor(color));
        loanNameLabel.setTextColor(Color.parseColor(color));
        dateLabel.setTextColor(Color.parseColor(color));
        valueLabel.setTextColor(Color.parseColor(color));
        descriptionLabel.setTextColor(Color.parseColor(color));

        fullNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PersonSearchFragment personSearchFragment = PersonSearchFragment.newInstance(color, cashId);
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
                            fullName.setText(builder.toString());
                        }
                    }
                });
                personSearchFragment.show(fm, "fragment_add_person");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.setAccentColor(Color.parseColor(color));
        }
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && dpd != null) {
                    dpd.show(getActivity().getFragmentManager(), "dateDialog");
                    search.requestFocus();
                }
            }
        });

        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangedEditText(getContext(), this, value, charSequence, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder builder = new StringBuilder();
                if (personModels != null && personModels.size() > 0)
                    builder.append(" AND DC.PERSON_ID in (".concat(PersonModel.getIds(personModels)).concat(")"));
                if (!TextUtils.isEmpty(loanName.getText()))
                    builder.append(" AND L.NAME LIKE '%".concat(loanName.getText().toString()).concat("%'"));
                if (!TextUtils.isEmpty(value.getText()))
                    builder.append(" AND DC.VALUE = ".concat(removeSeparator(getContext(), value.getText().toString()) + ""));
                if (!TextUtils.isEmpty(date.getText()))
                    builder.append(" AND DC.DATE = ".concat(DateUtil.toGregorian(date.getText().toString()).getTime() + ""));
                if (!TextUtils.isEmpty(description.getText()))
                    builder.append(" AND DC.DESCRIPTION LIKE '%".concat(description.getText().toString()).concat("%'"));


                if (getCompleteListener() != null)
                    getCompleteListener().onComplete(builder.toString());
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        new FontChangeCrawler(getContext().getAssets(), Constants.IRANSANS_MD).replaceFonts((ViewGroup) view);
    }

    public class DateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String d = year + "/" + addZero(++monthOfYear) + "/" + addZero(dayOfMonth);
            date.setText(LanguageUtils.getPersianNumbers(fixWeakCharacters(d)));
        }
    }
}
