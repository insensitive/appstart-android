package com.appstart.tabgroup;

import com.appstart.More;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class TabGroupMore extends TabGroupActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try{
        startChildActivity("More", new Intent(this,More.class));
        }catch(Exception e){
        	e.printStackTrace();
        	//Toast.makeText(getParent(), "Error", Toast.LENGTH_SHORT).show();
        }

        
    }
	
	
	
}
