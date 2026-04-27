package com.father.proverbs;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.Locale;

public class MainActivity extends Activity {
    private WebView webView;
    private TextToSpeech tts;
    private float rate = 0.92f;
    private float pitch = 1.0f;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        webView.addJavascriptInterface(new Bridge(), "AndroidTTS");

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.KOREAN);
                tts.setSpeechRate(rate);
                tts.setPitch(pitch);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override public void onStart(String id) { }
                    @Override public void onError(String id) { onDone(id); }
                    @Override public void onDone(String id) {
                        runOnUiThread(() -> webView.evaluateJavascript("window.onNativeTtsDone && window.onNativeTtsDone();", null));
                    }
                });
            }
        });
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }

    public class Bridge {
        @JavascriptInterface public void speak(String text) {
            if (tts == null) return;
            tts.setLanguage(Locale.KOREAN);
            tts.setSpeechRate(rate);
            tts.setPitch(pitch);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "verse");
        }
        @JavascriptInterface public void stop() { if (tts != null) tts.stop(); }
        @JavascriptInterface public void setRate(float r) { rate = r; if (tts != null) tts.setSpeechRate(rate); }
        @JavascriptInterface public void setPitch(float p) { pitch = p; if (tts != null) tts.setPitch(pitch); }
    }
}
