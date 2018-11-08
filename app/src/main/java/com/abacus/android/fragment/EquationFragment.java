package com.abacus.android.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abacus.android.CameraActivity;
import com.abacus.android.R;
import com.abacus.android.base.BaseFragment;
import com.abacus.android.parser.InputClassifier;
import com.abacus.android.util.ImageUtil;
import com.google.gson.Gson;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import katex.hourglass.in.mathlib.MathView;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EquationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EquationFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;
    @BindView(R.id.edtQuestion)
    EditText edtQuestion;
    @BindView(R.id.txtSolution)
    TextView txtSolution;
    @BindView(R.id.capture)
    ImageView capture;
    @BindView(R.id.mathView)
    MathView mathView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ExprEvaluator mExprEvaluator;

    public EquationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EquationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EquationFragment newInstance() {
        EquationFragment fragment = new EquationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        startCamera();
    }

    private void startCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);

        startActivityForResult(intent, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equation, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtQuestion.setVisibility(View.GONE);
        mathView.setClickable(true);
        setupKatex();
    }

    private void setupKatex() {
       /* MathView mathView = new MathView(getContext());
        mathView.setClickable(true);
        mathView.setTextSize(14);
        mathView.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
        mathView.setDisplayText(getResources().getString(R.string.runtime_formula));
        mathView.setViewBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.black));
        //parent_layout.addView(mathView);*/
    }


    private void solve(String input) {
        InputClassifier inputClassifier = new InputClassifier(getContext());
        txtSolution.setText("Solution : \n\t" + inputClassifier.executeClassifier(input));
        /*IExpr result;
        mExprEvaluator = new ExprEvaluator();
        result = mExprEvaluator.eval(input);

        txtSolution.setText(result.toString());
        return result.toString();*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1 && resultCode==2)
        {
            String latex = data.getStringExtra("LATEX");
            edtQuestion.setText(latex);
            mathView.setDisplayText("$" + latex + "$");
        }

    }

    @OnClick({R.id.btnSolve, R.id.btnReTake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSolve:
                solve(edtQuestion.getText().toString());
                break;
            case R.id.btnReTake:
                startCamera();
                break;
        }
    }
}
