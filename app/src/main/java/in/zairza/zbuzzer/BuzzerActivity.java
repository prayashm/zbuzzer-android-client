package in.zairza.zbuzzer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class BuzzerActivity extends ActionBarActivity {

    Button buzzer;
    TextView textViewgamename;
    TextView textViewusername;
    TextView textViewstatus;
    String ipadd;
    String username;
    String gamename;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzzer);
        setTitle("zBuzzer!");

        Intent intent = getIntent();
        ipadd = intent.getStringExtra(MainActivity.IP_ADD);
        username = intent.getStringExtra(MainActivity.USER_NAME);
        gamename = intent.getStringExtra(MainActivity.GAME_NAME);
        textViewgamename = (TextView) findViewById(R.id.gamename);
        textViewusername = (TextView) findViewById(R.id.username);
        textViewgamename.setText(gamename);
        textViewusername.setText(username);
        textViewstatus = (TextView) findViewById(R.id.statusmsg);
        {
            try {
                setStatus("Connecting...");
                socket = IO.socket("http://"+ipadd);
                socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                socket.on("reset", onReset);
                socket.connect();
                socket.emit("join", gamename, username);
                setStatus("Connected!");

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Failed to connect", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BuzzerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    };

    private Emitter.Listener onReset = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetBuzz();
                }
            });
        }
    };

    public void sendBuzz(View view){
        buzzer = (Button) findViewById(R.id.buzzer);
        setStatus("Buzz Sent");
        socket.emit("buzz", username, gamename);
        buzzer.setText("BUZZED!");
    }

    private void Toast(String toastText){
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    private void setStatus(String status){
        textViewstatus.setText(status);
    }

    public void resetBuzz(){
        buzzer = (Button) findViewById(R.id.buzzer);
        setStatus("Buzzer Reset");
        buzzer.setText("BUZZ!");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.emit("leave",username,gamename);
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.off("reset", onReset);
    }
}