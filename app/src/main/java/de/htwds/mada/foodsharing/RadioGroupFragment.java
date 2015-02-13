package de.htwds.mada.foodsharing;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.RadioGroup;

public class RadioGroupFragment extends DialogFragment
    implements RadioGroup.OnCheckedChangeListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
        Datenbank-Abfrage nach allen Kategorien, um daraus RadioGroup zu erstellen

         */


     //   return new RadioGroup(getActivity(), );
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
