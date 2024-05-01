import { Cauce } from "./cauce.js"
import { MallaInd } from "./malla-ind.js"
import { ObjetoVisualizable } from "./objeto-visu.js"
import { Assert, ComprErrorGL, Log, ContextoWebGL } from "./utilidades.js"
import { DescrVAO } from "./vaos-vbos.js"
import { CMat4, UVec3, Vec2, Vec3 } from "./vec-mat.js"




/**
 * Clase que encapsula un "frame buffer object"
 */
export class FramebufferObject 
{
    
    private gl           : ContextoWebGL
    private sizex        : number 
    private sizey        : number 
    private fbo_wgl_act  : WebGLFramebuffer | null = null 
    private color_buffer : WebGLTexture | null = null
    private depth_buffer : WebGLRenderbuffer | null = null
    private rectXY       : CuadradoXYcct | null = null

    protected get fbo() : WebGLFramebuffer 
    { 
        if ( this.fbo_wgl_act == null )
            throw new Error(`$FramebufferObject.fbo: todavía no se ha creado el objeto FBO de WebGL`)
        return this.fbo_wgl_act 
    }

    protected get cbuffer() : WebGLTexture
    { 
        if ( this.color_buffer == null )
            throw new Error(`$FramebufferObject.color: todavía no se ha creado el objeto color`)
        return this.color_buffer 
    }

    public get tamX() : number { return this.sizex }
    public get tamY() : number { return this.sizey }


    /**
     * Constructor 
     */
    constructor( gl : ContextoWebGL, sizex : number, sizey : number )
    {
        const fname = "Framebuffer.constructor:"
        Assert( sizex > 0 && sizey > 0, `${fname} tamaño del framebuffer inválido (${sizex} x ${sizey})` )

        this.sizex = sizex 
        this.sizey = sizey 
        this.gl = gl

        const tuple_length = 4 // 3 (when format == gl.RGB)
        const format  = gl.RGBA // gl.RGB
        const type    = gl.UNSIGNED_BYTE  // allways unsigned byte ???
        const level   = 0
        const iFormat = format  // gl.RGBA in the original example
        const border  = 0
        const data    = null
        
        // crear y activar la textura de color
        this.color_buffer = gl.createTexture()
        gl.bindTexture( gl.TEXTURE_2D, this.color_buffer )
        gl.texImage2D( gl.TEXTURE_2D, level, iFormat, this.sizex, this.sizey, 
                       border, format, type, data )
        
        // fijar el filtrado de forma que no se necesiten mip-maps
        gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST )  
        gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE )
        gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE )

        ComprErrorGL( gl, `${fname} textura creada con error`)

        // crear y activar el bufer de profundidad
        this.depth_buffer = gl.createRenderbuffer()
        gl.bindRenderbuffer( gl.RENDERBUFFER, this.depth_buffer )
        gl.renderbufferStorage( gl.RENDERBUFFER, gl.DEPTH_COMPONENT16, this.sizex, this.sizey )

        ComprErrorGL( gl, `${fname} buffer de profundidad creado con error`)

        // crear y activar e framebuffer
        // asociar el buffer de color y el de profundidad al framebuffer
        
        this.fbo_wgl_act = gl.createFramebuffer()
        
        gl.bindFramebuffer( gl.FRAMEBUFFER, this.fbo_wgl_act )
        gl.framebufferTexture2D( gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.TEXTURE_2D, this.color_buffer, level )
        gl.framebufferRenderbuffer( gl.FRAMEBUFFER, gl.DEPTH_ATTACHMENT, gl.RENDERBUFFER, this.depth_buffer )

        ComprErrorGL( gl, `${fname} Framebuffer creado con error`)

        if ( ! gl.isFramebuffer( this.fbo_wgl_act ) )
        {
            const msg : string = `${fname} no se ha podido crear el framebuffer`
            Log( msg )
            throw new Error( msg )
        }

        // comprobar estado (ver si es ok)
        const status : GLenum = gl.checkFramebufferStatus( gl.FRAMEBUFFER )
        if ( status != gl.FRAMEBUFFER_COMPLETE )
        {
            const msg : string = `${fname} el estado del framebuffer no es 'COMPLETE', es: ${status}`
            Log( msg )
            throw new Error( msg )
        }

        // restaurar estado previo
        gl.bindTexture( gl.TEXTURE_2D, null  )
        gl.bindRenderbuffer( gl.RENDERBUFFER, null  ) 
        gl.bindFramebuffer( gl.FRAMEBUFFER, null )

        Log( `${fname} Framebuffer creado correctamente` )
    }
    // ---------------------------------------------------------------
    
    /**
     * Activa este FBO, define el viewport y limpia el framebuffer
     */
    activar() : void 
    {
        const fname = "FramebufferObject.activar:"
        ComprErrorGL( this.gl, `${fname} inicio`)
        this.gl.bindFramebuffer( this.gl.FRAMEBUFFER, this.fbo )
        ComprErrorGL( this.gl, `${fname} fin`)

    }
    // ---------------------------------------------------------------
    
    /**
     * Desactiva el FBO (activar el framebuffer por defecto de WebGL)
     */
    desactivar() : void 
    {
        const fname = "FramebufferObject.desactivar:"
        ComprErrorGL( this.gl, `${fname} inicio`)
        this.gl.bindFramebuffer( this.gl.FRAMEBUFFER, null )
        ComprErrorGL( this.gl, `${fname} fin`)
    }
    // ---------------------------------------------------------------
    
    // ---------------------------------------------------------------
    // dibujar un rectángulo con la textura de color usando el cauce de la aplicación
    // lo deja mal 

    visualizarEn( cauce : Cauce, ancho : number, alto : number ) : void
    {
        const gl = this.gl
        
        if ( this.rectXY == null )
            this.rectXY = new CuadradoXYcct(  )
        cauce.activar()

        const rxy = this.sizex / this.sizey
        const ryx = this.sizey / this.sizex
        
        const m = 0.1 // margin 
        const w = 0.2 // half width
        let mtras = CMat4.traslacion( new Vec3([-1.0+m+w/2.0, -1.0+m+w/2, 0.0]))
        let mesc = CMat4.escalado( new Vec3([w,w*ryx,1.0]) )
        let mmod = mtras.componer( mesc )

        const mvista = CMat4.ident()
        const mproy = CMat4.escalado( new Vec3([1.0,1.0,1.0]) )
        
        gl.bindFramebuffer( gl.FRAMEBUFFER, null ) // framebuffer por defecto.
        gl.disable( gl.DEPTH_TEST )
        gl.disable( gl.CULL_FACE )
        gl.viewport( 0, 0, ancho, alto )
        
        cauce.fijarEvalMIL( false )
        cauce.fijarEvalText( true, this.cbuffer )
        cauce.resetMM()
        cauce.fijarMatrizVista( mvista )
        cauce.fijarMatrizProyeccion( mproy )
        cauce.resetMM()
        cauce.compMM( mmod )

        this.rectXY.visualizar()
    }
}


/**
 * Cuadrado en XY [-1..1] con coordenadas de textura en [0..1]
 */
class CuadradoXYcct extends MallaInd 
{

    // textura 
    //private textura : Textura
    
    /**
     * Crea una malla indexada con un cuadrado con coordenadas de textura,
     * se extiende en X y en Y
     */
    constructor(  )
    {
        super()
        this.nombre = "Cuadro XY con cc.t."
        

        this.posiciones =
        [
            new Vec3([ -1.0, -1.0,  0.0 ]),  // 0
            new Vec3([ +1.0, -1.0,  0.0 ]),  // 1
            new Vec3([ +1.0, +1.0,  0.0 ]),  // 2
            new Vec3([ -1.0, +1.0,  0.0 ]),  // 3
        ]

        this.coords_text =
        [
            new Vec2([  0.0,  0.0  ]),  // 0
            new Vec2([  1.0,  0.0  ]),  // 1
            new Vec2([  1.0,  1.0  ]),  // 2
            new Vec2([  0.0,  1.0  ]),  // 3
        ]

        this.triangulos =
        [
            new UVec3([ 0, 1, 2 ]),
            new UVec3([ 0, 2, 3 ])
        ]
        this.calcularNormales()
        this.comprobar("CuadradoXYcct.constructor")
    }
}