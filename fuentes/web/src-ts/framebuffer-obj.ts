import { Assert, ComprErrorGL, Log, ContextoWebGL } from "./utilidades.js"


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

    protected get fbo_wgl() : WebGLFramebuffer 
    { 
        if ( this.fbo_wgl_act == null )
            throw new Error(`$FramebufferObject.fbo: todavía no se ha creado el objeto FBO de WebGL`)
        return this.fbo_wgl_act 
    }


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
        if ( gl.isFramebuffer( this.fbo_wgl_act ) == false )
        {
            const msg : string = `${fname} no se ha podido crear el framebuffer`
            Log( msg )
            throw new Error( msg )
        }
        gl.bindFramebuffer( gl.FRAMEBUFFER, this.fbo_wgl_act )
        gl.framebufferTexture2D( gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.TEXTURE_2D, this.color_buffer, level )
        gl.framebufferRenderbuffer( gl.FRAMEBUFFER, gl.DEPTH_ATTACHMENT, gl.RENDERBUFFER, this.depth_buffer )

        ComprErrorGL( gl, `${fname} Framebuffer creado con error`)

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
}