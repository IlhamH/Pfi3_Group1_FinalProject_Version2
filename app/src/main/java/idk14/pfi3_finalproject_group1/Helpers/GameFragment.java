package idk14.pfi3_finalproject_group1.Helpers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import idk14.pfi3_finalproject_group1.Helpers.Constants;
import idk14.pfi3_finalproject_group1.Helpers.UserData;
import idk14.pfi3_finalproject_group1.Inventory.InventoryFragment;
import idk14.pfi3_finalproject_group1.R;
import idk14.pfi3_finalproject_group1.Scanner.IntentIntegrator;
import idk14.pfi3_finalproject_group1.Scanner.IntentResult;
import idk14.pfi3_finalproject_group1.TreasureDialogs.AirFragment;
import idk14.pfi3_finalproject_group1.TreasureDialogs.EmptyFragment;
import idk14.pfi3_finalproject_group1.TreasureDialogs.SunFragment;
import idk14.pfi3_finalproject_group1.TreasureDialogs.WaterFragment;


/**
 * by Lars
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    public static String myTreasure;
    public static String scanContent;
    public static Firebase ref = new Firebase(Constants.FIREBASE_URL);
    public static TextView tvTotalScore;
    public Button inventoryButton;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        tvTotalScore = (TextView)v.findViewById(R.id.textViewScore);
        tvTotalScore.setText(String.valueOf(UserData.totalScore));

        Button scanButton = (Button) v.findViewById(R.id.scanButton);
        inventoryButton = (Button) v.findViewById(R.id.inventory_button);

        if(UserData.totalTreasuresInInventory != 0){
            inventoryButton.setText("Inventory(" + UserData.totalTreasuresInInventory + ")");
        }

        inventoryButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);

        return v;
    }

    //Sets scan button to open up the scanner
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.scanButton) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        if (view.getId() == R.id.inventory_button){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_layout, new InventoryFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    //What happens once the user has scanned a QR code
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode != 0 && intent != null) {

            scanContent = scanningResult.getContents();

            System.out.println("content: " + scanContent);

            //method that checks the status of the firebase connection
            checkFirebaseConnection();

            if(scanContent.equals("TREE")){
                System.out.println("You scanned Bloom!");

                // If you scan Bloom and you have no treasure - toast
                if(UserData.inventory.get(0).equals("0")) {
                    Toast.makeText(getActivity(), "You scanned Bloom! Go get a treasure first!", Toast.LENGTH_LONG).show();
                }

            }else{
                checkFirebase();
            }

        } else {
            Toast.makeText(getActivity(), "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFoundTreasure(){
        // If you get Water treasure
        if(myTreasure.equals("1")) {

          addTreasureToInventory("1");

            FragmentManager fm = getFragmentManager();
            WaterFragment wf = new WaterFragment();
            wf.show(fm, "Water");
        }

        // If you get Air treasure
        if(myTreasure.equals("2")){
            addTreasureToInventory("2");

            FragmentManager fm = getFragmentManager();
            AirFragment af = new AirFragment();
            af.show(fm, "Air");


        }
        // If you get Sun treasure
        if(myTreasure.equals("3")){
            addTreasureToInventory("3");

            FragmentManager fm = getFragmentManager();
            SunFragment sf = new SunFragment();
            sf.show(fm, "Sun");
        }

        // If you scan inactive treasure QR
        if(myTreasure.equals("0")){

            FragmentManager fm = getFragmentManager();
            EmptyFragment ef = new EmptyFragment();
            ef.show(fm, "Empty");
        }
    }


    public void addTreasureToInventory(String treasureIn){
        //loops through inventory, finds first 0 element (no treasure)
        for(int i = 0; i < UserData.inventory.size(); i ++){
            if(UserData.inventory.get(i).equals("0")){

                //sets first empty element too incoming treasure value
                UserData.inventory.set(i, treasureIn);
                System.out.println("Treasure added to inventory of type: " + treasureIn);

                // Inventory button - counts how many treasure there is in the inventory
                UserData.totalTreasuresInInventory += 1;
                inventoryButton.setText("Inventory (" + UserData.totalTreasuresInInventory + ")");
                break;
            }
        }

    }


    //Firebase methods

    public void checkFirebase() {
        Firebase treasureRef = ref.child(scanContent);


        treasureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Treasure value is: " + snapshot.getValue());

                //stores value in the myTreasure object
                myTreasure = snapshot.getValue().toString();

                //checks that the value has been stored correctly
                System.out.println("My treasure type is: " + myTreasure);


                System.out.println("Firebase checked!");

                if(myTreasure != null) {
                    updateFirebase();

                    showFoundTreasure();
                    System.out.println("Here is your treasure!");
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    public void updateFirebase() {

        Firebase treasureRef = ref.child(scanContent);
        treasureRef.setValue(0);

        System.out.println("Firebase updated!");

    }

    public void checkFirebaseConnection() {

        Firebase connectedRef = new Firebase(Constants.FIREBASE_URL + "/.info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                    Toast.makeText(getActivity(), "No network connection", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }


}
