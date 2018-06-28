package com.mezzofy.mzcoupon.Activity;

import java.util.ArrayList;

import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.EntryItem;
import com.mezzofy.mzcoupon.apputills.Item;
import com.mezzofy.mzcoupon.apputills.SectionItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mezzofy.mzcoupon.module.CouponOrder_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;

public class OrderView_Activity extends Activity {

    ListView list;
    final Context context = this;

    private Merchantsite_Module merchantsiteModule;
    private CouponOrder_Module couponOrderModule;
    private Coupon_Module couponmodule;

    ArrayList<Item> items = new ArrayList<Item>();

    String prodid;
    String exp, expday, cupid;
    EditText Contactfield, Remarkfield;
    OrderEntity orderRes;
    ProgressDialog progressDialog;

    SharedPreferences settings;

    String staffId;

    String mobile;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            setContentView(R.layout.preview);

            couponOrderModule = new CouponOrder_Module(context);
            couponmodule = new Coupon_Module(context);
            merchantsiteModule = new Merchantsite_Module(context);

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mobile = settings.getString("mobile", "");

            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prodid = settings.getString("campaignId", null);
            cupid = settings.getString("couponId", null);
            exp = settings.getString("exp", null);
            staffId = settings.getString("staff_id", null);


            Contactfield = (EditText) findViewById(R.id.Contactfield);
            Remarkfield = (EditText) findViewById(R.id.Remarkfield);

            orderRes = couponOrderModule.getCouponOrder(cupid);

            CouponEntity couponData = couponmodule.getCoupon(cupid);

            SiteEntity mechantSite = null;
            if (orderRes!=null && orderRes.getSiteId() != null) {
                mechantSite = merchantsiteModule.getMerchantSite(orderRes.getSiteId());
            }

            TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
            txtTitle.setText(R.string.Order_View);

            TextView textView1 = (TextView) findViewById(R.id.textView1);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            TextView textView3 = (TextView) findViewById(R.id.textView3);
//            TextView textView4 = (TextView) findViewById(R.id.textView4);
            TextView textView5 = (TextView) findViewById(R.id.textView5);
            TextView textView6 = (TextView) findViewById(R.id.textView6);

//            LinearLayout timelayt = (LinearLayout) findViewById(R.id.timelinear);
            LinearLayout itemlayt = (LinearLayout) findViewById(R.id.itemlinear);
            LinearLayout prodlayt = (LinearLayout) findViewById(R.id.prodlinear);

//        EditText Contactfield = (EditText) findViewById(R.id.Contactfield);
//        EditText Remarkfield = (EditText) findViewById(R.id.Remarkfield);

            textView1.setText(couponData.getCouponName());

            if (mechantSite != null) {
                if(orderRes!=null) {
                    if (orderRes.getOrderType().equals("P") || orderRes.getOrderType().equals("B") || orderRes.getOrderType().equals("O")) {
                        textView2.setText(mechantSite.getSiteName());
                    } else {
                        textView2.setText(orderRes.getCustomerAddress());
                    }
                }
            } else {
                if(orderRes!=null) {
                    if (orderRes.getOrderType().equals("P") || orderRes.getOrderType().equals("B") || orderRes.getOrderType().equals("O")) {
                        textView2.setText("");
                    } else {
                        textView2.setText(orderRes.getCustomerAddress());
                    }
                }
            }

            if (orderRes.getOrderType().equals("P")) {
                textView3.setText(R.string.Pickup);
            } else if (orderRes.getOrderType().equals("D")) {
                textView3.setText(R.string.Delivery);
            } else if (orderRes.getOrderType().equals("O")) {
                textView3.setText(R.string.In_store_Redeem);
            } else if (orderRes.getOrderType().equals("B")) {
                textView3.setText(R.string.Booking);
            } else {
                textView3.setText(R.string.OutCall);
            }

            textView5.setText(orderRes.getOrderDate());

            if (orderRes.getStartendTime() != null)
                textView6.setText(orderRes.getStartendTime());


//	        StringBuilder sb = new StringBuilder();
//	        if(itemOrderRes.size()!=0){
//	        for(ItemOrderRes orderRes:itemOrderRes){
//	        ItemRes itemRes=itemDao.getItemRes(orderRes.getItemId());
//	        sb.append("Item Name:        "+ itemRes.getItemName() + "\n" );
//	        List<OrderModifierRes> modifierReslist =tranOrderDao.getOrderModifierlist(cupid, orderRes.getItemId());
//	        if(modifierReslist.size()!=0){
////	        sb.append("Modifier :       ");	
//	        for(OrderModifierRes modifierRes:modifierReslist){
//	        ModifierRes modiRes = itemDao.getModifierRes(modifierRes.getModifierId());
//	        sb.append("Modifier :             "+modiRes.getModifierName()+ "\n");
//	        }
//	        }
//	        }
//	        textView7.setText(sb.toString());
//	        }else{
//	        itemlayt.setVisibility(View.GONE);
//	        }

//            list = (ListView) findViewById(R.id.listView1);
//            if (itemOrderRes.size() != 0) {
//                for (ItemOrderRes orderRes : itemOrderRes) {
//                    ItemOrderRes itemRes = tranOrderDao.getOrderitem(orderRes.getCouponId(), orderRes.getItemId());
////			GroupRes grpres= productDao.getProductGroup(orderRes.getGroupId());
//                    items.add(new SectionItem(itemRes.getGroupName()));
//                    items.add(new EntryItem(itemRes.getItemName(), itemRes.getGroupId(), itemRes.getItemId()));
//                    List<OrderModifierRes> modifierReslist = tranOrderDao.getOrderModifierlist(cupid, orderRes.getItemId());
//                    if (modifierReslist.size() != 0) {
//                        for (OrderModifierRes modifierRes : modifierReslist) {
//                            OrderModifierRes modiRes = tranOrderDao.getOrderModifier(orderRes.getCouponId(), modifierRes.getModifierId());
//                            items.add(new EntryItem("--" + modiRes.getModifierName() + "\n", itemRes.getGroupId(), itemRes.getItemId()));
//                        }
//                    }
//                }
//                ItemEntryAdapter adapter = new ItemEntryAdapter(this, items);
//                list.setSelector(R.drawable.listselector);
//                list.setAdapter(adapter);
//                ListHelper.getListViewSize(list);
//            }

//        Contactfield.setText(org.apache.commons.lang.StringEscapeUtils.unescapeJava(orderRes.getContactNo()));
            Contactfield.setText(mobile);
            Remarkfield.setText(org.apache.commons.lang.StringEscapeUtils.unescapeJava(orderRes.getOrderRemark()));

            Contactfield.setEnabled(false);
            Remarkfield.setEnabled(false);

            TextView purchtext = (TextView) findViewById(R.id.purch);
            purchtext.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();


        int qty=couponmodule.ReleaseCouponStockByCampaignId(prodid,cupid);
        if(qty==0)
        {
            Toast.makeText(getApplicationContext(),"Error Release Coupon Stock", Toast.LENGTH_SHORT)
                    .show();
        }
        Intent intent;
        intent = new Intent(context, CouponDetail_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("prodid", prodid);
        intent.putExtra("cupid", cupid);
        intent.putExtra("exp", exp);
        intent.putExtra("expday", expday);

        startActivity(intent);
        overridePendingTransition(0, 0);
        OrderView_Activity.this.finish();
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
        public View getView(int position, View convertView, ViewGroup parent) {
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
//						sectionView.setPadding(10, 5, 0, 0);
//						sectionView.setBackgroundColor(Color.parseColor("#DEDEDE"));
//						sectionView.setTextColor(Color.DKGRAY);
//						sectionView.setSingleLine(true);

                } else {
                    EntryItem ei = (EntryItem) i;
                    v = vi.inflate(R.layout.item_entrydetail, null);
                    final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
                    title.setText(ei.title);
                    //title.setSingleLine(true);

                    RelativeLayout layoutbg = (RelativeLayout) v.findViewById(R.id.bg_item_entry_drawable);
                    ImageView tickicon = (ImageView) v.findViewById(R.id.list_item_entry_drawable);
                    ImageView arrowicon = (ImageView) v.findViewById(R.id.next_entry_drawable);

//						List<OrderModifierRes> orderModifierList = tranOrderDao.getOrderModifierlist();
//						for(OrderModifierRes modifierRes :orderModifierList){
//							if(modifierRes.getItemId()==ei.itemid){
//								  ItemOrderRes itemRes = new ItemOrderRes();
//						       	  itemRes.setItemId(ei.itemid);
//						       	  itemRes.setCouponId(cupid);
//						       	  itemRes.setGroupId(ei.groupid);
//						       	  orderDao.addOrderitem(itemRes);
//							}
//						}

                    tickicon.setVisibility(View.GONE);
                    layoutbg.setVisibility(View.GONE);
                    arrowicon.setVisibility(View.GONE);
                }
            }
            return v;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
}
