package com.example.speechapplication;
import android.Manifest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener
{

    Button btSpeak,btSpeech;
    TextView tvSpeak,tv;
    EditText etSpeech;
    TextToSpeech tts;

    private static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSpeak = findViewById(R.id.btSpeak);
        etSpeech = findViewById(R.id.etSpeech);
        btSpeech = findViewById(R.id.btSpeech);
        tvSpeak = findViewById(R.id.tvSpeak);
        tv = findViewById(R.id.tv);

        tts = new TextToSpeech(getApplicationContext(),this);
        tts.setSpeechRate(1f);
        tts.setPitch(9f);


        btSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = etSpeech.getText().toString();

                if(msg.equals(""))
                {
                    tts.speak("BAHILU KAIK TOH LUKH",TextToSpeech.QUEUE_ADD,null,null);
                }
                else
                {
                    tts.speak(msg,TextToSpeech.QUEUE_ADD,null,null);
                }
            }
        });

        btSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                ii.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(ii,555);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        }
        else
        {
            readContacts();
        }


    }

    private void makeCallWithContactId(String contactId) {

        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );
    }
    private void readContacts() {

        String[] projection = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
        StringBuilder contactsBuilder = new StringBuilder();

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                String contactName = cursor.getString(nameIndex);
                String contactId = cursor.getString(idIndex);
                contactsBuilder.append("Name: ").append(contactName).append(", ID: ").append(contactId).append("\n");

                Log.d("Contact", "Name: " + contactName + ", ID: " + contactId);
            } while (cursor.moveToNext());
            cursor.close(); // Close the cursor to release resources
            tv.setText(contactsBuilder.toString());
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Bye",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int req,int res,Intent data)
    {
        if(req==555 && res == RESULT_OK)
        {
            ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String speech="";


            for(int i=0;i<list.size();i++)
            {
                speech = speech + list.get(i)+"\n";
            }

            tvSpeak.setText(speech);
            speech = speech.toLowerCase();

            if(speech.contains("open youtube"))
            {
                Intent ii = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com"));
                startActivity(ii);
            }
            else if(speech.contains("yash"))
            {
                Intent ii = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"9173830752"));
                startActivity(ii);
            }
            else if(speech.contains("myntra"))
            {
                Intent ii = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"8320267829"));
                startActivity(ii);
            }
            else if (speech.contains("contact")) {
                // Check permission to read contacts
            }

        }


    }

    @Override
    public void onInit(int status) {



    }
}