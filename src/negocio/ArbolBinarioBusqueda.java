package negocio;

//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolBinarioBusqueda< K extends Comparable<K>, V>
        implements IArbolBinario<K, V> {

    public ArbolBinarioBusqueda() {

    }
    protected NodoBinario<K, V> raiz;
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
    public void vaciar() {
        this.raiz = null;
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int size() {
        Stack<NodoBinario<K, V>> pilaNodos = new Stack<>();
        int cantidad = 0;

        pilaNodos.push(this.raiz);
        while (!pilaNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaNodos.pop();
            cantidad++;
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    protected int altura(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaPorLaIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorLaDerecha = altura(nodoActual.getHijoIzquierdo());
        return alturaPorLaIzquierda > alturaPorLaDerecha ? alturaPorLaIzquierda + 1
                : alturaPorLaDerecha + 1;
    }

    @Override
    public int nivel() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int nivel = -1;
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.offer(raiz);

        while (!colaNodos.isEmpty()) {
            int cantidadDeNodo = colaNodos.size();
            int i = 0;
            while (i < cantidadDeNodo) {
                NodoBinario<K, V> nodoActual = colaNodos.poll();
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaNodos.offer(nodoActual.getHijoDerecho());
                }
                i++;
            }
            nivel++;
        }
        return nivel;
    }

    @Override
    public K minimo() {
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior.getClave();
    }

    @Override
    public K maximo() {
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoDerecho();
        }
        return nodoAnterior.getClave();
    }

    @Override
    public void insertar(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave no puede ser nula");
        }
        if (valor == null) {
            throw new IllegalArgumentException("Valor no puede ser nula");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(clave, valor);
            return;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = null;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            K claveActual = nodoActual.getClave();
            nodoAnterior = nodoActual;
            if (claveActual.compareTo(clave) > 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveActual.compareTo(clave) < 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else { // si la clave existe remplazamos
                nodoActual.setValor(valor);
                return;
            }
        }
        // si llego quiere decir que hay que insertar
        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(clave, valor);
        if (nodoAnterior.getClave().compareTo(clave) > 0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        } else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
    }

    @Override
    public void eliminar(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave a eliminar no puede ser nula");
        }
        this.raiz = eliminar(this.raiz, clave);
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodo, K clave) {
        if (NodoBinario.esNodoVacio(nodo)) {
            return nodo;
        }

        if (clave.compareTo(nodo.getClave()) < 0) {
            nodo.setHijoIzquierdo(eliminar(nodo.getHijoIzquierdo(), clave));
        } else if (clave.compareTo(nodo.getClave()) > 0) {
            nodo.setHijoDerecho(eliminar(nodo.getHijoDerecho(), clave));
        } else {
            if (nodo.esHijoIzquierdoVacio()) {
                return nodo.getHijoDerecho();
            } else if (nodo.esHijoDerechoVacio()) {
                return nodo.getHijoIzquierdo();
            }

            // Encuentra el nodo sucesor en orden (el menor valor en el subárbol derecho)
            NodoBinario<K, V> sucesor = Sucesor(nodo.getHijoDerecho());

            // Copia los datos del sucesor al nodo actual
            nodo.setClave(sucesor.getClave());
            nodo.setValor(sucesor.getValor());

            // Elimina el sucesor
            nodo.setHijoDerecho(eliminar(nodo.getHijoDerecho(), sucesor.getClave()));
        }

        return nodo;
    }

    protected NodoBinario<K, V> Sucesor(NodoBinario<K, V> nodo) {
        NodoBinario<K, V> actual = nodo;
        while (!actual.esHijoIzquierdoVacio()) {
            actual = actual.getHijoIzquierdo();
        }
        return actual;
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != null;
    }

    @Override
    public V buscar(K clave ) {

        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        pilaAuxiliar.add(raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.pop();
            if (nodoActual.getClave().compareTo(clave) == 0) {
                return nodoActual.getValor();
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoIzquierdo());
            }

        }
        return null;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        llenarPilaInOrden(pilaAuxiliar, raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.peek();
            if (!nodoActual.esHijoDerechoVacio()) {
                nodoActual = pilaAuxiliar.pop();
                listaElementos.add(nodoActual.getClave());
                llenarPilaInOrden(pilaAuxiliar, nodoActual.getHijoDerecho());
            } else {
                nodoActual = pilaAuxiliar.pop();
                listaElementos.add(nodoActual.getClave());
            }
        }
        return listaElementos;
    }

    private void llenarPilaInOrden(Stack<NodoBinario<K, V>> pilaAuxiliar, NodoBinario<K, V> nodo) {

        while (!NodoBinario.esNodoVacio(nodo)) {
            pilaAuxiliar.push(nodo);
            nodo = nodo.getHijoIzquierdo();
        }

    }

    @Override
    public List<K> recorridoEnPosOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        llenarPilaPostOrden(pilaAuxiliar, nodoActual);
        while (!pilaAuxiliar.isEmpty()) {
            nodoActual = pilaAuxiliar.pop();
            listaElementos.add(nodoActual.getClave());
            if (!pilaAuxiliar.isEmpty()) {
                NodoBinario<K, V> nodoTope = pilaAuxiliar.peek();
                if (!nodoTope.esHijoDerechoVacio() && nodoTope.getHijoDerecho() != nodoActual) {
                    llenarPilaPostOrden(pilaAuxiliar, nodoTope.getHijoDerecho());
                }
            }
        }
        return listaElementos;
    }

    private void llenarPilaPostOrden(Stack<NodoBinario<K, V>> pilaAuxiliar, NodoBinario<K, V> nodo) {
        while (!NodoBinario.esNodoVacio(nodo)) {
            pilaAuxiliar.push(nodo);
            if (!nodo.esHijoIzquierdoVacio()) {
                nodo = nodo.getHijoIzquierdo();
            } else {
                nodo = nodo.getHijoDerecho();
            }

        }
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Stack<NodoBinario<K, V>> pilaAuxiliar = new Stack<>();
        pilaAuxiliar.add(raiz);
        while (!pilaAuxiliar.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaAuxiliar.pop();
            listaElementos.add(nodoActual.getClave());
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaAuxiliar.push(nodoActual.getHijoIzquierdo());
            }

        }
        return listaElementos;
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Queue<NodoBinario<K, V>> colaNodos = new LinkedList<>();
        colaNodos.add(raiz);
        while (!colaNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = colaNodos.poll();

            listaElementos.add(nodoActual.getClave());
            if (!nodoActual.esHijoIzquierdoVacio()) {
                colaNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaNodos.offer(nodoActual.getHijoDerecho());
            }

        }
        return listaElementos;
    }

    public void imprimirArbol() {
        imprimirArbol(raiz, "", false, null);
    }

    private void imprimirArbol(NodoBinario<K, V> nodo, String prefijo, boolean esHijoIzquierdo, NodoBinario<K, V> nodoPadre) {
        if (nodo != null) {
            String simboloRama = esHijoIzquierdo ? "├── " : "└── ";
            System.out.println(prefijo + simboloRama + nodo.getClave() + " [" + (nodoPadre != null && nodo.getClave().compareTo(nodoPadre.getClave()) < 0 ? "I" : "D") + "]");

            String prefijoIzquierdo = prefijo + (nodo.getHijoDerecho() != null ? "│   " : "    ");
            String prefijoDerecho = prefijo + "    ";

            imprimirArbol(nodo.getHijoIzquierdo(), prefijoIzquierdo, true, nodo);
            imprimirArbol(nodo.getHijoDerecho(), prefijoDerecho, false, nodo);
        }
    }
}
