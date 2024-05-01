import { Assert, glsl }
from "./utilidades.js"

import { CauceBase }
from "./cauce-base.js"

import { ProgramObject } 
from "./program-obj.js"

import { FramebufferObject } 
from "./framebuffer-obj.js"

import { ShaderObject, VertexShaderObject, FragmentShaderObject } 
from "./shader-obj.js"

import { ComprErrorGL, Log, ContextoWebGL }
from   "./utilidades.js"
import { ObjetoVisualizable } from "./objeto-visu.js"
import { CMat4, Vec3 } from "./vec-mat.js"

// -------------------------------------------------------------------------

const src_vertex :string = 
glsl`#version 300 es
    precision highp float;

    uniform mat4 u_mat_modelado;
    uniform mat4 u_mat_modelado_nor;
    uniform mat4 u_mat_vista;
    uniform mat4 u_mat_proyeccion;

    layout( location = 0 ) in vec3 pos;
    layout( location = 2 ) in vec3 nor;

    void main()
    {
        mat4 m = u_mat_proyeccion * u_mat_vista * u_mat_modelado;
        gl_Position = m*vec4( pos, 1.0 );
    }
`
// -------------------------------------------------------------------------

const src_fragment : string = 
glsl`#version 300 es
    precision highp float;

    uniform mat4 u_mat_modelado;
    uniform mat4 u_mat_modelado_nor;
    uniform mat4 u_mat_vista;
    uniform mat4 u_mat_proyeccion;

    in vec3 pos;
    in vec3 nor;

    out vec4 color;
    
    void main()
    {
        color = vec4( 1.0, 0.0, 0.0, 1.0 );
    }
`

// -------------------------------------------------------------------------

export class CauceSombras extends CauceBase
{
    private fbo_opc : FramebufferObject | null = null
    private dir_vista : Vec3 = new Vec3([0,0,1]) // dirección de vista
    public get fbo() : FramebufferObject
    {
        if ( this.fbo_opc == null )
            throw new Error("CauceSombras.fbo: todavía no se ha creado el objeto FBO")
        return this.fbo_opc
    }
    
    // ------------------------------------------------------------------------- 

    constructor( gl : ContextoWebGL )
    {
        super( gl )
        
    }

    public async inicializar( sizex : number, sizey : number)
    {
        const fname : string = "CauceSombras.inicializar:"
        let gl = this.gl

        ComprErrorGL( gl, `${fname} al inicio`)

        this.objeto_programa = new ProgramObject( gl )

        this.objeto_programa.agregar( new VertexShaderObject( gl, null, src_vertex ) )
        this.objeto_programa.agregar( new FragmentShaderObject( gl, null, src_fragment ))
        
        await this.objeto_programa.compilarEnlazar()
        this.objeto_programa.usar()
        this.inicializarUniformsBase()

        this.fbo_opc = new FramebufferObject( this.gl, sizex, sizey )
        ComprErrorGL( gl, `${fname} al final`)
    }

    /**
     * 
     * @param gl Función que crea el cauce de sombras (asíncrono)
     * @param sizex number
     * @param sizey number
     * @returns promesa resuelta con el cauce de sombras nuevo
     */
    public static async crear( gl: ContextoWebGL, sizex : number, sizey : number ) : Promise<CauceSombras>
    {
        let cs = new CauceSombras(gl)
        await cs.inicializar( sizex, sizey )
        return cs 
    }

    // --------------------------------------------------------------------------

    public fijarDimensionesFBO( nuevo_tamX : number, nuevo_tamY : number ) : void
    {
        const fname : string = "CauceSombras.fijarDimensionesFBO:"
        Assert( nuevo_tamX > 0 && nuevo_tamY > 0, `${fname} tamaño del framebuffer inválido (${nuevo_tamX} x ${nuevo_tamY}`)
        if ( nuevo_tamX != this.fbo.tamX || nuevo_tamY != this.fbo.tamY )
            this.fbo_opc = new FramebufferObject( this.gl, nuevo_tamX, nuevo_tamY )
    }
    // --------------------------------------------------------------------------
    /**
     * fija la nueva dirección de vista (apunta hacia la fuente de luz)
     * @param nueva_dir Vec3
     */
    public fijarDireccionVista( nueva_dir : Vec3 ) : void
    {
        const fname : string = "CauceSombras.fijarDireccionVista:"
        const ld : number = nueva_dir.length

        Assert( Math.abs(ld) < 1e-3, `${fname} dirección de vista inválida` )
        this.dir_vista = nueva_dir

        // actualizar matrices de vista y proyección:
        // el view-frustum es un cubo de lado 2*w en XY y 3*w en Z
        // (centro en el origen, eje Z hacia la fuente de luz, eje X horizontal).)
        const w = 2.5

        const ejey_wcc = new Vec3([0,1,0])
        const ejez_ecc = nueva_dir.normalizado
        const ejex_ecc = ejey_wcc.cross( ejez_ecc ).normalizado 
        const ejey_ecc = ejez_ecc.cross( ejex_ecc ).normalizado

        this.mat_vista = CMat4.filas( ejex_ecc, ejey_ecc, ejez_ecc )
        this.mat_proyeccion = CMat4.ortho( -w, +w, -w, +w, -3*w, +3*w )
    }
    // --------------------------------------------------------------------------

    /**
     * Activa el cauce y el FBO
     */
    public inicioRender()
    {
        let gl = this.gl 

        this.programa.usar()
        this.fbo.activar()

        gl.viewport( 0, 0, this.fbo.tamX, this.fbo.tamY )
        gl.clearColor( 0.0, 0.0, 0.0, 1.0 )
        gl.clear( gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT )
    }
    // --------------------------------------------------------------------------
    /**
     * Visualizar la geometría de un objeto en este cauce, usando la 
     * matriz de vista actual
     * @param obj objeto a visualizar
     */

    visualizarGeometriaObjeto( obj : ObjetoVisualizable )
    {
        const fname = "CauceSombras.visualizarObjeto:"
        let gl = this.gl

        ComprErrorGL( gl, `${fname} inicio`)
        this.inicioRender()
        this.gl.enable( gl.DEPTH_TEST )
        this.gl.disable( gl.CULL_FACE )
        this.resetMM()
        this.fijarMatrizVista( this.mat_proyeccion )
        this.fijarMatrizProyeccion( this.mat_vista )
        obj.visualizarGeometria( this )
        this.finRender()
        ComprErrorGL( this.gl, `${fname} fin`)
    }

    // --------------------------------------------------------------------------
    /**
     * Desactiva el cauce y el FBO
     */
    public finRender()
    {
        this.gl.useProgram( null )
        this.gl.bindFramebuffer( this.gl.FRAMEBUFFER, null )
    }
    // --------------------------------------------------------------------------


}