//Archivo que contiene el procedimiento principal de la implementación,
//describiendo los pasos a seguir para la aplicación del FEM al problema
//de transferencia de calor.
package ejercicio1;

import java.util.ArrayList;
import java.lang.System.*;

//import ejercicio1.mesh;


public class main{
int main()
{
    //Se preparan dos vectores, uno para contener todas las Ks locales de los elementos de la malla
    //y uno para contener todas las bs locales de dichos elementos
    ArrayList<ArrayList<Integer>> localKs = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Integer>>> localbs = new ArrayList<>();

    //Se preparan también las variables para la K y la b globales, y una para las incógnitas de
    //temperatura, que es donde se almacenará la respuesta.
    ArrayList<ArrayList<ArrayList<Integer>>> K = new ArrayList<>();
    ArrayList<ArrayList<Integer>> b = new ArrayList<>();
    ArrayList<ArrayList<Integer>> T = new ArrayList<>();

    //Se coloca primero una introducción con todas las características de la implementación
    System.out.print ("IMPLEMENTACIÓN DEL MÉTODO DE LOS ELEMENTOS FINITOS\n"+
        "\t- TRANSFERENCIA DE CALOR\n" + "\t- 1 DIMENSIÓN\n"+
        "\t- FUNCIONES DE FORMA LINEALES\n" + "\t- PESOS DE GALERKIN\n"+
        "*********************************************************************************\n\n");

    //Se crea un objeto mesh, que contendrá toda la información de la malla
    mesh m;
    //Se procede a obtener toda la información de la malla y almacenarla en m
    leerMallayCondiciones(m);

    //Se procede a crear la K local y la b local de cada elemento, almacenando estas
    //estructuras en los vectores localKs y localbs
    crearSistemasLocales(m,localKs,localbs);
    //Descomentar la siguiente línea para observar las Ks y bs creadas
    //showKs(localKs); showbs(localbs);

    //Se inicializan con ceros la K global y la b global
    zeroes(K,m.getSize(NODES));
    zeroes(b,m.getSize(NODES));
    //Se procede al proceso de ensamblaje
    ensamblaje(m,localKs,localbs,K,b);
    //Descomentar la siguiente línea para observar las estructuras ensambladas
    //showMatrix(K); showVector(b);

    //Se aplican primero las condiciones de contorno de Neumann
    applyNeumann(m,b);
    //Descomentar la siguiente línea para observar los cambios en b
    //showVector(b);

    //Luego se aplican las condiciones de contorno de Dirichlet
    applyDirichlet(m,K,b);
    //Descomentar la siguiente línea para observar el SEL final luego
    //de los cambios provocados por Dirichlet
    //showMatrix(K); showVector(b);

    //Se prepara con ceros el vector T que contendrá las respuestas
    zeroes(T,b.size());
    //Finalmente, se procede a resolver el SEL
    calculate(K,b,T);

    //Se informa la respuesta:
    System.out.print("La respuesta es: \n");
    showVector(T);

    return 0;
}
}
