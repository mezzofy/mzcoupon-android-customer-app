package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.EntryItem;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.apputills.SectionItem;
import com.mezzofy.mzcoupon.apputills.UsefulContactAdapter;

/**
 * Created by LENOVO on 26/06/2015.
 */
public class Useful_ContactActivity extends Activity implements AdapterView.OnItemClickListener {

    String[] CONTENT;
    ListView listview = null;
    ArrayList<Item> items = new ArrayList<Item>();
    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usefulcontact_activity);

//        CONTENT = getApplicationContext().getResources().getStringArray(R.array.UsefulContact);

        listview = (ListView) findViewById(R.id.listView1);

        items.add(new SectionItem(CONTENT[0]));
        items.add(new EntryItem(CONTENT[1], 0, 0));
        items.add(new EntryItem(CONTENT[2], 0, 0));
        items.add(new EntryItem(CONTENT[3], 0, 0));

        items.add(new SectionItem(CONTENT[4]));
        items.add(new EntryItem(CONTENT[5], 0, 0));
        items.add(new EntryItem(CONTENT[6], 0, 0));
        items.add(new EntryItem(CONTENT[7], 0, 0));
        items.add(new EntryItem(CONTENT[8], 0, 0));
        items.add(new EntryItem(CONTENT[9], 0, 0));

        items.add(new SectionItem(CONTENT[10]));
        items.add(new EntryItem(CONTENT[11], 0, 0));
        items.add(new EntryItem(CONTENT[12], 0, 0));
        items.add(new EntryItem(CONTENT[13], 0, 0));
        items.add(new EntryItem(CONTENT[14], 0, 0));
        items.add(new EntryItem(CONTENT[15], 0, 0));
        items.add(new EntryItem(CONTENT[16], 0, 0));
        items.add(new EntryItem(CONTENT[17], 0, 0));
        items.add(new EntryItem(CONTENT[18], 0, 0));

        items.add(new SectionItem(CONTENT[19]));
        items.add(new EntryItem(CONTENT[20], 0, 0));

        UsefulContactAdapter adapter = new UsefulContactAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        //EntryItem item = (EntryItem)items.get(position);
        Intent intent;

        switch (position) {
            case 1:
                alertMethod("+62 361 222 387");
                break;
            case 2:
                alertMethod("+62 361 100");
                break;
            case 3:
                alertMethod("+62 361 147");
                break;
            case 5:
                alertMethod("+62 361 110");
                break;
            case 6:
                alertMethod("+62 361 118");
                break;
            case 7:
                alertMethod("+62 361 113");
                break;
            case 8:
                alertMethod("+62 361 111");
                break;
            case 9:
                alertMethod("+62 361 751 000");
                break;
            case 11:
                alertMethod("+62 361 761 263");
                break;
            case 12:
                alertMethod("+62 361 3000 911");
                break;
            case 13:
                alertMethod("+62 361 247 499");
                break;
            case 14:
                alertMethod("+62 361 710 505");
                break;
            case 15:
                alertMethod("+62 361 223 036");
                break;
            case 16:
                alertMethod("+62 361 236 225");
                break;
            case 17:
                alertMethod("+62 361 227 911");
                break;
            case 18:
                alertMethod("+62 361 235 041");
                break;
            case 20:
                alertMethod("+62 361 701 111");
                break;
        }
    }


    public void alertMethod(final String dailno) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(getString(R.string.Call) + "\n" + dailno);
        builder1.setCancelable(true);
        builder1.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + dailno));
                startActivity(callIntent);
                overridePendingTransition(0, 0);
            }
        });
        builder1.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabName", "");
        intent.putExtra("currTab", 4);
        startActivity(intent);
        overridePendingTransition(0, 0);
        Useful_ContactActivity.this.finish();
    }
}
