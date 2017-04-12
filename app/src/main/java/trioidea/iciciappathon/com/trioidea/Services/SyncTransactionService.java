package trioidea.iciciappathon.com.trioidea.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
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

    public SyncTransactionService() {
        super();
        rxBus.toObserverable().subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe(this);
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
            TransactionDto[] transactionDtos = null;
            transactionDtos = DbHelper.getInstance(getApplicationContext()).getNotSyncedTransaction();
            // ArrayList<TransactionDto> transactionDto=(ArrayList<TransactionDto>)intent.getSerializableExtra("transactionDTO");
            if (transactionDtos!=null && transactionDtos.length > 0) {
                ServiceLayer serviceLayer = ServiceLayer.getServiceLayer();
                serviceLayer.autheticateUser();
                serviceLayer.fundTransfer(transactionDtos);
                // serviceLayer.f
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

    @Override
    public void onTaskRemoved(Intent rootIntent) {
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
                    TransactionDto[] transactionDtos1=DbHelper.getInstance(getApplicationContext()).getAllTransaction();
                    Log.e("Service fund transfer",""+transactionDtos1[0].isSyncFlag());
                }
                Log.e("Event fund transfer", "here");
                break;
            case EventNumbers.AUTHENTICATE_USER:
                AuthenticateDto authenticateDtoArrayList = (AuthenticateDto) ((EventResponse) o).getResponse();
                Log.e("Event Authenticate user", authenticateDtoArrayList.getToken());
                ((EventResponse) o).setEvent(EventNumbers.AUTHENTICATE_USER);
                rxBus.send((EventResponse) o);
                break;
        }
    }
}