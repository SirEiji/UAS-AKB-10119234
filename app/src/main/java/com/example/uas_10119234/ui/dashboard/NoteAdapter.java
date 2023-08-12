/*
    NIM     : 10119234
    NAMA    : ARHAM JUSNI INDRAWAN
    KELAS   : IF-4
 */

package com.example.uas_10119234.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_10119234.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    ArrayList<Note> arrayList;
    OnItemClickListener onItemClickListener;

    public NoteAdapter(Context context, ArrayList<Note> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getCategory());
        holder.desc.setText(arrayList.get(position).getDesc());
        holder.date.setText(arrayList.get(position).getDate());
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;
        TextView category;
        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.list_item_tanggal);
            title = itemView.findViewById(R.id.list_item_title);
            category = itemView.findViewById(R.id.list_item_category);
            desc = itemView.findViewById(R.id.list_item_notes);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Note note);
    }

}
