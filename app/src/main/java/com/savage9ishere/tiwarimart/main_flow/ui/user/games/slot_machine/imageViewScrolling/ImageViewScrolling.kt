package com.savage9ishere.tiwarimart.main_flow.ui.user.games.slot_machine.imageViewScrolling

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.savage9ishere.tiwarimart.R

class ImageViewScrolling : FrameLayout {
    var current_image: ImageView? = null
    var next_image: ImageView? = null
    var last_result = 0
    var old_value = 0
    var eventEndd: IEventEnd? = null

    fun setEventEnd(eventEnd: IEventEnd?) {
        this.eventEndd = eventEnd
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(
            R.layout.image_view_scrolling, this
        )
        current_image = rootView.findViewById<View>(R.id.current_image) as ImageView
        next_image = rootView.findViewById<View>(R.id.next_image) as ImageView
        next_image!!.translationY = height.toFloat()
    }

    fun setValueRandom(image: Int, rotate_count: Int) {
        current_image!!.animate().translationY(-height.toFloat())
            .setDuration(ANIMATION_DUR.toLong()).start()
        next_image!!.translationY = next_image!!.height.toFloat()
        next_image!!.animate().translationY(0f)
            .setDuration(ANIMATION_DUR.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    setImage(current_image, old_value % 6) //Since we have 6 images
                    current_image!!.translationY = 0f
                    if (old_value != rotate_count) {
                        //if old value still not equal rotate count,we will still roll
                        setValueRandom(image, rotate_count)
                        old_value++
                    } else  //if rotate is reached
                    {
                        last_result = 0
                        old_value = 0
                        setImage(next_image, image)
                        eventEndd!!.eventEnd(image % 6, rotate_count)
                    }
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
    }

    private fun setImage(image_view: ImageView?, value: Int) {
        if (value == Util.BAR) image_view!!.setImageResource(R.drawable.bar_done) else if (value == Util.SEVEN) image_view!!.setImageResource(
            R.drawable.sevent_done
        ) else if (value == Util.ORANGE) image_view!!.setImageResource(R.drawable.orange_done) else if (value == Util.LEMON) image_view!!.setImageResource(
            R.drawable.lemon_done
        ) else if (value == Util.TRIPLE) image_view!!.setImageResource(R.drawable.triple_done) else image_view!!.setImageResource(
            R.drawable.waternelon_done
        )

        //set tag for image for use to compare result
        image_view.tag = value
        last_result = value
    }

    val value: Int
        get() = next_image!!.tag.toString().toInt()

    companion object {
        private const val ANIMATION_DUR = 150
    }
}