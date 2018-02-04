/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author miguel
 */
public class clsMinero implements Runnable{
    private int id;
    private String especialidad;
    private int velocidad;
    private int tiiempo_extraccion;
    private int sueldo;
    private ImageIcon Imagen;
    private int destino;
    private ArrayList<Point> posiciones;
    private int capacidad_carga;
    private ArrayList<Casilla>casillas;
   
    


    public int getDestino() {
        return destino;
    }


    public void setDestino(int destino) {
        this.destino = destino;
    }

    @Override
    public void run() {
        int temp=0;
        
        while (Principal.Consultar_Disponibilidad_material(destino) > 0) {
            for (int i = 0; i < getPosiciones().size(); i++) {
                System.out.println("posiciones en minero :" + getPosiciones().get(i).x + "--" + getPosiciones().get(i).y);
                try {
                    for (int j = 0; j < getCasillas().size(); j++) {
                        if (getCasillas().get(j).getId().x == getPosiciones().get(i).x && getCasillas().get(j).getId().y == getPosiciones().get(i).y) {
                            
                            getCasillas().get(j).cambiarfondo(getImagen());
                            if (i != 0) {
                                casillas.get(temp).cambiarfondo2();
                            }

                            temp = j;
                        }

                    }
                    Thread.sleep(getVelocidad() * 100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(clsMinero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                Thread.sleep(tiiempo_extraccion * 100);
            } catch (InterruptedException ex) {
                Logger.getLogger(clsMinero.class.getName()).log(Level.SEVERE, null, ex);
            }
            Principal.Descontar_Disponibilidad_material(destino, capacidad_carga);
            temp = 0;
            for (int i = 0; i < getPosiciones().size(); i++) {
                try {
                    for (int j = 0; j < getCasillas().size(); j++) {
                        if (getCasillas().get(j).getId().x == getPosiciones().get(getPosiciones().size() - 1 - i).x && getCasillas().get(j).getId().y == getPosiciones().get(getPosiciones().size() - 1 - i).y) {
                            getCasillas().get(j).cambiarfondo(getImagen());
                            if (i != 0) {
                                casillas.get(temp).cambiarfondo2();
                            }
                            temp = j;
                        }

                    }
                    Thread.sleep(getVelocidad() * 100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(clsMinero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Principal.TotalMaterialExtraido(capacidad_carga);
            if (Principal.Consultar_Disponibilidad_material(destino)<=0) {
              Principal.minas.get(id).actualizarDeposito(destino);
              int nuevodestino=Principal.minas.get(id).devolverDestino(destino);
              
                if (nuevodestino!=99) {
                    destino=nuevodestino;
                    this.posiciones=Principal.minas.get(id).ruta(destino);
                }else{
                    JOptionPane.showMessageDialog(null,"no hay mas depositos!!!");
                    
                }
                
                
              
            }
        }
        
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the especialidad
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * @param especialidad the especialidad to set
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * @return the velocidad
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * @param velocidad the velocidad to set
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * @return the tiiempo_extraccion
     */
    public int getTiiempo_extraccion() {
        return tiiempo_extraccion;
    }

    /**
     * @param tiiempo_extraccion the tiiempo_extraccion to set
     */
    public void setTiiempo_extraccion(int tiiempo_extraccion) {
        this.tiiempo_extraccion = tiiempo_extraccion;
    }

    /**
     * @return the sueldo
     */
    public int getSueldo() {
        return sueldo;
    }

    /**
     * @param sueldo the sueldo to set
     */
    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }

    /**
     * @return the Imagen
     */
    public ImageIcon getImagen() {
        return Imagen;
    }

    /**
     * @param Imagen the Imagen to set
     */
    public void setImagen(ImageIcon Imagen) {
        this.Imagen = Imagen;
    }

    /**
     * @return the posiciones
     */
    public ArrayList<Point> getPosiciones() {
        return posiciones;
    }

    /**
     * @param posiciones the posiciones to set
     */
    public void setPosiciones(ArrayList<Point> posiciones) {
        this.posiciones = posiciones;
    }

    /**
     * @return the capacidad_carga
     */
    public int getCapacidad_carga() {
        return capacidad_carga;
    }

    /**
     * @param capacidad_carga the capacidad_carga to set
     */
    public void setCapacidad_carga(int capacidad_carga) {
        this.capacidad_carga = capacidad_carga;
    }

    /**
     * @return the casillas
     */
    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    /**
     * @param casillas the casillas to set
     */
    public void setCasillas(ArrayList<Casilla> casillas) {
        this.casillas = casillas;
    }

    
    
}
