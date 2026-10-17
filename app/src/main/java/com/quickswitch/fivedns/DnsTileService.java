
package com.quickswitch.fivedns;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class DnsTileService extends TileService {
    private final String[] names = {
        "Google DNS", "Cloudflare DNS", "AdGuard DNS"
    };

    private final String[] hosts = {
        "dns.google",
        "one.one.one.one",
        "dns.adguard-dns.com"
    };

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        int current = getSharedPreferences(
            "dns_prefs", MODE_PRIVATE)
            .getInt("selected_dns", 0);

        int next = (current + 1) % names.length;

        getSharedPreferences("dns_prefs", MODE_PRIVATE)
            .edit().putInt("selected_dns", next).apply();

        updateTile();

        Intent intent = new Intent(
            "android.settings.PRIVATE_DNS_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivityAndCollapse(intent);
        } catch (Exception e) {
            startActivityAndCollapse(new Intent(
                android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile == null) return;

        int index = getSharedPreferences(
            "dns_prefs", MODE_PRIVATE)
            .getInt("selected_dns", 0);

        tile.setLabel(names[index]);
        tile.setSubtitle(hosts[index]);
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }
}
