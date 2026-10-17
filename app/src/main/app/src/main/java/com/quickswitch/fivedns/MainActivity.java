
package com.quickswitch.fivedns;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final String[] names = {
        "Google DNS", "Cloudflare DNS", "AdGuard DNS"
    };

    private final String[] hosts = {
        "dns.google",
        "one.one.one.one",
        "dns.adguard-dns.com"
    };

    private SharedPreferences prefs;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("dns_prefs", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (20 * getResources()
            .getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("5G & DNS Quick Switch");
        title.setTextSize(24);
        layout.addView(title);

        TextView info = new TextView(this);
        info.setText(
            "\nChoose a provider, copy its hostname, then " +
            "configure it in Android Private DNS settings.\n"
        );
        info.setTextSize(16);
        layout.addView(info);

        status = new TextView(this);
        status.setTextSize(18);
        layout.addView(status);

        for (int i = 0; i < names.length; i++) {
            final int index = i;
            Button button = new Button(this);
            button.setText(names[i] + "\n" + hosts[i]);
            layout.addView(button,
                new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ));

            button.setOnClickListener(v -> {
                prefs.edit()
                    .putInt("selected_dns", index).apply();

                ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(
                        Context.CLIPBOARD_SERVICE);

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "DNS hostname", hosts[index]));

                updateStatus();

                Toast.makeText(this,
                    hosts[index] + " copied. Apply it in Private DNS.",
                    Toast.LENGTH_LONG).show();
            });
        }

        Button settings = new Button(this);
        settings.setText("Open Private DNS Settings");
        layout.addView(settings);

        settings.setOnClickListener(v -> {
            try {
                startActivity(new Intent(
                    "android.settings.PRIVATE_DNS_SETTINGS"));
            } catch (Exception e) {
                startActivity(new Intent(
                    Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        Button network = new Button(this);
        network.setText("Open Mobile Network Settings");
        layout.addView(network);

        network.setOnClickListener(v ->
            startActivity(new Intent(
                Settings.ACTION_WIRELESS_SETTINGS)));

        setContentView(layout);
        updateStatus();
    }

    private void updateStatus() {
        int index = prefs.getInt("selected_dns", 0);
        status.setText("\nSelected: " + names[index] +
            "\nHostname: " + hosts[index] + "\n");
    }
}
