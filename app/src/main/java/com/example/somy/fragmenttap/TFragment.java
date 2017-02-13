package com.example.somy.fragmenttap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TFragment extends Fragment {

    // WebView
    WebView webView;

    View view;

    public TFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //Holder
        if(view !=null) return view;

        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_t, container, false);


        // 1. 사용할 위젯을 가져온다.
        webView = (WebView) view.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        // 줌사용
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        // 3. 웹뷰 클라이언트를 지정... (안하면 내장 웹브라우저가 팝업된다.)
        webView.setWebViewClient(new WebViewClient());
        // 3.1 둘다 세팅할것 : 프로토콜에 따라 클라이언트가 선택되는것으로 파악됨...
        webView.setWebChromeClient(new WebChromeClient());

        // 최초 로드시 google.com 이동
        goUrl("google.com");

        return view;

    }

    private void goUrl(String url) {
        // 1. 유효성 검사
        if (url != null && !url.equals("")) {
            // 2. 프로토콜 검사
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            // 3. url 이동
            webView.loadUrl(url);
        }
    }

    public boolean goBack() {
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        } else {
            return false;
        }

        // if it doesn't exist, not working
        //  webView.goBack();
        //  webView.canGoBack();
    }
}
