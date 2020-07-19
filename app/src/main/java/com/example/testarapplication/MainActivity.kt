package com.example.testarapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private val augmentedImageMap = HashMap<AugmentedImage, AugmentedImageNode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        arFragment = fragment as ArFragment

        arFragment.arSceneView.scene.addOnUpdateListener {
            val curentFrame = arFragment.arSceneView.arFrame
            if(curentFrame != null && curentFrame.camera.trackingState == TrackingState.TRACKING){
                updateTrackedImages(curentFrame)
            }
        }
    }

    private fun updateTrackedImages(frame: Frame){
        val imageList = frame.getUpdatedTrackables(AugmentedImage::class.java)
        for(image in imageList){
            if(image.trackingState == TrackingState.TRACKING){
                if(!augmentedImageMap.containsKey(image)){
                    AugmentedImageNode(this).apply {
                        setAugmentedImage(image)
                        augmentedImageMap[image] = this
                        arFragment.arSceneView.scene.addChild(this)
                    }
                }
            }else if(image.trackingState == TrackingState.STOPPED){
                augmentedImageMap.remove(image)
            }
        }
    }

}
// Target Test