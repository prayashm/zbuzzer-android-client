package in.zairza.zbuzzer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {

    public final static String IP_ADD = "in.zairza.zbuzzer.IP_ADD";
    public final static String USER_NAME = "in.zairza.zbuzzer.USER_NAME";
    public final static String GAME_NAME = "in.zairza.zbuzzer.GAME_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoBuzzer (View view) {
        Intent intent = new Intent(this, BuzzerActivity.class);
        EditText eipadd = (EditText) findViewById(R.id.editTextip);
        EditText eusername = (EditText) findViewById(R.id.editTextusername);
        EditText egamename = (EditText) findViewById(R.id.editTextgamename);
        String ipadd = eipadd.getText().toString();
        String username = eusername.getText().toString();
        String gamename= egamename.getText().toString();
        intent.putExtra(IP_ADD, ipadd);
        intent.putExtra(USER_NAME, username);
        intent.putExtra(GAME_NAME, gamename);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
