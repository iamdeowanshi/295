package com.abacus.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abacus.android.HomeActivity;
import com.abacus.android.R;
import com.abacus.android.base.BaseFragment;
import com.abacus.android.model.Bookmark;
import com.abacus.android.model.History;
import com.abacus.android.model.User;
import com.abacus.android.model.WordProblem;
import com.abacus.android.service.word.WordPresenter;
import com.abacus.android.service.word.WordPresenterImpl;
import com.abacus.android.service.word.WordViewInteractor;
import com.abacus.android.util.PreferenceUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.bookMark)
    ImageView bookMark;
    Unbinder unbinder;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    Spinner operatorSpinner;
    EditText operand1;
    EditText operand2;
    @BindView(R.id.feedback_layout)
    LinearLayout feedbackLayout;


    @BindView(R.id.webView)
    WebView webView;

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private WordPresenter presenter;
    private Map<String,String> activitymap;


    private WordProblem problem;
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
        activitymap = new HashMap<String, String>();
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
        Bundle bundle = getArguments();
        if ( bundle != null) {
            edtQuestion.setText(bundle.getString("value"));
        }
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

        problem = wordProblem;
        problem.setUserId(((HomeActivity)getActivity()).getUser().getId());
        txtSolution.setText(result);
        History history = new History();
        history.setLatex(false);
        history.setQuestion(edtQuestion.getText().toString());
        history.setSolution(result.toString());
        history.setTime(DateTime.now().toString());

        PreferenceUtil util = new PreferenceUtil(getContext());
        List<History> historyList = util.readHistory();
        historyList.add(history);
        util.saveHistory(historyList);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showFeedBack();
            }
        }, 1000);

    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnSolve)
    public void onViewClicked() {
        if (edtQuestion.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Please Enter problem to solve", Toast.LENGTH_LONG);
            return;
        }
        User user = ((HomeActivity)getActivity()).getUser();
        problem = new WordProblem();
        problem.setEmail(user.getEmail());
        problem.setQuestion(edtQuestion.getText().toString());
        problem.setUserId(user.getId());

        presenter.getSolution(problem);
        hideKeyboard(getActivity());

    }

    @OnClick(R.id.bookMark)
    public void onBookmarkClicked() {
        saveBookmark();
        Toast.makeText(getContext(), "Bookmark saved", Toast.LENGTH_LONG);
        sendLog("Bookmark");
    }

    private void saveBookmark() {
        bookMark.setImageResource(R.drawable.ic_star_filled);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String millisInString = dateFormat.format(new Date());
        PreferenceUtil util = new PreferenceUtil(getContext());
        User user = (User) util.read("USER", User.class);
        Bookmark bookmark = new Bookmark(edtQuestion.getText().toString(), millisInString, false);

        database.collection("abacus").document("bookmarks").collection(user.getId()).document(bookmark.getId()).set(bookmark).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                bookMark.setImageResource(R.drawable.ic_star_filled);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFeedBack() {
        feedbackLayout.setVisibility(View.VISIBLE);
    }

    public void enterFeedBack() {
        initPopupViewControls();
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) operatorSpinner.getAdapter();
        operand1.setText(String.valueOf(problem.getOperand1()));
        operand2.setText(String.valueOf(problem.getOperand2()));
        operatorSpinner.setSelection(adapter.getPosition(problem.getPredictedOperator()));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popupInputDialogView);

        // Create AlertDialog and show.
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        popupInputDialogView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                problem.setPredictedOperator(operatorSpinner.getSelectedItem().toString());
                problem.setOperand1(Integer.parseInt(operand1.getText().toString()));
                problem.setOperand2(Integer.parseInt(operand2.getText().toString()));
                presenter.sendFeedBack(problem);
                alertDialog.cancel();

            }
        });

        popupInputDialogView.findViewById(R.id.button_cancel_user_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        feedbackLayout.setVisibility(View.GONE);

    }

    private AlertDialog alertDialog;
    private View popupInputDialogView = null;


    private void initPopupViewControls() {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.layout_feedback, null);

        operatorSpinner = popupInputDialogView.findViewById(R.id.operator_spinner);
        operand1 = popupInputDialogView.findViewById(R.id.operand1);
        operand2 = popupInputDialogView.findViewById(R.id.operand2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        operatorSpinner.setAdapter(adapter);

        // Display values from the main activity list view in user input edittext.
    }

    @OnClick({R.id.correct, R.id.incorrect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.correct:
                problem.setCorrect(true);
                presenter.sendFeedBack(problem);
                break;
            case R.id.incorrect:
                enterFeedBack();
                break;
        }
    }

    @OnClick(R.id.clear)
    public void onClear() {
        bookMark.setImageResource(R.drawable.ic_star);
        edtQuestion.setText("");
        txtSolution.setText("" +
                "");
    }

    private void sendLog(String value) {
        activitymap.put("userActivity", value);
        ((HomeActivity)getActivity()).logEvent(activitymap);
    }

}
