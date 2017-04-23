package trioidea.iciciappathon.com.trioidea.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;

import rx.Observer;
import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by Harshal on 05-Apr-17.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    ProgressDialog progressDialog;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private TransferActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = (TransferActivity) activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            Log.e("p2p", "Wifi state changed");
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.e("p2p", "Requesting for peers");

            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {


            {

                if (mManager == null) {
                    return;
                }

                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if (networkInfo.isConnected()) {
                    mManager.requestConnectionInfo(mChannel,
                            new WifiP2pManager.ConnectionInfoListener() {

                                @Override
                                public void onConnectionInfoAvailable(
                                        WifiP2pInfo info) {
                                    if (info != null) {
                                        Log.e("p2p","Connection information:---" + info.groupOwnerAddress);
                                        mActivity.address = info.groupOwnerAddress;
                                        new FileClientAsyncTask(mActivity.getApplicationContext(), mActivity.address, mActivity.amount.getText().toString(), mActivity).execute();
                                    }
                                }
                            }

                    );
                } else {

                }
            }
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.e("p2p", "Wifi device state changed");
        }
    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            Log.e("p2p", "deviceCount = " + peers.getDeviceList().size());
            Collection<WifiP2pDevice> deviceList = peers.getDeviceList();
            if(deviceList.size()>0)
            {
                Iterator<WifiP2pDevice> iterator= deviceList.iterator();
                while(iterator.hasNext()!=false)
                {
                    mActivity.mobiles.clear();
                    mActivity.mobileNames.clear();
                    WifiP2pDevice device = iterator.next();
//                    Log.e("p2p", "device  = " + device.toString());
                    if (mActivity.mobiles.contains(device.deviceAddress) == false && mActivity.mobileNames.contains(device.deviceName) == false )
                    {
                        mActivity.mobileNames.add(device.deviceName);
                        mActivity.mobiles.add(device.deviceAddress);
                        if(mActivity.adapter != null)
                            mActivity.adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };


}

