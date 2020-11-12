package com.example.myapplication;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageButton;

import java.io.IOException;


public class SteeringPanel extends AppCompatActivity{

    private boolean ifLightsOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steering_panel);
        Toast.makeText(SteeringPanel.this, "Connected", Toast.LENGTH_SHORT).show();


        final AppCompatImageButton btn_forward = findViewById(R.id.button_forward);

        btn_forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        btn_forward.setColorFilter(getColor(R.color.blueButtonPressed));
                        MainActivity.outputStream.write("forward".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        btn_forward.setColorFilter(getColor(R.color.blueButtonReleased));
                        MainActivity.outputStream.write("stop".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_backward = findViewById(R.id.button_backward);

        btn_backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        btn_backward.setColorFilter(getColor(R.color.blueButtonPressed));
                        MainActivity.outputStream.write("backward".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        btn_backward.setColorFilter(getColor(R.color.blueButtonReleased));
                        MainActivity.outputStream.write("stop".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_left = findViewById(R.id.button_left);

        btn_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    try {
                        btn_left.setColorFilter(getColor(R.color.blueButtonPressed));
                        MainActivity.outputStream.write("left".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        btn_left.setColorFilter(getColor(R.color.blueButtonReleased));
                        MainActivity.outputStream.write("stop".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_right = findViewById(R.id.button_right);

        btn_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    try {
                        btn_right.setColorFilter(getColor(R.color.blueButtonPressed));
                        MainActivity.outputStream.write("right".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        btn_right.setColorFilter(getColor(R.color.blueButtonReleased));
                        MainActivity.outputStream.write("stop".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_liftup = findViewById(R.id.button_liftup);

        btn_liftup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        btn_liftup.setColorFilter(getColor(R.color.redButtonPressed));
                        MainActivity.outputStream.write("lifting".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_liftup.setColorFilter(getColor(R.color.redButtonReleased));
                    try {
                        MainActivity.outputStream.write("stop_lift".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_liftdown = findViewById(R.id.button_liftdown);

        btn_liftdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        btn_liftdown.setColorFilter(getColor(R.color.redButtonPressed));
                        MainActivity.outputStream.write("lowering".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btn_liftdown.setColorFilter(getColor(R.color.redButtonReleased));
                    try {
                        MainActivity.outputStream.write("stop_lift".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        final AppCompatImageButton btn_up = findViewById(R.id.button_up);

        btn_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_up.setColorFilter(getColor(R.color.redButtonPressed));
                    try {
                        MainActivity.outputStream.write("up".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btn_up.setColorFilter(getColor(R.color.redButtonReleased));
                    try {
                        MainActivity.outputStream.write("stop_lift".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return true;
            }
        });

        final AppCompatImageButton btn_down = findViewById(R.id.button_down);

        btn_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_down.setColorFilter(getColor(R.color.redButtonPressed));
                    try {
                        MainActivity.outputStream.write("down".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btn_down.setColorFilter(getColor(R.color.redButtonReleased));
                    try {
                        MainActivity.outputStream.write("stop_lift".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        final AppCompatImageButton btn_disconnect = findViewById(R.id.image_disconnect);

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.btSock.isConnected()) {
                    try {
                        MainActivity.outputStream.write("stop_server".getBytes());
                        MainActivity.btSock.close();
                        Toast.makeText(SteeringPanel.this, "Disconnected", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });

        final Button btn_lights = findViewById(R.id.button_lights);

        btn_lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ifLightsOn) {
                    try {
                        MainActivity.outputStream.write("lights_on".getBytes());
                        ifLightsOn = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        MainActivity.outputStream.write("lights_off".getBytes());
                        ifLightsOn = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });





    }
}