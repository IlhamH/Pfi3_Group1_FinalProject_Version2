package idk14.pfi3_finalproject_group1.View;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import idk14.pfi3_finalproject_group1.Helpers.UserData;
import idk14.pfi3_finalproject_group1.R;
import idk14.pfi3_finalproject_group1.View.HelpFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener {

    EditText usernameText;


    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.welcome, container, false);

        usernameText= (EditText) v.findViewById(R.id.editText_username);

        Button startButton = (Button) v.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_button) {

            UserData.username = usernameText.getText().toString();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_layout, new HelpFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }


}
