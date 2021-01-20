package com.lid.kkn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lid.kkn.Utils.AppConstant;
import com.lid.kkn.Utils.SnackBarInfo;
import com.lid.kkn.adapter.MahasiswaListAdapter;
import com.lid.kkn.field.MahasiswaField;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements MahasiswaListAdapter.ItemClickListener {

    RecyclerView rvMahasiswa;
    CoordinatorLayout container;
    LinearLayoutCompat containerEmpty;
    AppCompatImageView btnAdd;
    DatabaseReference reference;
    ProgressBar progressLoading;
    MahasiswaListAdapter mahasiswaListAdapter;
    ActivityResultLauncher<Intent> onMahasiswaResult;
    SnackBarInfo snackbar = new SnackBarInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpView();
        setUpActivityForResult();
    }

    private void setUpFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("mahasiswa");

        loadData();
    }

    public void setUpView() {
        container = findViewById(R.id.container);
        rvMahasiswa = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btn_add);
        containerEmpty = findViewById(R.id.container_empty);
        progressLoading = findViewById(R.id.progressLoading);

        setUpListener();
        setUpFirebase();
    }

    public void setUpListener() {
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, FormMahasiswaActivity.class);
            onMahasiswaResult.launch(intent);
        });
    }

    private void loadData() {
        showLoading(true);
        ValueEventListener valueEventListener = new ValueEventListener() {
            final ArrayList<MahasiswaField> bookFieldArrayList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookFieldArrayList.clear();
                for (DataSnapshot bookSnapShot : snapshot.getChildren()) {
                    if (!Objects.equals(bookFieldArrayList, bookSnapShot.getValue(MahasiswaField.class))) {
                        MahasiswaField bookField = bookSnapShot.getValue(MahasiswaField.class);
                        bookFieldArrayList.add(bookField);
                    }
                }
                showLoading(false);
                setUpRecyclerView(bookFieldArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar;
                snackbar = Snackbar.make(container, "Gagal memuat data", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();

            }
        };

        reference.orderByKey().addValueEventListener(valueEventListener);
    }


    private void setUpRecyclerView(ArrayList<MahasiswaField> mArrayList) {
        mahasiswaListAdapter = new MahasiswaListAdapter(this, mArrayList);
        rvMahasiswa.setAdapter(mahasiswaListAdapter);
        mahasiswaListAdapter.setClickListener(this);
        if (mahasiswaListAdapter.getItemCount() == 0) {
            containerEmpty.setVisibility(View.VISIBLE);
            rvMahasiswa.setVisibility(View.GONE);
        } else {
            containerEmpty.setVisibility(View.GONE);
            rvMahasiswa.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(MahasiswaField mahasiswaField, int pos) {
        Intent intent = new Intent(this, FormMahasiswaActivity.class);
        intent.putExtra(AppConstant.DATA, mahasiswaField);
        onMahasiswaResult.launch(intent);
    }

    private void setUpActivityForResult() {
        onMahasiswaResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppConstant.RESULT_ADD) {
                        snackbar.initializeSnackBar(this, container, getString(R.string.app_kkn_data_success_add));
                    } else if (result.getResultCode() == AppConstant.RESULT_UPDATE) {
                        snackbar.initializeSnackBar(this, container, getString(R.string.app_kkn_data_success_changed));
                    } else if (result.getResultCode() == AppConstant.RESULT_DELETE) {
                        snackbar.initializeSnackBar(this, container, getString(R.string.app_kkn_data_success_deleted));
                    }
                });
    }

    private void showLoading(Boolean isShow) {
        if (isShow) {
            progressLoading.setVisibility(View.VISIBLE);
        } else {
            progressLoading.setVisibility(View.GONE);
        }
    }
}