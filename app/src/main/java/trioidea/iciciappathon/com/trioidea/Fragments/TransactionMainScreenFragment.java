package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.Services.FileServerAsyncTask;

/**
 * Created by Harshal on 12-Apr-17.
 */
public class TransactionMainScreenFragment extends Fragment {

    TransferActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {
        return layoutInflater.inflate(R.layout.transaction_main_screen,viewGroup,false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        initUi();
    }
    public void initUi()

    {
        parentActivity=(TransferActivity)TransactionMainScreenFragment.this.getActivity();

        ((ImageButton)getActivity().findViewById(R.id.wifi_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("p2p", "discoverPeers() called--sender");
                parentActivity.getManager()
                        .discoverPeers(parentActivity.getChannel(), new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Log.e("p2p", "discoverPeers() Success--sender");
                                parentActivity.startSendScreenFragment();
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.e("p2p", "discoverPeers() Failure: " + reason);
                                if (reason == 2)
                                    Toast.makeText(getActivity(), "Start your wifi and try again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        (getActivity().findViewById(R.id.wifi_receive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("p2p", "discoverPeers() called--receiver");


                (parentActivity).getManager()
                        .discoverPeers(parentActivity.getChannel(), new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                Log.e("p2p", "discoverPeers() Success--receiver");
                                new FileServerAsyncTask(getActivity()).execute();
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.e("p2p", "discoverPeers() Failure: " + reason);
                                if (reason == 2)
                                    Toast.makeText(getActivity(), "Start your wifi and try again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
