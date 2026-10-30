package com.cafe.priceapp;

import android.content.Context;
import android.print.PrintManager;
import android.webkit.WebView;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "NativePrint")
public class NativePrintPlugin extends Plugin {

    @PluginMethod
    public void print(PluginCall call) {
        try {
            getActivity().runOnUiThread(() -> {
                try {
                    PrintManager printManager =
                        (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);

                    WebView webView = getBridge().getWebView();

                    if (printManager == null || webView == null) {
                        call.reject("امکان چاپ در این دستگاه وجود ندارد.");
                        return;
                    }

                    String jobName = "Cafe Invoice";
                    android.print.PrintDocumentAdapter adapter =
                        webView.createPrintDocumentAdapter(jobName);

                    printManager.print(jobName, adapter, null);

                    JSObject result = new JSObject();
                    result.put("success", true);
                    call.resolve(result);

                } catch (Exception e) {
                    call.reject("خطا هنگام شروع چاپ: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            call.reject("خطا در اجرای چاپ: " + e.getMessage());
        }
    }
}
