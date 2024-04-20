import { AplicacionPCG } from "./aplicacion-pcg.js";
import { MallaEsfera } from "./malla-sup-par.js";
import { ObjetoAnimado  } from "./objeto-anim.js";
import { CMat4, Vec3 } from "./vec-mat.js";
import { ObjetoCompuesto } from "./objeto-comp.js"


export class Animacion1 extends ObjetoAnimado 
{
    
    private pos_ini = new Vec3([0,0,0])
    private vel_ini = new Vec3([0.05,0.05,0])
    
        
    private pos : Vec3 = this.pos_ini
    private vel : Vec3 = this.vel_ini
    
    /**
     * Objeto compuesto que tiene la esfera dentro (instanciada con escalado y traslacion en Y)
     */
    private nodo_esfera : ObjetoCompuesto

    /**
     * Objeto compuesto con la esfera y resto de objetos de la misma 
     */
    private objetos : ObjetoCompuesto

    constructor()
    {
        super()
        this.nombre = "Animacion 1"
        
        const re = 0.1 // radio de la esfera  
        const mat_escR = CMat4.escalado( new Vec3([ re,re,re ]))
        const mat_traY = CMat4.traslacion( new Vec3([0.0, 1.0, 0.0]))

        let malla_esfera = new MallaEsfera( 32, 32 )
        malla_esfera.matrizModelado = mat_escR.componer( mat_traY )

        this.nodo_esfera = new ObjetoCompuesto         
        this.nodo_esfera.agregar( malla_esfera )

        this.objetos = new ObjetoCompuesto
        this.objetos.agregar( this.nodo_esfera )
    }
    
    /**
    * Pone el objeto en su estado inicial 
    * (debe ser redefinido en clases derivadas, y debe poner las variables específicas 
    * de estado al mismo valor que en el constructor.
    */
   protected estadoInicial() : void 
   {
        this.pos = this.pos_ini 
        this.vel = this.vel_ini
   }

   /**
    * Módifica la posición del objeto usando su velocidad y el tiempo transcurrido.
    * @param inc_t_animado (number) - incremento del tiempo respecto del estado actual del objeto
    */
   protected actualizarObjeto( inc_t_animado : number ) : void 
   {
        // p = p + dt*v
        this.pos = this.pos.mas( this.vel.mult( inc_t_animado ) ) 
   }

   /**
    * Visualiza el objeto 
    */
   public visualizar()
   {
        let gl = AplicacionPCG.instancia.gl 
        let cauce = AplicacionPCG.instancia.cauce 

        this.nodo_esfera.matrizModelado = CMat4.traslacion( this.pos )
        this.objetos.visualizar()
   }
}