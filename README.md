SimpleMultitouchController.java
===================

I had an hour or so of my time free, so I hacked together this little "lib".

It's a simple and hacky multitouch (dualtouch really) listener.

It can:

- Detect (multiple) singe taps from 1 or 2 fingers.
- Detect longpresses from 1 or 2 fingers.
- Detect swipe direction from 1 or 2 fingers (and report them as "left", "up", "right" or "down".

It can't:

- Detect more than 2 fingers, though it shouldn't be that hard to add.
- Detect pinch or rotate movements.

There's tons of improvements to be made, this was done in an hour or two while waiting for the old lady to get home from work.

The class in question is the SimpleMultitouchController.java.

It's uploaded in an example app since I felt it was a bit overkill to make it into a library project.

to use it, add it to your project and in your Activity (as an example, could just as well be a View object) like this:

```java
private SimpleMultitouchController controller;
    
@Override
public void onCreate(Bundle savedInstanceState) {
    ...
    controller = new SimpleMultitouchController(this);
    ...
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    return controller.onTouchEvent(event);
}
```

to make your activity respond to the actions from the controller, implement the `SimpleMultitouchListener` 
in your activity to gain access to the following:

```java
public void onTap(int tapCount, int numberOfFingers);
public void onLongPress(int numberOfFingers);
public void onSwipe(int direction, int numberOfFingers);
```

Sometimes if you tap with two fingers too far apart it will think you swipe (or did I iron that out?) oh well.

Provided as-is.
Apache 2 license.
