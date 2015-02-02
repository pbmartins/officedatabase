package GUI;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author pedromartins
 */
public class window extends JFrame {
    
    private JLabel AddLabel;
    private JLabel NameLabel;
    private JLabel ContactLabel;
    private JLabel FileNumberLabel;
    private JTextField NameField;
    private JTextField ContactField;
    private JTextField FileNumberField;
    private JButton AddButton;
    private JButton RemoveButton;
    private JButton SearchButton;
    private JButton ResetButton;
    private JComboBox SearchCB;

    private JTable ListTable;
    private TableModel model;
    private JScrollPane scrollListPane;

    //Load Database
    static file database[];
    

    public window() throws IOException {
    	super("Office Database");
    	setLayout(new FlowLayout());

        //Load Database
        database = loadDatabase();
        addWindowListener(new WindowHandler());

        //Adicionar à base de dados
        NameLabel = new JLabel("Nome:");
    	add(NameLabel);
        NameField = new JTextField("",40);
        add(NameField);

        ContactLabel = new JLabel("Contacto:");
        add(ContactLabel);
        ContactField = new JTextField("",40);
        add(ContactField);

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

        Object args[] = {"Conjuntamente","Independentemente"};
        SearchCB = new JComboBox(args);
        add(SearchCB);



        //Tabela

        Object column_names[] = {
            "Nome",
            "Contacto",
            "Número do processo"
        };

        Object data[][] = new Object[database.length][3];

        for (int i=0; i<database.length; i++) {
            data[i][0] = database[i].name;
            data[i][1] = database[i].contact;
            data[i][2] = database[i].file_number;
        }

        model = new DefaultTableModel(data, column_names) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ListTable = new JTable(model);

        TableColumn list_column = null;
        for (int i=0; i<3; i++) {
            list_column = ListTable.getColumnModel().getColumn(i);
            if (i == 0) {
                list_column.setPreferredWidth(250); //first column is bigger
            } else if(i == 1) {
                list_column.setPreferredWidth(120);
            } else if(i == 2) {
                list_column.setPreferredWidth(180);
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
            file newC = new file();

            newC.name = NameField.getText();
            newC.contact = Integer.parseInt(ContactField.getText());
            newC.file_number = Integer.parseInt(FileNumberField.getText());
            if (NameField.getText().length()==0 || ContactField.getText().length()==0 || FileNumberField.getText().length()==0) JOptionPane.showMessageDialog(null,"Nenhum campo pode estar vazio!");
            else {
            //Add to File
            file b[] = database;
            database = new file[b.length+1];
            int i;

            for (i=0; i<b.length; i++) {
                database[i] = b[i];
            } 

            database[i] = newC;

            //Add to Table
            DefaultTableModel model = (DefaultTableModel) ListTable.getModel();
            model.addRow(new Object[] { NameField.getText(), Integer.parseInt(ContactField.getText()), Integer.parseInt(FileNumberField.getText()) });

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
            String contact = ContactField.getText();
            String file_number = FileNumberField.getText();

            //Search in table
            
            TableRowSorter sorter = new TableRowSorter(model);
            ListTable.setRowSorter(sorter);
            List filters = new ArrayList();

            if (contact.length()==0 && file_number.length()==0) {
                filters.add(RowFilter.regexFilter(name));
            } else if (name.length()==0 && file_number.length()==0) {
                filters.add(RowFilter.regexFilter(contact));
            } else if (name.length()==0 && contact.length()==0) {
                filters.add(RowFilter.regexFilter(file_number));
            } else if (file_number.length()==0) {
                filters.add(RowFilter.regexFilter(name));
                filters.add(RowFilter.regexFilter(contact));
            } else if (contact.length()==0) {
                filters.add(RowFilter.regexFilter(name));
                filters.add(RowFilter.regexFilter(file_number));
            } else if (name.length()==0) {
                filters.add(RowFilter.regexFilter(contact));
                filters.add(RowFilter.regexFilter(file_number));
            } else {
                filters.add(RowFilter.regexFilter(name));
                filters.add(RowFilter.regexFilter(contact));
                filters.add(RowFilter.regexFilter(file_number));
            }

            if (name.length()==0 && contact.length()==0 && file_number.length()==0) {
                sorter.setRowFilter(null);
                JOptionPane.showMessageDialog(null,"Pelo menos, um dos campos não pode estar em branco");
            } else if(SearchCB.getSelectedIndex()==0) {
                RowFilter row_filter = RowFilter.andFilter(filters);
                sorter.setRowFilter(row_filter);
            } else if(SearchCB.getSelectedIndex()==1) {
                RowFilter row_filter = RowFilter.orFilter(filters);
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
            DefaultTableModel model = (DefaultTableModel) ListTable.getModel();
            for (int i=0; i<database.length; i++) {
                Object row = model.getValueAt(i,0);
                if (row.toString().indexOf(NameField.getText())>-1) {
                    remove=i;
                    c++;
                }
            }
            
            if(c==0) JOptionPane.showMessageDialog(null,"Não existe nenhum arquivo com o nome inserido!");
            else {
                model.removeRow(remove);
                JOptionPane.showMessageDialog(null,"Arquivo removido com sucesso!");
                //Remove from File
                database = removeIndexFile(remove);
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
            
            TableRowSorter sorter = new TableRowSorter(model);
            ListTable.setRowSorter(sorter);
            sorter.setRowFilter(null);
            
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
            
            try{
                saveDatabase();
            }
            catch(IOException e){
                //do something with e... log, perhaps rethrow etc.
            }
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

    public static file[] loadDatabase() throws IOException {
        File database_file = new File("database.txt");
        Scanner sf = new Scanner(database_file);
        int interval = 5, i=0;
        String tmp_contact = new String();
        String tmp_file = new String();
        
        file database[] = new file[interval];
        file b[];
        
        
        while (sf.hasNextLine()) {
            if (i<interval) {
                file newC = new file();
                newC.name = sf.nextLine();
                tmp_contact = sf.nextLine();
                newC.contact = Integer.parseInt(tmp_contact);
                tmp_file = sf.nextLine();
                newC.file_number = Integer.parseInt(tmp_file);
                database[i] = newC;
                i++;
            } else {
                b = database;
                database = new file[interval+=interval];
                for (int j=0; j<b.length; j++) {
                    database[j] = b[j];
                }
                file newC = new file();
                newC.name = sf.nextLine();
                tmp_contact = sf.nextLine();
                newC.contact = Integer.parseInt(tmp_contact);
                tmp_file = sf.nextLine();
                newC.file_number = Integer.parseInt(tmp_file);
                database[i] = newC;
                i++;
            }
        }
        
        if (i<interval) {
            b = database;
            database = new file[i];
            for (int j=0; j<i; j++) {
                database[j] = b[j];
            }
        }
        
        return database;
    }

    public static file[] removeIndexFile(int index) {
        file database_final[] = new file[database.length-1];
        int i;

        for (i=0; i<database.length; i++) {
            if (i==index) break;
            else {
                database_final[i] = database[i];
            }
        }

        for (int j=i+1; j<database.length; j++) {
            database_final[j-1] = database[j];
        }

        return database_final;
    }

    public static void saveDatabase() throws IOException {
        File database_file = new File("database.txt");
        PrintWriter pw = new PrintWriter(database_file);
        for (int i=0; i<database.length; i++) {
            pw.println(database[i].name);
            pw.println(database[i].contact);
            pw.println(database[i].file_number);
        }
        pw.close();
        System.out.println("Salvo com sucesso.");
    }

}

class file {
    String name;
    int contact;
    int file_number;
}