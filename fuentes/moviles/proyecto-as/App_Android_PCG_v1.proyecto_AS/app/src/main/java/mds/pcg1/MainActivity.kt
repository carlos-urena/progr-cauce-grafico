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

class MainActivity : ComponentActivity()
{
    override fun onCreate( savedInstanceState: Bundle? ) {

        super.onCreate( savedInstanceState )

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
        Log.v( TAG, "Inicialización de la aplicación finalizada")
    }
}
// ----------------------------------------------------------------------------

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola desde $name!",
        modifier = modifier
    )
}
// ----------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAndroidPCGV1Theme {
        Greeting("App PCG Android")
    }
}