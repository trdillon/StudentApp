package com.tdillon.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    private List<Term> termData = new ArrayList<>();
    private TermAdapter termAdapter;

    @BindView(R.id.term_recycler_view)
    RecyclerView rvTerm;

    @OnClick(R.id.term_add_btn)
    void handleAddBtn() {
        Intent intent = new Intent(this, TermEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_home)
    public void showHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViewModel() {
        final Observer<List<Term>> termObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);

                    if(termAdapter == null) {
                        termAdapter = new TermAdapter(termData, TermActivity.this, RecyclerContext.MAIN);
                        rvTerm.setAdapter(termAdapter);
                    }
                    else {
                        termAdapter.notifyDataSetChanged();
                    }
                };
        TermVM termVM = new ViewModelProvider(this).get(TermVM.class);
        termVM.vmTerms.observe(this, termObserver);
    }

    private void initRecyclerView() {
        rvTerm.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTerm.setLayoutManager(layoutManager);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }
}