package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.pojo.DrawRes;
import com.mezzofy.mzcoupon.apputills.CommonUtils;


import java.util.ArrayList;

/**
 * Created by udhayinforios on 24/2/16.
 */
public class DragListAdapter extends BaseAdapter {

    Context MyContext;
    ArrayList<DrawRes> albumList;
    int pos = 0;

    LayoutInflater li;

    public DragListAdapter(Context _MyContext, ArrayList<DrawRes> _albumList) {
        MyContext = _MyContext;
        this.albumList = _albumList;
//        daodrag = new DragDao(MyContext);
        li = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View MyView = null;

        try {
            if (convertView == null) {
                MyView = li.inflate(R.layout.drag_list_adapter, null);
            } else {
                MyView = convertView;
            }
            ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
            TextView title = (TextView) MyView.findViewById(R.id.textView1);
            TextView name = (TextView) MyView.findViewById(R.id.title);
            TextView desc = (TextView) MyView.findViewById(R.id.desc);
            ImageView remov = (ImageView) MyView.findViewById(R.id.imageViewRemove);
            remov.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
//                        daodrag.deletepromocode(albumList.get(position).getPromoid());
                        albumList.remove(position);
                        notifyDataSetChanged();

    //                Intent intent = new Intent(MyContext, TabviewActivity.class);
    //                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //                intent.putExtra("tabName", "");
    //                intent.putExtra("currTab", 2);
    //                MyContext.startActivity(intent);

                        CommonUtils.Snackbar(v, MyContext.getString(R.string.Item_Deleted));
                    } catch (Exception e) {

                    }
                }
            });


            if (albumList.get(position).getImage() != null && !albumList.get(position).getImage().equals("")) {

                try {

                    Glide.with(MyContext)
                            .load(albumList.get(position).getImage())
                            .error(R.drawable.appicon_large)
                            .into(iv);
                    Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

    //                imgLoader.DisplayImage(albumList.get(position).getImage(), loader, iv);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                iv.setImageResource(R.drawable.no_image_icon);
            }

            title.setText(albumList.get(position).getTitle());
            name.setText(albumList.get(position).getName());
            desc.setText(albumList.get(position).getDesc());
            desc.setMaxLines(3);
            desc.setEllipsize(TextUtils.TruncateAt.END);

        } catch (Exception e) {
            e.printStackTrace();
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
