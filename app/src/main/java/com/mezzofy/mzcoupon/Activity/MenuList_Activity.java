package com.mezzofy.mzcoupon.Activity;

import java.util.ArrayList;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.EntryItem;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.apputills.SectionItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class MenuList_Activity extends Activity {

    ListView list;
    final Context context = this;


    ArrayList<Item> items = new ArrayList<Item>();

    int specialId, prodid;
    String cupid, exp, expday;
    String specialImg, specialname, form;

    boolean flag = false;
    String prodfav;
    String cartid;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_item_details);


        Bundle extras = getIntent().getExtras();
        prodid = extras.getInt("prodid");
        cupid = extras.getString("cupid");
        cartid = extras.getString("cartid");
        exp = extras.getString("exp");
        expday = extras.getString("expday");
        specialId = extras.getInt("specialId");
        specialImg = extras.getString("specialimg");
        specialname = extras.getString("specialname");
        form = extras.getString("form");
        prodfav = extras.getString("prodfav");

        TextView specialtext = (TextView) findViewById(R.id.txtTitle);
        specialtext.setText(R.string.Today_Menu);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        List<GroupRes> groupResList = productDao.getProductGroupList(prodid);
//        for (GroupRes groupRes : groupResList) {
//            items.add(new SectionItem(groupRes.getGroupName()));
//            itemResList = itemDao.getItemResList(groupRes.getGroupId());
//            for (ItemRes itemRes : itemResList) {
//                items.add(new EntryItem(itemRes.getItemName(), groupRes.getGroupId(), itemRes.getItemId()));
//            }
//        }

        ItemEntryAdapter adapter = new ItemEntryAdapter(this, items);

        list = (ListView) findViewById(R.id.listView1);
        list.setSelector(R.drawable.listselector);
        list.setAdapter(adapter);
            //ListHelper.getListViewSize(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ItemEntryAdapter extends ArrayAdapter<Item> {

        private ArrayList<Item> items;
        private LayoutInflater vi;

        public ItemEntryAdapter(Context context, ArrayList<Item> items) {
            super(context, 0, items);
            this.items = items;
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            final Item i = items.get(position);
            if (i != null) {
                if (i.isSection()) {
                    SectionItem si = (SectionItem) i;
                    v = vi.inflate(R.layout.section_groupname, null);

                    v.setOnClickListener(null);
                    v.setOnLongClickListener(null);
                    v.setLongClickable(false);

                    final TextView sectionView = (TextView) v.findViewById(R.id.list_section_entry_title);
                    sectionView.setText(si.getTitle());
//						sectionView.setTextSize(14);
//						sectionView.setPadding(10, 10, 0, 0);
//						sectionView.setBackgroundColor(Color.parseColor("#DEDEDE"));
//						sectionView.setTextColor(Color.DKGRAY);

                } else {
                    EntryItem ei = (EntryItem) i;
                    v = vi.inflate(R.layout.menu_view, null);
                    final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
                    title.setText(ei.title);


//						List<OrderModifierRes> orderModifierList = orderDao.getOrderModifierlist();
//						for(OrderModifierRes modifierRes :orderModifierList){
//							if(modifierRes.getItemId()==ei.itemid){
//								  ItemOrderRes itemRes = new ItemOrderRes();
//						       	  itemRes.setItemId(ei.itemid);
//						       	  itemRes.setCouponId(cupid);
//						       	  itemRes.setGroupId(ei.groupid);
//						       	  orderDao.addOrderitem(itemRes);
//							}
//						}
                }
            }
            return v;
        }
    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
        Intent intent = null;
        if (form.equals("spec")) {
//            intent = new Intent(context, SpecialDetail_ProdActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("prodid", prodid);
//            intent.putExtra("specialId", specialId);
//            intent.putExtra("specialimg", specialImg);
//            intent.putExtra("specialname", specialname);
        }

        if (form.equals("cart")) {
            intent = new Intent(context, Cart_ProdDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", prodid);
            intent.putExtra("cartid", cartid);
        }

        if (form.equals("prod")) {
            intent = new Intent(context, Campaigndetail_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", prodid);
            intent.putExtra("prodfav", prodfav);
        }

        if (form.equals("coupon")) {
            intent = new Intent(context, CouponDetail_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("prodid", prodid);
            intent.putExtra("exp", exp);
            intent.putExtra("cupid", cupid);
            intent.putExtra("expday", expday);
        }

        startActivity(intent);
        overridePendingTransition(0, 0);
        MenuList_Activity.this.finish();
    }
}