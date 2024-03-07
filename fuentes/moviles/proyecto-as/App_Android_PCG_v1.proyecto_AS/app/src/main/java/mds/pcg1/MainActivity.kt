package mds.pcg1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import mds.pcg1.ui.theme.AppAndroidPCGV1Theme

import android.util.Log

// añadir campo TAG a todas las clases, devuelve el nombre de la clase cada vez que se evalua.
// este TAG es útil para los 'Logs' en el logcat (visto en S.O.)

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return "CUA: $tag"
    }

// actividad principal.
class MainActivity : ComponentActivity()
{
    //private  val TAG : String? = "CUA" // MainActivity::class.simpleName
    override fun onCreate( savedInstanceState: Bundle? ) {
        Log.v( TAG, "punto 1")
        super.onCreate( savedInstanceState )
        Log.v( TAG, "punto 2")
        setContent {
            AppAndroidPCGV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("App PCG Android v1")
                }
            }
        }
        Log.v( TAG, "punto 3")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola desde $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAndroidPCGV1Theme {
        Greeting("App PCG Android")
    }
}