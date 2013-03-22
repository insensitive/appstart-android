package com.appstart.tabgroup;

import com.appstart.HomeWallpaper;

import android.content.Intent;
import android.os.Bundle;

public class TabGroupHomeWallpaper1 extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        startChildActivity("HomeWallpaper", new Intent(this,HomeWallpaper.class));
        
        
        
    }
}
