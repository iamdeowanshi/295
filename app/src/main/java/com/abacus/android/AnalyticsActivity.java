package com.abacus.android;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.abacus.android.base.BaseActivity;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnalyticsActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.title)
    TextView title;

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.activity_analytics);
        ButterKnife.bind(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setInitialScale(200);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setMinimumFontSize(16);
        webView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
        if (bundle == null) {
            //webView.loadUrl("https://search-abacus-dn5ucj45reb3vvgd2kltvi637i.us-east-1.es.amazonaws.com/_plugin/kibana/goto/a76f419acc236a8d05ecc57150ca845b");
            return;
        }

        title.setText("Graph View");
        String latex = bundle.getString("latex").toLowerCase();
        Set<Character> set = new HashSet<>();
        for (Character ch : latex.toCharArray()) {
            if (ch >= 'a' && ch <= 'z')
                set.add(ch);
        }
        int count = 0;
        for (Character ch : set) {
            if (ch == 'x' || ch == 'y')
                continue;

            if (count == 0 && ch != 'x')
                latex = latex.replaceAll(ch.toString(),"x");
            else {
                latex = latex.replaceAll(ch.toString(),"y");
            }

            count++;
        }
        latex = latex.replaceAll("//*", "");
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
    }

    @OnClick(R.id.btn_close)
    public void onViewClicked() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
