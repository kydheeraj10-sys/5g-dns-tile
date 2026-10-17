
package com.quickswitch.fivedns;

import android.content.Intent;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class FiveGTileService extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();

        Tile tile = getQsTile();
        if (tile != null) {
            tile.setLabel("5G Settings");
            tile.setSubtitle("Open network settings");
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    @Override
    public void onClick() {
        super.onClick();

        Intent intent = new Intent(
            Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivityAndCollapse(intent);
    }
}
