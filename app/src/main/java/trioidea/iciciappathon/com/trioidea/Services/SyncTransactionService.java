package trioidea.iciciappathon.com.trioidea.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.CheckTransactionDTO;
import trioidea.iciciappathon.com.trioidea.DTO.FundTransferDto;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by asus on 09/04/2017.
 */
public class SyncTransactionService extends Service implements Observer {

    static int count = 0;
    RxBus rxBus = RxBus.getInstance();
    Intent intent;
    Subscription subscription;

    public SyncTransactionService() {
        super();
        subscription = rxBus.toObserverable().subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe(this);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        this.intent = intent;
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        if (isNetworkConnected) {
            /*Observable observable = Observable.fromCallable(new Callable() {
                @Override
                public Object call() throws Exception {
                    count++;
                    String jsonresponse = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/participantmapping?client_id=sandeshbankar24@gmail.com");
                    //String jsonresponse=WebService.getJSON("https://corporateapiprojectwar.mybluemix.net/corporate_banking/mybank/authenticate_client?client_id=sandeshbankar24@gmail.com&password=LP549V52");
                    Log.e("onHandleIntent", "count:" + count + " " + jsonresponse);
                    createNotification();
                    return jsonresponse;

                *//*Gson gson = new Gson();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    userDto = gson.fromJson(jsonObject.toString(), UserDto.class);
                    return userDto;
                } catch (Exception e) {
                    return e;
                    // return Observable.error(e);
                }*//*

                }
            });
            observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(this);*/

            // ArrayList<TransactionDto> transactionDto=(ArrayList<TransactionDto>)intent.getSerializableExtra("transactionDTO");
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
            String tokenId = sharedPreferences.getString("tokenId", null);
            if (tokenId == null || tokenId.isEmpty()) {
                Observable observable = Observable.fromCallable(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            AuthenticateClient authenticateClient = new AuthenticateClient();
                            EventResponse eventResponse = authenticateClient.authenticateClientWB("sandeshbankar24@gmail.com", "LP549V52");
                            return eventResponse;
                        } catch (Exception e) {
                            return e;
                        }
                    }
                });
                observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(SyncTransactionService.this);
                // serviceLayer.f
            } else {
                TransactionDto[] transactionDtos = null;
                transactionDtos = DbHelper.getInstance(getApplicationContext()).getNotSyncedTransaction();
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
                editor.putString("tokenId", tokenId);
                editor.commit();
                checkHistoryToSync(transactionDtos);
                ServiceLayer.getServiceLayer().checkTransactionOnServer(transactionDtos);
            }
        }
        return START_STICKY;
    }

    public void createNotification() {
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(this, SyncTransactionService.class), 0);
        Notification.Builder mBuilder = new Notification.Builder(this);

        Notification noti = mBuilder.setContentTitle("New mail from " + "test@gmail.com")
                .setTicker("title")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentText("Subject")
                .setAutoCancel(true)
                .setContentIntent(pIntent).getNotification();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notificationManager.notify(0, noti);

    }

    public void checkHistoryToSync(TransactionDto[] transactionDtos) {


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
        Intent broadcastIntent = new Intent("android.net.conn.CONNECTIVITY_CHANGE");
        sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        EventResponse eventResponse = (EventResponse) o;
        switch (((EventResponse) o).getEvent()) {
            case EventNumbers.FUND_TRANSFER:
                FundTransferDto fundTransferDto;
                TransactionDto transactionDto;
                if ((((EventResponse) o).getResponse()).getClass().getName().equalsIgnoreCase("FundTransferDTO")) {
                    fundTransferDto = (FundTransferDto) ((EventResponse) o).getResponse();
                } else {
                    transactionDto = (TransactionDto) ((EventResponse) o).getResponse();
                    DbHelper.getInstance(getApplicationContext()).markSynced(transactionDto);
                    TransactionDto[] transactionDtos1 = DbHelper.getInstance(getApplicationContext()).getAllTransaction();
                    Log.e("Service fund transfer", "" + transactionDtos1[0].isSyncFlag());
                }
                Log.e("Event fund transfer", "here");
                break;
            case EventNumbers.AUTHENTICATE_USER:
                ArrayList<AuthenticateDto> authenticateDtoArrayList = (ArrayList<AuthenticateDto>) ((EventResponse) o).getResponse();
                Log.e("Event Authenticate user", authenticateDtoArrayList.get(0).getToken());
                ((EventResponse) o).setEvent(EventNumbers.AUTHENTICATE_USER);
                TransactionDto[] transactionDtos = null;
                transactionDtos = DbHelper.getInstance(getApplicationContext()).getNotSyncedTransaction();
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
                editor.putString("tokenId", authenticateDtoArrayList.get(0).getToken());
                editor.commit();
                checkHistoryToSync(transactionDtos);
                ServiceLayer.getServiceLayer().setTokenId(authenticateDtoArrayList.get(0).getToken());
                ServiceLayer.getServiceLayer().checkTransactionOnServer(transactionDtos);
                break;
            case EventNumbers.CHECK_TRANSACTION_EVENT:
                TransactionDto[] transactionDtos1 = (TransactionDto[]) ((EventResponse) o).getResponse();
                Log.e("Event Authenticate user", ""+transactionDtos1[0].isSyncFlag());
                ServiceLayer.getServiceLayer().fundTransfer((TransactionDto[]) transactionDtos1);
                break;
        }

    }
}