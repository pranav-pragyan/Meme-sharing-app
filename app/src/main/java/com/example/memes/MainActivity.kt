package com.example.memes

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentMemeImage:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme()
    {
        val proBar: ProgressBar = findViewById(R.id.progress_bar)
        proBar.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

       // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Display the first 500 characters of the response string.
                currentMemeImage= response.getString("url")// I only need url field from API.
                val myImage: ImageView = findViewById(R.id.meme_image)
                Glide.with(this).load(currentMemeImage).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        proBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        proBar.visibility = View.GONE
                        return false
                    }
                }).into(myImage)// using Glide library we are taking image url
                // and then putting the image into the imageView.

            },
            {
                Toast.makeText(this, "Something Went Wrong",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)   // function to send the memes
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this meme from Raddit $currentMemeImage")
        val chooser = Intent.createChooser(intent,"share this meme using...") // createchooser shows multiple sharing app to share with
        startActivity(chooser)

    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}