package mds.pcg1.objeto_visu

import android.util.Log
import mds.pcg1.vec_mat.*
import mds.pcg1.utilidades.*

open class ObjetoVisualizable()
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
