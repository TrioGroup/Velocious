package trioidea.iciciappathon.com.trioidea.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Harshal on 07-Apr-17.
 */
public class FileServerAsyncTask extends AsyncTask {

    private Context context;
    private TextView statusText;
    byte buf[]  = new byte[64];

    public FileServerAsyncTask(Context context) {
        this.context = context;
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
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();
            Log.e("p2p","accepted socket");

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();
            if(inputStream.read(buf)>0)
            {
                String received = new String(buf).trim();
                Log.e("p2p", "data received" + received);
            }
            buf = "s".getBytes();
            outputStream.write(buf);
            Log.e("p2p", "data sent back");

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
//            serverSocket.close();
//            return f.getAbsolutePath();
        } catch (IOException e) {
            Log.e("p2p", e.getMessage());
            return null;
        }
        return 0;
    }
}
