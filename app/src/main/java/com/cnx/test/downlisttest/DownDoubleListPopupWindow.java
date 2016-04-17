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
 * Created by Administrator on 2016/4/6 0006.
 */
public abstract class DownDoubleListPopupWindow<T> extends PopupWindow {

    private Activity activity;
    private LayoutInflater inflater;

    private List<T> filters;
    private int selectedPosition = -1;
    private int selectedPositionSecond = -1;
    private XAdapter.OnItemClickListener itemClickListener = null;
    private LinearLayout listLayout;
    private SparseArray<View> bottomViewList;
    private View bottomLayout;
    private View rootView;
    private RecyclerView rvFirstFilter, rvFilter;

    public DownDoubleListPopupWindow(Activity activity, List<T> filters, XAdapter.OnItemClickListener itemClickListener, int bottomLayoutId) {
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
         rvFirstFilter = (RecyclerView) rootView.findViewById(R.id.rvFirstFilter);
         rvFilter = (RecyclerView) rootView.findViewById(R.id.rvFilter);
        rvFilter.setLayoutManager(new LinearLayoutManager(activity));
        rvFirstFilter.setLayoutManager(new LinearLayoutManager(activity));
        rvFirstFilter.setVisibility(View.VISIBLE);

        /**
         * 第一个列表
         */
        XAdapter firstAdapter = new XAdapter(activity, filters, R.layout.popup_item_filter) {
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
                        /**
                         * 子列表
                         */
                        XAdapter adapter = setSecondAdapter(getSecondAdapterList((T) dataList.get(pos)));
                        adapter.setOnItemClickListener(itemClickListener);
                        rvFilter.setAdapter(adapter);

                        if (adapter.getItemCount()>0) {
                            selectedPositionSecond = 0;
                        }
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
                tvName.setText(getFirstListItemText((T) dataList.get(pos)));
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
        if (filters.size()>0) {
            selectedPosition = 0;
            XAdapter adapter = setSecondAdapter(getSecondAdapterList(filters.get(0)));
            if (adapter.getItemCount()>0) {
                selectedPositionSecond = 0;
            }
            rvFilter.setAdapter(adapter);
        }
        rvFirstFilter.setAdapter(firstAdapter);

    }

    private XAdapter setSecondAdapter(List list) {
        XAdapter adapter = new XAdapter(activity, list, R.layout.popup_item_filter) {
        @Override
        public void creatingHolder(final CustomHolder holder, final List dataList, int viewType) {
            holder.getView(R.id.select_arrow);
            holder.getView(R.id.filter_name);
            holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldPos = selectedPositionSecond;
                    int pos = holder.getAdapterPosition();
                    selectedPositionSecond = pos;
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
            tvName.setText(getSecondListItemText((T) dataList.get(pos)));
            if (pos == selectedPositionSecond) {
                tvName.setTextColor(activity.getResources().getColor(R.color.text_blue));
                ivArrow.setVisibility(View.VISIBLE);
            } else {
                tvName.setTextColor(activity.getResources().getColor(R.color.text_color));
                ivArrow.setVisibility(View.GONE);
            }
            holder.getRootView().setBackgroundResource(R.color.white);
        }
    };
        return adapter;
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

    public void performFirstClick(int pos) {
//        itemClickListener.onItemClick();
    }

    /**
     * 返回每个Item显示的文字
     *
     * @param item
     */
    protected abstract String getFirstListItemText(T item);

    protected abstract String getSecondListItemText(T item);

    protected abstract List<T> getSecondAdapterList(T firstListItem);


}
