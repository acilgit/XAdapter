package com.cnx.test.downlisttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cnx.test.downlisttest.adapter.XAdapter;

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
                final List<UserBean> list = new ArrayList<>();
                list.add(new UserBean(11, "haha", 0));
                list.add(new UserBean(12, "hello", 0));
                list.add(new UserBean(13, "world", 1));
                list.add(new UserBean(14, "welcome", 0));
                int bottomViewLayoutId = 0; // 设置bottomViewLayoutId，0表示没有bottomViewLayout
                popupWindow = new DownListPopupWindow(this, list, new XAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object item, int pos) {
                        // do something...
                        Toast.makeText(getApplication(), ((UserBean) item).getName() + " ID:"+((UserBean) item).getID(), Toast.LENGTH_SHORT).show();
                    }
                }, bottomViewLayoutId) {
                    @Override
                    protected String getItemText(Object item) {
                        return ((UserBean) item).getName();
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
                final List<UserBean> list2 = new ArrayList<>();
                list2.add(new UserBean(31, "ha--ha", 0));
                list2.add(new UserBean(32, "he--llo", 0));
                list2.add(new UserBean(33, "wo--rld", 1));
                list2.add(new UserBean(34, "we--lcome", 0));
                int dBottomViewLayoutId = R.layout.layout_bottom_confirm; // 设置bottomViewLayoutId，0表示没有bottomViewLayout
                downDoubleListPopupWindow = new DownDoubleListPopupWindow(this, list2, new XAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object item, int pos) {
                        // do something...
                        Toast.makeText(getApplication(), ((EventBean) item).getDes() + " ID:"+((EventBean) item).getID(), Toast.LENGTH_SHORT).show();
                    }
                }, dBottomViewLayoutId) {
                    @Override
                    protected String getFirstListItemText(Object item) {
                        return ((UserBean) item).getName();
                    }

                    @Override
                    protected String getSecondListItemText(Object item) {
                        return ((EventBean) item).getDes();
                    }

                    @Override
                    protected List getSecondAdapterList(Object firstListItem) {
                        int id = ((UserBean) firstListItem).getID();
                        final List<EventBean> list = new ArrayList<>();
                        list.add(new EventBean(id +"-Eventaha", id + 2000));
                        list.add(new EventBean(id +"-Eventello", id + 2100));
                        list.add(new EventBean(id +"-Eventorld", id + 2200));
                        list.add(new EventBean(id +"-Eventelcome", id + 2300));
                        list.add(new EventBean(id +"-Eventoewdd", id + 2400));
                        list.add(new EventBean(id +"-Eventelhhyuome", id + 2500));
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
