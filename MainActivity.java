package com.tanveer.carromaim;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    EditText sx, sy, tx, ty, bx, by;
    int cyan = Color.rgb(71,215,255);
    LinearLayout root;
    TextView label(String s) { TextView t=new TextView(this); t.setText(s); t.setTextColor(Color.rgb(225,240,255)); t.setTextSize(14); t.setPadding(0,10,0,4); return t; }
    EditText field(String hint, String value) { EditText e=new EditText(this); e.setSingleLine(true); e.setHint(hint); e.setText(value); e.setTextColor(Color.WHITE); e.setHintTextColor(0xff8ca4bf); e.setInputType(2); e.setPadding(12,6,12,6); e.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xff29415b)); return e; }
    LinearLayout row() { LinearLayout r=new LinearLayout(this); r.setOrientation(LinearLayout.HORIZONTAL); r.setGravity(Gravity.CENTER_VERTICAL); return r; }
    void addFieldRow(String title, EditText a, EditText b) {
        root.addView(label(title)); LinearLayout r=row();
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0, -2, 1);
        p.setMargins(0,0,8,0); r.addView(a,p);
        LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(0, -2, 1); r.addView(b,q); root.addView(r);
    }
    Button button(String s) { Button b=new Button(this); b.setText(s); b.setTextColor(Color.WHITE); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xff145a7a)); return b; }
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        ScrollView scroll=new ScrollView(this); root=new LinearLayout(this); root.setOrientation(1); root.setPadding(22,24,22,24); root.setBackgroundColor(0xff070b13); scroll.addView(root);
        TextView title=label("CARROM // FLOATING AIM"); title.setTextSize(23); title.setTextColor(cyan); title.setGravity(Gravity.CENTER_VERTICAL); root.addView(title);
        TextView desc=label("Manual coordinate mode · screen coordinates in pixels"); desc.setTextColor(0xff8ca4bf); root.addView(desc);
        sx=field("X","540"); sy=field("Y","1500"); tx=field("X","850"); ty=field("Y","1100");
        bx=field("X",""); by=field("Y","");
        addFieldRow("STRIKER POSITION (X, Y)",sx,sy);
        addFieldRow("TARGET COIN POSITION (X, Y)",tx,ty);
        addFieldRow("OPTIONAL BANK POINT (X, Y)",bx,by);
        TextView note=label("Enter approximate screen pixel coordinates. Leave bank point empty for a direct line. The overlay draws guidance only; it does not control the game."); note.setTextColor(0xff8ca4bf); root.addView(note);
        Button start=button("GRANT OVERLAY + START AIM"); root.addView(start);
        Button stop=button("STOP FLOATING AIM"); root.addView(stop);
        start.setOnClickListener(v->{
            if(!Settings.canDrawOverlays(this)){Intent i=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())); startActivity(i); Toast.makeText(this,"Enable 'Display over other apps', then tap Start again.",Toast.LENGTH_LONG).show(); return;}
            Intent i=new Intent(this,OverlayService.class);
            i.putExtra("sx",num(sx,540));i.putExtra("sy",num(sy,1500));i.putExtra("tx",num(tx,850));i.putExtra("ty",num(ty,1100));
            i.putExtra("bx",num(bx,-1));i.putExtra("by",num(by,-1));
            startService(i); Toast.makeText(this,"Floating aim started",Toast.LENGTH_SHORT).show();
        });
        stop.setOnClickListener(v->stopService(new Intent(this,OverlayService.class)));
        setContentView(scroll);
    }
    int num(EditText e,int d){try{return Integer.parseInt(e.getText().toString().trim());}catch(Exception ex){return d;}}
}
