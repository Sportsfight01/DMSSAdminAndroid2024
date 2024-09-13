package com.dmss.dmssadminmaintanance

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dmss.dmssadminmaintanance.ui.theme.DMSSAdminMaintananceTheme


class SplashScreenActivity : ComponentActivity() {
    private lateinit var fasTagPref: SharedPreferences
    var USER_roleId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContent {
            DMSSAdminMaintananceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    getSplashImage(
//                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
//                AppConstants.slideToRightAnim(this)



        }, 1000)
    }
@Composable
fun getSplashImage(modifier: Modifier = Modifier){
    ImageExample()
    }
    @Composable
    fun ImageExample() {
        Column(
            // we are using column to align our
            // imageview to center of the screen.
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),

            // below line is used for specifying
            // vertical arrangement.
            verticalArrangement = Arrangement.Center,

            // below line is used for specifying
            // horizontal arrangement.
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // below line is used for creating a variable
            // for our image resource file.
            val painter = painterResource(id = R.drawable.dmss_jpg_logo)

            // below is the composable for image.
            Image(
                // first parameter of our Image
                // is our image path which we have created
                // above
                painter = painter,
                contentDescription = "Sample Image",

                // below line is used for creating a modifier for our image
                // which includes image size, padding and border
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
//                    .border(2.dp, Color.Black, CircleShape),

                // below line is used to give
                // alignment to our image view.
//                alignment = Alignment.Center,

                // below line is used to scale our image
                // we are using center crop for it.
//                contentScale = ContentScale.Crop,

                // below line is used to define the opacity of the image.
                // Here, it is set to the default alpha value, DefaultAlpha.
//                alpha = DefaultAlpha,
            )
        }
    }
}


