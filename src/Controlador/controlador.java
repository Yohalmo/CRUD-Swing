package Controlador;

import Modelo.Persona;
import Modelo.PersonaDAO;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class controlador implements ActionListener, KeyListener {

    PersonaDAO dao = new PersonaDAO();
    Persona p = new Persona();
    Vista v = new Vista();
    DefaultTableModel modelo = new DefaultTableModel();
    int rowSelected = -1;
    TableRowSorter<TableModel> rowSorter;
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == v.btnListar){
            this.Listar(v.Tabla);
        }

        if(e.getSource() == v.btnGuardar){
            this.Agregar();
        }

        if(e.getSource() == v.btnEliminar){
            this.Eliminar();
        }

        if(e.getSource() == v.btnEditar){
            this.Modificar();
        }

        if(e.getSource() == v.btnListo){
            this.CargarInfo();
        }

        if(e.getSource() == v.tfBuscador){
            this.Filtrar();
        }
    }
    
    public controlador(Vista v){
        this.v = v;
        this.v.btnListar.addActionListener(this);
        this.v.btnGuardar.addActionListener(this);
        this.v.btnEditar.addActionListener(this);
        this.v.btnEliminar.addActionListener(this);
        this.v.btnListo.addActionListener(this);
        this.v.tfBuscador.addKeyListener(this);
        
        rowSorter = new TableRowSorter<>(v.Tabla.getModel());
        v.Tabla.setRowSorter(rowSorter);
    }
    
    private void Agregar(){
        p.setNombre(v.tfNombre.getText());
        p.setCorreo(v.tfCorreo.getText());
        p.setTelefono(v.tfTelefono.getText());
        
        if(dao.Agregar(p) == 1){
            JOptionPane.showMessageDialog(v, "Usuario agregado");
            this.LimpiarCampos();
            p = dao.ultimo_registro();
            if(p.getId() > 0){
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(), 
                    p.getCorreo(),
                    p.getTelefono()
                });
                
                p.setId(0);
            }
        }else{
            JOptionPane.showMessageDialog(v, "Error");
        }
    }
    
    private void CargarInfo(){
        
        rowSelected = v.Tabla.getSelectedRow();
        
        if(rowSelected >= 0){
            v.tfId.setText(v.Tabla.getValueAt(rowSelected, 0).toString());
            v.tfNombre.setText(v.Tabla.getValueAt(rowSelected, 1).toString());
            v.tfCorreo.setText(v.Tabla.getValueAt(rowSelected, 2).toString());
            v.tfTelefono.setText(v.Tabla.getValueAt(rowSelected, 3).toString());
        }else{
            JOptionPane.showMessageDialog(v, "Debe seleccionar un registro primero");
        }
    }
    
    private void LimpiarCampos(){
        v.tfNombre.setText("");
        v.tfCorreo.setText("");
        v.tfTelefono.setText("");
        v.tfId.setText("");
    }
    
    private void Modificar(){
        
        if(this.rowSelected < 0){
            JOptionPane.showMessageDialog(v, "Debe seleccionar un registro primero");
            return;
        }
        
        p.setNombre(v.tfNombre.getText());
        p.setCorreo(v.tfCorreo.getText());
        p.setTelefono(v.tfTelefono.getText());
        p.setId(Integer.parseInt(v.tfId.getText()));
        
        int respuesta = JOptionPane.showConfirmDialog(v, "¿Deseas editar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
            
        if(respuesta == JOptionPane.YES_OPTION){
            if(dao.Editar(p) == 1){
                JOptionPane.showMessageDialog(v, "Usuario actualizado exitosamente");
                this.LimpiarCampos();

                modelo.setValueAt(p.getNombre(), rowSelected, 1);
                modelo.setValueAt(p.getCorreo(), rowSelected, 2);
                modelo.setValueAt(p.getTelefono(), rowSelected, 3);
                
                this.rowSelected = -1;
            }else{
                JOptionPane.showMessageDialog(v, "Error");
            }
        }
    }
    
    private void Eliminar(){
        
        int selectedRow = v.Tabla.getSelectedRow();
        
        if(selectedRow >= 0){
            int respuesta = JOptionPane.showConfirmDialog(v, "¿Deseas eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if(respuesta == JOptionPane.YES_OPTION){

                if(dao.Eliminar(Integer.parseInt(
                    v.Tabla.getValueAt(selectedRow, 0).toString()
                )) > 0){
                    JOptionPane.showMessageDialog(v, "Usuario eliminado exitosamente");
                    modelo.removeRow(v.Tabla.getSelectedRow());
                }else{
                    JOptionPane.showMessageDialog(v, "Error");
                }   
            }
        }else{
            JOptionPane.showMessageDialog(v, "Debe seleccionar un registro primero");
        }
    }
    
    private void Listar(JTable tabla){
        
        modelo = (DefaultTableModel)tabla.getModel();
        modelo.setRowCount(0);
        
        List<Persona> lista = dao.listar();
        Object[] object = new Object[4];
        
        for(Persona p: lista){
            object[0] = p.getId();
            object[1] = p.getNombre();
            object[2] = p.getCorreo();
            object[3] = p.getTelefono();
            
            modelo.addRow(object);
        }
    }
    
    private void Filtrar(){
        if (v.tfBuscador.getText().trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            // Filtrar por el texto escrito en cualquier columna
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + v.tfBuscador.getText()));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        this.Filtrar();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.Filtrar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.Filtrar();
    }
}
