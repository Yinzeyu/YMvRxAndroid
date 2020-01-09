/*
 * Copyright 2014 Toxic Bakery
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzy.example.widget.imagewatcher.transforms

import android.view.View
import kotlin.math.abs

class ZoomOutSlideTransformer : ABaseTransformer() {
    override fun onTransform(
        view: View,
        position: Float
    ) {
        if (position >= -1 || position <= 1) { // Modify the default slide transition to shrink the page as well
            val height = view.height.toFloat()
            val width = view.width.toFloat()
            val scaleFactor = Math.max(
                0.5f,
                1 - abs(position)
            )
            val vertMargin = height * (1 - scaleFactor) / 2
            val horzMargin = width * (1 - scaleFactor) / 2
            // Center vertically
            view.pivotY = 0.5f * height
            view.pivotX = 0.5f * width
            if (position < 0) {
                view.translationX = horzMargin - vertMargin / 2
            } else {
                view.translationX = -horzMargin + vertMargin / 2
            }
            // Scale the page down (between  0.5f and 1)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            // Fade the page relative to its size.
            view.alpha = 0.5f + (scaleFactor -  0.5f) / (1 -  0.5f) * (1 - 0.5f)
        }
    }

}