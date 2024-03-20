// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con objetos visualizables
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clase: ObjetoVisualizable
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

package mds.pcg1.objeto_visu

import android.util.Log
import mds.pcg1.aplicacion.AplicacionPCG
import mds.pcg1.cauce.CauceBase
import mds.pcg1.texturas.*
import mds.pcg1.vec_mat.*
import mds.pcg1.utilidades.*

open class ObjetoVisualizable
{
    var nombre : String = "no asignado" // nombre del objeto
        get() = field
        set( nuevo_nombre ) { field = nuevo_nombre }

    var color : Vec3? = null // color del objeto, null si no tiene (inicialmente no tiene)
        get() = field
        set( nuevo_color ) { field = nuevo_color }

    //val tieneColor : Boolean get() = color != null

    var textura : Textura? = null
        get() = field
        set( nueva_textura ) { field = nueva_textura }

    /**
     * Visualiza el objeto. este método debe ser redefinido en clases derivadas
     */
    open fun visualizar()
    {
        throw Error("El objeto '${nombre}' no tiene redefinido el método 'visualizar'")
    }

    /**
     * Visualiza las aristas del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene aristas que se pueden visualizar
     * o que no se ha implementado esto)
     */
    open fun visualizarAristas()
    {
        Log.v( TAG, "El objeto '${nombre}' no tiene método para visualizar aristas ('visualizarAristas')" )
    }

    /**
     * Visualiza las normales del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene normales que se pueden visualizar
     * o que no se ha implementado esto)
     */
    open fun visualizarNormales()
    {
        Log.v( TAG, "El objeto '${nombre}' no tiene método para visualizar normales ('visualizarNormales')." )
    }

    /**
     * guardar el estado de ciertos uniforms del cauce, y los cambia según este objeto
     *   - color
     *   - textura (que puede ser nula)
     *
     *   TODO: las texturas no se deberían guardar en el companion object, sino en una pila propia de cada cauce.
     */
    fun guardarCambiarEstado( cauce : CauceBase)
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        // si tiene color, guardar estado de color y cambiarlo
        if ( color != null )
        {   cauce.pushColor()
            cauce.fijarColor( color )
        }

        // guardar el estado de las texturas y cambiarlo
        // (la textura no se 'hereda' como el color, o bien el objeto tiene (textura!= null) o no tiene (textura == null)
        Textura.push( )
        Textura.fijar( textura, cauce )
    }

    /**
     * restaura el estado de ciertos uniforms del cauce (debe estar pareado con 'guardarEstado')
     */
    fun restaurarEstado( cauce : CauceBase )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        // si tiene color, restaurar el estado de color
        if ( color != null )
            cauce.popColor()

        // restaurar el estado de las texturas
        Textura.pop( cauce )
    }
}
