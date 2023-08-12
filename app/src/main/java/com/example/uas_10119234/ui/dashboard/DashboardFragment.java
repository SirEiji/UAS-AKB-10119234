/*
    NIM     : 10119234
    NAMA    : ARHAM JUSNI INDRAWAN
    KELAS   : IF-4
 */

package com.example.uas_10119234.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_10119234.MainActivity;
import com.example.uas_10119234.R;
import com.example.uas_10119234.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;
    private ArrayList<Note> noteList;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton addButton;
    private FragmentDashboardBinding binding;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View mView =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        // Mendapatkan instance Calendar untuk tanggal sekarang
        Calendar calendar = Calendar.getInstance();

        // Mendapatkan tanggal, bulan, dan tahun
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Januari dimulai dari 0
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Menampilkan tanggal sekarang
        String currentDate = dayOfMonth + " - " + (month + 1) + " - " + year;

        recyclerView = mView.findViewById(R.id.mynote);
        TextView empty = mView.findViewById(R.id.empty);

        FirebaseApp.initializeApp(requireContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FloatingActionButton add = mView.findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.add_note_dialog, null);
                TextInputLayout titleLayout, categoryLayout, notesLayout;
                titleLayout = view1.findViewById(R.id.titleLayout);
                categoryLayout = view1.findViewById(R.id.categoryLayout);
                notesLayout = view1.findViewById(R.id.notesLayout);
                TextInputEditText titleET, categoryET, notesET;
                titleET = view1.findViewById(R.id.titleET);
                categoryET = view1.findViewById(R.id.categoryET);
                notesET = view1.findViewById(R.id.notesET);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Add Your Note !")
                        .setView(view1)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                    titleLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(categoryET.getText()).toString().isEmpty()) {
                                    categoryLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(notesET.getText()).toString().isEmpty()) {
                                    notesLayout.setError("This field is required!");
                                } else {
                                    ProgressDialog dialog = new ProgressDialog(getContext());
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    Note note = new Note();
                                    note.setTitle(titleET.getText().toString());
                                    note.setCategory(categoryET.getText().toString());
                                    note.setDesc(notesET.getText().toString());
                                    note.setDate(currentDate);
                                    database.getReference().child("notes").push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(getContext(), noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noteAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    if (note != null) {
                        note.setId(dataSnapshot.getKey());
                        noteList.add(note);
                    }
                }

                noteAdapter.notifyDataSetChanged();
                if (noteList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                NoteAdapter adapter = new NoteAdapter(getContext(), noteList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Note note) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_note_dialog, null);
                        TextInputLayout titleLayout, categoryLayout, notesLayout;
                        TextInputEditText titleET, categoryET, notesET;

                        titleET = view.findViewById(R.id.titleET);
                        categoryET = view.findViewById(R.id.categoryET);
                        notesET = view.findViewById(R.id.notesET);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        categoryLayout = view.findViewById(R.id.categoryLayout);
                        notesLayout = view.findViewById(R.id.notesLayout);

                        titleET.setText(note.getTitle());
                        categoryET.setText(note.getCategory());
                        notesET.setText(note.getDesc());

                        ProgressDialog progressDialog = new ProgressDialog(getContext());

                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                            titleLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(categoryET.getText()).toString().isEmpty()) {
                                            categoryLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(notesET.getText()).toString().isEmpty()) {
                                            notesLayout.setError("This field is required!");
                                        } else {
                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Note note1 = new Note();
                                            note1.setTitle(titleET.getText().toString());
                                            note1.setCategory(categoryET.getText().toString());
                                            note1.setDesc(notesET.getText().toString());
                                            note1.setDate(currentDate);
                                            database.getReference().child("notes").child(note.getId()).setValue(note1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("Deleting...");
                                        progressDialog.show();
                                        database.getReference().child("notes").child(note.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return mView;
    }

}