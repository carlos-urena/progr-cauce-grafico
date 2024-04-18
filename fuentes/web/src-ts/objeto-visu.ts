
import { Log } from "./utilidades.js"
import { Vec3, Mat4 } from "./vec-mat.js"
import { Cauce } from "./cauce.js"

export class ObjetoVisualizable
{
    
    private color       : Vec3 | null = null // color del objeto, null si no tiene

    /**
     * Matriz de modelado de este objeto (si no es nula),
     * Si está presente, es adicional a la que haya establecida en el cauce
     * (se hace pushMM antes de visualizar y popMM después)
     */
    private matrizm : Mat4 | null = null

    /**
     * Devuelve 'true' si la matriz de modelado no es nula
     */
    public get tieneMatrizModelado() : boolean 
    {
        return this.matrizm != null 
    }
    /**
     * Devuelve la matriz de modelado (no nula)
     * Si es nula produce un error
     */
    public get matrizModelado() : Mat4 
    { 
        if ( this.matrizm == null )  
            throw new Error("Intento de leer una matriz de modelado nula")
        return this.matrizm
    }
    /**
     * Fija la matriz de modelado
     */
    public set matrizModelado( nueva_matrizm : Mat4 | null )
    {
        this.matrizm = nueva_matrizm
    }


    /**
     * Nombre del objeto
     */
    private nombre  : string = "no asignado" 

    /**
     * Devuelve el nombre del objeto 
     */
    public get leerNombre() : string
    {
        return this.nombre 
    }
    /**
     * Cambia el nombre del objeto.
     */
    public set fijarNombre( nuevo_nombre : string ) 
    {
        this.nombre = nuevo_nombre
    }
    public get tieneColor() : Boolean
    {
        return this.color != null
    }
    public get leerColor() : Vec3
    {
        if ( this.color == null )
            throw new Error(`intento de leer el color de un ObjetoVisualizable que no lo tiene (${this.nombre})`)
        return this.color
    }
    public set fijarColor( nuevo_color : Vec3 | null ) 
    {
        this.color = nuevo_color
    }

    /**
     * Visualiza el objeto. este método debe ser redefinido en clases derivadas
     */
    public visualizar() : void
    {
        throw new Error(`El objeto '${this.leerNombre}' no tiene redefinido el método 'visualizarGL'`)
    }

    /**
     * Visualiza las aristas del objeto. Este método puede ser redefinido en clases derivadas, si 
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene aristas que se pueden visualizar 
     * o que no se ha implementado esto)
     */
    public visualizarAristas() : void  
    {
        Log(`El objeto '${this.leerNombre}' no tiene método para visualizar aristas ('visualizarAristas')`)
    }

    /**
     * Visualiza las normales del objeto. Este método puede ser redefinido en clases derivadas, si 
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene normales que se pueden visualizar 
     * o que no se ha implementado esto)
     */
    public visualizarNormales() 
    {
        Log(`El objeto '${this.leerNombre}' no tiene método para visualizar normales ('visualizarNormales').`)
    }

    /**
     * Guarda el estado actual de los (algunos/todos?) los uniforms y 
     * lo actualiza según este objeto
     * @param cauce cauce sobre el que se fija y que guarda el estado.
     */
    public guardarCambiarEstado( cauce : Cauce )
    {
        if ( this.tieneColor )
        {
            cauce.pushColor()
            cauce.fijarColor( this.leerColor )
        }

        if ( this.tieneMatrizModelado )
        {
            cauce.pushMM()
            cauce.compMM( this.matrizModelado )
        }
    }

    /**
     * Restaura el estado previo a la última llamada a 'guardarFijarEstado'
     * @param cauce cauce desde donde se restaura el estado.
     */
    public restaurarEstado( cauce: Cauce )
    {
        if ( this.tieneMatrizModelado )
            cauce.popMM()

        if ( this.tieneColor )
            cauce.popColor()   
    }
}