package com.example.app.view

import android.content.Context
import android.graphics.*
import com.example.app.R
import android.graphics.drawable.Animatable
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_text_view.*
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.fragment.app.FragmentTabHost
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.method.LinkMovementMethod
import org.xml.sax.XMLReader
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance


class TextViewActivity : BaseTitleActivity() {
    override fun initViews() {
        startAnimation(tv_animation)
        //字体
        val typeface = Typeface.createFromAsset(assets, "LCALLIG.TTF")
        tv_type_face.setTypeface(typeface)

        //渐变色
        val shader = LinearGradient(
                0.toFloat(),
                0.toFloat(),
                0.toFloat(),
                tv_gradient.getTextSize(),
                Color.RED,
                Color.BLUE,
                Shader.TileMode.CLAMP)
        tv_gradient.getPaint().setShader(shader)

        //bitmap
        val bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.cheetah)
        val shaderBitmap = BitmapShader(
                bitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT)
        tv_cheetah.getPaint().setShader(shaderBitmap)

        //html
        val html = getString(R.string.from_html_text)
        tv_html.movementMethod = LinkMovementMethod.getInstance()
        tv_html.text = Html.fromHtml(html, ResourceImageGetter(), null)

        //Rainbow

        val text = tv_rainbow.getText().toString()
        val spannableString = SpannableString(text)
        val substring = "abc"
        val start = text.toLowerCase().indexOf(substring)
        val end = start + substring.length
        spannableString.setSpan(RainbowSpan(this), start, end, 0)
    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "TextViewActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_text_view
    }

    private fun startAnimation(textView: TextView) {
        val drawables = textView.compoundDrawables
        for (drawable in drawables) {
            if (drawable != null && drawable is Animatable) {
                (drawable as Animatable).start()
            }
        }
    }

    inner class ResourceImageGetter : Html.ImageGetter {
        // Constructor takes a Context
        override fun getDrawable(source: String): Drawable {
            val path = resources.getIdentifier(source, "drawable", packageName)
            val drawable = ContextCompat.getDrawable(this@TextViewActivity, path)
            drawable!!.setBounds(0, 0,
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight)
            return drawable
        }
    }

    inner class RainbowSpan(context: Context) : CharacterStyle(), UpdateAppearance {
        var colors = intArrayOf()

        init {
            colors = context.getResources().getIntArray(R.array.rainbow)
        }

        override fun updateDrawState(paint: TextPaint) {
            paint.setStyle(Paint.Style.FILL)
            val shader = LinearGradient(
                    0.toFloat(), 0.toFloat(), 0.toFloat(),
                    paint.getTextSize() * colors.size,
                    colors, null,
                    Shader.TileMode.MIRROR)
            val matrix = Matrix()
            matrix.setRotate(90.toFloat())
            shader.setLocalMatrix(matrix)
            paint.setShader(shader)
        }
    }
}