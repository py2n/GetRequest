package com.mohammad.getrequest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class getRequest extends Activity{
    boolean flag = true;
    TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        output = (TextView) findViewById(R.id.textView);
        tasks = new ArrayList<>();
        Bundle data=getIntent().getExtras();
        String request;
        request = data.getString("request");
        requestData(request);
        pb.setVisibility(View.INVISIBLE);
    }


    protected void updateDisplay(String result) {
        try {
            if (result.contains("Congratulation"))
                output.setText("Congratulation new mac address added to database" + "\n");
            else if (result.contains("been") && flag)
                output.setText("درخواست روشن شدن با موفقیت ثبت شد \n");
            else if (result.contains("been") && !flag)
                output.setText("درخواست خاموش شدن با موفقیت ثبت شد \n");
            else if (result.contains("error"))
                output.setText("در حال حاضر امکان برقراری ارتباط با\n مرکز سرویس دهی موجود نمی باشد \n");
            if (result.contains("new version"))
                output.setText("لطفا نرم افزار خود را بروز رسانی کنید");

            output.append(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected void requestData(final String req) {
        flag = true;

                pb.setVisibility(View.INVISIBLE);
                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                p.setUri("http://192.168.56.1:8000/rtc");
                p.setParam("mac_addr", "00:00:00:00:00:45");
                flag = true;
                MyTask task = new MyTask();
                task.execute(p);
            }


    private class MyTask extends AsyncTask<RequestPackage, String, String> {
        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }
        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            int begining=result.indexOf("body")+5;
            int end=result.lastIndexOf("body")-2;
            result=result.substring(begining, end);
            result=result.replace(',',' ');
            String regex="(?=\\()|(?<=\\)\\d)";
            String [] request=result.split(regex); // now incoming request converted to string []
            for (int i=1;i<request.length;i++){
                updateDisplay(request[i]+'\n');
            }
            try {
                tasks.remove(this);
                if (tasks.size() == 0) {
                    pb.setVisibility(View.INVISIBLE);
                }
                if (result.contains("error"))
                    updateDisplay("problem on connecting to service provider");
//                updateDisplay(result);
            } catch (Exception e) {
                System.out.println("error occured in " + e.toString());
                updateDisplay("problem on sending the request");
            }
        }
    }
}
