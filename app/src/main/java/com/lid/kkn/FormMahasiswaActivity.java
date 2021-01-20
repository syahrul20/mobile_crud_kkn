package com.lid.kkn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lid.kkn.Utils.AppConstant;
import com.lid.kkn.Utils.ConvertionUtils;
import com.lid.kkn.Utils.LoadingDialog;
import com.lid.kkn.field.MahasiswaField;

public class FormMahasiswaActivity extends AppCompatActivity {

    final Handler handler = new Handler(Looper.getMainLooper());
    public enum Mode {
        ADD, UPDATE
    }

    AppCompatImageView btnback;
    AppCompatButton btnSubmit, btnDelete;
    AppCompatTextView tvTitle;
    TextInputLayout inputLayoutMahasiswaNim, inputLayoutMahasiswaName, inputLayoutMahasiswaStambuk,
            inputLayoutMahasiswaMajor, inputLayoutMahasiswaFaculty,
            inputLayoutMahasiswaUniversity, inputLayoutMahasiswaLocationKkn;
    AppCompatEditText edMahasiswaNim, edMahasiswaName, edMahasiswaStambuk, edMahasiswaMajor,
            edMahasiswaFaculty, edMahasiswaUniversity, edMahasiswaLocation;
    DatabaseReference databaseReference;
    MahasiswaField mahasiswaField;
    Mode editMode = Mode.ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_mahasiswa);

        setUpView();
        setUpExtra();
        setUpFirebase();
        setUpListener();
    }

    private void setUpFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference("mahasiswa");
    }

    private void setUpExtra() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                editMode = Mode.UPDATE;
                mahasiswaField = (MahasiswaField) getIntent().getSerializableExtra(AppConstant.DATA);
                edMahasiswaNim.setText(mahasiswaField.getNim());
                edMahasiswaStambuk.setText(mahasiswaField.getStambuk());
                edMahasiswaName.setText(mahasiswaField.getStudentName());
                edMahasiswaMajor.setText(mahasiswaField.getMajor());
                edMahasiswaFaculty.setText(mahasiswaField.getFaculty());
                edMahasiswaUniversity.setText(mahasiswaField.getUniversity());
                edMahasiswaLocation.setText(mahasiswaField.getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (editMode == Mode.ADD) {
            tvTitle.setText(getString(R.string.app_add_kkn_data));
            btnSubmit.setText(getString(R.string.app_data_save));
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(
                    new ConvertionUtils().dpToPx(this, 4F),
                    new ConvertionUtils().dpToPx(this, 14F),
                    new ConvertionUtils().dpToPx(this, 4F),
                    new ConvertionUtils().dpToPx(this, 8F)
            );
            btnSubmit.setLayoutParams(params);
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitle.setText(getString(R.string.app_change_kkn_data));
            btnSubmit.setText(getString(R.string.app_data_change));
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setUpView() {
        inputLayoutMahasiswaNim = findViewById(R.id.input_layout_mahasiswa_nim);
        inputLayoutMahasiswaStambuk = findViewById(R.id.input_layout_mahasiswa_stambuk);
        inputLayoutMahasiswaName = findViewById(R.id.input_layout_mahasiswa_name);
        inputLayoutMahasiswaMajor = findViewById(R.id.input_layout_mahasiswa_major);
        inputLayoutMahasiswaFaculty = findViewById(R.id.input_layout_mahasiswa_faculty);
        inputLayoutMahasiswaUniversity = findViewById(R.id.input_layout_mahasiswa_university);
        inputLayoutMahasiswaLocationKkn = findViewById(R.id.input_layout_mahasiswa_location);

        edMahasiswaNim = findViewById(R.id.ed_mahasiswa_nim);
        edMahasiswaStambuk = findViewById(R.id.ed_mahasiswa_stambuk);
        edMahasiswaName = findViewById(R.id.ed_mahasiswa_name);
        edMahasiswaMajor = findViewById(R.id.ed_mahasiswa_major);
        edMahasiswaFaculty = findViewById(R.id.ed_mahasiswa_faculty);
        edMahasiswaUniversity = findViewById(R.id.ed_mahasiswa_university);
        edMahasiswaLocation = findViewById(R.id.ed_mahasiswa_location);

        btnback = findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btn_submit);
        btnDelete = findViewById(R.id.btn_delete);
        tvTitle = findViewById(R.id.tv_title);
    }

    private void setUpListener() {
        btnback.setOnClickListener(view -> finish());

        btnSubmit.setOnClickListener(view -> {
            String key;
            if (mahasiswaField == null) {
                key = databaseReference.push().getKey();
            } else {
                key = mahasiswaField.getUid();
            }
            String mahasiswaNim = edMahasiswaNim.getText().toString();
            String mahasiswaStambuk = edMahasiswaStambuk.getText().toString();
            String mahasiswaName = edMahasiswaName.getText().toString();
            String mahasiswaMajor = edMahasiswaMajor.getText().toString();
            String mahasiswaFaculty = edMahasiswaFaculty.getText().toString();
            String mahasiswaUniversity = edMahasiswaUniversity.getText().toString();
            String mahasiswaLocation = edMahasiswaLocation.getText().toString();
            MahasiswaField field = new MahasiswaField(
                    key,
                    mahasiswaNim,
                    mahasiswaStambuk,
                    mahasiswaName,
                    mahasiswaMajor,
                    mahasiswaFaculty,
                    mahasiswaUniversity,
                    mahasiswaLocation
            );
            if (key != null) {
                if (checkValidation()) {
                    showLoading(true);
                    databaseReference.child(key).setValue(field);
                    handler.postDelayed(() -> {
                        showLoading(false);
                        if (editMode == Mode.ADD) {
                            setResult(AppConstant.RESULT_ADD);
                        } else {
                            setResult(AppConstant.RESULT_UPDATE);
                        }
                        finish();
                    }, 500);
                }
            }
        });

        btnDelete.setOnClickListener(view -> {
            showLoading(true);
            databaseReference.child(mahasiswaField.getUid()).removeValue();
            handler.postDelayed(() -> {
                showLoading(false);
                setResult(AppConstant.RESULT_DELETE);
                finish();
            }, 500);
        });
    }

    private Boolean checkValidation() {
        if (edMahasiswaNim.getText().toString().matches("")) {
            inputLayoutMahasiswaNim.setError(getString(R.string.app_kkn_should_fill_nim));
            return false;
        } else {
            inputLayoutMahasiswaNim.setErrorEnabled(false);
        }

        if (edMahasiswaName.getText().toString().matches("")) {
            inputLayoutMahasiswaName.setError(getString(R.string.app_kkn_should_fill_student_name));
            return false;
        } else {
            inputLayoutMahasiswaName.setErrorEnabled(false);
        }

        if (edMahasiswaStambuk.getText().toString().matches("")) {
            inputLayoutMahasiswaStambuk.setError(getString(R.string.app_kkn_should_fill_stambuk));
            return false;
        } else {
            inputLayoutMahasiswaStambuk.setErrorEnabled(false);
        }

        if (edMahasiswaMajor.getText().toString().matches("")) {
            inputLayoutMahasiswaMajor.setError(getString(R.string.app_kkn_should_fill_major));
            return false;
        } else {
            inputLayoutMahasiswaMajor.setErrorEnabled(false);
        }

        if (edMahasiswaFaculty.getText().toString().matches("")) {
            inputLayoutMahasiswaFaculty.setError(getString(R.string.app_kkn_should_fill_faculty));
            return false;
        } else {
            inputLayoutMahasiswaFaculty.setErrorEnabled(false);
        }

        if (edMahasiswaUniversity.getText().toString().matches("")) {
            inputLayoutMahasiswaUniversity.setError(getString(R.string.app_kkn_should_fill_university));
            return false;
        } else {
            inputLayoutMahasiswaUniversity.setErrorEnabled(false);
        }

        if (edMahasiswaLocation.getText().toString().matches("")) {
            inputLayoutMahasiswaLocationKkn.setError(getString(R.string.app_kkn_should_fill_location));
            return false;
        } else {
            inputLayoutMahasiswaLocationKkn.setErrorEnabled(false);
        }

        return true;
    }

    public void showLoading(Boolean isShow) {
        LoadingDialog loadingDialog = new LoadingDialog();
        if (isShow) {
            loadingDialog.initializeDialog(this).show();
        } else {
            loadingDialog.initializeDialog(this).dismiss();
        }
    }
}