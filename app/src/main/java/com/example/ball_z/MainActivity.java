package com.example.ball_z;



import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;
import com.example.ball_z.BallView;


 class BallView extends View {
    private int ballX, ballY;
    private Paint ballPaint;

    public BallView(Context context) {
        super(context);
        ballPaint = new Paint();
        ballPaint.setColor(Color.RED); // Set the color of the ball
    }

    public void setBallPosition(int x, int y) {
        ballX = x;
        ballY = y;
        invalidate(); // Request a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(ballX, ballY, 20, ballPaint); // Draw the ball at (ballX, ballY) with radius 20
    }
}

public class MainActivity extends AppCompatActivity {

    private View ball;
    private int ballX, ballY;

    private Handler handler = new Handler();
    private boolean isLongPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        try{
            ball =findViewById(R.id.ball);
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
        ballX = ballY = 0;
        Button btnUp = findViewById(R.id.btnUp);
        Button btnDown = findViewById(R.id.btnDown);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBall(0, -10);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBall(0, 10);
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBall(-10, 0);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBall(10, 0);
            }
        });


        // Set long click listeners for buttons
        btnUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                handler.post(new AutoMoveRunnable(0, -50));
                final Handler handler1 = new Handler();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        handler.post(new AutoMoveRunnable(0, 50));
                    }
                };
                int delayMillis = 4000; // Delay in milliseconds
                handler1.postDelayed(runnable1, delayMillis);

                return true;
            }

        });

        btnDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                handler.post(new AutoMoveRunnable(0, 50));
                final Handler handler1 = new Handler();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        handler.post(new AutoMoveRunnable(0, -50));
                    }
                };
                int delayMillis = 4000; // Delay in milliseconds
                handler1.postDelayed(runnable1, delayMillis);
                return true;
            }
        });

        btnLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                handler.post(new AutoMoveRunnable(-50, 0));
                final Handler handler1 = new Handler();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        handler.post(new AutoMoveRunnable(50, 0));
                    }
                };
                int delayMillis = 2000; // Delay in milliseconds
                handler1.postDelayed(runnable1, delayMillis);
                return true;
            }
        });

        btnRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                handler.post(new AutoMoveRunnable(50, 0));
                final Handler handler1 = new Handler();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        handler.post(new AutoMoveRunnable(-50, 0));
                    }
                };
                int delayMillis = 2000; // Delay in milliseconds
                handler1.postDelayed(runnable1, delayMillis);

                return true;
            }
        });

        // Set touch listeners to detect long press release
        btnUp.setOnTouchListener(new LongPressReleaseListener());
        btnDown.setOnTouchListener(new LongPressReleaseListener());
      btnLeft.setOnTouchListener(new LongPressReleaseListener());
        btnRight.setOnTouchListener(new LongPressReleaseListener());
    }

    private class AutoMoveRunnable implements Runnable {
        private int deltaX, deltaY;

        AutoMoveRunnable(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        @Override
        public void run() {
            if (isLongPress) {
                moveBall(deltaX, deltaY);
                handler.postDelayed(this, 100); // Adjust the delay as needed
            }
        }
    }


 private class LongPressReleaseListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isLongPress = false;
                return true;
            }
            return false;
        }
    }

    private void moveBall(int deltaX, int deltaY) {
        ballX += deltaX;
        ballY += deltaY;

        // Ensure the ball stays within the screen boundaries
        ballX = Math.max(0, Math.min(ballX, getWindowManager().getDefaultDisplay().getWidth() - ball.getWidth()));
        ballY = Math.max(0, Math.min(ballY, getWindowManager().getDefaultDisplay().getHeight() - ball.getHeight()));

        ball.setX(ballX);
        ball.setY(ballY);
    }
}
