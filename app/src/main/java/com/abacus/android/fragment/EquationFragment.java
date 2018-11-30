package com.abacus.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abacus.android.AnalyticsActivity;
import com.abacus.android.CameraActivity;
import com.abacus.android.HomeActivity;
import com.abacus.android.R;
import com.abacus.android.base.BaseFragment;
import com.abacus.android.equation.parser.InputClassifier;
import com.abacus.android.model.Bookmark;
import com.abacus.android.model.History;
import com.abacus.android.model.User;
import com.abacus.android.util.PreferenceUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.matheclipse.core.eval.ExprEvaluator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import katex.hourglass.in.mathlib.MathView;

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
    @BindView(R.id.mathView)
    MathView mathView;
    @BindView(R.id.bookMark)
    ImageView bookMark;
    @BindView(R.id.viewGraph)
    TextView viewGraph;

    @BindView(R.id.webView)
    WebView webView;

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Map<String,String> activitymap;
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
        activitymap = new HashMap<String, String>();

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
        edtQuestion.setText("");
        mathView.setClickable(true);
        setupKatex();
        Bundle bundle = getArguments();
        if ( bundle != null) {
            mathView.setDisplayText("$" + bundle.getString("value") + "$");
        } else {
            startCamera();
        }
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
        String solution  = inputClassifier.executeClassifier(input);
        solution = formatOutput(solution);
        sendLog("equation-solver", inputClassifier.getOperation());
        txtSolution.setText("\n" + solution);
        History history = new History();
        history.setLatex(true);
        history.setQuestion(input);
        history.setSolution(solution);
        history.setTime(DateTime.now().toString());

        PreferenceUtil util = new PreferenceUtil(getContext());
        List<History> historyList = util.readHistory();
        historyList.add(history);
        util.saveHistory(historyList);

    }

    public String formatOutput(String result){

        List<String> output = new ArrayList<>();
        if (result.toLowerCase().contains("solve")) {
            return "Unsupported Equation  found !!!";
        } else if (result.contains("{}")) {
            return "No Roots found";
        }

        result = result.replaceAll("\\s/+", "").replaceAll("->", " = ");

        if(result.contains("{")){
            result=result.replaceAll("\\{","");
        }

        if(result.contains("}")){
            result=result.replaceAll("\\}","");
        }

        if(result.contains(",")){
            output.addAll(Arrays.asList(result.split(",")));

        }
        if (output.size() == 0)
            output.add(result);
        StringBuilder out_String = new StringBuilder();
        for (String item : output) {
            out_String.append(item);
            out_String.append("\n");
        }
        return out_String.toString();
        //return result;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1 && resultCode == 2) {
            String latex = data.getStringExtra("LATEX");
            edtQuestion.setText(latex);
            viewGraph.setVisibility(View.VISIBLE);
            edtQuestion.setVisibility(View.GONE);
            mathView.setDisplayText("$" + latex + "$");
        }

    }

    @OnClick({R.id.btnSolve, R.id.btnReTake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSolve:
                //loadGraph(edtQuestion.getText().toString());
                solve(edtQuestion.getText().toString());
                //sendLog("Equation");
                break;
            case R.id.btnReTake:
                startCamera();
                bookMark.setImageResource(R.drawable.ic_star);
                break;
        }
    }

    @OnClick(R.id.bookMark)
    public void onViewClicked() {
        saveBookmark();
        Toast.makeText(getContext(),"Bookmark saved", Toast.LENGTH_LONG);
        sendLog("Bookmark");
    }

    @OnClick(R.id.clear)
    public void onClear() {
        bookMark.setImageResource(R.drawable.ic_star);
        edtQuestion.setText("");
        edtQuestion.setVisibility(View.VISIBLE);
        mathView.setVisibility(View.GONE);
        mathView.setDisplayText("");
        txtSolution.setText("");
        viewGraph.setVisibility(View.GONE);
    }

    private void saveBookmark() {
        bookMark.setImageResource(R.drawable.ic_star_filled);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String millisInString = dateFormat.format(new Date());
        PreferenceUtil util = new PreferenceUtil(getContext());
        User user = (User) util.read("USER", User.class);
        Bookmark bookmark = new Bookmark(edtQuestion.getText().toString(), millisInString, true);

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

    private void sendLog(String value) {
        activitymap.put("userActivity", value);
        ((HomeActivity)getActivity()).logEvent(activitymap);
    }

    private void sendLog(String value, String subValue) {
        activitymap.put("userActivity", value);
        activitymap.put("other", subValue);
        ((HomeActivity)getActivity()).logEvent(activitymap);
    }

    /*public void loadGraph(String latex) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setInitialScale(200);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setMinimumFontSize(16);
        webView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
        webView.loadData("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "\n" +
                "<style>\n" +
                "    html, body {\n" +
                "      width: 100%;\n" +
                "      height: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "\n" +
                "    #calculator {\n" +
                "      width: 100%;\n" +
                "      height: 100%;\n" +
                "    }\n" +
                "  </style>" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\n" +
                "  <script src=\"https://www.desmos.com/api/v1.1/calculator.js?apiKey=dcb31709b452b1cf9dc26972add0fda6\"></script>\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"calculator\"></div>\n" +
                "\n" +
                "  <script >\n" +
                "    var elt = document.getElementById('calculator');\n" +
                "var calcOpts = {\n" +
                "      lockViewport: false,\n" +
                "      zoomButtons: true,\n" +
                "      expressions: false,\n" +
                "      settingsMenu: false,\n" +
                "      border : false\n" +
                "    };" +
                "    var calculator = Desmos.GraphingCalculator(elt,calcOpts);\n" +
                "    calculator.setExpression({id:'graph1', latex:'" +latex +"'});\n" +
                "  </script>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>" , "text/html", "UTF-8");
    }*/

    @OnClick(R.id.viewGraph)
    public void onViewGraphClick() {
        Bundle bundle = new Bundle();
        bundle.putString("latex", edtQuestion.getText().toString());
        Intent intent = new Intent(getContext(), AnalyticsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
