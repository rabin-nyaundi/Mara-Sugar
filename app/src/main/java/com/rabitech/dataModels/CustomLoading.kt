package com.rabitech.dataModels

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import com.rabitech.R
import kotlinx.android.synthetic.main.loading_dialog.view.*

class CustomLoading {

    lateinit var loadingDialog: Dialog



    @SuppressLint("ResourceAsColor")
    fun show(context: Context, title: CharSequence?):Dialog{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.loading_dialog,null)


        view.progress_layout.setBackgroundColor(Color.parseColor("#70000000"))
        setColorFilter(view.progressBar.indeterminateDrawable,
            ResourcesCompat.getColor(context.resources, R.color.accent, null))
        view.progress_text.setTextColor(Color.BLUE)


        view.progressBar.visibility = View.VISIBLE

        if(title != null){
            view.progress_text.text = title
        }

        loadingDialog = Dialog(context, R.style.CustomProgressBarTheme)
        loadingDialog.setContentView(view)
        loadingDialog.show()

        return  loadingDialog
    }

    fun setColorFilter(@NonNull drawable: Drawable, color:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}