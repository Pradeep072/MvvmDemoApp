package com.example.mvvmdemoapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mvvmdemoapp.R;
import com.example.mvvmdemoapp.room_pkg.Note;
import com.example.mvvmdemoapp.util.NoteAdapter;
import com.example.mvvmdemoapp.viewmodel_pkg.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOT_REQUEST=1;
    public static final int EDIT_NOT_REQUEST=2;
    private NoteViewModel noteViewModel;
    private String TAG="REpo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNote=findViewById(R.id.button_add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOT_REQUEST);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);//if you know that recyclerview size will not change

        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note DELETED", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnNoteItemClickListener(new NoteAdapter.OnNoteItemClickListener() {
            @Override
            public void onNoteItemClick(Note note) {
                Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,EDIT_NOT_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==ADD_NOT_REQUEST && resultCode==RESULT_OK){
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note =new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
        }else if (requestCode==EDIT_NOT_REQUEST && resultCode==RESULT_OK){
            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if (id==-1){
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note =new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note Successfully Updated", Toast.LENGTH_SHORT).show();


        }else {

            Toast.makeText(this, "Data NOT Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "ALL NOTES DELETED", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
