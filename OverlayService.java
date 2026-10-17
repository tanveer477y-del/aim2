package com.tanveer.carromaim;

import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;

public class OverlayService extends Service {
    WindowManager wm; View lineView, panel; WindowManager.LayoutParams lineParams, panelParams;
    int sx=540,sy=1500,tx=850,ty=1100,bx=-1,by=-1; boolean lineVisible=true;
    @Override public int onStartCommand(Intent intent,int flags,int startId){
        if(intent!=null){sx=intent.getIntExtra("sx",540);sy=intent.getIntExtra("sy",1500);tx=intent.getIntExtra("tx",850);ty=intent.getIntExtra("ty",1100);bx=intent.getIntExtra("bx",-1);by=intent.getIntExtra("by",-1);}
        if(wm==null)wm=(WindowManager)getSystemService(WINDOW_SERVICE);
        removeViews(); createLine(); createPanel(); return START_NOT_STICKY;
    }
    int overlayType(){return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;}
    void createLine(){
        lineView=new View(this){ Paint p=new Paint(3); @Override protected void onDraw(Canvas c){super.onDraw(c); if(!lineVisible)return;
            p.setColor(0x8847d7ff);p.setStrokeWidth(11);p.setStyle(Paint.Style.STROKE);p.setStrokeCap(Paint.Cap.ROUND);
            c.drawLine(sx,sy,tx,ty,p);
            p.setColor(Color.rgb(71,215,255));p.setStrokeWidth(4);c.drawLine(sx,sy,tx,ty,p);
            p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(71,215,255));c.drawCircle(sx,sy,13,p);
            p.setColor(Color.rgb(255,94,156));c.drawCircle(tx,ty,13,p);
            if(bx>=0&&by>=0){Path path=new Path();path.moveTo(sx,sy);path.lineTo(bx,by);path.lineTo(tx,ty);p.setColor(Color.rgb(92,255,177));p.setStrokeWidth(5);p.setStyle(Paint.Style.STROKE);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(92,255,177));c.drawCircle(bx,by,10,p);}
        }};
        lineParams=new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,overlayType(),WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,PixelFormat.TRANSLUCENT);
        lineParams.gravity=Gravity.TOP|Gravity.START; wm.addView(lineView,lineParams);
    }
    void createPanel(){
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(14,8,14,8);box.setBackgroundColor(0xee091625);
        TextView title=new TextView(this);title.setText("CARROM AIM  ⋮");title.setTextColor(Color.rgb(71,215,255));title.setTypeface(null,Typeface.BOLD);title.setTextSize(13);box.addView(title);
        TextView info=new TextView(this);info.setText("CYAN: aim  PINK: target\nGreen: bank path");info.setTextColor(Color.WHITE);info.setTextSize(11);box.addView(info);
        Button toggle=new Button(this);toggle.setText("HIDE / SHOW LINE");toggle.setTextSize(10);box.addView(toggle);
        Button close=new Button(this);close.setText("CLOSE AIM");close.setTextSize(10);box.addView(close);
        panel=box;panelParams=new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,overlayType(),WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);
        panelParams.gravity=Gravity.TOP|Gravity.START;panelParams.x=20;panelParams.y=180;wm.addView(panel,panelParams);
        toggle.setOnClickListener(v->{lineVisible=!lineVisible;if(lineView!=null)lineView.invalidate();});
        close.setOnClickListener(v->stopSelf());
        title.setOnTouchListener(new View.OnTouchListener(){float rx,ry;int ox,oy;public boolean onTouch(View v,MotionEvent e){if(e.getAction()==MotionEvent.ACTION_DOWN){rx=e.getRawX();ry=e.getRawY();ox=panelParams.x;oy=panelParams.y;return true;}if(e.getAction()==MotionEvent.ACTION_MOVE){panelParams.x=ox+(int)(e.getRawX()-rx);panelParams.y=oy+(int)(e.getRawY()-ry);wm.updateViewLayout(panel,panelParams);return true;}return e.getAction()==MotionEvent.ACTION_UP; }});
    }
    void removeViews(){try{if(panel!=null)wm.removeView(panel);}catch(Exception ignored){}try{if(lineView!=null)wm.removeView(lineView);}catch(Exception ignored){}panel=null;lineView=null;}
    @Override public void onDestroy(){removeViews();super.onDestroy();}
    @Override public IBinder onBind(Intent i){return null;}
}
