import java.io.File;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class separacion {
    private static class persona {

        List<String> amigos = new LinkedList<String>();
        public void add(String value) {
            this.amigos.add(value);
        }
    }

    static Map<String, persona> lecturaDocumento(Scanner sc) {

        Map<String, persona> grupoPersonas= new HashMap<String, persona>();
        while(sc.hasNext()){
            String s = sc.next();
            String t = sc.next();
            if (!grupoPersonas.containsKey(s)) {

                persona individuo = new persona();
                individuo.add(t);
                grupoPersonas.put(s, individuo);

            } else if (grupoPersonas.containsKey(s)) {

                if (contains(t, grupoPersonas.get(s))) {
                    grupoPersonas.get(s).amigos.add(t);
                }

            }

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

    static boolean contains(String nombre, persona lista) {
        if (lista.amigos.contains(nombre)) {
            return false;
        } 
        return true; 
    }

    static int gradoDeSeparacion(Map<String, persona> multitud, String primero, String ultimo) {
        List<Integer> caminoEncontrado = new LinkedList<Integer>();
        if (primero.equals(ultimo)) {
            return 0;
        } else if (multitud.containsKey(primero)) {
            persona grupoDeAmigos = multitud.get(primero);
            if (grupoDeAmigos.amigos.contains(ultimo)) {
                return 1;
            } else {
                Deque<String> predecesores = new ArrayDeque<String>(); 
                predecesores.add(primero);
                for (String i: grupoDeAmigos.amigos) {
                    System.out.println(i);
                    caminoEncontrado.add(gradoDeSeparacionRec(multitud, predecesores, i, ultimo));
                }
                System.out.println(predecesores.toString());
                System.out.println(caminoEncontrado.toString());
            }
        
        }
        if (minimo(caminoEncontrado) == Integer.MAX_VALUE){
            return -1;
        }

        return minimo(caminoEncontrado);
    }

    static int gradoDeSeparacionRec(Map<String, persona> multitud, Deque<String> predecesores, String primero, String ultimo) {
        String pre = predecesores.getLast();
        System.out.println("Predecesor principal 1: "+ pre);
        persona grupoDeAmigos = multitud.get(primero);
        if (grupoDeAmigos.amigos.contains(ultimo)) {
            System.out.println("Iteracion sobre: "+ primero);
            System.out.println("Conseguido en "+ primero);
            System.out.println("Volviendo a predecesor");
            return predecesores.size()+1;
        } else {
            List<Integer> arreglo = new LinkedList<Integer>();
            predecesores.add(primero);
            System.out.println("");
            System.out.println("Iteracion sobre: "+ primero);
            System.out.println("Se agrego "+ primero+ " a la lista de predecesores");
            System.out.println("");
            for (String i: grupoDeAmigos.amigos) {
                System.out.println("Iteracion sobre el sucesor: "+ i);
                if (!predecesores.contains(i)){
                    arreglo.add(gradoDeSeparacionRec(multitud, predecesores, i, ultimo));
                    predecesores.add(i);
                    System.out.println("Lista predecesores coño: "+ predecesores);
                }
            }
            System.out.println("Predecesor principal2: "+ pre);
            reduccionCadena(predecesores, pre);
            System.out.println("Cadena reducida:"+ predecesores+ " siendo "+ primero);
            System.out.println("Volviendo a predecesor luego de reducir cadena");
            return minimo(arreglo);
        }
    }

    static int minimo(List<Integer> arreglo) {
        int n = Integer.MAX_VALUE;
        for(Integer i: arreglo) {
            if (n >= i) {
                n = i;
            }
        }
        return n;
    }
    static void reduccionCadena(Deque<String> predecesores, String primero){
        while(!predecesores.getLast().equals(primero)) {
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
            
            for(Map.Entry<String, persona> i: grupo.entrySet()) {
                System.out.println("La llave es : "+i.getKey()+ " y el valor es: "+ i.getValue().amigos.toString());
            }
            
        } catch (Exception e) {
            grado = -16;
            System.out.print(grado);
        }
    }
}
