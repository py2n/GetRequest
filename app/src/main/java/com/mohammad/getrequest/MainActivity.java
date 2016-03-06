package com.mohammad.getrequest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	ProgressBar pb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_requests);
		pb=(ProgressBar) findViewById(R.id.progressBar);
		pb.setVisibility(View.INVISIBLE);
		Button r1 = (Button) findViewById(R.id.button);
		r1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOnline()) {
					Intent intent = new Intent(MainActivity.this, getRequest.class);
					intent.putExtra("request", "6545412");
					startActivity(intent);
				} else {
					toast("اشکال در ارتباط با اینترنت");
					Intent intent = new Intent(MainActivity.this, getRequest.class);
					intent.putExtra("request", "6545412");
					startActivity(intent);
				}
			}
		});
	}


	public void toast(String a){
		Toast.makeText(this, a, Toast.LENGTH_LONG).show();
	}

	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
}