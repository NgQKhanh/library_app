// MainActivity.java
package com.khanhnq.libraryapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.khanhnq.libraryapp.R;

public class BookSeatActivity extends AppCompatActivity {
    ImageView selectedSeat, btnBack;
    TextView title;
    RelativeLayout map;
    private float dX, dY;
    private int lastAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);

        // Ánh xạ id
        title = findViewById(R.id.toolbarTitle);
        btnBack = findViewById(R.id.back_icon);
        map = findViewById(R.id.map);
        title.setText("Chọn chỗ ngồi");

        setupTableSet(R.id.table_set_1);
        setupTableSet(R.id.table_set_2);
        setupTableSet(R.id.table_set_3);
        setupTableSet(R.id.table_set_4);

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            // Handle click event if needed
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        // Nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setNonAvailable(R.id.table_set_1, "chair3");
        setNonAvailable(R.id.table_set_2, "chair8");
        setNonAvailable(R.id.table_set_3, "chair1");
        setNonAvailable(R.id.table_set_4, "chair6");
        setNonAvailable(R.id.table_set_4, "chair7");
        setNonAvailable(R.id.table_set_4, "chair8");
    }
    private void setupTableSet(int tableSetId) {
        View tableSet = findViewById(tableSetId);
        if (tableSet instanceof ViewGroup) {
            ViewGroup tableSetLayout = (ViewGroup) tableSet;
            for (int i = 0; i < tableSetLayout.getChildCount(); i++) {
                View child = tableSetLayout.getChildAt(i);
                if (child instanceof ImageView) {
                    ImageView chair = (ImageView) child;
                    chair.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectSeat(chair,tableSetId);
                        }
                    });
                }
            }
        }
    }

    public void setNonAvailable(int tableSetId, String chairName) {
        View tableSet = findViewById(tableSetId);
        if (tableSet instanceof ViewGroup) {
            ViewGroup tableSetLayout = (ViewGroup) tableSet;
            for (int i = 0; i < tableSetLayout.getChildCount(); i++) {
                View child = tableSetLayout.getChildAt(i);
                if (child instanceof ImageView) {
                    String resourceName = getResources().getResourceEntryName(child.getId());
                    if (resourceName.equals(chairName)) {
                        ImageView chairView = (ImageView) child;
                        chairView.setColorFilter(Color.GRAY);
                        chairView.setTag("unselectable");
                        break;
                    }
                }
            }
        }
    }

    public void selectSeat(ImageView seat, int tableSetId) {
        if("selectable".equals(seat.getTag())) {
            if (selectedSeat != null) {
                selectedSeat.setColorFilter(Color.parseColor("#03C60B"));
            }
            String tableSetName = getResources().getResourceEntryName(tableSetId);
            String chairName = getResources().getResourceEntryName(seat.getId());
            selectedSeat = seat;
            seat.setColorFilter(Color.parseColor("#FFFF5722"));
        }
    }
}