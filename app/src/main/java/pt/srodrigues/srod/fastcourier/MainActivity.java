package pt.srodrigues.srod.fastcourier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.IOException;

import pt.srodrigues.srod.fastcourier.db.DBHelper;

public class MainActivity extends AppCompatActivity {
    private SimpleCursorAdapter listAdapter;
    private DBHelper helper;
    private ListView list;
    private Courier courier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        try {
            courier = new Courier();
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText inputField = new EditText(MainActivity.this);

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Add a task")
                                .setMessage("What do you want to do?")
                                .setView(inputField)
                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        final String task = inputField.getText().toString();
                                        if (MainActivity.this.courier.add(task)){
                                            new DBHelper(MainActivity.this).addTask(task);
                                            MainActivity.this.listAdapter.getCursor().requery();
                                        }
                                        updateUI();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateUI();
    }

    private void updateUI() {

        helper = new DBHelper(MainActivity.this);
        final SQLiteDatabase sqlDB = helper.getReadableDatabase();
        final Cursor cursor = sqlDB.query(DBHelper.TABLE, new String[]{DBHelper.Columns._ID, DBHelper.Columns.TASK}, null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(this,R.layout.item_layout,cursor,new String[]{DBHelper.Columns.TASK},new int[]{R.id.taskTextView},0);

        list.setAdapter(listAdapter);

    }
    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();
        if (courier.delete(task)){
            new DBHelper(MainActivity.this).delete(task);
            MainActivity.this.listAdapter.getCursor().requery();
        }
        updateUI();
    }
}
