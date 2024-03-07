

# El Cauce Gráfico en dispositivos móviles 


## Instalación de Android Studio y su SDK en Linux

En 1er lugar es necesario instalar Android Studio, accediendo aquí: 

 
 [developer.android.com/studio](https://developer.android.com/studio)
 
 Hay versiones para Linux, Windows y macOS. Los pasos siguientes de instalación se han probado en Linux (Ubuntu 22.04 LTS) con la versión <i>Android Studio Hedgehog</i> - 2023.1.1 Patch 2 para Linux.


Las instrucciones de instalación están aquí:

[developer.android.com/studio/install](https://developer.android.com/studio/install)

Ahí e dice que en Linux de 64 bits hay que instalar unas librerías adicionales. Para comprobas si Ubuntu es de 32 bits o no, se puede acceder a la ventana de configuración de Ubuntu y mirar en la sección _Acerca de_. En mi caso efectivamente se trata de un Ubuntu de 64 bits, así que hago 

``` 
sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
``` 

El primer paso es descargar de la Web el archivo con Android Studio, en Linux es un `.tar.gz`. Lo descargamos a una carpeta nueva vacía. En esa carpeta, extraemos el archivo. Puede ser un `.zip` o un `.tar.gz` (en mi prueba es un `.tar.gz`). Los archivos `.tar.gz` se pueden extraer en Linux con 

```
tar zxvf archivo.tar.gz
```

Al abrirlo se crea una subcarpeta, de nombre `android-studio`, a su vez tiene una subcarpeta de nombre `bin`. Hacemos `cd` a la subcarpeta `bin` y lanzamos Android Studio con:

```
./studio.sh
```

Al lanzarlo la primera vez se ejecuta el _setup wizard_ para instalar el SDK, pide <i>config or installation directory</i>, lo dejo en blanco para que se instale en la ubicación por defecto (creo que es en `Android/Sdk` dentro de la carpeta _home_). Me pide instalación _Standard_ o _Custom_, elijo _Standard_ 

En el recuadro para aceptar el acuerdo de licencia hay que pulsar _accept__ **para cada una**  de las componentes que se instalan (es decir, hay que pulsarlo varias veces).

## Creación de un proyecto para OpenGL 

Aunque en esta asignatura los estudiantes abrirán un proyecto ya creado, en esta subcarpeta se dan detalles acerca de como se ha creado dicho proyecto: 

[Creacion del proyecto](creacion-proyecto)

## Desarrollo en el proyecto ya creado.

Para el desarrollo y pruebas en el proyecto ya creado, se puede acceder a la carpeta `proyecto-as`, para más detalles ver el README de dicha carpeta:

[Proyecto Android Studio](proyecto-as)










