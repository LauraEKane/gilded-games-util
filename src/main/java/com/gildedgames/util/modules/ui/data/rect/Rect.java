package com.gildedgames.util.modules.ui.data.rect;

import com.gildedgames.util.modules.ui.data.Pos2D;
import com.gildedgames.util.modules.ui.input.InputProvider;

public interface Rect
{

	RectBuilder rebuild();

	float degrees();

	float originX();

	float originY();

	float scale();

	float maxX();

	float maxY();

	float centerX();

	float centerY();

	Pos2D center();

	float x();

	float y();

	float width();

	float height();

	boolean isCenteredX();

	boolean isCenteredY();

	boolean intersects(float x, float y);

	boolean intersects(Rect dim);

	boolean isHovered(InputProvider input);

}
