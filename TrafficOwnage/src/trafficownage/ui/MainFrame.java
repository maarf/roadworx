/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 2-mei-2011, 21:36:47
 */

package trafficownage.ui;

import trafficownage.simulation.MainLoop;

/**
 *
 * @author Gerrit
 */
public class MainFrame extends javax.swing.JFrame implements UIListener {

    private MainLoop m;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();

        m = new MainLoop();
        m.init(this);

        mapComponent2.init(m);

        mapComponent2.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mapComponent1 = new trafficownage.ui.MapComponent();
        mapComponent2 = new trafficownage.ui.MapComponent();

        javax.swing.GroupLayout mapComponent1Layout = new javax.swing.GroupLayout(mapComponent1);
        mapComponent1.setLayout(mapComponent1Layout);
        mapComponent1Layout.setHorizontalGroup(
            mapComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        mapComponent1Layout.setVerticalGroup(
            mapComponent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mapComponent2Layout = new javax.swing.GroupLayout(mapComponent2);
        mapComponent2.setLayout(mapComponent2Layout);
        mapComponent2Layout.setHorizontalGroup(
            mapComponent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );
        mapComponent2Layout.setVerticalGroup(
            mapComponent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapComponent2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapComponent2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private trafficownage.ui.MapComponent mapComponent1;
    private trafficownage.ui.MapComponent mapComponent2;
    // End of variables declaration//GEN-END:variables

    public void carsUpdated() {
        
    }

}
