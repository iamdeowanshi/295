package com.abacus.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.model.History;
import com.abacus.android.util.PreferenceUtil;

import org.joda.time.DateTime;

import java.util.List;

import katex.hourglass.in.mathlib.MathView;


public class HistoryActivity extends BaseActivity {

    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        List<History> histories = getHistory();

        historyAdapter = new HistoryAdapter(this, histories);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historyAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<History> getHistory() {
        PreferenceUtil util = new PreferenceUtil(this);
        List<History> histories = util.readHistory();
        return histories;
    }

    private class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_HEADER = 1;

        private List<History> historyList;
        private Context context;

        public HistoryAdapter(Context context, List<History> historyList) {
            this.context = context;
            this.historyList = historyList;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return TYPE_HEADER;

            DateTime currentDateTime = DateTime.parse(historyList.get(position - 1).getTime());
            DateTime messageDateTime = DateTime.parse(historyList.get(position).getTime());

            int value = messageDateTime.getDayOfYear() - currentDateTime.getDayOfYear();

            return (value != 0) ? TYPE_HEADER : TYPE_ITEM;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_HEADER:
                    return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_header, parent, false));
                case TYPE_ITEM:
                    return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                    configureHeaderViewHolder(headerViewHolder, position);
                    break;
                case TYPE_ITEM:
                    ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                    configureItemViewHolder(itemViewHolder, position);
                    break;
            }
        }

        private void configureItemViewHolder(ItemViewHolder itemViewHolder, int position) {

            if (position == getItemCount() -1)
                itemViewHolder.viewLine.setVisibility(View.INVISIBLE);

            itemViewHolder.index.setText(position + 1 + " :");
            if (historyList.get(position).isLatex()) {
                itemViewHolder.mathView.setDisplayText("$" + historyList.get(position).getQuestion() + "$");
                itemViewHolder.tvItem.setVisibility(View.GONE);
            }
            else {
                itemViewHolder.tvItem.setText(historyList.get(position).getQuestion());
                itemViewHolder.mathView.setVisibility(View.GONE);
            }
        }

        private void configureHeaderViewHolder(HeaderViewHolder holder, int position) {
            DateTime currentDateTime = DateTime.now();
            DateTime messageDateTime = DateTime.parse(historyList.get(position).getTime());

            int differenceInDays = currentDateTime.getDayOfYear() - messageDateTime.getDayOfYear();

            if (position == getItemCount() -1)
                holder.viewLine.setVisibility(View.INVISIBLE);
            if (differenceInDays == 0) {
                holder.tvTitle.setText("TODAY");
                holder.index.setText(position + 1 + " :");
                if (historyList.get(position).isLatex()) {
                    holder.mathView.setDisplayText("$" + historyList.get(position).getQuestion() + "$");
                    holder.tvItem.setVisibility(View.GONE);
                }
                else {
                    holder.tvItem.setText(historyList.get(position).getQuestion());
                    holder.mathView.setVisibility(View.GONE);
                }


                return;
            }

            if (differenceInDays == 1) {
                holder.tvTitle.setText("YESTERDAY");
                holder.index.setText(position + 1 + " :");
                if (historyList.get(position).isLatex()) {
                    holder.mathView.setDisplayText("$" + historyList.get(position).getQuestion() + "$");
                    holder.tvItem.setVisibility(View.GONE);
                }
                else {
                    holder.tvItem.setText(historyList.get(position).getQuestion());
                    holder.mathView.setVisibility(View.GONE);
                }
                return;
            }

            holder.tvTitle.setText(DateTime.parse(historyList.get(position).getTime()).toString("MMMM dd, YYYY"));
            holder.index.setText(position + 1 + " :");
            if (historyList.get(position).isLatex()) {
                holder.mathView.setDisplayText("$" + historyList.get(position).getQuestion() + "$");
                holder.tvItem.setVisibility(View.GONE);
            }
            else {
                holder.tvItem.setText(historyList.get(position).getQuestion());
                holder.mathView.setVisibility(View.GONE);
            }
        }
        @Override
        public int getItemCount() {
            return historyList.size();
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {

            private final View viewLine;
            private final TextView tvTitle;
            private final TextView index;
            private final TextView tvItem;
            private final MathView mathView;

            HeaderViewHolder(View view) {
                super(view);

                mathView = view.findViewById(R.id.mathView);
                viewLine = view.findViewById(R.id.view);
                tvTitle = view.findViewById(R.id.tvTitle);
                index = view.findViewById(R.id.index);
                tvItem = view.findViewById(R.id.tvItem);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            private final View viewLine;
            private final TextView tvItem;
            private final TextView index;
            private final MathView mathView;

            ItemViewHolder(View view) {
                super(view);

                mathView = view.findViewById(R.id.mathView);
                viewLine = view.findViewById(R.id.view);
                index = view.findViewById(R.id.index);
                tvItem = view.findViewById(R.id.tvItem);
            }
        }

    }
}

