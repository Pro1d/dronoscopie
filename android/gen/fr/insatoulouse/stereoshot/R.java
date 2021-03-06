/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package fr.insatoulouse.stereoshot;

public final class R {
    public static final class attr {
        /** <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>circle</code></td><td>0</td><td></td></tr>
<tr><td><code>rect</code></td><td>1</td><td></td></tr>
</table>
         */
        public static final int bound=0x7f010000;
        /** <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int boundSize=0x7f010005;
        /** <p>May be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
<p>May be a color value, in the form of "<code>#<i>rgb</i></code>", "<code>#<i>argb</i></code>",
"<code>#<i>rrggbb</i></code>", or "<code>#<i>aarrggbb</i></code>".
         */
        public static final int centerDrawable=0x7f010002;
        /** <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>fixed</code></td><td>0</td><td></td></tr>
<tr><td><code>dynamic</code></td><td>1</td><td></td></tr>
<tr><td><code>follow</code></td><td>2</td><td></td></tr>
</table>
         */
        public static final int position=0x7f010001;
        /** <p>May be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
<p>May be a color value, in the form of "<code>#<i>rgb</i></code>", "<code>#<i>argb</i></code>",
"<code>#<i>rrggbb</i></code>", or "<code>#<i>aarrggbb</i></code>".
         */
        public static final int stickDrawable=0x7f010003;
        /** <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int stickRadius=0x7f010004;
    }
    public static final class drawable {
        public static final int ic_launcher=0x7f020000;
    }
    public static final class id {
        public static final int b_capture=0x7f060008;
        public static final int buttonDown=0x7f060013;
        public static final int buttonLeft=0x7f060011;
        public static final int buttonRight=0x7f060012;
        public static final int buttonUp=0x7f060010;
        public static final int button_send=0x7f06000c;
        public static final int buttonsContainer=0x7f06000e;
        public static final int circle=0x7f060000;
        public static final int dynamic=0x7f060002;
        public static final int fixed=0x7f060003;
        public static final int follow=0x7f060004;
        public static final int joystickView=0x7f06000d;
        public static final int radioGroup1=0x7f060009;
        public static final int rect=0x7f060001;
        public static final int send_feedback=0x7f06000a;
        public static final int send_text=0x7f06000b;
        public static final int start_service=0x7f060006;
        public static final int surfaceViewCamera=0x7f060005;
        public static final int textOutput=0x7f06000f;
        public static final int tv_controller=0x7f060007;
    }
    public static final class layout {
        public static final int activity_camera=0x7f030000;
        public static final int activity_controller=0x7f030001;
        public static final int activity_game_listener_sample=0x7f030002;
        public static final int activity_gamepad_sample=0x7f030003;
    }
    public static final class string {
        public static final int action_settings=0x7f040003;
        public static final int app_name=0x7f040001;
        public static final int hello_world=0x7f040002;
        public static final int json=0x7f040000;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.
    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.
        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.
    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f050000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f050001;
    }
    public static final class styleable {
        /** Attributes that can be used with a Joystick.
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #Joystick_bound fr.insatoulouse.stereoshot:bound}</code></td><td></td></tr>
           <tr><td><code>{@link #Joystick_boundSize fr.insatoulouse.stereoshot:boundSize}</code></td><td></td></tr>
           <tr><td><code>{@link #Joystick_centerDrawable fr.insatoulouse.stereoshot:centerDrawable}</code></td><td></td></tr>
           <tr><td><code>{@link #Joystick_position fr.insatoulouse.stereoshot:position}</code></td><td></td></tr>
           <tr><td><code>{@link #Joystick_stickDrawable fr.insatoulouse.stereoshot:stickDrawable}</code></td><td></td></tr>
           <tr><td><code>{@link #Joystick_stickRadius fr.insatoulouse.stereoshot:stickRadius}</code></td><td></td></tr>
           </table>
           @see #Joystick_bound
           @see #Joystick_boundSize
           @see #Joystick_centerDrawable
           @see #Joystick_position
           @see #Joystick_stickDrawable
           @see #Joystick_stickRadius
         */
        public static final int[] Joystick = {
            0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003,
            0x7f010004, 0x7f010005
        };
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#bound}
          attribute's value can be found in the {@link #Joystick} array.


          <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>circle</code></td><td>0</td><td></td></tr>
<tr><td><code>rect</code></td><td>1</td><td></td></tr>
</table>
          @attr name fr.insatoulouse.stereoshot:bound
        */
        public static final int Joystick_bound = 0;
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#boundSize}
          attribute's value can be found in the {@link #Joystick} array.


          <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name fr.insatoulouse.stereoshot:boundSize
        */
        public static final int Joystick_boundSize = 5;
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#centerDrawable}
          attribute's value can be found in the {@link #Joystick} array.


          <p>May be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
<p>May be a color value, in the form of "<code>#<i>rgb</i></code>", "<code>#<i>argb</i></code>",
"<code>#<i>rrggbb</i></code>", or "<code>#<i>aarrggbb</i></code>".
          @attr name fr.insatoulouse.stereoshot:centerDrawable
        */
        public static final int Joystick_centerDrawable = 2;
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#position}
          attribute's value can be found in the {@link #Joystick} array.


          <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>fixed</code></td><td>0</td><td></td></tr>
<tr><td><code>dynamic</code></td><td>1</td><td></td></tr>
<tr><td><code>follow</code></td><td>2</td><td></td></tr>
</table>
          @attr name fr.insatoulouse.stereoshot:position
        */
        public static final int Joystick_position = 1;
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#stickDrawable}
          attribute's value can be found in the {@link #Joystick} array.


          <p>May be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
<p>May be a color value, in the form of "<code>#<i>rgb</i></code>", "<code>#<i>argb</i></code>",
"<code>#<i>rrggbb</i></code>", or "<code>#<i>aarrggbb</i></code>".
          @attr name fr.insatoulouse.stereoshot:stickDrawable
        */
        public static final int Joystick_stickDrawable = 3;
        /**
          <p>This symbol is the offset where the {@link fr.insatoulouse.stereoshot.R.attr#stickRadius}
          attribute's value can be found in the {@link #Joystick} array.


          <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name fr.insatoulouse.stereoshot:stickRadius
        */
        public static final int Joystick_stickRadius = 4;
    };
}
