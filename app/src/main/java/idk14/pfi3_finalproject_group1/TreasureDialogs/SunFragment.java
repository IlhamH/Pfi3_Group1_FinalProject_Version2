package idk14.pfi3_finalproject_group1.TreasureDialogs;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import idk14.pfi3_finalproject_group1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SunFragment extends DialogFragment implements View.OnClickListener {


    public SunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sun, container, false);
        v.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        SunFragment.this.dismiss();
    }
}
