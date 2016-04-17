package com.cnx.test.downlisttest;

import android.app.Activity;
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


import com.cnx.test.downlisttest.adapter.XAdapter;

import java.util.List;

/**
 * 区域筛选条件
 * Created by Xiu on 2016/4/16 0006.
 */

public abstract class DownListPopupWindow<T> extends PopupWindow {

    private Activity activity;
    private LayoutInflater inflater;

    private List<T> filters;
    private int selectedPosition = -1;
    private XAdapter.OnItemClickListener itemClickListener = null;
    private LinearLayout listLayout;
    private SparseArray<View> bottomViewList;
    private View bottomLayout;
    private View rootView;

    public DownListPopupWindow(Activity activity, List<T> filters, XAdapter.OnItemClickListener itemClickListener, int bottomLayoutId) {
//        super(activity.getLayoutInflater().inflate(R.layout.popup_window_filter, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
    }

    private void initViews() {
        inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(R.layout.popup_window_filter, null);

        setContentView(rootView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(null);
        update();

        listLayout = (LinearLayout) rootView.findViewById(R.id.llFilterList);
        RecyclerView rvFilter = (RecyclerView) rootView.findViewById(R.id.rvFilter);
        rvFilter.setLayoutManager(new LinearLayoutManager(activity));
        XAdapter adapter = new XAdapter(activity, filters, R.layout.popup_item_filter) {
            @Override
            public void creatingHolder(final CustomHolder holder, final List dataList, int viewType) {
                holder.getView(R.id.select_arrow);
                holder.getView(R.id.filter_name);
                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int oldPos = selectedPosition;
                        int pos = holder.getAdapterPosition();
                        selectedPosition = pos;
                        if (itemClickListener != null) itemClickListener.onItemClick(v, dataList.get(pos), pos);
                        notifyItemChanged(pos);
                        if (oldPos >= 0) {
                            notifyItemChanged(oldPos);
                        }
                    }
                });
            }

            @Override
            public void bindingHolder(CustomHolder holder, List dataList, int pos) {
                TextView tvName = (TextView) holder.getView(R.id.filter_name);
                ImageView ivArrow = (ImageView) holder.getView(R.id.select_arrow);
                tvName.setText(getItemText((T) dataList.get(pos)));
                if (pos == selectedPosition) {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_blue));
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    tvName.setTextColor(activity.getResources().getColor(R.color.text_color));
                    ivArrow.setVisibility(View.GONE);
                }
                holder.getRootView().setBackgroundResource(R.color.white);
            }
        };
        adapter.setOnItemClickListener(itemClickListener);

        rvFilter.setAdapter(adapter);
    }

    public <B extends View> B getBottomView(int bottomViewId) {
        if (bottomLayout == null) {
            return null;
        }
        View view = bottomViewList.get(bottomViewId);
        if (view == null) {
            view = bottomLayout.findViewById(bottomViewId);
            bottomViewList.put(bottomViewId, view);
        }
        return (B) view;
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
    protected abstract String getItemText(T item);


}
