package com.savage9ishere.tiwarimart.main_flow.ui.user.games.roulette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.databinding.RouletteFragmentBinding
import java.util.*

class RouletteFragment : Fragment() {

    private lateinit var viewModel: RouletteViewModel

    private lateinit var spinBtn : Button
    private lateinit var resultText : TextView
    private lateinit var wheelImage : ImageView

    // We create a Random instance to make our wheel spin randomly
    private val RANDOM = Random()
    private var degree = 0
    private  var degreeOld:Int = 0

    // We have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector
    // we divide by 2 to have a half sector
    private val HALF_SECTOR = 360f / 37f / 2f

    private val sectors = arrayOf(
        "32 red", "15 black",
        "19 red", "4 black", "21 red", "2 black", "25 red", "17 black", "34 red",
        "6 black", "27 red", "13 black", "36 red", "11 black", "30 red", "8 black",
        "23 red", "10 black", "5 red", "24 black", "16 red", "33 black",
        "1 red", "20 black", "14 red", "31 black", "9 red", "22 black",
        "18 red", "29 black", "7 red", "28 black", "12 red", "35 black",
        "3 red", "26 black", "zero"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RouletteFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(RouletteViewModel::class.java)



        spinBtn = binding.spinBtn
        resultText = binding.resultTv
        wheelImage = binding.wheel

        spinBtn.setOnClickListener {
            degreeOld = degree % 360
            degree = RANDOM.nextInt(360) + 720

            val rotateAnim = RotateAnimation(
                degreeOld.toFloat(),
                degree.toFloat(),
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateAnim.duration = 3600
            rotateAnim.fillAfter = true
            rotateAnim.interpolator = DecelerateInterpolator()
            rotateAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    resultText.text = ""
                }

                override fun onAnimationEnd(animation: Animation?) {
                    resultText.text = getSector(360 - (degree % 360))
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })

            wheelImage.startAnimation(rotateAnim)
        }

        return binding.root
    }

    private fun getSector(degrees: Int): String? {
        var i = 0
        var text: String? = null
        do {
            // start and end of each sector on the wheel
            val start: Float = HALF_SECTOR * (i * 2 + 1)
            val end: Float = HALF_SECTOR * (i * 2 + 3)
            if (degrees >= start && degrees < end) {
                // degrees is in [start;end[
                // so text is equals to sectors[i];
                text = sectors[i]
            }
            i++
            // now we can test our Android Roulette Game :)
            // That's all !
            // In the second part, you will learn how to add some bets on the table to play to the Roulette Game :)
            // Subscribe and stay tuned !
        } while (text == null && i < sectors.size)
        return text
    }

}