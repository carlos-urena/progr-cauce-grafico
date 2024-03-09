// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Implementación de la actividad principal
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clase: MainActivity
// **
// ** This program is free software: you can redistribute it and/or modify
// ** it under the terms of the GNU General Public License as published by
// ** the Free Software Foundation, either version 3 of the License, or
// ** (at your option) any later version.
// **
// ** This program is distributed in the hope that it will be useful,
// ** but WITHOUT ANY WARRANTY; without even the implied warranty of
// ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// ** GNU General Public License for more details.
// **
// ** You should have received a copy of the GNU General Public License
// ** along with this program.  If not, see <http://www.gnu.org/licenses/>.
// **
// *********************************************************************

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

import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*



// -------------------------------------------------------------------------------------------------

/**
 * Clase con tests para vectores y matrices
 */
class VecMatTest {
    public fun run()
    {
        val v2a = Vec2(1.0f, 2.0f)
        val v2b = Vec2(3.0f, 4.0f)
        var v2c = Vec2(3.0f, 4.0f)

        val v3a = Vec3(3.0f, 4.0f, 5.0f )
        val v3b = Vec3(3.0f, 4.0f, 6.0f )
        var v3c = v3a + v3b

        val v4 = Vec4( 1f, 2f, 3f, 4f )

        v2c = v2a + v2b ;

        Log.v( TAG, "v2a == $v2a")
        Log.v( TAG, "v2b == $v2b")
        Log.v( TAG, "v2c == $v2c")
        Log.v( TAG, "v3a == ${v3a}")
        Log.v( TAG, "v3b == ${v3b}")
        Log.v( TAG, "v3c == ${v3c}")
    }
}
// ---------------------------------------------------------------------------

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
        Log.v( TAG, "Inicialización de la aplicación finalizada - va test")

        var test = VecMatTest() ;
        test.run()
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