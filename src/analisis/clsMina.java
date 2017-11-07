/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author miguel
 */
public class clsMina {
    private int  id;
    private String Material;
    private int cantidad_mineros;
    private int valor;
    private int tiempo_extraccion;
    private int velocidad_desplazamiento;
    private int[][]mapa;
    private Point entrada;
    ArrayList<clsDeposito>depostios;
    ArrayList<clsMinero>mineros;
    ArrayList<Casilla>casillas;

    public clsMina(int id,String material,int Cantidad,int tiempo,int velocidad) {
        this.id=id;
        this.Material=material;
        this.cantidad_mineros=Cantidad;
        this.tiempo_extraccion=tiempo;
        this.velocidad_desplazamiento=velocidad;
        depostios=new ArrayList<>();
        mineros=new ArrayList<>();
        casillas=new ArrayList<>();
        mapa=new int[8][12];
    }
    public void ingresarnuemromapa(Point p, int valor){
        mapa[p.x][p.y] = valor;
        for (int x = 0; x < mapa.length; x++) {
            System.out.print("|");
            for (int y = 0; y < mapa[x].length; y++) {
                System.out.print(mapa[x][y]);
                if (y != mapa[x].length - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println("|");
        }
    }
    
    public void crearDeposito(int id,int cantidad,Point p){
        clsDeposito d=new clsDeposito(id, cantidad, p);
        depostios.add(d);
        
    }

    /**
     * @return the entrada
     */
    public Point getEntrada() {
        return entrada;
    }

    /**
     * @param entrada the entrada to set
     */
    public void setEntrada(Point entrada) {
        this.entrada = entrada;
    }
    
}
