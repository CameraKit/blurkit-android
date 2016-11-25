![BlurKit Header](.repo/blurkit-android-header.png)

BlurKit is an extraordinarily easy to use utility to get live blurring just like on iOS. Built by [Dylan McIntyre](https://github.com/dwillmc).

![BlurKit Demo](.repo/demo.gif)

## Perfomance

BlurKit is faster than other blurring libraries due to a number of bitmap retrieval and drawing optimizations. We've been logging benchmarks for the basic high-intensity tasks for a 300dp x 100dp BlurView:

| Task                      | BlurKit time       | Avg. time without BlurKit |
| --------------------------| -------------------| -----------------------   |
| Retrieve source bitmap    | 1-2 ms             | 8-25 ms                   |
| Blur and draw to BlurView | 1-2 ms             | 10-50ms                   |

This results in an average work/frame time of 2-4ms, which will be a seamless experience for most users and apps.

## Setup
Add __BlurKit__ to your dependencies block:
```groovy
compile 'com.wonderkiln:blurkit:1.0.0'
```

You also need to add __RenderScript__ to your app module. Add these lines to the `defaultConfig` block of your __build.gradle__.

```groovy
renderscriptTargetApi 24
renderscriptSupportModeEnabled true
```

## Usage
### BlurLayout
Add a `BlurLayout` to your layout just like any other view.

```xml
<com.wonderkiln.blurkit.BlurLayout
    android:id="@+id/blurLayout"
    android:layout_width="150dp"
    android:layout_height="150dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="BlurKit!"
        android:textColor="@android:color/white" />

</com.wonderkiln.blurkit.BlurLayout>
```

The layout background will continuously blur the content behind it. If you know your background content will be somewhat static, you can set the layout `fps` to `0`. At any time you can re-blur the background content by calling `invalidate()` on the `BlurLayout`. 

```xml
<com.wonderkiln.blurkit.BlurLayout xmlns:blurkit="http://schemas.android.com/apk/res-auto"
    android:id="@+id/blurLayout"
    android:layout_width="150dp"
    android:layout_height="150dp"
    blurkit:fps="0" />
```

Other attributes you can configure are the blur radius and the downscale factor. Getting these to work together well can take some experimentation. The downscale factor is a performance optimization; the bitmap for the background content will be downsized by this factor before being drawn and blurred.

```xml
<com.wonderkiln.blurkit.BlurLayout xmlns:blurkit="http://schemas.android.com/apk/res-auto"
    android:id="@+id/blurLayout"
    android:layout_width="150dp"
    android:layout_height="150dp"
    blurkit:blurRadius="12"
    blurkit:downscaleFactor="0.12"
    blurkit:fps="60" />
```

### Other
You can use the `BlurKit` class which has a few useful blurring utilities. Before using this class outside of a `BlurLayout`, you need to initialize `BlurKit`.

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        BlurKit.init(this);
    }
}
```

You can blur a `View`, or a `Bitmap` directly.

```java
// View
BlurKit.blur(View src, int radius);

// Bitmap
BlurKit.blur(Bitmap src, int radius);
```

You can also __fastBlur__ a `View`. This optimizes the view blurring process by allocating a downsized bitmap and using a `Matrix` with the bitmaps `Canvas` to prescale the drawing of the view to the bitmap.

```java
BlurKit.fastBlur(View src, int radius, float downscaleFactor);
```

## To Do (incoming!)
- [ ] `SurfaceView` support
- [ ] Support for use outside of an `Activity` (dialogs, etc.)
- [ ] Enhance retrieval of background content to only include views drawn behind the `BlurLayout`.

## Credits
Dylan McIntyre

## License
