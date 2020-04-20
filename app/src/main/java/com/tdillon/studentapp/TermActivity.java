package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdillon.studentapp.model.Term;
import com.tdillon.studentapp.ui.RecyclerContext;
import com.tdillon.studentapp.ui.TermAdapter;
import com.tdillon.studentapp.viewmodel.TermVM;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermActivity extends AppCompatActivity {

    @BindView(R.id.term_recycler_view)
    RecyclerView aTermRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, TermEditActivity.class);
        startActivity(intent);
    }

    private List<Term> termData = new ArrayList<>();
    private TermAdapter aTermAdapter;
    private TermVM aTermVM;

    private void initViewModel() {
        final Observer<List<Term>> termObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);

                    if(aTermAdapter == null) {
                        aTermAdapter = new TermAdapter(termData, TermActivity.this, RecyclerContext.MAIN);
                        aTermRecyclerView.setAdapter(aTermAdapter);
                    } else {
                        aTermAdapter.notifyDataSetChanged();
                    }
                };
        aTermVM = new ViewModelProvider(this).get(TermVM.class);
        aTermVM.vmTerms.observe(this, termObserver);
    }

    private void initRecyclerView() {
        aTermRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aTermRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}