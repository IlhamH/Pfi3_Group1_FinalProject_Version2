package idk14.pfi3_finalproject_group1;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * Created by Lars
 * A simple {@link Fragment} subclass.
 */
public class TreasureDialog extends Fragment implements View.OnClickListener {
    public Button scanButton;
    public String treasureValue;
    public TextView treasureText;
    public String scanContent;
    public TextView desText;
    public ImageView treasureImage;
    public Button backButton;

    public TreasureDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_treasure_dialog, container, false);

        treasureValue = (String) getArguments().getSerializable("treasure");

        scanButton = (Button) view.findViewById(R.id.scan_button);
        backButton = (Button) view.findViewById(R.id.back_to_game_button);

        treasureText = (TextView) view.findViewById(R.id.treasureText);
        desText = (TextView) view.findViewById(R.id.treasureDescription);

        treasureImage = (ImageView) view.findViewById(R.id.treasureImage);

        if(treasureValue.equals("1")){
            treasureText.setText("Water");
            treasureImage.setImageDrawable(getResources().getDrawable(R.drawable.treasure_water));


        }
        if(treasureValue.equals("2")){
            treasureText.setText("Air");
            treasureImage.setImageDrawable(getResources().getDrawable(R.drawable.treasure_air));


        }
        if(treasureValue.equals("3")){
            treasureText.setText("Sun");
            treasureImage.setImageDrawable(getResources().getDrawable(R.drawable.treasure_sun));


        }
        if(treasureValue.equals("0")){
            desText.setText("Nothing here!\nGo find treasure to fill here.");
            treasureImage.setImageDrawable(getResources().getDrawable(R.drawable.treasure_empty));
            //desText.setVisibility(View.INVISIBLE);
            scanButton.setVisibility(View.GONE);



        }

        scanButton.setOnClickListener(this);
        backButton.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if (view.getId() == R.id.back_to_game_button){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_layout, new GameFragment());
            ft.commit();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode != 0 && intent != null) {

            scanContent = scanningResult.getContents();
            //String scanFormat = scanningResult.getFormatName();
            //contentTxt.setText("CONTENT: " + scanContent);
            System.out.println("content: " + scanContent);

            if(scanContent.equals("TREE")){
                System.out.println("You scanned the tree!");
                updateLightCue();


            }else{
                System.out.println("This is not the tree");
                desText.setText("Bring your treasure back to the tree!");

            }

        } else {
            Toast.makeText(getActivity(), "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLightCue(){

        Firebase lightCueRef = GameFragment.ref.child("LightCue");

        lightCueRef.setValue(Integer.parseInt(treasureValue));

        System.out.println("You got a light show.");

        //update text and images:

        desText.setText("Look up! You got a light show!");
        treasureImage.setImageDrawable(getResources().getDrawable(R.drawable.treasure_one_point));
        //desText.setVisibility(View.GONE);
        scanButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);

        //remove the item that has been delivered from the arraylist
        UserData.inventory.remove(InventoryFragment.selectedTreasure);
        //add an empty element to the end of the list
        UserData.inventory.add("0");
        //remove one from int totalTreasuresInInventory
        UserData.totalTreasuresInInventory -= 1;

        updateTotalScore();
    }

    public void updateTotalScore(){
        UserData.totalScore += 1;
        GameFragment.tvTotalScore.setText(String.valueOf(UserData.totalScore));
        System.out.println("Total score updated to: " + UserData.totalScore);

    }

    public void goToGameFragment(View view){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_layout, new GameFragment());
        ft.commit();
    }


}

