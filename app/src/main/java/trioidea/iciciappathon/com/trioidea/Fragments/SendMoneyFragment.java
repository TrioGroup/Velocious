package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.Services.FileClientAsyncTask;
import trioidea.iciciappathon.com.trioidea.Services.WiFiDirectBroadcastReceiver;

/**
 * Created by Harshal on 12-Apr-17.
 */
public class SendMoneyFragment extends Fragment
{

    TransferActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {
        return layoutInflater.inflate(R.layout.send_money,viewGroup,false);
    }

    public void initUi()

    {
        parentActivity=(TransferActivity)SendMoneyFragment.this.getActivity();

        parentActivity.textView = (TextView) parentActivity.findViewById(R.id.balance);
        parentActivity.textView.setText( Double.toString(parentActivity.balance));

        parentActivity.adapter = new ArrayAdapter(parentActivity, R.layout.activity_listview, parentActivity.mobiles);
        parentActivity.listView= (ListView) parentActivity.findViewById(R.id.mobile_list);
        parentActivity.listView.setAdapter(parentActivity.adapter);

        parentActivity.amount = (EditText) parentActivity.findViewById(R.id.amount);
        parentActivity.amount.clearFocus();

        parentActivity.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = ((TextView) view).getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_LONG).show();
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = item;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
                parentActivity.mWifiP2pManager.connect(parentActivity.mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("p2p", "connected to device");
                        new FileClientAsyncTask(getActivity().getApplicationContext(), parentActivity.address, parentActivity.amount.getText().toString(), parentActivity).execute();
                    }

                    @Override

                    public void onFailure(int reason) {
                        Log.e("p2p", "couldn't connect to device.. reason:" + reason);
                    }
                });
            }
        });
        parentActivity.adapter.notifyDataSetChanged();

    }


    @Override
    public void onResume()
    {
        super.onResume();
        initUi();
        parentActivity.mReceiver = new WiFiDirectBroadcastReceiver(parentActivity.mWifiP2pManager, parentActivity.mChannel, parentActivity);
        parentActivity.registerReceiver(parentActivity.mReceiver, parentActivity.mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        parentActivity.unregisterReceiver(parentActivity.mReceiver);
    }


}