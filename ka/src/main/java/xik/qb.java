package xik;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import m.ki.Dva;

public class qb extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView z0, int z1) {
        super.onProgressChanged(z0, z1);
        Dva.INSTANCE.dexLog("H5:" + z1);
        if (z1 == 100) {af.u4(z1);}
    }
}
