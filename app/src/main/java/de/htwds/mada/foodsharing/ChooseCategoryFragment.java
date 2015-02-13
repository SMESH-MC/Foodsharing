package de.htwds.mada.foodsharing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioGroup;

public class ChooseCategoryFragment extends DialogFragment implements AlertDialog.OnClickListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a category du Jockel")
                //TODO: setItems als setAdapter http://developer.android.com/guide/topics/ui/dialogs.html#AddingAList
                .setItems(R.array.categories_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //which: index position of selected item

                    }
                });

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
