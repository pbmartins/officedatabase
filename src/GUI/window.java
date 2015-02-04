package GUI;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;
import java.io.*;
import java.sql.*;

import net.proteanit.sql.DbUtils;

import org.sqlite.*;
/**
 *
 * @author pedromartins
 */
@SuppressWarnings("unused")
public class window extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel NameLabel;
    private JLabel FileTypeLabel;
    private JLabel FileNumberLabel;
    private JTextField NameField;
    private JTextField FileTypeField;
    private JTextField FileNumberField;
    private JButton AddButton;
    private JButton RemoveButton;
    private JButton SearchButton;
    private JButton ResetButton;
    private JComboBox<String> SearchCB;

    private JTable ListTable;
    private TableModel model;
    private JScrollPane scrollListPane;

    Connection connection = null;
    
    public window() throws IOException {
    	super("Office Database");
    	setLayout(new FlowLayout());
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(new Dimension(600,400));
        setResizable(false);
        setLocationRelativeTo(null);

        //Load Database
        connection = sqlConnection.dbConnector();
        addWindowListener(new WindowHandler());

        //Adicionar à base de dados
        NameLabel = new JLabel("Nome do cliente:");
    	add(NameLabel);
        NameField = new JTextField("",32);
        add(NameField);

        FileTypeLabel = new JLabel("Tipo de processo:");
        add(FileTypeLabel);
        FileTypeField = new JTextField("",32);
        add(FileTypeField);

        FileNumberLabel = new JLabel("Número do processo:");
        add(FileNumberLabel);
        FileNumberField = new JTextField("",30);
        add(FileNumberField);

        AddButton = new JButton("Adicionar");
        addDatabaseHandler AddButton_Handler = new addDatabaseHandler();
        AddButton.addMouseListener(AddButton_Handler);
        AddButton.addMouseMotionListener(AddButton_Handler);
        add(AddButton);

        RemoveButton = new JButton("Remover");
        removeDatabaseHandler RemoveButton_Handler = new removeDatabaseHandler();
        RemoveButton.addMouseListener(RemoveButton_Handler);
        RemoveButton.addMouseMotionListener(RemoveButton_Handler);
        add(RemoveButton);

        SearchButton = new JButton("Pesquisar");
        searchDatabaseHandler SearchButton_Handler = new searchDatabaseHandler();
        SearchButton.addMouseListener(SearchButton_Handler);
        SearchButton.addMouseMotionListener(SearchButton_Handler);
        add(SearchButton);

        String args[] = {"Conjuntamente","Independentemente"};
        SearchCB = new JComboBox<String>(args);
        add(SearchCB);

        //Tabela

        try {
        	String query = "select * from office";
        	PreparedStatement pst = connection.prepareStatement(query);
        	ResultSet rs = pst.executeQuery();
        	
        	model = DbUtils.resultSetToTableModel(rs);
        	
        	rs.close();
        	pst.close();
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, e);
        }

        ListTable = new JTable(model);
        ListTable.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Nome");
        ListTable.getTableHeader().getColumnModel().getColumn(1).setHeaderValue("Tipo de Processo");
        ListTable.getTableHeader().getColumnModel().getColumn(2).setHeaderValue("Número do Processo");
        repaint();

        TableColumn list_column = null;
        for (int i=0; i<3; i++) {
            list_column = ListTable.getColumnModel().getColumn(i);
            if (i == 0) {
                list_column.setPreferredWidth(220); //first column is bigger
            } else if(i == 1) {
                list_column.setPreferredWidth(180);
            } else if(i == 2) {
                list_column.setPreferredWidth(140);
            }
        }

        scrollListPane = new JScrollPane(ListTable);
        scrollListPane.setPreferredSize(new Dimension(500,200));
        ListTable.setFillsViewportHeight(true);
        add(scrollListPane);

        //Reset Button

        ResetButton = new JButton("Apagar pesquisa");
        resetHandler reset_handler = new resetHandler();
        ResetButton.addMouseListener(reset_handler);
        ResetButton.addMouseMotionListener(reset_handler);
        add(ResetButton);

    }

    public class addDatabaseHandler implements MouseListener, MouseMotionListener {
        //Add to Database event
        public void mouseClicked(MouseEvent event) {
            
            if (NameField.getText().length()==0 || FileTypeField.getText().length()==0 || FileNumberField.getText().length()==0) JOptionPane.showMessageDialog(null,"Nenhum campo pode estar vazio!");
            else {
                //Add to Table
                DefaultTableModel model = (DefaultTableModel) ListTable.getModel();
                model.addRow(new Object[] { NameField.getText(), FileTypeField.getText(), Integer.parseInt(FileNumberField.getText()) });
                
                try {
                	String query = "insert into office (rowid, name, file_type, file_number) values (?, ?, ?, ?)";
                	PreparedStatement pst = connection.prepareStatement(query);
                	pst.setInt(1, ListTable.getRowCount());
                	pst.setString(2, NameField.getText());
                	pst.setString(3, FileTypeField.getText());
                	pst.setString(4, FileNumberField.getText());
                	
                	pst.execute();

                	pst.close();
                } catch (Exception e) {
                	JOptionPane.showMessageDialog(null, e);
                }
                
                JOptionPane.showMessageDialog(null,"Arquivo adicionado com sucesso!");
        }
        }

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}
    }

    private class searchDatabaseHandler implements MouseListener, MouseMotionListener {
        //Search Database event
        public void mouseClicked(MouseEvent event) {
            String name = NameField.getText();
            String file_type = FileTypeField.getText();
            String file_number = FileNumberField.getText();

            //Search in table
            
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
            ListTable.setRowSorter(sorter);
            List<RowFilter<TableModel, Integer>> filters = new ArrayList<RowFilter<TableModel, Integer>>();

            if (file_type.length()==0 && file_number.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+name));
            } else if (name.length()==0 && file_number.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+file_type));
            } else if (name.length()==0 && file_type.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+file_number));
            } else if (file_number.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+name));
                filters.add(RowFilter.regexFilter("(?i)"+file_type));
            } else if (file_type.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+name));
                filters.add(RowFilter.regexFilter("(?i)"+file_number));
            } else if (name.length()==0) {
                filters.add(RowFilter.regexFilter("(?i)"+file_type));
                filters.add(RowFilter.regexFilter("(?i)"+file_number));
            } else {
                filters.add(RowFilter.regexFilter("(?i)"+name));
                filters.add(RowFilter.regexFilter("(?i)"+file_type));
                filters.add(RowFilter.regexFilter("(?i)"+file_number));
            }

            if (name.length()==0 && file_type.length()==0 && file_number.length()==0) {
                sorter.setRowFilter(null);
                JOptionPane.showMessageDialog(null,"Pelo menos, um dos campos não pode estar em branco");
            } else if(SearchCB.getSelectedIndex()==0) {
                RowFilter<TableModel, Integer> row_filter = RowFilter.andFilter(filters);
                sorter.setRowFilter(row_filter);
            } else if(SearchCB.getSelectedIndex()==1) {
                RowFilter<TableModel, Integer> row_filter = RowFilter.orFilter(filters);
                sorter.setRowFilter(row_filter);
            }

            
        }

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}
    }

    private class removeDatabaseHandler implements MouseListener, MouseMotionListener {
        //Remove Database event
        public void mouseClicked(MouseEvent event) {
            //Remove from Table
            int remove=0, c=0;
            String remove_filenumber = new String();
            DefaultTableModel model = (DefaultTableModel) ListTable.getModel();
            
            if (NameField.getText().length()==0 && FileNumberField.getText().length()==0) {
            	JOptionPane.showMessageDialog(null, "Tem de preencher o campo 'Nome' ou 'Número do Processo' para poder remover");
            } else if (NameField.getText().length()==0 && FileNumberField.getText().length()!=0) {
            	for (int i=0; i<ListTable.getRowCount(); i++) {
            		Object filenumber_row = model.getValueAt(i,2);
            		if (filenumber_row.toString().equals(FileNumberField.getText())) {
            			remove_filenumber = filenumber_row.toString();
                        remove=i;
                        c++;
            		}
            	}
            } else if (NameField.getText().length()!=0 && FileNumberField.getText().length()==0) {
            	for (int i=0; i<ListTable.getRowCount(); i++) {
            		Object name_row = model.getValueAt(i,0);
            		Object filenumber_row = model.getValueAt(i,2);
            		if (name_row.toString().equals(NameField.getText())) {
            			remove_filenumber = filenumber_row.toString();
                        remove=i;
                        c++;
            		}
            	}
            } else if (NameField.getText().length()!=0 && FileNumberField.getText().length()!=0) {
            	for (int i=0; i<ListTable.getRowCount(); i++) {
            		Object name_row = model.getValueAt(i,0);
            		Object filenumber_row = model.getValueAt(i,2);
            		if (name_row.toString().equals(NameField.getText()) || filenumber_row.toString().equals(FileNumberField.getText())) {
            			remove_filenumber = filenumber_row.toString();
                        remove=i;
                        c++;
            		}
            	}
            }
            
            if(c==0) JOptionPane.showMessageDialog(null,"Não existe nenhum arquivo com o nome inserido!");
            else {
            	//Delete from table
                model.removeRow(remove);
                
                //Delete from database
                try{
                	String query = "delete from office where file_number=?";
                	PreparedStatement pst = connection.prepareStatement(query);
                	pst.setString(1, remove_filenumber);
                	pst.execute();
                	pst.close();
                } catch (Exception e) {
                	JOptionPane.showMessageDialog(null, e);
                }
                
                JOptionPane.showMessageDialog(null,"Arquivo removido com sucesso!");
            }

        }

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}
    }

    private class resetHandler implements MouseListener, MouseMotionListener {
        //Reset Search event
        public void mouseClicked(MouseEvent event) {
            //Reset Search
            
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
            ListTable.setRowSorter(sorter);
            sorter.setRowFilter(null);
            
            //Reset Fields
            
            NameField.setText(null);
            FileTypeField.setText(null);
            FileNumberField.setText(null);
            
        }

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}
    }

    private class WindowHandler implements WindowListener {
        public void windowOpened(WindowEvent event) {}

        public void windowClosing(WindowEvent event) {

            int closing = JOptionPane.showConfirmDialog(null,"Tem a certeza que pretende sair?");
            if (closing==JOptionPane.YES_OPTION) {
                System.exit(0);
            }

        }

        public void windowClosed(WindowEvent event) {}

        public void windowIconified(WindowEvent event) {}

        public void windowDeiconified(WindowEvent event) {}

        public void windowActivated(WindowEvent event) {}

        public void windowDeactivated(WindowEvent event) {}
    }

}
