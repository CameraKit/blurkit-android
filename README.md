![BlurKit Header](.repo/blurkit-android-header.png)

BlurKit is an extraordinarily easy to use utility to get live blurring just like on iOS. Built by [Dylan McIntyre](https://github.com/dwillmc).

Proper overview coming soon.

## Download
Gradle:
```groovy
compile 'com.wonderkiln:blurkit:1.0.0'
```

## Usage
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

## To Do (incoming!)
- [ ] `SurfaceView` support
- [ ] Support for use outside of an `Activity` (dialogs, etc.)
- [ ] Enhance retrieval of background content to only include views drawn behind the `BlurLayout`.

## Credits
Dylan McIntyre

## License
