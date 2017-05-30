package com.almondmendoza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.floor;


public class ColorActivity extends Activity implements OnClickListener {

    private boolean clickedMove = false;
    ArrayList<ColorModelBelow> SelectColors;
    ArrayList<ColorModelBelow> SelectColorshalf;
    ArrayList<ColorModelBelow> gradientcolors = new ArrayList<>();
    ArrayList<ColorModelBelow> gradientcolorsMain = new ArrayList<>();
    ArrayList<ColorModelBelow> gradientcolors2 = new ArrayList<>();
    public static int pos = -1;
    boolean undo = true;
    HorizontalAdapter adapter;
    int[] f2121p;

    Bitmap mBitmap;
    ArrayList<ColorModel> colors;
    ArrayList<ColorModel> colors2;
    ArrayList<Point> pts = new ArrayList<>();
    String Drag = "none";
    public static String[] abn = new String[]{"Red", "Maroon", "Yellow", "Olive", "Lime", "Green", "Cyan", "Teal", "Blue", "Navy", "Magenta", "Purple", "remove", "remove", "Orange", "Silver",
            "Gray",
            "White",
            "Black"
    };

    RecyclerView circle_recyclerView, circle_recyclerView2;

    RecyclerView recyclerView;
    TextView txt_home, txt_next;
    ImageView click_button;

    private RelativeLayout drawingLayout;
    private MyView myView;
    Paint paint;

    public boolean isFillModeEnabled = true;
    public int imageWidth;
    public int imageHeight;

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mLastTouchX;
    private float mLastTouchY;
    public float mPosX = 1;
    public float mPosY = 1;
    Rect rect;
    Animation animation;
    Animation animation2;

    HorizontalCircleAdapter2 adapter2;
    HorizontalCircleAdapter circleAdapter;
    private int mImageWidthScaled;
    private int mImageHeightScaled;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.draw_imagelayout);
        this.f2121p = new int[1];
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        circle_recyclerView = (RecyclerView) findViewById(R.id.circle_recyclerView);
        circle_recyclerView2 = (RecyclerView) findViewById(R.id.circle_recyclerView2);

        animation = AnimationUtils.loadAnimation(ColorActivity.this, R.anim.slide_in_left_w);
        animation2 = AnimationUtils.loadAnimation(ColorActivity.this, R.anim.slide_in_right_w);


        myView = new MyView(this);
        drawingLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        drawingLayout.addView(myView);

        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_next = (TextView) findViewById(R.id.txt_next);
        click_button = (ImageView) findViewById(R.id.click_button);


        //coringImage = (ImageView) findViewById(R.id.coringImage);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ColorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        // RecyclerView.LayoutManager circleLayoutManagaer = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        LinearLayoutManager circleLayoutManagaer = new LinearLayoutManager(ColorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        circle_recyclerView.setLayoutManager(circleLayoutManagaer);

        LinearLayoutManager circleLayoutManagaer2 = new LinearLayoutManager(ColorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        circle_recyclerView2.setLayoutManager(circleLayoutManagaer2);

        txt_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ColorActivity.this, FinalizeActivity.class);
//                startActivity(intent);
            }
        });


        click_button.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (clickedMove)
                                                    clickedMove = false;
                                                else
                                                    clickedMove = true;
                                            }
                                        }
        );

        getColors();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Implements onPause().
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Implements onResume().
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Implements onClick().
     */
    @Override
    public void onClick(View view) {

    }


    public void getColors() {
        colors = HSVColors();
        adapter = new HorizontalAdapter(colors);
        recyclerView.setAdapter(adapter);
    }

    // Custom method to generate hsv colors list
    public static ArrayList<ColorModel> HSVColors() {
        ArrayList<ColorModel> colors = new ArrayList<>();
        for (int h = 0; h <= 360; h += 60) {
            ColorModel model66 = new ColorModel();
            model66.setHue(h);
            model66.setSaturation(1);
            model66.setValue(1);
            model66.setColor(HSVColor(h, 1, 1));
            model66.setColorname("Blues");
            colors.add(model66);

            ColorModel model = new ColorModel();
            model.setHue(h);
            model.setSaturation(1);
            model.setValue(50);
            model.setColor(HSVColor(h, 1, .5f));
            model.setColorname("Blues");
            colors.add(model);

            // colors.add(HSVColor(h, 1, 1));
        }

        ColorModel model37 = new ColorModel();
        model37.setHue(33);
        model37.setSaturation(1);
        model37.setValue(1);
        model37.setColor(HSVColor(.33f, 1, 1));
        model37.setColorname("Orange");
        colors.add(model37);


        ColorModel model3 = new ColorModel();
        model3.setHue(0);
        model3.setSaturation(0);
        model3.setValue(50);
        model3.setColor(HSVColor(0, 0, .75f));
        model3.setColorname("Silver");
        colors.add(model3);

        ColorModel model8 = new ColorModel();
        model8.setHue(0);
        model8.setSaturation(0);
        model8.setValue(50);
        model8.setColor(HSVColor(0, 0, .50f));
        model8.setColorname("Gray");
        colors.add(model8);

        ColorModel model1 = new ColorModel();
        model1.setHue(0);
        model1.setSaturation(0);
        model1.setValue(0);
        model1.setColor(HSVColor(0, 0, 0));
        model1.setColorname("Blues");
        colors.add(model1);
        List<Integer> postions = new ArrayList<>();

        for (int h = 0; h < colors.size(); h++) {
            Log.e("color", "\n" + abn[h]);
            if (abn[h].equalsIgnoreCase("remove"))
                postions.add(h);
            else
                colors.get(h).setColorname(abn[h]);
        }

        for (int h = 0; h < postions.size(); h++) {
            int pos = postions.get(h);
            colors.remove(pos);
        }
        colors.remove(12);
        return colors;
    }

    void getGradientColor() {
        gradientcolors.clear();
        for (int i = 0; i < SelectColorshalf.size(); i++) {
            ColorModelBelow modelBelow = new ColorModelBelow();
            if (i == SelectColorshalf.size() - 1) {
                modelBelow.setColor(SelectColors.get(i - 1).getColor());
                modelBelow.setEndcolor(SelectColorshalf.get(i).getColor());
                gradientcolors.add(modelBelow);
            } else {
                modelBelow.setColor(SelectColors.get(i).getColor());
                modelBelow.setEndcolor(SelectColorshalf.get(i).getColor());
                gradientcolors.add(modelBelow);
            }
        }
    }

    void getGradientColorMain() {
        gradientcolorsMain.clear();
        colors2 = HSVColors();
        for (int i = 0; i < colors2.size(); i++) {

            ColorModelBelow modelBelow = new ColorModelBelow();
            modelBelow.setColor(shadeColor(colors2.get(i).getColor(), 0.5));
            modelBelow.setEndcolor(colors2.get(i).getColor());
            gradientcolorsMain.add(modelBelow);
        }
    }

    void getGradientColorTwo() {
        gradientcolors2.clear();
        for (int i = 0; i < SelectColors.size(); i++) {
            ColorModelBelow modelBelow = new ColorModelBelow();
            modelBelow.setColor(SelectColorshalf.get(i).getColor());
            modelBelow.setEndcolor(SelectColors.get(i).getColor());
            gradientcolors2.add(modelBelow);
        }
    }

    private int shadeColor(@ColorInt int color, double percent) {
        String hex = String.format("#%06X", (0xFFFFFF & color));
        long f = Long.parseLong(hex.substring(1), 16);
        double t = percent < 0 ? 0 : 255;
        double p = percent < 0 ? percent * -1 : percent;
        long R = f >> 16;
        long G = f >> 8 & 0x00FF;
        long B = f & 0x0000FF;
        int alpha = Color.alpha(color);
        int red = (int) (Math.round((t - R) * p) + R);
        int green = (int) (Math.round((t - G) * p) + G);
        int blue = (int) (Math.round((t - B) * p) + B);
        return Color.argb(alpha, red, green, blue);
    }

    private ArrayList<ColorModelBelow> getColorShades(@ColorInt int color) {
        ArrayList<ColorModelBelow> colors = new ArrayList<>();

        ColorModelBelow model = new ColorModelBelow();
        model.setColor(shadeColor(color, 0.9));
        model.setSelected(false);
        colors.add(model);

        ColorModelBelow model1 = new ColorModelBelow();
        model1.setColor(shadeColor(color, 0.7));
        model1.setSelected(false);
        colors.add(model1);

        ColorModelBelow model2 = new ColorModelBelow();
        model2.setColor(shadeColor(color, 0.5));
        model2.setSelected(false);
        colors.add(model2);

        ColorModelBelow model3 = new ColorModelBelow();
        model3.setColor(shadeColor(color, 0.333));
        model3.setSelected(false);
        colors.add(model3);

        ColorModelBelow model4 = new ColorModelBelow();
        model4.setColor(shadeColor(color, 0.233));
        model4.setSelected(false);
        colors.add(model4);

        SelectColorshalf = new ArrayList<>();

        ColorModelBelow model5 = new ColorModelBelow();
        model5.setColor(shadeColor(color, 0.166));
        model5.setSelected(false);
        SelectColorshalf.add(model5);

        ColorModelBelow model6 = new ColorModelBelow();
        model6.setColor(shadeColor(color, -0.166));
        model6.setSelected(false);
        SelectColorshalf.add(model6);
//
        ColorModelBelow model7 = new ColorModelBelow();
        model7.setColor(shadeColor(color, -0.125));
        model7.setSelected(false);
        SelectColorshalf.add(model7);
//ghhhhhhhhhhhhhhhh
        ColorModelBelow model15 = new ColorModelBelow();
        model15.setColor(shadeColor(color, -0.325));
        model15.setSelected(false);
        SelectColorshalf.add(model15);


        ColorModelBelow model16 = new ColorModelBelow();
        model16.setColor(shadeColor(color, -0.525));
        model16.setSelected(false);
        SelectColorshalf.add(model16);
//
        ColorModelBelow model17 = new ColorModelBelow();
        model17.setColor(shadeColor(color, -0.5));
        model17.setSelected(false);
        SelectColorshalf.add(model17);

        return colors;
    }


    public static ArrayList HSVSColors() {
        ArrayList<Integer> colors = new ArrayList<>();

        return colors;
    }

    // Create HSV color from values
    public static int HSVColor(float hue, float saturation, float black) {

        return Color.HSVToColor(255, new float[]{hue, saturation, black});
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtView;
            public ImageView image;

            MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.txtView);
                txtView.setVisibility(View.VISIBLE);
                image = (ImageView) view.findViewById(R.id.image);
                image.setVisibility(View.GONE);
            }
        }

        HorizontalAdapter(ArrayList<ColorModel> horizontalList) {
            colors = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            if (position == 0) {
                // paint.setColor(colors.get(position).getColor());
                SelectColors = getColorShades(colors.get(position).getColor());

                getColorShades(colors.get(position).getColor());
                circleAdapter = new HorizontalCircleAdapter(SelectColors);
                circle_recyclerView.setAdapter(circleAdapter);

                adapter2 = new HorizontalCircleAdapter2(SelectColorshalf);
                circle_recyclerView2.setAdapter(adapter2);

                circle_recyclerView.startAnimation(animation);
                circle_recyclerView2.startAnimation(animation2);
            }
            ColorModel currentColor = colors.get(position);
            holder.txtView.setBackgroundColor(currentColor.getColor());
            holder.txtView.setTag(position);
            Log.e("color ", currentColor.getColorname() + " pos0--" + position);
            holder.txtView.setText(currentColor.getColorname());

            holder.txtView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    paint.setColor(colors.get(pos).getColor());
                    SelectColors = getColorShades(colors.get(pos).getColor());
                    circleAdapter = new HorizontalCircleAdapter(SelectColors);
                    circle_recyclerView.setAdapter(circleAdapter);

                    adapter2 = new HorizontalCircleAdapter2(SelectColorshalf);
                    circle_recyclerView2.setAdapter(adapter2);

                    circle_recyclerView.startAnimation(animation);
                    circle_recyclerView2.startAnimation(animation2);

                    circleAdapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }

    }

    public class HorizontalCircleAdapter extends RecyclerView.Adapter<HorizontalCircleAdapter.MyViewHolder> {
        private ArrayList<ColorModelBelow> colors;
        private ArrayList<Shader> colorsG;
        String shade = "no";

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtView;
            ImageView image, image_select;
            RelativeLayout circleLayout;

            MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.txtView);
                image = (ImageView) view.findViewById(R.id.image);
                image_select = (ImageView) view.findViewById(R.id.image_select);
                circleLayout = (RelativeLayout) view.findViewById(R.id.circleLayout);
                image.setPadding(10, 10, 10, 10);
            }
        }

        HorizontalCircleAdapter(ArrayList<ColorModelBelow> horizontalList) {
            this.colors = horizontalList;
        }

        HorizontalCircleAdapter(ArrayList<Shader> horizontalList, String shader) {
            this.colorsG = horizontalList;
            shade = shader;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (shade.equalsIgnoreCase("no")) {
                if (colors.get(position).isSelected() && pos == 1)
                    holder.image_select.setVisibility(View.VISIBLE);
                else
                    holder.image_select.setVisibility(View.GONE);
                int currentColor = colors.get(position).getColor();
                // holder.image.setBackgroundColor(currentColor);
                holder.image.setTag(position);
                GradientDrawable gd = (GradientDrawable) holder.image.getBackground();
                gd.setColor(currentColor);
                Log.e("holder.image.width", "" + holder.image.getWidth());

                //setAnimation(holder.image, position);

                holder.image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag();
                        ColorActivity.pos = 1;
                        paint.setColor(colors.get(pos).getColor());
                        for (int h = 0; h < colors.size(); h++) {
                            if (colors.get(h).isSelected())
                                colors.get(h).setSelected(false);
                        }
                        colors.get(pos).setSelected(true);
                        notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            if (shade.equalsIgnoreCase("no")) {
                return colors.size();
            } else {
                return colorsG.size();
            }
        }
    }

    public class HorizontalCircleAdapter2 extends RecyclerView.Adapter<HorizontalCircleAdapter2.MyViewHolder> {
        private ArrayList<ColorModelBelow> colors;
        private ArrayList<Shader> colorsG;
        String shade = "no";

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtView;
            ImageView image, image_select;
            RelativeLayout circleLayout;

            MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.txtView);
                image = (ImageView) view.findViewById(R.id.image);
                image_select = (ImageView) view.findViewById(R.id.image_select);
                circleLayout = (RelativeLayout) view.findViewById(R.id.circleLayout);
                image.setPadding(10, 10, 10, 10);
            }
        }

        HorizontalCircleAdapter2(ArrayList<ColorModelBelow> horizontalList) {
            this.colors = horizontalList;
        }

        HorizontalCircleAdapter2(ArrayList<Shader> horizontalList, String shader) {
            this.colorsG = horizontalList;
            shade = shader;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (shade.equalsIgnoreCase("no")) {
                if (colors.get(position).isSelected() && pos == 2)
                    holder.image_select.setVisibility(View.VISIBLE);
                else
                    holder.image_select.setVisibility(View.GONE);
                int currentColor = colors.get(position).getColor();
                // holder.image.setBackgroundColor(currentColor);
                holder.image.setTag(position);
                GradientDrawable gd = (GradientDrawable) holder.image.getBackground();
                gd.setColor(currentColor);
                Log.e("holder.image.width", "" + holder.image.getWidth());

                //setAnimation(holder.image, position);

                holder.image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag();
                        ColorActivity.pos = 2;
                        paint.setColor(colors.get(pos).getColor());
                        for (int h = 0; h < colors.size(); h++) {
                            if (colors.get(h).isSelected())
                                colors.get(h).setSelected(false);
                        }
                        colors.get(pos).setSelected(true);
                        notifyDataSetChanged();
                        circleAdapter.notifyDataSetChanged();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            if (shade.equalsIgnoreCase("no")) {
                return colors.size();
            } else {
                return colorsG.size();
            }
        }
    }

    public class MyView extends View {


        final Point p1 = new Point();
        Canvas canvas;


        // Bitmap mutableBitmap ;
        public MyView(Context context) {
            super(context);
            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setDither(true);
            paint.setColor(Color.parseColor("#a6f914"));
            paint.setAntiAlias(true);

            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ffffffffffffffffffff).copy(Bitmap.Config.ARGB_8888, true);

            //  path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas1) {
            Log.e("onDraw", "==========Canvas===========");
            this.canvas = canvas1;
            canvas.translate(mPosX, mPosY);
            canvas.scale(mScaleFactor, mScaleFactor);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            //canvas.drawPath(path, paint);
        }


        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            boolean color_image = false;
            // Let the ScaleGestureDetector inspect all events.
            mScaleDetector.onTouchEvent(ev);
            rect = canvas.getClipBounds();
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    Drag = "down";
                    Log.e("MotionEvent", "==========ACTION_DOWN===========");
                    final float x = ev.getX();
                    final float y = ev.getY();
                    Log.e("MotionEvent", "==========ACTION_DOWN===" + x + "====" + y);
                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_POINTER_DOWN:
                    color_image = false;
                    isFillModeEnabled = false;
                    break;

                case MotionEvent.ACTION_MOVE: {
                    color_image = false;
                    Drag = "move";
                    Log.e("MotionEvent", "==========ACTION_MOVE===========");
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);
                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!clickedMove) {

                        if (!mScaleDetector.isInProgress()) {
                            isFillModeEnabled = true;
                            final float dx = x - mLastTouchX;
                            final float dy = y - mLastTouchY;

                            mPosX += dx;
                            mPosY += dy;

                            invalidate();

                        } else {
                            isFillModeEnabled = false;
                        }
                    }

                    mLastTouchX = x;
                    mLastTouchY = y;

                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Log.e("MotionEvent", "==========ACTION_UP===========");
                    setFocusable(false);
                    Log.e("ACTION_UP", "===============imageWidth==========" + imageWidth);
                    Log.e("ACTION_UP", "============imageHeight===========" + imageHeight);
                    // if (down) {
                    mImageWidthScaled = (int) (imageWidth * mScaleFactor);
                    mImageHeightScaled = (int) (imageHeight * mScaleFactor);

                    mActivePointerId = INVALID_POINTER_ID;
                    mActivePointerId = INVALID_POINTER_ID;

                    float mx = ev.getX() / mScaleFactor + rect.left;
                    float my = ev.getY() / mScaleFactor + rect.top;
                    rect = canvas.getClipBounds();
                    //  this.isFillEnabled = true;
                    Log.e("MotionEvent", "==========ACTION_UP===" + mx + "====" + my);
                    // fillHandler(mx, my, mImageWidthScaled, mImageHeightScaled);
                    p1.x = (int) mx;
                    p1.y = (int) my;
                    if (isFillModeEnabled) {
                        if (mBitmap.getHeight() > my && mBitmap.getWidth() > mx) {
                            if (my >= 0 && mx >= 0) {

//                                Shader shader = new LinearGradient(p1.x, p1.y, p1.x + 10, p1.y + 10, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
//                                paint.setShader(shader);
                                //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                                final int sourceColor = mBitmap.getPixel((int) mx, (int) my);

                                int targetColor = paint.getColor();


                                // targetColor = p1.x * (Color.RED - Color.GREEN) + Color.GREEN;
                                // targetColor = (int) (floor(targetColor * 10.0) * 0.1);
                                new TheTask(mBitmap, p1, sourceColor, targetColor).execute();
                                invalidate();
                                //drawBitmap(mBitmap, pts);
                            }
                            color_image = false;
                        }

                    }
                    Drag = "none";

                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    color_image = false;

                    Log.e("MotionEvent", "==========ACTION_CANCEL===========");
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    color_image = false;

                    isFillModeEnabled = false;
                    Log.e("MotionEvent", "==========ACTION_POINTER_UP===========");
                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = ev.getX(newPointerIndex);
                        mLastTouchY = ev.getY(newPointerIndex);
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
            return true;
        }


        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
                invalidate();
                return true;
            }
        }

        class TheTask extends AsyncTask<Void, Integer, Void> {

            Bitmap bmp;
            Point pt;
            int replacementColor, targetColor;


            TheTask(Bitmap bm, Point p, int sc, int tc) {
                this.bmp = bm;
                this.pt = p;
                this.replacementColor = tc;
                this.targetColor = sc;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onProgressUpdate(Integer... values) {

            }

            @Override
            protected Void doInBackground(Void... params) {
                floodFill(bmp, pt, targetColor, replacementColor);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                invalidate();
            }
        }

    }

    void fillTwoColor(Bitmap bitmap) {
        ArrayList<Point> pts1 = new ArrayList<>();
        ArrayList<Point> pts2 = new ArrayList<>();
        for (int i = 0; i < pts.size(); i++) {
            if (pts.size() < i + 1) {
                if (pts.get(i).x < pts.get(i + 1).x) {
                    pts1.add(pts.get(i));
                } else {
                    pts1.add(pts.get(i + 1));
                }
            }
        }
        int size = pts.size();
        size = size / 2;
        for (int i = 0; i < size; i++) {
            Log.e("Here", "Blue " + pts1.get(i).x);
            pts2.add(pts1.get(i));
            bitmap.setPixel(pts1.get(i).x, pts1.get(i).y, Color.BLUE);
        }
    }


    private void floodFill(Bitmap bitmap, Point point, int i, int j) {
        int k = bitmap.getWidth();
        int l = bitmap.getHeight();

        pts.clear();
        if (i == j) {
            j = Color.WHITE;
        }
        if (i != j) {
            LinkedList linkedlist = new LinkedList();

            do {

                int i1 = point.x;
                int j1;
                for (j1 = point.y; i1 > 0 && !isBlack(bitmap.getPixel(i1 - 1, j1), j); i1--) {
                }
                boolean flag = false;
                boolean flag1 = false;
                while (i1 < k && !isBlack(bitmap.getPixel(i1, j1), j)) {//
                    bitmap.setPixel(i1, j1, j);

                    if (!flag && j1 > 0 && !isBlack(bitmap.getPixel(i1, j1 - 1), j)) {
                        linkedlist.add(new Point(i1, j1 - 1));

                        flag = true;
                    } else if (flag && j1 > 0 && isBlack(bitmap.getPixel(i1, j1 - 1), j)) {
                        flag = false;
                    }
                    if (!flag1 && j1 < l - 1 && !isBlack(bitmap.getPixel(i1, j1 + 1), j)) {


                        linkedlist.add(new Point(i1, j1 + 1));
                        flag1 = true;
                    } else if (flag1 && j1 < l - 1 && isBlack(bitmap.getPixel(i1, j1 + 1), j)) {
                        flag1 = false;
                    }
                    i1++;
                }
                point = (Point) linkedlist.poll();
                pts.add(point);
                //Log.e("linkedlist","---"+point.x+"   "+point.y);
            } while (point != null);


        }
    }


    private void floodFill2(Bitmap bitmap, Point point, int i, int j) {
        int k = bitmap.getWidth();
        int l = bitmap.getHeight();

        pts.clear();
        if (i == j) {
            j = Color.WHITE;
        }
        if (i != j) {
            LinkedList linkedlist = new LinkedList();
            do {

                int i1 = point.x;
                int j1;
                for (j1 = point.y; i1 > 0 && !isBlack(bitmap.getPixel(i1 - 1, j1), j); i1--) {
                }
                boolean flag = false;
                boolean flag1 = false;
                while (i1 < k && !isBlack(bitmap.getPixel(i1, j1), j)) {
                    bitmap.setPixel(i1, j1, j);

                    if (!flag && j1 > 0 && !isBlack(bitmap.getPixel(i1, j1 - 1), j)) {
                        linkedlist.add(new Point(i1, j1 - 1));

                        flag = true;
                    } else if (flag && j1 > 0 && isBlack(bitmap.getPixel(i1, j1 - 1), j)) {
                        flag = false;
                    }
//                    if (!flag1 && j1 < l - 1 && !isBlack(bitmap.getPixel(i1, j1 + 1), j)) {
//
//
//                        linkedlist.add(new Point(i1, j1 + 1));
//                        flag1 = true;
//                    } else if (flag1 && j1 < l - 1 && isBlack(bitmap.getPixel(i1, j1 + 1), j)) {
//                        flag1 = false;
//                    }
                    i1++;
                }
                point = (Point) linkedlist.poll();
                pts.add(point);
                //Log.e("linkedlist","---"+point.x+"   "+point.y);
            } while (point != null);
            // fillTwoColor(bitmap);

        }
    }


    private boolean isBlack(int i, int j) {
        while (Color.red(i) == Color.green(i) && Color.green(i) == Color.blue(i) && Color.red(i) < 150 || i == j) {
            return true;
        }
        return false;
    }

}
