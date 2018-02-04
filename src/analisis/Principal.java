/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public  class Principal extends javax.swing.JFrame{
    static public ArrayList<Thread>hilos=new ArrayList<>();
    DefaultListModel modelo_Minas= new DefaultListModel();
    private ArrayList<clsMinero>mineros=new ArrayList<>();
    public static ArrayList<clsMina>minas=new ArrayList<>();
    static int  botontunel = 0;
    static int botonentrada = 0;
    static int botondeposito = 0;
    static boolean entrada=true;
    static  String material;
    static int indexminaactual=-1;
    
    public  Principal() {
        
         
        this.setDefaultCloseOperation(Principal.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                TerminarPrograma();
                System.exit(0);
            }

        }
        );
        initComponents(); 
        
    }
    
    public void TerminarPrograma(){
        
        for (int i = 0; i < hilos.size(); i++) {
            hilos.get(i).stop();
        }
    }
    synchronized public static void TotalMaterialExtraido(int cantidadSumar){
        int total=Integer.parseInt(lblcantidadMaterial.getText())+cantidadSumar;
        lblcantidadMaterial.setText(String.valueOf(total));
       
    }
    
    
    public void Cargar(File ruta) throws Exception {

        JSONParser parser = new JSONParser();
         Object obj = parser.parse(new FileReader(ruta));
        JSONObject archivocargado = (JSONObject) obj;
        JSONArray tags = (JSONArray) archivocargado.get("minas");
        for (int i = 0; i < tags.size(); i++) {
            JSONObject info=(JSONObject)tags.get(i);
            material=info.get("tipoMineral").toString();
            int capadeposito=Integer.parseInt(info.get("capacidadDeposito").toString());
            double tiempoextraccion=Double.parseDouble(info.get("tiempoExtraccion").toString())*100;
            double velocidaddesplaza=Double.parseDouble(info.get("velocidadDesplazamiento").toString());
            double numeromineros=Double.parseDouble(info.get("capacidadMineros").toString());
            crearmina(material,(int)numeromineros,(int)tiempoextraccion,(int)velocidaddesplaza);
            JSONObject ent=(JSONObject)info.get("entradaMina");
            crearEntradamina(Integer.parseInt(ent.get("x").toString()),Integer.parseInt(ent.get("y").toString()));
            JSONArray tags2 = (JSONArray) info.get("seccionesMina");
            for (int j = 0; j < tags2.size(); j++) {
                JSONObject seccimina=(JSONObject)tags2.get(j);
                if (seccimina.get("tipo").toString().equals("T")) {
                    crearTunel(Integer.parseInt(seccimina.get("x").toString()),Integer.parseInt(seccimina.get("y").toString()));
                }
                else{
                    crearDeposito(Integer.parseInt(seccimina.get("x").toString()),Integer.parseInt(seccimina.get("y").toString()),capadeposito);
                }
            }
            
            
        }
       

    }
    public void crearDeposito(int x,int y,int cantidadmaterial){
        Point dep=new Point(x, y);
        ImageIcon nuevaimegen;
        if (material.equalsIgnoreCase("oro")) {
            nuevaimegen=new  ImageIcon(getClass().getResource("../imagenes/oro.png"));
        }
        else if (material.equalsIgnoreCase("plata")) {
            nuevaimegen=new  ImageIcon(getClass().getResource("../imagenes/plata.png"));
        }
        else{
            nuevaimegen=new  ImageIcon(getClass().getResource("../imagenes/cobre.png"));
        }
        
        for (int i = 0; i < minas.get(indexminaactual).casillas.size(); i++) {
            if (minas.get(indexminaactual).casillas.get(i).getId().x==x&&minas.get(indexminaactual).casillas.get(i).getId().y==y) {
                minas.get(indexminaactual).casillas.get(i).fondo = nuevaimegen;
                minas.get(indexminaactual).ingresarnuemromapa(dep, minas.get(indexminaactual).depostios.size() + 1);
                minas.get(indexminaactual).crearDeposito(minas.get(indexminaactual).depostios.size() + 1, cantidadmaterial, dep);
                cargar_Tabla(minas.get(indexminaactual).depostios.size(), cantidadmaterial);
            }
        }
        
        getContentPane().repaint();
    }
    public void crearTunel(int x,int y){
        Point tun=new Point(x, y);
        
        for (int i = 0; i < minas.get(indexminaactual).casillas.size(); i++) {
            if (minas.get(indexminaactual).casillas.get(i).getId().x==x&&minas.get(indexminaactual).casillas.get(i).getId().y==y) {
                ImageIcon nuevaimegen=new  ImageIcon(getClass().getResource("../imagenes/tunel.png"));
                minas.get(indexminaactual).casillas.get(i).fondo= nuevaimegen;
                minas.get(indexminaactual).ingresarnuemromapa(tun, -1);
            }
        }
        
        getContentPane().repaint();
    }
    
    public void crearEntradamina(int x,int y){
        Point entra=new Point(x, y);
        minas.get(indexminaactual).setEntrada(entra);
        for (int i = 0; i < minas.get(indexminaactual).casillas.size(); i++) {
            if (minas.get(indexminaactual).casillas.get(i).getId().x==x&&minas.get(indexminaactual).casillas.get(i).getId().y==y) {
                ImageIcon nuevaimegen=new  ImageIcon(getClass().getResource("../imagenes/entrada2.png"));
                minas.get(indexminaactual).casillas.get(i).fondo= nuevaimegen;
                minas.get(indexminaactual).ingresarnuemromapa(entra, 100);
            }
        }
        
        getContentPane().repaint();
    }
    
       public void crearmina(String material,int cantidadmineros,int tiempoextraccion,int velocidaddesplazamiento){
          
        this.material=material;
        Limpiar_Disponibilidad_material();
        if (indexminaactual >= 0) {
            Eliminar_casillas();
        }
        if (material.equalsIgnoreCase("oro") || material.equalsIgnoreCase("plata") || material.equalsIgnoreCase("cobre")) {
            entrada = true;
            botones();
            clsMina m = new clsMina(this.minas.size(), material,cantidadmineros,tiempoextraccion, velocidaddesplazamiento);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 12; j++) {
                    Casilla c = new Casilla();
                    c.setId(new Point(i, j));
                    c.setMina(minas.size());
                    getContentPane().setLayout(null);
                    getContentPane().add(c);
                    c.setVisible(true);
                    c.setBounds(j * 75 + 280, i * 75, 75, 75);
                    m.casillas.add(c);
                    
                }
            }
            minas.add(m);
            modelo_Minas.addElement(material);
            ListaMinas.setModel(modelo_Minas);
            indexminaactual++;
        } else {
            JOptionPane.showMessageDialog(this, "Material invalido");
        }
        
    
    }

    public void Actualizar_Tabla_Seleccionada(){
       Limpiar_Disponibilidad_material();
        DefaultTableModel modelo = (DefaultTableModel) tblCantidaddepositos.getModel();
        for (int i = 0; i < minas.get(indexminaactual).depostios.size(); i++) {
            modelo.addRow(new Object[]{i+1,minas.get(indexminaactual).depostios.get(i).getCantidad_material()});
        }
    }
    
    public void Actualizar_Mina_Seleccionada(){
        Eliminar_casillas();
        getContentPane().repaint();
        for (int i = 0; i < minas.get(indexminaactual).casillas.size(); i++) {
                
                    getContentPane().setLayout(null);
                    getContentPane().add(minas.get(indexminaactual).casillas.get(i));
                   
           }
    
    }
     public void botones() {
        ImageIcon face = new ImageIcon(getClass().getResource("../imagenes/icono_camino.png"));
        btnTunel.setIcon(face);
        face = new ImageIcon(getClass().getResource("../imagenes/icono_depositos.png"));
        btnDeposito.setIcon(face);
        face = new ImageIcon(getClass().getResource("../imagenes/icono_entrada.png"));
        btnEntrada.setIcon(face);
        
    }
     public void Eliminar_casillas(){
         for (int i = 5; i < 101; i++) {
             getContentPane().remove(5);
             
         }
             
         
     }
     
     public static  void notificar(int mina,Point p){
         if (botontunel==1) {
             minas.get(mina).ingresarnuemromapa(p, -1);
             
         }
         else if (botonentrada==1) {
             entrada=false;
             minas.get(mina).ingresarnuemromapa(p,100);
             minas.get(mina).setEntrada(p);
         }else{
             int cm=Integer.parseInt(JOptionPane.showInputDialog(null,"ingrese cantidad de material"));
             minas.get(mina).ingresarnuemromapa(p,minas.get(mina).depostios.size()+1);
             minas.get(mina).crearDeposito(minas.get(mina).depostios.size()+1,cm,p);
             cargar_Tabla(minas.get(mina).depostios.size(),cm);
         }
     }
     
     
     public static void cargar_Tabla(int deposito,int cantidad){
         DefaultTableModel modelo= (DefaultTableModel)tblCantidaddepositos.getModel();
         modelo.addRow(new Object[]{String.valueOf(deposito),String.valueOf(cantidad)});
         
     }
     public void Limpiar_Disponibilidad_material(){
         DefaultTableModel modelo= (DefaultTableModel)tblCantidaddepositos.getModel();
         int cantidad=tblCantidaddepositos.getRowCount();
         for (int i = 0;i<cantidad; i++) {
            
            modelo.removeRow(0);
            
        }
         
     }
     
    public static int Consultar_Disponibilidad_material(int deposito) {
        int disponibilidad = 0;
        for (int i = 0; i < tblCantidaddepositos.getRowCount(); i++) {
            if (String.valueOf(deposito).equalsIgnoreCase(String.valueOf(tblCantidaddepositos.getValueAt(i, 0)))) {
                disponibilidad=Integer.parseInt(String.valueOf(tblCantidaddepositos.getValueAt(i, 1)));
            }
            
        }
        return  disponibilidad;
    }
    public synchronized static void Descontar_Disponibilidad_material (int deposito,int cantidad_descontar) {
        minas.get(indexminaactual).depostios.get(deposito-1).setCantidad_material(minas.get(indexminaactual).depostios.get(deposito-1).getCantidad_material()-cantidad_descontar);
        for (int i = 0; i < tblCantidaddepositos.getRowCount(); i++) {
            if (String.valueOf(deposito).equalsIgnoreCase(String.valueOf(tblCantidaddepositos.getValueAt(i, 0)))) {
                int nuevo_valor=Integer.parseInt(String.valueOf(tblCantidaddepositos.getValueAt(i, 1)))-cantidad_descontar;
                tblCantidaddepositos.setValueAt(nuevo_valor, i, 1);
            }
            
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        btnTunel = new javax.swing.JButton();
        btnDeposito = new javax.swing.JButton();
        btnEntrada = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCantidaddepositos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        ListaMinas = new javax.swing.JList();
        lblMaterial = new javax.swing.JLabel();
        lblcantidadMaterial = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        btnTunel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTunelActionPerformed(evt);
            }
        });

        btnDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositoActionPerformed(evt);
            }
        });

        btnEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaActionPerformed(evt);
            }
        });

        tblCantidaddepositos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Deposito", "cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblCantidaddepositos);

        ListaMinas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ListaMinasValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(ListaMinas);

        lblMaterial.setText("Material");

        lblcantidadMaterial.setText("0");

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Crear");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Simulacion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Pruebas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Cargar Json");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Crear mineros");
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnEntrada, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(btnTunel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(btnDeposito, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblcantidadMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1059, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTunel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMaterial)
                        .addGap(3, 3, 3)
                        .addComponent(lblcantidadMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Limpiar_Disponibilidad_material();
        if (indexminaactual >= 0) {
            Eliminar_casillas();
        }
        
        material = JOptionPane.showInputDialog(this, "Ingrese material de la mina");
        if (material.equalsIgnoreCase("oro") || material.equalsIgnoreCase("plata") || material.equalsIgnoreCase("cobre")) {
            entrada = true;
            botones();
            clsMina m = new clsMina(this.minas.size(), material, Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese cantidad maxima de mineros")), Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese tiempo de extraccion")), Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese velocidad de desplazamiento")));
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 12; j++) {
                    Casilla c = new Casilla();
                    c.setId(new Point(i, j));
                    c.setMina(minas.size());
                    getContentPane().setLayout(null);
                    getContentPane().add(c);
                    c.setVisible(true);
                    c.setBounds(j * 75 + 280, i * 75, 75, 75);
                    m.casillas.add(c);
                    
                }
            }
            minas.add(m);
            modelo_Minas.addElement(material);
            ListaMinas.setModel(modelo_Minas);
            indexminaactual++;
        } else {
            JOptionPane.showMessageDialog(this, "Material invalido");
        }
        
        
         
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//        ImageIcon face = new ImageIcon(getClass().getResource("../imagenes/icono_camino.png"));
//        btnTunel.setIcon(face);
//        face = new ImageIcon(getClass().getResource("../imagenes/icono_depositos.png"));
//        btnDeposito.setIcon(face);
//        face = new ImageIcon(getClass().getResource("../imagenes/icono_entrada.png"));
//        btnEntrada.setIcon(face);
    }//GEN-LAST:event_formWindowOpened

    private void btnTunelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTunelActionPerformed
        botontunel=1;
        botonentrada=0;
        botondeposito=0;
    }//GEN-LAST:event_btnTunelActionPerformed

    private void btnDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositoActionPerformed
        botondeposito=1;
        botonentrada=0;
        botontunel=0;
    }//GEN-LAST:event_btnDepositoActionPerformed

    private void btnEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaActionPerformed
        
            botonentrada = 1;
            botontunel = 0;
            botondeposito = 0;
        
        
    }//GEN-LAST:event_btnEntradaActionPerformed
;
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int min = Integer.parseInt(JOptionPane.showInputDialog(this, "ingrese cantidad de mineros"));
        for (int i = 0; i < min; i++) {
            clsMinero minero = new clsMinero();
            minas.get(indexminaactual).asignar(minero);
            
            
        }
        minas.get(indexminaactual).simular();
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void ListaMinasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ListaMinasValueChanged
        
        indexminaactual=ListaMinas.getSelectedIndex();
        Actualizar_Mina_Seleccionada();
        Actualizar_Tabla_Seleccionada();
    }//GEN-LAST:event_ListaMinasValueChanged

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JFileChooser file = new JFileChooser();
        file.showOpenDialog(this);
        File abre = file.getSelectedFile();
        
        try {
            Cargar(abre);
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ListaMinas;
    private javax.swing.JButton btnDeposito;
    private javax.swing.JButton btnEntrada;
    private javax.swing.JButton btnTunel;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMaterial;
    public static javax.swing.JLabel lblcantidadMaterial;
    public static javax.swing.JTable tblCantidaddepositos;
    // End of variables declaration//GEN-END:variables
}