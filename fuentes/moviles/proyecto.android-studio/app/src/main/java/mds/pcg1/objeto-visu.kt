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

    val tieneColor : Boolean get() = color != null

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
}
