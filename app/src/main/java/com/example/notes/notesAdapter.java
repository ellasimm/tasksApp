package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notesAdapter extends RecyclerView.Adapter {
    private ArrayList<Note> noteData;
    private static View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public notesAdapter(ArrayList<Note> arrayList, Context context) {
        noteData = arrayList;
        parentContext = context;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        NoteViewHolder cvh = (NoteViewHolder) holder;
        cvh.getTextTitle().setText(noteData.get(position).getTitle());
        cvh.getTextDescription().setText(noteData.get(position).getDescription());
        cvh.getTextDate().setText(noteData.get(position).getDate() + ",");
        //cvh.getTextPriority().setText(noteData.get(position).getPriority());

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position){
        Note note = noteData.get(position);
        NotesDBHelper ds = new NotesDBHelper(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteNote(note.getNoteID());
            ds.close();
            if (didDelete) {
                noteData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;
        public TextView textDescription;
        public Button deleteButton;
        public TextView textDate;
       // public TextView textPriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDate = itemView.findViewById(R.id.textDueDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);

        //    textPriority = itemView.findViewById(R.id.textPriority);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public TextView getTextTitle() {

            return textTitle;
        }

        public TextView getTextDescription() {

            return textDescription;
        }
        public TextView getTextDate() {

            return textDate;
        }

      //  public TextView getTextPriority(){
      //  return textPriority;
      //  }
    }


    public static void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NoteViewHolder(v);
    }


    @Override
    public int getItemCount () {

        return noteData.size();
    }
}
