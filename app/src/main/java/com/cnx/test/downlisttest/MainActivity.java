package com.cnx.test.downlisttest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DownListPopupWindow popupWindow ;
    DownDoubleListPopupWindow downDoubleListPopupWindow ;
    private Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btnSingle);
        btn2 = (Button) findViewById(R.id.btnDouble);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSingle:
                if  (popupWindow!=null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return;
                }

                /**
                 * setting single List popupWindow
                 */
                final List<FirstBean> list = new ArrayList<>();
                list.add(new FirstBean(11, "haha", 0));
                list.add(new FirstBean(12, "hello", 0));
                list.add(new FirstBean(13, "world", 1));
                list.add(new FirstBean(14, "welcome", 0));
                int bottomViewLayoutId = 0; // 设置bottomViewLayoutId，0表示没有bottomViewLayout
                popupWindow = new DownListPopupWindow<FirstBean>(this, list, new XAdapter.OnItemClickListener<FirstBean>() {
                    @Override
                    public void onItemClick(XAdapter.CustomHolder holder, FirstBean item) {
                        // do something...
                        Toast.makeText(getApplication(), item.getName() + " ID:"+item.getID(), Toast.LENGTH_SHORT).show();
                    }
                }, bottomViewLayoutId) {
                    @Override
                    protected String getItemText(FirstBean item) {
                        return item.getName();
                    }

                };
                popupWindow.setListHeight(480);
                popupWindow.showAsDropDown(v);
                break;
            case R.id.btnDouble:
                if (downDoubleListPopupWindow!= null && downDoubleListPopupWindow.isShowing()) {
                    downDoubleListPopupWindow.dismiss();
                    return;
                }

                /**
                 * setting single List popupWindow
                 */
                final List<FirstBean> list2 = new ArrayList<>();
                list2.add(new FirstBean(31, "ha--ha", 0));
                list2.add(new FirstBean(32, "he--llo", 0));
                list2.add(new FirstBean(33, "wo--rld", 1));
                list2.add(new FirstBean(34, "we--lcome", 0));
                int dBottomViewLayoutId = R.layout.layout_bottom_confirm; // 设置bottomViewLayoutId，0表示没有bottomViewLayout
                downDoubleListPopupWindow = new DownDoubleListPopupWindow<FirstBean>(this, list2, new XAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(XAdapter.CustomHolder holder, Object item) {
                        // do something...
                       FirstBean aBean =  ((FirstBean) holder.getRootView().getTag());

                        Toast.makeText(getApplication(), ((SecondBean) item).getDes() + " ID:" + aBean.getID() +" "+((SecondBean) item).getID(), Toast.LENGTH_SHORT).show();
                    }
                }, dBottomViewLayoutId) {
                    @Override
                    protected String getFirstListItemText(FirstBean item) {
                        return item.getName();
                    }

                    @Override
                    protected String getSecondListItemText(Object item) {
                        return ((SecondBean) item).getDes();
                    }

                    @Override
                    protected List getSecondAdapterList(FirstBean firstListItem) {
                        int id = firstListItem.getID();
                        final List<SecondBean> list = new ArrayList<>();
                        // 取得第二列表的数据
                        list.add(new SecondBean(id +"-Eventaha", id + 2000));
                        list.add(new SecondBean(id +"-Eventello", id + 2100));
                        list.add(new SecondBean(id +"-Eventorld", id + 2200));
                        list.add(new SecondBean(id +"-Eventelcome", id + 2300));
                        list.add(new SecondBean(id +"-Eventoewdd", id + 2400));
                        list.add(new SecondBean(id +"-Eventelhhyuome", id + 2500));
                        return list;
                    }

                };
                downDoubleListPopupWindow.getBottomView(R.id.btnCancel).setOnClickListener(this);
                downDoubleListPopupWindow.getBottomView(R.id.btnConfirm).setOnClickListener(this);
                downDoubleListPopupWindow.setListHeight(480);
                downDoubleListPopupWindow.showAsDropDown(v);
                break;
            case R.id.btnCancel:
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                if (downDoubleListPopupWindow!= null && downDoubleListPopupWindow.isShowing()) {
                    downDoubleListPopupWindow.dismiss();
                }
                break;
            case R.id.btnConfirm:
                Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
                if (downDoubleListPopupWindow!= null && downDoubleListPopupWindow.isShowing()) {
                    downDoubleListPopupWindow.dismiss();
                }
                break;
        }
    }
}
