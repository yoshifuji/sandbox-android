package com.example.sample.myrssreader;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sample.myrssreader.adapter.LinkAdapter;
import com.example.sample.myrssreader.data.Link;
import com.example.sample.myrssreader.loader.LinkListLoader;

import java.util.List;

/**
 * Created by yoshitaka.fujisawa on 2017/07/12.
 */

public class LinkListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Link>>, LinkAdapter.OnItemClickListener {

    private static final int LOADER_LINKS = 1;

    public interface LinkListFragmentListener {
        void onLinkClicked(@NonNull Link link);
    }

    private LinkAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof LinkListFragmentListener)) {
            throw new RuntimeException(context.getClass().getSimpleName() + " does not implement LinkListFragmentListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_LINKS, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(LOADER_LINKS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_links, container, false);
        Context context = inflater.getContext();

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.LinkList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        mAdapter = new LinkAdapter(context);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public Loader<List<Link>> onCreateLoader(int id, Bundle args) {

        if (id == LOADER_LINKS) {
            LinkListLoader loader = new LinkListLoader(getActivity());
            loader.forceLoad();
            return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Link>> loader, List<Link> data) {
        int id = loader.getId();

        if (id == LOADER_LINKS && data != null && data.size() > 0) {
            mAdapter.addItems(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Link>> loader) {

    }

    @Override
    public void onItemClick(Link link) {
        LinkListFragmentListener listener = (LinkListFragmentListener) getActivity();
        if (listener != null) {
            listener.onLinkClicked(link);
        }
    }

    public void removeLinks(long siteId) {
        mAdapter.removeItem(siteId);
    }

    public void reload() {
        mAdapter.clearItems();
        Loader loader = getLoaderManager().getLoader(LOADER_LINKS);

        if (loader != null)
            loader.forceLoad();
        else
            getLoaderManager().restartLoader(LOADER_LINKS, null, this);

    }

}
