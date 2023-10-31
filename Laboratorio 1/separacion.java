import java.io.File;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class separacion {
    private List<String> caminoEncontrado = new LinkedList<String>();

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
        int grado = 0;
        if (primero.equals(ultimo)) {
            return grado;
        } else if (multitud.containsKey(primero)) {
            persona grupoDeAmigos = multitud.get(primero);
            if (grupoDeAmigos.amigos.contains(ultimo)) {
                return 1;
            } else {
                Deque<String> predecesores = new ArrayDeque<String>(multitud.size()); 
                predecesores.add(primero);
                for (String i: grupoDeAmigos.amigos) {
                    grado = 1;
                    System.out.println(i);
                    grado = gradoDeSeparacionRec(multitud, i, ultimo, grado);
                    if(grado < -1) {
                       return grado*(-1); 
                    }
                }
            }
        } 
        return -1;
    }

    static int gradoDeSeparacionRec(Map<String, persona> multitud, String primero, String ultimo, int grado) {
        int g = grado;
        persona grupoDeAmigos = multitud.get(primero);
        if (grupoDeAmigos.amigos.contains(ultimo)) {
            g++;
            return g*(-1);
        } else {
            for (String i: grupoDeAmigos.amigos) {
                g++;
                System.out.println(i);
                g = gradoDeSeparacionRec(multitud, i, ultimo, g);
                if(g < -1) {
                    break; 
                }
            }
            return g;
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
            grado = -1;
            System.out.print(grado);
        }
    }
}
