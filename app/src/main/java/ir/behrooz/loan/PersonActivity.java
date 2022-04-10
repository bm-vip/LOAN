package ir.behrooz.loan;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import ir.behrooz.loan.common.BaseActivity;
import ir.behrooz.loan.common.Utils;
import ir.behrooz.loan.common.sql.DBUtil;
import ir.behrooz.loan.entity.CashtEntity;
import ir.behrooz.loan.entity.PersonEntity;
import ir.behrooz.loan.entity.PersonEntityDao;

import static ir.behrooz.loan.common.StringUtil.fixWeakCharacters;
import static ir.behrooz.loan.common.StringUtil.isMobileValid;

public class PersonActivity extends BaseActivity {

    private EditText name, family, phone, nationalCode;
    private Long personId;
    private PersonEntityDao personEntityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
//        titleBar.setText(getString(R.string.title_activity_person));
        Utils.askForPermission(this, Manifest.permission.READ_CONTACTS, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }

        personEntityDao = DBUtil.getWritableInstance(this).getPersonEntityDao();
        name = findViewById(R.id.name);
        family = findViewById(R.id.family);
        phone = findViewById(R.id.phone);
        nationalCode = findViewById(R.id.nationalCode);
        long subscribCode = personEntityDao.count() + 1L;
        nationalCode.setText(subscribCode + "");

        if (getIntent().hasExtra("personId")) {
            personId = getIntent().getExtras().getLong("personId");
            loadForm();
        } else {
            personId = null;
        }
        nationalCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    savePerson(v);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void loadForm() {
        PersonEntity personEntity = personEntityDao.load(personId);

        name.setText(personEntity.getName());
        family.setText(personEntity.getFamily());
        phone.setText(personEntity.getPhone());
        nationalCode.setText(personEntity.getNationalCode());
    }

    public void savePerson(View view) {
        name.setError(null);
        family.setError(null);
        phone.setError(null);
        nationalCode.setError(null);

        View focusView = null;
        boolean cancel = focusView != null;

        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        } else if (TextUtils.isEmpty(family.getText())) {
            family.setError(getString(R.string.error_field_required));
            focusView = family;
            cancel = true;
        } else if (phone.getText().toString().isEmpty()) {
            phone.setError(getString(R.string.error_field_required));
            focusView = phone;
            cancel = true;
        } else if (!isMobileValid(phone.getText().toString())) {
            phone.setError(getString(R.string.error_field_invalid));
            focusView = phone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            PersonEntity personEntity;
            if (personId == null) {
                personEntity = new PersonEntity();
                personEntity.setCashId(new CashtEntity(this).getId());
            }
            else
                personEntity = personEntityDao.load(personId);

            personEntity.setName(name.getText().toString());
            personEntity.setFamily(family.getText().toString());
            personEntity.setPhone(phone.getText().toString());
            personEntity.setNationalCode(nationalCode.getText().toString());

            personEntityDao.save(personEntity);

            finish();
        }
    }

    public void addNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 99);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (99):
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        Uri contactData = data.getData();
                        Cursor cur = getContentResolver().query(contactData, null, null, null, null);
                        ContentResolver contentResolver = getContentResolver();

                        if (cur.moveToFirst()) {
                            String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            Cursor phoneCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                            if (phoneCur.moveToFirst()) {
                                String[] displayName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).split(" ");
                                StringBuilder builder = new StringBuilder();
                                for (int i = 0; i < displayName.length; i++) {
                                    if (i == 0)
                                        this.name.setText(displayName[i]);
                                    else {
                                        builder.append(displayName[i]);
                                        builder.append(" ");
                                    }
                                }
                                this.family.setText(builder.toString());
                                String number = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                this.phone.setText(fixWeakCharacters(number.trim().replace(" ", "")));
                            }
                            id = null;
                            phoneCur = null;
                        }
                        contentResolver = null;
                        cur = null;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
