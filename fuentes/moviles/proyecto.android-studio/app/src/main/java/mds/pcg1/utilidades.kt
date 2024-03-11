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

import android.util.Log
import android.opengl.GLES20

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
 */
fun ComprErrorGL( msg : String )
{
    val error : Int = GLES20.glGetError()

    if ( error != GLES20.GL_NO_ERROR )
    {
        Log.v( "ComprErrorGL", msg )
        throw Error(msg)
    }
}
// -------------------------------------------------------------------------------------------------

/**
 * Devuelve el nombre de la función que la llama
 * (ver: https://www.baeldung.com/kotlin/name-of-currently-executing-function)
 */
fun nombreFunc(): String {
    return object {}.javaClass.enclosingMethod.name
}

