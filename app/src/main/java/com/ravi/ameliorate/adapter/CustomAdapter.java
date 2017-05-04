package com.ravi.ameliorate.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.ravi.ameliorate.R;
import com.ravi.ameliorate.activity.RecentActivity;
import com.ravi.ameliorate.model.Contact;

/**
 * Created by Ankit on 13-01-2017.
 */

public class CustomAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private RecentActivity activity;
    private ArrayList<Contact> data = new ArrayList<Contact>();
    private static LayoutInflater inflater=null;
    Contact tempValues=null;
    int i=0;
    Context context;
    SharedPreferences sp;
    String value,value2;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(RecentActivity a, ArrayList<Contact> d) {


        /********** Take passed values **********/
        activity = a;
        data=d;
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView word;
        public RadioButton pSize;
        public RadioButton pSauce;
        public RadioButton pizz;
        public RadioButton exclude1;
        public RadioButton exclude2;
        public RadioGroup radio_group;
        public RadioButton radio_groupClicked;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        context =this.activity;
        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.word = (TextView) vi.findViewById(R.id.name);

            //holder.exclude1=(RadioButton) vi.findViewById(R.id.exclusive1);
            //holder.exclude2=(RadioButton) vi.findViewById(R.id.exclusive2);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.word.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( Contact ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.word.setText( tempValues.getName());


            final View finalVi = vi;



        }
        return vi;
    }




}
