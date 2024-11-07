
package negocio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AVL< K extends Comparable<K>, V>
        extends ArbolBinarioBusqueda<K, V> {

    private static final byte DIFERENCIA_MAXIMA = 1;
    private static final String UBICACION_ARCHIVO = "src\\Datos\\Diccionario.txt";

    @Override
    public void cargarDatosDelTxt() {
        try {

            BufferedReader lector = new BufferedReader(new FileReader(UBICACION_ARCHIVO));

            String linea;
            int i = 0;
            while ((linea = lector.readLine()) != null) {
                String[] palabras = linea.split("-");
                K clave = (K) palabras[0].trim();
                V valor = (V) palabras[1].trim();
                this.insertar(clave, valor);
            }

            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave no puede ser nula");
        }
        if (valor == null) {
            throw new IllegalArgumentException("Valor no puede ser nula");
        }
        super.raiz = this.insertar(this.raiz, clave, valor);

    }

    private NodoBinario<K, V> insertar(NodoBinario<K, V> nodoActual, K clave, V valor) {

        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K, V> nuevoNodo = new NodoBinario<>(clave, valor);
            return nuevoNodo;
        }
        K claveActual = nodoActual.getClave();
        if (clave.compareTo(claveActual) > 0) {
            nodoActual.setHijoDerecho(insertar(nodoActual.getHijoDerecho(),
                    clave, valor));
            return this.balancear(nodoActual);
        }
        if (clave.compareTo(claveActual) < 0) {
            NodoBinario<K, V> hijoIzquierdo = insertar(nodoActual.getHijoIzquierdo(),
                    clave, valor);
            nodoActual.setHijoIzquierdo(hijoIzquierdo);
            return this.balancear(nodoActual);
        }
        //-------- si llego aca actaulizamo su valor ------------------------
        nodoActual.setValor(valor);
        return nodoActual;
    }

    @Override
    public void eliminar(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave no puede ser nula");
        }
        super.raiz = this.eliminar(this.raiz, clave);
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K clave) {
        K claveActual = nodoActual.getClave();
        if (clave.compareTo(claveActual) > 0) {
            NodoBinario<K, V> hijoDereho = eliminar(nodoActual.getHijoDerecho(),
                    clave);
            nodoActual.setHijoDerecho(hijoDereho);
            return balancear(nodoActual);
        }
        if (clave.compareTo(claveActual) < 0) {
            NodoBinario<K, V> hijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(),
                    clave);
            nodoActual.setHijoIzquierdo(hijoIzquierdo);
            return balancear(nodoActual);

        }
        //--------- caso1 el nodo a eliminar es hoja -------------------------
        if (nodoActual.esHoja()) {
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }
        //------ caso 2 el nodo eliminar tiene almenos un hijo ---------------
        if (!nodoActual.esHijoDerechoVacio() && nodoActual.esHijoIzquierdoVacio()) {
            return nodoActual.getHijoDerecho();
        }
        if (nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquierdoVacio()) {
            return nodoActual.getHijoIzquierdo();
        }
        //----- caso 3 el nodo tiene 2 hijo buscamos el sucesor -------------
        NodoBinario<K, V> nodoSucesor = Sucesor(nodoActual.getHijoDerecho());
        // Copia los datos del sucesor al nodo actual
        nodoActual.setClave(nodoSucesor.getClave());
        nodoActual.setValor(nodoSucesor.getValor());

        // Elimina el sucesor
        nodoActual.setHijoDerecho(eliminar(nodoActual.getHijoDerecho(), nodoSucesor.getClave()));

        return nodoActual;

    }

    private NodoBinario<K, V> balancear(NodoBinario<K, V> nodoActual) {
        int alturaIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaDerecha = altura(nodoActual.getHijoDerecho());
        int diferencia = alturaIzquierda - alturaDerecha;
        if (diferencia > DIFERENCIA_MAXIMA) {
            //-- es rotacion a la derecha ----------------
            NodoBinario<K, V> hijoIzquierdo = nodoActual.getHijoIzquierdo();
            alturaIzquierda = altura(hijoIzquierdo.getHijoIzquierdo());
            alturaDerecha = altura(hijoIzquierdo.getHijoDerecho());
            if (alturaDerecha > alturaIzquierda) {
                //----- es rotacion doble a la derecha ---------
                return rotacionDobleADerecha(nodoActual);
            } else {
                //----- es rotacion simple a la derecha ---------
                return rotacionSimpleADerecha(nodoActual);
            }
        } else if (diferencia < -DIFERENCIA_MAXIMA) {
            //----- es rotacion a la izquierda  -------------
            NodoBinario<K, V> hijoDerecho = nodoActual.getHijoDerecho();
            alturaIzquierda = altura(hijoDerecho.getHijoIzquierdo());
            alturaDerecha = altura(hijoDerecho.getHijoDerecho());
            if (alturaIzquierda > alturaDerecha) {
                return rotacionDobleAIzquierda(nodoActual);
            } else {
                return rotacionSimpleAIzquierda(nodoActual);
            }

        }
        return nodoActual;
    }

    private NodoBinario<K, V> rotacionSimpleADerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoIzquierdo();//nodoIzqu para balanceo
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());//actualizar punteroNodo
        nodoQueRota.setHijoDerecho(nodoActual);//rotacion hecha apunta a nodo actual
        return nodoQueRota;
    }

    private NodoBinario<K, V> rotacionDobleADerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoDePrimeraRotacion = rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(nodoDePrimeraRotacion);
        return rotacionSimpleADerecha(nodoActual);
    }

    //metodo falta completado
    private NodoBinario<K, V> rotacionSimpleAIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoDerecho();//nodoDere para balanceo
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());//actualizar punteroNodo
        nodoQueRota.setHijoIzquierdo(nodoActual);//rotacion hecha apunta a nodo actual
        return nodoQueRota;
    }
    //metodo completado

    private NodoBinario<K, V> rotacionDobleAIzquierda(NodoBinario<K, V> nodoActual) {
        //rotacion simple a derecha luego rotacion a izquierda
        NodoBinario<K, V> nodoDePrimeraRotacion = rotacionSimpleADerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(nodoDePrimeraRotacion);
        return rotacionSimpleAIzquierda(nodoActual);
    }
}
