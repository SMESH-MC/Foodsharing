package de.htwds.mada.foodsharing;


import android.view.View;
import android.widget.AdapterView;

public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        //communication with Suchergebnissen
    }
}
