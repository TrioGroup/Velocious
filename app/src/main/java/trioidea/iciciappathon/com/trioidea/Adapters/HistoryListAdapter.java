package trioidea.iciciappathon.com.trioidea.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 15/04/2017.
 */
public class HistoryListAdapter extends ArrayAdapter<TransactionDto> {

    TransactionDto[] transactionDtos;
    Activity context;
    public HistoryListAdapter(Activity context, int resource, TransactionDto[] objects) {
        super(context, resource, objects);
        this.context=context;
        this.transactionDtos=objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.history_list_item, null, true);
        TextView fromAccount=(TextView) listViewItem.findViewById(R.id.tv_from_account_number);
        TextView toAccount=(TextView) listViewItem.findViewById(R.id.tv_to_account_number);
        TextView timestamp=(TextView) listViewItem.findViewById(R.id.tv_timestamp);
        TextView amount=(TextView) listViewItem.findViewById(R.id.tv_amount);

        fromAccount.setText(String.valueOf(transactionDtos[position].getSenderID()));
        toAccount.setText(String.valueOf(transactionDtos[position].getReceiverId()));
        timestamp.setText(String.valueOf(transactionDtos[position].getTime()));
        amount.setText(String.valueOf(transactionDtos[position].getAmount()));
        return listViewItem;
    }
}
