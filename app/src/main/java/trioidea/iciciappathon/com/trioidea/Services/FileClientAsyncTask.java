package trioidea.iciciappathon.com.trioidea.Services;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by Harshal on 09-Apr-17.
 */
public class FileClientAsyncTask extends AsyncTask {
    Context context;
    InetAddress host;
    int port = 8888;
    int len;
    Socket socket = new Socket();
    byte buf[];
    String data;
    TransferActivity activity;

    public FileClientAsyncTask(Context context, InetAddress hostId, String amount, TransferActivity main) {
        this.context = context;
        host = hostId;
        data = amount;
        activity = main;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("p2p", "do in background of client");
        try {

            Log.e("p2p", "connecting to server socket");
            socket.connect((new InetSocketAddress(host, port)), 500);
            Log.e("p2p", "------------------data: " + data.trim() + "--------------------------------------");
            buf = (data.trim()+":sentence").getBytes();

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write(buf);
            Log.e("p2p", "Data sent");

            buf = null;
            buf = new byte[2048];
            inputStream.read(buf);
            String received = new String(buf).trim();
            Log.e("p2p", "Data got back" + received);
            String[] receivedStrings=received.split(":");
            if (receivedStrings[0].equals("s")) {
                activity.balance = activity.balance - Double.parseDouble(data);
                Log.e("p2p", "Current balance" + activity.balance);
                buf = null;
                buf = new byte[64];
                buf = "s".getBytes();
                outputStream.write(buf);
                Log.e("p2p", "Transaction completed for client");

            }
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            //catch logic
        } catch (IOException e) {
            //catch logic
        }

/**
 * Clean up any open sockets when done
 * transferring or if an exception occurred.
 */ finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        Log.e("p2p", "Closing Socket");
                        socket.close();
                        activity.startTransactionScreenFragment();
                        activity.disconnect();
                        activity.mWifiP2pManager.requestGroupInfo(activity.mChannel, new WifiP2pManager.GroupInfoListener() {
                            @Override
                            public void onGroupInfoAvailable(WifiP2pGroup group) {
                                activity.deletePersistentGroup(group);
                                Log.e("p2p", "Permant group removed");
                            }
                        });
//                        activity.mWifiP2pManager.removeGroup(activity.mChannel, new WifiP2pManager.ActionListener() {
//                            @Override
//                            public void onSuccess() {
//                                Log.e("p2p", "Closing wifi connection");
//                            }
//
//                            @Override
//                            public void onFailure(int reason) {
//                                Log.e("p2p", "Failed to close wifi connection.. reason: " + reason);
//                            }
//                        });
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }
        EventResponse eventResponse = new EventResponse(0, EventNumbers.CLIENT_ASYNC_EVENT);
        RxBus rxBus=RxBus.getInstance();
        rxBus.send(eventResponse);
        return 0;
    }
}
