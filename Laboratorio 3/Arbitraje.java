import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class Arbitraje {
    public static  HashMap<String, moneda> lecturaTXT(Scanner sc) {
        HashMap<String, moneda> pares = new HashMap<String, moneda>();
        while(sc.hasNextLine()) {
            String monedaPrincipal = sc.next();
            String monedaSecundaria = sc.next();
            Double valor = Double.parseDouble(sc.next());
            
            if(!pares.containsKey(monedaPrincipal)) {
                moneda nuevoMoneda = new moneda(monedaPrincipal, monedaSecundaria, valor);
                pares.put(monedaPrincipal, nuevoMoneda);
            } else {
                if (!pares.get(monedaPrincipal).contenida(monedaSecundaria)) {
                    pares.get(monedaPrincipal).agregarPar(monedaSecundaria, valor);
                } else {
                    if(!pares.get(monedaPrincipal).getValor(monedaSecundaria).equals(valor) ) {
                        pares.get(monedaPrincipal).agregarPar(monedaSecundaria, valor);
                    }
                }
            } 
        }
        return pares;
    }
    static boolean trading(HashMap<String, moneda> pares) {
        Double ganancia;
        for (Map.Entry<String, moneda> i: pares.entrySet()) {
            ganancia = 1.0;
            i.getValue().changeVisitado(true);
            for (Map.Entry<String, Double> j: i.getValue().getPares().entrySet()) {
                
                pares.get(j.getKey()).changeVisitado(true);
                ganancia = conversion(ganancia, j.getValue());
                
                if(tradingREC(pares, i.getKey(), j.getKey(), 1, ganancia)) {
                    return true;
                }
                pares.get(j.getKey()).changeVisitado(false);
            }
            i.getValue().changeVisitado(false);
        }
        return false;
    }

    static Double conversion(Double base, Double cambio) {
        return base*cambio;
    }

    static boolean tradingREC(HashMap<String, moneda> pares, String monedaInicial, String monedaActual, Integer iteracion, Double ganancia) {
        Double cambio;
        if(iteracion == 1) {
            for (Map.Entry<String, Double> i: pares.get(monedaActual).getPares().entrySet()) {
                
                if(!i.getKey().equals(monedaInicial)) {
                    pares.get(i.getKey()).changeVisitado(true);
                    cambio = conversion(ganancia, i.getValue());
                    
                    if(tradingREC(pares, monedaInicial, i.getKey(), 2, cambio)) {
                        return true;
                    }
                    pares.get(i.getKey()).changeVisitado(false);
                }
            }
            return false;
        } else {
            for (Map.Entry<String, Double> i: pares.get(monedaActual).getPares().entrySet()) {
                if(i.getKey().equals(monedaInicial)) {
                    cambio = conversion(ganancia, i.getValue());
                    if (cambio >= 1.0) {
                        return true;
                    }
                } else if(!pares.get(i.getKey()).getVisitado()){
                    pares.get(i.getKey()).changeVisitado(true);
                    cambio = conversion(ganancia, i.getValue());
                    if(tradingREC(pares, monedaInicial, i.getKey(), 2, cambio)) {
                        return true;
                    }
                    pares.get(i.getKey()).changeVisitado(false);
                }
            }
            return false;
        }
    }

    public static class moneda {
        private String monedaName;
        private HashMap<String, Double> pares = new HashMap<String, Double>();
        private boolean visitado = false;

        public moneda(String nombre, String moneda,Double valor) {
            this.monedaName = nombre;
            pares.put(moneda, valor);
        }

        public String getName() {
            pares.entrySet();
            return this.monedaName;
        }

        public HashMap<String, Double> getPares() {
            return this.pares;
        }

        public boolean contenida(String name) {
            return this.pares.containsKey(name);
        }

        public void agregarPar(String nombre, Double valor) {
            this.pares.put(nombre, valor);
        }

        public Double getValor(String nombre) {
            return this.pares.get(nombre);
        }

        public boolean getVisitado() {
            return this.visitado;
        }

        public void changeVisitado(boolean cambio) {
            this.visitado = cambio;
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("tasas.txt");
            Scanner sc = new Scanner(file);
            HashMap<String, moneda> listaDeMonedas = lecturaTXT(sc);
            if(trading(listaDeMonedas)) {
                System.out.println("DINERO FACIL DESDE TU CASA");
            } else {
                System.out.println("TODO GUAY DEL PARAGUAY");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error al leer el documento");
            System.exit(1);
        } catch (NoSuchElementException e) {
            System.out.println("En el archivo txt hace falta indicar alguna de las moneda");
            System.exit(1);
        }
    }    
}
