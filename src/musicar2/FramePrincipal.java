
package musicar2;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.border.AbstractBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.Insets;

/**
 * Application to organize and select music files manually.
 * The user chooses songs one by one, edits their names and exports them
 * to the "Ordered" folder in the exact playback order.
 *
 * IMPORTANT: This is pure Java and does not require external tools
 * such as nircmd.exe. All files are copied using native Java streams.
 */
public class FramePrincipal extends javax.swing.JFrame {
    
    // IMPORTANT: Sequence counter and model for the selected songs list
    private int secuencia = 0;
    private DefaultListModel<String> modeloCancionesSeleccionadas;
    private java.util.List<File> archivosSeleccionados = new java.util.ArrayList<>();
    private java.util.Map<String, File> mapaCancionesArchivos = new java.util.HashMap<>(); // mapping song->file
    private String nombreSeleccionadoActual; // used to update the map when a name is edited
    // Application background (custom painted for dark theme)
    private javax.swing.JComponent backgroundPanel;
    
    // Filter to select common audio file types
    private FileNameExtensionFilter filtro = new FileNameExtensionFilter(
        "Music Files", "mp3", "wma", "wav", "flac", "m4a");
    
    public FramePrincipal() {
        // Apply a modern dark theme before creating components
        applyModernDarkTheme();
        initComponents();
        // Assign program icon (synchronous load)
        try {
            java.net.URL iconUrl = getClass().getResource("/Images/Icon2.png");
            if (iconUrl != null) {
                java.awt.Image imgIconSync = ImageIO.read(iconUrl);
                setIconImage(imgIconSync);
            }
        } catch (Exception e) {
            // Ignore if the resource is not found
        }

        // Create a programmatic dark background panel (gradient + subtle vignette)
        backgroundPanel = new javax.swing.JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Vertical gradient (very dark blue -> dark gray)
                Color top = new Color(10, 12, 16);
                Color bottom = new Color(30, 32, 35);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, top, 0, h, bottom);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // Subtle vignette (darken edges)
                float[] dist = {0.0f, 0.7f, 1.0f};
                Color[] cols = {new Color(0,0,0,0), new Color(0,0,0,60), new Color(0,0,0,150)};
                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                        new java.awt.Point(w/2, h/2), Math.max(w, h) * 0.75f, dist, cols);
                g2.setPaint(rgp);
                g2.fillRect(0, 0, w, h);

                g2.dispose();
            }
        };

        backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        getLayeredPane().add(backgroundPanel, Integer.valueOf(Integer.MIN_VALUE));
        backgroundPanel.setVisible(true);
        if (getContentPane() instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) getContentPane()).setOpaque(false);
        }

        // Adjust on resize
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (backgroundPanel != null) {
                    backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
                    backgroundPanel.repaint();
                    getLayeredPane().setPosition(backgroundPanel, Integer.MIN_VALUE);
                }
            }
        });

        this.setLocationRelativeTo(null);
    }

    /** Apply a compact modern dark theme using UI defaults. */
    private void applyModernDarkTheme() {
        try {
            UIManager.put("control", new ColorUIResource(30, 30, 30));
            UIManager.put("Panel.background", new ColorUIResource(30, 30, 30));
            UIManager.put("List.background", new ColorUIResource(18, 18, 18));
            UIManager.put("List.foreground", new ColorUIResource(230, 230, 230));
            UIManager.put("Label.foreground", new ColorUIResource(220, 220, 220));
            UIManager.put("Button.background", new ColorUIResource(50, 50, 50));
            UIManager.put("Button.foreground", new ColorUIResource(240, 240, 240));
            UIManager.put("TextField.background", new ColorUIResource(28, 28, 28));
            UIManager.put("TextField.foreground", new ColorUIResource(230, 230, 230));
            UIManager.put("TextField.caretForeground", new ColorUIResource(200, 200, 200));
            UIManager.put("ScrollPane.background", new ColorUIResource(24, 24, 24));
            UIManager.put("ToolTip.background", new ColorUIResource(60, 60, 60));
            UIManager.put("ToolTip.foreground", new ColorUIResource(240, 240, 240));
            UIManager.put("OptionPane.background", new ColorUIResource(30, 30, 30));
            UIManager.put("OptionPane.messageForeground", new ColorUIResource(230, 230, 230));
        } catch (Exception e) {
            // If anything fails, continue with defaults
        }
    }

    /** Small helper to style buttons with rounded borders and colors. */
    private void styleButton(JButton b, String variant) {
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new RoundedBorder(12));
        b.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, Math.max(11, b.getFont().getSize())));
        b.setForeground(new Color(245, 245, 245));
        switch (variant) {
            case "primary":
                b.setBackground(new Color(38, 166, 91));
                break;
            case "accent":
                b.setBackground(new Color(0, 120, 215));
                break;
            case "danger":
                b.setBackground(new Color(200, 60, 60));
                break;
            default:
                b.setBackground(new Color(70, 70, 70));
                break;
        }
        b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    /** Simple rounded border for modern button visuals. */
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color edge = new Color(60, 60, 60, 200);

        RoundedBorder(int radius) { this.radius = radius; }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(edge);
            RoundRectangle2D.Float r = new RoundRectangle2D.Float(x + 1, y + 1, width - 2, height - 2, radius, radius);
            g2.draw(r);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) { return new Insets(6, 12, 6, 12); }

        @Override
        public Insets getBorderInsets(java.awt.Component c, Insets insets) {
            insets.left = insets.right = 12;
            insets.top = insets.bottom = 6;
            return insets;
        }
    }

    /** Custom renderer to give JLists a modern padded look. */
    private static class ListItemRenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer<Object> {
        private final Color bg = new Color(22, 22, 22);
        private final Color selBg = new Color(0, 120, 215);
        private final Color fg = new Color(230, 230, 230);

        ListItemRenderer() {
            setOpaque(true);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        }

        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(String.valueOf(value));
            if (isSelected) {
                setBackground(selBg);
                setForeground(new Color(255, 255, 255));
            } else {
                setBackground(bg);
                setForeground(fg);
            }
            return this;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // List models
        modeloCancionesSeleccionadas = new DefaultListModel<>();
        
        // Main components
        JLabel lblBiblioteca = new JLabel("Music Library:");
        JLabel lblSeleccionadas = new JLabel("Selected Songs:");
        
        JButton btnExaminar = new JButton("Browse Folder");
        JButton btnAgregarCancion = new JButton(">> Add");
        JButton btnRemoverCancion = new JButton("Remove");
        JButton btnMoverArriba = new JButton("↑ Up");
        JButton btnMoverAbajo = new JButton("↓ Down");
        JButton btnEnviarOrdenada = new JButton("Export to \"Organized\" Folder");
        
        JLabel lblEditarNombre = new JLabel("Edit song name:");
        txtEditarNombre = new JTextField();
        JButton btnActualizarNombre = new JButton("Update Name");
        
        JLabel lblMensaje = new JLabel("");
        
        listaCancionesDisponibles = new JList<>();
        listaCancionesSeleccionadas = new JList<>(modeloCancionesSeleccionadas);
        
        // Configure main window
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MusiCar2 v2.0 - Music Organizer for CD/USB/SD");
        // setIconImage(getIconImage()); // REMOVIDO: Causa problemas en JAR
        setSize(1100, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        // LEFT PANEL: Folder browser and available songs
        lblBiblioteca.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblBiblioteca.setForeground(new java.awt.Color(220, 220, 220));
        getContentPane().add(lblBiblioteca, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 20));
        
        btnExaminar.addActionListener(evt -> btnExaminarActionPerformed(evt));
        styleButton(btnExaminar, "secondary");
        getContentPane().add(btnExaminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 140, 30));
        
        listaCancionesDisponibles.setBackground(new java.awt.Color(22, 22, 22));
        listaCancionesDisponibles.setForeground(new java.awt.Color(230, 230, 230));
        listaCancionesDisponibles.setFont(new java.awt.Font("Segoe UI", 0, 12));
        listaCancionesDisponibles.setBorder(new RoundedBorder(10));
        listaCancionesDisponibles.setCellRenderer(new ListItemRenderer());
        listaCancionesDisponibles.setFixedCellHeight(30);
        listaCancionesDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = listaCancionesDisponibles.locationToIndex(evt.getPoint());
                    if (idx != -1) {
                        String nombreOriginal = listaCancionesDisponibles.getModel().getElementAt(idx);

                        // Avoid duplicates by checking the original name in the selected list
                        boolean existe = false;
                        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
                            String elem = modeloCancionesSeleccionadas.get(i);
                            if (elem.endsWith(nombreOriginal)) { existe = true; break; }
                        }
                        if (existe) {
                            // If it already exists, simply select that element
                            for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
                                if (modeloCancionesSeleccionadas.get(i).endsWith(nombreOriginal)) {
                                    listaCancionesSeleccionadas.setSelectedIndex(i);
                                    txtEditarNombre.setText(nombreOriginal);
                                    break;
                                }
                            }
                            return;
                        }

                        secuencia++;
                        String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
                        modeloCancionesSeleccionadas.addElement(nombreFormato);

                        // Find the file in the map or in the loaded files list
                        File archivo = mapaCancionesArchivos.get(nombreOriginal);
                        if (archivo == null) {
                            for (File f : archivosSeleccionados) {
                                if (f.getName().equals(nombreOriginal)) { archivo = f; break; }
                            }
                        }

                        mapaCancionesArchivos.put(nombreFormato, archivo);
                        listaCancionesSeleccionadas.setSelectedIndex(modeloCancionesSeleccionadas.size() - 1);
                        txtEditarNombre.setText(nombreOriginal);
                    }
                }
            }
        });
        getContentPane().add(listaCancionesDisponibles, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 300, 450));
        
        // CENTER BUTTONS
        btnAgregarCancion.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnAgregarCancion.addActionListener(evt -> btnAgregarCancionActionPerformed(evt));
        styleButton(btnAgregarCancion, "accent");
        getContentPane().add(btnAgregarCancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 80, 30));

        // SHUFFLE BUTTON: Add all songs from the folder in random order
        JButton btnAleatorio = new JButton("Shuffle");
        btnAleatorio.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnAleatorio.addActionListener(evt -> {
            if (archivosSeleccionados == null || archivosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No songs loaded. Use 'Browse Folder' first.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            java.util.List<File> copia = new java.util.ArrayList<>(archivosSeleccionados);
            java.util.Collections.shuffle(copia);

            // Replace the selected list entirely with the new shuffled order
            modeloCancionesSeleccionadas.clear();
            mapaCancionesArchivos.clear();
            secuencia = 0;

            for (File archivo : copia) {
                secuencia++;
                String nombreOriginal = archivo.getName();
                String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
                modeloCancionesSeleccionadas.addElement(nombreFormato);
                mapaCancionesArchivos.put(nombreFormato, archivo);
            }

            // Select the first element and load its name into the editor if present
            if (modeloCancionesSeleccionadas.size() > 0) {
                listaCancionesSeleccionadas.setSelectedIndex(0);
                String primero = modeloCancionesSeleccionadas.get(0);
                txtEditarNombre.setText(primero.substring(primero.indexOf("-") + 2));
            }
        });
        styleButton(btnAleatorio, "accent");
        getContentPane().add(btnAleatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 80, 30));
        
        // RIGHT PANEL: Selected songs
        lblSeleccionadas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblSeleccionadas.setForeground(new java.awt.Color(220, 220, 220));
        getContentPane().add(lblSeleccionadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 300, 20));
        listaCancionesSeleccionadas.setBackground(new java.awt.Color(22, 22, 22));
        listaCancionesSeleccionadas.setForeground(new java.awt.Color(230, 230, 230));
        listaCancionesSeleccionadas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        listaCancionesSeleccionadas.setBorder(new RoundedBorder(10));
        listaCancionesSeleccionadas.setCellRenderer(new ListItemRenderer());
        listaCancionesSeleccionadas.setFixedCellHeight(30);
        listaCancionesSeleccionadas.addListSelectionListener(evt -> listaSeleccionadaActionPerformed(evt));
        getContentPane().add(listaCancionesSeleccionadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 35, 350, 320));
        
        // Order buttons
        btnMoverArriba.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnMoverArriba.addActionListener(evt -> btnMoverArribaActionPerformed(evt));
        styleButton(btnMoverArriba, "secondary");
        getContentPane().add(btnMoverArriba, new org.netbeans.lib.awtextra.AbsoluteConstraints(765, 35, 80, 30));
        
        btnMoverAbajo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnMoverAbajo.addActionListener(evt -> btnMoverAbajoActionPerformed(evt));
        styleButton(btnMoverAbajo, "secondary");
        getContentPane().add(btnMoverAbajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 35, 80, 30));
        
        btnRemoverCancion.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnRemoverCancion.addActionListener(evt -> btnRemoverCancionActionPerformed(evt));
        styleButton(btnRemoverCancion, "danger");
        getContentPane().add(btnRemoverCancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(935, 35, 80, 30));
        
        // Editor de nombre
        lblEditarNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblEditarNombre.setForeground(new java.awt.Color(220, 220, 220));
        getContentPane().add(lblEditarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 360, 250, 20));
        
        txtEditarNombre.setFont(new java.awt.Font("Segoe UI", 0, 12));
        txtEditarNombre.setBackground(new java.awt.Color(28, 28, 28));
        txtEditarNombre.setForeground(new java.awt.Color(230, 230, 230));
        txtEditarNombre.setBorder(new RoundedBorder(8));
        getContentPane().add(txtEditarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 385, 510, 30));
        
        btnActualizarNombre.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnActualizarNombre.addActionListener(evt -> btnActualizarNombreActionPerformed(evt));
        styleButton(btnActualizarNombre, "accent");
        getContentPane().add(btnActualizarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 385, 90, 30));
        
        // Primary button: Export to Ordered folder
        btnEnviarOrdenada.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        btnEnviarOrdenada.addActionListener(evt -> btnEnviarOrdenada_ActionPerformed(evt));
        styleButton(btnEnviarOrdenada, "primary");
        getContentPane().add(btnEnviarOrdenada, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 425, 510, 50));
        
        // Mensaje de estado
        lblMensaje.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        lblMensaje.setForeground(java.awt.Color.WHITE);
        getContentPane().add(lblMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 485, 510, 80));
        
        // MENÚ REMOVIDO: No se necesita para la funcionalidad básica
        // El menú "Archivo" y "Mas..." han sido eliminados para simplificar la interfaz
        // y evitar dependencias externas como nircmd.exe
        
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * IMPORTANT: Opens a dialog to browse the user's music library.
     * The user chooses a folder and the available files are loaded.
     */
    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser seleccionador = new JFileChooser();
        seleccionador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        seleccionador.setApproveButtonText("Browse");
        seleccionador.setDialogTitle("Select your music folder");

        // Ensure the file chooser uses a light background so file/folder names are readable
        try {
            seleccionador.setBackground(java.awt.Color.WHITE);
            seleccionador.setForeground(java.awt.Color.BLACK);
            fixFileChooserColors(seleccionador);
        } catch (Exception e) {
            // ignore if look-and-feel prevents direct changes
        }

        int resultado = seleccionador.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File carpetaSeleccionada = seleccionador.getSelectedFile();
            cargarCancionesDisponibles(carpetaSeleccionada);
        }
    }

    /**
     * IMPORTANT: Loads the list of available songs from a folder.
     * Only supported audio files are shown.
     */
    private void cargarCancionesDisponibles(File carpeta) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        archivosSeleccionados.clear(); // Limpiar lista anterior
        mapaCancionesArchivos.clear(); // Limpiar mapa
        
        File[] archivos = carpeta.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".mp3") || lower.endsWith(".wma") || 
                   lower.endsWith(".wav") || lower.endsWith(".flac") || 
                   lower.endsWith(".m4a");
        });

        if (archivos != null && archivos.length > 0) {
            for (File archivo : archivos) {
                String nombreOriginal = archivo.getName();
                modelo.addElement(nombreOriginal);
                archivosSeleccionados.add(archivo);
                // IMPORTANT: Save mapping name -> file
                mapaCancionesArchivos.put(nombreOriginal, archivo);
            }
            listaCancionesDisponibles.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No music files found in this folder.", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * IMPORTANT: Adds the selected song to the ordered playlist.
     * The song is appended while maintaining sequential numbering.
     */
    private void btnAgregarCancionActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesDisponibles.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Select a song first", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        secuencia++;
        String nombreOriginal = listaCancionesDisponibles.getSelectedValue();
        String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
        
        modeloCancionesSeleccionadas.addElement(nombreFormato);
        
        // IMPORTANT: Save mapping of the formatted song name to the original file
        mapaCancionesArchivos.put(nombreFormato, mapaCancionesArchivos.get(nombreOriginal));
        
        // Cargar el nombre en el editor
        txtEditarNombre.setText(nombreOriginal);
    }

    /**
     * IMPORTANT: Removes the selected song from the ordered list.
     */
    private void btnRemoverCancionActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Select a song to remove", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        secuencia--;
        reordenarSecuencia();
    }

    private void btnMoverArribaActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        if (indiceSeleccionado <= 0) {
            return; // No se puede mover el primer elemento arriba
        }

        String elemento = modeloCancionesSeleccionadas.get(indiceSeleccionado);
        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        modeloCancionesSeleccionadas.add(indiceSeleccionado - 1, elemento);

        listaCancionesSeleccionadas.setSelectedIndex(indiceSeleccionado - 1);
        reordenarSecuencia();
    }

    private void btnMoverAbajoActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        if (indiceSeleccionado == -1 || indiceSeleccionado >= modeloCancionesSeleccionadas.size() - 1) {
            return; // No se puede mover el último elemento abajo
        }

        String elemento = modeloCancionesSeleccionadas.get(indiceSeleccionado);
        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        modeloCancionesSeleccionadas.add(indiceSeleccionado + 1, elemento);

        listaCancionesSeleccionadas.setSelectedIndex(indiceSeleccionado + 1);
        reordenarSecuencia();
    }

    /**
     * IMPORTANT: Reorders the sequence numbers when the list is modified.
     */
    private void reordenarSecuencia() {
        java.util.Map<String, File> mapaTemporal = new java.util.HashMap<>();
        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
            String elementoAntiguo = modeloCancionesSeleccionadas.get(i);
            String nombreSinNumero = elementoAntiguo.substring(elementoAntiguo.indexOf("-") + 2);
            String nuevoElemento = String.format("%03d - %s", i + 1, nombreSinNumero);
            modeloCancionesSeleccionadas.set(i, nuevoElemento);

            File archivo = mapaCancionesArchivos.remove(elementoAntiguo);
            mapaTemporal.put(nuevoElemento, archivo);
        }

        mapaCancionesArchivos.clear();
        mapaCancionesArchivos.putAll(mapaTemporal);
    }

    /**
     * IMPORTANT: Loads the song name into the editor when an item is selected.
     */
    private void listaSeleccionadaActionPerformed(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) return;
        
        String seleccionado = listaCancionesSeleccionadas.getSelectedValue();
        if (seleccionado != null) {
            nombreSeleccionadoActual = seleccionado; // Guardar para actualizar mapa
            // Extrae el nombre sin el número
            String nombre = seleccionado.substring(seleccionado.indexOf("-") + 2);
            txtEditarNombre.setText(nombre);
        }
    }

    /**
     * IMPORTANT: Updates the song name in the list.
     */
    private void btnActualizarNombreActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Select a song to edit", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoNombre = txtEditarNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Name cannot be empty", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String elementoActualizado = String.format("%03d - %s", indiceSeleccionado + 1, nuevoNombre);
        modeloCancionesSeleccionadas.set(indiceSeleccionado, elementoActualizado);
        
        // IMPORTANT: Update the map to point to the new name
        File archivo = mapaCancionesArchivos.remove(nombreSeleccionadoActual);
        mapaCancionesArchivos.put(elementoActualizado, archivo);
    }

    /**
     * IMPORTANT: Exports all songs to the "Ordered" folder in exact order.
     * Creates the folder if it does not exist and copies the renamed files.
     */
    private void btnEnviarOrdenada_ActionPerformed(java.awt.event.ActionEvent evt) {
        if (modeloCancionesSeleccionadas.size() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Add at least one song first", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create the "Ordered" folder inside the user's Music directory
        String rutaDocumentos = System.getProperty("user.home") + File.separator + "Music";
        File carpetaOrdenada = new File(rutaDocumentos + File.separator + "Organized");

        // IMPORTANT: If the folder already exists, delete it completely
        if (carpetaOrdenada.exists()) {
            try {
                java.nio.file.Files.walk(java.nio.file.Paths.get(carpetaOrdenada.getAbsolutePath()))
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(java.nio.file.Path::toFile)
                    .forEach(java.io.File::delete);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting previous folder: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!carpetaOrdenada.mkdirs()) {
            JOptionPane.showMessageDialog(this, 
                "Could not create the 'Organized' folder", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder mensajeResultado = new StringBuilder();
        int copiadasExitosas = 0;
        
        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
            String nombreEnLista = modeloCancionesSeleccionadas.get(i);
            
            // Obtener el archivo original desde el mapa
            File archivoOriginal = mapaCancionesArchivos.get(nombreEnLista);
            
            if (archivoOriginal != null && archivoOriginal.exists()) {
                try {
                    // IMPORTANT: Always preserve the file's original extension
                    String extension = archivoOriginal.getName().substring(
                            archivoOriginal.getName().lastIndexOf("."));
                    
                    // Extraer el nombre editado sin el número
                    String nombreSinNumero = nombreEnLista.substring(nombreEnLista.indexOf("-") + 2);
                    
                    // IMPORTANT: If the edited name ends with the extension, strip it to avoid duplicates
                    String nombreBase = nombreSinNumero;
                    if (nombreBase.toLowerCase().endsWith(extension.toLowerCase())) {
                        nombreBase = nombreBase.substring(0, nombreBase.length() - extension.length());
                    }
                    
                    // Crear nombre final: número - nombre base + extensión original
                    String nombreNuevo = String.format("%03d - %s%s", i + 1, nombreBase, extension);
                    File archivoDestino = new File(carpetaOrdenada, nombreNuevo);
                    
                    // Copiar archivo
                    copiarArchivo(archivoOriginal, archivoDestino);
                    copiadasExitosas++;
                    mensajeResultado.append(String.format("%d. %s ✓\n", i + 1, nombreNuevo));
                    
                } catch (IOException e) {
                    mensajeResultado.append(String.format("%d. Error: %s\n", i + 1, e.getMessage()));
                }
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("════════════════════════════════════\n");
        respuesta.append("     SONGS SUCCESSFULLY ORGANIZED\n");
        respuesta.append("════════════════════════════════════\n\n");
        respuesta.append(mensajeResultado);
        respuesta.append("\n════════════════════════════════════\n");
        respuesta.append("✓ Songs copied: ").append(copiadasExitosas).append(" of ").append(modeloCancionesSeleccionadas.size()).append("\n");
        respuesta.append("Location: ").append(carpetaOrdenada.getAbsolutePath()).append("\n");
        respuesta.append("════════════════════════════════════\n\n");
        respuesta.append("You can now copy this folder to your USB or SD.\n");
        respuesta.append("Songs will play in this exact order.");

        JOptionPane.showMessageDialog(this, 
            respuesta.toString(), 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * IMPORTANT: Copy a source file to a destination using pure Java streams.
     * Does NOT use external tools like nircmd.exe - uses FileInputStream
     * and FileOutputStream for maximum compatibility and portability.
     *
     * @param origen - source file to copy
     * @param destino - destination file
     * @throws IOException if an error occurs during the copy
     */
    private void copiarArchivo(File origen, File destino) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(destino)) {
            byte[] buffer = new byte[8192]; // 8KB buffer for efficient copying
            int bytesLeidos;
            
            try (FileInputStream fis = new FileInputStream(origen)) {
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesLeidos);
                }
            }
        }
    }

    /** Recursively set light colors on JFileChooser components for readability. */
    private void fixFileChooserColors(java.awt.Component comp) {
        if (comp == null) return;

        if (comp instanceof javax.swing.JList) {
            javax.swing.JList<?> list = (javax.swing.JList<?>) comp;
            list.setBackground(java.awt.Color.WHITE);
            list.setForeground(java.awt.Color.BLACK);
        } else if (comp instanceof javax.swing.JTable) {
            javax.swing.JTable table = (javax.swing.JTable) comp;
            table.setBackground(java.awt.Color.WHITE);
            table.setForeground(java.awt.Color.BLACK);
            if (table.getTableHeader() != null) {
                table.getTableHeader().setBackground(new java.awt.Color(240,240,240));
                table.getTableHeader().setForeground(java.awt.Color.BLACK);
            }
        } else if (comp instanceof javax.swing.JTree) {
            javax.swing.JTree tree = (javax.swing.JTree) comp;
            tree.setBackground(java.awt.Color.WHITE);
            tree.setForeground(java.awt.Color.BLACK);
        } else if (comp instanceof javax.swing.JTextField) {
            javax.swing.JTextField tf = (javax.swing.JTextField) comp;
            tf.setBackground(java.awt.Color.WHITE);
            tf.setForeground(java.awt.Color.BLACK);
            tf.setCaretColor(java.awt.Color.BLACK);
        } else if (comp instanceof javax.swing.JComponent) {
            javax.swing.JComponent jc = (javax.swing.JComponent) comp;
            jc.setBackground(java.awt.Color.WHITE);
            jc.setForeground(java.awt.Color.BLACK);
        }

        if (comp instanceof java.awt.Container) {
            for (java.awt.Component child : ((java.awt.Container) comp).getComponents()) {
                fixFileChooserColors(child);
            }
        }
    }


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
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
            
            
            
            
        });
    }

/**
 * IMPORTANT: Pad a number with leading zeros for formatting.
 * Example: agregarCeros("5", 3) returns "005"
 * Used to create sequentially ordered filenames.
 *
 * @param numero - number string to format
 * @param longitudDeseada - total desired length
 * @return number string padded with leading zeros
 */
private static String agregarCeros(String numero, int longitudDeseada) {
    int cerosAgregar = longitudDeseada - numero.length();
    if (cerosAgregar > 0) {
        return "0".repeat(cerosAgregar) + numero;
    }
    return numero;
}

    /**
     * IMPORTANT: Replace accented characters with their non-accented equivalents.
     * Useful to avoid issues on systems that do not support accented characters.
     * Example: acentos("Música") returns "Musica"
     *
     * @param texto - input text that may contain accents
     * @return text without accents
     */
    private String acentos(String texto) {
        if (texto == null) {
            return null;
        }
        
        // Mapeo de caracteres acentuados y sus reemplazos
        final String ACENTUADOS = "ÁáÉéÍíÓóÚúÑñÜü";
        final String SIN_ACENTOS = "AaEeIiOoUuNnUu";
        
        char[] arrayCaracteres = texto.toCharArray();
        for (int i = 0; i < arrayCaracteres.length; i++) {
            int posicion = ACENTUADOS.indexOf(arrayCaracteres[i]);
            if (posicion > -1) {
                arrayCaracteres[i] = SIN_ACENTOS.charAt(posicion);
            }
        }
        return new String(arrayCaracteres);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JList<String> listaCancionesDisponibles;
    private JList<String> listaCancionesSeleccionadas;
    private JTextField txtEditarNombre;
    // End of variables declaration//GEN-END:variables
}
