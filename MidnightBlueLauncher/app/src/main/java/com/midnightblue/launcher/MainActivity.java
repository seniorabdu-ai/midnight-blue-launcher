package com.midnightblue.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private HomeView home;
    @Override public void onCreate(Bundle b) { super.onCreate(b); getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); home = new HomeView(); setContentView(home); }
    @Override public void onWindowFocusChanged(boolean hasFocus) { super.onWindowFocusChanged(hasFocus); if(hasFocus) getWindow().getDecorView().setSystemUiVisibility(5894|1024|512); }

    class HomeView extends View {
        Paint p = new Paint(3); Handler handler = new Handler(); int selected=0; long lastTap=0;
        String[] names={"MBC","WATCH IT","Yango Play","Shahid","Netflix","Prime Video","YouTube"};
        String[][] packages={{"com.mbc","com.mbc.tv","com.mbcgroup.mbc","com.mbc.shahid"},{"com.watchit.vod"},{"com.yango.saft.tv"},{"net.mbc.shahidTV"},{"com.netflix.ninja"},{"com.amazon.amazonvideo.livingroom","com.google.android.videos"},{"com.google.android.youtube.tv"}};
        int[] tileColors={0xff315b72,0xff326c82,0xff254d68,0xff3e547f,0xff8f2630,0xff375d78,0xffa62d2d};
        float sx,sy; boolean[] installed=new boolean[names.length];
        Runnable ticker=new Runnable(){public void run(){invalidate();handler.postDelayed(this,1000);}};
        HomeView(){super(MainActivity.this); setFocusable(true); setFocusableInTouchMode(true); for(int i=0;i<names.length;i++) installed[i]=findPackage(packages[i])!=null; handler.post(ticker);}
        String findPackage(String[] list){ PackageManager pm=getPackageManager(); for(String s:list) try{pm.getPackageInfo(s,0); return s;}catch(Exception e){} return null; }
        protected void onDraw(Canvas c){ super.onDraw(c); float w=getWidth(),h=getHeight(); sx=w/1920f; sy=h/1080f; c.save(); c.scale(sx,sy);
            // Red Sea-inspired dusk background, drawn locally (no network or photo permissions).
            p.setShader(new LinearGradient(0,0,0,1080,new int[]{0xff071827,0xff0b2b40,0xff17445a,0xff081d2c},null,Shader.TileMode.CLAMP)); c.drawRect(0,0,1920,1080,p); p.setShader(null);
            p.setColor(0xffe0a879); c.drawCircle(1510,245,82,p); p.setColor(0x22ffffff); c.drawCircle(1510,245,112,p);
            p.setColor(0xff123e52); Path sea=new Path(); sea.moveTo(0,520); sea.cubicTo(430,465,710,565,1050,510); sea.cubicTo(1390,460,1620,535,1920,490); sea.lineTo(1920,1080);sea.lineTo(0,1080);sea.close();c.drawPath(sea,p);
            p.setColor(0xff0b3044); Path shore=new Path(); shore.moveTo(0,700);shore.cubicTo(400,610,620,750,960,680);shore.cubicTo(1300,610,1570,740,1920,650);shore.lineTo(1920,1080);shore.lineTo(0,1080);shore.close();c.drawPath(shore,p);
            for(int i=0;i<7;i++){p.setColor(0x228ac4d5);p.setStrokeWidth(2);float y=570+i*48;c.drawLine(900+i*12,y,1840,y-8,p);}
            text(c,"MIDNIGHT BLUE",104,88,34,0xffd7e9ef,true); text(c,"AL QUSEIR  ·  RED SEA",104,130,18,0xff9bb9c7,false);
            String time=new SimpleDateFormat("h:mm",Locale.ENGLISH).format(new Date()); String am=new SimpleDateFormat("a",Locale.ENGLISH).format(new Date());
            text(c,time,104,355,150,0xfff4f2eb,true); text(c,am,420,350,28,0xffc1d8df,true);
            String date=new SimpleDateFormat("EEEE, d MMMM yyyy",Locale.ENGLISH).format(new Date()); text(c,date,112,408,25,0xffc3d6dc,false);
            PrayerTimes pt=new PrayerTimes(new Date()); String next=pt.next(); text(c,"NEXT PRAYER",112,520,17,0xff8fb8c8,true); text(c,next,112,568,40,0xfff1e4d3,true); text(c,pt.countdown(),112,610,22,0xffb9d1da,false);
            text(c,"TODAY'S PRAYERS",112,700,17,0xff8fb8c8,true);
            String[] pn={"FAJR","DHUHR","ASR","MAGHRIB","ISHA"}; String[] pv=pt.values();
            for(int i=0;i<5;i++){float x=112+i*170; text(c,pn[i],x,744,14,0xff92b5c2,true);text(c,pv[i],x,780,23,0xfff1eee8,true);}
            text(c,"STREAMING",104,865,17,0xffa5c4d0,true);
            float start=104, y=900, tw=225, th=104, gap=22;
            for(int i=0;i<names.length;i++){float x=start+i*(tw+gap); boolean focus=(i==selected); p.setShader(new LinearGradient(x,y,x+tw,y+th,tileColors[i],0xff102c40,Shader.TileMode.CLAMP)); c.drawRoundRect(x,y,x+tw,y+th,18,18,p);p.setShader(null); if(focus){p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(5);p.setColor(0xffe5c7a4);c.drawRoundRect(x-4,y-4,x+tw+4,y+th+4,20,20,p);p.setStyle(Paint.Style.FILL);} text(c,names[i],x+18,y+58,22,0xffffffff,true); if(!installed[i]) text(c,"NOT FOUND",x+18,y+84,11,0xffc1d1d8,false); }
            text(c,"Use the remote to select an app",104,1035,15,0xff7797a6,false); text(c,"SILENT PRAYER CLOCK",1600,1035,14,0xff7797a6,false);
            c.restore();
        }
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(size);p.setTypeface(bold?Typeface.create("sans-serif",Typeface.BOLD):Typeface.create("sans-serif",Typeface.NORMAL));p.setShadowLayer(8,0,2,0x55000000);c.drawText(s,x,y,p);p.clearShadowLayer();}
        @Override public boolean onKeyDown(int key,KeyEvent e){ if(key==KeyEvent.KEYCODE_DPAD_RIGHT){selected=(selected+1)%names.length;invalidate();return true;} if(key==KeyEvent.KEYCODE_DPAD_LEFT){selected=(selected+names.length-1)%names.length;invalidate();return true;} if(key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_ENTER){launch(selected);return true;} if(key==KeyEvent.KEYCODE_BACK){return true;} return super.onKeyDown(key,e); }
        void launch(int i){String pkg=findPackage(packages[i]); if(pkg==null){Toast.makeText(MainActivity.this,names[i]+" is not installed or its package name is different",Toast.LENGTH_LONG).show();return;} Intent in=getPackageManager().getLeanbackLaunchIntentForPackage(pkg); if(in==null)in=getPackageManager().getLaunchIntentForPackage(pkg); if(in!=null)try{startActivity(in);}catch(Exception ex){Toast.makeText(MainActivity.this,"Cannot open "+names[i],Toast.LENGTH_SHORT).show();} }
    }

    // Approximate Egyptian-method prayer calculation for Al Quseir. Times are local Africa/Cairo.
    class PrayerTimes {
        double lat=26.1043, lon=34.2770; Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Africa/Cairo")); double[] t=new double[6];
        PrayerTimes(Date d){cal.setTime(d);calc();}
        double dsin(double x){return Math.sin(Math.toRadians(x));} double dcos(double x){return Math.cos(Math.toRadians(x));} double dtan(double x){return Math.tan(Math.toRadians(x));} double darcsin(double x){return Math.toDegrees(Math.asin(x));} double darccos(double x){return Math.toDegrees(Math.acos(x));} double darctan2(double y,double x){return Math.toDegrees(Math.atan2(y,x));} double fix(double x){x%=360;return x<0?x+360:x;} double norm(double x){x%=24;return x<0?x+24:x;}
        double julian(int y,int m,int d){if(m<=2){y--;m+=12;}int a=y/100,b=2-a+a/4;return Math.floor(365.25*(y+4716))+Math.floor(30.6001*(m+1))+d+b-1524.5;}
        double[] sun(double jd){double d=jd-2451545.0,g=fix(357.529+0.98560028*d),q=fix(280.459+0.98564736*d),L=fix(q+1.915*dsin(g)+0.020*dsin(2*g)),e=23.439-0.00000036*d,ra=fix(darctan2(dcos(e)*dsin(L),dcos(L)))/15,eq=norm(q/15-ra),dec=darcsin(dsin(e)*dsin(L));return new double[]{dec,eq};}
        double hour(double angle,double dec){double v=(-dsin(angle)-dsin(lat)*dsin(dec))/(dcos(lat)*dcos(dec));return darccos(Math.max(-1,Math.min(1,v)))/15;}
        void calc(){int y=cal.get(Calendar.YEAR),m=cal.get(Calendar.MONTH)+1,d=cal.get(Calendar.DAY_OF_MONTH);double jd=julian(y,m,d),mid=12+cal.getTimeZone().getOffset(cal.getTimeInMillis())/3600000.0-lon/15; double[] s=sun(jd+mid/24);double dec=s[0],eq=s[1]; double noon=12-eq-lon/15+cal.getTimeZone().getOffset(cal.getTimeInMillis())/3600000.0; double fajr=noon-hour(-19.5,dec),sunrise=noon-hour(-0.833,dec),sunset=noon+hour(-0.833,dec),asr=noon+darccos((dtan(Math.abs(lat-dec))+1)/(dtan(Math.abs(lat-dec))+1.0))/15; // overwritten below
            double shadow=1; double angle=darccos((dsin(darctan2(1,shadow+dtan(Math.abs(lat-dec))))-dsin(lat)*dsin(dec))/(dcos(lat)*dcos(dec))); double asrH=hour(-darcsin(1/Math.sqrt(1+Math.pow(1/(Math.tan(Math.toRadians(Math.abs(lat-dec)))+1),2))),dec); asr=noon+asrH; double isha=noon+hour(-17.5,dec); t=new double[]{fajr,sunrise,noon+0.0,asr,sunset,isha}; }
        String fmt(double h){int mins=(int)Math.round(norm(h)*60);return String.format(Locale.ENGLISH,"%02d:%02d",(mins/60)%24,mins%60);}
        String[] values(){return new String[]{fmt(t[0]),fmt(t[2]),fmt(t[3]),fmt(t[4]),fmt(t[5])};}
        String next(){long now=System.currentTimeMillis();String[] n={"Fajr","Dhuhr","Asr","Maghrib","Isha"};int[] idx={0,2,3,4,5};for(int i=0;i<idx.length;i++){Calendar c=(Calendar)cal.clone();int min=(int)Math.round(norm(t[idx[i]])*60);c.set(Calendar.HOUR_OF_DAY,(min/60)%24);c.set(Calendar.MINUTE,min%60);c.set(Calendar.SECOND,0);if(c.getTimeInMillis()>now)return n[i];}return "Fajr";}
        String countdown(){long now=System.currentTimeMillis();int[] idx={0,2,3,4,5};for(int j:idx){Calendar c=(Calendar)cal.clone();int min=(int)Math.round(norm(t[j])*60);c.set(Calendar.HOUR_OF_DAY,(min/60)%24);c.set(Calendar.MINUTE,min%60);c.set(Calendar.SECOND,0);long diff=c.getTimeInMillis()-now;if(diff>0){long h=diff/3600000,m=(diff/60000)%60,s=(diff/1000)%60;return String.format(Locale.ENGLISH,"IN %02d:%02d:%02d",h,m,s);}}return "IN 00:00:00";}
    }
}
