
package arboles.Excepciones;


public class ExcepcionOrdenInvalido  extends Exception{

    public ExcepcionOrdenInvalido() {
        super("Arbol Orden Invalido");
    }

    public ExcepcionOrdenInvalido(String string) {
        super(string);
    }
    
    
}
