package com.example.term_20184056;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Queue<String> File_Q = new LinkedList<>();  //계산식 저장
    String input, ex1, ex2, an;

    TextView sentence, answer;
    //TextView error;
    EditText edit1;
    Button submit, btn_left, btn_right, btn_dot, btn_back, btn_enter, btn_file, ca, add, sub, mul, div, mod, and, or, not, xor, history, quiz;
    Button[] numButtons = new Button[10];
    Integer[] numBtnIDs = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
    int i, QSIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cal);
        setTitle("20184056 계산기");

        checkPermission();  //파일 접근 권한 요청
        init();    //위젯 연결 & 화면 및 리스트

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input=edit1.getText().toString(); //중위표기 수식 (사용자가 입력)
                String[] array = input.split("\n");   //\n로 구분하여 자름
                for(int i=0;i<array.length;i++) {
                    File_Q.add(array[i]);   //사용자로부터 입력받은 식을 큐에 삽입
                }
                QSIZE=File_Q.size();    //몇개의 식이 입력되었는지 저장
                for(int i=0;i<QSIZE;i++){
                    ex1=File_Q.poll();    //큐에 저장된 내용 반환 및 삭제
                    sentence.setText(ex1);  //계산중인 식 표시
                    ex2= change(ex1);   //후위표기 수식
                    an=calculate(ex2);  //후위표기 수식을 가지고 답을 계산
                    answer.setText(an); //계산 결과 표시
                    saveFile(0, ex1+"="+an); //파일 쓰기
                    //error.setText(ex2);
                }
                edit1.setText("");  //사용자가 편하게 입력할 수 있도록 edit 초기화
            }
        });

        for(i=0;i<numBtnIDs.length;i++){
            final int index; index = i;
            numButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addstring(numButtons[index].getText().toString());
                }
            });
        }

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("(");
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring(")");
            }
        });
        btn_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring(".");
            }
        });
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("\n");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edit1.getText().toString().trim();
                if(str.length()!=0){
                    str  = str.substring( 0, str.length() - 1 );    //뒤에서부터 한글자씩 삭제
                    edit1.setText ( str );
                }
            }
        });
        ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence.setText("");
                answer.setText("");
                edit1.setText("");  //입력된 식 삭제
            }
        });
        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFile();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("+");
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("-");
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("*");
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("/");
            }
        });
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("%");
            }
        });

        and.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("&");
            }
        });
        or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("|");
            }
        });
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("~");
            }
        });
        xor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstring("^");
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile(1,"");
            }
        });

    }

    void init(){
        sentence = (TextView)findViewById(R.id.sentence);
        answer = (TextView)findViewById(R.id.answer);
        //error = (TextView)findViewById(R.id.error);
        edit1 = (EditText)findViewById(R.id.edit1);
        submit = (Button)findViewById(R.id.submit);

        btn_left = (Button)findViewById(R.id.btn_left);
        btn_right = (Button)findViewById(R.id.btn_right);
        btn_dot = (Button)findViewById(R.id.btn_dot);
        btn_enter = (Button)findViewById(R.id.btn_enter);

        btn_back = (Button)findViewById(R.id.btn_back);
        ca = (Button)findViewById(R.id.btn_ca);
        btn_file = (Button)findViewById(R.id.btn_file);

        add = (Button)findViewById(R.id.add);
        sub = (Button)findViewById(R.id.sub);
        mul = (Button)findViewById(R.id.mul);
        div = (Button)findViewById(R.id.div);
        mod = (Button)findViewById(R.id.mod);

        and = (Button)findViewById(R.id.and);
        or = (Button)findViewById(R.id.or);
        not = (Button)findViewById(R.id.not);
        xor = (Button)findViewById(R.id.xor);
        history = (Button)findViewById(R.id.history);
        quiz = (Button)findViewById(R.id.quiz);

        for(i=0;i<numBtnIDs.length;i++){ numButtons[i] = (Button)findViewById(numBtnIDs[i]); }

        sentence.setText("");
        answer.setText("");
        edit1.setText("");
    }
    void addstring(String str){
        edit1.append(str);   //입력한 대로 텍스트뷰 출력
    }
    int getweight(char op) {
        //우선순위 지정 https://dojang.io/mod/page/view.php?id=188 참고
        switch (op) {
            case '~':
                return 7;  //비트 NOT 연산자
            case '*':
            case '/':
            case '%':
                return 6;   //곱하기, 나누기, 나머지
            case '+':
            case '-':
                return 5;   //더하기, 빼기
            case '&':
                return 4;   //비트 AND 연산자
            case '^':
                return 3;   //비트 XOR 연산자
            case '|':
                return 2; //비트 OR 연산자
            case '(':
                return 1;
        }
        return -1;
    }
    public boolean isProceed(char op1, char op2) {
        //우선순위 비교
        int op1Prec = getweight(op1);
        int op2Prec = getweight(op2);
        if (op1Prec >= op2Prec) return true;
        else return false;
    }
    public String change(String exp) {
        //전위 표기법 -> 후위 표기법
        Stack<Character> stack = new Stack<>();
        int len = exp.length();
        String postFix = "";

        for (int i = 0; i < len; i++) {
            char ch = exp.charAt(i);
            if (ch >= '0' && ch <= '9' || ch=='.') {
                postFix += ch;
            } else {
                postFix += " "; //연산자 만난 경우 숫자 구분을 위해 공백 추가
                switch (ch) {
                    case '(':
                        stack.push(ch);
                        break;
                    case ')':
                        while (true) {
                            char pop = stack.pop();
                            if (pop == '(')
                                break;
                            postFix += pop;
                        }
                        break;
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '%':
                    case '&':
                    case '|':
                    case '^':
                    case '~':
                        while (!stack.isEmpty() && isProceed(stack.peek(), ch))
                            postFix += stack.pop()+" "; //공백 추가
                        stack.push(ch);
                        break;
                }
            }
        }
        while (!stack.isEmpty()) postFix += " "+stack.pop();    //공백 추가
        return postFix; //완성된 후위 표기 수식 리턴
    }
    public String calculate(String ex) {
       Stack stack = new Stack();
        char[] f = ex.toCharArray();
        String number="";

        for (char c : f) {
            if (c >= '0' && c <= '9' || c=='.'){
                number += String.valueOf(c);    //공백을 만날때까지 숫자 저장 및 완성
            }
            else if(c==' '){
                //공백을 만났을 때
                if(!number.equals("")) stack.push(number);  //number에 입력된 값이 있다면 stack저장
                number="";  //다음숫자를 저장하기 위해 다시 초기화
            }
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c=='%') {
                //int a = Integer.parseInt(stack.pop().toString());
                //int b = Integer.parseInt(stack.pop().toString());
                //double a = Double.parseDouble(stack.pop().toString());
                //double b = Double.parseDouble(stack.pop().toString());

                BigDecimal a = new BigDecimal(stack.pop().toString());
                BigDecimal b = new BigDecimal(stack.pop().toString());

                if (c == '+') stack.push(b.add(a));
                if (c == '-') stack.push(b.subtract(a));
                if (c == '*') stack.push(b.multiply(a));
                if (c == '/') stack.push(b.divide(a, MathContext.DECIMAL32));   //소숫점 지정
                if (c == '%') stack.push(b.remainder(a));

                /*
                if (c == '+') stack.push(b + a);
                if (c == '-') stack.push(b - a);
                if (c == '*') stack.push(b * a);
                if (c == '/') stack.push(b / a);
                if (c == '%') stack.push(b % a);
                 */
            }
            else if (c == '&' || c == '|' || c == '^') {
                int a = Integer.parseInt(stack.pop().toString());
                int b = Integer.parseInt(stack.pop().toString());

                if (c == '&') stack.push(b&a);
                if (c == '|') stack.push(b|a);
                if (c == '^') stack.push(b^a);
            }
            else if (c == '~'){
                int a = Integer.parseInt(stack.pop().toString());
                stack.push(~a);
            }
        }
        return stack.pop().toString();
    }
    public void checkPermission(){
        //파일 접근 권한 요청
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void saveFile(int check, String str){
        //check0 = 입력받은 계산식의 결과 출력
        //check1 = 사용자가 입력한 계산식 (History)
        File saveFile = null;
        if( Build.VERSION.SDK_INT < 29) saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/calSaveFiles");
        else saveFile = MainActivity.this.getExternalFilesDir("/calSaveFiles");
        if(!saveFile.exists()) saveFile.mkdir();    //만약 파일이 없으면 새 파일 생성

        try {
            long now = System.currentTimeMillis(); //현재시간 받아오기
            Date date = new Date(now); //Date 객체 생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String nowTime = sdf.format(date);  //현재 시간 문자열 (파일이름)
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime2 = sdf2.format(date);  //현재 시간 문자열 (파일내용)

            if(check==0){
                //BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/Answer_"+nowTime+".txt", true));
                //buf.append(str); // 받아온 문자열 쓰기
                //buf.newLine(); // 개행
                //buf.close();

                BufferedWriter buf2 = new BufferedWriter(new FileWriter(saveFile+"/"+check+"_List.txt", true));
                buf2.append(nowTime2 + " : "); // 날짜 쓰기
                buf2.append(str); buf2.newLine(); buf2.close(); //사용자가 입력한 모든 식의 정보 저장 (Log)

                BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/HistoryList.txt", true));
                buf.append(str); buf.newLine(); buf.close(); //식과 답 저장
            }

            if(check==1){
                BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/test.txt", false));
                buf.append("(100+100)*2");
                buf.newLine();
                buf.append("350/2");
                buf.newLine();
                buf.append("150*5");
                buf.newLine();
                buf.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile(){
        String line = null; // 한줄씩 읽기
        File saveFile = null;
        if( Build.VERSION.SDK_INT < 29) saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/calSaveFiles");
        else saveFile = MainActivity.this.getExternalFilesDir("/calSaveFiles");
        if(!saveFile.exists()) saveFile.mkdir();    //만약 파일이 없으면 새 파일 생성
        try {
            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/test.txt"));
            while((line=buf.readLine())!=null){
                edit1.append(line);
                edit1.append("\n");
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}