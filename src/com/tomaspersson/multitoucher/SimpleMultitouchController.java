package com.tomaspersson.multitoucher;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 
 * Cans:
 * 		Multiple taps with one or two fingers.
 * 		Longpresses with one or two fingers.
 * 		Swipe direction with one or two fingers.
 * 
 * Can'ts:
 * 		Handle more than 2 fingers.
 * 
 * @author Tomas Persson
 *
 */
public class SimpleMultitouchController {
	
	public interface SimpleMultitouchListener {
		public void onTap(int tapCount, int numberOfFingers);
		public void onLongPress(int numberOfFingers);
		public void onSwipe(int direction, int numberOfFingers);
		
		// Pinch and rotate could possibly be a bit tricky
//		public void onPinch(int direction);
//		public void onRotate(int direction); 
	}
	
	public static final int SWIPE_LEFT = 0;
	public static final int SWIPE_UP = 1;
	public static final int SWIPE_RIGHT = 2;
	public static final int SWIPE_DOWN = 3;
	
	private int tapTimeout = ViewConfiguration.getTapTimeout();
	// Name should probs change since it's more of a "timeout-to-wait-for-consequetive-taps"-timeout
	private int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
	private int longPressTimeout = ViewConfiguration.getLongPressTimeout();
	
	private boolean twoFingers = false;
	private long secondFingerReleaseTime;
	
	private Handler handler;
	private Runnable longPressRunnable;
	private boolean wasLongPress = false;
	
	private Runnable tapRunnable;
	private int tapCount = 0;
	
	private SimpleMultitouchListener listener;
	private float mainFingerDownX;
	private float mainFingerDownY;
	private boolean didSwipe;
	
	public SimpleMultitouchController() {
		handler = new Handler();
		
		longPressRunnable = new Runnable() {
			public void run() {
				wasLongPress = true;
				if (listener != null) {
					listener.onLongPress(twoFingers ? 2 : 1);
				}
			}
		};
		
		tapRunnable = new Runnable() {
			public void run() {
				if (listener != null) {
					listener.onTap(tapCount, twoFingers ? 2 : 1);
				}
				tapCount = 0;
				twoFingers = false;
			}
		};
	}
	
	public SimpleMultitouchController(SimpleMultitouchListener listener) {
		this();
		this.listener = listener;
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		
		if (action == MotionEvent.ACTION_UP) {
			handler.removeCallbacks(longPressRunnable);
			
			if (wasLongPress) {
				return true;
			}
			
			long upTime = event.getEventTime();
			long downTime = twoFingers ? secondFingerReleaseTime : event.getDownTime();
			float upX = event.getX();
			float upY = event.getY();
			
			if (didSwipe) {
				if (listener != null) {
					listener.onSwipe(calculateDirection(mainFingerDownX, mainFingerDownY, upX, upY), twoFingers ? 2 : 1);
				}
				didSwipe = false;
				return true;
				
			} else {
				if (upTime - downTime < tapTimeout) {
					postTapAction();
					return true;
				}
			}
			
		} else if (action == MotionEvent.ACTION_DOWN) {
			twoFingers = false;
			mainFingerDownX = event.getX();
			mainFingerDownY = event.getY();
			postLongPressAction();
			
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (calculateDirection(mainFingerDownX, mainFingerDownY, event.getX(), event.getY()) != -1) {
				didSwipe = true;
	        	handler.removeCallbacks(longPressRunnable);
	        	handler.removeCallbacks(tapRunnable);
			}
		}
		
		if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
			twoFingers = true;
			didSwipe = false;
			postLongPressAction();
			secondFingerReleaseTime = event.getEventTime();
		}
		
		return false;
	}
	
	public void setListener(SimpleMultitouchListener listener) {
		this.listener = listener;
	}
	
	private void postLongPressAction() {
		wasLongPress = false;
		handler.removeCallbacks(longPressRunnable);
		handler.postDelayed(longPressRunnable, longPressTimeout);
	}
	
	private void postTapAction() {
		tapCount++;
		handler.removeCallbacks(tapRunnable);
		handler.postDelayed(tapRunnable, tapCount > 1 ? doubleTapTimeout : tapTimeout);
	}

	private int calculateDirection(float startX, float startY, float endX, float endY) {
        float dx = endX - startX;
        float dy = endY - startY;
        
        if ((dx > 100 || dx < -100) || (dy > 100 || dy < -100)) {
        	if (Math.abs(dx) > Math.abs(dy)) {
        		return dx > 0 ? SWIPE_RIGHT : SWIPE_LEFT;
        	} else {
        		return dy > 0 ? SWIPE_DOWN : SWIPE_UP;
        	}
        } else {
        	return -1;
        }
	}
}
