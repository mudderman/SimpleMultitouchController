SimpleMultitouchController.java
===================

I had an hour or so of my time free, so I hacked together this little "lib".

It's a simple and hacky multitouch (dualtouch really) listener.

It can:

- Detect (multiple) singe taps from 1 or 2 fingers.
- Detect longpresses from 1 or 2 fingers.
- Detect swipe direction from 1 or 2 fingers (and report them as "left", "up", "right" or "bottom".

It can't:

- Detect more than 2 fingers.
- Detect pinch or rotate movements.

There's tons of improvements to be made, this was done in an hour or two while waiting for the old lady to get home from work.

The class in question is the SimpleMultitouchController.java.

It's uploaded in an example app since I felt it was a bit overkill to make it into a library project.

Sometimes if you tap with two fingers too far apart it will think you swipe (or did I iron that out?) oh well.

Provided as-is.
Apache 2 license.
