package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.PoOrder_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class Transcation_detailActivity extends Activity {
	
	RelativeLayout layout;
	final Context context = this;
	ListView list;
	List<PoDetailEntity> poDetailData=null;
	PoEntity poData;
	private PoOrder_Module transcationModule;
	private Customer_Module customerModule;

	CustomerEntity customerData=null;

	String PoId;

	SharedPreferences settings;
	String frm;

	  @SuppressLint("NewApi")
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
		  try{
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.transcation_detailmain);
			  transcationModule = new PoOrder_Module(context);
			  customerModule=new Customer_Module(context);

		    settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	        Bundle extras = getIntent().getExtras();
			  PoId = extras.getString("PoId");
			  frm = extras.getString("frm");

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			  customerData=customerModule.getUser();
			  poDetailData=transcationModule.getpodetaildata(PoId);
			  poData=transcationModule.getpodata(PoId);

			
        list=(ListView) findViewById(R.id.listView1);
        list.setSelector(R.drawable.listselector);
        list.setAdapter(new ImageBaseAdapter(this,poDetailData));
        
        ImageView receipt=(ImageView) findViewById(R.id.imageView1);
        receipt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, Receipt_MailActivity.class);
//			    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			    intent.putExtra("transId", transId);
//			    
//			    Transcation_detailActivity.this.finish();
//			    startActivity(intent);
//			    overridePendingTransition(0, 0);
                StringBuilder sb=new StringBuilder();
                double total;
				
				Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.appicon);
		        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

		        File file = new File(extStorageDirectory, "logo.png");
			    try {
			    	FileOutputStream outStream = new FileOutputStream(file);
			        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			        outStream.flush();
			        outStream.close();
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
				
//				sb.append("<html><table style=\"width:100%;bgcolor:#6A7C8B;\" ><tr><td height=\"150\" align=\"center\"><img src=\"https://s3-ap-southeast-1.amazonaws.com/com.mezzofy.ebs/logo/edenred_logo.png\" style=\"width:200px;\"/></td></tr></table><br/></html>");
//				sb.append("<html><body background=\"https://s3-ap-southeast-1.amazonaws.com/com.mezzofy.ebs/logo/edenred_logo.png\" /><br/></html>");

			    sb.append("<html><table style=\"width:100%\"><tr><td>Invoice 發票號碼 : "+ poData.getPoNo() +"</td><br/><td align=\"right\">"+ poData.getPoDate() +"</td></tr></table><br/></html>");
		 		sb.append("<html><hr><br/></html>");
		 		
		 		total=0.00;
		 	    for (int i =0; i<poDetailData.size(); i++) {
					if(settings.getString("decimal","N").equals("Y")) {
						sb.append("<html><table style=\"width:100%\"><tr><td>" + poDetailData.get(i).getCampaignName() + "<br/> x " + poDetailData.get(i).getCampaignQty() + "</td><td align=\"right\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + settings.getString("currency", "IDR") +  poDetailData.get(i).getCampaignTotal() + "</td></tr></table><br/><br/></html>");
					}else{
						sb.append("<html><table style=\"width:100%\"><tr><td>" +  poDetailData.get(i).getCampaignName() + "<br/> x " + poDetailData.get(i).getCampaignQty() + "</td><td align=\"right\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + settings.getString("currency", "IDR") +  poDetailData.get(i).getCampaignTotal() + "</td></tr></table><br/><br/></html>");

					}
		 	         total = total + Double.parseDouble(poDetailData.get(i).getCampaignTotal());
		 	    }
		 	    
		 	    sb.append("<html><hr><br/></html>");
				if(settings.getString("decimal","N").equals("Y")) {
					sb.append("<html><table style=\"width:100%\"><tr><td><b>Total 總金額</b></td><td align=\"right\"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + settings.getString("currency", "IDR") + String.format("%,.2f", total) + "</b></td></tr></table><br/></html>");
				}else{
					sb.append("<html><table style=\"width:100%\"><tr><td><b>Total 總金額</b></td><td align=\"right\"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + settings.getString("currency", "IDR") + String.format("%,.0f", total) + "</b></td></tr></table><br/></html>");
				}

		 	    sb.append("<br/><br/><html><div align=\"center\">2015 &copy; MZCoupon</div><br/></html>");
			    
			    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			     
		         emailIntent.setType("text/html");
		         emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {customerData.getCustomerEmail()});
		         emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
		         emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invoice Number 發票號碼: "+poData.getPoNo());
		         emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(sb.toString()).toString());
		         
		         emailIntent.setType("image/png");
			     emailIntent.setType("application/octet-stream");
		         emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

		         try {
		            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//		            finish();
		            //Log.i("Finished sending email...", "");
		         } catch (android.content.ActivityNotFoundException ex) {
					 CommonUtils.Snackbar(list, "There is no email client installed.");
				 }
			}
		});
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	    }

	  private ImageGetter imgGetter = new ImageGetter() {

		    public Drawable getDrawable(String source) {
		        Drawable drawable = null;
		            try {
		                drawable = getResources().getDrawable(R.drawable.appicon);
		                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		                    drawable.getIntrinsicHeight());
		            } catch (Exception e) {
		                e.printStackTrace();
		                Log.d("Exception thrown",e.getMessage());
		            } 
		            return drawable;
		    }
		};

	 public class ImageBaseAdapter extends BaseAdapter {
			Context MyContext;
		    List<PoDetailEntity> albumList;

			int pos = 0;
		    double totprod=0.00;
			Boolean favclk = false;

			public ImageBaseAdapter(Context _MyContext,List<PoDetailEntity> _albumList) {
				albumList=null;
				notifyDataSetChanged();
				MyContext = _MyContext;
				albumList=_albumList;
			}

			@Override
			public int getCount() {
				return albumList.size();
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View MyView;
				pos = position;
				 if(convertView==null)
			        {
			            LayoutInflater li = getLayoutInflater();
			            MyView = li.inflate(R.layout.transcation, null);
			        }else{
			        	MyView = convertView;
			        }
					
					TextView trantext=(TextView) MyView.findViewById(R.id.textView1);
					TextView datetext=(TextView) MyView.findViewById(R.id.textView3);
					TextView selltext=(TextView) MyView.findViewById(R.id.textView2);
					TextView tranNotext=(TextView) MyView.findViewById(R.id.textView4);
					TextView payresp=(TextView) MyView.findViewById(R.id.TextView01);


					
					 ImageView remov=(ImageView) MyView.findViewById(R.id.imageViewRemove);
					 remov.setVisibility(View.GONE);
					 datetext.setVisibility(View.GONE);

//						tranNotext.setText(albumList.get(position).getMerchantName());
						payresp.setText(albumList.get(position).getCampaignName());

						payresp.setMaxLines(2);
						payresp.setEllipsize(TruncateAt.END);
						if(settings.getString("decimal","N").equals("Y")) {
							selltext.setText(" " + settings.getString("currency", "IDR") + albumList.get(position).getCampaignTotal() + " ");
						}else{
							selltext.setText(" " + settings.getString("currency", "IDR") + albumList.get(position).getCampaignTotal()+ " ");

						}

						trantext.setText("X"+String.valueOf(albumList.get(position).getCampaignQty()));
					    
					    totprod = totprod + Double.parseDouble(albumList.get(position).getCampaignTotal());
					    
					    if(position==albumList.size()-1){
					    	RelativeLayout layout=(RelativeLayout) MyView.findViewById(R.id.custom_rely);
					    	layout.setVisibility(View.VISIBLE);
					    	TextView trantext1=(TextView) MyView.findViewById(R.id.textView5);
							TextView datetext1=(TextView) MyView.findViewById(R.id.textView7);
							TextView selltext1=(TextView) MyView.findViewById(R.id.textView6);
							TextView tranNotext1=(TextView) MyView.findViewById(R.id.textView8);
							TextView transucess1=(TextView) MyView.findViewById(R.id.textView08);
							ImageView remov1=(ImageView) MyView.findViewById(R.id.imageViewRemove);
							 remov1.setVisibility(View.GONE);
							 
							if(poData.getPayResponse().equals("0"))
							transucess1.setText(getString(R.string.Success));
						    else
						    transucess1.setText(getString(R.string.Failure));

							if(poData.getPoId().toString()!=null) {
								long timestamp = Long.parseLong(poData.getPoId().toString());
								Log.d("timestamp po", timestamp + "  - " + poData.getPoId().toString());

								SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
								sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
								sdf.setTimeZone(TimeZone.getDefault());
								String formattedDate = sdf.format(timestamp);
								tranNotext1.setText(formattedDate);
							}
							else
								tranNotext1.setText("");


							if(settings.getString("decimal","N").equals("Y")) {
								selltext1.setText(" " + settings.getString("currency", "IDR") + totprod + " ");
							}else{
								selltext1.setText(" " + settings.getString("currency", "IDR") + totprod + " ");

							}
						    datetext1.setText(poData.getPoDate());
						    trantext1.setText(R.string.total);
					    }
					   
				        
				return MyView;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return albumList.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
		}
	  
	  
	 @Override
	 public void onBackPressed() {
		 super.onBackPressed();
		 Intent intent = new Intent(getApplicationContext(), PaymentTranscationActivity.class);
	       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		   intent.putExtra("frm", frm);
		   startActivity(intent);
		   overridePendingTransition(0, 0);
		   Transcation_detailActivity.this.finish();
	 } 
	 
}