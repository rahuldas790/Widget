package rahulkumardas.floatingwidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rahul Kumar Das on 02-03-2017.
 */

public class ShortcutsAdapter extends BaseAdapter {

    public ShortcutsAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    private Context context;
    private List<Item> list;


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shortcuts, viewGroup, false);
            holder = new Holder();
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            holder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.checkBox.setChecked(list.get(i).checked);
        holder.title.setText(list.get(i).text);
        final int pos = i;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(pos).checked == true) {
                    list.get(pos).checked = false;
                } else {
                    list.get(pos).checked = true;
                }
                Log.i("Rahul", "CHeckbox at " + pos + " : " + list.get(pos).checked);
            }
        });
        
        return view;
    }

    public class Holder {
        public CheckBox checkBox;
        public TextView title;
    }
}
