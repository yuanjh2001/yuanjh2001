/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 * 
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.happynetwork.vrestate.mywidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MyWheelView extends View {

	/**
	 * Constructor
	 */
	public MyWheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Constructor
	 */
	public MyWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor
	 */
	public MyWheelView(Context context) {
		super(context);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
