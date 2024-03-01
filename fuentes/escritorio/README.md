# PCG: Compilación y ejecución de los fuentes para escritorio 

En esta carpeta se encuentra el código fuente para aplicaciones de escritorio de la asignatura PCG. Los fuentes se pueden compilar y ejecutar Linux, MacOS o Windows. Se usan las librerías GLFW (para gestión de ventanas), GLM (vectores y matrices) y GLEW (acceso a funciones de OpenGL de la versión 3, únicamente en Linux y Windows).



##  Requisitos

###  Linux

En linux es necesario tener instalado el compilador de C++ de GNU o del proyecto LLVM, esto permite invocar la orden `g++` y la orden `make`. Si no se tienen disponibles, estas herramientas se pueden instalar con la orden:

```
sudo apt install build-essential
```

Se debe usar _apt_ para instalar _cmake_, que se usa para poder compilar fácilmente el ejemplo, se hace con:

```
sudo apt install cmake
```

Finalmente se deben instalar los paquetes _libglew-dev_, _libglfw3-dev_ y _libglm-dev_ (tienen las librerías que se usan en estos fuentes, es decir *GLEW*, *GLFW* y *GLM*), se puede hacer con:

```
sudo apt install libglew-dev
sudo apt install libglfw3-dev
sudo apt install libglm-dev
```

### MacOS

En ordenadores macOS hay que tener instalada la herramienta de desarrollo de **XCode** ([developer.apple.com/xcode](https://developer.apple.com/xcode/)).
Este herramienta de desarrollo incorpora (entre otros) el compilador de C++ del proyecto LLVM adaptado por Apple, el IDE de desarrollo para Apple, así como el _framework_ de **OpenGL**. 

Una vez instalado _XCode_ (y si no se ha hecho durante la instalación) es necesario instalar un componente adicional de XCode llamado _Command line Tools_ (CLT), se puede hacer desde la propia línea de órdenes con:

```
xcode-select --install
```

Además de _XCode_, también podemos usar el instalador de paquetes open source **Homebrew** ([brew.sh](https://brew.sh/index_es)), para instalarlo se deben seguir las instrucciones que podemos encontrar en la página Web, aunque en realidad es fácil hacerlo desde la línea de órdenes con:

```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

La librería OpenGL ya viene instalada con _XCode_, así que únicamente hará falta instalar la orden **cmake** y las librerías **GLFW** y **glm**. Estos paquetes se pueden instalar fácilmente con _homebrew_, usando:  

```
brew install cmake
brew install glfw
brew install glm
```

### Windows

En Windows hay que instalar **Visual Studio** ([visualstudio.microsoft.com](https://visualstudio.microsoft.com))
 de Microsoft, es un entorno de desarrollo y una suite compiladores e intérpretes para varios lenguajes de programación. 

Este entorno de desarrollo incluye numerosos componentes para sus distintos lenguajes de programación. Para este repositorio únicamente hay que instalar los componentes para desarrollo de aplicaciones de escritorio con C y C++, aquí hay instrucciones específicas para esto:

[learn.microsoft.com/es-es/cpp/build/vscpp-step-0-installation](https://learn.microsoft.com/es-es/cpp/build/vscpp-step-0-installation)

Este software incluye tanto _cmake_ como _git_.

Los archivos de compilación están preparados para compilar en la línea de órdenes usando librerías instaladas con **vcpkg**, que es un instalador (open source) de paquetes con librerías de C/C++ de Microsoft ([vcpkg.io](https://vcpkg.io)).  Para instalar _vcpkg_ debes hacer `cd` a tu carpeta _home_ (es decir, la carpeta `C:\Users\usuario\`) y una vez en ella clonar el repositorio de _vcpkg_ con:

```
git clone https://github.com/Microsoft/vcpkg.git
```

Si todo va bien se crea una carpeta de nombre `vcpkg` dentro de tu carpeta _home_. Es posible situar esta carpeta en cualquier otro lugar del sistema de archivos, pero los scripts de compilación y otros archivos de configuración esperan que esté en tu _home_. Por tanto, si la situas en otro sitio, tendrás que adapatar dichos scripts de compilación. Para finalizar la instalación debes hacer `cd` a tu carpeta _home_, y ahí ejecutar:

```
.\vcpkg\bootstrap-vcpkg.bat
```

En la carpeta `vcpkg` quedará el archivo ejecutable `vcpkg.exe`, que se puede ejecutar directamente desde la línea de órdenes. En windows debemos de instalar las librerías **GLEW** y **GLFW**, se puede hacer `cd` a la carpeta `vcpkg` y ejecutar 

```
.\vcpkg install glew  --triplet x64-windows
.\vcpkg install glfw3 --triplet x64-windows
.\vcpkg install glm   --triplet x64-windows
```

El _switch_ `--triplet` indica que se instalen las versiones de 64 bits _dinámicas_ de estas librerías. La instalación de GLEW conlleva la instalación de la librería OpenGL.




##  Compilar y Ejecutar

###  Linux o MacOS

En estos sistemas operativos podemos compilar en la línea de órdenes usando un terminal normal.

Para la generación de los archivos de compilación y la compilación en sí se usa `cmake`, para ello es necesario ir a la carpeta `builds/macos` o `builds/linux`, según el sistema operativo. En esa carpeta debemos asegurarnos de que la sub-carpeta `cmake` está vacía (si no lo estaba ya, hay que borrar todos los archivos ahí, excepto `.gitignore`).  Para generar los archivos de compilación, hay que hacer entrar a la carpeta `cmake` y ahí escribir: 

```
cmake ..
``` 

Esto hay que hacerlo una vez, o cada vez que se añadan nuevos fuentes o se quiera cambiar la configuración de compilación. Esto genera diversos archivos y carpetas en `cmake`. Después, para compilar los fuentes, hay que ejecutar (en esa misma carpeta):

```
make
```

Si la compilación va bien se genera el ejecutable, que tiene el nombre  `debug_exe` y está en la carpeta `bin`. La orden `make` también se puede usar con un argumento para otros fines:

* `make clean` para eliminar el programa compilado y los archivos asociados.
* `make release_exe` para generar el ejecutable `release_exe` (también en `bin`), el cual no tiene los símbolos de depuración y además está optimizado (es más pequeño y puede que sea más rápido al ejecutarse)

Para forzar un recompilado de todos los fuentes, basta con vaciar la carpeta `cmake` y volver a hacer `cmake ..` en ella. Es necesario hacerlo si se añaden o quitan unidades de compilación o cabeceras de las carpetas con los fuentes.


### Windows

En Windows se debe que usar el terminal llamado __Developer PowerShell for VS__, es la aplicación de terminal para _PowerShell_ de Microsoft, pero configurada con las variables de entorno necesarias para compilar desde la línea de órdenes. 

Estos fuentes se deben compilar con `cmake`, para ello es necesario ir a la carpeta `builds\windows`. 
En esa carpeta debemos asegurarnos de que la sub-carpeta `cmake` está vacía. Si no lo estaba ya hay que borrar todos los archivos ahí, excepto `.gitignore`.  Para generar los archivos de compilación, dentro de `cmake` vacío escribimos: 

```
cmake ..
``` 

Esto hay que hacerlo una vez, o cada vez que se añadan nuevos fuentes o se quiera cambiar la configuración de compilación. Esto genera diversos archivos y carpetas en `cmake`. 

Una vez generados los archivos de compilación, cada vez que queramos recompilar los fuentes hay que ejecutar, en `cmake`, esta orden:

```
cmake --build .
```

Si la compilación va bien se genera el ejecutable, que tiene el nombre `ejecutable.exe` y está situado en la sub-carpeta `Debug` dentro de la carpeta `bin`, dicha carpeta también incluye archivos `.dll` (librerías dinámicas de Windows) y un archivo `.pdb` para depuración.

Para forzar un recompilado de todos los fuentes, basta con vaciar la carpeta `cmake`, repetir `cmake ..` en ella y finalmente compilar con `cmake --build .`

En Windows se genera por defecto una versión _Debug_ del ejecutable, si se quiere generar una versión _Release_, el paso de compilación debe ser de esta forma:

```
cmake --build . --config Release
```
en este caso, el ejecutable (y sus archivos `.dll`) quedará en la subcarpeta `Release` dentro de `bin` (no se genera el `.pdb`).

En windows, las sentencias `cout` o `printf` de C/C++ que contengan acentos o la eñe producen caracteres extraños en el terminal, ya que el terminal no asume por defecto que las cadenas que se imprimen están codificadas en UTF-8, mientras que los programaas fuentes de este repositorio sí están codificadas en ese formato (deben estarlo así). Para solucionar este problema, hay que ejecutar una vez esta orden en el terminal Powershell (incluyendo el carácter `$` inicial):

```
$OutputEncoding = [console]::InputEncoding = [console]::OutputEncoding = New-Object System.Text.UTF8Encoding
```

Si no se quiere teclear esto en cada inicio de sesión, se puede añadir esa línea al archivo de script (tipo `.ps1`) que se ejecuta cada vez que se abre _Powershell_, el nombre comleto de dicho archivo está en la variable de entorno `$profile` de _Powershell_.

(Nota: todo esto se ha probado probado en Febrero de 2024 en Windows 11).

## Uso de VS Code en Linux, macOS y Windows.

Las carpetas `build/linux`, `build/macos` y `build/windows` incluyen archivos de nombre `workspace` (y extensión `.code-workspace`). Estos archivos se pueden abrir con la aplicación *VS Code* de Microsoft, para poder editar, compilar, ejecutar y depurar fácilmente el código.

