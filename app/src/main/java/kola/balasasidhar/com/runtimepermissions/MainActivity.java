package kola.balasasidhar.com.runtimepermissions;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        /** check whether permissions are Granted or not to read Contacts  */

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {

            /** If permission is Granted continue to read contacts */
            readContacts();
        } else {
            /** If permission is Not-Granted request for permissions */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    /**
     * Method to read contact details from Phone Book
     */
    private void readContacts() {

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Toast.makeText(MainActivity.this, "" + contactName, Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        /** permissions array holds the requested permissions */

        /** grantResults array holds either ' PERMISSION_GRANTED ' or ' PERMISSION_DENIED ' for the
         *  corresponding permissions
         */

        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /** permission was granted, yay! Do the contacts-related task you need to do. */
                    readContacts();
                } else {
                    /** permission denied, boo! Disable the functionality that depends on this permission. */
                    Toast.makeText(MainActivity.this, "Permissions denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
}
