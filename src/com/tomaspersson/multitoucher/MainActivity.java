package com.tomaspersson.multitoucher;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.tomaspersson.multitoucher.SimpleMultitouchController.SimpleMultitouchListener;

public class MainActivity extends Activity implements SimpleMultitouchListener {
	
	private SimpleMultitouchController controller;
	
	private final String[] directions = {"left", "up", "right", "down"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        controller = new SimpleMultitouchController(this);
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return controller.onTouchEvent(event);
	}

	public void onTap(int tapCount, int numberOfFingers) {
		System.out.println("Tapped " + tapCount + " time(s) with " + numberOfFingers + " finger(s)");
	}

	public void onLongPress(int numberOfFingers) {
		System.out.println("Longpressed with " + numberOfFingers + " finger(s)");
	}

	public void onSwipe(int direction, int numberOfFingers) {
		System.out.println("Swiped " + directions[direction] + " with " + numberOfFingers + " finger(s)");
	}
}