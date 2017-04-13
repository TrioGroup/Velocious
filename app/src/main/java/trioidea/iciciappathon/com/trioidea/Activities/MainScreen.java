package trioidea.iciciappathon.com.trioidea.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.Fragments.FeatureOptionFragment;
import trioidea.iciciappathon.com.trioidea.R;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        /*int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));*/
        //abTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,null)); //with theme);
        /*Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);*/
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.app_name) + "</font>")));
        DbHelper dbHelper=DbHelper.getInstance(this);
        startFeaturesOptionFragment();
    }

    public void startFeaturesOptionFragment()
    {
        replaceFragment(new FeatureOptionFragment());
    }

    public void replaceFragment(android.app.Fragment fragment)
    {
        android.app.FragmentManager fragmentManager= getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
    public void addFragment(android.app.Fragment fragment)
    {
        android.app.FragmentManager fragmentManager= getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }
    public void startActivityTransferOffline()
    {
        Intent intent=new Intent(this,TransferActivity.class);
        startActivity(intent);
    }
}
