

# Creación del proyecto desde cero.



Lanzo Android Studio y en la pantalla inicial selecciono _New Project_

Hay que elegir el tipo de proyecto, el lenguaje, la carpeta, etc... todo lo que se describe aquí son pruebas:


**Tipo de proyecto**: Empty Activity

**Nombre**: "App PCG Moviles"

**Package name**:

Para el _package name_, tener en cuenta esto sobre dicho nombre: 

_The package name of an Android app uniquely identifies your app on the device, in Google Play Store, and in supported third-party Android stores_ (de: [Google Help](https://support.google.com/admob/answer/9972781))

así que elijo **ugr.mds.pcg.movi**


**Save location**:  elijo una carpeta dentro del repositorio de la asignatura, en concreto eligo dentro de `proyecto-as/AppPCGMoviles`

**Minimun SDK**: API 24

Espero a que se descargue todo, me ha creado una aplicación con un archivo principal en Kotlin (`MainActivity.kt`), con una declaracion de la clase `MainActivity`


``` 
package ugr.mds.pcg.movi

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
import ugr.mds.pcg.movi.ui.theme.AppPCGMovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPCGMovilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting( name: String, modifier: Modifier = Modifier ) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview( showBackground = true )
@Composable
fun GreetingPreview() {
    AppPCGMovilesTheme {
        Greeting("Android")
    }
}
``` 

Lo ha creado todo dentro de la `AppPCGMoviles` (nombre del proy, sin espacios), tiene 724k en este momento. me salgo del editor. Veo que AS ha añadido cosas al repo del github, no todas, yo añado todo lo que faltaba.



Seguir lo que se dice aquí:

(Vistas OpenGL)[https://developer.android.com/develop/ui/views/graphics/opengl?hl=es-419]

Sobre comprobar la versión, mirar:

[Sobre OpenGL: comprobación de versiones](https://developer.android.com/develop/ui/views/graphics/opengl/about-opengl#version-check)


















