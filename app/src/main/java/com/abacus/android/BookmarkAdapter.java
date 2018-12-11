package com.abacus.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abacus.android.model.Bookmark;
import com.abacus.android.model.User;
import com.abacus.android.service.logEvent.LogEventImp;
import com.abacus.android.util.PreferenceUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import katex.hourglass.in.mathlib.MathView;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<Bookmark> bookmarkList;
    private Context context;
    private BookmarkListener listener;

    public BookmarkAdapter(Context context, List<Bookmark> bookmarkList) {
        this.context = context;
        this.bookmarkList = bookmarkList;
        Collections.reverse(bookmarkList);
    }

    public void setBookmarkClickListner(BookmarkListener bookmarkListener) {
        this.listener = bookmarkListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == getItemCount() -1)
            holder.viewLine.setVisibility(View.INVISIBLE);

        if (bookmarkList.get(position).isLatex()) {
            holder.mathView.setDisplayText("$" + bookmarkList.get(position).getValue() + "$");
            holder.wordView.setVisibility(View.GONE);
        }
        else {
            holder.wordView.setText(bookmarkList.get(position).getValue());
            holder.mathView.setVisibility(View.GONE);
        }
        holder.index.setText((position + 1) +(":"));

    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnBookmark)
        ImageButton btnBookmark;
        @BindView(R.id.mathView)
        MathView mathView;
        @BindView(R.id.index)
        TextView index;
        @BindView(R.id.wordText)
        TextView wordView;
        @BindView(R.id.view)
        View viewLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.setClickable(true);
        }

        @OnClick(R.id.mathViewLayout)
        void onEquationClick() {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("fragment",0);
            intent.putExtra("value", bookmarkList.get(getAdapterPosition()).getValue());
            context.startActivity(intent);
        }

        @OnClick(R.id.wordText)
        void onWordProblemClick() {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt("fragment",1);
            bundle.putString("value", bookmarkList.get(getAdapterPosition()).getValue());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

        @OnClick(R.id.btnBookmark)
        void onTitleTextClick() {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            PreferenceUtil util = new PreferenceUtil(context);
            User user = (User) util.read("USER", User.class);
            CollectionReference collectionRef = database.collection("abacus").document("bookmarks").collection(user.getId());

            collectionRef.document(bookmarkList.get(getAdapterPosition()).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Bookmark removed", Toast.LENGTH_LONG ).show();

                }
            });
            logEvent(user);
            listener.onItemRemove(getAdapterPosition());

        }

        private void logEvent(User user) {
            Map<String, String> logMap = new HashMap<>();
            logMap.put("other","N/A");
            logMap.put("userID", user.getId());
            logMap.put("userEmail", user.getEmail());
            logMap.put("userActivity", "BookmarkRemoved");

            new LogEventImp().logEvent(logMap);
        }
    }

    interface BookmarkListener {

        void onItemRemove(int postion);
    }
}
