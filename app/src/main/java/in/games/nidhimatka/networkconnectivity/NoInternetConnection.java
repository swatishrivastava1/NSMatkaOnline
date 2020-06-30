package in.games.nidhimatka.networkconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import in.games.nidhimatka.Activity.splash_activity;
import in.games.nidhimatka.R;


public class NoInternetConnection extends AppCompatActivity {
  ImageView mCheckConnection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.actvity_no_internet_connection);
    mCheckConnection = (ImageView) findViewById(R.id.no_internet_connection);
    mCheckConnection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
          Intent intent = new Intent(NoInternetConnection.this, splash_activity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                  Intent.FLAG_ACTIVITY_CLEAR_TASK |
                  Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        } else {
          Toast.makeText(getApplicationContext(),"Check your connection.Try again!", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
