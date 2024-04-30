import { glsl }
from "./utilidades.js"

import { CauceBase }
from "./cauce-base.js"

const vsSombras :string = glsl`
#version 300 es
 
`

class CauceSombras extends CauceBase
{
    constructor( gl : WebGL2RenderingContext )
    {
        super( gl )
    }

    public inicializar() : void
    {

    }
}