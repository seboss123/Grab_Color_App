package com.cmechem.grabcolorapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        view.findViewById<Button>(R.id.grabColorButton).setOnClickListener{
           val intent = Intent(Intent.ACTION_PICK)
            val chooser :Intent = Intent.createChooser(intent,"Please Choose a Color Picker")
            startActivityForResult(chooser,1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       try {
           super.onActivityResult(requestCode, resultCode, data)
           if (resultCode==android.app.Activity.RESULT_OK){
               val anim = AlphaAnimation(1.0f, 0.0f)
               val color = data?.getStringExtra("Color")
               if (color!=null) Log.e("Color is:",color)

               anim.duration = 2000
               anim.repeatCount = 1
               anim.repeatMode = Animation.REVERSE

               anim.setAnimationListener(object : Animation.AnimationListener {
                   override fun onAnimationEnd(animation: Animation?) { }
                   override fun onAnimationStart(animation: Animation?) { }
                   override fun onAnimationRepeat(animation: Animation?) {
                       textview_first.text=color

                   }
               })

               textview_first.startAnimation(anim)

           }


       }
       catch (e:Exception){
           Toast.makeText(
               context, e.toString(),
               Toast.LENGTH_SHORT).show();
       }
    }
}
