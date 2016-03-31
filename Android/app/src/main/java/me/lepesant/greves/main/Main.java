package me.lepesant.greves.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import me.lepesant.greves.R;
import me.lepesant.greves.network.Client;
import me.lepesant.greves.station.StationList;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView msg = (TextView) findViewById(R.id.errorPrint);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getIntent() == null) {
            msg.setText("Prêt à se connecter");
        } else {
            msg.setText(getIntent().getStringExtra("Message"));
        }


        Button connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView msg = (TextView) findViewById(R.id.errorPrint);
                EditText ip = (EditText) findViewById(R.id.serverIp);
                try {
                    Client client = Client.getInstance();
                    client.beginConnexion(ip.getText().toString(), 8888);
                    Intent listIntent = new Intent(Main.this, StationList.class);
                    startActivity(listIntent);
                } catch (IOException e) {
                    msg.setText("Erreur de connexion au serveur : " + e.getMessage());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView msg = (TextView) findViewById(R.id.errorPrint);
        msg.setText("Prêt à se connecter");
    }
}
