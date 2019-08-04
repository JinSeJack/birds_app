package com.bage.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.bage.activity.MenuActivity;

public class WhatIDoFragment extends DialogFragment {

    public static final String bird = "鸟";
    public static final String cicada = "蝉";
    private String[] species = {bird, cicada};

    private WhatIDoFragment.SpeciesChooseListener speciesChooseListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(species, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (speciesChooseListener != null)
                    speciesChooseListener.onSpeciesChoosed(species[which]);
            }
        });
        return builder.create();
    }

    public void setSpeciesChooseListener(WhatIDoFragment.SpeciesChooseListener speciesChooseListener) {
        this.speciesChooseListener = speciesChooseListener;
    }

    public interface SpeciesChooseListener {
        void onSpeciesChoosed(String species);
    }

}