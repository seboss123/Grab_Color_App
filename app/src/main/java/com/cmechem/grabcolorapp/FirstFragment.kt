package com.cmechem.grabcolorapp

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var viewToChangeColor: View? = null
    var enableChangeButton = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewToChangeColor = view.findViewById(R.id.showColor2)

        view.findViewById<TextView>(R.id.colorText).setOnClickListener {
            viewToChangeColor = it
            displayColorChangeAlert()

        }


        view.findViewById<Button>(R.id.grabColorButton).setOnClickListener {
            if (enableChangeButton) {
                viewToChangeColor = it
            }
            grabColor()
        }
        view.findViewById<Button>(R.id.button2).setOnClickListener {
            if (enableChangeButton) {
                viewToChangeColor = it
            }
            grabColor()
        }

        view.findViewById<View>(R.id.showColor).setOnClickListener {
            viewToChangeColor = it
            Log.e("You selected", viewToChangeColor.toString())
            displayColorChangeAlert()
        }
        view.findViewById<View>(R.id.showColor2).setOnClickListener {
            viewToChangeColor = it
            displayColorChangeAlert()
        }
        changeButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableChangeButton = true
            } else {
                enableChangeButton = false
                viewToChangeColor = null
            }
            Log.e("State is", enableChangeButton.toString())

        }
    }

    //Open Colorpicking app
    fun grabColor() {
        val intent = Intent(Intent.ACTION_SEND)
        val chooser: Intent = Intent.createChooser(intent, "Please Choose a Color Picker")
        startActivityForResult(chooser, 1)
    }

    //Dialog to ask if you would like to change color of View
    fun displayColorChangeAlert() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Would you like to change the color?")
            ?.setPositiveButton("Yes", { dialog, id ->
                Log.e("This Worked", "LOLDASDA")
                grabColor()
            })
            ?.setNegativeButton("No", { dialog, id ->
            })
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    @SuppressLint("ObjectAnimatorBinding")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == android.app.Activity.RESULT_OK) {
                val extras: Bundle? = data?.extras
                val anim = AlphaAnimation(1.0f, 0.0f)
                val viewColor1 = showColor.background as ColorDrawable
                val oldColor1: Int = viewColor1.color
                val viewColor2 = showColor2.background as ColorDrawable
                val oldColor2: Int = viewColor2.color
                Log.e("Color is", oldColor1.toString())

                var red: Int? = extras?.getInt("Red")
                var green: Int? = extras?.getInt("Green")
                var blue: Int? = extras?.getInt("Blue")
                var backgroundColorChanger: ObjectAnimator


                if (red != null && green != null && blue != null) {
                    if (viewToChangeColor == view?.findViewById(
                            R.id.showColor
                        )
                    ) {
                        textView1.text = ""
                        Log.e("Color is:", red.toString())
                        backgroundColorChanger = ObjectAnimator.ofObject(
                            viewToChangeColor, "backgroundColor", ArgbEvaluator(),
                            oldColor1, Color.rgb(red, green, blue)
                        )
                        backgroundColorChanger.duration = 1000
                        backgroundColorChanger.start()
                    }
                    //Change color of View2
                    else if (viewToChangeColor == view?.findViewById(
                            R.id.showColor2
                        )
                    ) {
                        textView2.text = ""
                        Log.e("Color is:", red.toString())
                        backgroundColorChanger = ObjectAnimator.ofObject(
                            viewToChangeColor, "backgroundColor", ArgbEvaluator(),
                            oldColor2, Color.rgb(red, green, blue)
                        )
                        backgroundColorChanger.duration = 1000
                        backgroundColorChanger.start()
                    }
                    //Different method to animate the color change of buttons
                    else if (viewToChangeColor is Button) {
                        Log.e("Color is:", red.toString())
                        var rippleColor: Int = 0

                        val rippleDrawable = viewToChangeColor!!.background
                        val state: Drawable.ConstantState = rippleDrawable.constantState!!
                        try {
                            val colorField = state.javaClass.getDeclaredField("color")
                            colorField.isAccessible = true
                            val colorStateList = colorField.get(state) as ColorStateList
                            rippleColor = colorStateList.defaultColor
                        } catch (e: NoSuchFieldException) {
                            e.printStackTrace()
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }

                        backgroundColorChanger = ObjectAnimator.ofObject(
                            viewToChangeColor, "backgroundColor", ArgbEvaluator(),
                            rippleColor, Color.rgb(red, green, blue)
                        )
                        backgroundColorChanger.duration = 1000
                        backgroundColorChanger.start()
                    }
                    else if (viewToChangeColor is TextView){
                        backgroundColorChanger = ObjectAnimator.ofObject(
                            viewToChangeColor, "textColor",ArgbEvaluator(),oldColor1, Color.rgb(red, green, blue)

                        )

                        backgroundColorChanger.duration = 1000
                        backgroundColorChanger.start()
                    }
                }
            }


        } catch (e: NullPointerException) {
            Log.e("ERROr", e.toString())
            Toast.makeText(
                context, "Please select a widget to change color",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Log.e("ERROr", e.toString())
            Toast.makeText(
                context, e.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
