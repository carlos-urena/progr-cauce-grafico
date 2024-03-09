# El Cauce Gráfico en dispositivos móviles 

En esta carpeta se encuentra una subcarpeta (con extensión `.proyecto_AS` con los fuentes _Kotlin_ y demás archivos del proyecto de _Android Studio_ para la asignatura. En este README están las instrucciones para 

## Instalación de Android Studio 


Para instalar Android Studio se accede aquí: 

 
 [developer.android.com/studio](https://developer.android.com/studio)
 
 Hay versiones para Linux, Windows y macOS.

### Linux

 Los pasos siguientes de instalación se han probado en Linux (Ubuntu 22.04 LTS) con la versión <i>Android Studio Hedgehog</i> - 2023.1.1 Patch 2 para Linux.


Las instrucciones de instalación están aquí:

[developer.android.com/studio/install](https://developer.android.com/studio/install)

Ahí e dice que en Linux de 64 bits hay que instalar unas librerías adicionales. Para comprobar si Ubuntu es de 32 bits o no, se puede acceder a la ventana de configuración de Ubuntu y mirar, en la sección _Acerca de_ (al final), el tipo de SO (64 o 32 bits). En mi caso efectivamente se trata de un Ubuntu de 64 bits, así que es necesario hacer:

``` 
sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
``` 

El siguiente paso es descargar de la Web el archivo con Android Studio, en Linux es un `.tar.gz`. Lo descargamos a una carpeta nueva vacía. En esa carpeta, extraemos el archivo. Se puede extraer con el administrador de archivos o en la línea de órdenes con:

```
tar zxvf archivo.tar.gz
```

Al abrirlo se crea una subcarpeta, de nombre `android-studio`, a su vez tiene una subcarpeta de nombre `bin`. Hacemos `cd` a la subcarpeta `bin` y lanzamos Android Studio con:

```
./studio.sh
```

Al lanzarlo la primera vez se ejecuta el _Setup Wizard_ para instalar el SDK de Android (conjunto de herramientas y librerías de desarrollo de Google para Android). Durante la instalación se pregunta:

  -  La carpeta de instalación (<i>config or installation directory</i>), lo dejo en blanco para que se instale en la ubicación por defecto, que será 
  en `~/Android/Sdk` 
  - Si se quiere hacer una instalación  _Standard_ o _Custom_, elijo _Standard_ 

En el recuadro para aceptar el acuerdo de licencia hay que pulsar _accept__ **para cada una**  de las componentes que se instalan (es decir, hay que pulsarlo varias veces). La instalación tarda un poco (son 5,5 Gb en Ubuntu).

Una vez que se ha completado la instalación, aparece un recuadro para abrir un proyecto o crear uno nuevo. En ese momento se puede salir de la aplicación y mover la carpeta `android-studio` a su ubicación definitiva, que puede ser, por ejemplo, dentro de la carpeta `~/Android`, junto con el SDK. 

Finalmente para lanzar la aplicación hay que volver a ejecutar el script `studio.sh`. Para poder hacer esto fácilmente desde la línea de órdenes, se puede definir un _alias_ de Linux (de nombre, por ejemplo, `studio`) que lance la aplicación.

### macOS

La instalación en el SO de Apple es similar a la de Linux, excepto que en este caso se decarga un archivo ¿`.dmg`?. Después de descargar, la aplicación Android Studio se debe mover a la carpeta `Aplicaciones`, como es habitual en macOs. En la primera ejecución se instalará el SDK, al igual que en Linux. Para lanzar el programa fácilmente, un _alias_, igul que en Linux.

### Windows 

Pendiente.

## Creación de un proyecto para OpenGL 

Aunque en esta asignatura los estudiantes abrirán un proyecto ya creado, en esta subcarpeta se dan detalles acerca de como se ha creado dicho proyecto: 

[Creacion del proyecto](creacion-proyecto)

## Desarrollo con este proyecto

Para el desarrollo y pruebas en el proyecto ya creado, se puede acceder a este README:

[Proyecto Android Studio](proyecto-as)










