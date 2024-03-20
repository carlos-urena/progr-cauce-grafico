// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con texturas
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clase: Textura
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

package mds.pcg1.texturas

import android.graphics.Bitmap
import android.opengl.GLES30
import android.util.Log

import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.IntBuffer.*

import mds.pcg1.utilidades.*
import mds.pcg1.cauce.*


// -------------------------------------------------------------------------------------

class Textura( p_nombre_archivo : String )
{
    private var nombre_archivo : String  = p_nombre_archivo
    private var imagen         : Imagen  = LeerArchivoImagen( nombre_archivo )
    private var texture        : Int     = 0


    // --------------------------------------------------------------
    // variables de instancia estáticas ('static'), no específicas de una instancia

    companion object
    {
        // Textura actualmente activada en el cauce (se usa para push/pop)
        // (si es null es que no hay textura activada)

        var actual : Textura? = null

        // Pila de texturas

        var pila   : MutableList<Textura?> = mutableListOf()

        fun push(  )
        {
            pila.add( actual )
        }
        fun pop( cauce : CauceBase )
        {
            assert( pila.size > 0 ) {"pila vacía intentado hacer pop de textura"}
            var textura : Textura? = pila.last()
            pila.removeLast()

            if ( textura != null )
                textura.activar( cauce )
            else
                cauce.fijarEvalText( false, 0  )
        }
        fun fijar( textura : Textura? , cauce : CauceBase )
        {
            textura?.activar( cauce ) ?: cauce.fijarEvalText( false, 0 )
            actual = textura
        }
    }

    fun crearTexturaGLES( )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        ComprErrorGL( "${TAGF} al inicio")

        val level       : Int = 0
        val internalFmt : Int = GLES30.GL_RGB
        val srcFmt      : Int = GLES30.GL_RGB
        val srcType     : Int = GLES30.GL_UNSIGNED_BYTE
        val border      : Int = 0

        // create, bind and fill the texture
        var ids_texturas = allocate(1)

        GLES30.glGenTextures( 1, ids_texturas )
        texture = ids_texturas[0]

        GLES30.glBindTexture( GLES30.GL_TEXTURE_2D, texture )

        GLES30.glTexImage2D( GLES30.GL_TEXTURE_2D, level, internalFmt, imagen.ancho, imagen.alto, border,
                              srcFmt, srcType, imagen.pixels  )
        // ???
        //GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE )
        //GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE )
        //GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR )

        GLES30.glGenerateMipmap( GLES30.GL_TEXTURE_2D )
        GLES30.glBindTexture( GLES30.GL_TEXTURE_2D, 0 )
        ComprErrorGL( "${TAGF} al final")
    }

    fun activar( cauce : CauceBase  )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        if ( texture == 0 )
            crearTexturaGLES()

        Textura.actual = this
        cauce.fijarEvalText( true, texture )

        ComprErrorGL( "${TAGF} al final" )
    }
}
