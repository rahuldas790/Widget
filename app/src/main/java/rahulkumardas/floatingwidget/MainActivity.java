package rahulkumardas.floatingwidget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private List<Item> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }

    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setShortcut(View view) {
        SharedPreferences pref = getSharedPreferences("buttons", MODE_PRIVATE);
        names = new ArrayList<>();
        names.add(new Item(pref.getBoolean(0 + "", true), "Home Activity"));
        names.add(new Item(pref.getBoolean(1 + "", true), "Maps Activity"));
        names.add(new Item(pref.getBoolean(2 + "", true), "Settings Activity"));
        names.add(new Item(pref.getBoolean(3 + "", true), "Abc Activity"));
        names.add(new Item(pref.getBoolean(4 + "", true), "Xyz Activity"));
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Shortcuts");
//        ad.setMessage("Select the shortcuts to be shown in the home screen");
        ad.setAdapter(new ShortcutsAdapter(this, names), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                applyChanges();
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }


    private void applyChanges() {
        SharedPreferences.Editor editor = getSharedPreferences("buttons", MODE_PRIVATE).edit();
        for (int i = 0; i < 5; i++) {
            if (names.get(i).checked)
                editor.putBoolean(i + "", true);
            else
                editor.putBoolean(i + "", false);
        }
        editor.commit();
    }
}

