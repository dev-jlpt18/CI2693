import java.io.File;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DegreesOfSeparation {
    // Clase Persona que poseera el atributo amigos, el cual nos ayudara almacenar los amigos de la persona
    private static class persona {

        List<String> amigos = new LinkedList<String>();
        public void add(String value) {
            this.amigos.add(value);
        }
    }
    // Metodo lecturaDocumento: Este leera el archivo input.txt (este debe estar en la misma carpeta)
    // El metodo mos devolvera un HashMap que tendra: Nombre(key) y Persona(value) 
    // Usar esta estructura me permite acceder a la lista de amigos mas rapido, solo debo tener el nombre de la persona a buscar.
    static Map<String, persona> lecturaDocumento(Scanner sc) {

        Map<String, persona> grupoPersonas= new HashMap<String, persona>();
        while(sc.hasNext()){
            // Valor de la columna izquierda
            String s = sc.next();
            // Valor de la columna derecha
            String t = sc.next();

            // Verificacion con el valor izquierdo
            if (!grupoPersonas.containsKey(s)) {
                // Si la persona no esta en el hashmap agregala
                persona individuo = new persona();
                individuo.add(t);
                grupoPersonas.put(s, individuo);

            } else if (grupoPersonas.containsKey(s)) {
                // Si la persona esta en el hashmap, busca el valor con la llave s y agrega a su lista de amigos de esa persona
                if (contains(t, grupoPersonas.get(s))) {
                    grupoPersonas.get(s).amigos.add(t);
                }

            }

            // Verificacion con el valor izquierdo
            if (!grupoPersonas.containsKey(t)) {

                persona individuo = new persona();
                individuo.add(s);
                grupoPersonas.put(t, individuo);

            } else if (grupoPersonas.containsKey(t)) {

                if (contains(s, grupoPersonas.get(t))) {
                    grupoPersonas.get(t).amigos.add(s);
                }
            }
        } 
        return grupoPersonas;
    }

    // Metodo contains: Nos permite verificar que no haya nombres repetidos en la lista de amigos de cada persona
    static boolean contains(String nombre, persona lista) {
        if (lista.amigos.contains(nombre)) {
            return false;
        } 
        return true; 
    }

    // Metodo gradoDeSeparacion: Este metodo recibe dos nombre y un hashmap, este recorrera todos los caminos
    // que tiene el primer nombre hasta alcanzar el segundo nombre. Se usara el hashmap como el guia entre los caminos
    static int gradoDeSeparacion(Map<String, persona> multitud, String primero, String ultimo) {

        // Se crea una lista que contendra los grados de todos los caminos encontrados de primero a ultimo
        List<Integer> caminoEncontrado = new LinkedList<Integer>();

        // Si el primer y ultimo son iguales retorna 0
        if (primero.equals(ultimo)) {
            return 0;
        } else if (multitud.containsKey(primero)) {
            // Si el primer nombre esta en la lista, toma su lista de amigos(value)
            persona grupoDeAmigos = multitud.get(primero);
            if (grupoDeAmigos.amigos.contains(ultimo)) {
                // Si se encuentra en su lista de amigos retorna 1
                return 1;
            } else {
                // En caso contrario creamos una pila para llevar la cuenta de los amigos que tiene hasta llegar al amigo deseado.
                // Agregamos el primer nombre
                Deque<String> predecesores = new ArrayDeque<String>(); 
                predecesores.add(primero);
                // Iteramos por cada uno de los amigos del primero
                for (String i: grupoDeAmigos.amigos) {
                    caminoEncontrado.add(gradoDeSeparacionRec(multitud, predecesores, i, ultimo, primero));
                }
            }
        
        }
        
        int m = minimo(caminoEncontrado);
        // Verifica si el valor minimo de caminoEncontrado es menor que MAX_VALUE
        if (m == Integer.MAX_VALUE){
            return -1;
        }

        return m;
    }

    // Metodo gradoDeSeparacionRec: Este metodo recibe dos nombre y un hashmap, este recorrera todos los caminos
    // que tiene el primer nombre hasta alcanzar el segundo nombre. Se usara el hashmap como el guia entre los caminos
    static int gradoDeSeparacionRec(Map<String, persona> multitud, Deque<String> predecesores, String primero, String ultimo, String predecesor) {
        // Tomamos la el valor(persona) contenida en la llave identidicada con primero 
        persona grupoDeAmigos = multitud.get(primero);

        // Si esta en su grupo de amigos retorna el tamaño de los predecesores y sumale 1.
        if (grupoDeAmigos.amigos.contains(ultimo)) {
            return predecesores.size()+1;
        } else {
            // En caso contrario, crea una lista que guardara los valores obtenidos desde este punto del camino
            List<Integer> arreglo = new LinkedList<Integer>();
            // Agrega a la lista de predecesores
            predecesores.add(primero);
            // Iteramos cada amigo que posee esta persona
            for (String i: grupoDeAmigos.amigos) {
               // Como es simetrico podemos conseguir puntos ya recorridos, por lo tanto verificamos que i no pertenezca a la lista de predecesores.
                if (!predecesores.contains(i)){
                    arreglo.add(gradoDeSeparacionRec(multitud, predecesores, i, ultimo, primero));
                }
            }
            
            reduccionCadena(predecesores, predecesor);
            return minimo(arreglo);
        }
    }

    // Metodo minimo: Busca el menor numero en la lista
    static int minimo(List<Integer> arreglo) {
        int n = Integer.MAX_VALUE;
        for(Integer i: arreglo) {
            if (n >= i) {
                n = i;
            }
        }
        return n;
    }
    
    // Metodo reduccionCadena: Regresa la lista de predecesores hasta el predecesor indicado
    static void reduccionCadena(Deque<String> predecesores, String predecesor){
        while(!predecesores.getLast().equals(predecesor)) {
                predecesores.removeLast();
            }
    }

    public static void main(String[] args) {
        String primerConector = args[0];
        String ultimoConector = args[1];
        int grado;
        try {
            File file = new File("input.txt");
            Scanner sc = new Scanner(file); 
            Map<String, persona> grupo = lecturaDocumento(sc);
            grado = gradoDeSeparacion(grupo, primerConector, ultimoConector);
            System.out.println(grado);
            
        } catch (Exception e) {
            grado = -1;
            System.out.print(grado);
        }
    }
}
