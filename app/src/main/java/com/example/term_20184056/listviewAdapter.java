package com.example.term_20184056;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class listviewAdapter extends ArrayAdapter implements View.OnClickListener  {
    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }
    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;

    listviewAdapter(Context context, int resource, ArrayList<listview> list, ListBtnClickListener clickListener) {
        super(context, resource, list) ;
        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;
        this.listBtnClickListener = clickListener ;
    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final TextView number = (TextView) convertView.findViewById(R.id.textView_number);
        final TextView ex = (TextView) convertView.findViewById(R.id.textView_ex);
        final TextView an = (TextView) convertView.findViewById(R.id.textView_an);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final listview listViewItem = (listview) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        number.setText(listViewItem.getnumber());
        ex.setText(listViewItem.getex());
        an.setText(listViewItem.getan());

        // button1 클릭 시 TextView(textView1)의 내용 변경.
        Button button1 = (Button) convertView.findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ex.setText(Integer.toString(pos + 1) + "번 아이템 선택.");
            }
        });

        // button2의 TAG에 position값 지정. Adapter를 click listener로 지정.
        //Button button2 = (Button) convertView.findViewById(R.id.button2);
        //button2.setTag(position);
        //button2.setOnClickListener(this);

        return convertView;
    }

    // button2가 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
         //ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener != null) {
           this.listBtnClickListener.onListBtnClick((int)v.getTag()) ;
        }
    }

}