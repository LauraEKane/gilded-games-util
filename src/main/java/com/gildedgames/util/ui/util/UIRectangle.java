package com.gildedgames.util.ui.util;

import com.gildedgames.util.ui.UIElementHolder;
import com.gildedgames.util.ui.UIFrame;
import com.gildedgames.util.ui.data.Dimensions2D;
import com.gildedgames.util.ui.data.DrawingData;
import com.gildedgames.util.ui.graphics.IGraphics;
import com.gildedgames.util.ui.input.InputProvider;

public class UIRectangle extends UIFrame
{

	protected DrawingData startColor, endColor;
	
	public UIRectangle(Dimensions2D dim)
	{
		this(dim, new DrawingData());
	}
	
	public UIRectangle(Dimensions2D dim, DrawingData data)
	{
		this(dim, data, null);
	}
	
	public UIRectangle(Dimensions2D dim, DrawingData startColor, DrawingData endColor)
	{
		super(dim);

		this.startColor = startColor;
		this.endColor = endColor;
	}
	
	public DrawingData getDrawingData()
	{
		return this.startColor;
	}
	
	public void setDrawingData(DrawingData data)
	{
		this.startColor = data;
	}

	@Override
	public void draw(IGraphics graphics, InputProvider input)
	{
		if (this.endColor == null)
		{
			graphics.drawRectangle(this.getFocusArea(), this.startColor);
		}
		else
		{
			graphics.drawGradientRectangle(this.getFocusArea(), this.startColor, this.endColor);
		}
		
	}

	@Override
	public void init(UIElementHolder elementHolder, Dimensions2D screenDimensions)
	{
		
	}

}
