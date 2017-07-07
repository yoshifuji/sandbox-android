package com.example.sample.mymemoapp;

import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

/**
 * Created by yoshitaka.fujisawa on 2017/07/07.
 */

public class MemoLoadFragment extends ListFragment {

    public interface MemoLoadFragmentListener {
        void onMemoSelected(@Nullable Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof MemoLoadFragmentListener)) {
            throw new RuntimeException(context.getClass().getSimpleName() + " does not implement MemoLoadFragmentListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.memo_list_create, null);
        getListView().addHeaderView(header);

        Cursor cursor = MemoRepository.query(getActivity());

        MemoAdapter adapter = new MemoAdapter(getActivity(), cursor); //TODO: Check here is correct
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            ((MemoLoadFragmentListener)getActivity()).onMemoSelected(null);
        } else {
            Uri selectedItem = ContentUris.withAppendedId(MemoProvider.CONTENT_URI, id);
            ((MemoLoadFragmentListener)getActivity()).onMemoSelected(selectedItem);
        }
    }

}
