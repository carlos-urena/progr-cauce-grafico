import { AplicacionWeb }   from "./aplicacion-pcg.js"
import { MallaEsfera }     from "./malla-sup-par.js"
import { ObjetoAnimado  }  from "./objeto-anim.js"
import { CMat4, Vec3 }     from "./vec-mat.js"
import { ObjetoCompuesto } from "./objeto-comp.js"
import { CuadradoXZ }      from "./malla-ind.js"

/**
 * Animación sencilla de una esfera que hace circunferencias entorno al origen
 */
export class EsferaRotacion extends ObjetoAnimado 
{    
     private ang_grad   : number = 0     // ángulo actual en grados
     private w_grad_sec : number = 120.0 // velocidad angular en grados por segundo
     private radio_circ : number = 0.8 // radio de la circunferencia que hace la esfera
     private radio_esf  : number = 0.1 // radio de la esfera  
     
     /**
      * Objeto compuesto que tiene la esfera dentro (instanciada con escalado y traslacion en Y)
      */
     private nodo_esfera : ObjetoCompuesto

     constructor()
     {
          super()
          this.nombre = "Esfera Rotación"
          
          const re = this.radio_esf

          // construir las matrices que le afectarán a la esfera (aparte de la de animación)
          const mat_traX = CMat4.traslacion( new Vec3([this.radio_circ, 0.0, 0.0])) // 3. trasladar en X una distancia igual al radio de la circunferencia
          const mat_escR = CMat4.escalado( new Vec3([ re,re,re ]))       // 2. escalar para que tenga el radio 're' en lugar de 1 (=this.radio_esf)
          const mat_traY = CMat4.traslacion( new Vec3([0.0, 1.0, 0.0]))  // 1. elevar centro en Y una unidad para que la parte inferior sea tangente al rectángulo de la base.
          const mat_esf  = mat_traX.componer( mat_escR ).componer( mat_traY ) // componer trasl.Y, después escalado, después trasl. X
          
          // crear la malla con la esfera y con su transformación (mat_esf)
          let malla_esfera = new MallaEsfera( 32, 32 )
          malla_esfera.matrizModelado = mat_esf

          // el nodo de la esfera contiene unicamente la esfera, pero se usa 
          // para después cambiarle su matriz de modelado con el tiempo
          this.nodo_esfera = new ObjetoCompuesto         
          this.nodo_esfera.agregar( malla_esfera )

          // el objeto raiz contiene los dos sub-objetos de la escena: el nodo esfera y el cuadrado de la base.
          let raiz = new ObjetoCompuesto
          raiz.agregar( this.nodo_esfera )
          raiz.agregar( new CuadradoXZ() )

          // 'raiz' será el objeto que se usará para visualizar:
          this.obj_vis = raiz
     }
     
     /**
      * Pone el objeto en su estado inicial 
      * (pone el ángulo en grados a cero)
      */
     protected estadoInicial() : void 
     {
          this.ang_grad = 0  
     }

     /**
      * Modifica el ángulo del objeto, después de transcurrido cierto tiempo en segundos.
      * @param inc_t_animado (number) - incremento del tiempo respecto del estado actual del objeto
      */
     protected actualizarObjeto( inc_t_animado : number ) : void 
     {
          this.ang_grad += this.w_grad_sec * inc_t_animado 
          this.nodo_esfera.matrizModelado = CMat4.rotacionYgrad( this.ang_grad )
     }
}