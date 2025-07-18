package main.views.Usuario;

import main.models.Usuario;
import main.views.Components.BotonAzul;
import main.views.Components.RoundedBorder;
import main.views.Components.RoundedPanel;
import main.views.Components.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardUserView extends JFrame {
    private RoundedPanel formPanel;
    private JLabel dateLabel, profileIcon, credencialInfoLabel, saldoLabel;
    private BotonAzul consultarMenuBtn, cerrarSesionBtn;

    public DashboardUserView(Usuario usuario) {
        setTitle("Panel del Estudiante");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(36, 136, 242));

        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(dateLabel);

        profileIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(169, 169, 169));
                g2.fillOval(0, 0, 100, 100);
                g2.setColor(Color.WHITE);
                g2.fillOval(30, 20, 40, 40);
                g2.fillOval(15, 60, 70, 50);
                g2.dispose();
            }
        };
        formPanel.add(profileIcon);

        consultarMenuBtn = UIFactory.crearBotonAzul("Consultar Menú", new Dimension(200, 40));
        formPanel.add(consultarMenuBtn);

        cerrarSesionBtn = UIFactory.crearBotonAzul("Cerrar Sesión", new Dimension(150, 35));
        formPanel.add(cerrarSesionBtn);

        credencialInfoLabel = new JLabel("Credencial: " + usuario.getCredencial());
        credencialInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(credencialInfoLabel);

        saldoLabel = new JLabel(String.format("Saldo: %.2f$", usuario.getSaldo()));
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        saldoLabel.setForeground(new Color(0, 153, 255));
        formPanel.add(saldoLabel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                updateLayout();
            }
        });

        updateLayout();
    }

    private void updateLayout() {
        int topMargin = 40;
        int bottomMargin = 60;
        int sideMargin = 40;

        formPanel.setBounds(sideMargin, topMargin, getWidth() - 2 * sideMargin, getHeight() - topMargin - bottomMargin);

        int formWidth = formPanel.getWidth();
        int formHeight = formPanel.getHeight();

        saldoLabel.setBounds(formWidth - 220, 45, 200, 20);

        dateLabel.setBounds(20, 20, 150, 20);

        int profileX = (formWidth / 2 - 100) / 2;
        int profileY = (formHeight - 100) / 2 - 30;
        profileIcon.setBounds(profileX, profileY, 100, 100);
        credencialInfoLabel.setBounds(profileX, profileY + 105, 200, 20);


        consultarMenuBtn.setBounds(formWidth / 2 + 50, profileY + 50, 200, 40);
        cerrarSesionBtn.setBounds(formWidth - 170, formHeight - 50, 150, 35);
    }

    public JButton getConsultarMenuBtn() { return consultarMenuBtn; }
    public JButton getCerrarSesionBtn() { return cerrarSesionBtn; }

    public void updateDate(LocalDateTime fecha) {
        dateLabel.setText(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void actualizarSaldo(double saldo) {
        saldoLabel.setText(String.format("Saldo: %.2f$", saldo));
    }
}