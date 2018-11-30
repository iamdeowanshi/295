package com.abacus.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.model.Bookmark;
import com.abacus.android.model.User;
import com.abacus.android.util.PreferenceUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private List<Bookmark> bookmarks;
    private BookmarkAdapter bookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bookmarks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bookmarks = new ArrayList<>();
        getBookmarks();

        bookmarkAdapter = new BookmarkAdapter(this, bookmarks);
        //List<String> bookmarks = Collections.nCopies(10, this.getString(R.string.sample_equ));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(bookmarkAdapter);

    }

    private void getBookmarks() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        PreferenceUtil util = new PreferenceUtil(this);
        User user = (User) util.read("USER", User.class);
        CollectionReference collectionRef = database.collection("abacus").document("bookmarks").collection(user.getId());

        collectionRef.orderBy("time").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                bookmarks.addAll(task.getResult().toObjects(Bookmark.class));
                bookmarkAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
