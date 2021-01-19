package com.lid.kkn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lid.kkn.R;
import com.lid.kkn.field.MahasiswaField;

import java.util.ArrayList;

public class MahasiswaListAdapter extends RecyclerView.Adapter<MahasiswaListAdapter.ItemViewHolder> {

    private final LayoutInflater mInflate;
    private final ArrayList<MahasiswaField> dataMahasiswa;
    private ItemClickListener itemClickListener;

    public MahasiswaListAdapter(Context context, ArrayList<MahasiswaField> dataMahasiswa) {
        this.mInflate = LayoutInflater.from(context);
        this.dataMahasiswa = dataMahasiswa;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_mahasiswa_kkn, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MahasiswaField bookData = dataMahasiswa.get(position);
        holder.bind(bookData);
    }

    @Override
    public int getItemCount() {
        return dataMahasiswa.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvName, tvNim, tvMajorAngkatan, tvFacultyUniversity, tvLocation;
        CardView cardViewItemBuku;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNim = itemView.findViewById(R.id.tv_nim);
            tvMajorAngkatan = itemView.findViewById(R.id.tv_major_angkatan);
            tvFacultyUniversity = itemView.findViewById(R.id.tv_faculty_university);
            tvLocation = itemView.findViewById(R.id.tv_location);
            cardViewItemBuku = itemView.findViewById(R.id.cardview_item_buku);
        }

        @SuppressLint("SetTextI18n")
        private void bind(MahasiswaField mahasiswaField) {
            tvName.setText(mahasiswaField.getStudentName());
            tvNim.setText("NIM: " + mahasiswaField.getNim());
            tvFacultyUniversity.setText(mahasiswaField.getFaculty() + " - " + mahasiswaField.getUniversity());
            tvMajorAngkatan.setText(mahasiswaField.getMajor() + " - " + mahasiswaField.getStambuk());
            tvLocation.setText(mahasiswaField.getLocation());
            cardViewItemBuku.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(mahasiswaField, getAdapterPosition());
                }
            });
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(MahasiswaField mahasiswaField, int pos);
    }
}
