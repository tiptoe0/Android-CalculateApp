package com.example.term_20184056;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity implements listviewAdapter.ListBtnClickListener {
    LinkedList S = new LinkedList();
    
    @Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        setTitle("20184056 History");

        S.clear();  //연결리스트 초기화
        ListView listview ;
        listviewAdapter adapter;
        ArrayList<listview> items = new ArrayList<listview>() ;

        loadFile(items); ;    //리스트뷰 생성 (데이터 로드)
        adapter = new listviewAdapter(this, R.layout.listview, items, this) ;
        listview = (ListView) findViewById(R.id.listView1);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        }) ;
    }

    private void loadFile(ArrayList<listview> list){
        //파일로부터 데이터를 읽은 후 연결리스트에 저장
        String line = null; // 한줄씩 읽기;
        File saveFile = null;
        if( Build.VERSION.SDK_INT < 29) saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/calSaveFiles");
        else saveFile = HistoryActivity.this.getExternalFilesDir("/calSaveFiles");
        if(!saveFile.exists()) saveFile.mkdir();    //만약 파일이 없으면 새 파일 생성
        try {
            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/HistoryList.txt"));
            while((line=buf.readLine())!=null){
                S.add(line);
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setlistView(list);
    }
    private void setlistView(ArrayList<listview> list){
        //연결리스트로부터 리스트뷰 세팅
        listview item ;
        int i = 1;  //1부터 순서 시작
        String sentence;
        if (list == null) { list = new ArrayList<listview>() ; }
        for(int j=0;j<S.size();j++){
            sentence=S.get(j).toString();
            System.out.println("sentence:"+sentence);
            String[] array = sentence.split("=");   //=로 구분하여 자름
            item = new listview() ;
            item.setnumber(Integer.toString(i));
            item.setex(array[0]);   //식
            item.setan(array[1]);   //답
            list.add(item);
            i++;
        }
    }

    /*
    private void loadFile(ArrayList<listview> list){
        //리스트뷰 내용 표시
        listview item ;
        String sentence;
        int i = 1;  //1부터 순서 시작
        if (list == null) {
            list = new ArrayList<listview>() ;
        }
        String line = null; // 한줄씩 읽기
        File saveFile = null;
        if( Build.VERSION.SDK_INT < 29) saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/calSaveFiles");
        else saveFile = HistoryActivity.this.getExternalFilesDir("/calSaveFiles");
        if(!saveFile.exists()) saveFile.mkdir();    //만약 파일이 없으면 새 파일 생성
        try {
            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/HistoryList.txt"));
            while((line=buf.readLine())!=null){
                sentence=line;
                System.out.println("sentence:"+sentence);
                String[] array = sentence.split("=");   //=로 구분하여 자름
                item = new listview() ;
                item.setnumber(Integer.toString(i));
                item.setex(array[0]);   //식
                item.setan(array[1]);   //답
                list.add(item);
                i++;
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //연결리스트에 내용 추가
    }

     */
}
