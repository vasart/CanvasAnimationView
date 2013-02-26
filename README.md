Canvas Animation View
=================

Android project for drawing animations on canvas.


Usage
=====

First you have to add widget `com.wagado.widget.CanvasAnimationView` to
your layout. It extends `ImageView`, so you can specify `src` and `background`
attributes. Then in your activity class create an instance of desired animation
and start it like this:

    canvasAnimationView.setCanvasAnimation(ellipseAnimation);
    canvasAnimationView.startCanvasAnimation();

Have a look at `MainActivity` for demo. 


Including In Your Project
=====

This project will be available as `.jar` library which you can include in the `libs/`
folder of your application.
