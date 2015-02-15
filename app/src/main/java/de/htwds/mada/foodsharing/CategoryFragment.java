package de.htwds.mada.foodsharing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class CategoryFragment extends DialogFragment {

/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] values = new String[] { "Kategorie 1", "Kategorie 2", "Kategorie C", "noch", "unzählige", "mehr"};

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(   getActivity(),  android.R.layout.list_content,    values);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        Log.i("onlistitemclick", "item " + id + "clicked");
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] values = new String[] { "Kategorie 1", "Kategorie 2", "Kategorie C", "noch", "unzählige", "mehr"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Test")
               .setItems(values, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       EditText et = (EditText) getActivity().findViewById(R.id.offer_category_edit);
                       et.setText(values[which]);
                   }
               });
        return builder.create();
    }
}
