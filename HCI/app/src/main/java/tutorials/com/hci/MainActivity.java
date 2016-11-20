package tutorials.com.hci;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int VOICE = 1001;
    private static final int NUMBER = 20995;
    private static final int CONTACT = 8888;
    private static final int NAME = 9999;
    private static final int VOLUMEUP = 7777;
    private static final int VOLUMEDOWN = 6666;
    private static final int NOTE = 5555;
    private static final int HOURRING = 4444;
    private static final int MINUSRING = 3333;
    private static final int NOTERING = 2222;
    private Button btnCall;
    private String number, name;
    public static String hour, minus, note;
    private ListView lvNote;
    private ArrayList<String> arrList=null;
    private ArrayAdapter<String> adapter=null;
    private PendingIntent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCall = (Button) findViewById(R.id.btnCall);
        lvNote = (ListView) findViewById(R.id.lvNote);
        arrList=new ArrayList<String>();
        adapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1,
                        arrList);
        lvNote.setAdapter(adapter);
        if(checkVoiceRecognition() == true){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            startActivityForResult(intent, VOICE);
        }
        Intent launchIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, launchIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, alarmIntent);
    }

    public boolean checkVoiceRecognition(){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(activities.size() == 0){
            btnCall.setEnabled(false);
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void speak(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        startActivityForResult(intent, VOICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VOICE){
            if(resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String command = text.get(0).toLowerCase().trim();
                if(command.equals("gọi")){
                    Toast.makeText(this, "Đọc số điện thoại", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, NUMBER);
                }else if(command.equals("xem nhật ký")){
                    Intent showCalllog = new Intent();
                    showCalllog.setAction(Intent.ACTION_VIEW);
                    showCalllog.setType(CallLog.Calls.CONTENT_TYPE);
                    startActivity(showCalllog);
                }else if(command.equals("lưu")){
                    Toast.makeText(this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, CONTACT);
                }else if(command.equals("tăng âm lượng")){
                    Toast.makeText(this, "1.Media/2.nhạc chuông/3.thông báo/4.hệ thống", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, VOLUMEUP);
                }else if(command.equals("giảm âm lượng")){
                    Toast.makeText(this, "1.Media/2.nhạc chuông/3.thông báo/4.hệ thống", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, VOLUMEDOWN);
                }else if(command.equals("ghi chú")){
                    Toast.makeText(this, "nhập nội dung", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, NOTE);
                }else if(command.equals("đặt lịch")){
                    Toast.makeText(this, "nhập giờ (ví dụ: 3)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                    startActivityForResult(intent, HOURRING);
                }else if(command.equals("phát lời nhắn")){
                    //mẫu giao diện
                }else if(command.equals("lưu cuộc gọi")){
                    //lưu cuộc gọi
                }else if(command.equals("nghe lại")){
                    //nghe lại
                }
            }
        }else if(requestCode == NUMBER){
            if (resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setPackage("com.android.phone");
                intent.setData(Uri.parse("tel:" + text.get(0)));
                startActivity(intent);
            }
        }else if(requestCode == CONTACT){
            if (resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                number = text.get(0).trim();
                Toast.makeText(this, "Nhập tên", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                startActivityForResult(intent, NAME);
            }
        }else if(requestCode == NAME){
            if(resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                name = text.get(0).trim();
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                name).build());
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
                try {
                    this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Contact " + name + " added.", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == VOLUMEUP){
            if (resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String choose = text.get(0).trim();
                AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                int a = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                int b = mgr.getStreamVolume(AudioManager.STREAM_RING);
                int c = mgr.getStreamVolume(AudioManager.STREAM_SYSTEM);
                int d = mgr.getStreamVolume(AudioManager.STREAM_ALARM);
                int maxa = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int maxb = mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
                int maxc = mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                int maxd = mgr.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                int set = 0;
                if(choose.equals("1")){
                    if(a + 5 > maxa) set = maxa;
                    else set = a + 5;
                    mgr.setStreamVolume(AudioManager.STREAM_MUSIC, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("2")){
                    if(b + 5 > maxb) set = maxb;
                    else set = b + 5;
                    mgr.setStreamVolume(AudioManager.STREAM_RING, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("3")){
                    if(c + 5 > maxc) set = maxc;
                    else set = c + 5;
                    mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("4")){
                    if(d + 5 > maxd) set = maxd;
                    else set = d + 5;
                    mgr.setStreamVolume(AudioManager.STREAM_ALARM, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                Toast.makeText(this, "Đã thay đổi âm lượng", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == VOLUMEDOWN){
            if (resultCode == RESULT_OK && null != data){
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String choose = text.get(0).trim();
                AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                int a = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                int b = mgr.getStreamVolume(AudioManager.STREAM_RING);
                int c = mgr.getStreamVolume(AudioManager.STREAM_SYSTEM);
                int d = mgr.getStreamVolume(AudioManager.STREAM_ALARM);
                int set = 0;
                if(choose.equals("1")){
                    if(a - 5 > 0) set = a - 5;
                    mgr.setStreamVolume(AudioManager.STREAM_MUSIC, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("2")){
                    if(b - 5 > 0) set = b - 5;
                    mgr.setStreamVolume(AudioManager.STREAM_RING, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("3")){
                    if(c - 5 > 0) set = c - 5;
                    mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }else if(choose.equals("4")){
                    if(d - 5 > 0) set = d - 5;
                    mgr.setStreamVolume(AudioManager.STREAM_ALARM, set, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                Toast.makeText(this, "Đã thay đổi âm lượng", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == NOTE){
            if(resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                arrList.add(text.get(0));
                adapter.notifyDataSetChanged();
            }
        }else if(requestCode == HOURRING){
            if(resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                hour = text.get(0).trim();
                int a = Integer.parseInt(hour);
                if(0 <= a && a < 10) hour = "0" + hour;
                Toast.makeText(this, "nhập phút (ví dụ: 45)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                startActivityForResult(intent, MINUSRING);
            }
        }else if(requestCode == MINUSRING){
            if(resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                minus = text.get(0).trim();
                int b = Integer.parseInt(minus);
                if(0 <= b && b < 10) hour = "0" + minus;
                Toast.makeText(this, "nhập nội dung", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                startActivityForResult(intent, NOTERING);
            }
        }else if(requestCode == NOTERING){
            if(resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                note = text.get(0).trim();
                Toast.makeText(this, "Đã đặt lịch", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
