package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageUrl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       loadMeme()
    }

   private fun loadMeme(){
       progress.visibility = View.VISIBLE  //The progress.bisibility ka progress word is from the id of progress bar of the xml file...so to access anything from sml u need an id
        val textView = findViewById<TextView>(R.id.text)
// ...

// Instantiate the RequestQueue.

        val url = "https://meme-api.herokuapp.com/gimme"
       /* this val url is for the web url and the currentimageurl defined in main and used in upcoming jsonobject is for storing the image ka link
         which in this api is stored under the name url in the form of string */

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
                { response ->
                       currentImageUrl = response.getString("url")
                      // the url is double codes is a part of the json object which we call.. it is a id which has image ka link with it thich is converted to string here
                      // in next line we will use glide library to get image frm that "url" json object which we converted to string..
                      // REMEMBER THIS double codded url is diff from the variable url

                    /*NOW there can be a delay in loading meme which is caused by delay of image load (done by glide hence progressbar call in glide)and not the url
                    hence we want to invoke a progress bar which is done by adding the
                      .listener(object : RequestListener<drawable>{  which in itself has 2 functions called onLoadFailed and onResourceReady ie when image is load succesfully })  */


                    Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                          progress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progress.visibility = View.GONE
                            return false
                        }
                    }).into(imageView)
                  },
                {Toast.makeText(this,"ERROR", Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


   }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        // the intent type is important as it will decide which app can share the meme and the app will show those apps only which can share that type of content
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this meme $currentImageUrl")

        // below is a chooser used to create the send to apps wala optn where in u see the apps where u can share the meme
        val chooser = Intent.createChooser(intent, "Share this meme using")
        startActivity(chooser)

    }
    fun nextMeme(view: View) {
      loadMeme()
    }


}