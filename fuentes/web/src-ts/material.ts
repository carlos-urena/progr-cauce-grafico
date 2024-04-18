import { Assert, Log } from "./utilidades.js"
import { AplicacionPCG } from "./aplicacion-pcg.js"

export class Material
{
   private nombre_mat : string = "material anónimo";  // nombre del material

   public set nombre( nuevo_nombre : string ) { this.nombre_mat = nuevo_nombre }
   public get nombre( ) : string              { return this.nombre_mat }
   
   public ka  : number = 0.2  // coeficiente de reflexión ambiente (en 0..1)
   public kd  : number = 0.8  // coeficiente de reflexión difusa (en 0..1)
   public ks  : number = 0.0  // coeficiente de reflexión pseudo-especular (en 0..1)
   public exp : number = 5.0  // exponente de brillo para reflexión pseudo-especular

   // Material actualmente activado en el cauce (se usa para push/pop)
   // 'null' antes de activar ninguno, al inicio de 'VisualizarFrame' se debe activar el material 
   // por defecto.
   //private static actual : Material | null = null

   // pila de materiales, inicialmente vacía
   //private static pila : Material[] = []


   // ---------------------------------------------------------------------------

   /**
    * Crea un nuevo material
    * @param ka (number) coeficiente de reflexión ambiente (en 0..1)
    * @param kd (number) coeficiente de reflexión difusa (en 0..1)
    * @param ks (number) coeficiente de reflexión pseudo-especular (en 0..1)
    * @param exp (number) exponente de brillo para reflexión pseudo-especular (2.0 como mínimo)
    */
   constructor( ka : number, kd : number, ks : number, exp : number )
   {
      const nombref : string = 'Material.constructor:'

      Assert( 0.0 <= ka && ka <= 1.0, `${nombref} 'ka' (${ka}) está fuera del rango 0..1`)
      Assert( 0.0 <= kd && kd <= 1.0, `${nombref} 'kd' (${kd}) está fuera del rango 0..1`)
      Assert( 0.0 <= ks && ks <= 1.0, `${nombref} 'ks' (${ks}) está fuera del rango 0..1`)
      Assert( 2.0 <= exp, `${nombref} 'exp_pse' (${exp}) no puede ser menor que 2.0`)

      this.ka  = ka
      this.kd  = kd 
      this.ks  = ks
      this.exp = exp
   }
   // ---------------------------------------------------------------------------

   // /**
   //  *  activa el material en el cauce actual de la aplicación
   //  */
   // public activar(  ) : void
   // {
   //    Material.actual = this // registrar el material actual
   //    let cauce = AplicacionPCG.instancia.cauce
   //    cauce.fijarParamsMIL( this.k_amb, this.k_dif, this.k_pse, this.exp_pse )
   // }
   // --------------------------------------------------------------------
   // Métodos estáticos ('static') o de clase (no se ejecutan sobre una instancia)

   /**
    * Hace push (en la pila de materiales) del material actualmente activado 
    * (si no hay ningún material activado, provoca una excepción)
    */
   // public static push() : void 
   // {
   //    const nombref : string = "Material.push:"
   //    if ( Material.actual == null )
   //       throw new Error(`${nombref} no hay material actualmente activado`)
   //    Material.pila.push( Material.actual )
   // }
   // // --------------------------------------------------------------------

   // /**
   //  * Hace pop de la pila de materiales (reactiva el material en el top y lo elimina de la pila)
   //  * (la pila no puede estar vacía)
   //  * 
   //  */
   // public static pop() : void 
   // {
   //    const nombref : string = "Material.pop:"
   //    let pm = Material.pila

   //    if ( pm.length == 0 )
   //       throw new Error(`${nombref} la pila está vacía`)

   //    let mat = pm[ pm.length-1 ]
   //    pm.pop()
   //    mat.activar()
   // }

}




