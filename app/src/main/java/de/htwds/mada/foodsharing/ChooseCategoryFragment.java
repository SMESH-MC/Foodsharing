package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioGroup;

public class ChooseCategoryFragment extends DialogFragment
        implements DialogInterface.OnClickListener {

    static int mSelectedIndex;
    static int mResourceArray;
    static OnDialogSelectorListener mDialogSelectorCallback;

    public interface OnDialogSelectorListener {
        public void onSelectedOption(int dialogID);
            //dialogID: selectedIndex

    }

    public static ChooseCategoryFragment newInstance(int res, int selected) { //, int selected
        final ChooseCategoryFragment dialog = new ChooseCategoryFragment();
        mResourceArray = res;
        mSelectedIndex = selected;
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDialogSelectorCallback = (OnDialogSelectorListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + "hat OnDialogSelectorListener nit implementiert du Hirni");
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
       
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Choose a category du Jockel")
                .setItems(R.array.categories_array, this)
                .setPositiveButton("OK", this)
                .setNegativeButton("Cancer", this);


        return builder.create();
    }
/*
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_chooser, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
     /*   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a category du Jockel")
               .setItems(R.array.categories_array, new AlertDialog.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               })
                //TODO: setItems als setAdapter http://developer.android.com/guide/topics/ui/dialogs.html#AddingAList
    /*             .setItems(R.array.categories_array, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //which: index position of selected item
                    }
                }) ;

        return builder.create();
    }
*/
}
