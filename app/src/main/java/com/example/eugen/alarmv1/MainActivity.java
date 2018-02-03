package com.example.eugen.alarmv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements DeleteMessageDialog.DeleteMessageDialogListener {
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    public static final String ACTION = "ACTION";
    public static final String UUID = "UUID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ApplicationModel applicationModel = ApplicationModel.getInstance(getApplicationContext());
        List <Alarm> alarms = applicationModel.getAlarms();
        alarmAdapter = new AlarmAdapter(alarms);
        recyclerView.setAdapter(alarmAdapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        //Разделение элементов списка
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AlarmActivity.class);
                intent.putExtra(ACTION,0);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            //Создать диалог about
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Холдер
    private class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView stateTextView;
        private Alarm alarm;
        public AlarmHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list,parent,false));
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void bind(Alarm alarm){
            this.alarm = alarm;
            nameTextView.setText(this.alarm.getName());
            if(this.alarm.getHours()<=9) {
                if(this.alarm.getMinutes()<=9)
                    timeTextView.setText("0" + this.alarm.getHours() + " : 0" + alarm.getMinutes());
                else
                    timeTextView.setText("0" + this.alarm.getHours() + " : " + alarm.getMinutes());
            }
            else{
                if(this.alarm.getMinutes()<=9)
                    timeTextView.setText(this.alarm.getHours() + " : 0" + alarm.getMinutes());
                else
                    timeTextView.setText(this.alarm.getHours() + " : " + alarm.getMinutes());
            }
            if(this.alarm.getState() == 0){
                stateTextView.setText(getString(R.string.off));
            }
            else{
                stateTextView.setText(getString(R.string.on));
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,AlarmActivity.class);
            intent.putExtra(ACTION,1);
            intent.putExtra(UUID,alarm.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            DialogFragment dialogFragment = DeleteMessageDialog.newInstance(alarm.getId());
            dialogFragment.show(getSupportFragmentManager(),"deleteDialog");
            //Удалить информацию о будильнике из системы
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(MainActivity.this,AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,alarm.getId().hashCode(),alarmIntent,0);
            alarmManager.cancel(pendingIntent);
            alarmIntent.putExtra(AlarmActivity.SERVICE,false);
            sendBroadcast(alarmIntent);
            return true;
        }
    }

    //Адаптер
    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder>{
        private List<Alarm> alarms;
        public AlarmAdapter(List<Alarm> alarms){
            this.alarms = alarms;
        }
        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            return new AlarmHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            holder.bind(alarms.get(position));
        }

        @Override
        public int getItemCount() {
            return alarms.size();
        }
        public void setAlarms(List<Alarm> alarms){
            this.alarms = alarms;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationModel applicationModel = ApplicationModel.getInstance(getApplicationContext());
        List <Alarm> alarms = applicationModel.getAlarms();
        alarmAdapter.setAlarms(alarms);
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ApplicationModel applicationModel = ApplicationModel.getInstance(getApplicationContext());
        List <Alarm> alarms = applicationModel.getAlarms();
        alarmAdapter.setAlarms(alarms);
        alarmAdapter.notifyDataSetChanged();
    }

}
