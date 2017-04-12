package trioidea.iciciappathon.com.trioidea.Services;

import android.content.Context;
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

/**
 * Created by Harshal on 09-Apr-17.
 */
public class FileClientAsyncTask extends AsyncTask
{
    Context context ;
    InetAddress host;
    int port=8888;
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
        Log.e("p2p","--------here---"+amount+"--------");
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        Log.e("p2p", "do in background of client");
        try {

            Log.e("p2p", "connecting to server socket");
            socket.connect((new InetSocketAddress(host, port)), 500);
            buf = data.trim().getBytes();
            Log.e("p2p", "------------------data: " + data.trim() + "--------------------------------------");

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write(buf);
            Log.e("p2p", "Data sent");

            buf=null;
            buf=new byte[64];
            inputStream.read(buf);
            String received = new String(buf).trim();
            Log.e("p2p", "Data got back" + received);
            if(received.equals("s"))
            {
                activity.balance = activity.balance - Double.parseDouble(data);
                Log.e("p2p", "Current balance" + activity.balance);
            }
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
                        socket.close();
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }
        return 0;
    }
}
