package trioidea.iciciappathon.com.trioidea.Services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;

/**
 * Created by asus on 09/04/2017.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that which service class will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                SyncTransactionService.class.getName());
        Toast.makeText(context,"instart",Toast.LENGTH_SHORT).show();
        boolean connected=isConnected(context);
        /*TransactionDto[] transactionDtos=null;
        if(connected)
        {
            transactionDtos=DbHelper.getInstance(context).getNotSyncedTransaction();
        }*/
        intent.putExtra("isNetworkConnected",connected);
        //ArrayList<TransactionDto> arrayList = new ArrayList<TransactionDto>(Arrays.asList(transactionDtos));
        //intent.putExtra("transactionDTO",arrayList);
        context.startService(intent.setComponent(comp));
    }
    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
