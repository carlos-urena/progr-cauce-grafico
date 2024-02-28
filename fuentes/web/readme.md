# Instalación, compilación y ejecución de la aplicación Web

Prerequisitos e instrucciones de compilación y ejecución para la aplicación Web

## Prerequisitos: 

+ python3 para lanzar el servidor
+ typescript para compilar fuentes


## Instalación del compilador de _Typescript_

### Linux

Instalar 'nvm' con 'wget' según se dice en el repo: https://github.com/nvm-sh/nvm#installing-and-updating

Abrir una nueva terminal, para tener disponible la orden `nvm` (_node version manager_) y en ella hacer:

`nvm install 18`

Ahora está disponible la orden `npm` (_node package manager_), y la usamos para instalar el compilador de _typescript_, así: 

`npm install -g typescript`

### macOS

Es necesario instalar _Node_, se puede hacer con el gestor de paquetes _Brew_ (), con la orden: 

`brew install node` 

o bien descargarlo e instalarlo: https://nodejs.org/en/download. Una vez instalado, se dispone de la orden `npm`, la usamos para instalar el compilador de _typescript_, así:

`npm install -g typescript`

### Windows

En Windows usaremos siempre el terminal de tipo _Developer Powershell_. Hay que dar estos pasos:

Descargar e instalar _Node_ de: https://nodejs.org/en/download

Instalar el compilador (pendiente de verificar que esto funciona)

`npm install -g typescript`  ??

## Ejecución de la aplicación en un navegador:

### Linux o macOS

Ejecutar el script `comp-lanzar.sh` que hay en la carpeta `scripts` (estando en ella), es decir:

``` 
cd scripts
./comp-lanzar.sh
``` 

Esto compila los fuentes Typescript (que necesiten recompilarse), y genera los fuentes Javascript. Si no ha habido errores al compilar, lanza un servidor Web local que hace accesible la aplicación Web en la URL que aparece en el terminal, es esta:

`http://0.0.0.0:8000`

### Windows

Pendiente