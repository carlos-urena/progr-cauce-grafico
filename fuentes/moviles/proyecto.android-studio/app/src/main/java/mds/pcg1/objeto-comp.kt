package mds.pcg1.objeto_comp

import android.opengl.GLES30
import mds.pcg1.aplicacion.AplicacionPCG
import mds.pcg1.malla_ind.MallaIndHelloRectangleXY
import mds.pcg1.malla_ind.MallaIndHelloRectangleXZ
import mds.pcg1.objeto_visu.*
import mds.pcg1.utilidades.nfnd


// *************************************************************************************************
/**
 * Una clase para objetos visualizables que están compuestos de listas de otros objetos visualizables
 */
open class ObjetoVisCompuesto : ObjetoVisualizable()
{

    protected var sub_objetos : MutableList<ObjetoVisualizable> = mutableListOf()

    /**
     * Visualiza el objeto compuesto
     */
    override fun visualizar()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        val cauce = AplicacionPCG.instancia.leer_cauce

        assert( sub_objetos.size > 0 ) {"$TAGF el array de subobjetos está vacío"}

        guardarCambiarEstado( cauce )

        for( obj in sub_objetos )
            obj.visualizar()

        restaurarEstado( cauce )
    }

    /**
     * Visualiza las aristas del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene aristas que se pueden visualizar
     * o que no se ha implementado esto)
     */
    override fun visualizarAristas()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        //val cauce = AplicacionPCG.instancia.leer_cauce

        for( obj in sub_objetos )
            obj.visualizarAristas()
    }
}
// *************************************************************************************************

/**
 * Una clase de test para objetos compuestos
 */
class DosCuadrados : ObjetoVisCompuesto()
{
    init
    {
        nombre = "Dos cuadrados"
        sub_objetos.add( MallaIndHelloRectangleXY() )
        sub_objetos.add( MallaIndHelloRectangleXZ() )
    }
}

