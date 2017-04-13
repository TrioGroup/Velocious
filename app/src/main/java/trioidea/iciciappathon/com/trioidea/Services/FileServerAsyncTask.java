package trioidea.iciciappathon.com.trioidea.Services;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by Harshal on 07-Apr-17.
 */
public class FileServerAsyncTask extends AsyncTask {

    private Context context;
    RxBus rxBus=RxBus.getInstance();
    private TextView statusText;
    byte buf[]  = new byte[64];
    ServerSocket serverSocket;
    Socket client;
    TransferActivity activity;

    public FileServerAsyncTask(Context context, TransferActivity main ) {
        this.context = context;
        activity = main;
//        this.statusText = (TextView) statusText;
    }


    /**
     * Start activity that can handle the JPEG image
     */
    protected void onPostExecute(String result) {
//        if (result != null) {
//            statusText.setText("File copied - " + result);
//            Intent intent = new Intent();
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse("file://" + result), "image/*");
//            context.startActivity(intent);
//        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("p2p","do in background of server");
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
             serverSocket = new ServerSocket(8888);
             client = serverSocket.accept();
            Log.e("p2p","accepted socket");

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();
            if(inputStream.read(buf)>0)
            {
                String received = new String(buf).trim();
                Log.e("p2p", "data received" + received);
                String amount = received.trim();
                activity.balance = activity.balance + Double.parseDouble(amount);
                Log.e("p2p", "Updated balance : " + activity.balance);
            }
            buf = "s".getBytes();
            outputStream.write(buf);
            Log.e("p2p", "data sent back");
            buf=null;
            buf=new byte[64];
            if(inputStream.read(buf)>0)
            {
                String received = new String(buf).trim();
                if(received == "s")
                {
                    Log.e("p2p", "Transaction Complete for Server" + received);
                }
            }
            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
//            final File f = new File(Environment.getExternalStorageDirectory() + "/"
//                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
//                    + ".jpg");
//
//            File dirs = new File(f.getParent());
//            if (!dirs.exists())
//                dirs.mkdirs();
//            f.createNewFile();
//            InputStream inputstream = client.getInputStream();
//
//            return f.getAbsolutePath();
        } catch (IOException e) {
            Log.e("p2p", e.getMessage());
            return null;
        }
        finally {
            if (serverSocket != null) {
//                if (serverSocket.isBound() || !serverSocket.isClosed())
                {
                    try {
                        Log.e("p2p", "Closing ServerSocket");
                        serverSocket.close();
                        client.close();
                        activity.startTransactionScreenFragment();
                        activity.disconnect();
                        activity.mWifiP2pManager.requestGroupInfo(activity.mChannel, new WifiP2pManager.GroupInfoListener() {
                            @Override
                            public void onGroupInfoAvailable(WifiP2pGroup group) {
                                activity.deletePersistentGroup(group);
                                Log.e("p2p"," group removed");
                            }
                        });
//                        activity.mIntentFilter=null;
//                        activity.mWifiP2pManager.removeGroup(activity.mChannel, new WifiP2pManager.ActionListener() {
//                            @Override
//                            public void onSuccess() {
//                                Log.e("p2p", "Closing wifi connection");
//                            }
//
//                            @Override
//                            public void onFailure(int reason) {
//                                Log.e("p2p", "Failed to close wifi connection.. reason: "+reason);
//                            }
//                        });
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }

        return 0;

    }
}
