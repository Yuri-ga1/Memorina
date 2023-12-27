package com.example.memorina

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var image1: ImageView? = null
    private var image2: ImageView? = null

    private var max_cards_on_field = 16

    private val images = arrayOf(R.drawable.img1,R.drawable.img2,
        R.drawable.img3,R.drawable.img4,R.drawable.img5,
        R.drawable.img6,R.drawable.img7,R.drawable.img8)

    private var founded_images: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)

        layout.orientation = LinearLayout.VERTICAL

        val image_indexes = IntArray(max_cards_on_field){it}

        val indexes = IntArray(max_cards_on_field){it}
        val image_number = IntArray(max_cards_on_field/2){it}

        image_number.forEach{sprite ->
            for (i in 0 until 2){
                val i = (0 until max_cards_on_field).random()
                image_indexes[indexes[i]] = sprite
                indexes[i] = indexes[max_cards_on_field-1]
                max_cards_on_field--
            }
        }

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.toFloat() // единичный вес
        params.height = 580

        val cards = ArrayList<ImageView>()
        for (i in 0..15) {
            cards.add( // вызываем конструктор для создания нового ImageView
                ImageView(applicationContext).apply {
                    val t = image_indexes[i]
                    setImageResource(R.drawable.hos)
                    layoutParams = params
                    tag = "image_$t"
                    setOnClickListener(colorListener)
                })
        }

        val rows = Array(4, { LinearLayout(applicationContext) })

        var count = 0
        for (card in cards) {
            val row: Int = count / 4
            rows[row].addView(card)
            count ++
        }
        for (row in rows) {
            layout.addView(row)
        }
        setContentView(layout)

    }

    private suspend fun setBackgroundWithDelay(v: ImageView) {
        var idx = v.tag.toString().filter { it.isDigit() }.toInt()
        v.setImageResource(images[idx])
        delay(500)
        if (image1 == null)
        {
            image1 = v
        }
        else if (image2 == null && image1 != v)
        {
            image2 = v
        }
        if (image1 != null && image2 != null){
            if (image1!!.tag == image2!!.tag){
                image1!!.visibility = View.INVISIBLE
                image2!!.visibility = View.INVISIBLE

                image1 = null
                image2 = null

                founded_images += 2
                if (founded_images == 16){
                    Toast.makeText(this, "You won!",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                image1!!.setImageResource(R.drawable.hos)
                image2!!.setImageResource(R.drawable.hos)

                image1 = null
                image2 = null
            }
        }
    }

    // обработчик нажатия на кнопку
    @OptIn(DelicateCoroutinesApi::class)
    val colorListener = View.OnClickListener() {
        // запуск функции в фоновом потоке
        GlobalScope.launch (Dispatchers.Main)
        {
            setBackgroundWithDelay( it as ImageView ) }
    }
}