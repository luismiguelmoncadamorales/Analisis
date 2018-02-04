/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
    clsRutaMinima ruta;

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
        ruta=new clsRutaMinima();
    }
    public void ingresarnuemromapa(Point p, int valor){
        mapa[p.x][p.y] = valor;
        
        pintar_matriz();
    }
    public void actualizarDeposito(int depo){
       for (int x = 0; x < mapa.length; x++) {
            for (int y = 0; y < mapa[x].length; y++) {
                if(mapa[x][y]==depo){
                   mapa[x][y]=-1;
                    for (int i = 0; i < casillas.size(); i++) {
                        if (casillas.get(i).getId().x==x&&casillas.get(i).getId().y==y) {
                            casillas.get(i).cambiarfondo(new ImageIcon(getClass().getResource("../imagenes/tunel.png")));
                            casillas.get(i).fondo=new ImageIcon(getClass().getResource("../imagenes/tunel.png"));
                            
                        }
                    }
                }
            }
            
        }
       pintar_matriz();
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
    

    public ArrayList ruta(int d){
        
        ArrayList<Point>p=new ArrayList<>();
        ruta.deposito=d;
        int distancia=ruta.solucion(mapa, entrada.x, entrada.y, 0, 0);
        System.out.println("distancia"+distancia);
        p= ruta.camino(mapa, entrada.x, entrada.y, 0, 0, distancia,p);
        return p;
    }
    
    public void asignar(clsMinero m){
        m.setId(id);
        m.setCapacidad_carga(cantidad_mineros);
        m.setCasillas(this.casillas);
        m.setDestino(devolverDestino(mineros.size()+1));
        m.setEspecialidad(Material);
        m.setPosiciones(this.ruta(m.getDestino()));
        m.setVelocidad(velocidad_desplazamiento);
        m.setTiiempo_extraccion(tiempo_extraccion);
        if (Material.equalsIgnoreCase("oro")) {
            m.setImagen(new ImageIcon(getClass().getResource("../imagenes/Moro.png")));
        }
        else if (Material.equalsIgnoreCase("plata")) {
            m.setImagen(new ImageIcon(getClass().getResource("../imagenes/Mplata.png")));
           
        }
        else{
            m.setImagen(new ImageIcon(getClass().getResource("../imagenes/Mcobre.png")));
        }
        mineros.add(m);
        
    }
    
    public int devolverDestino(int dest){
        
        JOptionPane.showMessageDialog(null,"destino"+dest);
        pintar_matriz();
        ruta.deposito=dest;
        int distancia=ruta.solucion(mapa, entrada.x, entrada.y, 0, 0);
        JOptionPane.showMessageDialog(null,"distancia"+distancia);
        if (distancia==99) {
            dest=99;
            for (int i = 0; i < depostios.size(); i++) {
                ruta.deposito=i+1;
                distancia=ruta.solucion(mapa, entrada.x, entrada.y, 0, 0);
                if (distancia!=99) {
                   dest=i+1;
                   i=depostios.size();
                }
            }
        }
        JOptionPane.showMessageDialog(null,"asignado al deposito"+dest);
        return dest;
    }
    
    
     public void simular(){
         for (int i = 0; i < mineros.size(); i++) {
             Thread hilo=new Thread(mineros.get(i));
             hilo.start();
             Principal.hilos.add(hilo);
         }
     }
      public void pintar_matriz(){
          System.out.println("--------------------------");
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
    
    }
    

