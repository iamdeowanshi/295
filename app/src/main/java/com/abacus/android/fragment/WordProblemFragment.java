package com.abacus.android.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abacus.android.model.LatexResponse;
import com.abacus.android.R;
import com.abacus.android.model.WordProblem;
import com.abacus.android.base.BaseFragment;
import com.abacus.android.service.latex.LatexPresenter;
import com.abacus.android.service.latex.LatexPresenterImpl;
import com.abacus.android.service.latex.LatexViewInteractor;
import com.abacus.android.service.word.WordPresenter;
import com.abacus.android.service.word.WordPresenterImpl;
import com.abacus.android.service.word.WordViewInteractor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordProblemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordProblemFragment extends BaseFragment implements WordViewInteractor {
    @BindView(R.id.edtQuestion)
    EditText edtQuestion;
    @BindView(R.id.txtSolution)
    TextView txtSolution;
    @BindView(R.id.btnSolve)
    Button btnSolve;
    @BindView(R.id.btnReTake)
    Button btnReTake;
    Unbinder unbinder;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private WordPresenter presenter;
    public WordProblemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WordProblemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordProblemFragment newInstance() {
        WordProblemFragment fragment = new WordProblemFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        presenter = new WordPresenterImpl();
        presenter.attachViewInteractor(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equation, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnReTake.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSolution(WordProblem wordProblem) {
        StringBuilder result = new StringBuilder();
        for (String str : wordProblem.getAnswers()) {
            result.append(str);
            result.append("\n");
        }

        txtSolution.setText(result);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnSolve)
    public void onViewClicked() {
        presenter.getSolution(edtQuestion.getText().toString());
        hideKeyboard(getActivity());
    }

    public static void hideKeyboard(Activity activity) {
        try{
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
