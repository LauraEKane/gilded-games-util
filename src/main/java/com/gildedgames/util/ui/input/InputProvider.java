package com.gildedgames.util.ui.input;

import com.gildedgames.util.ui.data.Dimensions2D;

public interface InputProvider
{
	
	int getMouseX();
	
	int getMouseY();
	
	boolean isHovered(Dimensions2D dim);
	
}
