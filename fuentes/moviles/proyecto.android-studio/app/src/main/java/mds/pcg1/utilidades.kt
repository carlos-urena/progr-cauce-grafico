// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Funciones y variables auxiliares
// ** Copyright (C) 2024 Carlos Ureña
// **
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

package mds.pcg1.utilidades

import android.content.res.AssetManager
import android.util.Log
import android.opengl.GLES30
import mds.pcg1.OpenGLES30Activity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

val nfnd : String = "No disponible"  // nombre de función no disponible (para TAGF)

// -------------------------------------------------------------------------------------------------

/**
 * Etiqueta para mensajes impresos con Log.v
 * Ver: [esto](https://stackoverflow.com/questions/45841067/what-is-the-best-way-to-define-log-tag-constant-in-kotlin))
 * @return 'CUA +  nombre de la clase'
 */
val Any.TAG : String get()
    {
        val tag = javaClass.simpleName
        return "CUA $tag"
    }

// -------------------------------------------------------------------------------------------------

/**
 * Comprueba y desactiva la variable interna de error de OpenGL, si estaba activada, lanza una
 * excepción de error con el mensaje en [msg], en otro caso no hace nada.
 *
 * Descripciones de los errores obtenidas de:
 * https://registry.khronos.org/OpenGL-Refpages/es3.0/html/glGetError.xhtml
 */
fun ComprErrorGL( donde : String )
{
    val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"
    val codigo_error : Int = GLES30.glGetError()

    if ( codigo_error == GLES30.GL_NO_ERROR )
        return

    val descripcion_error : String = when ( codigo_error )
    {
        GLES30.GL_INVALID_ENUM -> "An unacceptable value is specified for an enumerated argument. The offending command is ignored and has no other side effect than to set the error flag."
        GLES30.GL_INVALID_VALUE -> "A numeric argument is out of range. The offending command is ignored and has no other side effect than to set the error flag."
        GLES30.GL_INVALID_OPERATION -> "The specified operation is not allowed in the current state. The offending command is ignored and has no other side effect than to set the error flag."
        GLES30.GL_INVALID_FRAMEBUFFER_OPERATION -> "The framebuffer object is not complete. The offending command is ignored and has no other side effect than to set the error flag."
        GLES30.GL_OUT_OF_MEMORY -> "There is not enough memory left to execute the command. The state of the GL is undefined, except for the state of the error flags, after this error is recorded."
        else -> "No hay descripción de texto disponible para este código de error."
    }

    Log.v( TAGF, "")
    Log.v( TAGF, "--------------------------------------------------")
    Log.v( TAGF, "Error de OpenGL en: $donde" )
    Log.v( TAGF, "Código de error == $codigo_error")
    Log.v( TAGF, "Descripción     == $descripcion_error")
    Log.v( TAGF, "--------------------------------------------------")
    Log.v( TAGF, "")


    throw Error( "$TAGF Error OpenGL en: $donde (ver descripción)" )

}
// -------------------------------------------------------------------------------------------------

/**
 * Lee un archivo con texto en la carpeta de assets.
 * Devuelve el archivo completo como un String (respetando las lineas).
 * Si hay cualquier problema al leer, se aborta con una excepción
 *
 * @param [nombre_archivo] Nombre del archivo con extensión,
 *                         puede incluir path, relativo a la carpeta de assets.
 * @return cadena con el nombre del archivo
 *
 * @see stackoverflow.com/questions/27574136/where-to-store-shader-code-in-android-app
 * @see stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
 *
 */
fun LeerArchivoEnAssets( nombre_archivo : String ) : String
{
    val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

    var assets : AssetManager = OpenGLES30Activity.instancia?.applicationContext?.assets
        ?: throw Error("$TAGF no puedo recuperar el 'Assets manager'")

    var istream: InputStream

    try {
        istream = assets.open( nombre_archivo )
    }
    catch( e : java.io.FileNotFoundException )
    {
        throw Error("$TAGF No encuentro el archivo '${nombre_archivo}' en la carpeta de assets.")
    }

    // leer istream en un String usando "Stream API"

    val texto_archivo : String = BufferedReader( InputStreamReader( istream ) )
        .lines().collect(Collectors.joining("\n"));

    istream.close()

    return texto_archivo
}

