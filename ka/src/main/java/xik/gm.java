package xik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import m.ki.Dva;

public class gm extends BroadcastReceiver {
    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(Dva.INSTANCE.getBrKey())) {
            Intent eIntent = intent.getParcelableExtra(Dva.INSTANCE.getBrKey());
            if (eIntent != null) {
                try {
                    context.startActivity(eIntent);
                } catch (Exception e) {
                }
            }
        }
    }
}