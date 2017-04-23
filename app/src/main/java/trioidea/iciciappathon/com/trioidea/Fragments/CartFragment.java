package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import trioidea.iciciappathon.com.trioidea.Activities.ShoppingActivity;
import trioidea.iciciappathon.com.trioidea.Adapters.CartAdapter;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 23/04/2017.
 */
public class CartFragment extends Fragment {
    AlertDialog alertDialog;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.cart_list, viewGroup, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        ImageButton confirm = (ImageButton) getActivity().findViewById(R.id.checkout);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(getActivity()).create();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "unknown");
                String address = sharedPreferences.getString("address", "default");
                String phoneNo = sharedPreferences.getString("phone", "default");
                if (!name.equals("unknown"))
                    name = EncryptionClass.symmetricDecrypt(name);
                if(!address.equals("default"))
                    address=EncryptionClass.symmetricDecrypt(address);
                if(!phoneNo.equals("default"))
                    phoneNo=EncryptionClass.symmetricDecrypt(phoneNo);
                alertDialog.setMessage("Name: "+name+"\n\nAddress: "+address+"\n\nPhone Number: "+phoneNo+
                        "\n\nAre you sure you want to purchase these products?");
                alertDialog.setTitle("Confirm");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Products purchased", Toast.LENGTH_LONG).show();
                        ((ShoppingActivity) getActivity()).getFragmentManager().popBackStack();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        TextView textView = (TextView) getActivity().findViewById(R.id.no_items);
        if (((ShoppingActivity) getActivity()).getItemListDTOArrayList().size() == 0) {
            textView.setText("No items to display.");
            textView.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
        }
        ListView listView = (ListView) getActivity().findViewById(R.id.cartList);
        CartAdapter cartAdapter = new CartAdapter(getActivity(), ((ShoppingActivity) getActivity()).getItemListDTOArrayList());
        listView.setAdapter(cartAdapter);
    }
}
