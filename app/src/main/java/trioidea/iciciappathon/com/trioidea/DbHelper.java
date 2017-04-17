package trioidea.iciciappathon.com.trioidea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import trioidea.iciciappathon.com.trioidea.DTO.TransactionDTOEncrypted;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;

/**
 * Created by asus on 05/04/2017.
 * class file created for having db functions.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String dbName = "velocious.db";
    private static final int dbVersion = 1;
    private static DbHelper dbHelper;
    //private static SQLiteDatabase mydatabase;
    SQLiteDatabase db;
    Context context;
    private String[] comlumns = {"t_id", "sender_id", "sender_name", "receiver_id", "receiver_name", "amount", "time", "balance", "sync_flag"};

    private DbHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        //SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(dbName),null);
        db = this.getWritableDatabase();
        //mydatabase = SQLiteDatabase.openOrCreateDatabase("transactionDB",null);
    }

    public static DbHelper getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DbHelper(context);
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TRANSACTION = "CREATE TABLE transactions(t_id INTEGER PRIMARY KEY AUTOINCREMENT,sender_id TEXT,sender_name TEXT,receiver_id TEXT,receiver_name TEXT,amount TEXT,time TEXT,balance TEXT,sync_flag TEXT);";
        //String CREATE_TABLE_TRANSACTION = "CREATE TABLE transactions(t_id TEXT,sender_id TEXT);";
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void insertTransaction(TransactionDto transactionDto) {
        ContentValues values = new ContentValues();
       /* values.put("t_id", String.valueOf(transactionDto.getTransactionId()));
        values.put("sender_id", String.valueOf(transactionDto.getSenderID()));*/
        //values.put("t_id", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getTransactionId())).trim());
        values.put("sender_id", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getSenderID())).trim());
        values.put("sender_name", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getSenderName())).trim());
        values.put("receiver_id", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getReceiverId())).trim());
        values.put("receiver_name", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getReceiverName())).trim());
        values.put("amount", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getAmount())).trim());
        values.put("time", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getTime())).trim());
        values.put("balance", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.getBalance())).trim());
        values.put("sync_flag", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.isSyncFlag())).trim());

        //String insertQuery = "INSERT INTO transactions VALUES(\"" + transactionId.trim() + "\",\"" + senderID.trim() + "\",\"" + senderName.trim() + "\",\"" + receiverId.trim() + "\",\"" + receiverName.trim() + "\",\"" + amount.trim() + "\",\"" + time.trim() + "\",\"" + balance.trim() + "\",\"" + syncFlag .trim()+ "\");";
        db.insert("transactions", null, values);
    }

    public void insertEncryptedValuesInTransaction(TransactionDTOEncrypted transactionDto) {
        ContentValues values = new ContentValues();
        //values.put("t_id", (String.valueOf(transactionDto.getTransactionId())).trim());
        values.put("sender_id", (String.valueOf(transactionDto.getSenderID())).trim());
        values.put("sender_name", (String.valueOf(transactionDto.getSenderName())).trim());
        values.put("receiver_id", (String.valueOf(transactionDto.getReceiverId())).trim());
        values.put("receiver_name", (String.valueOf(transactionDto.getReceiverName())).trim());
        values.put("amount", (String.valueOf(transactionDto.getAmount())).trim());
        values.put("time", (String.valueOf(transactionDto.getTime())).trim());
        values.put("balance", (String.valueOf(transactionDto.getBalance())).trim());
        values.put("sync_flag", (String.valueOf(transactionDto.getSyncFlag())).trim());
        //String insertQuery = "INSERT INTO transactions VALUES(\"" + transactionId.trim() + "\",\"" + senderID.trim() + "\",\"" + senderName.trim() + "\",\"" + receiverId.trim() + "\",\"" + receiverName.trim() + "\",\"" + amount.trim() + "\",\"" + time.trim() + "\",\"" + balance.trim() + "\",\"" + syncFlag .trim()+ "\");";
        db.insert("transactions", null, values);
    }

    public TransactionDto[] getAllTransaction() {
        String selectQuery = "SELECT * FROM transactions;";
        int transactionId;
        long senderID;
        String senderName;
        long receiverId;
        String receiverName;
        double amount;
        String time;
        double balance;
        boolean syncFlag;
        TransactionDto[] transactionDtos = null;
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            //Cursor cursor = db.query(true, "transactions", comlumns, null, null, null, null, null, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            transactionDtos = new TransactionDto[count];
            int i = 0;
            int temp = cursor.getPosition();

            do {

               // Log.e("DBTEST", "" + EncryptionClass.symmetricDecrypt(cursor.getString(0)));
                //Log.e("DBTEST", "" + EncryptionClass.symmetricDecrypt(cursor.getString(1)));
                /*transactionId = Integer.parseInt((cursor.getString(0)));
                senderID = Integer.parseInt((cursor.getString(1)));*/
               transactionId =cursor.getInt(0);
                senderID = Long.parseLong(EncryptionClass.symmetricDecrypt(cursor.getString(1)));
                senderName = EncryptionClass.symmetricDecrypt(cursor.getString(2));
                receiverId = Long.parseLong(EncryptionClass.symmetricDecrypt(cursor.getString(3)));
                receiverName = EncryptionClass.symmetricDecrypt(cursor.getString(4));
                amount = Double.parseDouble(EncryptionClass.symmetricDecrypt(cursor.getString(5)));
                time = EncryptionClass.symmetricDecrypt(cursor.getString(6));
                balance = Double.parseDouble(EncryptionClass.symmetricDecrypt(cursor.getString(7)));
                syncFlag = Boolean.parseBoolean(EncryptionClass.symmetricDecrypt(cursor.getString(8)));
                transactionDtos[i] = new TransactionDto(transactionId, senderID, senderName, receiverId, receiverName, amount, time, balance, syncFlag);
                //transactionDtos[i] = new TransactionDto(transactionId, senderID, null,0,null, 0, null,0,false);
                //Log.e("DBTEST",""+transactionDtos[i].getTransactionId());
                i++;
            } while (cursor.moveToNext());
            cursor.close();
            return transactionDtos;
        } catch (Exception e) {
            return transactionDtos;
        }
    }

    public TransactionDTOEncrypted[] getAllTransactionNotDecrypted() {
        String selectQuery = "SELECT * FROM transactions;";
        String transactionId;
        String senderID;
        String senderName;
        String receiverId;
        String receiverName;
        String amount;
        String time;
        String balance;
        String syncFlag;
        TransactionDTOEncrypted[] transactionDtosEncrypteds = null;
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            //Cursor cursor = db.query(true, "transactions", comlumns, null, null, null, null, null, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            transactionDtosEncrypteds = new TransactionDTOEncrypted[count];
            int i = 0;
            int temp = cursor.getPosition();

            do {
                /*transactionId = Integer.parseInt((cursor.getString(0)));
                senderID = Integer.parseInt((cursor.getString(1)));*/
                transactionId = cursor.getString(0);
                senderID = cursor.getString(1);
                senderName = cursor.getString(2);
                receiverId = cursor.getString(3);
                receiverName = cursor.getString(4);
                amount = cursor.getString(5);
                time = cursor.getString(6);
                balance = cursor.getString(7);
                syncFlag = cursor.getString(8);
                transactionDtosEncrypteds[i] = new TransactionDTOEncrypted(transactionId, senderID, senderName, receiverId, receiverName, amount, time, balance, syncFlag);
                //transactionDtos[i] = new TransactionDto(transactionId, senderID, null,0,null, 0, null,0,false);
                //Log.e("DBTEST",""+transactionDtos[i].getTransactionId());
                i++;
            } while (cursor.moveToNext());
            cursor.close();
            return transactionDtosEncrypteds;
        } catch (Exception e) {
            return transactionDtosEncrypteds;
        }
    }

    public TransactionDto[] getNotSyncedTransaction() {
        TransactionDto[] transactionDtos = null;
        TransactionDto[] transactionDtosToPass = null;
        transactionDtos = getAllTransaction();
        if (transactionDtos != null) {
            transactionDtosToPass = new TransactionDto[transactionDtos.length];
            int j = 0;
            for (int i = 0; i < transactionDtos.length; i++) {
                if (!transactionDtos[i].isSyncFlag()) {
                    transactionDtosToPass[j] = transactionDtos[i];
                    j++;
                }
            }
        }
        return transactionDtosToPass;
    }

    public double getBalance()
    {
        String selectQuery = "SELECT balance FROM transactions ORDER BY t_id DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        double bal=Double.parseDouble(EncryptionClass.symmetricDecrypt(cursor.getString(0)));
        TransactionDto[] transactionDtos=getAllTransaction();
        if(count>0)
           return Double.parseDouble(EncryptionClass.symmetricDecrypt(cursor.getString(0)));
        else
            return 0;
    }
    public void markSynced(TransactionDto transactionDto) {
        //String selectQuery = "UPDATE transactions SET sync_flag=\""+transactionDto.isSyncFlag()+"\" WHERE t_id=\""+transactionDto.getTransactionId()+"\";";
        ContentValues values = new ContentValues();
        values.put("sync_flag", EncryptionClass.symmetricEncrypt(String.valueOf(transactionDto.isSyncFlag())).trim());
        db.update("transactions", values, "WHERE t_id=\'" + transactionDto.getTransactionId() + "\'", null);

    }

}
