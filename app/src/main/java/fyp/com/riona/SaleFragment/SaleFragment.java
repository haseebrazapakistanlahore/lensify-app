package fyp.com.riona.SaleFragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import fyp.com.riona.R;

public class SaleFragment extends Fragment {
    Dialog mDialog;
    WebView scheduledWebview;

    public SaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        scheduledWebview = (WebView) view.findViewById(R.id.webview);
        internetAvail();

        return view;
    }


    private void initWebview(String url) {
        Log.e("TGED", url);
        scheduledWebview.getSettings().setJavaScriptEnabled(true);
        scheduledWebview.getSettings().setSupportZoom(true);
        scheduledWebview.setScrollbarFadingEnabled(true);
        scheduledWebview.getSettings().setDomStorageEnabled(true);
        scheduledWebview.getSettings().setBuiltInZoomControls(true);
        scheduledWebview.getSettings().setDisplayZoomControls(false);
        scheduledWebview.setVerticalScrollBarEnabled(true);

        scheduledWebview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        scheduledWebview.getSettings().setUseWideViewPort(true);
//        scheduledWebview.getSettings().setLoadWithOverviewMode(true);

        startWebView();
    }

    private void startWebView() {
//        mDialog = getDialog(getActivity());
        scheduledWebview.getSettings().setPluginState(WebSettings.PluginState.ON);

        scheduledWebview.setWebChromeClient(new WebChromeClient(){
            // Need to accept permissions to use the camera
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
//                L.d("onPermissionRequest");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });
//        scheduledWebview.setWebChromeClient(new WebChromeClient());
//        scheduledWebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
//                super.shouldOverrideUrlLoading(view, url);
//                return false;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                mDialog.show();
//                scheduledWebview.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                scheduledWebview.setVisibility(View.VISIBLE);
//                mDialog.dismiss();
//                view.loadUrl("javascript:(function() { document.querySelectorAll('div').forEach(function(item, index){" +
//                        "    if(index != 0){" +
//                        "        item.hidden = true;" +
//                        "    }" +
//                        "})})()");
//                view.loadUrl("javascript:(function() { document.querySelectorAll('form div').forEach(function(item, index){" +
//                        "    if(index != 0){" +
//                        "        item.hidden = false;" +
//                        "    }" +
//                        "})})()");
////                Log.e("TGED","DONE");
//                view.loadUrl("javascript:(function() { document.querySelectorAll('td,th,body').forEach(function(item){item.style.backgroundColor='#FFF';}) })()");
//                view.loadUrl("javascript:(function() { " +
//                        "document.querySelector('table').parentElement.hidden=false" +
//                        " })()");
//            }
//        });

//        scheduledWebview.loadUrl("http://www.gencalc.com/gen/eng_genc.php?sp=0LBmaskGr");
        scheduledWebview.loadUrl("https://sdk.developer.deepar.ai/virtualTryOn/index.html");

    }


    private void internetAvail() {

        initWebview("https://sdk.developer.deepar.ai/virtualTryOn/index.html");

    }



}