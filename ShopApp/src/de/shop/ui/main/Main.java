package de.shop.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import de.shop.R;

public class Main extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getFragmentManager().beginTransaction()
                            .add(R.id.details, new Startseite())
                            .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
