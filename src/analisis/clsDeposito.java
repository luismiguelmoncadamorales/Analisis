/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.awt.Point;

/**
 *
 * @author miguel
 */
public class clsDeposito {
    private int id;
    private int cantidad_material;
    private Point posisicon;

    public clsDeposito(int id, int cantidad_material, Point posisicon) {
        this.id = id;
        this.cantidad_material = cantidad_material;
        this.posisicon = posisicon;
    }

    /**
     * @return the cantidad_material
     */
    public int getCantidad_material() {
        return cantidad_material;
    }

    /**
     * @param cantidad_material the cantidad_material to set
     */
    public void setCantidad_material(int cantidad_material) {
        this.cantidad_material = cantidad_material;
    }
     
    
}
