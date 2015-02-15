package de.htwds.mada.foodsharing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListFragment;
import android.widget.Toast;

public class CategoryFragment extends ListFragment  {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] values = new String[] { "Kategorie 1", "Kategorie 2", "Kategorie C", "noch", "unzählige", "mehr"};

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(   getActivity(),  android.R.layout.simple_list_item_1,    values);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        Log.i("onlistitemclick", "item " + id + "clicked");
    }

}
