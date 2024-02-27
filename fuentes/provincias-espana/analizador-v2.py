import xml.etree.ElementTree as ET

input_file = 'provincias-espana-v2.svg'
output_file = 'provincias-espana-v2.txt'
try: 
    tree = ET.parse( input_file )
except:    
    ## error de tipo  xml.etree.ElementTree.ParseError (ver: https://docs.python.org/3/library/xml.etree.elementtree.html#exceptions)
    print("Error al analizar el fichero:",input_file)
    exit()


root = tree.getroot() 
print( "Árbol leído sin errores, root.tag:",root.tag)

if ( root.tag != "{http://www.w3.org/2000/svg}svg"):
    print( "El fichero no es un svg:", input_file )
    exit() 

min_x = 0.0
max_x = 0.0

min_y = 0.0
max_y = 0.0

n = 0

try:
    file = open( output_file, 'w') 
except:
    print("Error al crear el fichero de salida:", output_file )
    exit() 

for polygon_elem in root.iter('{http://www.w3.org/2000/svg}path'): 

    n  = n+1
    id = polygon_elem.get('id')

    ## nota: los polígonos con z al final deben cerrarse, por ahora se ignora eso
    points_string_raw = polygon_elem.get('d').replace(",", " ")

    strings_array_pre = points_string_raw.split("m")

    print("-- polygon encontrado, id ==",id,"(número",n,")")
    print("      dividido en ",len(strings_array_pre),"sub-polígonos.")

    strings_array = [ strings_array_pre[1] ] ## el primero está vacío, me quedo con el segundo (el resto son huecos que ya están incluidos en el primero)
    ##strings_array = strings_array_pre        ## me quedo con todos

    for points_string_chars in strings_array:
 
        points_string = points_string_chars.replace("z", " ").strip() 
        if points_string == "":
            continue
       
        # Create an array of float values
        values_array = [float(v) for v in points_string.split()]
        
        # Check if the number of floats in points is even
        if len(values_array) % 2 != 0:
            print("Error: el número de valores flotantes en el array no es par")
            exit()

        # Create an array of 2-tuples from the values array 
        relative_points_array = [(values_array[i], values_array[i+1]) for i in range(0, len(values_array), 2)] 
        
        ## Compute the array in absolute coordinates
        absolute_points_array = [relative_points_array[0]]  # Initialize points_array with the first tuple from relative_points_array

        for i in range(1, len(relative_points_array)):
            last_point = absolute_points_array[-1]  # Get the last absolute point in points_array
            new_point = (last_point[0] + relative_points_array[i][0], last_point[1] + relative_points_array[i][1])  # Compute the new absolute point
            absolute_points_array.append(new_point)  # Add the new absolute point to points_array

        for i in range(0, len(absolute_points_array)):
            p = absolute_points_array[i] 
            file.write( str(round(p[0],4)) + " " + str(round(p[1],4)) + " " )
        file.write('\n')

        print("-- fin polygon")
        print("  ")

print("Archivo de salida creado:",output_file)
print("Número de polígonos encontrados:",n)


