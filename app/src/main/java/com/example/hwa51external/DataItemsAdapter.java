package com.example.hwa51external;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class DataItemsAdapter extends BaseAdapter {
    private List<DataItems> dataItemsList;
    private LayoutInflater inflater;
    private DeleteListener deleteListener;

    public DataItemsAdapter(List<DataItems> dataItems, Context context, DeleteListener deleteListener) {
        this.dataItemsList = dataItems;
        this.inflater = LayoutInflater.from(context);
        this.deleteListener = deleteListener;
    }

    @Override
    public int getCount() {
        return dataItemsList != null ? dataItemsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataItemsList != null ? dataItemsList.get(position) : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View currentView;
        if (convertView != null) {
            currentView = convertView;
        } else {
            currentView = inflater.inflate(R.layout.item_list_view, parent, false);
        }

        final DataItems model = (DataItems) getItem(position);
        if (model != null) {
            TextView title = currentView.findViewById(R.id.title);
            TextView subTitle = currentView.findViewById(R.id.subtitle);
            ImageView imageView = currentView.findViewById(R.id.icon_view);
            Button buttonDelete = currentView.findViewById(R.id.btn_delete);

            imageView.setBackgroundResource(model.getImageID());
            title.setText(model.getTitle_view());
            subTitle.setText(model.getSubTitle_view());
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteListener.onDelete(position);
                    btnDel(position);
                }
            });
        }
        return currentView;
    }

    private void btnDel(int position) {
        dataItemsList.remove(position);
        notifyDataSetChanged();
    }
}
