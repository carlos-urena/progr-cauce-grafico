.SUFFIXES:
.PHONY: clean, v1, v2

v2: provincias-espana-v2.txt 

v1: provincias-espana.txt


provincias-espana.txt: analizador.py provincias-espana.svg
	python3 analizador.py  

provincias-espana-v2.txt: analizador-v2.py provincias-espana-v2.svg
	python3 analizador-v2.py

clean:
	rm -f provincias-espana.txt provincias-espana-v2.txt