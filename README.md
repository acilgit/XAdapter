# XAdapter
万能RecyclerView的Adapter

/**
 * 使用单一Layout
 * @param context
 * @param dataList
 * @param layoutId 或者 layoutIdList Adapter的Item Layout XML
 */
XAdapter<T> xAdapter = new XAdapter<T>(context, list, R.layout.layout_id) {
new XAdapter时可以把ClassType传进XAdapter，如UserBean等实现下面两个方法：
    @Override
    public void creatingHolder(CustomHolder holder, List<T> dataList, int viewType) {
      创建Holder时调用的方法，在此处使用holder.getView(ViewId)绑定Listeners
    }
    @Override
    public void bindingHolder(CustomHolder holder, List<T> dataList, int pos) {
      对Holder进行数据绑定
    }
    如果需要对不同layout显示不同的holder， 请重写protected int getItemType(T item)方法
    并在创建时传入layoutId改成SparseArray里面传入对应的type和LayoutId
｝
    
---------------------------------------------
本例在使用两种弹窗：单列表弹窗，和双列表弹窗
底部的bottomView能过layoutId传入，可以能过getBottomView(viewId)取出对应的View对其进行绑定和操作

    /**
     * 返回每个Item显示的文字
     * @param item
     */
    protected abstract String getFirstListItemText(T item);
    protected abstract String getSecondListItemText(Object item);
    protected abstract List getSecondAdapterList(T firstListItem);


