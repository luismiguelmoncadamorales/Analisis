/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author miguel
 */
public class Casilla extends javax.swing.JPanel {
    private Point id;
    private  int mina;
   
  
    public Casilla() {
        
        initComponents();
    }
        ImageIcon fondo=new ImageIcon(getClass().getResource("../imagenes/piedra.png"));
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(75, 75));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 73, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
      
        if (Principal.botonentrada==1&&Principal.entrada) {
            fondo=new ImageIcon(getClass().getResource("../imagenes/entrada2.png"));
            this.getGraphics().drawImage(fondo.getImage(), 0, 0,74,75, null);  
            Principal.notificar(getMina(), id);
        }
        if (Principal.botontunel==1) {
            fondo = new ImageIcon(getClass().getResource("../imagenes/tunel.png"));
            this.getGraphics().drawImage(fondo.getImage(), 0, 0, 74, 75, null);
            Principal.notificar(getMina(), id);
        }
        //debe de pintar el deposito dependiendo del material de la mina
        if (Principal.botondeposito==1) {
            if (Principal.material.equalsIgnoreCase("oro")) {
                fondo = new ImageIcon(getClass().getResource("../imagenes/oro.png"));
            }
            if (Principal.material.equalsIgnoreCase("plata")) {
                fondo = new ImageIcon(getClass().getResource("../imagenes/plata.png"));
            }
            if (Principal.material.equalsIgnoreCase("cobre")) {
                fondo = new ImageIcon(getClass().getResource("../imagenes/cobre.png"));
            }
            
            this.getGraphics().drawImage(fondo.getImage(), 0, 0, 74, 75, null);
            Principal.notificar(getMina(), id);
            
        }
        
        
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public Point getId() {
        return id;
    }
    public void setId(Point id) {
        this.id = id;
    }

    public void paint(Graphics g){
        g.drawImage(fondo.getImage(), 0, 0,74,75, this);
    }

    /**
     * @return the mina
     */
    public int getMina() {
        return mina;
    }

    /**
     * @param mina the mina to set
     */
    public void setMina(int mina) {
        this.mina = mina;
    }
   
    synchronized public void cambiarfondo(ImageIcon i){
       this.getGraphics().drawImage(i.getImage(), 0, 0,74,75, null); 
    }
    synchronized public void cambiarfondo2(){
       
       this.getGraphics().drawImage(fondo.getImage(), 0, 0,74,75, null); 
    }
}
