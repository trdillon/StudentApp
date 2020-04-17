package com.tdillon.studentapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdillon.studentapp.R;
import com.tdillon.studentapp.TermDetailsActivity;
import com.tdillon.studentapp.TermEditActivity;
import com.tdillon.studentapp.model.Term;
import com.tdillon.studentapp.util.TextFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tdillon.studentapp.util.Constants.TERM_ID_KEY;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

    private final Context uContext;
    private final RecyclerContext rContext;
    private final List<Term> uTerms;

    public TermAdapter(List<Term> uTerms, Context uContext, RecyclerContext rContext) {
        this.uTerms = uTerms;
        this.uContext = uContext;
        this.rContext = rContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_term_title)
        TextView tvTitle;
        @BindView(R.id.view_term_dates)
        TextView tvDates;
        @BindView(R.id.view_term_fab)
        FloatingActionButton termFab;
        @BindView(R.id.btn_term_details)
        ImageButton termImageBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder holder, int position) {
        final Term term = uTerms.get(position);
        holder.tvTitle.setText(term.getTitle());
        String startEnd = TextFormatter.cardDateFormat.format(term.getStartDate() +
                " to " + TextFormatter.cardDateFormat.format(term.getEndDate()));
        holder.tvDates.setText(startEnd);

        switch(rContext) {
            case MAIN:
                holder.termFab.setImageDrawable(ContextCompat.getDrawable(uContext, R.drawable.ic_edit));
                holder.termImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(uContext, TermDetailsActivity.class);
                    intent.putExtra(TERM_ID_KEY, term.getId());
                    uContext.startActivity(intent);
                });

                holder.termFab.setOnClickListener(v -> {
                    Intent intent = new Intent(uContext, TermEditActivity.class);
                    intent.putExtra(TERM_ID_KEY, term.getId());
                    uContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.termFab.setImageDrawable(ContextCompat.getDrawable(uContext, R.drawable.ic_delete));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return uTerms.size();
    }
}