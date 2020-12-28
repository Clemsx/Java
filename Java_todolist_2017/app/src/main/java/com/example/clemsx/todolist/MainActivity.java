package com.example.clemsx.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clemsx.todolist.db.Task;
import com.example.clemsx.todolist.db.TaskHelper;

import java.util.ArrayList;

class Info {
    public String title;
    public String date;
    public String status;

    public Info(String title, String date, String status) {
        this.title = title;
        this.date = date;
        this.status = status;
    }
}

class InfoAdapter extends ArrayAdapter<Info> {
    public InfoAdapter(Context context, ArrayList<Info> infos) {
        super(context, 0, infos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Info info = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        if (info.status.equalsIgnoreCase("true")) {
            convertView.setBackgroundColor(Color.parseColor("#31b404"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.task_title);
        TextView tvDate = (TextView) convertView.findViewById(R.id.task_date);

        tvTitle.setText(info.title);
        tvDate.setText(info.date);
        return convertView;
    }
}


public class MainActivity extends AppCompatActivity {

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    */

    private static final String TAG = "MainActivity";
    private TaskHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter mAdapter;
    public int taskID;
    public String status_todo = "false";

    public EditText taskEditText;
    public EditText taskEditContent;
    public EditText taskEditDate;
    public boolean error = true;


    public int getTaskID() {
        return taskID;
    }

    public String getStatus_todo() {
        return status_todo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        /*
        SQLiteDatabase database = mHelper.getWritableDatabase();
        mHelper.onUpgrade(database, 2, 3);
        */
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean checkDate(String date) {
        if (date.length() == 10) {
            if (date.charAt(2) != '/') {
                return false;
            } else if (date.charAt(5) != '/') {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean checkNumber(String date){
        if (date.length() == 10) {
            String day;
            String month;
            String year;
            int dayInt;
            int monthInt;
            int yearInt;

            day = date.substring(0, 2);
            month = date.substring(3, 5);
            year = date.substring(6);

            dayInt = Integer.parseInt(day);
            monthInt = Integer.parseInt(month);
            yearInt = Integer.parseInt(year);


            if (date.charAt(3) == '0' && (date.charAt(4) == '0'))
                return false;
            else if (dayInt > 31 || dayInt < 1)
                return false;
            else if (monthInt > 12 || dayInt < 1)
                return false;
            else if (yearInt < 2018)
                return false;
        }else{
            return false;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.action_add_task):
                LayoutInflater factory = LayoutInflater.from(this);
                final View textEntryView = factory.inflate(R.layout.add_todo, null);

                taskEditText = textEntryView.findViewById(R.id.title_todo);
                taskEditContent = textEntryView.findViewById(R.id.content_todo);
                taskEditDate = textEntryView.findViewById(R.id.date_todo);

                taskEditText.setText(null, TextView.BufferType.EDITABLE);
                taskEditContent.setText(null, TextView.BufferType.EDITABLE);
                taskEditDate.setText(null, TextView.BufferType.EDITABLE);

                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add Task")
                        .setView(textEntryView)
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button buttons = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        buttons.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int error = 0;
                                if (taskEditContent.getText().length() == 0) {
                                    taskEditContent.setError("Please set content");
                                    error = 1;
                                }
                                if (taskEditText.getText().length() == 0) {
                                    taskEditText.setError("Please set title");
                                    error = 1;
                                }
                                if (taskEditDate.getText().length() == 0) {
                                    taskEditDate.setError("Please set date");
                                    error = 1;
                                }
                                if (!checkDate(String.valueOf(taskEditDate.getText())) || !checkNumber(String.valueOf(taskEditDate.getText()))) {
                                    taskEditDate.setError("Please set correct date format");
                                    error = 1;
                                }

                                if (error == 0) {
                                    String taskTitle = String.valueOf(taskEditText.getText());
                                    String taskContent = String.valueOf(taskEditContent.getText());
                                    String taskDate = String.valueOf(taskEditDate.getText());

                                    SQLiteDatabase db = mHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();

                                    values.put(Task.TaskEntry.COL_TASK_TITLE, taskTitle);
                                    values.put(Task.TaskEntry.COL_TASK_DESCRIPTION, taskContent);
                                    values.put(Task.TaskEntry.COL_TASK_DATE, taskDate);
                                    values.put(Task.TaskEntry.COL_TASK_STATUS, "false");
                                    db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                    db.close();
                                    updateUI();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateUI() {
        ArrayList<Info> arrayOfInfo = new ArrayList<Info>();
        InfoAdapter iAdapter;

        //ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[]{Task.TaskEntry._ID, Task.TaskEntry.COL_TASK_TITLE, Task.TaskEntry.COL_TASK_DESCRIPTION, Task.TaskEntry.COL_TASK_DATE, Task.TaskEntry.COL_TASK_STATUS},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Info infos = new Info(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_DATE)),
                    cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_STATUS)));
            arrayOfInfo.add(infos);
            /*
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
            */
        }

        if (mAdapter == null) {
            iAdapter = new InfoAdapter(this, arrayOfInfo);
            mTaskListView.setAdapter(iAdapter);
            /*
            mAdapter = new ArrayAdapter(this,R.layout.item_todo, R.id.task_title, taskList);
            mTaskListView.setAdapter(mAdapter);
            */
        } else {
            mAdapter.clear();
            mAdapter.addAll(arrayOfInfo);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        TextView taskTextView2 = (TextView) parent.findViewById(R.id.task_date);
        String task = String.valueOf(taskTextView.getText());
        String date = String.valueOf(taskTextView2.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE, Task.TaskEntry.COL_TASK_TITLE + " = ? AND " + Task.TaskEntry.COL_TASK_DATE + " = ?", new String[]{task, date});
        db.close();
        updateUI();
    }

    public void editTask(View view) {
        final View parent = (View) view.getParent();
        TextView taskTextViewParent = (TextView) parent.findViewById(R.id.task_title);
        TextView taskTextViewParent2 = (TextView) parent.findViewById(R.id.task_date);
        final String task = String.valueOf(taskTextViewParent.getText());
        final String date = String.valueOf(taskTextViewParent2.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String query = "SELECT * FROM tasks WHERE date='" + date + "' AND title='" + task + "';";
        Cursor cursor = db.rawQuery(query, null);

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.edit_todo, null);

        final EditText taskEditText = (EditText) textEntryView.findViewById(R.id.edit_title_todo);
        final EditText taskEditContent = (EditText) textEntryView.findViewById(R.id.edit_content_todo);
        final EditText taskEditDate = (EditText) textEntryView.findViewById(R.id.edit_date_todo);
        final CheckBox checkBox = (CheckBox) textEntryView.findViewById(R.id.edit_status_todo);

        while (cursor.moveToNext()) {
            status_todo = cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_STATUS));
            if (status_todo.equalsIgnoreCase("true")) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            taskID = cursor.getInt(cursor.getColumnIndex(Task.TaskEntry._ID));
            taskEditText.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE)), TextView.BufferType.EDITABLE);
            taskEditContent.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_DESCRIPTION)), TextView.BufferType.EDITABLE);
            taskEditDate.setText(cursor.getString(cursor.getColumnIndex(Task.TaskEntry.COL_TASK_DATE)), TextView.BufferType.EDITABLE);

        }
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Task")
                .setView(textEntryView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button buttons = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int error = 0;
                        if (taskEditContent.getText().length() == 0) {
                            taskEditContent.setError("Please set content");
                            error = 1;
                        }
                        if (taskEditText.getText().length() == 0) {
                            taskEditText.setError("Please set title");
                            error = 1;
                        }
                        if (taskEditDate.getText().length() == 0) {
                            taskEditDate.setError("Please set date");
                            error = 1;
                        }
                        if (!checkDate(String.valueOf(taskEditDate.getText())) || !checkNumber(String.valueOf(taskEditDate.getText()))) {
                            taskEditDate.setError("Please set correct date format");
                            error = 1;
                        }

                        if (error == 0) {
                            if (checkBox.isChecked()) {
                                status_todo = "true";

                            } else {
                                status_todo = "false";
                            }
                            String taskTitle = String.valueOf(taskEditText.getText());
                            String taskContent = String.valueOf(taskEditContent.getText());
                            String taskDate = String.valueOf(taskEditDate.getText());

                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            values.put(Task.TaskEntry.COL_TASK_TITLE, taskTitle);
                            values.put(Task.TaskEntry.COL_TASK_DESCRIPTION, taskContent);
                            values.put(Task.TaskEntry.COL_TASK_DATE, taskDate);
                            values.put(Task.TaskEntry.COL_TASK_STATUS, status_todo);
                            db.updateWithOnConflict(Task.TaskEntry.TABLE, values, Task.TaskEntry._ID + "='" + getTaskID() + "';", null, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            updateUI();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

}
