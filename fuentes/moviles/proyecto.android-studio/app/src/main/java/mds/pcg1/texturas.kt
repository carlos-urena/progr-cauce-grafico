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
import mds.pcg1.utilidades.*
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.IntBuffer.allocate


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

        var pila   : Array<Textura> = emptyArray()
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
                              srcFmt, srcType, ByteBuffer.wrap( imagen.pixels)  )

        GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE )
        GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE )
        GLES30.glTexParameteri( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR )
        /**
        // Generate MIPMAPS ....
        // WebGL1 has different requirements for power of 2 images
        // vs non power of 2 images so check if the image is a
        // power of 2 in both dimensions.
        if ( esPotenciaDe2( img.width ) && esPotenciaDe2( img.height ))
        {
            // Yes, it's a power of 2. Generate mips.
            gl.generateMipmap( gl.TEXTURE_2D )
            Log(`${nombref} generado mip-map.`)
        }
        else
        {
            // No, it's not a power of 2. Turn off mips and set
            // wrapping to clamp to edge
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR)
            Log(`${nombref} mip-map no generado.`)
        }
        **/
        GLES30.glBindTexture( GLES30.GL_TEXTURE_2D, 0 )
        ComprErrorGL( "${TAGF} al final`)


    }
}

/**
 * COPIA de Javascript

// -------------------------------------------------------------------------------------

/**
 * Clase que encapsula una imagen usada como textura y el
 * correspondiente objeto textura de WebGL
 */
export class Textura
{
   private url          : string = ""
   private elemento_img : HTMLImageElement | null = null
   private texture      : WebGLTexture | null = null

   // --------------------------------------------------------------
   // variables de instancia estáticas ('static'), no específicas de una instancia

   // Textura actualmente activada en el cauce (se usa para push/pop)
   // (si es null es que no hay textura activada)
   private static actual : Textura | null = null

   // pila de texturas, inicialmente vacía
   private static pila : Array<Textura|null> = []

   // -----------------------------------------------------------------

   constructor( url : string  )
   {
      const nombref : string = 'Textura.constructor:'
      Assert( url != "" , `${nombref} la url está vacía`)
      this.url = url
   }

   async leer() : Promise<void>
   {
      const nombref : string = 'Textura.leer:'
      Assert( this.elemento_img == null , `${nombref} no se puede leer una textura ya leída (this.url)`)
      this.elemento_img = await LeerArchivoImagen( this.url )

      Log(`${nombref} textura '${this.url}' cargada, dimensiones == ${this.elemento_img.width} x ${this.elemento_img.height}`)
   }

   crearTexturaWebGL( ) : void
   {
      const nombref : string = 'Textura.crearTexturaWebGL'
      if ( this.elemento_img == null )
         throw Error(`${nombref} no se puede crear el objeto textura WebGL si la imagen no está cargada`)
      Assert( this.texture == null, `${nombref} no se puede crear la textura dos veces`)

      let img : HTMLImageElement = this.elemento_img
      let gl = AplicacionPCG.instancia.gl

      ComprErrorGL( gl, `${nombref} al inicio`)

      const
        level       : number = 0,
        internalFmt : number = gl.RGB,
        srcFmt      : number = gl.RGB,   //// ---> como saberlo ??
        srcType     : number = gl.UNSIGNED_BYTE

      // create, bind and fill the texture
      this.texture = gl.createTexture()
      gl.bindTexture( gl.TEXTURE_2D, this.texture )
      gl.texImage2D( gl.TEXTURE_2D, level, internalFmt, srcFmt, srcType, img )

      // Generate MIPMAPS ....
      // WebGL1 has different requirements for power of 2 images
      // vs non power of 2 images so check if the image is a
      // power of 2 in both dimensions.
      if ( esPotenciaDe2( img.width ) && esPotenciaDe2( img.height ))
      {
         // Yes, it's a power of 2. Generate mips.
         gl.generateMipmap( gl.TEXTURE_2D )
         Log(`${nombref} generado mip-map.`)
      }
      else
      {
         // No, it's not a power of 2. Turn off mips and set
         // wrapping to clamp to edge
         gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE)
         gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE)
         gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR)
         Log(`${nombref} mip-map no generado.`)
      }

      gl.bindTexture( gl.TEXTURE_2D, null )
      ComprErrorGL( gl, `${nombref} al final`)


   }
   // -----------------------------------------------------------------------------------------

   activar(  ) : void
   {
      const nombref : string = 'Textura.activar:'
      let gl = AplicacionPCG.instancia.gl
      let cauce : Cauce = AplicacionPCG.instancia.cauce

      ComprErrorGL( gl, `${nombref} al inicio` )

      if ( this.texture == null )
         this.crearTexturaWebGL()

      Textura.actual = this
      cauce.fijarEvalText( true, this.texture )

      ComprErrorGL( gl, `${nombref} al final` )
   }
   // -------------------------------------------------------------------------------------
   // Métodos estáticos ('static') o de clase (no se ejecutan sobre una instancia)

   public static desactivar(  ) : void
   {
      let cauce : Cauce = AplicacionPCG.instancia.cauce
      Textura.actual = null
      cauce.fijarEvalText( false, null )
   }
   // --------------------------------------------------------------------


   /**
    * Crea un objeto textura y espera a leerlo desde el servidor
    *
    * @param url (string) URL del archivo de textura en el servidor
    * @returns (Textura) textura leída
    */
   public static async crear( url : string ) : Promise<Textura>
   {
      const nombref : string = 'LeerTextura:'
      let textura = new Textura( url )
      await textura.leer()
      return textura
   }
   // --------------------------------------------------------------------

   /**
    * Hace push (en la pila de texturas) de la textura actualmente activada
    * (si no hay ninguna textura activada, introduce 'null')
    */
   public static push() : void
   {
      const nombref : string = "Material.push:"
      Textura.pila.push( Textura.actual )
   }
   // --------------------------------------------------------------------

   /**
    * Hace pop de la pila de texturas (la pila no puede estar vacía)
    * - Lee la textura en el top de la pila.
    * - Si esa textura es 'null', desactiva las texturas, en otro caso activa esa textura.
    *
    */
   public static pop() : void
   {
      const nombref : string = "Material.pop:"
      let pt = Textura.pila

      if ( pt.length == 0 )
         throw new Error(`${nombref} la pila está vacía`)

      let text = pt[ pt.length-1 ]
      pt.pop()

      if ( text == null )
         Textura.desactivar()
      else
         text.activar()
   }
}
// -----------------------------------------------------------------------------------------






 */

