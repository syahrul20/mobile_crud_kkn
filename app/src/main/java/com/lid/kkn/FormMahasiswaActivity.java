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

    public enum Mode {
        ADD, UPDATE
    }

    AppCompatImageView btnback;
    AppCompatButton btnSubmit, btnDelete;
    AppCompatTextView tvTitle;
    TextInputLayout inputLayoutMahasiswaNim, inputLayoutMahasiswaName, inputLayoutMahasiswaStambuk,
            inputLayoutMahasiswaMajor, inputLayoutMahasiswaFaculty,
            inputLayoutMahasiswaUniversity, inputLayoutMahasiswaLocationKkn;
    AppCompatEditText edBookTitle, edBookPublished, edBookCreator, edBookCategory,
            edBookYearPublished, edBookThickness, edBookIsbn;
    DatabaseReference databaseReference;
    MahasiswaField mahasiswaField;
    final Handler handler = new Handler(Looper.getMainLooper());
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
                edBookTitle.setText(mahasiswaField.getNim());
                edBookCreator.setText(mahasiswaField.getStambuk());
                edBookPublished.setText(mahasiswaField.getStudentName());
                edBookCategory.setText(mahasiswaField.getMajor());
                edBookYearPublished.setText(mahasiswaField.getFaculty());
                edBookThickness.setText(mahasiswaField.getUniversity());
                edBookIsbn.setText(mahasiswaField.getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (editMode == Mode.ADD) {
            tvTitle.setText(getString(R.string.app_add_book_data));
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
            tvTitle.setText(getString(R.string.app_change_book_data));
            btnSubmit.setText(getString(R.string.app_data_change));
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setUpView() {
        inputLayoutMahasiswaNim = findViewById(R.id.input_layout_book_title);
        inputLayoutMahasiswaStambuk = findViewById(R.id.input_layout_book_creator);
        inputLayoutMahasiswaName = findViewById(R.id.input_layout_book_published);
        inputLayoutMahasiswaMajor = findViewById(R.id.input_layout_book_category);
        inputLayoutMahasiswaFaculty = findViewById(R.id.input_layout_book_year_published);
        inputLayoutMahasiswaUniversity = findViewById(R.id.input_layout_book_thickness);
        inputLayoutMahasiswaLocationKkn = findViewById(R.id.input_layout_book_isbn);

        edBookTitle = findViewById(R.id.ed_book_title);
        edBookCreator = findViewById(R.id.ed_book_creator);
        edBookPublished = findViewById(R.id.ed_book_publsihed);
        edBookCategory = findViewById(R.id.ed_book_category);
        edBookYearPublished = findViewById(R.id.ed_book_year_published);
        edBookThickness = findViewById(R.id.ed_book_thickness);
        edBookIsbn = findViewById(R.id.ed_book_isbn);

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
            String bookTitle = edBookTitle.getText().toString();
            String bookCreator = edBookCreator.getText().toString();
            String bookPublished = edBookPublished.getText().toString();
            String bookCategory = edBookCategory.getText().toString();
            String bookYearPublished = edBookYearPublished.getText().toString();
            String bookThickness = edBookThickness.getText().toString();
            String bookIsbn = edBookIsbn.getText().toString();
            MahasiswaField field = new MahasiswaField(
                    key,
                    bookTitle,
                    bookCreator,
                    bookPublished,
                    bookCategory,
                    bookYearPublished,
                    bookThickness,
                    bookIsbn
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
        if (edBookTitle.getText().toString().matches("")) {
            inputLayoutMahasiswaNim.setError(getString(R.string.app_kkn_should_fill_nim));
            return false;
        } else {
            inputLayoutMahasiswaNim.setErrorEnabled(false);
        }

        if (edBookCreator.getText().toString().matches("")) {
            inputLayoutMahasiswaStambuk.setError(getString(R.string.app_kkn_should_fill_student_name));
            return false;
        } else {
            inputLayoutMahasiswaStambuk.setErrorEnabled(false);
        }

        if (edBookPublished.getText().toString().matches("")) {
            inputLayoutMahasiswaName.setError(getString(R.string.app_kkn_should_fill_stambuk));
            return false;
        } else {
            inputLayoutMahasiswaName.setErrorEnabled(false);
        }

        if (edBookCategory.getText().toString().matches("")) {
            inputLayoutMahasiswaMajor.setError(getString(R.string.app_kkn_should_fill_major));
            return false;
        } else {
            inputLayoutMahasiswaMajor.setErrorEnabled(false);
        }

        if (edBookYearPublished.getText().toString().matches("")) {
            inputLayoutMahasiswaFaculty.setError(getString(R.string.app_kkn_should_fill_faculty));
            return false;
        } else {
            inputLayoutMahasiswaMajor.setErrorEnabled(false);
        }

        if (edBookThickness.getText().toString().matches("")) {
            inputLayoutMahasiswaUniversity.setError(getString(R.string.app_kkn_should_fill_university));
            return false;
        } else {
            inputLayoutMahasiswaUniversity.setErrorEnabled(false);
        }

        if (edBookIsbn.getText().toString().matches("")) {
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