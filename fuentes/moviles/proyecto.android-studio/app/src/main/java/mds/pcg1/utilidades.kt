package mds.pcg1.utilidades

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

