package com.savage9ishere.tiwarimart.main_flow.ui.user.games.slot_machine

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.savage9ishere.tiwarimart.databinding.SlotMachineFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.user.games.slot_machine.imageViewScrolling.Common
import com.savage9ishere.tiwarimart.main_flow.ui.user.games.slot_machine.imageViewScrolling.IEventEnd
import com.savage9ishere.tiwarimart.main_flow.ui.user.games.slot_machine.imageViewScrolling.ImageViewScrolling
import kotlin.random.Random

class SlotMachineFragment : Fragment(), IEventEnd {

    private lateinit var upButtonImage : ImageView
    private lateinit var downButtonImage : ImageView
    private lateinit var image1 : ImageViewScrolling
    private lateinit var image2 : ImageViewScrolling
    private lateinit var image3 : ImageViewScrolling
    private lateinit var scoreText : TextView

    private lateinit var viewModel: SlotMachineViewModel

    private var countDone = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SlotMachineFragmentBinding.inflate(inflater)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        viewModel = ViewModelProvider(this).get(SlotMachineViewModel::class.java)

        upButtonImage = binding.btnUp
        downButtonImage = binding.btnDown
        image1 = binding.image
        image2 = binding.image2
        image3 = binding.image3
        scoreText = binding.txtScore

        val score = "Points : ${Common.score}"
        scoreText.text = score

        image1.setEventEnd(this)
        image2.setEventEnd(this)
        image3.setEventEnd(this)

        upButtonImage.setOnClickListener {
            if (Common.score >= 50) {
                upButtonImage.visibility = View.GONE
                downButtonImage.visibility = View.VISIBLE

                image1.setValueRandom(Random.nextInt(6), Random.nextInt((15 - 5) + 1) + 5)
                image2.setValueRandom(Random.nextInt(6), Random.nextInt((15 - 5) + 1) + 5)
                image3.setValueRandom(Random.nextInt(6), Random.nextInt((15 - 5) + 1) + 5)

                Common.score -= 50
                val score = "Points : ${Common.score}"
                scoreText.text = score
            }
            else {
                Toast.makeText(context, "Not Enough points, Come later", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun eventEnd(result: Int, count: Int) {
        if (countDone < 2) countDone++
        else {
            downButtonImage.visibility = View.GONE
            upButtonImage.visibility = View.VISIBLE
            countDone = 0

            if (image1.value == image2.value && image2.value == image3.value){
                Toast.makeText(context, "You win 150 points!!", Toast.LENGTH_SHORT).show()
                Common.score += 150
                val score = "Points : ${Common.score}"
                scoreText.text = score
            }
            else if (image1.value == image2.value || image2.value == image3.value || image3.value == image1.value){
                Toast.makeText(context, "You win 50 points!", Toast.LENGTH_SHORT).show()
                Common.score += 50
                val score = "Points : ${Common.score}"
                scoreText.text = score
            }
            else {
                Toast.makeText(context, "Sorry, better luck next time", Toast.LENGTH_SHORT).show()
            }
        }
    }

}