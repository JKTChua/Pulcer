package com.me.pulcer.adapter;

import java.io.File;
import java.util.ArrayList;

import com.example.pulcer.R;
import com.me.pulcer.entity.UlcerEnt;
import com.me.pulcer.entity.UlcerGroup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UlcerGalleryAdapter extends BaseAdapter 
{
    private Activity activity;
    private static LayoutInflater inflater = null;
    private static String[] imageLocations;
    private static String[] dates;
    ArrayList<UlcerEnt> list;
    public int selectedId=-1;
	Context context;

    public UlcerGalleryAdapter(Activity a) 
    {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UlcerGalleryAdapter(Activity a, Context c, ArrayList<UlcerEnt> ulcerList)
	{
    	activity = a;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = ulcerList;
		this.context = c;
	}
    
//    public UlcerGalleryAdapter(Context context,ArrayList<UlcerEnt> ulcerList)
//	{
//		this.list = ulcerList;
//		this.context = context;
//		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
    
    public static void loadUlcer(int ulcer_id)
    {
    	
    }
    
//    public static void setName(String[] names, int[] datas)
//    {
//    	name = names;//new String[]{"Image2", "Image1", "Image3", "Image4", "Image5", "Image6"};
//    	data = datas;
//    }

    public int getCount() 
    {
    	return data.length;
//        return list.size();
    }

    public Object getItem(int position) 
    {
        return position;
    }

    public long getItemId(int position) 
    {
        return position;
    }

    public static class ViewHolder
    {
        public TextView text;
        public ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View vi = convertView;
        ViewHolder holder;
        if(convertView == null)
        {
        	
            vi = inflater.inflate(R.layout.ulcer_image_gallery_items, null);
            holder = new ViewHolder();
            holder.text = (TextView)vi.findViewById(R.id.textView1);
            holder.image = (ImageView)vi.findViewById(R.id.image);
            vi.setTag(holder);
        }
        else
            holder = (ViewHolder)vi.getTag();
//        holder.text.setText(list.get(position).date);
//        holder.text.setText("Testing");
        String location = null;
        if (position < list.size())
        {
        	holder.text.setText(list.get(position).date);
        	//location = list.get(position).image;
        	location = list.get(position).ulcerId + "-" + list.get(position).groupId + ".jpg";
        }
        else
        	holder.text.setText(name[position]);
        
        
        if (location != null)
        {
        	Bitmap bitmap = null;
        	BitmapFactory.Options options = new BitmapFactory.Options();
        	options.inSampleSize = 8;
	        try
	        {
	        	File file = new File(context.getExternalFilesDir(null), location);
		        if (file.exists())
		        {
		        	try
		        	{
		        		bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		        	}
		        	catch (OutOfMemoryError e)
		        	{
		        		System.gc();
		        		
		        		try
		        		{
		        			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		        		}
		        		catch(Exception e1)
		        		{
		        			e1.printStackTrace();
		        		}
		        	}
		        	holder.image.setImageBitmap(bitmap);
		        }
		        else
		        	holder.image.setImageResource(R.drawable.down);
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        }
        }
        else
        {
        	final int stub_id=data[position];
            holder.image.setImageResource(stub_id);
        }
        return vi;
    }

    private int[] data = {
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher
    };
    private String[] name = {
            "Image1", "Image2",
            "Image3", "Image4",
            "Image5", "Image6"
    };
}

