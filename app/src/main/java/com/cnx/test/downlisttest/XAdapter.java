package com.cnx.test.downlisttest;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiu on 2016/4/3.
 */
public abstract class XAdapter<T> extends RecyclerView.Adapter {

    public static final int SINGLE_LAYOUT = -1;
    private List<T> dataList;
    private Context context;
    private SparseArray<Integer> layoutIdList;
    // 点击及长按Listener
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 使用单一Layout
     * @param context
     * @param dataList
     * @param layoutId
     */
    public XAdapter(Context context, List<T> dataList, @LayoutRes int layoutId) {
        this.context = context;
        this.dataList = dataList;
        this.layoutIdList = new SparseArray<>();
        layoutIdList.put(SINGLE_LAYOUT, layoutId);
    }

    /**
     * 使用Holder分类列表Layout
     * @param context
     * @param dataList
     * @param layoutIdList key: viewType  value: layoutId
     */
    public XAdapter(Context context, List<T> dataList, SparseArray layoutIdList) {
        this.context = context;
        this.dataList = dataList;
        this.layoutIdList = layoutIdList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        @LayoutRes int layoutId = (layoutIdList.size() == 1 ? layoutIdList.get(SINGLE_LAYOUT) : layoutIdList.get(viewType));
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        final CustomHolder holder = new CustomHolder(itemView){
            @Override
            protected void createHolder(final CustomHolder holder) {
                holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleItemViewClick(holder, dataList.get(holder.getAdapterPosition()));
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder, dataList.get(holder.getAdapterPosition()));
                    }
                }
            });

                if (onItemLongClickListener != null) {
                    holder.getRootView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemLongClickListener.onItemLongClick(holder, dataList.get(holder.getAdapterPosition()));
                        }
                    });
                }
                 creatingHolder(holder, dataList, getAdapterPosition(), viewType);
            }
        };
        

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindingHolder(((CustomHolder) holder), dataList, position);
    }

    /**
     * 创建Holer时绑定控件
     * @param holder
     * @param dataList
     * @param viewType
     */
    public abstract void creatingHolder(CustomHolder holder, List<T> dataList,int adapterPos, int viewType);

    /**
     * 在适配器中显示数据集
     * @param holder
     * @param dataList
     * @param pos
     */
    public abstract void bindingHolder(CustomHolder holder, List<T> dataList, int pos);


    /**
     * 复写此方法可以在不同的layout中显示
     * @param item
     * @return
     */
    protected int getItemType(T item) {
        return SINGLE_LAYOUT;
    }


    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        if (this.dataList != null) {
            this.dataList.clear();
        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void removeItem( int pos) {
        dataList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addItem( int pos, T item) {
        dataList.add(pos, item);
        notifyItemInserted(pos);
    }

    public void addItem(T item) {
        dataList.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        int type = getItemType(dataList.get(position));
        if (type == SINGLE_LAYOUT) {
            return super.getItemViewType(position);
        } else {
            return type;
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if(this.onItemClickListener!= null) this.onItemClickListener = null;
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        if(this.onItemLongClickListener!= null) this.onItemLongClickListener = null;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 重写此事件，用于处理holder rootView点击事件，处理完毕后再处理onItemClickListener()
     * @param holder
     * @param item
     */
    protected void handleItemViewClick(CustomHolder holder, T item) {

    }

    /**
     * 点击接口
     */
    public interface OnItemClickListener<T> {
        void onItemClick(CustomHolder holder, T item);
    }

    /**
     * 长按接口
     */
    public interface OnItemLongClickListener<T> {
        void onItemLongClick(CustomHolder holder, T item);
    }

    public  static abstract class CustomHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> viewList;
        private View itemView;

        public CustomHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            viewList = new SparseArray<>();
            createHolder(this);
        }

        protected abstract void createHolder(CustomHolder holder);

        public <T extends View> T getView(int viewId) {
            View view = viewList.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                viewList.put(viewId, view);
            }
            return (T) view;
        }

        public View getRootView() {
            return itemView;
        }

        public CustomHolder setText(int viewId, String text) {
            TextView textView = getView(viewId);
            textView.setText(text);
            return this;
        }

        public CustomHolder setEditText(int viewId, String text) {
            EditText editText = getView(viewId);
            editText.setText(text);
            return this;
        }
    }
}
