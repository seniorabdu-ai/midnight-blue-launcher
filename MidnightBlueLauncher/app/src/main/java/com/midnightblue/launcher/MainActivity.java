package com.midnightblue.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.*;

/** MIDNIGHT BLUE — premium Red Sea Android TV launcher. */
public class MainActivity extends Activity {
    private HomeView home;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(5894 | 1024 | 512);
        home = new HomeView();
        setContentView(home);
    }
    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(5894 | 1024 | 512);
    }

    class AppItem {
        String title; String[] packages; String pkg; Drawable icon;
        AppItem(String title, String... packages) {
            this.title=title; this.packages=packages;
            for (String candidate: packages) {
                try { icon=getPackageManager().getApplicationIcon(candidate); pkg=candidate; break; }
                catch (Exception ignored) {}
            }
        }
    }

    class HomeView extends View {
        final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        final Handler handler = new Handler();
        final String[] appNames = {"Netflix", "YouTube", "Prime Video", "Shahid", "WATCH IT", "MBC", "Yango Play", "All Apps"};
        final String[][] appPackages = {
            {"com.netflix.ninja"},
            {"com.google.android.youtube.tv"},
            {"com.amazon.amazonvideo.livingroom", "com.google.android.videos"},
            {"net.mbc.shahidTV"},
            {"com.watchit.vod"},
            {"com.mbc", "com.mbc.tv", "com.mbcgroup.mbc", "com.mbc.shahid"},
            {"com.yango.saft.tv"},
            {}
        };
        final int[] accents = {0xffe50914,0xffe5e9f0,0xff1594e8,0xff22c6b7,0xffffc329,0xffbd2934,0xff9b43ed,0xff6b91b6};
        AppItem[] apps = new AppItem[7];
        int selected=0; boolean allApps=false; int allSelected=0;
        ArrayList<ResolveInfo> launchables = new ArrayList<>();
        float sx=1, sy=1;
        Runnable ticker = new Runnable(){ public void run(){ invalidate(); handler.postDelayed(this,1000); }};

        HomeView(){ super(MainActivity.this); setFocusable(true); setFocusableInTouchMode(true);
            for(int i=0;i<7;i++) apps[i]=new AppItem(appNames[i],appPackages[i]);
            setLayerType(View.LAYER_TYPE_SOFTWARE,null);
            handler.post(ticker); requestFocus();
        }

        @Override protected void onDraw(Canvas canvas){
            super.onDraw(canvas); float w=getWidth(), h=getHeight(); sx=w/1920f; sy=h/1080f;
            canvas.save(); canvas.scale(sx,sy); drawBackdrop(canvas);
            if(allApps) drawAllApps(canvas); else drawHome(canvas);
            canvas.restore();
        }

        void drawBackdrop(Canvas c){
            p.setShader(new LinearGradient(0,0,0,1080,new int[]{0xff071326,0xff102b49,0xff0a3450,0xff061629},null,Shader.TileMode.CLAMP));
            c.drawRect(0,0,1920,1080,p); p.setShader(null);
            // Sunset glow and distant Red Sea mountains.
            p.setShader(new RadialGradient(1350,290,680,new int[]{0x66d98270,0x22c36c73,0x00000000},null,Shader.TileMode.CLAMP));
            c.drawRect(650,0,1920,760,p); p.setShader(null);
            Path mountains=new Path(); mountains.moveTo(0,430); mountains.cubicTo(180,330,270,360,420,405); mountains.cubicTo(570,320,720,315,860,390); mountains.cubicTo(1030,330,1140,350,1280,405); mountains.cubicTo(1450,310,1600,260,1760,350); mountains.cubicTo(1830,370,1880,385,1920,365); mountains.lineTo(1920,700); mountains.lineTo(0,700); mountains.close();
            p.setShader(new LinearGradient(0,300,0,700,0xff263b55,0xff142a43,Shader.TileMode.CLAMP)); c.drawPath(mountains,p); p.setShader(null);
            // Water plane with subtle reflected light.
            p.setShader(new LinearGradient(0,470,0,1080,new int[]{0xff164a65,0xff0a3652,0xff071d35},null,Shader.TileMode.CLAMP));
            c.drawRect(0,470,1920,1080,p); p.setShader(null);
            p.setShader(new LinearGradient(960,490,1560,800,0x55f0a58a,0x00000000,Shader.TileMode.CLAMP));
            Path reflection=new Path(); reflection.moveTo(1190,470); reflection.lineTo(1450,470); reflection.lineTo(1670,900); reflection.lineTo(980,900); reflection.close(); c.drawPath(reflection,p); p.setShader(null);
            // Coastal lights and palm silhouettes add depth without external assets.
            Random r=new Random(14);
            for(int i=0;i<78;i++){ float x=40+r.nextFloat()*950, y=470+r.nextFloat()*145; p.setColor(i%4==0?0x99f4c58b:0x55f2d6a5); c.drawCircle(x,y,1.5f+r.nextFloat()*2.2f,p); }
            for(int i=0;i<14;i++){float y=565+i*27; p.setColor(0x123bd3e8); p.setStrokeWidth(1.2f); c.drawLine(980+i*9,y,1880,y-10,p);}
            p.setShader(new LinearGradient(0,0,0,1080,0xaa061326,0x55061326,Shader.TileMode.CLAMP)); c.drawRect(0,0,1920,1080,p); p.setShader(null);
            p.setShader(new LinearGradient(0,0,900,0,0xbb061326,0x00061326,Shader.TileMode.CLAMP)); c.drawRect(0,0,1000,1080,p); p.setShader(null);
        }

        void drawHome(Canvas c){
            // Brand and understated system row.
            text(c,"MIDNIGHT BLUE",104,86,27,0xfff0f5fa,true,2.5f);
            text(c,"RED SEA  /  EGYPT",106,119,13,0xff8daec4,true,2.4f);
            text(c,"●  SYSTEM READY",1510,78,13,0xff91c7d0,true,1.2f);
            text(c,new SimpleDateFormat("EEE, d MMM",Locale.ENGLISH).format(new Date()).toUpperCase(Locale.ENGLISH),1690,78,14,0xffc5d4e0,true,1.1f);
            // Main time block.
            Date now=new Date();
            String clock=new SimpleDateFormat("h:mm",Locale.ENGLISH).format(now);
            String ampm=new SimpleDateFormat("a",Locale.ENGLISH).format(now);
            text(c,clock,104,315,146,0xfff5f7fa,false,-5f);
            text(c,ampm,430,309,27,0xffa8c8d9,true,1.5f);
            text(c,new SimpleDateFormat("EEEE, d MMMM yyyy",Locale.ENGLISH).format(now),112,365,25,0xffd0dce6,false,0.3f);
            text(c,"AL QUSEIR  ·  RED SEA  ·  EGYPT",112,412,15,0xff8fb1c5,true,2.0f);
            // Prayer panel.
            drawPrayerPanel(c);
            // App shelf.
            text(c,"YOUR APPS",104,605,17,0xffb7cbd9,true,2.5f);
            text(c,"Choose an app",1660,605,13,0xff7898af,false,0.4f);
            float x0=104, y=642, cw=194, ch=172, gap=22;
            for(int i=0;i<8;i++) drawAppCard(c,i,x0+i*(cw+gap),y,cw,ch,i==selected);
            text(c,"Use the directional pad to move  ·  OK to open  ·  Back to return",104,894,14,0xff7895aa,false,0.2f);
            // Minimal footer mark.
            line(c,104,944,1816,944,0x447fa1b8,1);
            text(c,"MIDNIGHT BLUE",104,980,12,0xff7794a8,true,2.1f);
            text(c,"A calmer way to arrive home.",104,1008,14,0xff9bb2c1,false,0.1f);
            text(c,"SILENT PRAYER CLOCK  ·  AFRICA/CAIRO",1400,1008,12,0xff7896aa,true,1.0f);
        }

        void drawPrayerPanel(Canvas c){
            float l=1000,t=170,r=1816,b=500;
            round(c,l,t,r,b,28,0xc9142944,0x558bb8d3,1.4f);
            text(c,"PRAYER TIMES",1040,220,17,0xffdbe8f1,true,2.2f);
            text(c,"AL QUSEIR  ·  EGYPTIAN CALCULATION",1040,249,12,0xff8faec1,true,1.2f);
            PrayerTimes pt=new PrayerTimes(new Date());
            String[] names={"FAJR","DHUHR","ASR","MAGHRIB","ISHA"};
            String[] vals=pt.values(); int active=pt.nextIndex();
            float sx=1038, yy=278, ww=145, hh=92, gg=12;
            for(int i=0;i<5;i++){
                float x=sx+i*(ww+gg);
                if(i==active) round(c,x,yy,x+ww,yy+hh,14,0x553e8fb0,0xffd7b77d,1.8f);
                else round(c,x,yy,x+ww,yy+hh,14,0x66324860,0x446e8ba2,1f);
                textCentered(c,names[i],x+ww/2,yy+34,13,i==active?0xffffd99b:0xffa9c0d0,true,1.6f);
                textCentered(c,vals[i],x+ww/2,yy+66,19,0xfff1f5f8,true,0.2f);
            }
            round(c,1038,390,1792,466,16,0x88314a65,0x557da2bc,1f);
            text(c,"NEXT PRAYER",1064,420,12,0xff9db9ca,true,1.8f);
            text(c,pt.nextName(),1064,449,22,0xffffd99b,true,0.2f);
            text(c,"COUNTDOWN",1470,420,11,0xff9db9ca,true,1.5f);
            text(c,pt.countdown(),1470,450,24,0xffeaf5fb,true,0.2f);
            text(c,"No adhan audio",1660,487,11,0xff83a3b8,false,0.1f);
        }

        void drawAppCard(Canvas c,int i,float x,float y,float w,float h,boolean focus){
            int accent=accents[i];
            if(focus){ p.setShadowLayer(24,0,0,0x9958baff); }
            round(c,x,y,x+w,y+h,22,focus?0xe51b3550:0xd9162c43,focus?0xffd7edff:0x667b9bb5,focus?3.2f:1.1f);
            p.clearShadowLayer();
            // Accent hairline.
            round(c,x+18,y+16,x+52,y+20,2,accent,accent,0);
            if(i<7 && apps[i].icon!=null){
                Drawable d=apps[i].icon; int size=64; int ix=(int)(x+(w-size)/2), iy=(int)(y+39);
                d.setBounds(ix,iy,ix+size,iy+size); d.draw(c);
            } else {
                if(i==7) drawGridIcon(c,x+w/2,y+70);
                else drawFallbackIcon(c,i,x+w/2,y+70,accent);
            }
            textCentered(c,appNames[i].toUpperCase(Locale.ENGLISH),x+w/2,y+137,13,0xffeef4f8,true,1.1f);
            if(i<7 && apps[i].pkg==null) textCentered(c,"NOT INSTALLED",x+w/2,y+158,9,0xff8ca6b8,true,0.7f);
            if(focus){ p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(2.4f); p.setColor(0xff8ed7ff); c.drawRoundRect(x+4,y+4,x+w-4,y+h-4,19,19,p); p.setStyle(Paint.Style.FILL); }
        }

        void drawGridIcon(Canvas c,float cx,float cy){
            for(int yy=0;yy<2;yy++)for(int xx=0;xx<2;xx++) round(c,cx-19+xx*23,cy-19+yy*23,cx-4+xx*23,cy-4+yy*23,4,0xffc6d7e4,0xffc6d7e4,0);
        }
        void drawFallbackIcon(Canvas c,int i,float cx,float cy,int color){
            p.setColor(color); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(4); c.drawCircle(cx,cy,23,p); p.setStyle(Paint.Style.FILL);
            textCentered(c,"▶",cx,cy+9,25,0xfff4f7fa,true,0);
        }

        void drawAllApps(Canvas c){
            text(c,"ALL APPLICATIONS",104,110,28,0xfff2f6fa,true,2.4f);
            text(c,"Installed apps on this TV",106,145,15,0xff9bb5c7,false,0.2f);
            loadLaunchables();
            int cols=5, cellW=300, cellH=165; float left=104, top=205;
            for(int i=0;i<launchables.size();i++){
                int row=i/cols,col=i%cols; float x=left+col*330,y=top+row*190;
                boolean f=i==allSelected;
                round(c,x,y,x+cellW,y+cellH,20,f?0xe52c4c68:0xb8172d45,f?0xffbde8ff:0x557e9db5,f?3:1);
                ResolveInfo ri=launchables.get(i); Drawable icon=ri.loadIcon(getPackageManager());
                if(icon!=null){icon.setBounds((int)x+24,(int)y+42,(int)x+88,(int)y+106);icon.draw(c);}
                String label=ri.loadLabel(getPackageManager()).toString(); text(c,ellipsize(label,22),x+110,y+88,19,0xfff1f5f8,true,0.1f);
            }
            text(c,"Press OK to launch  ·  BACK to home",104,1010,15,0xff9bb5c7,false,0.2f);
        }
        String ellipsize(String s,int n){return s.length()>n?s.substring(0,n-1)+"…":s;}
        void loadLaunchables(){if(!launchables.isEmpty())return; Intent q=new Intent(Intent.ACTION_MAIN);q.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER); launchables.addAll(getPackageManager().queryIntentActivities(q,0)); if(launchables.isEmpty()){q.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);q.addCategory(Intent.CATEGORY_LAUNCHER);launchables.addAll(getPackageManager().queryIntentActivities(q,0));} Collections.sort(launchables,(a,b)->a.loadLabel(getPackageManager()).toString().compareToIgnoreCase(b.loadLabel(getPackageManager()).toString()));}

        void round(Canvas c,float l,float t,float r,float b,float rad,int fill,int stroke,float sw){p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(fill);p.setShadowLayer(16,0,5,0x44000000);c.drawRoundRect(l,t,r,b,rad,rad,p);p.clearShadowLayer();if(sw>0){p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(sw);p.setColor(stroke);c.drawRoundRect(l,t,r,b,rad,rad,p);p.setStyle(Paint.Style.FILL);}}
        void line(Canvas c,float x1,float y1,float x2,float y2,int color,float sw){p.setColor(color);p.setStrokeWidth(sw);c.drawLine(x1,y1,x2,y2,p);}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold,float spacing){p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(size);p.setTypeface(Typeface.create(bold?"sans-serif-medium":"sans-serif-light",bold?Typeface.BOLD:Typeface.NORMAL));p.setShadowLayer(5,0,2,0x66000000);c.drawText(s,x,y,p);p.clearShadowLayer();}
        void textCentered(Canvas c,String s,float cx,float y,float size,int color,boolean bold,float spacing){p.setTextSize(size);p.setTypeface(Typeface.create(bold?"sans-serif-medium":"sans-serif-light",bold?Typeface.BOLD:Typeface.NORMAL));float width=p.measureText(s);text(c,s,cx-width/2,y,size,color,bold,spacing);}

        @Override public boolean onKeyDown(int key,KeyEvent e){
            if(allApps){
                loadLaunchables(); int cols=5;
                if(key==KeyEvent.KEYCODE_BACK){allApps=false;invalidate();return true;}
                if(key==KeyEvent.KEYCODE_DPAD_RIGHT){allSelected=Math.min(launchables.size()-1,allSelected+1);invalidate();return true;}
                if(key==KeyEvent.KEYCODE_DPAD_LEFT){allSelected=Math.max(0,allSelected-1);invalidate();return true;}
                if(key==KeyEvent.KEYCODE_DPAD_DOWN){allSelected=Math.min(launchables.size()-1,allSelected+cols);invalidate();return true;}
                if(key==KeyEvent.KEYCODE_DPAD_UP){allSelected=Math.max(0,allSelected-cols);invalidate();return true;}
                if(key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_ENTER){launchResolve(allSelected);return true;}
                return true;
            }
            if(key==KeyEvent.KEYCODE_DPAD_RIGHT){selected=(selected+1)%8;invalidate();return true;}
            if(key==KeyEvent.KEYCODE_DPAD_LEFT){selected=(selected+7)%8;invalidate();return true;}
            if(key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_ENTER){if(selected==7){allApps=true;allSelected=0;loadLaunchables();}else launch(selected);invalidate();return true;}
            if(key==KeyEvent.KEYCODE_BACK)return true;
            return super.onKeyDown(key,e);
        }
        void launch(int i){String pkg=apps[i].pkg;if(pkg==null){Toast.makeText(MainActivity.this,appNames[i]+" is not installed",Toast.LENGTH_SHORT).show();return;}Intent in=getPackageManager().getLeanbackLaunchIntentForPackage(pkg);if(in==null)in=getPackageManager().getLaunchIntentForPackage(pkg);if(in!=null)try{startActivity(in);}catch(Exception ex){Toast.makeText(MainActivity.this,"Unable to open "+appNames[i],Toast.LENGTH_SHORT).show();}}
        void launchResolve(int i){if(i<0||i>=launchables.size())return;ResolveInfo ri=launchables.get(i);Intent in=new Intent(Intent.ACTION_MAIN);in.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);in.setClassName(ri.activityInfo.packageName,ri.activityInfo.name);in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);try{startActivity(in);}catch(Exception ex){Toast.makeText(MainActivity.this,"Unable to open app",Toast.LENGTH_SHORT).show();}}
    }

    // Solar prayer-time calculation for Al Quseir. Egyptian General Authority angles;
    // Asr shadow factor 1. Display is calculated locally and silent (no adhan audio).
    class PrayerTimes {
        final double lat=26.1043, lon=34.2770; Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Africa/Cairo")); double[] t=new double[5];
        PrayerTimes(Date d){cal.setTime(d);calc();}
        double sin(double x){return Math.sin(Math.toRadians(x));} double cos(double x){return Math.cos(Math.toRadians(x));}
        double fix(double x){x%=360;return x<0?x+360:x;} double norm(double x){x%=24;return x<0?x+24:x;}
        double acos(double x){return Math.toDegrees(Math.acos(Math.max(-1,Math.min(1,x))));}
        double julian(int y,int m,int d){if(m<=2){y--;m+=12;}int a=y/100,b=2-a+a/4;return Math.floor(365.25*(y+4716))+Math.floor(30.6001*(m+1))+d+b-1524.5;}
        double[] sun(double jd){double d=jd-2451545.0,g=fix(357.529+0.98560028*d),q=fix(280.459+0.98564736*d),L=fix(q+1.915*sin(g)+0.020*sin(2*g)),e=23.439-0.00000036*d,ra=fix(Math.toDegrees(Math.atan2(cos(e)*sin(L),cos(L))))/15,eq=norm(q/15-ra),dec=Math.toDegrees(Math.asin(sin(e)*sin(L)));return new double[]{dec,eq};}
        double hour(double angle,double dec){double v=(-sin(angle)-sin(lat)*sin(dec))/(cos(lat)*cos(dec));return acos(v)/15;}
        void calc(){int y=cal.get(Calendar.YEAR),m=cal.get(Calendar.MONTH)+1,d=cal.get(Calendar.DAY_OF_MONTH);double jd=julian(y,m,d),tz=cal.getTimeZone().getOffset(cal.getTimeInMillis())/3600000.0;double[] s=sun(jd+(12+tz-lon/15)/24);double dec=s[0],eq=s[1];double noon=12-eq-lon/15+tz;double fajr=noon-hour(-19.5,dec);double sunrise=noon-hour(-0.833,dec);double sunset=noon+hour(-0.833,dec);double dhuhr=noon+2.0/60.0;double asrAngle=-Math.toDegrees(Math.atan(1.0/(1.0+Math.tan(Math.toRadians(Math.abs(lat-dec))))));double asr=noon+hour(asrAngle,dec);double isha=noon+hour(-17.5,dec);t=new double[]{fajr,dhuhr,asr,sunset,isha};}
        String fmt(double h){int mins=(int)Math.round(norm(h)*60);return String.format(Locale.ENGLISH,"%02d:%02d",(mins/60)%24,mins%60);}
        String[] values(){return new String[]{fmt(t[0]),fmt(t[1]),fmt(t[2]),fmt(t[3]),fmt(t[4])};}
        int nextIndex(){Calendar now=Calendar.getInstance(TimeZone.getTimeZone("Africa/Cairo"));int cur=now.get(Calendar.HOUR_OF_DAY)*60+now.get(Calendar.MINUTE);for(int i=0;i<t.length;i++){int m=(int)Math.round(norm(t[i])*60);if(m>cur)return i;}return 0;}
        String nextName(){return new String[]{"Fajr","Dhuhr","Asr","Maghrib","Isha"}[nextIndex()];}
        String countdown(){Calendar now=Calendar.getInstance(TimeZone.getTimeZone("Africa/Cairo"));long n=now.getTimeInMillis();int idx=nextIndex();Calendar target=(Calendar)now.clone();int mins=(int)Math.round(norm(t[idx])*60);target.set(Calendar.HOUR_OF_DAY,(mins/60)%24);target.set(Calendar.MINUTE,mins%60);target.set(Calendar.SECOND,0);target.set(Calendar.MILLISECOND,0);if(target.getTimeInMillis()<=n)target.add(Calendar.DAY_OF_MONTH,1);long diff=target.getTimeInMillis()-n;long h=diff/3600000,m=(diff/60000)%60,s=(diff/1000)%60;return String.format(Locale.ENGLISH,"%02d:%02d:%02d",h,m,s);}
    }
}
