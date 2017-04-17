package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.Adapters.HistoryListAdapter;
import trioidea.iciciappathon.com.trioidea.DTO.HistoryDateRangeDTO;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.FragmentControllers.FeatureOptionFragmentController;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 15/04/2017.
 */
public class HistoryFragment extends Fragment{
    TextView noTransaction;
    ListView historyList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_history_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        historyList=(ListView)getActivity().findViewById(R.id.history_list);
        noTransaction=(TextView) getActivity().findViewById(R.id.tv_no_transaction);
        DbHelper dbHelper=DbHelper.getInstance(getActivity());
        TransactionDto[] transactionDtos=dbHelper.getAllTransaction();
        if(transactionDtos!=null || transactionDtos.length>0)
        {
            HistoryListAdapter historyListAdapter=new HistoryListAdapter(getActivity(),R.id.history_list,transactionDtos);
            historyList.setAdapter(historyListAdapter);
            noTransaction.setVisibility(View.GONE);
            historyList.setVisibility(View.VISIBLE);
        }
        else
        {
            historyList.setVisibility(View.GONE);
            noTransaction.setVisibility(View.VISIBLE);
        }
    }
}
