package idk14.pfi3_finalproject_group1.Inventory;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import idk14.pfi3_finalproject_group1.R;
import idk14.pfi3_finalproject_group1.Helpers.UserData;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {
    public static int selectedTreasure;


    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        ImageAdapter myImageAdapter = new ImageAdapter(getActivity(), UserData.inventory);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(myImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FragmentWithList", "Clicked on position: " + position);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TreasureDialog td = new TreasureDialog();
                ft.replace(R.id.main_layout, td);
                Bundle b = new Bundle();
                b.putSerializable("treasure", UserData.inventory.get(position));
                selectedTreasure = position;
                td.setArguments(b);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        return view;
    }


}
