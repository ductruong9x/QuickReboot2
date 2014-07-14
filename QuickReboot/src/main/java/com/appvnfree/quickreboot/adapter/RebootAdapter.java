package com.appvnfree.quickreboot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appvnfree.quickreboot.R;
import com.appvnfree.quickreboot.model.Reboot;

import java.util.ArrayList;

/**
 * Created by truongtvd on 7/14/14.
 */
public class RebootAdapter extends ArrayAdapter<Reboot> {

    private Context context;
    private LayoutInflater inflater;

    public RebootAdapter(Context context, int resource, ArrayList<Reboot> list) {
        super(context, resource, list);
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_layout,null);
            viewHolder.tvTitle=(TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.imgAvatar=(ImageView)convertView.findViewById(R.id.imgAvatar);
            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        Reboot reboot=getItem(position);

        viewHolder.tvTitle.setText(reboot.getTitle());
        viewHolder.imgAvatar.setImageResource(reboot.getImage());

        return convertView;
    }

    private class ViewHolder{
        TextView tvTitle;
        ImageView imgAvatar;
    }
}
