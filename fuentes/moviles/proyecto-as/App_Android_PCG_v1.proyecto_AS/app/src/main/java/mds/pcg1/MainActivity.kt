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