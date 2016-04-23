package com.cnx.test.downlisttest;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域筛选条件
 * Created by Administrator on 2016/4/6 0006.
 */
public abstract class DownDoubleListPopupWindow<T> extends PopupWindow {

    private Activity activity;
    private LayoutInflater inflater;

    private List<Integer> selectedList;
    private List<T> filters;
    private int selectedPosition = -1;
    private int selectedPositionSecond = -1;
    private XAdapter.OnItemClickListener itemClickListener = null;
    private LinearLayout listLayout;
    private SparseArray<View> bottomViewList;
    private View bottomLayout;
    private View rootView;
    private RecyclerView rvFirstFilter, rvFilter;
    private XAdapter<T> firstAdapter, adapter;

    public DownDoubleListPopupWindow(Activity activity, List<T> filters, XAdapter.OnItemClickListener itemClickListener, @LayoutRes int bottomLayoutId) {
        super(activity);
        this.activity = activity;
        this.itemClickListener = itemClickListener;
        this.filters = filters;
        initViews();
        bottomViewList = new SparseArray<>();
        if (bottomLayoutId > 0) {
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.llFilterBottom);
            bottomLayout = inflater.inflate(bottomLayoutId, null);
            if (bottomLayout != null) {
                linearLayout.addView(bottomLayout);
            }
        }
        selectedList = new ArrayList<>();
        for (int i = 0; i < filters.size(); i++) {
            selectedList.add( 0);
        }
    }

    private void initViews() {
        inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.popup_window_filter, null);

        setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        update();

        listLayout = (LinearLayout) rootView.findViewById(R.id.llFilterList);
         rvFirstFilter = (RecyclerView) rootView.findViewById(R.id.rvFirstFilter);
         rvFilter = (RecyclerView) rootView.findViewById(R.id.rvFilter);
        rvFilter.setLayoutManager(new LinearLayoutManager(activity));
        rvFirstFilter.setLayoutManager(new LinearLayoutManager(activity));
        rvFirstFilter.setVisibility(View.VISIBLE);

        /**
         * 第一个列表
         */
        firstAdapter = new XAdapter<T>(activity, filters, R.layout.popup_item_filter) {

            @Override
            protected void handleItemViewClick(CustomHolder holder, T item) {
                int oldPos = selectedPosition;
                int pos = holder.getAdapterPosition();
                selectedPosition = pos;
                notifyItemChanged(pos);
                if (oldPos >= 0) {
                    notifyItemChanged(oldPos);
                }
                /**
                 * 更新子列表
                 */
                List list = getSecondAdapterList(item);
                if (list.size()>0) {
                    selectedPositionSecond = 0;
                }
                adapter.setDataList(list);
            }

            @Override
            public void creatingHolder(CustomHolder holder, List<T> dataList, int adapterPos, int viewType) {
                holder.getView(R.id.select_arrow);
                holder.getView(R.id.filter_name);

            }

            @Override
            public void bindingHolder(CustomHolder holder, List<T> dataList, int pos) {
                TextView tvName =  holder.getView(R.id.filter_name);
                ImageView ivArrow =  holder.getView(R.id.select_arrow);
                tvName.setText(getFirstListItemText(dataList.get(pos)));
                if (pos == selectedPosition) {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_blue));
                    holder.getRootView().setBackgroundResource(R.color.white);
                } else {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_color));
                    holder.getRootView().setBackgroundResource(android.R.color.transparent);
                }
                ivArrow.setVisibility(View.GONE);
            }
        };
        List<T> list = new ArrayList<>();
        if (filters.size()>0) {
            selectedPosition = 0;
           list = getSecondAdapterList(filters.get(0));
            if (list.size()>0) {
                selectedPositionSecond = 0;
            }
        }
        adapter = new XAdapter<T>(activity, list, R.layout.popup_item_filter) {

            @Override
            protected void handleItemViewClick(CustomHolder holder, T item) {
                int oldPos = selectedList.get(selectedPosition);
                int pos = holder.getAdapterPosition();
                selectedPositionSecond = pos;
                selectedList.set(selectedPosition, pos);
                notifyItemChanged(pos);
                if (oldPos >= 0) {
                    notifyItemChanged(oldPos);
                }
            }

            @Override
            public void creatingHolder(CustomHolder holder, List<T> dataList, int adapterPos, int viewType) {
                holder.getView(R.id.select_arrow);
                holder.getView(R.id.filter_name);

            }

            @Override
            public void bindingHolder(CustomHolder holder, List<T> dataList, int pos) {
                TextView tvName =  holder.getView(R.id.filter_name);
                ImageView ivArrow =  holder.getView(R.id.select_arrow);
                tvName.setText(getSecondListItemText(dataList.get(pos)));
                if (pos == selectedList.get(selectedPosition)) {
//                if (pos == selectedPositionSecond) {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_blue));
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_color));
                    ivArrow.setVisibility(View.GONE);
                }
                holder.getRootView().setBackgroundResource(R.color.white);
                holder.getRootView().setTag(filters.get(selectedPosition));
            }
        };

        adapter.setOnItemClickListener(itemClickListener);

        rvFilter.setAdapter(adapter);
        rvFirstFilter.setAdapter(firstAdapter);

    }

    public <V extends View> V getBottomView(int bottomViewId) {
        if (bottomLayout == null) {
            return null;
        }
        View view = bottomViewList.get(bottomViewId);
        if (view == null) {
            view = bottomLayout.findViewById(bottomViewId);
            bottomViewList.put(bottomViewId, view);
        }
        return (V) view;
    }

    public void setListHeight(int height) {
        ViewGroup.LayoutParams params = this.listLayout.getLayoutParams();
        params.height = height;
        this.listLayout.setLayoutParams(params);
    }

    /**
     * 返回每个Item显示的文字
     *
     * @param item
     */
    protected abstract String getFirstListItemText(T item);

    protected abstract String getSecondListItemText(Object item);

    protected abstract List getSecondAdapterList(T firstListItem);


}
