package de.mytfg.uac.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    TextView license_links;
    TextView contact_links;
    TextView info_version;
    TextView info_commit;
    TextView info_build_date;
    TextView info_install_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        license_links = (TextView) findViewById(R.id.license_links);
        if (license_links != null) {
            license_links.setMovementMethod(LinkMovementMethod.getInstance());
        }
        contact_links = (TextView) findViewById(R.id.contact_links);
        if (contact_links != null) {
            contact_links.setMovementMethod(LinkMovementMethod.getInstance());
        }

        info_version = (TextView) findViewById(R.id.info_version);
        info_commit = (TextView) findViewById(R.id.info_commit);
        info_build_date = (TextView) findViewById(R.id.info_build_date);
        info_install_date = (TextView) findViewById(R.id.info_install_date);

        // Read version from PackageInfo and display in info_* TextViews
        PackageManager manager = getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        info_version.setText(info.versionName);
        String[] version_name_parts = info.versionName.split("-");
        String commit_text = "Commit: " + version_name_parts[3];
        info_commit.setText(commit_text);
        String build_date_raw_string = version_name_parts[2];
        String build_date_text = "Compiliert: " + Utils.getDateTime(Utils.parseDateTime("yyyyMMddHHmmss", build_date_raw_string));
        info_build_date.setText(build_date_text);
        String install_date_text = "Installiert: " + Utils.getDateTime(info.lastUpdateTime);
        info_install_date.setText(install_date_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_MainActivity:
                Intent intent_main = new Intent(this, MainActivity.class);
                startActivity(intent_main);
                return true;
            case R.id.action_InfoActivity:
                Intent intent_info = new Intent(this, InfoActivity.class);
                startActivity(intent_info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
